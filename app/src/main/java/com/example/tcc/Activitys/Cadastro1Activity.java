package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.MainActivity;
import com.example.tcc.Models.Usuario;
import com.example.tcc.databinding.ActivityCadastro1Binding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Cadastro1Activity extends AppCompatActivity {

    private ActivityCadastro1Binding binding;
    private APICall api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastro1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAvancarCadastro1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etMaskCPFCadastro1.getUnMasked() != "")
                    logar();
            }
        });
    }

    public void logar(){
        configurarRetrofit();

        Call<String> existe = api.existeUsuarioByCPF(binding.etMaskCPFCadastro1.getUnMasked());
        existe.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 400)//não existe
                {
                    Toast.makeText(Cadastro1Activity.this, "foi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Cadastro1Activity.this, Cadastro2Activity.class);
                    intent.putExtra("cpf", binding.etMaskCPFCadastro1.getUnMasked());
                    startActivity(intent);
                }
                else//code 200, entao existe
                    Toast.makeText(Cadastro1Activity.this, "usuario já cadastrado no sistema!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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

    public void irParaCadastro2(View view){
        Intent intent = new Intent(Cadastro1Activity.this, Cadastro2Activity.class);
        startActivity(intent);
    }
}