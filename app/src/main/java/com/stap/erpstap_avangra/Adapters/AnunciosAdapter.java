package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.islamkhsh.CardSliderAdapter;
import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Clases.Anuncio;
import com.stap.erpstap_avangra.R;

import java.util.ArrayList;

public class AnunciosAdapter extends CardSliderAdapter<Anuncio> {

    Context context;

    public AnunciosAdapter(ArrayList<Anuncio> anuncio, Context ctx){
        super(anuncio);
        this.context = ctx;
    }

    @Override
    public void bindView(int i, View view, Anuncio anuncio) {

        TextView anuncio_title = view.findViewById(R.id.anuncio_title);
        TextView anuncio_description = view.findViewById(R.id.anuncio_description);
        ImageView anuncio_image = view.findViewById(R.id.anuncio_image);

        anuncio_title.setText(anuncio.getTitulo());
        anuncio_description.setText(anuncio.getDescripcion());

        anuncio_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(context).load(anuncio.getUrlImage()).into(anuncio_image);
    }

    @Override
    public int getItemContentLayout(int i) {
        return R.layout.cardview_anuncio;
    }
}
