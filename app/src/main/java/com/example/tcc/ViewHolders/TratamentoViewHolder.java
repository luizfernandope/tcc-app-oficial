package com.example.tcc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;
import com.example.tcc.databinding.FragmentServicosBinding;

public class TratamentoViewHolder extends RecyclerView.ViewHolder {

    public CardView card1, card2;
    public TextView titulo1, titulo2, descricao1, descricao2, tipo1, tipo2, marcar1, marcar2; //public para que o adapter tenha acesso e possa manipular


    public TratamentoViewHolder(@NonNull View itemView) {
        super(itemView);

        card1 = itemView.findViewById(R.id.card1_tratamento);
        card2 = itemView.findViewById(R.id.card2_tratamento);
        titulo1 = itemView.findViewById(R.id.titulo1_tratamento);
        titulo2 = itemView.findViewById(R.id.titulo2_tratamento);
        descricao1 = itemView.findViewById(R.id.descricao1_tratamento);
        descricao2 = itemView.findViewById(R.id.descricao2_tratamento);
        tipo1 = itemView.findViewById(R.id.tipo1_tratamento);
        tipo2 = itemView.findViewById(R.id.tipo2_tratamento);
        marcar1 = itemView.findViewById(R.id.marcarConsulta1);
        marcar2 = itemView.findViewById(R.id.marcarConsulta2);
    }
}
