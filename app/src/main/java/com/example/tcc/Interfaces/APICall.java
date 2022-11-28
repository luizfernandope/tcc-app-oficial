package com.example.tcc.Interfaces;

import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.Models.Usuario;

import java.util.List;

import kotlin.ParameterName;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICall {

// https://clinica-tcc-api.herokuapp.com/            link api

    // Usuario.
    @GET("servico/listar")
    Call<List<Tratamento>> listarTratamentos();


    //Consulta
    @GET("consultas/listar")
    Call<List<Consulta>> listarConsultas();

    @HTTP(method = "GET", path = "usuario/login", hasBody = true)
    Call<Usuario> login(@Body Usuario login);

}
