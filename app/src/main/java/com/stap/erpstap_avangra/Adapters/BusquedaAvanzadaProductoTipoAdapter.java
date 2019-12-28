package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.R;

import java.util.ArrayList;
import java.util.List;

public class BusquedaAvanzadaProductoTipoAdapter extends RecyclerView.Adapter<BusquedaAvanzadaProductoTipoAdapter.CardViewHolder> {

    public List<String> listaTipo = new ArrayList<>();
    public BusquedaAvanzadaProductoTipoAdapter.CardViewHolder holder;
    public BusquedaAvanzadaProductoTipoAdapter.CardViewHolder holderAnterior = null;

    private static final int UNSELECTED = -1;
    public int pos;
    private int selectedItem = UNSELECTED;

    String tipoSeleccionado;

    private BusquedaAvanzadaProductoTipoAdapter.OnItemClickListener onItemClickListener; // Global scope

    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, String tipoSeleccionado, List<String> listaTipo);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public BusquedaAvanzadaProductoTipoAdapter(Context mCtx, List<String> lista, BusquedaAvanzadaProductoTipoAdapter.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaTipo = lista;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public BusquedaAvanzadaProductoTipoAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_tipo_caracteristica_producto, null);

        return new BusquedaAvanzadaProductoTipoAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BusquedaAvanzadaProductoTipoAdapter.CardViewHolder _holder, final int position) {
        holder = _holder;
        tipoSeleccionado = listaTipo.get(position);
        pos = position;

        holder.txtTipoCaracteristicaProducto.setText(tipoSeleccionado);

        holder.linearLayout_tipo_caracteristica_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tipoSeleccionado = listaTipo.get(position);
                holder = _holder;

                //holder.linearLayout_tipo_caracteristica_producto.setBackgroundColor(Color.parseColor("#f7f7f7"));

                if(holderAnterior == null){
                    holderAnterior = _holder;
                }

                if(holderAnterior != holder){
                    if (holderAnterior.rbt_tipo_caracteristica_producto.isChecked()) {
                        holderAnterior.rbt_tipo_caracteristica_producto.setChecked(false);
                        holderAnterior.linearLayout_tipo_caracteristica_producto.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }

                if (holder.rbt_tipo_caracteristica_producto.isChecked()) {
                    holder.rbt_tipo_caracteristica_producto.setChecked(false);
                    holder.linearLayout_tipo_caracteristica_producto.setBackgroundColor(Color.parseColor("#ffffff"));
                    onItemClickListener.onItemClicked(position, 0, "", listaTipo);

                } else {
                    holder.rbt_tipo_caracteristica_producto.setChecked(true);
                    holder.linearLayout_tipo_caracteristica_producto.setBackgroundColor(Color.parseColor("#f7f7f7"));
                    onItemClickListener.onItemClicked(position, 0, tipoSeleccionado, listaTipo);

                }

                holderAnterior = holder;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaTipo.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtTipoCaracteristicaProducto;
        RadioButton rbt_tipo_caracteristica_producto;
        LinearLayout linearLayout_tipo_caracteristica_producto;

        public CardViewHolder(View itemView) {
            super(itemView);

            linearLayout_tipo_caracteristica_producto = itemView.findViewById(R.id.linearLayout_tipo_caracteristica_producto);
            txtTipoCaracteristicaProducto = itemView.findViewById(R.id.txtTipoCaracteristicaProducto);
            rbt_tipo_caracteristica_producto = itemView.findViewById(R.id.rbt_tipo_caracteristica_producto);
        }
    }


}
