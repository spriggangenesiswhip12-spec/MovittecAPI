package com.example.movittecapi.controller;

import com.example.movittecapi.dto.AgregarEquipoRequest;
import com.example.movittecapi.dto.EquipoJugadorDTO;
import com.example.movittecapi.dto.EstadoUsuarioDTO;
import com.example.movittecapi.entity.Equipo;
import com.example.movittecapi.entity.JugadorEquipo;
import com.example.movittecapi.entity.Usuario;
import com.example.movittecapi.repository.EquipoRepository;
import com.example.movittecapi.repository.JugadorEquipoRepository;
import com.example.movittecapi.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * La app consulta esto cada cierto tiempo para saber si el admin desactivó al usuario
 * (y para refrescar sus equipos). Un usuario desactivado recibe 403.
 */
@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class EstadoUsuarioController {

    private final UsuarioRepository usuarios;
    private final JugadorEquipoRepository jugadorEquipos;
    private final EquipoRepository equipos;

    public EstadoUsuarioController(UsuarioRepository usuarios,
                                   JugadorEquipoRepository jugadorEquipos,
                                   EquipoRepository equipos) {
        this.usuarios = usuarios;
        this.jugadorEquipos = jugadorEquipos;
        this.equipos = equipos;
    }

    private static ResponseEntity<?> mensaje(int codigo, String texto) {
        return ResponseEntity.status(codigo).body(Map.of("mensaje", texto));
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<?> estado(@PathVariable Integer id) {
        Optional<Usuario> opt = usuarios.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("mensaje", "Usuario no encontrado"));
        }

        boolean activo = Boolean.TRUE.equals(opt.get().getActivo());
        if (!activo) {
            return ResponseEntity.status(403).body(Map.of("mensaje", "Usuario desactivado"));
        }

        return ResponseEntity.ok(new EstadoUsuarioDTO(true,
                EquipoJugadorDTO.de(jugadorEquipos.equiposDe(id))));
    }

    /**
     * POST /usuarios/{id}/equipos  {contrasena, equipoId, dorsal}
     * El jugador se agrega a otro equipo. Reglas: una sola categoría por equipo
     * (no dos equipos en la misma categoría) y dorsal libre dentro del equipo.
     */
    @PostMapping("/{id}/equipos")
    public ResponseEntity<?> agregarEquipo(@PathVariable Integer id, @RequestBody AgregarEquipoRequest r) {
        Usuario u = usuarios.findById(id).orElse(null);
        if (u == null) {
            return mensaje(404, "Usuario no encontrado");
        }
        if (!Boolean.TRUE.equals(u.getActivo())) {
            return mensaje(403, "Usuario desactivado");
        }
        if (r.contrasena() == null || u.getContrasena() == null || !u.getContrasena().equals(r.contrasena())) {
            return mensaje(401, "Contraseña incorrecta");
        }
        if (u.getTipoUsuario() == null || !u.getTipoUsuario().equalsIgnoreCase("usuario")) {
            return mensaje(403, "Solo los jugadores pueden agregar equipos");
        }
        if (r.equipoId() == null || r.dorsal() == null) {
            return mensaje(400, "Elige el equipo y tu dorsal");
        }
        if (r.dorsal() < 0 || r.dorsal() > 99) {
            return mensaje(400, "El dorsal debe estar entre 0 y 99");
        }

        Equipo equipo = equipos.findById(r.equipoId()).orElse(null);
        if (equipo == null || equipo.getCategoriaId() == null) {
            return mensaje(400, "El equipo no existe");
        }
        if (jugadorEquipos.existsByIdUsuarioAndCategoriaId(id, equipo.getCategoriaId())) {
            return mensaje(400, "Ya tienes un equipo en esa categoría");
        }
        if (jugadorEquipos.existsByEquipoIdAndDorsal(equipo.getId(), r.dorsal())) {
            return mensaje(400, "Ese dorsal ya está ocupado en ese equipo");
        }

        try {
            jugadorEquipos.save(new JugadorEquipo(id, equipo.getId(), equipo.getCategoriaId(), r.dorsal()));
        } catch (DataIntegrityViolationException e) {
            // por si dos peticiones llegan al mismo tiempo: la base de datos tiene la última palabra
            return mensaje(400, "No se pudo agregar: dorsal ocupado o ya tienes equipo en esa categoría");
        }

        return ResponseEntity.ok(new EstadoUsuarioDTO(true,
                EquipoJugadorDTO.de(jugadorEquipos.equiposDe(id))));
    }
}
