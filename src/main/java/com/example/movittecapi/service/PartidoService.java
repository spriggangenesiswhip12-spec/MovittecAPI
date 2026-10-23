package com.example.movittecapi.service;

import com.example.movittecapi.dto.EventoPartidoDTO;
import com.example.movittecapi.dto.EventoRequest;
import com.example.movittecapi.dto.PartidoDTO;
import com.example.movittecapi.entity.Equipo;
import com.example.movittecapi.entity.EventoPartido;
import com.example.movittecapi.entity.Partido;
import com.example.movittecapi.entity.Usuario;
import com.example.movittecapi.repository.EquipoRepository;
import com.example.movittecapi.repository.EventoPartidoRepository;
import com.example.movittecapi.repository.PartidoRepository;
import com.example.movittecapi.repository.UsuarioRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class PartidoService {

    private static final Set<String> ESTADOS = Set.of("programado", "en_curso", "finalizado");
    private static final Set<String> TIPOS = Set.of("gol", "tarjeta_amarilla", "tarjeta_roja", "sustitucion");

    private final PartidoRepository partidos;
    private final EquipoRepository equipos;
    private final EventoPartidoRepository eventos;
    private final UsuarioRepository usuarios;

    public PartidoService(PartidoRepository partidos,
                          EquipoRepository equipos,
                          EventoPartidoRepository eventos,
                          UsuarioRepository usuarios) {
        this.partidos = partidos;
        this.equipos = equipos;
        this.eventos = eventos;
        this.usuarios = usuarios;
    }

    // ---------------------------------------------------------------- consultas

    public List<PartidoDTO> listar(Integer categoriaId, String estado) {
        List<Partido> lista = (categoriaId == null)
                ? partidos.findAllByOrderByFechaAscHoraAsc()
                : partidos.findByCategoriaIdOrderByFechaAscHoraAsc(categoriaId);

        if (estado != null && !estado.isBlank()) {
            lista = lista.stream()
                    .filter(p -> estado.equalsIgnoreCase(p.getEstado()))
                    .toList();
        }
        return aDTO(lista);
    }

    public Optional<PartidoDTO> obtener(Integer id) {
        return partidos.findById(id).map(p -> aDTO(List.of(p)).get(0));
    }

    public List<EventoPartidoDTO> eventosDe(Integer partidoId) {
        return eventosADTO(eventos.findByPartidoIdOrderByMinutoAscIdEventoAsc(partidoId));
    }

    // ---------------------------------------------------------------- acciones de árbitro

    @Transactional
    public PartidoDTO cambiarEstado(Integer id, String estado) {
        Partido p = buscar(id);
        String nuevo = estado == null ? "" : estado.trim().toLowerCase();
        if (!ESTADOS.contains(nuevo)) {
            throw error(400, "Estado no válido (programado, en_curso o finalizado)");
        }
        p.setEstado(nuevo);
        partidos.save(p);
        return aDTO(List.of(p)).get(0);
    }

    @Transactional
    public PartidoDTO fijarMarcador(Integer id, Integer local, Integer visitante) {
        Partido p = buscar(id);
        if (local == null || visitante == null || local < 0 || visitante < 0 || local > 99 || visitante > 99) {
            throw error(400, "Marcador no válido");
        }
        p.setMarcadorLocal(local);
        p.setMarcadorVisitante(visitante);
        partidos.save(p);
        return aDTO(List.of(p)).get(0);
    }

    @Transactional
    public EventoPartidoDTO registrarEvento(Integer partidoId, EventoRequest r) {
        Partido p = buscar(partidoId);

        if ("finalizado".equals(p.getEstado())) {
            throw error(409, "El partido ya finalizó. Reábrelo (en curso) para editar eventos.");
        }

        String tipo = r.tipo() == null ? "" : r.tipo().trim().toLowerCase();
        if (!TIPOS.contains(tipo)) {
            throw error(400, "Tipo de evento no válido");
        }
        if (r.minuto() == null || r.minuto() < 0 || r.minuto() > 130) {
            throw error(400, "Minuto no válido");
        }
        if (r.jugadorId() == null || !usuarios.existsById(r.jugadorId())) {
            throw error(400, "Jugador no encontrado");
        }

        Integer equipoId = r.equipoId();

        if (tipo.equals("gol")) {
            boolean esLocal = equipoId != null && equipoId.equals(p.getEquipoLocalId());
            boolean esVisitante = equipoId != null && equipoId.equals(p.getEquipoVisitanteId());
            if (!esLocal && !esVisitante) {
                throw error(400, "El gol debe asignarse al equipo local o al visitante");
            }
            if (r.jugadorAsistenciaId() != null) {
                if (r.jugadorAsistenciaId().equals(r.jugadorId())) {
                    throw error(400, "Quien asiste no puede ser quien anota");
                }
                if (!usuarios.existsById(r.jugadorAsistenciaId())) {
                    throw error(400, "Jugador de asistencia no encontrado");
                }
            }
            if (esLocal) {
                p.setMarcadorLocal(valor(p.getMarcadorLocal()) + 1);
            } else {
                p.setMarcadorVisitante(valor(p.getMarcadorVisitante()) + 1);
            }
        }

        // el primer evento arranca el partido automáticamente
        if (p.getEstado() == null || "programado".equals(p.getEstado())) {
            p.setEstado("en_curso");
        }

        EventoPartido e = new EventoPartido();
        e.setPartidoId(partidoId);
        e.setTipo(tipo);
        e.setJugadorId(r.jugadorId());
        e.setJugadorAsistenciaId(tipo.equals("gol") ? r.jugadorAsistenciaId() : null);
        e.setEquipoId(equipoId);
        e.setMinuto(r.minuto());
        e.setDescripcion(r.descripcion());

        partidos.save(p);
        EventoPartido guardado = eventos.save(e);

        return eventosADTO(List.of(guardado)).get(0);
    }

    @Transactional
    public void eliminarEvento(Integer partidoId, Integer idEvento) {
        Partido p = buscar(partidoId);

        EventoPartido e = eventos.findById(idEvento)
                .filter(ev -> partidoId.equals(ev.getPartidoId()))
                .orElseThrow(() -> error(404, "Evento no encontrado"));

        // si era un gol, se le resta al marcador del equipo correspondiente
        if ("gol".equals(e.getTipo()) && e.getEquipoId() != null) {
            if (e.getEquipoId().equals(p.getEquipoLocalId())) {
                p.setMarcadorLocal(Math.max(0, valor(p.getMarcadorLocal()) - 1));
            } else if (e.getEquipoId().equals(p.getEquipoVisitanteId())) {
                p.setMarcadorVisitante(Math.max(0, valor(p.getMarcadorVisitante()) - 1));
            }
            partidos.save(p);
        }

        eventos.delete(e);
    }

    // ---------------------------------------------------------------- utilidades

    private Partido buscar(Integer id) {
        return partidos.findById(id).orElseThrow(() -> error(404, "Partido no encontrado"));
    }

    private static int valor(Integer n) {
        return n == null ? 0 : n;
    }

    private static ResponseStatusException error(int codigo, String mensaje) {
        return new ResponseStatusException(HttpStatusCode.valueOf(codigo), mensaje);
    }

    private List<PartidoDTO> aDTO(List<Partido> lista) {
        Set<Integer> ids = new HashSet<>();
        for (Partido p : lista) {
            ids.add(p.getEquipoLocalId());
            ids.add(p.getEquipoVisitanteId());
        }

        Map<Integer, String> nombres = new HashMap<>();
        for (Equipo e : equipos.findAllById(ids)) {
            nombres.put(e.getId(), e.getNombre());
        }

        return lista.stream()
                .map(p -> new PartidoDTO(
                        p.getId(),
                        p.getCategoriaId(),
                        p.getEquipoLocalId(),
                        nombres.get(p.getEquipoLocalId()),
                        p.getEquipoVisitanteId(),
                        nombres.get(p.getEquipoVisitanteId()),
                        p.getFecha(),
                        p.getHora(),
                        p.getLugar(),
                        valor(p.getMarcadorLocal()),
                        valor(p.getMarcadorVisitante()),
                        p.getEstado()))
                .toList();
    }

    private List<EventoPartidoDTO> eventosADTO(List<EventoPartido> lista) {
        Set<Integer> ids = new HashSet<>();
        for (EventoPartido e : lista) {
            if (e.getJugadorId() != null) ids.add(e.getJugadorId());
            if (e.getJugadorAsistenciaId() != null) ids.add(e.getJugadorAsistenciaId());
        }

        Map<Integer, String> nombres = new HashMap<>();
        for (Usuario u : usuarios.findAllById(ids)) {
            nombres.put(u.getIdUsuario(), (u.getNombre() + " " + u.getApellidoPaterno()).trim());
        }

        return lista.stream()
                .map(e -> new EventoPartidoDTO(
                        e.getIdEvento(),
                        e.getPartidoId(),
                        e.getTipo(),
                        e.getMinuto(),
                        e.getEquipoId(),
                        e.getJugadorId(),
                        nombres.get(e.getJugadorId()),
                        e.getJugadorAsistenciaId(),
                        e.getJugadorAsistenciaId() == null ? null : nombres.get(e.getJugadorAsistenciaId()),
                        e.getDescripcion()))
                .toList();
    }
}
