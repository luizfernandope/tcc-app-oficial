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
import com.example.tcc.R;
import com.example.tcc.databinding.FragmentInicioFuncionarioBinding;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class InicioFuncionarioFragment extends Fragment {

    private FragmentInicioFuncionarioBinding binding;
    ArrayList<Integer> imagensCarrosel = new ArrayList<Integer>();
    CarroselFotosAdapter carroselFotosAdapter;
    private String cpf, senha;
    private Boolean cliente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInicioFuncionarioBinding.inflate(inflater,container,false);

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

        binding.horarioMarcadoAtalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trocarTela(3);
            }
        });

        binding.servicosAtalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trocarTela(2);
            }
        });

        binding.verClientesAtalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trocarTela(5);
            }
        });

        binding.apurarSaldoAtalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trocarTela(4);
            }
        });

        binding.politicasAtalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.sobreNosAtalho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        else if(idTela == 4)
            fragment = new ReceitaFragment();
        else if(idTela == 5)
            fragment = new VerClientesFragment();

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