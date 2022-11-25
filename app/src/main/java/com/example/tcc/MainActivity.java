package com.example.tcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.tcc.Fragments.AgendaFragment;
import com.example.tcc.Fragments.ServicosFragment;
import com.example.tcc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        trocaFragment(binding.btnServicos);
    }

    public void trocaFragment(View btnClicado){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(binding.btnAgenda == btnClicado)
            fragmentTransaction.replace(binding.frameLayoutTelas.getId(), new AgendaFragment());
        else if(binding.btnServicos == btnClicado)
            fragmentTransaction.replace(binding.frameLayoutTelas.getId(), new ServicosFragment());
        fragmentTransaction.commit();
    }
}