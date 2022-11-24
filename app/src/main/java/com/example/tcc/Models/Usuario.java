package com.example.tcc.Models;

public class Usuario {

    private String cpf;

    private  String senha;

    private boolean cliente;

    private String dt_admissao;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isCliente() {
        return cliente;
    }

    public void setCliente(boolean cliente) {
        this.cliente = cliente;
    }

    public String getDt_admissao() {
        return dt_admissao;
    }

    public void setDt_admissao(String dt_admissao) {
        this.dt_admissao = dt_admissao;
    }
}
