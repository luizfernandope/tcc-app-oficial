package com.example.tcc.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.Activitys.LoginActivity;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Interfaces.EnvioEmailFastthoot;
import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Funcionario;
import com.example.tcc.Models.Usuario;
import com.example.tcc.R;
import com.example.tcc.Validacoes.ValidacoesCadastro;
import com.example.tcc.databinding.FragmentPerfilBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private APICall apiCall;

    private String cpf, senha, email;
    private Cliente cliente;
    private Funcionario funcionario;
    boolean ehFuncionario = false;
    String novaSenha = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        //pegando o usuario e senha da activity main
        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnMudarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarSenha();
            }
        });

        if(cliente == null)
            carregarCliente();
        if(funcionario == null)
            carregarFuncionario();


        binding.btnEditarPefil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.btnEditarPefil.getText().toString().contains("editar perfil"))
                {
                    binding.btnEditarPefil.setBackgroundResource(R.drawable.bg_curvo12);
                    binding.btnEditarPefil.setText("salvar");
                    if(cliente != null) {
                        binding.etNome.setEnabled(true);
                        binding.emailEditTextPerfil.setEnabled(true);
                        binding.dataNasciEditTextPerfil.setEnabled(true);
                        binding.sexoEditTextPerfil.setEnabled(true);
                    }
                    else{
                        binding.etNome.setEnabled(true);
                        binding.emailEditTextPerfil.setEnabled(true);
                    }
                }
                else{
                    atualizarPerfil();
                }
            }
        });

        binding.btnDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deslogar();
            }
        });

        binding.btnTermosPoliticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaPolitica();
            }
        });

        binding.btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaPerguntas();
            }
        });
    }

    public void carregarCliente(){
        configurarRetrofit();
        Call<Cliente> pegarCliente = apiCall.findCliente(cpf, senha);
        pegarCliente.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful())
                {
                    binding.etNome.setText(response.body().getNome());
                    binding.etMaskCPF.setText(response.body().getUsuario().getCpf());
                    binding.dataNasciEditTextPerfil.setText(response.body().getDtNascimento());
                    binding.sexoEditTextPerfil.setText(response.body().getSexo());
                    binding.emailEditTextPerfil.setText(response.body().getEmail());
                    cliente = response.body();
                    email = response.body().getEmail();
                }
                else{
                    ehFuncionario = true;
                    carregarFuncionario();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
            }
        });
    }

    public void carregarFuncionario(){
        configurarRetrofit();
        Call<Funcionario> pegarFuncionario = apiCall.findFuncionario(cpf, senha);
        pegarFuncionario.enqueue(new Callback<Funcionario>() {
            @Override
            public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                if(response.isSuccessful()){
                    binding.etNome.setText(response.body().getNome());
                    binding.etMaskCPF.setText(response.body().getUsuario().getCpf());
                    binding.dataNasciEditTextPerfil.setVisibility(View.GONE);
                    binding.dataNasciContainerPerfil.setVisibility(View.GONE);
                    binding.sexoEditTextPerfil.setText(response.body().getCargo());
                    binding.emailEditTextPerfil.setText(response.body().getEmail());
                    funcionario = response.body();
                }
            }

            @Override
            public void onFailure(Call<Funcionario> call, Throwable t) {

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

    public void atualizarPerfil(){
        configurarRetrofit();
        if(!ehFuncionario) {
            cliente.setNome(binding.etNome.getText().toString());
            cliente.setDtNascimento(binding.dataNasciEditTextPerfil.getText().toString());
            cliente.setSexo(binding.sexoEditTextPerfil.getText().toString());
            cliente.setEmail(binding.emailEditTextPerfil.getText().toString());
            Call<Cliente> clienteCall = apiCall.atualizarCliente(cliente);
            clienteCall.enqueue(new Callback<Cliente>() {
                @Override
                public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "Atualização de perfil bem sucedida", Toast.LENGTH_SHORT).show();
                        binding.btnEditarPefil.setBackgroundResource(R.drawable.borda_black);
                        binding.btnEditarPefil.setText("editar perfil");

                        binding.etNome.setEnabled(false);
                        binding.emailEditTextPerfil.setEnabled(false);
                        binding.dataNasciEditTextPerfil.setEnabled(false);
                        binding.sexoEditTextPerfil.setEnabled(false);

                    } else
                        Toast.makeText(getContext(), "Falha ao atualizar perfil!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Cliente> call, Throwable t) {
                    Toast.makeText(getContext(), "Falha requisicao de atualizacao", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (ehFuncionario){
            funcionario.setNome(binding.etNome.getText().toString());
            funcionario.setEmail(binding.emailEditTextPerfil.getText().toString());
            Call<Funcionario> funcionarioCall = apiCall.atualizarFuncionario(funcionario);
            funcionarioCall.enqueue(new Callback<Funcionario>() {
                @Override
                public void onResponse(Call<Funcionario> call, Response<Funcionario> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getContext(), "Atualização de perfil bem sucedida", Toast.LENGTH_SHORT).show();
                        binding.btnEditarPefil.setBackgroundResource(R.drawable.borda_black);
                        binding.btnEditarPefil.setText("editar perfil");
                        binding.etNome.setEnabled(false);
                        binding.emailEditTextPerfil.setEnabled(false);
                    }
                    else
                        Toast.makeText(getContext(), "Falha ao atualizar perfil!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Funcionario> call, Throwable t) {
                    Toast.makeText(getContext(), "Falha requisicao de atualizacao", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void deslogar(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);

        //Tirando o loginAutomatico
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("cpf", null);
        editor.putString("senha", null);
        editor.putBoolean("cliente", true);
        editor.apply();
        getActivity().finish();
    }

    void mudarSenha(){

        // Mudando senha por email.
        LayoutInflater inflater = getLayoutInflater();
        View dialogConfirmar = inflater.inflate(R.layout.confirma_mudar_senha, null);

        final Button btnSim = (Button) dialogConfirmar.findViewById(R.id.btnSim);
        final Button btnNao = (Button) dialogConfirmar.findViewById(R.id.btnNao);
        final TextView tvMensagem = (TextView) dialogConfirmar.findViewById(R.id.tvMensagem);

        AlertDialog.Builder popupConfirmaMudarSenha = new AlertDialog.Builder(getContext());

        popupConfirmaMudarSenha.setView(dialogConfirmar);
        popupConfirmaMudarSenha.setCancelable(false);
        tvMensagem.setText("Deseja realmente alterar sua senha?");

        final AlertDialog showConfSenha = popupConfirmaMudarSenha.show();

        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfSenha.dismiss();
            }
        });

        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfSenha.dismiss();
                View dialogView = inflater.inflate(R.layout.popup_email_layou, null);

                final TextView mensagemPopup = (TextView) dialogView.findViewById(R.id.mensagemPopup);
                final EditText etCodigo =  (EditText) dialogView.findViewById(R.id.etCodigo);
                final Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
                final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
                AlertDialog.Builder popupCodigoConfirmacao = new AlertDialog.Builder(getContext());

                popupCodigoConfirmacao.setView(dialogView);
                popupCodigoConfirmacao.setCancelable(false);
                mensagemPopup.setText(getString(R.string.mensagemPopup) + email);
                // --------------------

                // gera número no intervalo de 1500 a 5000.
                int codigo = gerarCodigoConfirmacao(5000, 1500);
                System.out.println("\n\nCódigo de confirmação: " + codigo);

                String emailTexto = getString(R.string.email_texto) + codigo;

                EnvioEmailFastthoot envioEmail = new EnvioEmailFastthoot(getString(R.string.email_assunto_trocarSenha), email, emailTexto);
                final AlertDialog showPopupCodigo = popupCodigoConfirmacao.show();

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupCodigo.dismiss();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String codigoInserido = etCodigo.getText().toString();

                        // se usuário digitou código incorreto.
                        if (!codigoInserido.matches(String.valueOf(codigo))){

                            System.out.println("\nValor incorreto!");
                            System.out.println("\nvalor inserido: " + codigoInserido + " - Codigo correto: " + String.valueOf(codigo) + "-");
                            dialogView.findViewById(R.id.tvInvalido).setVisibility(View.VISIBLE);
                        } else {
                            System.out.println("\n\nCódigo confirmado!");

                            showPopupCodigo.dismiss();
                            View dialogNovaSenha = inflater.inflate(R.layout.popup_novasenha, null);
                            AlertDialog.Builder popupNovaSenha = new AlertDialog.Builder(getContext());

                            EditText tvNovaSenha = (EditText) dialogNovaSenha.findViewById(R.id.etNovaSenha);
                            TextView tvErro = (TextView) dialogNovaSenha.findViewById(R.id.tvErroSenha);
                            Button btnAlterarSenha = dialogNovaSenha.findViewById(R.id.btnPronto);
                            Button btnCancelarAlteramento = dialogNovaSenha.findViewById(R.id.btnCancelarUpdate);
                            popupNovaSenha.setView(dialogNovaSenha);


                            final AlertDialog show = popupNovaSenha.show();

                            btnCancelarAlteramento.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    show.dismiss();
                                }
                            });
                            btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    ValidacoesCadastro validacoesCadastro = new ValidacoesCadastro();
                                    // Operador ternario (se senha é válida --> variavel true. senão variavel false).

                                    novaSenha = tvNovaSenha.getText().toString();
                                    boolean senhaEhValida = validacoesCadastro.validarSenha(novaSenha) == null? true: false;

                                    if (senhaEhValida){
                                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                        configurarRetrofit();
                                        Usuario user = new Usuario();

                                        user.setSenha(novaSenha);
                                        user.setCpf(cpf);
                                        Call<Usuario> usuarioCall = apiCall.atualizarSenhaUsuario(user);
                                        usuarioCall.enqueue(new Callback<Usuario>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                                if (response.isSuccessful()){
                                                    Toast.makeText(getContext(), "Senha atualizada com sucesso!.", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                    show.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Usuario> call, Throwable t) {
                                                Toast.makeText(getContext(), "Erro ao atualizar senha.", Toast.LENGTH_SHORT).show();
                                                t.printStackTrace();
                                            }
                                        });
                                    } else{
                                        tvErro.setVisibility(View.VISIBLE);
                                        tvErro.setText(validacoesCadastro.validarSenha(novaSenha));
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    int gerarCodigoConfirmacao(int maximo, int minimo){

        // Obtendo número aleatório a partir da config. do tempo em milisegundos do sistema.
        Calendar lCDateTime = Calendar.getInstance();
        return (int)(lCDateTime.getTimeInMillis() % (maximo - minimo + 1) + minimo);
    }

    public void abrirTelaPerguntas(){
        // Abrir alertDialog com perguntas.
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_perguntas_frequentes, null);

        AlertDialog.Builder alertTermos = new AlertDialog.Builder(getContext());

        alertTermos.setView(dialogView);
        TextView tvPerguntas =  dialogView.findViewById(R.id.tvPerguntas);
        Button btnFecharPerguntas = dialogView.findViewById(R.id.btnFecharPerguntas);

        tvPerguntas.setText(Html.fromHtml(getString(R.string.perguntas_frequentes)));

        // Habilita o texto a ser 'Scrollável'.
        tvPerguntas.setMovementMethod(new ScrollingMovementMethod());
        final AlertDialog show = alertTermos.show();

        btnFecharPerguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });
    }

    public void abrirTelaPolitica(){
        // Abrir alertDialog com termos.
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_termos, null);

        AlertDialog.Builder alertTermos = new AlertDialog.Builder(getContext());

        alertTermos.setView(dialogView);

        TextView tvTextoAtual =  dialogView.findViewById(R.id.tvTextoAtual);
        Button btnFecharTermos = dialogView.findViewById(R.id.btnFecharTermos);
        Button btnTabTermos = dialogView.findViewById(R.id.btnTabTermos);
        Button btnTabPolitica = dialogView.findViewById(R.id.btnTabPolitica);


        tvTextoAtual.setText(Html.fromHtml(getString(R.string.termos_uso)));


        btnTabTermos.setBackgroundTintList(getResources().getColorStateList(R.color.azul));
        btnTabPolitica.setBackgroundTintList(getResources().getColorStateList(R.color.white));

        btnTabTermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTextoAtual.setText(Html.fromHtml(getString(R.string.termos_uso)));
                btnTabTermos.setBackgroundTintList(getResources().getColorStateList(R.color.azul));
                btnTabPolitica.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }
        });

        btnTabPolitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTextoAtual.setText(Html.fromHtml(getString(R.string.politica_privacidade)));
                btnTabTermos.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                btnTabPolitica.setBackgroundTintList(getResources().getColorStateList(R.color.azul));
            }
        });

        // Habilita o texto a ser 'Scrollável'.
        tvTextoAtual.setMovementMethod(new ScrollingMovementMethod());
        final AlertDialog show = alertTermos.show();

        btnFecharTermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });
    }
}