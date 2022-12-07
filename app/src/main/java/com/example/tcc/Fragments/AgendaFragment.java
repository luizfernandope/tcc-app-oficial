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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tcc.Adapters.ConsultaAdapter;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.R;
import com.example.tcc.databinding.FragmentAgendaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private Boolean cliente;
    LinearLayout layoutBtnFiltros;
    int sizeListaDeBotoesFiltro, btnFiltroAtual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAgendaBinding.inflate(inflater, container, false);
        //pegando o usuario e senha da activity main
        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");
        cliente = bundle.getBoolean("cliente", true);
        layoutBtnFiltros = binding.layoutBtnFiltros;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        consultas = new ArrayList<Consulta>();
        configurarRetrofit();
        if(!cliente)
            binding.tituloPagina.setText("horarios marcados");
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

        // pega todos os botões de filtro que estão dentro do linearLayout.
        List<View> listaDeBotoesFiltro =  layoutBtnFiltros.getTouchables();

        String filtro = ((Button) listaDeBotoesFiltro.get(0)).getText().toString();
        sizeListaDeBotoesFiltro = listaDeBotoesFiltro.size();

        // Click Listener dos botões de filtro.
        for (int i = 0; i < sizeListaDeBotoesFiltro; i++){
            listaDeBotoesFiltro.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Loop para obter qual é o índice do botão clicado dentro da lista de botões (listaDeBotoesFiltro).
                    // Precisamos do índice do btn Clicado para posteriormente percorrer o array e desabilitar as cores de todos os outros q n foram clicado.
                    for (int i = 0; i < sizeListaDeBotoesFiltro; i++){
                        System.out.println("Clicado ID --> " + view.getId() + "Posição ID --> " + listaDeBotoesFiltro.get(i).getId());
                        // Se o btn clicado for o botao equivalente na lista de botoes.
                        if(((Button) view) == ((Button) listaDeBotoesFiltro.get(i))) {
                            System.out.println("\nI -->" + i);
                            btnFiltroAtual = i;
                        }
                    }

                    adapter.notifyDataSetChanged();
                    String filtro = ((Button) view).getText().toString().toLowerCase(Locale.ROOT);
                    if(filtro.matches("aguardando confirmação")){
                        filtro = "Aguardando confirmação";
                    }

                    System.out.println("\n\nFiltro --> " + filtro);
                    ArrayList<Consulta> consultasFiltrados = new ArrayList<>();
                    for(Consulta c: consultas){
                        if(filtro.matches("todos")){
                            consultasFiltrados.add(c);
                        }else if(c.getSituacao().matches(filtro)){
                            System.out.println("\n\nFiltragem de tipos.");
                            consultasFiltrados.add(c);
                        }
                    }

                    if (consultasFiltrados.isEmpty()) {
                        Toast.makeText(binding.getRoot().getContext(), "Busca sem resultados", Toast.LENGTH_SHORT).show();
                        //exibe nenhum item
                        adapter = new ConsultaAdapter(getContext(), consultasFiltrados, cpf, senha, cliente);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        //exibe os itens
                        adapter = new ConsultaAdapter(getContext(), consultasFiltrados, cpf, senha, cliente);
                        recyclerView.setAdapter(adapter);
                    }

                    // Alterando cores de botao, de acordo se ele foi clicado ou não.
                    for (int j = 0; j < sizeListaDeBotoesFiltro; j++){
                        System.out.println("J --> " + j + " - Atual --> " + btnFiltroAtual);
                        if (j != btnFiltroAtual){
                            listaDeBotoesFiltro.get(j).setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                            ((Button)listaDeBotoesFiltro.get(j)).setTextColor(getContext().getResources().getColorStateList(R.color.black));
                        } else{
                            ((Button) view).setBackgroundTintList(getContext().getResources().getColorStateList(R.color.azul));
                            ((Button) view).setTextColor(getContext().getResources().getColorStateList(R.color.white));
                        }
                    }
                }
            });
        }
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
        recyclerView = binding.recyclerViewAgenda;
        recyclerView.setHasFixedSize(true);//da mais desempenho na listagem
        adapter = new ConsultaAdapter(binding.getRoot().getContext(), consultas, cpf, senha, cliente);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}