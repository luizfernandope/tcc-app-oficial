package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.databinding.ActivitySobreConsultaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SobreConsultaActivity extends AppCompatActivity {

    private ActivitySobreConsultaBinding binding;
    private Integer idConsulta = 0;
    String cpf, senha;
    APICall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySobreConsultaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idConsulta = getIntent().getIntExtra("idConsulta", 0);
        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");

        formatarTextos();

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

    public void formatarTextos(){
        configurarRetrofit();
        Call<Consulta> consultaCall = apiCall.pegarConsulta(idConsulta, cpf, senha);
        consultaCall.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.code() == 200){
                    String endereco = response.body().getClinica().getRua() + ", " + response.body().getClinica().getNumero() + " - " + response.body().getClinica().getBairro();
                    binding.tvEndereco.setText(endereco);
                    binding.tvData.setText(response.body().getDatahora());
                    binding.tvSituacao.setText(response.body().getSituacao());
                    binding.tvValor.setText(response.body().getValor().toString());
                    binding.tvSessoes.setText(response.body().getId().toString() + " de " + response.body().getId() + " sessões");
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {

            }
        });
    }

    public void voltarSobreConsulta(View view){
        onBackPressed();
    }
}