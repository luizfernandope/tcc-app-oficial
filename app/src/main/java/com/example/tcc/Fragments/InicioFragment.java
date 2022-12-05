package com.example.tcc.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tcc.Adapters.CarroselFotosAdapter;
import com.example.tcc.MainActivity;
import com.example.tcc.R;
import com.example.tcc.databinding.FragmentInicioBinding;

import java.util.ArrayList;
@RequiresApi(api = Build.VERSION_CODES.O)
public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;
    ArrayList<Integer> imagensCarrosel = new ArrayList<Integer>();
    CarroselFotosAdapter carroselFotosAdapter;
    private String cpf, senha;
    private Boolean cliente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        cpf = bundle.getString("cpf");
        senha = bundle.getString("senha");
        cliente = bundle.getBoolean("cliente", true);
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

        binding.minhaAgendaAtalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trocarTela(3);
            }
        });

        binding.servicosAtalho.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                trocarTela(2);
            }
        });
    }


    public void trocarTela(int idTela){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        if(idTela==2)
            fragment = new ServicosFragment();
        else if(idTela == 3)
            fragment = new AgendaFragment();
        if(fragment!=null) {
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            bundle.putString("cpf", cpf);
            bundle.putString("senha", senha);
            bundle.putBoolean("cliente", cliente);
            //iniciando os fragments no frame layout
            fragmentTransaction.replace(R.id.frameLayout_telas, fragment);
            fragmentTransaction.commit();
            MeowBottomNavigation bottomNavigation = getActivity().findViewById(R.id.bottomNavigation);
            bottomNavigation.show(idTela, true);
        }
    }

}