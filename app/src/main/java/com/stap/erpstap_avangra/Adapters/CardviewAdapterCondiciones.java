package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.R;

import java.util.List;

public class CardviewAdapterCondiciones extends RecyclerView.Adapter<CardviewAdapterCondiciones.CardViewHolder> {

    private List<Condiciones> listaCondiciones;
    Condiciones condicion;
    int contador = 0;

    private OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, Condiciones condicion);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterCondiciones(Context mCtx, List<Condiciones> lista, CardviewAdapterCondiciones.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaCondiciones = lista;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_condiciones, null);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        condicion = listaCondiciones.get(position);

        if(condicion.getEsTitulo() == true){
            contador = 0;
            holder.txtDescripcionOTituloCondicionCotizacion.setText(condicion.getNombreItem());
            holder.cardview_condiciones.setBackgroundColor(Color.parseColor("#ffe085"));
        }
        else
        {
            contador++;
            holder.txtContadorCondicionCotizacion.setText(contador+"");
            holder.txtDescripcionOTituloCondicionCotizacion.setText(condicion.getDescripcion());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Condiciones cotizacionSeleccionada = listaCondiciones.get(position);
                onItemClickListener.onItemClicked(position, 0, cotizacionSeleccionada);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCondiciones.size();
    }
    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtContadorCondicionCotizacion, txtDescripcionOTituloCondicionCotizacion;
        CardView cardview_condiciones;

        public CardViewHolder(View itemView) {
            super(itemView);

            cardview_condiciones = itemView.findViewById(R.id.cardview_condiciones);
            txtContadorCondicionCotizacion = itemView.findViewById(R.id.txtContadorCondicionCotizacion);
            txtDescripcionOTituloCondicionCotizacion = itemView.findViewById(R.id.txtDescripcionOTituloCondicionCotizacion);
        }
    }
}
