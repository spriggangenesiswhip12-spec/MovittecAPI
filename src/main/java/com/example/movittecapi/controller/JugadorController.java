package com.example.movittecapi.controller;

import com.example.movittecapi.dto.JugadorDTO;
import com.example.movittecapi.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jugadores")
@CrossOrigin(origins = "*")
public class JugadorController {

    private final UsuarioRepository usuarios;

    public JugadorController(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * GET /jugadores?categoria=Primera Fuerza&equipo=Tigres
     * Ordenados de más a menos goles (sirve para la plantilla y para el ranking de goleadores).
     */
    @GetMapping
    public List<JugadorDTO> listar(@RequestParam(required = false) String categoria,
                                   @RequestParam(required = false) String equipo) {

        String cat = (categoria == null || categoria.isBlank()) ? null : categoria.trim();
        String eq = (equipo == null || equipo.isBlank()) ? null : equipo.trim();

        return usuarios.estadisticas(cat, eq).stream()
                .map(s -> new JugadorDTO(
                        s.getId(),
                        (s.getNombre() + " " + (s.getApellido() == null ? "" : s.getApellido())).trim(),
                        s.getEquipo(),
                        s.getCategoria(),
                        s.getDorsal(),
                        s.getGoles() == null ? 0 : s.getGoles(),
                        s.getAsistencias() == null ? 0 : s.getAsistencias()))
                .toList();
    }
}
