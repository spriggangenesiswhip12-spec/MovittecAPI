package com.example.movittecapi.dto;

/** token solo viene para árbitros y admins; para los demás es null. */
public record LoginResponse(
        Integer idUsuario,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String correo,
        String tipoUsuario,
        String fotoPerfil,
        String equipo,
        String categoria,
        Integer dorsal,
        String token
) {
}
