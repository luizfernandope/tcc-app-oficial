package com.example.tcc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Models.Tratamento;
import com.example.tcc.R;
import com.example.tcc.ViewHolders.TratamentoViewHolder;

import java.util.ArrayList;

public class TratamentoAdapter extends RecyclerView.Adapter<TratamentoViewHolder> {
    //clase que configura o adapter, para permitir que a listagem no RecyclerView seja feita

    private Context context;
    private ArrayList<Tratamento> listTratamentos;

    public TratamentoAdapter(Context ctx,ArrayList<Tratamento> listTratamentos) {
        this.context = ctx;
        this.listTratamentos = listTratamentos;
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
