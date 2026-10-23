package com.example.movittecapi.dto;

public record JugadorDTO(
        Integer id,
        String nombre,
        String equipo,
        String categoria,
        Integer dorsal,
        long goles,
        long asistencias
) {
}
