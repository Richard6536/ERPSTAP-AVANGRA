package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Activity.CarroCompraActivity;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.stap.erpstap_avangra.Activity.CarroCompraActivity.no_prod_layout;

public class CardviewAdapterCarroCompra extends RecyclerView.Adapter<CardviewAdapterCarroCompra.CardViewHolder> {


    private List<ProductoEnCarro> listaProductos;
    ProductoEnCarro producto;
    CardviewAdapterCarroCompra.CardViewHolder holder;
    List<CardViewHolder> holderAdapters = new ArrayList<>();

    private CardviewAdapterCarroCompra.OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, ProductoEnCarro productoEnCarro);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterCarroCompra(Context mCtx, List<ProductoEnCarro> lista, CardviewAdapterCarroCompra.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaProductos = lista;
        this.onItemClickListener = onItemClickListener;
    }

    public void notifyAdapter(int position, int cantidad) {
        producto = listaProductos.get(position);
        producto.setCantidad(cantidad);
        holder.txtCantidadCardview.setText("Cantidad: "+producto.getCantidad()+" (cambiar)");
    }

    @NonNull
    @Override
    public CardviewAdapterCarroCompra.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_carrocompra, null);

        return new CardviewAdapterCarroCompra.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardviewAdapterCarroCompra.CardViewHolder _holder, final int position) {
        holder = _holder;

        producto = listaProductos.get(position);

        holder.txtNombreProductoCarroCompra.setText(producto.getNombre());
        holder.txtPrecioProductoCarroCompra.setText("$"+ new Moneda().Format(Integer.parseInt(producto.getValor())));
        holder.txtCantidadCardview.setText("Cantidad: "+producto.getCantidad()+" (cambiar)");

        List<String> imagenes = producto.getImagenes();
        String primeraImagen = (String)imagenes.get(0);

        Picasso.with(mCtx).load(primeraImagen).fit().into(holder.imgViewProductoCarroCompra);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder = _holder;
                ProductoEnCarro productoSeleccionado = listaProductos.get(position);
                onItemClickListener.onItemClicked(position, 0, productoSeleccionado);
            }
        });

        holder.close_img_dialog_carroCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaProductos.remove(position);
                holderAdapters.remove(position);

                ProductoEnCarro.productosEnCarro = listaProductos;
                CarroCompraActivity.productoEnCarroList = listaProductos;

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listaProductos.size());
                notifyItemRangeChanged(position, holderAdapters.size());

                if(listaProductos.size() == 0){
                    no_prod_layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreProductoCarroCompra, txtPrecioProductoCarroCompra, txtCantidadCardview;
        ImageView imgViewProductoCarroCompra, close_img_dialog_carroCompra;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtNombreProductoCarroCompra = itemView.findViewById(R.id.txtNombreProductoCarroCompra);
            txtPrecioProductoCarroCompra = itemView.findViewById(R.id.txtPrecioProductoCarroCompra);
            imgViewProductoCarroCompra = itemView.findViewById(R.id.imgViewProductoCarroCompra);
            close_img_dialog_carroCompra = itemView.findViewById(R.id.close_img_dialog_carroCompra);
            txtCantidadCardview = itemView.findViewById(R.id.txtCant);

            holderAdapters.add(this);
        }
    }


    public boolean holderRepetido(CardViewHolder holder){
        for(CardViewHolder h : holderAdapters){
            if(h == holder){
                return true;
            }
        }
        return false;
    }
}
