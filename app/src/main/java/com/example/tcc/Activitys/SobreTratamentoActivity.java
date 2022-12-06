package com.example.tcc.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
import com.example.tcc.databinding.ActivitySobreTratamentoBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
@RequiresApi(api = Build.VERSION_CODES.O)
public class SobreTratamentoActivity extends AppCompatActivity {

    private ActivitySobreTratamentoBinding binding;
    private String cpf, senha;
    private Integer idTratamento;
    private APICall apiCall;
    Tratamento tratamento = new Tratamento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySobreTratamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");
        idTratamento = getIntent().getIntExtra("idTratamento", 0);

        formatarTextos();

    }

    public void voltarSobreTratamento(View view){
        onBackPressed();
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
        Call<Tratamento> findTratameto = apiCall.findTratameto(idTratamento);
        findTratameto.enqueue(new Callback<Tratamento>() {
            @Override
            public void onResponse(Call<Tratamento> call, Response<Tratamento> response) {
                if(response.code() == 200)
                {
                    tratamento = response.body();
                    binding.nomeTratameno.setText(response.body().getNome());
                    binding.tvDescricaoTratamento.setText(response.body().getDescricao());
                    binding.tvDuracaoTratamento.setText(response.body().getTempo() + " Hora(s).");
                    binding.tvPqFazerTratamento.setText(response.body().getPqFazer());
                    binding.tvTipoTratamento.setText(response.body().getTipo());
                    binding.tvSessoesTratamento.setText(response.body().getSessoes().toString() + " sessões");
                    String valorFormatado = String.format("%.2f", response.body().getValor());
                    binding.tvValorTratamento.setText("R$ " + valorFormatado);
                }
            }
            @Override
            public void onFailure(Call<Tratamento> call, Throwable t) {
                Toast.makeText(SobreTratamentoActivity.this, "erro de requisicao", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void irParaAgendamento(View view){
        Intent intent = new Intent(SobreTratamentoActivity.this, MarcarConsultaActivity.class);
        intent.putExtra("idTratamento", idTratamento);
        intent.putExtra("cpf", cpf);
        intent.putExtra("senha", senha);
        startActivity(intent);
    }
}