package com.example.tcc.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.MainActivity;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Usuario;
import com.example.tcc.R;
import com.example.tcc.databinding.ActivityLoginBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

//        mandarEmail("app teste", "pcgamerluizinho@gmail.com", "teste app...");

        binding.btnEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("\n\nCliclou\n\n");
                logar();
            }
        });

        binding.naoPossuiContaLogin.setText(Html.fromHtml(getString(R.string.naoPossuiConta)));

        binding.naoPossuiContaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaCadastro();
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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("cpf", response.body().getCpf());
                    intent.putExtra("senha", response.body().getSenha());
                    intent.putExtra("cliente", response.body().isCliente());
                    startActivity(intent);
                    finish();//fecha activity login
                    if(binding.switchLogarAutomatico.isChecked())
                        loginAutomatico(response.body().getCpf(), response.body().getSenha(), response.body().isCliente());
                }
                else
                    Toast.makeText(LoginActivity.this, "usuario ou senha inv??lido(s).", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                    System.out.println("\n\nFalhou o enqueve\n\n" +t.getMessage());

            }
        });
    }

    public void loginAutomatico(String cpf,String senha, Boolean ehCliente){

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("cpf", cpf);
        editor.putString("senha", senha);
        editor.putBoolean("cliente", ehCliente);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String cpf = sharedPref.getString("cpf", null);
        String senha = sharedPref.getString("senha", null);
        boolean cliente = sharedPref.getBoolean("cliente", true);

        if(cpf != null && !senha.isEmpty()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("cpf", cpf);
            intent.putExtra("senha", senha);
            intent.putExtra("cliente", cliente);
            startActivity(intent);
            finish();//fecha activity login
        }
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
        api = retrofit.create(APICall.class);

    }


    void mandarEmail(String assunto, String emailTo, String textoMensagem){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String value = "{\r\"personalizations\": [\r {\r\"to\": [\r {\r\"email\": \""+ emailTo +"\"\r}\r],\r\"subject\": \""+ assunto +"\"\r}\r],\r\"from\": {\r\"email\": \"tccmewtwo@example.com\"\r},\r\"content\": [\r{\r\"type\": \"text/plain\",\r\"value\": \""+ textoMensagem +"\"\r}\r]\r}";
        RequestBody body = RequestBody.create(mediaType, value);
        Request request = new Request.Builder()
        .url("https://rapidprod-sendgrid-v1.p.rapidapi.com/mail/send")
        .post(body)
        .addHeader("content-type", "application/json")
        .addHeader("X-RapidAPI-Key", "8bd3ffbbcfmsh868f33f6917af53p1147e2jsn999848e2aec1")
        .addHeader("X-RapidAPI-Host", "rapidprod-sendgrid-v1.p.rapidapi.com")
        .build();

        okhttp3.Call response = client.newCall(request);
        response.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });
    }

    public void irParaCadastro(){
        Intent intent = new Intent(LoginActivity.this, Cadastro1Activity.class);
        startActivity(intent);
    }

    public void irParaEsqueceuSenha(View view){
        Intent intent = new Intent(LoginActivity.this, EsqueceuSenhaActivity.class);
        startActivity(intent);
    }
}