package com.example.movittecapi.dto;

public record EventoPartidoDTO(
        Integer idEvento,
        Integer partidoId,
        String tipo,
        Integer minuto,
        Integer equipoId,
        Integer jugadorId,
        String jugador,
        Integer jugadorAsistenciaId,
        String asistencia,
        String descripcion
) {
}
