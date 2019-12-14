package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.github.islamkhsh.CardSliderAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Clases.AnimationUtility;
import com.stap.erpstap_avangra.Clases.Anuncio;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.PaletteImage;
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
        Picasso.with(context).load(anuncio.getUrlImage()).into(anuncio_image, new Callback() {
            @Override
            public void onSuccess() {
                if (new PaletteImage().getPosterPalette()!=null){
                    new PaletteImage().setUpInfoBackgroundColor(linearLayoutCardViewAnuncio , new PaletteImage().getPosterPalette());

                }
                else {
                    Bitmap bitmap = ((BitmapDrawable) anuncio_image.getDrawable()).getBitmap();

                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            new PaletteImage().setPosterPalette(palette);

                            new PaletteImage().setUpInfoBackgroundColor(linearLayoutCardViewAnuncio, palette);

                        }
                    });
                }
            }

            @Override
            public void onError() {

            }
        });

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
