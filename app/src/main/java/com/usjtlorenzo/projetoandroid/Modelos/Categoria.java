package com.usjtlorenzo.projetoandroid.Modelos;

public class Categoria {
    private String nome;
    private String icone;

    public Categoria(String nomeCategoria, String nomeIcone) {
        this.nome = nomeCategoria;
        this.icone = nomeIcone;
    }

    public Categoria() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }
}
