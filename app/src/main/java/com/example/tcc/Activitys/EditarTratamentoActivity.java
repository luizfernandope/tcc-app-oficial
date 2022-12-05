package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
import com.example.tcc.databinding.ActivityEditarTratamentoBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditarTratamentoActivity extends AppCompatActivity {

    private ActivityEditarTratamentoBinding binding;
    private Integer idTratamento;
    private APICall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarTratamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idTratamento = getIntent().getIntExtra("idTratamento", 0);
        if(idTratamento == 0) {
            binding.boxNomeTratamento.setVisibility(View.VISIBLE);
            binding.tvNomeTratamento.setText("Novo serviço");
            binding.tvSubtitulo.setText("Criação de tratamento");
            binding.btnSalvar.setText("Cadastrar serviço");
        }
        else
            formatarTextos();
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
                    binding.etPqFazer.setText(response.body().getDescricao());
                    binding.etHoras.setText(response.body().getTempo());
                    binding.etQtdSessoes.setText(response.body().getId().toString());
                    binding.etPreco.setText(response.body().getValor().toString());
                    for(int i = tipo.length-1; i >=0; i--){
                        if(tipo[i].contains(response.body().getTipo()))
                            binding.spinnerClinicas.setSelection(i);
                    }
                }
            }
            @Override
            public void onFailure(Call<Tratamento> call, Throwable t) {
                Toast.makeText(EditarTratamentoActivity.this, "erro de requisicao", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void voltarSobreTratamento(View view){
        onBackPressed();
    }
}