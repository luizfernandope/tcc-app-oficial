package com.example.tcc.Models;

public class Consulta {

    private Integer id;

    private Double valor;

    private String datahora;

    private String situacao;

    private Integer sessaoAtual;

    private Cliente cliente;


    private Tratamento servico;

    private Clinica clinica;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDatahora() {
        return datahora;
    }

    public void setDatahora(String datahora) {
        this.datahora = datahora;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tratamento getServico() {
        return servico;
    }

    public void setServico(Tratamento servico) {
        this.servico = servico;
    }

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    public Integer getSessaoAtual() {
        return sessaoAtual;
    }

    public void setSessaoAtual(Integer sessaoAtual) {
        this.sessaoAtual = sessaoAtual;
    }
}
