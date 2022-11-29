package com.example.tcc.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.MainActivity;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Usuario;
import com.example.tcc.databinding.ActivityLoginBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
@RequiresApi(api = Build.VERSION_CODES.O)
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private APICall api;
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("\n\nCliclou\n\n");
                logar();
            }
        });
    }

    public void logar(){
        configurarRetrofit();

        Call<Usuario> login = api.login(binding.etMaskCPFLogin.getUnMasked(), binding.etSenhaLogin.getText().toString());
        login.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, response.body().getCpf()+"\n"+response.body().getSenha()+"\n"+response.body().getDt_admissao(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("cpf", response.body().getCpf());
                    intent.putExtra("senha", response.body().getSenha());
                    startActivity(intent);
                    finish();//fecha activity login
                }
                else
                    Toast.makeText(LoginActivity.this, "usuario ou senha inválido(s).", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                    System.out.println("\n\nFalhou o enqueve\n\n" +t.getMessage());

            }
        });
    }

    void configurarRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinica-tcc-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Instacia da interface;
        api = retrofit.create(APICall.class);

    }

}