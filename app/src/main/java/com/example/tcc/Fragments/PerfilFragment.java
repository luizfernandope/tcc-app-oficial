package com.example.tcc.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Cliente;
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
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {

            }
        });

    }

    public void carregarCliente(){

    }

    void configurarRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinica-tcc-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Instacia da interface;
        apiCall = retrofit.create(APICall.class);
    }
}