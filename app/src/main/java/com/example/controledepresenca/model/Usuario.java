package com.example.controledepresenca.model;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String rgm;
    private String Modo;

    public Usuario(){
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRgm() {
        return rgm;
    }

    public void setRgm(String rgm) {
        this.rgm = rgm;
    }

    public String getModo() {
        return Modo;
    }

    public void setModo(String modo) {
        this.Modo = modo;
    }
}
