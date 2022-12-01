package com.example.tcc.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tcc.Dtos.ConsultaDto;
import com.example.tcc.Interfaces.APICall;
import com.example.tcc.Models.Consulta;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
import com.example.tcc.databinding.ActivityMarcarConsultaBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MarcarConsultaActivity extends AppCompatActivity {

    private ActivityMarcarConsultaBinding binding;
    private String data;
    private String dateParaApi, timeParaApi, dateTimeParaApi;
    private LocalDate horarioEscolhido = LocalDate.now();
    private Integer idTratamento = 0, idClinica; //pegar o id do tratamento
    private String cpf, senha;
    private APICall apiCall;
    private Tratamento tratamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarcarConsultaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");
        idTratamento = getIntent().getIntExtra("idTratamento", 0);//pegando id do tratamento
        pegarTratamento();

        //variaveis para configurar o calendario
        Date dataMaxFormatada = new Date();
        LocalDate dataMax = LocalDate.now().plusMonths(2);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/YYYY"); //formato que o calendarView aceita
        dataMaxFormatada.setTime(Date.parse(dataMax.format(dateFormat))); //pegando a data maxima e deixando no formato correto
        //configurando datas permitidas no calendario
        binding.calendarQuadrado.setMinDate(binding.calendarQuadrado.getDate());//data minima permitida é hoje
        binding.calendarQuadrado.setMaxDate(dataMaxFormatada.getTime()); // configurando data maxima do calendarView
        //definindo a data dos dados da consulta para data atual
        data = horarioEscolhido.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt", "br")) + " " + horarioEscolhido.getDayOfMonth() + " de " + horarioEscolhido.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "br")) + " de "+ horarioEscolhido.getYear();
        binding.tvDataConsulta.setText(data);
        //quando escolhe algum dia do calendario
        binding.calendarQuadrado.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int ano, int mes, int dia) {
                mes++; //acertando mês, pois aqui janeiro é 0 e dezembro é 11

                horarioEscolhido = LocalDate.of(ano,mes,dia);
                String diaString = horarioEscolhido.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt", "br"));
                String mesString = horarioEscolhido.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "br"));
                data = diaString + " " + dia + " de " + mesString + " de "+ ano;
                if(mes>0 && mes<10){
                    if(dia>0 && dia<10)
                        dateParaApi = "0"+dia+"/"+"0"+mes+"/"+ano;
                    else
                        dateParaApi = dia+"/"+"0"+mes+"/"+ano;
                }
                else if(dia>0 && dia<10)
                    dateParaApi = "0"+dia+"/"+mes+"/"+ano;
                else
                    dateParaApi  = dia+"/"+mes+"/"+ano;
                resetarHoras();
                binding.tvDataConsulta.setText(data);
            }
        });
        //spinner estilização
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.array_clinicas, R.layout.color_spinner_layout); // estilo do texto do spinner
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);// estilo das opcoes do spinner
        binding.spinnerClinicas.setAdapter(adapter);

        //spinner quando troca o item (quando escolhe outra unidade)
        binding.spinnerClinicas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String unidade = adapterView.getSelectedItem().toString();
                configurarEndereco(unidade);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(binding.spinnerClinicas.getSelectedItem().toString().contains("carapícuiba"))
                    idClinica = 1;
                else if(binding.spinnerClinicas.getSelectedItem().toString().contains("vila maria"))
                    idClinica = 2;
            }
        });
    }

    public void configurarEndereco(String unidade){
        resetarHoras();
        if(unidade.contains("carapícuiba")){
            binding.tvEnderecoConsulta.setText(R.string.enderecoCarapicuiba);
        }
        else if(unidade.contains("são paulo")){
            binding.tvEnderecoConsulta.setText(R.string.enderecoVilaMaria);
        }
    }

    public void pegarHora(View horaSelecionada){
        resetarHoras();
        horaSelecionada.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black_fill_blue, null));
        AppCompatButton b = (AppCompatButton)horaSelecionada;
        binding.tvDataConsulta.setText(data + " às " + b.getText());

        LocalDate dataAgora = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YYYY"); //formato da api
        if(dateParaApi == null)
            dateParaApi = dataAgora.format(dateFormat);
        timeParaApi = b.getText().toString();
        dateTimeParaApi = dateParaApi + " " + timeParaApi;
        Toast.makeText(this, dateTimeParaApi, Toast.LENGTH_SHORT).show();
    }
    private void resetarHoras(){
        binding.hora1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora3.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora4.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora5.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora6.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora7.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora8.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora9.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora10.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora11.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora12.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora13.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora14.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora15.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora16.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora17.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora18.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora19.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
        binding.hora20.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borda_black, null));
    }

    public void voltarTela(View view){
        onBackPressed();
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
        apiCall = retrofit.create(APICall.class);
    }

    public void pegarTratamento(){
        configurarRetrofit();
        Call<Tratamento> findTratameto = apiCall.findTratameto(idTratamento);
        findTratameto.enqueue(new Callback<Tratamento>() {
            @Override
            public void onResponse(Call<Tratamento> call, Response<Tratamento> response) {
                if(response.code() == 200)
                {
                    tratamento = response.body();
                }
            }
            @Override
            public void onFailure(Call<Tratamento> call, Throwable t) {
                onBackPressed();
            }
        });
    }

    public void efetuarAgendamento(View view){
        configurarRetrofit();
        ConsultaDto dados = new ConsultaDto();
        dados.setValor(tratamento.getValor());
        dados.setDatahora(dateTimeParaApi);
        dados.setSituacao("Aguardando confirmação");
        dados.setCpf_cliente(cpf);
        dados.setId_servico(idTratamento);
        dados.setId_clinica(4);

        Call<Consulta> agendar = apiCall.marcarConsulta(dados);
        agendar.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.code() == 201)
                    finish();
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {

            }
        });
    }
}