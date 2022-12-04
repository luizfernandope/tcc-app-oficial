package com.example.tcc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Activitys.EditarTratamentoActivity;
import com.example.tcc.Activitys.MarcarConsultaActivity;
import com.example.tcc.Activitys.SobreTratamentoActivity;
import com.example.tcc.Fragments.ServicosFragment;
import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
import com.example.tcc.ViewHolders.TratamentoViewHolder;

import java.util.ArrayList;
@RequiresApi(api = Build.VERSION_CODES.O)
public class TratamentoAdapter extends RecyclerView.Adapter<TratamentoViewHolder> {
    //clase que configura o adapter, para permitir que a listagem no RecyclerView seja feita

    private Context context;
    private ArrayList<Tratamento> listTratamentos;
    private String cpf, senha;
    private Boolean cliente;

    public TratamentoAdapter(Context ctx,ArrayList<Tratamento> listTratamentos, String cpf, String senha, Boolean cliente) {
        this.context = ctx;
        this.listTratamentos = listTratamentos;
        this.cpf = cpf;
        this.senha = senha;
        this.cliente = cliente;
    }


    @NonNull
    @Override
    public TratamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_tratamento, parent, false); // view que sera replicada
        TratamentoViewHolder viewHolder = new TratamentoViewHolder(view); //declarando e configurando viewHolder
        return viewHolder;
    }
    int proximo=0;
    @Override
    public void onBindViewHolder(@NonNull TratamentoViewHolder holder, int position) { // quando carrega um valor
        int qtd = listTratamentos.size();
        if(!cliente)
        {
            holder.marcar1.setText("editar tratamento");
            holder.marcar2.setText("editar tratamento");
        }

        if(proximo!=0 && proximo +1 <qtd)
            proximo = position+1;
        if(proximo <= qtd-1 && proximo % 2 == 0) {
            Tratamento card1 = listTratamentos.get(proximo);
            holder.titulo1.setText(card1.getNome());
            holder.descricao1.setText(card1.getDescricao());
            holder.tipo1.setText(card1.getTipo());
            holder.card2.setVisibility(View.INVISIBLE);
            if(proximo==qtd-1){
                proximo = proximo+100;
            }
            //quando clicar no card
            holder.card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cliente) {
                        Intent intent = new Intent(context, SobreTratamentoActivity.class);
                        intent.putExtra("idTratamento", card1.getId());
                        intent.putExtra("cpf", cpf);
                        intent.putExtra("senha", senha);
                        context.startActivity(intent);
                    }
                    else if(cliente == false){
                        Intent intent = new Intent(context, EditarTratamentoActivity.class);
                        intent.putExtra("idTratamento", card1.getId());
                        context.startActivity(intent);
                    }
                }
            });
            //quando clicar em marcar consulta
            holder.marcar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cliente) {
                        Intent intent = new Intent(context, MarcarConsultaActivity.class);
                        intent.putExtra("idTratamento", card1.getId());
                        intent.putExtra("cpf", cpf);
                        intent.putExtra("senha", senha);
                        context.startActivity(intent);
                    }
                    else if(cliente == false){
                        Intent intent = new Intent(context, EditarTratamentoActivity.class);
                        intent.putExtra("idTratamento", card1.getId());
                        context.startActivity(intent);
                    }
                }
            });
        }
        else{
            holder.card1.removeAllViews();
            holder.card2.removeAllViews();
        }
        if(proximo+1 <qtd){
            proximo=proximo+1;
            holder.card2.setVisibility(View.VISIBLE);
            Tratamento card2 = listTratamentos.get(proximo);
            holder.titulo2.setText(card2.getNome());
            holder.descricao2.setText(card2.getDescricao());
            holder.tipo2.setText(card2.getTipo());
            //quando clicar no card
            holder.card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cliente) {
                        Intent intent = new Intent(context, SobreTratamentoActivity.class);
                        intent.putExtra("idTratamento", card2.getId());
                        intent.putExtra("cpf", cpf);
                        intent.putExtra("senha", senha);
                        context.startActivity(intent);
                    }
                    else if(cliente == false){
                        Intent intent = new Intent(context, EditarTratamentoActivity.class);
                        intent.putExtra("idTratamento", card2.getId());
                        context.startActivity(intent);
                    }
                }
            });
            //quando clicar em marcar consulta
            holder.marcar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cliente) {
                        Intent intent = new Intent(context, MarcarConsultaActivity.class);
                        intent.putExtra("idTratamento", card2.getId());
                        intent.putExtra("cpf", cpf);
                        intent.putExtra("senha", senha);
                        context.startActivity(intent);
                    }
                    else if(cliente == false){
                        Intent intent = new Intent(context, EditarTratamentoActivity.class);
                        intent.putExtra("idTratamento", card2.getId());
                        context.startActivity(intent);
                    }
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        if(listTratamentos.toArray() != null)
            return listTratamentos.size();
        else
            return 0;
    }
}
