package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Clases.CaracteristicaProducto;
import com.stap.erpstap_avangra.R;

import java.util.List;

public class CardviewAdapterCaracteristicasProducto extends RecyclerView.Adapter<CardviewAdapterCaracteristicasProducto.CardViewHolder>  {


    private List<CaracteristicaProducto> listaCaracteristicas;
    CaracteristicaProducto caracteristicaProducto;
    int contador = 0;

    private OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, CaracteristicaProducto caracteristicaProducto);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterCaracteristicasProducto(Context mCtx, List<CaracteristicaProducto> lista, CardviewAdapterCaracteristicasProducto.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaCaracteristicas = lista;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_caracteristicas_ver_producto, null);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        caracteristicaProducto = listaCaracteristicas.get(position);

        holder.txtDescripcionCaracteristicaVerProducto.setText(caracteristicaProducto.getDescripcion());
        String value = "";

        try {

            value = caracteristicaProducto.getValor();
            if(value.equals("true") || value.equals("false")){
                boolean valueBoolean = Boolean.parseBoolean(value);
                if(valueBoolean){
                    value = "Sí";
                }
                else{
                    value = "No";
                }
            }

        }
        catch (Exception e){

        }

        holder.txtRespuestaCaracteristicaVerProducto.setText(value);

    }

    @Override
    public int getItemCount() {
        return listaCaracteristicas.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtDescripcionCaracteristicaVerProducto, txtRespuestaCaracteristicaVerProducto;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtDescripcionCaracteristicaVerProducto = itemView.findViewById(R.id.txtDescripcionCaracteristicaVerProducto);
            txtRespuestaCaracteristicaVerProducto = itemView.findViewById(R.id.txtRespuestaCaracteristicaVerProducto);
        }
    }
}
