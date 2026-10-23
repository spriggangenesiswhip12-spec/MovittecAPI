package com.example.movittecapi.repository;

/** Proyección del query nativo de estadísticas (los alias del SQL coinciden con estos getters). */
public interface JugadorStats {

    Integer getId();

    String getNombre();

    String getApellido();

    String getEquipo();

    String getCategoria();

    Integer getDorsal();

    Long getGoles();

    Long getAsistencias();
}
