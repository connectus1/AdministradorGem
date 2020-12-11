package com.gem.administradorgem.ui.Fragment_Alumnos.Adapter;

import java.io.Serializable;

public class Alumno implements Serializable {
    private String id;
    private String nombre;
    private String correo;

    private String plantel;
    private String matricula;

    private String nivel;
    private String grado;
    private String grupo;

    public Alumno() {

    }

    public Alumno(String id, String nombre, String correo, String plantel, String matricula, String nivel, String grado, String grupo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.plantel = plantel;
        this.matricula = matricula;
        this.nivel = nivel;
        this.grado = grado;
        this.grupo = grupo;
    }

    public Alumno(Alumno alumno){
        this.id = alumno.getId();
        this.nombre = alumno.getNombre();
        this.correo = alumno.getCorreo();
        this.plantel = alumno.getPlantel();
        this.matricula = alumno.getMatricula();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPlantel() {
        return plantel;
    }

    public void setPlantel(String plantel) {
        this.plantel = plantel;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
