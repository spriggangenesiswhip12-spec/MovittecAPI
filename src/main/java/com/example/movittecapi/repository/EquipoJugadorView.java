package com.example.movittecapi.repository;

/** Proyección: un equipo de un jugador con su categoría y dorsal. */
public interface EquipoJugadorView {

    Integer getEquipoId();

    String getEquipo();

    Integer getCategoriaId();

    String getCategoria();

    Integer getDorsal();
}
