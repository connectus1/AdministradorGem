package com.gem.administradorgem.Fragment.CargarUsuarios.Adapter;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private String mensaje;
    private String nombre;

    private String id;
    private String tipo;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String fecha,String id,String tipo) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.id = id;
        this.tipo = tipo;
    }

    public Mensaje(Mensaje mensaje){
        this.nombre = mensaje.getNombre();
        this.mensaje = mensaje.getMensaje();
        this.id = mensaje.getId();
        this.tipo = mensaje.getTipo();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
