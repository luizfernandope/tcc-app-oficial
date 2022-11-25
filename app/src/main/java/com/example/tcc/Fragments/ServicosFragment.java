package com.example.tcc.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tcc.Adapters.TratamentoAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.databinding.FragmentServicosBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServicosFragment extends Fragment {

    private FragmentServicosBinding binding;
    APICall apiCall;
    TratamentoAdapter adapter;
    ArrayList<Tratamento> tratamentos;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServicosBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }/*fim dos metodos padrões das classes tipo Fragment; metodos: onCreateView, onViewCreated, onDestroyView*/

    /**/

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
        adapter = new TratamentoAdapter(binding.getRoot().getContext(), tratamentos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}