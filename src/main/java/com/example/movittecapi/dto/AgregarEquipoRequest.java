package com.example.movittecapi.dto;

/** El jugador se agrega solo a otro equipo; confirma con su contraseña porque los jugadores no usan token. */
public record AgregarEquipoRequest(
        String contrasena,
        Integer equipoId,
        Integer dorsal
) {
}
