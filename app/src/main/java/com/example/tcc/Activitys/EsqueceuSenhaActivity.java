package com.example.tcc.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Interfaces.EnvioEmailFastthoot;
import com.example.tcc.Models.Cliente;
import com.example.tcc.R;
import com.example.tcc.Validacoes.ValidacoesCadastro;
import com.example.tcc.databinding.ActivityEsqueceuSenhaBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.santalu.maskara.widget.MaskEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EsqueceuSenhaActivity extends AppCompatActivity {


    ActivityEsqueceuSenhaBinding binding;
    String cpf, email, telefone;
    EditText maskEmail;
    MaskEditText maskCpf, maskTelefone;
    Button btnVoltar;
    TextView tvErro;

    private APICall apiCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        maskCpf = findViewById(R.id.etMaskCpf);
        maskTelefone = findViewById(R.id.etTelefoneMask);
        maskEmail = findViewById(R.id.etEmailMask);
        tvErro = findViewById(R.id.tvMensagemError);

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void recuperarSenha(View view){

        cpf = maskCpf.getUnMasked();
        email = maskEmail.getText().toString();
        telefone = maskTelefone.getUnMasked();
        boolean ehCpf = ValidacoesCadastro.isCPF(cpf);
        boolean ehEmail = ValidacoesCadastro.validarEmail(email);

        System.out.println("\n\nCPF -->" + cpf + "- email:" + email + "- Telefone:" + telefone);
        if(ehCpf && ehEmail){
            configurarRetrofit();
            Call<Cliente> existe = apiCall.findByCpfEmailAndTelefone(cpf, email, telefone);
            existe.enqueue(new Callback<Cliente>() {
                @Override
                public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                    if(response.code() == 200){
                        System.out.println("FOi");
                        String emailTexto = "Sua senha: " + response.body().getUsuario().getSenha();
                        EnvioEmailFastthoot envioEmail = new EnvioEmailFastthoot("Sua senha FastThoot", email, emailTexto);
                        Toast.makeText(EsqueceuSenhaActivity.this, "Senha enviada para seu email.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        System.out.println("Erro");
                        System.out.println("\n\n" + response.errorBody());
                        System.out.println("\n\nCode -- " + response.code());
                        tvErro.setVisibility(View.VISIBLE);
                        tvErro.setText("Erro!Você ainda não possui cadastro no sistema. Considere cadastrar-se");
                    }
                }

                @Override
                public void onFailure(Call<Cliente> call, Throwable t) {

                }
            });
        }else{
            if (!ehCpf){
                System.out.println("Erro");
                tvErro.setVisibility(View.VISIBLE);
                tvErro.setText("Erro! CPF inválido");
            }

            if (!ehEmail){
                System.out.println("Erro");
                tvErro.setVisibility(View.VISIBLE);
                tvErro.setText("Erro! Email inválido");
            }
        }
    }

    int gerarCodigoConfirmacao(int maximo, int minimo){

        // Obtendo número aleatório a partir da config. do tempo em milisegundos do sistema.
        Calendar lCDateTime = Calendar.getInstance();
        return (int)(lCDateTime.getTimeInMillis() % (maximo - minimo + 1) + minimo);
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
}