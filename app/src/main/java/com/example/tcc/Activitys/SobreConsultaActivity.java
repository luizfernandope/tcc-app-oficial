package com.example.tcc.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
import com.example.tcc.databinding.ActivitySobreConsultaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
@RequiresApi(api = Build.VERSION_CODES.O)
public class SobreConsultaActivity extends AppCompatActivity {

    private ActivitySobreConsultaBinding binding;
    private Integer idConsulta = 0;
    String cpf, senha;
    APICall apiCall;
    Integer idTratamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySobreConsultaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idConsulta = getIntent().getIntExtra("idConsulta", 0);
        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");

        formatarTextos();

        binding.btnSobreConsulta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SobreConsultaActivity.this, SobreTratamentoActivity.class);
                intent.putExtra("idTratamento", idTratamento);
                intent.putExtra("cpf", cpf);
                intent.putExtra("senha", senha);
                startActivity(intent);
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
        Call<Consulta> consultaCall = apiCall.pegarConsulta(idConsulta, cpf, senha);
        consultaCall.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.code() == 200){
                    String endereco = response.body().getClinica().getRua() + ", " + response.body().getClinica().getNumero() + " - " + response.body().getClinica().getBairro();
                    binding.tvEndereco.setText(endereco);
                    binding.tvData.setText(response.body().getDatahora());
                    if(response.body().getSituacao()=="aguardando confirmação");
                        binding.tvSituacao.setTextColor(Color.parseColor("#eead2d"));
                    if(response.body().getSituacao()=="pendentes")
                        binding.tvSituacao.setTextColor(Color.DKGRAY);
                    if(response.body().getSituacao()=="realizadas")
                        binding.tvSituacao.setTextColor(Color.GREEN);
                    if(response.body().getSituacao()=="canceladas")
                        binding.tvSituacao.setTextColor(Color.RED);
                    if(response.body().getSituacao()=="finalizado")
                        binding.tvSituacao.setTextColor(Color.GREEN);

                    binding.tvSituacao.setText(response.body().getSituacao());
                    binding.nomeTratameno.setText(response.body().getServico().getNome());
                    String valorFormatado = String.format("%.2f", response.body().getValor());
                    binding.tvValor.setText("R$ "+ valorFormatado);
                    binding.tvSessoes.setText(response.body().getSessaoAtual() + " de " + response.body().getServico().getSessoes() + " sessões");
                    binding.telefone.setText(binding.telefone.getText() + response.body().getClinica().getTelefone());
                    binding.whatssap.setText(binding.whatssap.getText() + response.body().getClinica().getWhatsapp());

                    idTratamento = response.body().getServico().getId();
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