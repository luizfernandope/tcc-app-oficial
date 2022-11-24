package com.example.tcc.Activitys;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Adapters.ConsultaAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.databinding.ActivityAgendaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Agenda extends AppCompatActivity {

    private ActivityAgendaBinding binding;
    private APICall apiCall;
    private ConsultaAdapter adapter;
    ArrayList<Consulta> consultas;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        consultas = new ArrayList<Consulta>();
        configurarRetrofit();

        Call<List<Consulta>> chamadaApi = apiCall.listarConsultas();

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
                Toast.makeText(binding.getRoot().getContext(), "erro: "+t.getMessage(), Toast.LENGTH_SHORT).show();
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
                .baseUrl("https://clinica-tcc-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Instacia da interface;
        apiCall = retrofit.create(APICall.class);

    }

    void inicializarListagem(){
        recyclerView = binding.recyclerViewAgenda;
        recyclerView.setHasFixedSize(true);//da mais desempenho na listagem
        adapter = new ConsultaAdapter(Agenda.this, consultas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Agenda.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}