package com.example.movittecapi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record PartidoResponse(
        Integer id,
        Integer categoriaId,
        String categoria,
        Integer equipoLocalId,
        String equipoLocal,
        String logoLocal,
        Integer equipoVisitanteId,
        String equipoVisitante,
        String logoVisitante,
        LocalDate fecha,
        LocalTime hora,
        String lugar,
        Integer marcadorLocal,
        Integer marcadorVisitante,
        String estado
) {
}
