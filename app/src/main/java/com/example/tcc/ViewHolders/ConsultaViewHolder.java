package com.example.tcc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;

public class ConsultaViewHolder extends RecyclerView.ViewHolder {

    public TextView titulo, endereco, data, hora;
    public ConstraintLayout card;

    public ConsultaViewHolder(@NonNull View itemView) {
        super(itemView);

        titulo = itemView.findViewById(R.id.titulo_consulta);
        endereco = itemView.findViewById(R.id.endereco_consulta);
        data = itemView.findViewById(R.id.data_consulta);
        hora = itemView.findViewById(R.id.hora_consulta);
        card = itemView.findViewById(R.id.card_consulta);

    }
}
