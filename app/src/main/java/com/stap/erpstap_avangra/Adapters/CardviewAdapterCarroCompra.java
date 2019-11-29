package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Activity.CarroCompraActivity;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.R;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import static com.stap.erpstap_avangra.Clases.ProductoEnCarro.productosEnCarro;
import static com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment.no_prod_layout;
import static com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment.recyclerView_carro_compra;

public class CardviewAdapterCarroCompra extends RecyclerView.Adapter<CardviewAdapterCarroCompra.CardViewHolder> implements ExpandableLayout.OnExpansionUpdateListener {

    private static final int UNSELECTED = -1;
    public int pos;
    private int selectedItem = UNSELECTED;

    private List<ProductoEnCarro> listaProductos;
    ProductoEnCarro producto;
    ProductoEnCarro productoSeleccionado;
    CardviewAdapterCarroCompra.CardViewHolder holder;
    CardviewAdapterCarroCompra.CardViewHolder holderAnterior = holder;
    List<CardViewHolder> holderAdapters = new ArrayList<>();

    private CardviewAdapterCarroCompra.OnItemClickListener onItemClickListener; // Global scope

    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {

    }

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
        pos = position;

        producto = listaProductos.get(position);

        holder.txtNombreProductoCarroCompra.setText(producto.getNombre());
        holder.txtPrecioProductoCarroCompra.setText("$"+ new Moneda().Format(Integer.parseInt(producto.getValor())));
        holder.txtCantidadCardview.setText("Cantidad: "+producto.getCantidad()+" (cambiar)");

        List<String> imagenes = producto.getImagenes();
        String primeraImagen = (String)imagenes.get(0);

        holder.imgViewProductoCarroCompra.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(mCtx).load(primeraImagen).into(holder.imgViewProductoCarroCompra);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder = _holder;
                productoSeleccionado = listaProductos.get(position);
                //onItemClickListener.onItemClicked(position, 0, productoSeleccionado);

                if (holderAnterior != null) {
                    holderAnterior.itemView.setSelected(false);
                    holderAnterior.expandableLayout.collapse();
                }

                //int position = getAdapterPosition();
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {

                    holder.editTextCantidadProductoCarroCompra.setText(productoSeleccionado.getCantidad()+"");

                    holder.itemView.setSelected(true);
                    holder.expandableLayout.expand();
                    selectedItem = position;
                    holderAnterior = holder;
                }
            }
        });

        holder.close_img_dialog_carroCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaProductos.remove(position);
                holderAdapters.remove(position);

                productosEnCarro = listaProductos;
                CarroCompraActivity.productoEnCarroList = listaProductos;

                new BottomNavigationController().updateCountBadgeCart(listaProductos.size());

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listaProductos.size());
                notifyItemRangeChanged(position, holderAdapters.size());

                if(listaProductos.size() == 0){
                    no_prod_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.btnAumentarCarroCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = productoSeleccionado.getCantidad();
                cantidad = cantidad + 1;

                holder.editTextCantidadProductoCarroCompra.setText(cantidad+"");
                holder.txtCantidadCardview.setText("Cantidad: "+ cantidad+" (cambiar)");

                productoSeleccionado.setCantidad(cantidad);
                productosEnCarro.set(position, productoSeleccionado);
            }
        });

        holder.btnDisminuirCarroCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = productoSeleccionado.getCantidad();

                if(cantidad > 1){
                    cantidad = cantidad - 1;

                    holder.editTextCantidadProductoCarroCompra.setText(cantidad+"");
                    holder.txtCantidadCardview.setText("Cantidad: "+ cantidad+" (cambiar)");

                    productoSeleccionado.setCantidad(cantidad);
                    productosEnCarro.set(position, productoSeleccionado);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreProductoCarroCompra, txtPrecioProductoCarroCompra, txtCantidadCardview, editTextCantidadProductoCarroCompra;
        ImageView imgViewProductoCarroCompra, close_img_dialog_carroCompra;
        Button btnDisminuirCarroCompra, btnAumentarCarroCompra;
        ExpandableLayout expandableLayout;

        public CardViewHolder(View itemView) {
            super(itemView);

            editTextCantidadProductoCarroCompra = itemView.findViewById(R.id.editTextCantidadProductoCarroCompra);
            txtNombreProductoCarroCompra = itemView.findViewById(R.id.txtNombreProductoCarroCompra);
            txtPrecioProductoCarroCompra = itemView.findViewById(R.id.txtPrecioProductoCarroCompra);
            imgViewProductoCarroCompra = itemView.findViewById(R.id.imgViewProductoCarroCompra);
            close_img_dialog_carroCompra = itemView.findViewById(R.id.close_img_dialog_carroCompra);
            txtCantidadCardview = itemView.findViewById(R.id.txtCant);

            btnDisminuirCarroCompra = itemView.findViewById(R.id.btnDisminuirCarroCompra);
            btnAumentarCarroCompra = itemView.findViewById(R.id.btnAumentarCarroCompra);

            expandableLayout = itemView.findViewById(R.id.expandable_layout_cardview);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
                @Override
                public void onExpansionUpdate(float expansionFraction, int state) {
                    if (state == ExpandableLayout.State.EXPANDING) {
                        recyclerView_carro_compra.smoothScrollToPosition(pos);
                    }
                }
            });

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
