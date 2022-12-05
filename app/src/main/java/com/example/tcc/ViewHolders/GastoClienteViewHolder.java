package com.example.tcc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;

public class GastoClienteViewHolder extends RecyclerView.ViewHolder{

    public TextView nomeClie, emailClie, dtNasciClie, gasto;

    public GastoClienteViewHolder(@NonNull View itemView) {
        super(itemView);

        nomeClie = itemView.findViewById(R.id.nome_Cliente);
        emailClie = itemView.findViewById(R.id.email_Cliente);
        dtNasciClie = itemView.findViewById(R.id.dataNasci_Cliente);
        gasto = itemView.findViewById(R.id.gasto_Cliente);
    }
}
