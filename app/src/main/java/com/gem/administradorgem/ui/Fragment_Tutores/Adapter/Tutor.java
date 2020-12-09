package com.gem.administradorgem.ui.Fragment_Tutores.Adapter;

import java.io.Serializable;

public class Tutor implements Serializable {
    private String nombre;
    private String matricula;

    private String id;

    public Tutor() {
    }

    public Tutor(String nombre, String matricula, String id) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.id = id;
    }

    public Tutor(Tutor tutor){
        this.nombre = tutor.getNombre();
        this.matricula = tutor.getMatricula();
        this.id = tutor.getId();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
