package com.example.movittecapi.controller;

import com.example.movittecapi.dto.JugadorResponse;
import com.example.movittecapi.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jugadores")
@CrossOrigin(origins = "*")
public class JugadorController {

    private final UsuarioRepository usuarioRepository;

    public JugadorController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // GET /jugadores?categoria=Sub-17&equipo=Tigres
    // Mismo criterio que la pantalla Jugadores de NetBeans (goles y asistencias
    // contados en eventos_partido), ordenado por goles.
    @GetMapping
    public List<JugadorResponse> listar(@RequestParam(required = false) String categoria,
                                        @RequestParam(required = false) String equipo) {

        List<JugadorResponse> resultado = new ArrayList<>();

        for (Object[] f : usuarioRepository.listarJugadores()) {
            String cat = f[4] == null ? null : f[4].toString();
            String eq = f[3] == null ? null : f[3].toString();

            if (categoria != null && !categoria.isBlank() && !categoria.equals(cat)) continue;
            if (equipo != null && !equipo.isBlank() && !equipo.equals(eq)) continue;

            resultado.add(new JugadorResponse(
                    ((Number) f[0]).intValue(),
                    (String) f[1],
                    (String) f[2],
                    eq,
                    cat,
                    f[5] == null ? null : f[5].toString(),
                    f[6] == null ? 0 : ((Number) f[6]).intValue(),
                    f[7] == null ? 0 : ((Number) f[7]).intValue()
            ));
        }

        return resultado;
    }
}
