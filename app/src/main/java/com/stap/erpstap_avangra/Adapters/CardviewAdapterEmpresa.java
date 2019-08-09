package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.R;

import java.util.List;

public class CardviewAdapterEmpresa extends RecyclerView.Adapter<CardviewAdapterEmpresa.CardViewHolder> {

    private List<Empresa> listaEmpresas;
    Empresa empresa;

    private CardviewAdapterEmpresa.OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, Empresa empresa);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterEmpresa(Context mCtx, List<Empresa> lista, CardviewAdapterEmpresa.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaEmpresas = lista;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CardviewAdapterEmpresa.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_empresa, null);

        return new CardviewAdapterEmpresa.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardviewAdapterEmpresa.CardViewHolder holder, final int position) {
        empresa = listaEmpresas.get(position);

        holder.txtNombreEmpresaCardview.setText(empresa.getNombre());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Empresa empresaSeleccionada = listaEmpresas.get(position);
                onItemClickListener.onItemClicked(position, 0, empresaSeleccionada);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEmpresas.size();
    }
    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreEmpresaCardview;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtNombreEmpresaCardview = itemView.findViewById(R.id.txtNombreEmpresaCardview);
        }
    }
}
