package com.example.tcc.Dtos;

import java.time.LocalDateTime;

public class ConsultaDto {

    private Double valor;
    private String datahora;
    private String situacao;
    private String cpf_cliente;
    private Integer id_servico;
    private Integer id_clinica;

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

    public String getCpf_cliente() {
        return cpf_cliente;
    }

    public void setCpf_cliente(String cpf_cliente) {
        this.cpf_cliente = cpf_cliente;
    }

    public Integer getId_servico() {
        return id_servico;
    }

    public void setId_servico(Integer id_servico) {
        this.id_servico = id_servico;
    }

    public Integer getId_clinica() {
        return id_clinica;
    }

    public void setId_clinica(Integer id_clinica) {
        this.id_clinica = id_clinica;
    }
}
