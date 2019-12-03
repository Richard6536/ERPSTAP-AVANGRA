package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.islamkhsh.CardSliderAdapter;
import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Clases.Anuncio;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.R;

import java.util.ArrayList;

public class AnunciosAdapter extends CardSliderAdapter<Anuncio> {

    Context context;

    public AnunciosAdapter(ArrayList<Anuncio> anuncio, Context ctx){
        super(anuncio);
        this.context = ctx;
    }

    @Override
    public void bindView(int i, final View view, final Anuncio anuncio) {

        ImageView anuncio_image = view.findViewById(R.id.anuncio_image);
        LinearLayout linearLayoutCardViewAnuncio = view.findViewById(R.id.linearLayoutCardViewAnuncio);

        anuncio_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(context).load(anuncio.getUrlImage()).into(anuncio_image);

        linearLayoutCardViewAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                //Toast.makeText(context, anuncio.getTitulo(), Toast.LENGTH_LONG).show();
                new DialogBox().AnuncioDialog(context, anuncio.getTitulo(), anuncio.getDescripcion());
            }
        });
    }

    @Override
    public int getItemContentLayout(int i) {
        return R.layout.cardview_anuncio;
    }
}
