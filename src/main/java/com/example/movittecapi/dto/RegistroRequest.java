package com.example.movittecapi.dto;

public record RegistroRequest(
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String correo,
        String contrasena,
        String equipo,
        String categoria,
        Integer dorsal
) {
}
