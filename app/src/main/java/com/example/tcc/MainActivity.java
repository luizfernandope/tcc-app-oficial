package com.example.tcc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tcc.Activitys.SobreTratamentoActivity;
import com.example.tcc.Fragments.AgendaFragment;
import com.example.tcc.Fragments.InicioFragment;
import com.example.tcc.Fragments.PerfilFragment;
import com.example.tcc.Fragments.ServicosFragment;
import com.example.tcc.databinding.ActivityMainBinding;
@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public String cpf, senha;
    private int idTelaAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cpf = getIntent().getStringExtra("cpf");
        senha = getIntent().getStringExtra("senha");

        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_lupa));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_calendario));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_person));

        binding.bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        binding.bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
        binding.bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                if(idTelaAtual == item.getId())
                {

                }
                else{
                    idTelaAtual = item.getId();
                    trocaFragment(item.getId());
                }
            }
        });

        binding.bottomNavigation.show(1, false); //iniciar com o fragment de inicio
    }


    public void trocaFragment(int idTela){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new InicioFragment();
        if(idTela == 1)
            fragment = new InicioFragment();
        else if(idTela == 2)
            fragment = new ServicosFragment();
        else if(idTela == 3)
            fragment = new AgendaFragment();
        else if(idTela == 4)
            fragment = new PerfilFragment();
        //passando os dados para os fragments
        Bundle bundle = new Bundle();
        bundle.putString("cpf", cpf);
        bundle.putString("senha", senha);
        fragment.setArguments(bundle);
        //iniciando os fragments no frame layout
        fragmentTransaction.replace(binding.frameLayoutTelas.getId(), fragment);
        fragmentTransaction.commit();
    }
}