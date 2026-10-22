package com.example.movittecapi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "partidos")
public class Partido {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "equipo_local_id")
    private Integer equipoLocalId;

    @Column(name = "equipo_visitante_id")
    private Integer equipoVisitanteId;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "lugar")
    private String lugar;

    @Column(name = "marcador_local")
    private Integer marcadorLocal;

    @Column(name = "marcador_visitante")
    private Integer marcadorVisitante;

    // programado | en_curso | finalizado (los mismos valores que usa NetBeans)
    @Column(name = "estado")
    private String estado;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    public Partido() {
    }

    public Integer getId() { return id; }
    public Integer getEquipoLocalId() { return equipoLocalId; }
    public Integer getEquipoVisitanteId() { return equipoVisitanteId; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public String getLugar() { return lugar; }
    public Integer getMarcadorLocal() { return marcadorLocal; }
    public Integer getMarcadorVisitante() { return marcadorVisitante; }
    public String getEstado() { return estado; }
    public Integer getCategoriaId() { return categoriaId; }
}
