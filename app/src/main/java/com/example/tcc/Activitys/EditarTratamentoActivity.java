package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
import com.example.tcc.databinding.ActivityEditarTratamentoBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditarTratamentoActivity extends AppCompatActivity {

    private ActivityEditarTratamentoBinding binding;
    private Integer idTratamento;
    private APICall apiCall;
    private String cpf, senha;
    Tratamento tratamento = null;
    boolean rodou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarTratamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idTratamento = getIntent().getIntExtra("idTratamento", 0);
        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");

        if(idTratamento == 0) {
            binding.boxNomeTratamento.setVisibility(View.VISIBLE);
            binding.tvNomeTratamento.setText("Novo serviço");
            binding.tvSubtitulo.setText("Criação de tratamento");
            binding.btnSalvar.setText("Cadastrar serviço");
        }
        else
            formatarTextos();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.array_tiposTratamentos, R.layout.color_spinner_layout); // estilo do texto do spinner
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);// estilo das opcoes do spinner
        binding.spinnerClinicas.setAdapter(adapter);

        binding.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idTratamento != 0)
                    atualizarTratamento();
                else
                    cadastrarTratamento();


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

    public void formatarTextos(){
        configurarRetrofit();
        String tipo[] = getResources().getStringArray(R.array.array_tiposTratamentos);
        Call<Tratamento> findTratameto = apiCall.findTratameto(idTratamento);
        findTratameto.enqueue(new Callback<Tratamento>() {
            @Override
            public void onResponse(Call<Tratamento> call, Response<Tratamento> response) {
                if(response.code() == 200)
                {
                    //preenche com os valores
                    binding.tvNomeTratamento.setText(response.body().getNome());
                    binding.etDescricao.setText(response.body().getDescricao());
                    binding.etPqFazer.setText(response.body().getPqFazer());
                    binding.etHoras.setText(response.body().getTempo());
                    binding.etQtdSessoes.setText(response.body().getSessoes().toString());
                    String valorFormatado = String.format("%.2f", response.body().getValor());
                    binding.etPreco.setText(valorFormatado);
                    for(int i = tipo.length-1; i >=0; i--){
                        if(tipo[i].contains(response.body().getTipo()))
                            binding.spinnerClinicas.setSelection(i);
                    }
                    tratamento = response.body();
                }
            }
            @Override
            public void onFailure(Call<Tratamento> call, Throwable t) {
                Toast.makeText(EditarTratamentoActivity.this, "erro de requisicao", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void atualizarTratamento(){
        configurarRetrofit();
        tratamento.setDescricao(binding.etDescricao.getText().toString());
        tratamento.setPqFazer(binding.etPqFazer.getText().toString());
        tratamento.setTempo(binding.etHoras.getText().toString());
        tratamento.setValor(Double.parseDouble(binding.etPreco.getText().toString().replace(",", ".")));
        tratamento.setSessoes(Integer.parseInt(binding.etQtdSessoes.getText().toString()));
        tratamento.setTipo(binding.spinnerClinicas.getSelectedItem().toString());
        Call<Tratamento> tratamentoCall = apiCall.atualizarTratamento(idTratamento, cpf, senha, tratamento);
        tratamentoCall.enqueue(new Callback<Tratamento>() {
            @Override
            public void onResponse(Call<Tratamento> call, Response<Tratamento> response) {
                if(response.isSuccessful()){
                    Toast.makeText(EditarTratamentoActivity.this, "atualizou tratamento com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(EditarTratamentoActivity.this, "erro ao salvar edições!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tratamento> call, Throwable t) {

            }
        });
    }

    public void cadastrarTratamento(){
        configurarRetrofit();
        tratamento = new Tratamento();
        tratamento.setDescricao(binding.etDescricao.getText().toString());
        tratamento.setPqFazer(binding.etPqFazer.getText().toString());
        tratamento.setTempo(binding.etHoras.getText().toString());
        tratamento.setValor(Double.parseDouble(binding.etPreco.getText().toString().replace(",", ".")));
        tratamento.setSessoes(Integer.parseInt(binding.etQtdSessoes.getText().toString()));
        tratamento.setTipo(binding.spinnerClinicas.getSelectedItem().toString());
        Call<Optional<Tratamento>> t = apiCall.cadastrarTratamento(cpf, senha, tratamento);
        t.enqueue(new Callback<Optional<Tratamento>>() {
            @Override
            public void onResponse(Call<Optional<Tratamento>> call, Response<Optional<Tratamento>> response) {
                if(response.code() == 201)
                {
                    Toast.makeText(EditarTratamentoActivity.this, "novo tratamento criado", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditarTratamentoActivity.this, "Falha ao criar tratamento", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(EditarTratamentoActivity.this, Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Optional<Tratamento>> call, Throwable t) {

            }
        });
    }

    public void voltarSobreTratamento(View view){
        onBackPressed();
    }
}