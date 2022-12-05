package com.example.tcc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Models.Cliente;
import com.example.tcc.Models.Consulta;
import com.example.tcc.R;
import com.example.tcc.ViewHolders.GastoClienteViewHolder;

import java.util.ArrayList;

public class GastoClienteAdapter extends RecyclerView.Adapter<GastoClienteViewHolder> {

    private Context context;
    private ArrayList<Cliente> listClientes;
    private ArrayList<Double> gastos;

    public GastoClienteAdapter(Context context, ArrayList<Cliente> listClientes, ArrayList<Double> gastos) {
        this.context = context;
        this.listClientes = listClientes;
        this.gastos = gastos;
    }

    @NonNull
    @Override
    public GastoClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_gasto_cliente, parent, false);
        GastoClienteViewHolder viewHolder = new GastoClienteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GastoClienteViewHolder holder, int position) {
        Cliente clienteAtual = listClientes.get(position);

        holder.nomeClie.setText(clienteAtual.getNome());
        holder.emailClie.setText("Email: "+clienteAtual.getEmail());
        holder.dtNasciClie.setText("CPF: " +clienteAtual.getUsuario().getCpf());
        holder.gasto.setText(clienteAtual.getSexo()+"\n"+clienteAtual.getDtNascimento());
    }

    @Override
    public int getItemCount() {
        if(listClientes.toArray() != null )
            return listClientes.size();
        else
            return 0;

    }
}
