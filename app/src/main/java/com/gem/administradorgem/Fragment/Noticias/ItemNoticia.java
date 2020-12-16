package com.gem.administradorgem.Fragment.Noticias;

public class ItemNoticia {
    private String titulo;
    private String descripcion;
    private String fecha;

    private String urlAlmacenamiento;

    private String nivel;
    private String grado;
    private String grupo;

    public String getUrlAlmacenamiento() {
        return urlAlmacenamiento;
    }

    public void setUrl(String url) {
        this.urlAlmacenamiento = url;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
