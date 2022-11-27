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

import com.example.tcc.R;
import com.example.tcc.databinding.ActivityMarcarConsultaBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MarcarConsultaActivity extends AppCompatActivity {

    private ActivityMarcarConsultaBinding binding;
    private String data;
    private LocalDate horarioEscolhido = LocalDate.now();
    private Integer idTratamento = 0; //pegar o id do tratamento

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarcarConsultaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

            }
        });
        idTratamento = getIntent().getIntExtra("idTratamento", 0);//pegando id do tratamento
        Toast.makeText(this, "id tratamento = "+ idTratamento, Toast.LENGTH_SHORT).show();
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
}