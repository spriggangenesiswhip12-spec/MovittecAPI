package com.example.movittecapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name="salones")
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_salon")
    private Integer idSalon;


    @Column(name="id_edificio")
    private Integer idEdificio;


    private String nombre;


    private String descripcion;


    private String imagen;


    private String piso;


    public Integer getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(Integer idSalon) {
        this.idSalon = idSalon;
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


    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }
}