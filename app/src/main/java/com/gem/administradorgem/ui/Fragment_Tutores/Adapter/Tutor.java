package com.gem.administradorgem.ui.Fragment_Tutores.Adapter;

import java.io.Serializable;
import java.util.List;

public class Tutor implements Serializable {
    private String id;
    private String nombre;
    private String correo;
    private String contra;

    private List<ItemHijo> hijos;

    public Tutor() {
    }

    public Tutor(String id, String nombre, String correo, String contra, List<ItemHijo> hijos) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contra = contra;
        this.hijos = hijos;
    }

    public Tutor(Tutor tutor) {
        this.id = tutor.getId();
        this.nombre = tutor.getNombre();
        this.correo = tutor.getCorreo();
        this.contra = tutor.getContra();
        this.hijos = getHijos();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public List<ItemHijo> getHijos() {
        return hijos;
    }

    public void setHijos(List<ItemHijo> hijos) {
        this.hijos = hijos;
    }
}
