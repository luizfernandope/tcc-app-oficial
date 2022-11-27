package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.tcc.databinding.ActivitySobreTratamentoBinding;

public class SobreTratamentoActivity extends AppCompatActivity {

    private ActivitySobreTratamentoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySobreTratamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void voltarSobreTratamento(View view){
        onBackPressed();
    }
}