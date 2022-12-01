package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.databinding.ActivitySobreConsultaBinding;

public class SobreConsultaActivity extends AppCompatActivity {

    private ActivitySobreConsultaBinding binding;
    private Integer idConsulta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySobreConsultaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idConsulta = getIntent().getIntExtra("idConsulta", 0);
    }

    public void voltarSobreConsulta(View view){
        onBackPressed();
    }
}