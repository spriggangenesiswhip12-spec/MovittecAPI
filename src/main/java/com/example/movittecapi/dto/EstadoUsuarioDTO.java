package com.example.movittecapi.dto;

import java.util.List;

/** Respuesta de GET /usuarios/{id}/estado: si sigue activo y sus equipos actuales. */
public record EstadoUsuarioDTO(
        Boolean activo,
        List<EquipoJugadorDTO> equipos
) {
}
