package com.example.tcc.Interfaces;

import com.example.tcc.Dtos.ConsultaDto;
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
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICall {

// https://clinica-tcc-api.herokuapp.com/            link api


    @GET("servico/listar")
    Call<List<Tratamento>> listarTratamentos();

    @GET("consultas/listar/{cpf}/{senha}")
    Call<List<Consulta>> consultasCliente(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("consultas/{id}/{cpf}/{senha}")
    Call<Consulta> pegarConsulta(@Path("id") Integer id, @Path("cpf") String cpf, @Path("senha") String senha);

    @POST("consultas")
    Call<Consulta> marcarConsulta(@Body ConsultaDto consulta);

    @GET("usuario/login/{cpf}/{senha}")
    Call<Usuario> login(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("usuario/existe/{cpf}")
    Call<String> existeUsuarioByCPF(@Path("cpf") String cpf);

    @POST("usuario")
    Call<Usuario> cadastrarUsuario(@Body Usuario usuario);

    @POST("clientes")
    Call<Cliente> cadastrarCliente(@Body Cliente novoCliente);

    @GET("clientes/findCliente/{cpf}/{senha}")
    Call<Cliente> findCliente(@Path("cpf") String cpf, @Path("senha") String senha);

    @PUT("clientes/atualizarCliente")
    Call<Cliente> atualizarCliente(@Body Cliente cliente);

    @GET("servico/{id}")
    Call<Tratamento> findTratameto(@Path("id") Integer idTratamento);

}
