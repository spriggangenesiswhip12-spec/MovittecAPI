package com.example.movittecapi.dto;

public class LoginResponse {

    private Integer idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String matricula;
    private String correo;
    private String tipoUsuario;
    private String fotoPerfil;
    private String grupo;
    private String grupoIngles;
    private String equipo;
    private String categoria;
    private String dorsal;


    public LoginResponse(
            Integer idUsuario,
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String matricula,
            String correo,
            String tipoUsuario,
            String fotoPerfil,
            String grupo,
            String grupoIngles,
            String equipo,
            String categoria,
            String dorsal
    ) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.matricula = matricula;
        this.correo = correo;
        this.tipoUsuario = tipoUsuario;
        this.fotoPerfil = fotoPerfil;
        this.grupo = grupo;
        this.grupoIngles = grupoIngles;
        this.equipo = equipo;
        this.categoria = categoria;
        this.dorsal = dorsal;
    }


    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getGrupoIngles() {
        return grupoIngles;
    }

    public String getEquipo() {
        return equipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDorsal() {
        return dorsal;
    }
}
