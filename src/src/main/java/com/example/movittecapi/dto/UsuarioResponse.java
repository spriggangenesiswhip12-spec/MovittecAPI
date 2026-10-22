package com.example.movittecapi.dto;


public class UsuarioResponse {


    private Integer idUsuario;

    private String nombre;

    private String apellidoPaterno;

    private String apellidoMaterno;

    private String correo;

    private String tipoUsuario;

    private String fotoPerfil;

    private Boolean activo;



    public Integer getIdUsuario() {
        return idUsuario;
    }


    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getApellidoPaterno() {
        return apellidoPaterno;
    }


    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }


    public String getApellidoMaterno() {
        return apellidoMaterno;
    }


    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }


    public String getCorreo() {
        return correo;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getTipoUsuario() {
        return tipoUsuario;
    }


    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }


    public String getFotoPerfil() {
        return fotoPerfil;
    }


    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }


    public Boolean getActivo() {
        return activo;
    }


    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}