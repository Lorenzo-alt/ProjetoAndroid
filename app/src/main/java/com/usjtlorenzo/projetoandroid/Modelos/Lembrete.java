package com.usjtlorenzo.projetoandroid.Modelos;

public class Lembrete {
    private String nome, real_data, data, cor, correlacao;

    public Lembrete() {
    }

    public Lembrete(String nome, String real_data, String data, String cor, String correlacao) {
        this.nome = nome;
        this.real_data = real_data;
        this.data = data;
        this.cor = cor;
        this.correlacao = correlacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getReal_data() {
        return real_data;
    }

    public void setReal_data(String real_data) {
        this.real_data = real_data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCorrelacao() {
        return correlacao;
    }

    public void setCorrelacao(String correlacao) {
        this.correlacao = correlacao;
    }
}
