package com.modelos.carros.carro;

public class Carro implements java.io.Serializable {

    private int renavam, ano;

    private String nome, placa, id_modelo;

    float valor;

    public Carro() {}


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId_modelo() {
        return id_modelo;
    }

    public void setId_modelo(String id_modelo) {
        this.id_modelo = id_modelo;
    }

    public int getRenavam() {
        return renavam;
    }

    public void setRenavam(int renavam) {
        this.renavam = renavam;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

}