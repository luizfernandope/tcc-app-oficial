package com.example.tcc.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Funcionario;
import com.example.tcc.R;
import com.example.tcc.databinding.FragmentPerfilBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private APICall apiCall;

    private String cpf, senha;
    private Cliente cliente;
    private Funcionario funcionario;
    boolean ehFuncionario = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        //pegando o usuario e senha da activity main
        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(cliente == null)
            carregarCliente();
        if(funcionario == null)
            carregarFuncionario();


        binding.btnEditarPefil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.btnEditarPefil.getText().toString().contains("editar perfil"))
                {
                    binding.btnEditarPefil.setBackgroundResource(R.drawable.bg_curvo12);
                    binding.btnEditarPefil.setText("salvar");
                    if(cliente != null) {
                        binding.etNome.setEnabled(true);
                        binding.emailEditTextPerfil.setEnabled(true);
                        binding.dataNasciEditTextPerfil.setEnabled(true);
                        binding.sexoEditTextPerfil.setEnabled(true);
                    }
                    else{
                        binding.etNome.setEnabled(true);
                        binding.emailEditTextPerfil.setEnabled(true);
                    }
                }
                else{
                    atualizarPerfil();
                }
            }
        });
    }

    public void carregarCliente(){
        configurarRetrofit();
        Call<Cliente> pegarCliente = apiCall.findCliente(cpf, senha);
        pegarCliente.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful())
                {
                    binding.etNome.setText(response.body().getNome());
                    binding.etMaskCPF.setText(response.body().getUsuario().getCpf());
                    binding.dataNasciEditTextPerfil.setText(response.body().getDtNascimento());
                    binding.sexoEditTextPerfil.setText(response.body().getSexo());
                    binding.emailEditTextPerfil.setText(response.body().getEmail());
                    cliente = response.body();
                }
                else{
                    ehFuncionario = true;
                    carregarFuncionario();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
            }
        });
    }

    public void carregarFuncionario(){
        configurarRetrofit();
        Call<Funcionario> pegarFuncionario = apiCall.findFuncionario(cpf, senha);
        pegarFuncionario.enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if(response.isSuccessful()){
                    binding.etNome.setText(response.body().getNome());
                    binding.etMaskCPF.setText(response.body().getUsuario().getCpf());
                    binding.dataNasciEditTextPerfil.setVisibility(View.GONE);
                    binding.dataNasciContainerPerfil.setVisibility(View.GONE);
                    binding.sexoEditTextPerfil.setText(response.body().getCargo());
                    binding.emailEditTextPerfil.setText(response.body().getEmail());
                    funcionario = response.body();
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {

            }
        });
    }

    void configurarRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-164-21-210.compute-1.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Instacia da interface;
        apiCall = retrofit.create(APICall.class);
    }

    public void atualizarPerfil(){
        configurarRetrofit();
        if(!ehFuncionario) {
            cliente.setNome(binding.etNome.getText().toString());
            cliente.setDtNascimento(binding.dataNasciEditTextPerfil.getText().toString());
            cliente.setSexo(binding.sexoEditTextPerfil.getText().toString());
            cliente.setEmail(binding.emailEditTextPerfil.getText().toString());
            Call<Cliente> clienteCall = apiCall.atualizarCliente(cliente);
            clienteCall.enqueue(new Callback<Cliente>() {
                @Override
                public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "Atualização de perfil bem sucedida", Toast.LENGTH_SHORT).show();
                        binding.btnEditarPefil.setBackgroundResource(R.drawable.borda_black);
                        binding.btnEditarPefil.setText("editar perfil");

                        binding.etNome.setEnabled(false);
                        binding.emailEditTextPerfil.setEnabled(false);
                        binding.dataNasciEditTextPerfil.setEnabled(false);
                        binding.sexoEditTextPerfil.setEnabled(false);

                    } else
                        Toast.makeText(getContext(), "Falha ao atualizar perfil!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Cliente> call, Throwable t) {
                    Toast.makeText(getContext(), "Falha requisicao de atualizacao", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (ehFuncionario){
            funcionario.setNome(binding.etNome.getText().toString());
            funcionario.setEmail(binding.emailEditTextPerfil.getText().toString());
            Call<Funcionario> funcionarioCall = apiCall.atualizarFuncionario(funcionario);
            funcionarioCall.enqueue(new Callback<Funcionario>() {
                @Override
                public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getContext(), "Atualização de perfil bem sucedida", Toast.LENGTH_SHORT).show();
                        binding.btnEditarPefil.setBackgroundResource(R.drawable.borda_black);
                        binding.btnEditarPefil.setText("editar perfil");
                        binding.etNome.setEnabled(false);
                        binding.emailEditTextPerfil.setEnabled(false);
                    }
                    else
                        Toast.makeText(getContext(), "Falha ao atualizar perfil!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Funcionario> call, Throwable t) {
                    Toast.makeText(getContext(), "Falha requisicao de atualizacao", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}