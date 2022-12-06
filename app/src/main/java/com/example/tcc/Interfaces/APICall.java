package com.example.tcc.Interfaces;

import com.example.tcc.Dtos.ConsultaDto;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Funcionario;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.Models.Usuario;

import java.util.List;
import java.util.Optional;

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
import retrofit2.http.QueryName;

public interface APICall {

// https://clinica-tcc-api.herokuapp.com/            link api


    @GET("servico/listar")
    Call<List<Tratamento>> listarTratamentos();

    @GET("consultas/listar/{cpf}/{senha}")
    Call<List<Consulta>> consultasCliente(@Path("cpf") String cpf, @Path("senha") String senha); // esse para numero de consultas

    @GET("consultas/{id}/{cpf}/{senha}")
    Call<Consulta> pegarConsulta(@Path("id") Integer id, @Path("cpf") String cpf, @Path("senha") String senha);

    @POST("consultas")
    Call<Consulta> marcarConsulta(@Body ConsultaDto consulta);

    @GET("consultas/saldo/{cpf}/{senha}")
    Call<Double> saldoTotal(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("consultas/mediaConsulta/{cpf}/{senha}")
    Call<Double> valorMedioPorConsulta(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("consultas/gastoClientes/{cpf}/{senha}")
    Call<List<String>> gastoPorCliente(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("usuario/login/{cpf}/{senha}")
    Call<Usuario> login(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("usuario/existe/{cpf}")
    Call<String> existeUsuarioByCPF(@Path("cpf") String cpf);

    @PUT("usuario/mudarSenha")
    Call<Usuario> atualizarSenhaUsuario(@Body Usuario usuario);

    @POST("usuario")
    Call<Usuario> cadastrarUsuario(@Body Usuario usuario);

    @POST("clientes")
    Call<Cliente> cadastrarCliente(@Body Cliente novoCliente);

    @GET("clientes/findCliente/{cpf}/{senha}")
    Call<Cliente> findCliente(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("clientes/listar/{cpf}/{senha}")
    Call<List<Cliente>> todosClientes(@Path("cpf") String cpf, @Path("senha") String senha);

    @GET("clientes/find/{cpfclie}/{cpf}/{senha}")
    Call<Cliente> findClienteByCpfCliente(@Path("cpfclie") String cpfclie,@Path("cpf") String cpf, @Path("senha") String senha);

    @PUT("clientes/atualizarCliente")
    Call<Cliente> atualizarCliente(@Body Cliente cliente);

    @GET("funcionarios/findFuncionario/{cpf}/{senha}")
    Call<Funcionario> findFuncionario(@Path("cpf") String cpf, @Path("senha") String senha);

    @PUT("funcionarios/atualizar")
    Call<Funcionario> atualizarFuncionario(@Body Funcionario funcionario);

    @GET("servico/{id}")
    Call<Tratamento> findTratameto(@Path("id") Integer idTratamento);

    @GET("clientes/{cpf}/{email}/{phone}")
    Call<Cliente> findByCpfEmailAndTelefone(@Path(value = "cpf") String cpf, @Path(value = "email") String email,@Path(value = "phone") String phone);

    @PUT("servico/atualizarServico/{id}/{cpf}/{senha}")
    Call<Tratamento> atualizarTratamento(@Path("id") Integer id, @Path("cpf") String cpf, @Path("senha") String senha,@Body Tratamento servico);

    @POST("servico/{cpf}/{senha}")
    Call<Optional<Tratamento>> cadastrarTratamento(@Path("cpf") String cpf, @Path("senha") String senha, @Body Tratamento servico);
}
