package com.example.tcc.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.R;
import com.example.tcc.databinding.ActivitySobreConsultaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
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
    LinearLayout linearLayout;
    int botaoAtual;
    boolean clicouSituacao = false, ehCliente = true;
    Consulta consulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySobreConsultaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idConsulta = getIntent().getIntExtra("idConsulta", 0);
        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");
        ehCliente = getIntent().getBooleanExtra("cliente", true);

        if(!ehCliente){
            binding.paraFuncionario.setVisibility(View.VISIBLE);
            binding.btnSobreConsulta.setBackgroundResource(R.drawable.borda_black);
            binding.btnSobreConsulta.setTextColor(getColor(R.color.black));
        }

        linearLayout = binding.layoutSituacoes;
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

        List<View> listaBtnSituacao = linearLayout.getTouchables();

        for(int i = 0; i<3; i++){
            listaBtnSituacao.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i = 0; i<3; i++){
                        if(((Button) view) == ((Button) listaBtnSituacao.get(i))){
                            botaoAtual = i;
                        }
                    }

                    if(botaoAtual == 0){
                        AppCompatButton button = (AppCompatButton) listaBtnSituacao.get(0);
                        AppCompatButton button2 = (AppCompatButton) listaBtnSituacao.get(1);
                        AppCompatButton button3 = (AppCompatButton) listaBtnSituacao.get(2);
                        button.setBackgroundResource(R.color.verde);
                        button2.setBackgroundResource(R.drawable.bg_curvo12);
                        button3.setBackgroundResource(R.drawable.bg_curvo12);
                        button.setTextColor(getColor(R.color.white));
                        button2.setTextColor(getColor(R.color.black));
                        button3.setTextColor(getColor(R.color.black));
                    }
                    if(botaoAtual == 1){
                        AppCompatButton button = (AppCompatButton) listaBtnSituacao.get(1);
                        AppCompatButton button2 = (AppCompatButton) listaBtnSituacao.get(0);
                        AppCompatButton button3 = (AppCompatButton) listaBtnSituacao.get(2);
                        button.setBackgroundResource(R.color.vermelho);
                        button2.setBackgroundResource(R.drawable.bg_curvo12);
                        button3.setBackgroundResource(R.drawable.bg_curvo12);
                        button.setTextColor(getColor(R.color.white));
                        button2.setTextColor(getColor(R.color.black));
                        button3.setTextColor(getColor(R.color.black));
                    }
                    if(botaoAtual == 2){
                        AppCompatButton button = (AppCompatButton) listaBtnSituacao.get(2);
                        AppCompatButton button2 = (AppCompatButton) listaBtnSituacao.get(0);
                        AppCompatButton button3 = (AppCompatButton) listaBtnSituacao.get(1);
                        button.setBackgroundResource(R.color.ciza);
                        button2.setBackgroundResource(R.drawable.bg_curvo12);
                        button3.setBackgroundResource(R.drawable.bg_curvo12);
                        button.setTextColor(getColor(R.color.white));
                        button2.setTextColor(getColor(R.color.black));
                        button3.setTextColor(getColor(R.color.black));
                    }
                    clicouSituacao = true;
                    binding.avisoSalvar.setVisibility(View.VISIBLE);
                }
            });
        }

        binding.btnSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicouSituacao == true){
                    AppCompatButton button = (AppCompatButton) listaBtnSituacao.get(botaoAtual);
                    consulta.setSituacao(button.getText().toString());
                    Call<Consulta> call = apiCall.atualizarConsulta(idConsulta, cpf, senha, consulta);
                    call.enqueue(new Callback<Consulta>() {
                        @Override
                        public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(SobreConsultaActivity.this, "atualizacao efetuada com sucesso", Toast.LENGTH_SHORT).show();
                                button.setBackgroundResource(R.drawable.bg_curvo12);
                                button.setTextColor(getColor(R.color.black));
                                clicouSituacao = false;
                                binding.avisoSalvar.setVisibility(View.GONE);
                                formatarTextos();
                            }
                        }

                        @Override
                        public void onFailure(Call<Consulta> call, Throwable t) {

                        }
                    });
                }
                else{
                    Toast.makeText(SobreConsultaActivity.this, "clique em alguma opção de situação", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnDesmarcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desmarcar();
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

                    binding.tvSituacao.setText(response.body().getSituacao());
                    binding.nomeTratameno.setText(response.body().getServico().getNome());
                    String valorFormatado = String.format("%.2f", response.body().getValor());
                    binding.tvValor.setText("R$ "+ valorFormatado);
                    binding.tvSessoes.setText(response.body().getSessaoAtual() + " de " + response.body().getServico().getSessoes() + " sessões");
                    binding.telefone.setText(binding.telefone.getText() + response.body().getClinica().getTelefone());
                    binding.whatssap.setText(binding.whatssap.getText() + response.body().getClinica().getWhatsapp());

                    binding.nomeCliente.setText(response.body().getCliente().getNome());
                    binding.emailCliente.setText("Email: "+response.body().getCliente().getEmail());
                    binding.cpfCliente.setText("CPF: "+response.body().getCliente().getUsuario().getCpf());
                    binding.sexoEdtnasciCliente.setText(response.body().getCliente().getSexo()+"\n"+response.body().getCliente().getDtNascimento());

                    idTratamento = response.body().getServico().getId();
                    consulta = response.body();

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

    public void desmarcar(){
        configurarRetrofit();
        Call<ResponseBody> call = apiCall.deletarConsulta(idConsulta, cpf, senha);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    Toast.makeText(SobreConsultaActivity.this, "Consulta desmarcada e apagada.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SobreConsultaActivity.this, "erro", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}