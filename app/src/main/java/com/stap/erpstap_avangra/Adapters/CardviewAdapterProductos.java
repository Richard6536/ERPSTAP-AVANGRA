package com.stap.erpstap_avangra.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardviewAdapterProductos extends RecyclerView.Adapter<CardviewAdapterProductos.CardViewHolder> implements Filterable {

    private List<SpinnerAdapter> listaProductos = new ArrayList<>();
    private List<SpinnerAdapter> listaProductosFull = new ArrayList<>();

    SpinnerAdapter producto;

    private OnItemClickListener onItemClickListener; // Global scope

    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, SpinnerAdapter auto);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterProductos(Context mCtx, List<SpinnerAdapter> lista, CardviewAdapterProductos.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaProductos = lista;
        this.onItemClickListener = onItemClickListener;

        listaProductosFull.addAll(listaProductos);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_producto, null);

        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        producto = listaProductos.get(position);

        holder.txtNombreProducto.setText(producto.getNombre());
        holder.txtPrecioProducto.setText("$"+ new Moneda().Format(Integer.parseInt(producto.getValor())));

        List<String> imagenes = producto.getImagenes();

        try {
            String primeraImagen = (String)imagenes.get(0);

            Picasso.with(mCtx).load(primeraImagen).fit().into(holder.imgViewProducto);

        }
        catch (Exception e){
            Picasso.with(mCtx).load("http://www.losprincipios.org/images/default.jpg").fit().into(holder.imgViewProducto);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SpinnerAdapter productoSeleccionado = listaProductos.get(position);
                onItemClickListener.onItemClicked(position, 0, productoSeleccionado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }
    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreProducto, txtPrecioProducto;
        ImageView imgViewProducto;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecioProducto = itemView.findViewById(R.id.txtPrecioProducto);
            imgViewProducto = itemView.findViewById(R.id.imgViewProducto);
        }
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SpinnerAdapter> filterProducts = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filterProducts.add((SpinnerAdapter) listaProductos);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(SpinnerAdapter product : listaProductos){
                    if(product.getNombre().toLowerCase().contains(filterPattern)){
                        filterProducts.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterProducts;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(filterResults.values != null){
                if(listaProductos.size() == 0){
                    listaProductos.addAll(listaProductosFull);
                }
                listaProductos.clear();
                listaProductos.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
            else {
                listaProductos.clear();
                listaProductos.addAll(listaProductosFull);
                notifyDataSetChanged();
            }
        }
    };
}

