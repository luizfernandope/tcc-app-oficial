package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Usuario;
import com.example.tcc.databinding.ActivityLoginBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

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
        Usuario user = new Usuario();
        user.setCpf("71923695002");
        user.setSenha("senha");
        Call<Usuario> login = api.login(user);
        login.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Toast.makeText(LoginActivity.this, response.body().getDt_admissao(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("\n\nFalhou o enqueve\n\n" +call.toString());
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