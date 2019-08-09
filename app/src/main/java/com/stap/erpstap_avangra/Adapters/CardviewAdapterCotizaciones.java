package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.R;

import java.util.List;

public class CardviewAdapterCotizaciones extends RecyclerView.Adapter<CardviewAdapterCotizaciones.CardViewHolder>{

    private List<Cotizacion> listaCotizaciones;
    Cotizacion cotizacion;

    private OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, Cotizacion cotizacion);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterCotizaciones(Context mCtx, List<Cotizacion> lista, CardviewAdapterCotizaciones.OnItemClickListener onItemClickListener) {
        //Collections.reverse(lista);

        this.mCtx = mCtx;
        this.listaCotizaciones = lista;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_cotizaciones, null);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        cotizacion = listaCotizaciones.get(position);

        holder.txtCotizacionCodigo.setText(cotizacion.getCodigo());
        holder.txtFechaCotizacion.setText(cotizacion.getFecha());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cotizacion cotizacionSeleccionada = listaCotizaciones.get(position);
                onItemClickListener.onItemClicked(position, 0, cotizacionSeleccionada);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCotizaciones.size();
    }
    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtCotizacionCodigo, txtFechaCotizacion;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtCotizacionCodigo = itemView.findViewById(R.id.txtCotizacionCodigo);
            txtFechaCotizacion = itemView.findViewById(R.id.txtFechaCotizacion);
        }
    }
}
