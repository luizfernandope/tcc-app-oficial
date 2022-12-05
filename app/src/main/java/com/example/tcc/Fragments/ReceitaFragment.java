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
import android.widget.Toast;

import com.example.tcc.Adapters.GastoClienteAdapter;
import com.example.tcc.Adapters.TratamentoAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Consulta;
import com.example.tcc.R;
import com.example.tcc.databinding.FragmentReceitaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceitaFragment extends Fragment {

    private FragmentReceitaBinding binding;
    private APICall apiCall;
    RecyclerView recyclerView;
    GastoClienteAdapter adapter;
    ArrayList<String> cpfsClie = new ArrayList<String>();
    ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    ArrayList<Double> gastoClientes = new ArrayList<Double>();
    private String cpf, senha;
    int qtdClientesComConsulta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceitaBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerViewGastoclientes;

        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configurarRetrofit();

        Call<Double> chamarSaldo = apiCall.saldoTotal(cpf, senha);
        chamarSaldo.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if(response.body() != null)
                {
                    binding.saldoTotal.setText("R$ "+response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {

            }
        });

        Call<List<String>> gastosPorClientes = apiCall.gastoPorCliente(cpf, senha);
        gastosPorClientes.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()) {
                    binding.numClientes.setText(Integer.toString(response.body().size()));
                    for (int i = 0; i < response.body().size(); i++) {
                        cpfsClie.add(response.body().get(i).substring(0, 11));
                        int totalCaracteres = response.body().get(i).length();
                        gastoClientes.add(Double.parseDouble(response.body().get(i).substring(12, totalCaracteres)));
                        //preenchendo clientes
                    }
//                        carregarClientes();
                    qtdClientesComConsulta = response.body().size();


                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        Call<Double> mediaCall = apiCall.valorMedioPorConsulta(cpf, senha);
        mediaCall.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if(response.body() != null)
                    binding.precoMedioConsulta.setText("R$ "+ Double.toString(response.body()));
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {

            }
        });

        Call<List<Consulta>> consultasCall = apiCall.consultasCliente(cpf, senha);
        consultasCall.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if(response.isSuccessful())
                    binding.numeroConsultas.setText(Integer.toString(response.body().size()));
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {

            }
        });

        Call<List<Cliente>> call = apiCall.todosClientes(cpf, senha);
        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if(response.isSuccessful()) {
                    binding.totalClientes.setText(Integer.toString(response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {

            }
        });
    }

    public void carregarClientes(){
        configurarRetrofit();
        System.out.println("\n\n\n\n\n\nteste\n"+(cpfsClie.get(0)));
        Call<Cliente> call1 = apiCall.findClienteByCpfCliente(cpfsClie.get(0), cpf, senha);
        clientes = new ArrayList<Cliente>();
        call1.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                if(response.isSuccessful())
                    clientes.add(response.body());
                clientes.add(response.body());
                clientes.add(response.body());
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {

            }
        });
//        inicializarListagem();
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