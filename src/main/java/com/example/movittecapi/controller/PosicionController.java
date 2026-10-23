package com.example.movittecapi.controller;

import com.example.movittecapi.dto.PosicionDTO;
import com.example.movittecapi.entity.Equipo;
import com.example.movittecapi.entity.Partido;
import com.example.movittecapi.repository.EquipoRepository;
import com.example.movittecapi.repository.PartidoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posiciones")
@CrossOrigin(origins = "*")
public class PosicionController {

    private final EquipoRepository equipos;
    private final PartidoRepository partidos;

    public PosicionController(EquipoRepository equipos, PartidoRepository partidos) {
        this.equipos = equipos;
        this.partidos = partidos;
    }

    private static int n(Integer valor) {
        return valor == null ? 0 : valor;
    }

    /**
     * GET /posiciones?categoria=3
     * Solo cuentan los partidos finalizados. Victoria = 3 pts, empate = 1.
     */
    @GetMapping
    public List<PosicionDTO> tabla(@RequestParam Integer categoria) {

        // [jugados, ganados, empatados, perdidos, golesFavor, golesContra]
        Map<Integer, int[]> stats = new LinkedHashMap<>();
        Map<Integer, String> nombres = new HashMap<>();

        for (Equipo e : equipos.findByCategoriaIdOrderByNombreAsc(categoria)) {
            stats.put(e.getId(), new int[6]);
            nombres.put(e.getId(), e.getNombre());
        }

        for (Partido p : partidos.findByCategoriaIdOrderByFechaAscHoraAsc(categoria)) {
            if (!"finalizado".equalsIgnoreCase(p.getEstado())) {
                continue;
            }
            int[] local = stats.get(p.getEquipoLocalId());
            int[] visita = stats.get(p.getEquipoVisitanteId());
            if (local == null || visita == null) {
                continue;
            }

            int gl = n(p.getMarcadorLocal());
            int gv = n(p.getMarcadorVisitante());

            local[0]++;
            visita[0]++;
            local[4] += gl;
            local[5] += gv;
            visita[4] += gv;
            visita[5] += gl;

            if (gl > gv) {
                local[1]++;
                visita[3]++;
            } else if (gl < gv) {
                visita[1]++;
                local[3]++;
            } else {
                local[2]++;
                visita[2]++;
            }
        }

        List<PosicionDTO> sinOrden = new ArrayList<>();
        for (Map.Entry<Integer, int[]> entrada : stats.entrySet()) {
            int[] s = entrada.getValue();
            sinOrden.add(new PosicionDTO(
                    0,
                    entrada.getKey(),
                    nombres.get(entrada.getKey()),
                    s[0], s[1], s[2], s[3], s[4], s[5],
                    s[4] - s[5],
                    s[1] * 3 + s[2]));
        }

        sinOrden.sort(
                Comparator.comparingInt(PosicionDTO::puntos).reversed()
                        .thenComparing(Comparator.comparingInt(PosicionDTO::diferencia).reversed())
                        .thenComparing(Comparator.comparingInt(PosicionDTO::golesFavor).reversed())
                        .thenComparing(PosicionDTO::equipo));

        List<PosicionDTO> resultado = new ArrayList<>();
        for (int i = 0; i < sinOrden.size(); i++) {
            PosicionDTO d = sinOrden.get(i);
            resultado.add(new PosicionDTO(i + 1, d.equipoId(), d.equipo(), d.jugados(), d.ganados(),
                    d.empatados(), d.perdidos(), d.golesFavor(), d.golesContra(), d.diferencia(), d.puntos()));
        }
        return resultado;
    }
}
