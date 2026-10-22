package com.example.movittecapi.dto;

public record JugadorResponse(
        Integer idUsuario,
        String nombre,
        String apellidoPaterno,
        String equipo,
        String categoria,
        String dorsal,
        int goles,
        int asistencias
) {
}
