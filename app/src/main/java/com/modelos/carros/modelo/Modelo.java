package com.modelos.carros.modelo;

public class Modelo implements java.io.Serializable {

    private String nome,id_marca;

    public Modelo() { }
    public String getId_marca() {
        return id_marca;
    }

    public void setId_marca(String id_marca) {
        this.id_marca = id_marca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}