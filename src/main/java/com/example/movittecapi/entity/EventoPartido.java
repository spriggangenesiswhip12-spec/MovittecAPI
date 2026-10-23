package com.example.movittecapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "eventos_partido")
public class EventoPartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Integer idEvento;

    @Column(name = "partido_id")
    private Integer partidoId;

    private String tipo;

    @Column(name = "jugador_id")
    private Integer jugadorId;

    @Column(name = "jugador_asistencia_id")
    private Integer jugadorAsistenciaId;

    @Column(name = "equipo_id")
    private Integer equipoId;

    private Integer minuto;

    private String descripcion;

    public EventoPartido() {
    }

    public Integer getIdEvento() { return idEvento; }
    public void setIdEvento(Integer idEvento) { this.idEvento = idEvento; }
    public Integer getPartidoId() { return partidoId; }
    public void setPartidoId(Integer partidoId) { this.partidoId = partidoId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getJugadorId() { return jugadorId; }
    public void setJugadorId(Integer jugadorId) { this.jugadorId = jugadorId; }
    public Integer getJugadorAsistenciaId() { return jugadorAsistenciaId; }
    public void setJugadorAsistenciaId(Integer jugadorAsistenciaId) { this.jugadorAsistenciaId = jugadorAsistenciaId; }
    public Integer getEquipoId() { return equipoId; }
    public void setEquipoId(Integer equipoId) { this.equipoId = equipoId; }
    public Integer getMinuto() { return minuto; }
    public void setMinuto(Integer minuto) { this.minuto = minuto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
