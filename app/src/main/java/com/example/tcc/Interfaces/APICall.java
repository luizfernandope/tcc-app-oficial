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


    @GET("servico/listar")
    Call<List<Tratamento>> listarTratamentos(); // funcional

    @GET("consultas/listar/{cpf}/{senha}")
    Call<List<Consulta>> consultasCliente(@Path("cpf") String cpf, @Path("senha") String senha);// funcional

    @GET("usuario/login/{cpf}/{senha}")
    Call<Usuario> login(@Path("cpf") String cpf, @Path("senha") String senha); // funcional

    @GET("clientes/findCliente/{cpf}/{senha}")
    Call<Cliente> findCliente(@Path("cpf") String cpf, @Path("senha") String senha); // funcional
}
