package com.example.movittecapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "logo_url")
    private String logoUrl;

    public Equipo() {
    }

    public Integer getId() { return id; }
    public Integer getCategoriaId() { return categoriaId; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getLogoUrl() { return logoUrl; }
}
