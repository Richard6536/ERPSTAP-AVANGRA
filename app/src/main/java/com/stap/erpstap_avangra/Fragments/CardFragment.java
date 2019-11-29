package com.stap.erpstap_avangra.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Clases.Categoria;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.Interfaces.CardAdapterCategory;
import com.stap.erpstap_avangra.R;

import java.util.ArrayList;
import java.util.List;


public class CardFragment extends Fragment {
    private CardView cardView;
    private String imageURL;

    public static Fragment getInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapterCategory.MAX_ELEVATION_FACTOR);
        ImageView imageViewProductCategoryCard = (ImageView)view.findViewById(R.id.imageViewProductCategoryCard);

        int position = getArguments().getInt("position");
        final Categoria categoria = Categoria.categoriasList.get(position);

        for(Producto producto : Producto.productosList){
            if(producto.getCategoriaId() == categoria.getId()){
                if(producto.getImagenes().size() > 0){
                    imageURL = producto.getImagenes().get(0);
                    break;
                }
            }
        }

        imageViewProductCategoryCard.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(getContext()).load(imageURL).into(imageViewProductCategoryCard);

        TextView title = (TextView) view.findViewById(R.id.title);
        //Button button = (Button)view.findViewById(R.id.button);

        title.setText(categoria.getNombre());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Producto> productoListCategoria = new ArrayList<>();

                for(Producto producto : Producto.productosList){
                    if(producto.getCategoriaId() == categoria.getId()){
                        productoListCategoria.add(producto);
                    }
                }

                FragmentManager fm = getFragmentManager();
                ProductosListFragment fragm = (ProductosListFragment)fm.findFragmentById(R.id.fragment_container);
                fragm.agregarProductosAlAdapter(productoListCategoria);

                //new ProductosListFragment().agregarProductosAlAdapter(productoListCategoria);
            }
        });

        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}
