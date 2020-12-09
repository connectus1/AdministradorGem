package com.gem.administradorgem.Fragment.Chat;

public class itemChat {
    private String Mensaje;
    private String Fecha;
    private boolean id;
    private String Nombre;

    public itemChat() {
    }

    public itemChat(String mensaje, String fecha, boolean id,String nombre) {
        Mensaje = mensaje;
        Fecha = fecha;
        this.id = id;
        Nombre = nombre;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
