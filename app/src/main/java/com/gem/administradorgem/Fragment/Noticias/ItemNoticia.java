package com.gem.administradorgem.Fragment.Noticias;

public class ItemNoticia {
    private String titulo;
    private String descripcion;
    private String urlAlmacenamiento;
    private String fecha;

    //private String id;


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

    public void setUrlAlmacenamiento(String urlAlmacenamiento) {
        this.urlAlmacenamiento = urlAlmacenamiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
