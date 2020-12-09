package com.gem.administradorgem.ui.Fragment_Alumnos.Adapter;

import java.io.Serializable;

public class Alumno implements Serializable {
    private String id;
    private String nombre;
    private String correo;
    private String plantel;
    private String matricula;

    public Alumno() {

    }

    public Alumno(String id, String nombre, String correo, String plantel,String matricula) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.plantel = plantel;
        this.matricula = matricula;
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
}
