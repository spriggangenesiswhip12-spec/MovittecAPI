package com.example.movittecapi.dto;

public record EquipoResponse(
        Integer id,
        Integer categoriaId,
        String categoria,
        String nombre,
        String descripcion,
        String logoUrl
) {
}
