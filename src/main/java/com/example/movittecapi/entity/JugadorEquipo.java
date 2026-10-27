package com.example.movittecapi.entity;

import jakarta.persistence.*;

/**
 * Un jugador puede jugar en varios equipos, pero solo en uno por categoría
 * (unique id_usuario + categoria_id) y con un dorsal propio en cada equipo.
 */
@Entity
@Table(name = "jugador_equipo")
public class JugadorEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "equipo_id")
    private Integer equipoId;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    private Integer dorsal;

    public JugadorEquipo() {
    }

    public JugadorEquipo(Integer idUsuario, Integer equipoId, Integer categoriaId, Integer dorsal) {
        this.idUsuario = idUsuario;
        this.equipoId = equipoId;
        this.categoriaId = categoriaId;
        this.dorsal = dorsal;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Integer getEquipoId() { return equipoId; }
    public void setEquipoId(Integer equipoId) { this.equipoId = equipoId; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    public Integer getDorsal() { return dorsal; }
    public void setDorsal(Integer dorsal) { this.dorsal = dorsal; }
}
