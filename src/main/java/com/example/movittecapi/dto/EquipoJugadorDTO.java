package com.example.movittecapi.dto;

import com.example.movittecapi.repository.EquipoJugadorView;

import java.util.List;

/** Un equipo en el que juega el usuario. */
public record EquipoJugadorDTO(
        Integer equipoId,
        String equipo,
        Integer categoriaId,
        String categoria,
        Integer dorsal
) {

    public static List<EquipoJugadorDTO> de(List<EquipoJugadorView> vistas) {
        return vistas.stream()
                .map(v -> new EquipoJugadorDTO(v.getEquipoId(), v.getEquipo(),
                        v.getCategoriaId(), v.getCategoria(), v.getDorsal()))
                .toList();
    }
}
