package com.example.movittecapi.dto;

/** tipo: gol, tarjeta_amarilla, tarjeta_roja o sustitucion. */
public record EventoRequest(
        String tipo,
        Integer equipoId,
        Integer jugadorId,
        Integer jugadorAsistenciaId,
        Integer minuto,
        String descripcion
) {
}
