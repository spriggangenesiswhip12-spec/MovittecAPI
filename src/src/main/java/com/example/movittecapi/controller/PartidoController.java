package com.example.movittecapi.controller;

import com.example.movittecapi.dto.PartidoResponse;
import com.example.movittecapi.dto.PosicionResponse;
import com.example.movittecapi.entity.Categoria;
import com.example.movittecapi.entity.Equipo;
import com.example.movittecapi.entity.Partido;
import com.example.movittecapi.repository.CategoriaRepository;
import com.example.movittecapi.repository.EquipoRepository;
import com.example.movittecapi.repository.PartidoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class PartidoController {

    private static final String FINALIZADO = "finalizado";

    private final PartidoRepository partidoRepository;
    private final EquipoRepository equipoRepository;
    private final CategoriaRepository categoriaRepository;

    public PartidoController(PartidoRepository partidoRepository,
                             EquipoRepository equipoRepository,
                             CategoriaRepository categoriaRepository) {
        this.partidoRepository = partidoRepository;
        this.equipoRepository = equipoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // GET /partidos?categoriaId=&estado=&equipoId=   (todos los filtros son opcionales)
    @GetMapping("/partidos")
    public List<PartidoResponse> listar(@RequestParam(required = false) Integer categoriaId,
                                        @RequestParam(required = false) String estado,
                                        @RequestParam(required = false) Integer equipoId) {

        List<Partido> partidos = (categoriaId == null)
                ? partidoRepository.findAllByOrderByFechaAscHoraAsc()
                : partidoRepository.findByCategoriaIdOrderByFechaAscHoraAsc(categoriaId);

        Map<Integer, Equipo> equipos = equiposPorId();
        Map<Integer, String> categorias = nombresCategorias();

        return partidos.stream()
                .filter(p -> estado == null || estado.isBlank() || estado.equalsIgnoreCase(p.getEstado()))
                .filter(p -> equipoId == null
                        || equipoId.equals(p.getEquipoLocalId())
                        || equipoId.equals(p.getEquipoVisitanteId()))
                .map(p -> aRespuesta(p, equipos, categorias))
                .toList();
    }

    // GET /posiciones?categoriaId=3  -> tabla general (3 pts victoria, 1 empate)
    // Solo cuentan los partidos con estado "finalizado".
    @GetMapping("/posiciones")
    public List<PosicionResponse> posiciones(@RequestParam(required = false) Integer categoriaId) {

        List<Equipo> equipos = (categoriaId == null)
                ? equipoRepository.findAllByOrderByNombreAsc()
                : equipoRepository.findByCategoriaIdOrderByNombreAsc(categoriaId);

        Map<Integer, int[]> tabla = new LinkedHashMap<>(); // [pj, g, e, p, gf, gc]
        for (Equipo e : equipos) {
            tabla.put(e.getId(), new int[6]);
        }

        List<Partido> partidos = (categoriaId == null)
                ? partidoRepository.findAllByOrderByFechaAscHoraAsc()
                : partidoRepository.findByCategoriaIdOrderByFechaAscHoraAsc(categoriaId);

        for (Partido p : partidos) {
            if (!FINALIZADO.equalsIgnoreCase(p.getEstado())) continue;

            int[] local = tabla.get(p.getEquipoLocalId());
            int[] visita = tabla.get(p.getEquipoVisitanteId());
            if (local == null || visita == null) continue;

            int gl = p.getMarcadorLocal() == null ? 0 : p.getMarcadorLocal();
            int gv = p.getMarcadorVisitante() == null ? 0 : p.getMarcadorVisitante();

            local[0]++;  visita[0]++;
            local[4] += gl; local[5] += gv;
            visita[4] += gv; visita[5] += gl;

            if (gl > gv)      { local[1]++;  visita[3]++; }
            else if (gl < gv) { visita[1]++; local[3]++;  }
            else              { local[2]++;  visita[2]++; }
        }

        List<PosicionResponse> resultado = new ArrayList<>();
        for (Equipo e : equipos) {
            int[] t = tabla.get(e.getId());
            resultado.add(new PosicionResponse(
                    e.getId(), e.getNombre(), e.getLogoUrl(),
                    t[0], t[1], t[2], t[3], t[4], t[5],
                    t[4] - t[5],
                    t[1] * 3 + t[2]
            ));
        }

        resultado.sort(Comparator
                .comparingInt(PosicionResponse::pts).reversed()
                .thenComparing(Comparator.comparingInt(PosicionResponse::dg).reversed())
                .thenComparing(Comparator.comparingInt(PosicionResponse::gf).reversed())
                .thenComparing(PosicionResponse::equipo, Comparator.nullsLast(String::compareToIgnoreCase)));

        return resultado;
    }

    private Map<Integer, Equipo> equiposPorId() {
        Map<Integer, Equipo> mapa = new HashMap<>();
        for (Equipo e : equipoRepository.findAll()) {
            mapa.put(e.getId(), e);
        }
        return mapa;
    }

    private Map<Integer, String> nombresCategorias() {
        Map<Integer, String> mapa = new HashMap<>();
        for (Categoria c : categoriaRepository.findAll()) {
            mapa.put(c.getIdCategoria(), c.getNombre());
        }
        return mapa;
    }

    private PartidoResponse aRespuesta(Partido p, Map<Integer, Equipo> equipos, Map<Integer, String> categorias) {
        Equipo local = equipos.get(p.getEquipoLocalId());
        Equipo visita = equipos.get(p.getEquipoVisitanteId());

        return new PartidoResponse(
                p.getId(),
                p.getCategoriaId(),
                categorias.get(p.getCategoriaId()),
                p.getEquipoLocalId(),
                local == null ? null : local.getNombre(),
                local == null ? null : local.getLogoUrl(),
                p.getEquipoVisitanteId(),
                visita == null ? null : visita.getNombre(),
                visita == null ? null : visita.getLogoUrl(),
                p.getFecha(),
                p.getHora(),
                p.getLugar(),
                p.getMarcadorLocal(),
                p.getMarcadorVisitante(),
                p.getEstado()
        );
    }
}
