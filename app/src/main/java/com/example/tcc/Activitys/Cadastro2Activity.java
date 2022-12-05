package com.example.tcc.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.tcc.Interfaces.APICall;
import com.example.tcc.MainActivity;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Usuario;
import com.example.tcc.R;
import com.example.tcc.Validacoes.ValidacoesCadastro;
import com.example.tcc.databinding.ActivityCadastro2Binding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
@RequiresApi(api = Build.VERSION_CODES.O)
public class Cadastro2Activity extends AppCompatActivity {

    private ActivityCadastro2Binding binding;
    private String cpf;
    private APICall api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastro2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cpf = getIntent().getStringExtra("cpf");

        binding.etMaskDataNasciCadastro2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.etMaskDataNasciCadastro2.getUnMasked().length() == 8) {
                    String data = binding.etMaskDataNasciCadastro2.getUnMasked();
                    String mensagem = ValidacoesCadastro.validarDataNasi(data);
                    if (mensagem == null)
                        binding.dataNasciContainerCadastro2.setError(null);
                    else
                        binding.dataNasciContainerCadastro2.setError(mensagem);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.etSenhaCadastro2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mensagem = ValidacoesCadastro.validarSenha(binding.etSenhaCadastro2.getText().toString());
                if(mensagem==null)
                    binding.senhaContainerCadastro2.setError(null);
                else
                    binding.senhaContainerCadastro2.setError(mensagem);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.checkMasculino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.checkMasculino.isChecked())
                    binding.checkFeminino.setChecked(false);
            }
        });

        binding.checkFeminino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.checkFeminino.isChecked())
                    binding.checkMasculino.setChecked(false);
            }
        });

        binding.btnCadastrarCadastro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.checkMasculino.isChecked() && !binding.checkFeminino.isChecked())
                {
                    Toast.makeText(Cadastro2Activity.this, "Preencha seu sexo", Toast.LENGTH_SHORT).show();
                }
                else if (binding.checkBoxTermosCadastro2.isChecked()){
                    if(camposSaoValidos()){
                        Usuario newUser = new Usuario();
                        newUser.setCpf(cpf);
                        newUser.setSenha(binding.etSenhaCadastro2.getText().toString());

                        configurarRetrofit();

                        Call<Usuario> cadastrarUsuario = api.cadastrarUsuario(newUser);
                        cadastrarUsuario.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                                    System.out.println("\n\nusuario cadastrado");
                                    Cliente newClie = new Cliente();
                                    newClie.setNome(binding.etNome.getText().toString());
                                    newClie.setEmail(binding.etMaskEmailCadastro2.getText().toString());
                                    newClie.setTelefone(binding.etMaskTelefoneCadastro2.getUnMasked());
                                    newClie.setDtNascimento(binding.etMaskDataNasciCadastro2.getMasked());
                                    newClie.setUsuario(newUser);
                                    if(binding.checkMasculino.isChecked())
                                        newClie.setSexo("Masculino");
                                    else if(binding.checkFeminino.isChecked())
                                        newClie.setSexo("Feminino");
                                    System.out.println("\n\ncliente criado");
                                    Call<Cliente> cadastrarCliente = api.cadastrarCliente(newClie);
                                    cadastrarCliente.enqueue(new Callback<Cliente>() {

                                        @Override
                                        public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                                            System.out.println("\n\ncliente requisicao");
                                            if(response.code() == 201){
                                                System.out.println("\n\ncliente cadastrado");
                                                Intent intent = new Intent(Cadastro2Activity.this, MainActivity.class);
                                                intent.putExtra("cpf", response.body().getUsuario().getCpf());
                                                intent.putExtra("senha", response.body().getUsuario().getSenha());
                                                startActivity(intent);
                                                finish();finish();finish(); //queria fechar todas activitys anteriores
                                            }
                                            else
                                                Toast.makeText(Cadastro2Activity.this, "erro ao cadastrar cliente : "+Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Cliente> call, Throwable t) {
                                            Toast.makeText(Cadastro2Activity.this, "falha ao requisitar o cadastro de cliente", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                                Toast.makeText(Cadastro2Activity.this, "falha ao requisitar o cadastro de usuario", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(Cadastro2Activity.this, "preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Cadastro2Activity.this, "termos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean camposSaoValidos(){
        if(ValidacoesCadastro.validarEmail(binding.etMaskEmailCadastro2.getText().toString()))
            binding.emailContainerCadastro2.setError(null);
        else
            binding.emailContainerCadastro2.setError("email inválido!");

        if(!binding.etNome.getText().toString().isEmpty() || !binding.etNome.getText().toString().trim().isEmpty())
            if(!binding.etMaskTelefoneCadastro2.getUnMasked().isEmpty() || !binding.etMaskTelefoneCadastro2.getUnMasked().trim().isEmpty())
                if(!binding.etMaskEmailCadastro2.getText().toString().isEmpty() || !binding.etMaskEmailCadastro2.getText().toString().trim().isEmpty())
                    if(!binding.etMaskDataNasciCadastro2.getUnMasked().isEmpty() || !binding.etMaskDataNasciCadastro2.getUnMasked().trim().isEmpty())
                        if(!binding.etSenhaCadastro2.getText().toString().isEmpty() || !binding.etSenhaCadastro2.getText().toString().trim().isEmpty())
                        {
                            if(binding.emailContainerCadastro2.getError() == null && binding.dataNasciContainerCadastro2.getError() == null && binding.senhaContainerCadastro2.getError() == null)
                            {
                                if(binding.etMaskTelefoneCadastro2.getUnMasked().length() == 11) {
                                    return true;
                                }
                            }
                        }

        return false;
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
}