package com.example.movittecapi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "partidos")
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "equipo_local_id")
    private Integer equipoLocalId;

    @Column(name = "equipo_visitante_id")
    private Integer equipoVisitanteId;

    private LocalDate fecha;

    private LocalTime hora;

    private String lugar;

    @Column(name = "marcador_local")
    private Integer marcadorLocal;

    @Column(name = "marcador_visitante")
    private Integer marcadorVisitante;

    private String estado;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    public Partido() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getEquipoLocalId() { return equipoLocalId; }
    public void setEquipoLocalId(Integer equipoLocalId) { this.equipoLocalId = equipoLocalId; }
    public Integer getEquipoVisitanteId() { return equipoVisitanteId; }
    public void setEquipoVisitanteId(Integer equipoVisitanteId) { this.equipoVisitanteId = equipoVisitanteId; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public Integer getMarcadorLocal() { return marcadorLocal; }
    public void setMarcadorLocal(Integer marcadorLocal) { this.marcadorLocal = marcadorLocal; }
    public Integer getMarcadorVisitante() { return marcadorVisitante; }
    public void setMarcadorVisitante(Integer marcadorVisitante) { this.marcadorVisitante = marcadorVisitante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
}
