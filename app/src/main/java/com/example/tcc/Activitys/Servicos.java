package com.example.tcc.Activitys;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Adapters.TratamentoAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.databinding.ActivityServicosBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Servicos extends AppCompatActivity {

    private ActivityServicosBinding binding;
    APICall apiCall;
    TratamentoAdapter adapter;
    ArrayList<Tratamento> tratamentos;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServicosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tratamentos = new ArrayList<Tratamento>();
        configurarRetrofit();

        Call<List<Tratamento>> chamadaAPI = apiCall.listarTratamentos();
        chamadaAPI.enqueue(new Callback<List<Tratamento>>() {
            @Override
            public void onResponse(Call<List<Tratamento>> call, Response<List<Tratamento>> response) {
                for(int i=0; i<response.body().size(); i++){
                    tratamentos.add(response.body().get(i));
                }
                //depois de carregar o array de tratamentos
                inicializarListagem();
            }

            @Override
            public void onFailure(Call<List<Tratamento>> call, Throwable t) {

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
        recyclerView = binding.recyclerViewTratamento;
        recyclerView.setHasFixedSize(true);//da mais desempenho na listagem
        adapter = new TratamentoAdapter(Servicos.this, tratamentos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Servicos.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}