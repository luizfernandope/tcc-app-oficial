package com.example.tcc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Activitys.SobreConsultaActivity;
import com.example.tcc.Models.Clinica;
import com.example.tcc.Models.Consulta;
import com.example.tcc.R;
import com.example.tcc.ViewHolders.ConsultaViewHolder;

import java.util.ArrayList;

public class ConsultaAdapter extends RecyclerView.Adapter<ConsultaViewHolder> {

    private Context context;
    private ArrayList<Consulta> listConsultas;

    public ConsultaAdapter(Context context, ArrayList<Consulta> listConsultas) {
        this.context = context;
        this.listConsultas = listConsultas;
    }

    @NonNull
    @Override
    public ConsultaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_consulta, parent, false); // view que sera replicada
        ConsultaViewHolder viewHolder = new ConsultaViewHolder(view); //declarando e configurando viewHolder

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ConsultaViewHolder holder, int position) {
        Consulta consulta = listConsultas.get(position);
        Clinica clinica = consulta.getClinica();

        String endereco = clinica.getRua() +" "+ clinica.getNumero().toString();
        holder.titulo.setText(consulta.getServico().getNome());
        holder.endereco.setText(endereco);
        holder.data.setText(consulta.getDatahora().substring(0,10));
        holder.hora.setText(consulta.getDatahora().substring(11,16) + " H");
        if(position<listConsultas.size()-1)
            holder.card.setPadding(0,0,0,20);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SobreConsultaActivity.class);
                intent.putExtra("idConsulta", consulta.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listConsultas.toArray() != null)
            return listConsultas.size();
        else
            return 0;
    }
}
