package com.gem.administradorgem.ui.Fragment_Tutores.Adapter;

public class ItemHijo {
    private String nivel;
    private String matricula;

    private String grado;
    private String grupo;

    public ItemHijo() {
    }

    public ItemHijo(String nivel, String matricula, String grado, String grupo) {
        this.nivel = nivel;
        this.matricula = matricula;
        this.grado = grado;
        this.grupo = grupo;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
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

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
