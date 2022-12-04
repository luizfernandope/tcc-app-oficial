package com.example.tcc.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tcc.Adapters.ConsultaAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.databinding.FragmentAgendaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AgendaFragment extends Fragment {

   private FragmentAgendaBinding binding;
    private APICall apiCall;
    private ConsultaAdapter adapter;
    ArrayList<Consulta> consultas;
    RecyclerView recyclerView;
    private String cpf, senha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAgendaBinding.inflate(inflater, container, false);
        //pegando o usuario e senha da activity main
        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        consultas = new ArrayList<Consulta>();
        configurarRetrofit();

        Call<List<Consulta>> chamadaApi = apiCall.consultasCliente(cpf, senha);

        chamadaApi.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                for(int i=0; i<response.body().size(); i++){
                    consultas.add(response.body().get(i));
                }
                //depois de carregar o array de tratamentos
                inicializarListagem();
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Toast.makeText(binding.getRoot().getContext(), "erro ao requisitar suas consultas | "+t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("\n\n\n"+"mensagem: "+t.getMessage());
                System.out.println("\n\n\n"+"erro: "+t.getLocalizedMessage());
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

    void inicializarListagem(){
        recyclerView = binding.recyclerViewAgenda;
        recyclerView.setHasFixedSize(true);//da mais desempenho na listagem
        adapter = new ConsultaAdapter(binding.getRoot().getContext(), consultas, cpf, senha);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}