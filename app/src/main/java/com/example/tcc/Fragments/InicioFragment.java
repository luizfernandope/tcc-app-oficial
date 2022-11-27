package com.example.tcc.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tcc.Adapters.CarroselFotosAdapter;
import com.example.tcc.R;
import com.example.tcc.databinding.FragmentInicioBinding;

import java.util.ArrayList;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;
    ArrayList<Integer> imagensCarrosel = new ArrayList<Integer>();
    CarroselFotosAdapter carroselFotosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagensCarrosel.add(R.drawable.foto_carrosel1);
        imagensCarrosel.add(R.drawable.foto_carrosel1);
        imagensCarrosel.add(R.drawable.foto_carrosel1);
        imagensCarrosel.add(R.drawable.foto_carrosel1);
        carroselFotosAdapter = new CarroselFotosAdapter(binding.getRoot().getContext(), imagensCarrosel);
        binding.viewPagerCarroselFotos.setAdapter(carroselFotosAdapter);
    }
}