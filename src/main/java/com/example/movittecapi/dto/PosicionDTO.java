package com.example.movittecapi.dto;

public record PosicionDTO(
        int posicion,
        Integer equipoId,
        String equipo,
        int jugados,
        int ganados,
        int empatados,
        int perdidos,
        int golesFavor,
        int golesContra,
        int diferencia,
        int puntos
) {
}
