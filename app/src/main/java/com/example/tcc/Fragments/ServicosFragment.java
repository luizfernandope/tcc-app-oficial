package com.example.tcc.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tcc.Adapters.TratamentoAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
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

@RequiresApi(api = Build.VERSION_CODES.O)
public class ServicosFragment extends Fragment {

    private FragmentServicosBinding binding;
    APICall apiCall;
    TratamentoAdapter adapter;
    ArrayList<Tratamento> tratamentos = new ArrayList<Tratamento>();
    private String cpf, senha;
    RecyclerView recyclerViewTratamentos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServicosBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");

        recyclerViewTratamentos = binding.recyclerViewTratamento;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configurarRetrofit();

        if(tratamentos.size() <1){
            System.out.println("\n\nchamada da api para listar\n");
            Call<List<Tratamento>> chamadaAPI = apiCall.listarTratamentos();
            chamadaAPI.enqueue(new Callback<List<Tratamento>>() {
                @Override
                public void onResponse(Call<List<Tratamento>> call, Response<List<Tratamento>> response) {
                    for(int i=0; i<response.body().size(); i++){
                        tratamentos.add(response.body().get(i));
                    }
                    //depois de carregar o array de tratamentos
                    inicializarListagem();
                    System.out.println("\n\nlistagem da api feita\n");
                }

                @Override
                public void onFailure(Call<List<Tratamento>> call, Throwable t) {

                }
            });
        }

        binding.barraPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                binding.barraPesquisa.setBackgroundResource(R.drawable.borda_black_fill_blue);
                verificarPesquisa(newText);
                return true;
            }

        });
    }

    private void verificarPesquisa(String texto) {
        System.out.println("\n\nmetodo barra de pesquisa\n");
        ArrayList<Tratamento> tratamentosFiltrados = new ArrayList<>();
        for(Tratamento t: tratamentos){
            if(t.getNome().toLowerCase().contains(texto.toLowerCase())){
                tratamentosFiltrados.add(t);
            }
        }
        //exibe os itens
        adapter = new TratamentoAdapter(getContext(), tratamentosFiltrados, cpf, senha);
        recyclerViewTratamentos.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }/*fim dos metodos padrões das classes tipo Fragment; metodos: onCreateView, onViewCreated, onDestroyView*/



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
        recyclerViewTratamentos.setHasFixedSize(true);//da mais desempenho na listagem
        adapter = new TratamentoAdapter(getContext(), tratamentos, cpf, senha);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewTratamentos.setLayoutManager(layoutManager);
        recyclerViewTratamentos.setAdapter(adapter);
    }
}