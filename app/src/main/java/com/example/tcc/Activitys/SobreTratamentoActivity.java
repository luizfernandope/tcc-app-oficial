package com.example.tcc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tcc.databinding.ActivitySobreTratamentoBinding;

public class SobreTratamentoActivity extends AppCompatActivity {

    private ActivitySobreTratamentoBinding binding;
    private String cpf, senha;
    private Integer idTratamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySobreTratamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");
        idTratamento = getIntent().getIntExtra("idTratamento", 0);

        Toast.makeText(SobreTratamentoActivity.this, "cliclou\n"+cpf+"\n"+senha+"\nidTratamento: "+idTratamento, Toast.LENGTH_SHORT).show();//teste recebimento dos valores
    }

    public void voltarSobreTratamento(View view){
        onBackPressed();
    }
}