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

import com.example.tcc.Adapters.GastoClienteAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Cliente;
import com.example.tcc.R;
import com.example.tcc.databinding.FragmentVerClientesBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VerClientesFragment extends Fragment {

    private FragmentVerClientesBinding binding;
    private APICall apiCall;
    RecyclerView recyclerView;
    GastoClienteAdapter adapter;
    ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    ArrayList<Double> gastoClientes = new ArrayList<Double>();
    private String cpf, senha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVerClientesBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");
        recyclerView = binding.recyclerViewClientes;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configurarRetrofit();
        Call<List<Cliente>> call = apiCall.todosClientes(cpf, senha);
        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if(response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        clientes.add(response.body().get(i));
                    }
                    binding.numClientes.setText(Integer.toString(response.body().size()));
                    inicializarListagem();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {

            }
        });
    }

    void configurarRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.urlConexaoApi))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Instacia da interface;
        apiCall = retrofit.create(APICall.class);

    }

    void inicializarListagem(){
//
//        System.out.println("\n\n\n\n\n\nteste\n"+(cpfsClie.get(1)));
        recyclerView.setHasFixedSize(true);//da mais desempenho na listagem
        adapter = new GastoClienteAdapter(getContext(), clientes, gastoClientes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}