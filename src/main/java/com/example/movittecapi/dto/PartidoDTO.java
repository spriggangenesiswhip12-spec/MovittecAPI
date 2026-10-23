package com.example.movittecapi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record PartidoDTO(
        Integer id,
        Integer categoriaId,
        Integer equipoLocalId,
        String equipoLocal,
        Integer equipoVisitanteId,
        String equipoVisitante,
        LocalDate fecha,
        LocalTime hora,
        String lugar,
        int marcadorLocal,
        int marcadorVisitante,
        String estado
) {
}
