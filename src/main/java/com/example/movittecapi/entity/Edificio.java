package com.example.movittecapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "edificios")
public class Edificio {

    @Id
    @Column(name = "id_edificio")
    private Integer idEdificio;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "coordenada_x")
    private Float coordenadaX;

    @Column(name = "coordenada_y")
    private Float coordenadaY;

    @Column(name = "ancho")
    private Float ancho;

    @Column(name = "alto")
    private Float alto;


    public Edificio() {
    }


    public Integer getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(Integer idEdificio) {
        this.idEdificio = idEdificio;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


    public Float getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(Float coordenadaX) {
        this.coordenadaX = coordenadaX;
    }


    public Float getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(Float coordenadaY) {
        this.coordenadaY = coordenadaY;
    }


    public Float getAncho() {
        return ancho;
    }

    public void setAncho(Float ancho) {
        this.ancho = ancho;
    }


    public Float getAlto() {
        return alto;
    }

    public void setAlto(Float alto) {
        this.alto = alto;
    }
}