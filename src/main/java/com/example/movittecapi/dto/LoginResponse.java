package com.example.movittecapi.dto;

/**
 * token solo viene para árbitros y admins; para los demás es null.
 * equipo / categoria / dorsal son los del primer equipo (compatibilidad con la app vieja);
 * la lista completa viene en "equipos".
 */
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
        String token,
        java.util.List<EquipoJugadorDTO> equipos
) {
}
