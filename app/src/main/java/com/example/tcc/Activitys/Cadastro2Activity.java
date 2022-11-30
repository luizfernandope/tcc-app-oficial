package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.tcc.databinding.ActivityCadastro2Binding;

public class Cadastro2Activity extends AppCompatActivity {

    private ActivityCadastro2Binding binding;
    public String cpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastro2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cpf = getIntent().getStringExtra("cpf");
        Toast.makeText(this, cpf, Toast.LENGTH_SHORT).show();
    }
}