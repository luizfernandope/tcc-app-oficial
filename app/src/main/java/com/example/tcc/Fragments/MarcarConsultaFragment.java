package com.example.tcc.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.tcc.R;
import com.example.tcc.databinding.FragmentMarcarConsultaBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class MarcarConsultaFragment extends Fragment {

    private FragmentMarcarConsultaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMarcarConsultaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date dataMaxFormatada = new Date();
        LocalDate dataMax = LocalDate.now().plusMonths(2);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/YYYY"); //formato que o calendarView aceita
        dataMaxFormatada.setTime(Date.parse(dataMax.format(dateFormat))); //pegando a data maxima e deixando no formato correto


        binding.calendarQuadrado.setMinDate(binding.calendarQuadrado.getDate());//data minima permitida é hoje
        binding.calendarQuadrado.setMaxDate(dataMaxFormatada.getTime()); // configurando data maxima do calendarView


        binding.calendarQuadrado.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int ano, int mes, int dia) {
                mes++; //acertando mês, pois aqui janeiro é 0 e dezembro é 11
                System.out.println("\n\n\nteste: "+dia+"/"+mes+"/"+ano);
                System.out.println("\n\n\nteste2: "+calendarView.getDate());
            }
        });
        //spinner estilização
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.array_clinicas, R.layout.color_spinner_layout); // estilo do texto do spinner
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);// estilo das opcoes do spinner
        binding.spinnerClinicas.setAdapter(adapter);

        //horario consultas
        binding.timePicker.setLabelText("","","","H","min", "seg");
        binding.horas.setMinValue(7);
        binding.horas.setMaxValue(20);
        binding.minutos.setMinValue(0);
        binding.minutos.setMaxValue(59);
    }
}