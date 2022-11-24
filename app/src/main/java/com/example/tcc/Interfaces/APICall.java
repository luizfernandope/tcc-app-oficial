package com.example.tcc.Interfaces;

import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Tratamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APICall {

// https://clinica-tcc-api.herokuapp.com/            link api

    // Usuario.
    @GET("servico/listar")
    Call<List<Tratamento>> listarTratamentos();


    //Consulta
    @GET("consultas/listar")
    Call<List<Consulta>> listarConsultas();
}
