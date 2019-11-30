package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Activity.CarroCompraActivity;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import static com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment.productosTemporalEnCarroFragment;
import static com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment.recyclerView_carro_compra_pasos;

public class CardviewAdapterCarroCompraPasos extends RecyclerView.Adapter<CardviewAdapterCarroCompraPasos.CardViewHolder> implements ExpandableLayout.OnExpansionUpdateListener{

    public List<Producto> listaProductos = new ArrayList<>();
    private CardviewAdapterCarroCompraPasos.CardViewHolder holder;
    private CardviewAdapterCarroCompraPasos.CardViewHolder holderAnterior = holder;

    private static final int UNSELECTED = -1;
    public int pos;
    private int selectedItem = UNSELECTED;

    Producto productoSeleccionado;

    private CardviewAdapterCarroCompraPasos.OnItemClickListener onItemClickListener; // Global scope

    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {

    }

    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, Producto auto);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterCarroCompraPasos(Context mCtx, List<Producto> lista, CardviewAdapterCarroCompraPasos.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaProductos = lista;
        this.onItemClickListener = onItemClickListener;

        //productosEnCarroTemporal.addAll(listaProductos);
        //listaProductosFull.addAll(listaProductos);
    }

    @NonNull
    @Override
    public CardviewAdapterCarroCompraPasos.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_carrocompra_pasos, null);

        return new CardviewAdapterCarroCompraPasos.CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CardviewAdapterCarroCompraPasos.CardViewHolder _holder, final int position) {
        holder = _holder;
        productoSeleccionado = listaProductos.get(position);
        pos = position;

        holder.txtNombreProducto.setText(productoSeleccionado.getNombre());
        holder.txtPrecioProducto.setText("$"+ new Moneda().Format(productoSeleccionado.getValor()));

        holder.txtCantidadPasosCarroCompra.setText("Cantidad: " + productoSeleccionado.getCantidad());

        List<String> imagenes = productoSeleccionado.getImagenes();

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

                holder = _holder;
                productoSeleccionado = listaProductos.get(position);

                if(holder.checkBox_carrocompra_pasos.isChecked()){
                    productoSeleccionado.setChecked(false);
                    holder.checkBox_carrocompra_pasos.setChecked(false);
                    holder.layoutCardview_carrocompa_pasos.setBackgroundColor(Color.parseColor("#ffffff"));
                    holder.itemView.setSelected(true);
                    holder.expandableLayout.collapse();
                }
                else{
                    productoSeleccionado.setChecked(true);
                    holder.checkBox_carrocompra_pasos.setChecked(true);
                    holder.editTextCantidadProductoCarroCompra.setText(productoSeleccionado.getCantidad()+"");
                    holder.layoutCardview_carrocompa_pasos.setBackgroundColor(Color.parseColor("#ffffff"));
                    holder.itemView.setSelected(true);
                    holder.expandableLayout.expand();
                }


                selectedItem = position;
                //holderAnterior = holder;
                listaProductos.set(position, productoSeleccionado);
                productosTemporalEnCarroFragment = listaProductos;
                /*
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {

                }*/

                //onItemClickListener.onItemClicked(position, 0, productoSeleccionado);
            }
        });

        holder.imgViewProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productoSeleccionado = listaProductos.get(position);
                onItemClickListener.onItemClicked(position, 0, productoSeleccionado);
            }
        });

        holder.btnAumentarCarroCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder = _holder;
                productoSeleccionado = listaProductos.get(position);

                int cantidad = productoSeleccionado.getCantidad();
                cantidad = cantidad + 1;

                holder.editTextCantidadProductoCarroCompra.setText(cantidad+"");
                productoSeleccionado.setCantidad(cantidad);

                listaProductos.set(position, productoSeleccionado);
                productosTemporalEnCarroFragment = listaProductos;

                holder.txtCantidadPasosCarroCompra.setText("Cantidad: " +cantidad);
                //onItemClickListener.onItemClicked(position, 0, productoSeleccionado);
            }
        });


        holder.btnDisminuirCarroCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder = _holder;
                productoSeleccionado = listaProductos.get(position);

                int cantidad = productoSeleccionado.getCantidad();

                if(cantidad > 1){
                    cantidad = cantidad - 1;

                    holder.editTextCantidadProductoCarroCompra.setText(cantidad+"");
                    productoSeleccionado.setCantidad(cantidad);

                    listaProductos.set(position,productoSeleccionado);
                    productosTemporalEnCarroFragment = listaProductos;

                    holder.txtCantidadPasosCarroCompra.setText("Cantidad: " +cantidad);
                    //onItemClickListener.onItemClicked(position, 0, productoSeleccionado);
                }

            }
        });

        if(productoSeleccionado.isMarcado()){
            productoSeleccionado.setChecked(true);
            holder.checkBox_carrocompra_pasos.setChecked(true);
            holder.expandableLayout.expand();
        }

        productosTemporalEnCarroFragment = listaProductos;
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreProducto, txtPrecioProducto, editTextCantidadProductoCarroCompra , txtCantidadPasosCarroCompra;
        ImageView imgViewProducto;
        Button btnDisminuirCarroCompra, btnAumentarCarroCompra;
        ExpandableLayout expandableLayout;
        CheckBox checkBox_carrocompra_pasos;
        LinearLayout layoutCardview_carrocompa_pasos;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtNombreProducto = itemView.findViewById(R.id.txtNombreProductoCarroCompraPasos);
            txtPrecioProducto = itemView.findViewById(R.id.txtPrecioProductoCarroCompraPasos);

            txtCantidadPasosCarroCompra = itemView.findViewById(R.id.txtCantidadPasosCarroCompra);

            imgViewProducto = itemView.findViewById(R.id.imgViewProductoCarroCompraPasos);

            editTextCantidadProductoCarroCompra = itemView.findViewById(R.id.editTextCantidadProductoCarroCompraPasos);
            btnDisminuirCarroCompra = itemView.findViewById(R.id.btnDisminuirCarroCompraPasos);
            btnAumentarCarroCompra = itemView.findViewById(R.id.btnAumentarCarroCompraPasos);
            checkBox_carrocompra_pasos = itemView.findViewById(R.id.checkBox_carrocompra_pasos);
            layoutCardview_carrocompa_pasos = itemView.findViewById(R.id.layoutCardview_carrocompa_pasos);

            expandableLayout = itemView.findViewById(R.id.expandable_layout_cardview_carroCompra_pasos);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
                @Override
                public void onExpansionUpdate(float expansionFraction, int state) {
                    Log.d("ExpandableLayout", "State: " + state);
                    if (state == ExpandableLayout.State.EXPANDING) {
                        recyclerView_carro_compra_pasos.smoothScrollToPosition(pos);
                    }
                }
            });
        }
    }


}
