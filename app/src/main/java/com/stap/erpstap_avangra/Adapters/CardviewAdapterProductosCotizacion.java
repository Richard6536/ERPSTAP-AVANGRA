package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Activity.ImageviewActivity;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardviewAdapterProductosCotizacion extends RecyclerView.Adapter<CardviewAdapterProductosCotizacion.CardViewHolder>{

    private List<Producto> listaProductos;
    Producto producto;

    private Context mCtx;

    private OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, Producto producto, Context _context);
    }


    //getting the context and product list with constructor
    public CardviewAdapterProductosCotizacion(Context mCtx, List<Producto> lista, CardviewAdapterProductosCotizacion.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaProductos = lista;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_producto_cotizacion, null);

        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        producto = listaProductos.get(position);

        int vTotal = producto.getValor() * producto.getCantidad();

        holder.txtNombreProductoCotizacion.setText(producto.getNombre());
        holder.txtCantidadCotizacion.setText("C.: "+producto.getCantidad());
        holder.txtPrecioProductoCotizacion.setText("$"+ new Moneda().Format(vTotal));

        List<String> imagenes = producto.getImagenes();

        try {
            String primeraImagen = (String)imagenes.get(0);
            holder.imgViewProductoCotizacion.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(mCtx).load(primeraImagen).into(holder.imgViewProductoCotizacion);

        }
        catch (Exception e){
            Picasso.with(mCtx).load("http://www.losprincipios.org/images/default.jpg").fit().into(holder.imgViewProductoCotizacion);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Producto productoSeleccionado = listaProductos.get(position);
                onItemClickListener.onItemClicked(position, 0, productoSeleccionado, mCtx);
            }
        });

        holder.imgViewProductoCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Producto productoSeleccionado = listaProductos.get(position);

                List<String> imagenes = productoSeleccionado.getImagenes();

                if(imagenes.size() > 0){

                    ArrayList<String> imgArray = new ArrayList<>(imagenes);

                    Intent intent = new Intent( mCtx, ImageviewActivity.class);
                    Bundle arguments = new Bundle();
                    arguments.putString("Nombre", productoSeleccionado.getNombre());
                    arguments.putString("Descripcion", productoSeleccionado.getDescripcion());
                    arguments.putBoolean("MostrarDescripcion", true);
                    arguments.putStringArrayList("Imagenes", imgArray);
                    arguments.putInt("Position", 0);
                    intent.putExtras(arguments);
                    mCtx.startActivity(intent);
                }
                onItemClickListener.onItemClicked(position, 0, productoSeleccionado, mCtx);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }
    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreProductoCotizacion, txtPrecioProductoCotizacion, txtCantidadCotizacion;
        ImageView imgViewProductoCotizacion;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtNombreProductoCotizacion = itemView.findViewById(R.id.txtNombreProductoCotizacion);
            txtPrecioProductoCotizacion = itemView.findViewById(R.id.txtPrecioProductoCotizacion);
            txtCantidadCotizacion = itemView.findViewById(R.id.txtCantidadCotizacion);

            imgViewProductoCotizacion = itemView.findViewById(R.id.imgViewProductoCotizacion);
        }
    }
}
