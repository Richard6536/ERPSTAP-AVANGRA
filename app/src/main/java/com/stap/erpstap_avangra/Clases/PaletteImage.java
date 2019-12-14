package com.stap.erpstap_avangra.Clases;

import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.stap.erpstap_avangra.R;

public class PaletteImage {
    public Palette posterPalette;

    public Palette getPosterPalette() {
        return posterPalette;
    }

    public void setPosterPalette(Palette posterPalette) {
        this.posterPalette = posterPalette;
    }

    Palette.Swatch getMostPopulousSwatch(Palette palette) {
        Palette.Swatch mostPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
                    mostPopulous = swatch;
                }
            }
        }
        return mostPopulous;
    }

    public void setUpInfoBackgroundColor(LinearLayout ll, Palette palette) {
        Palette.Swatch swatch = getMostPopulousSwatch(palette);
        if(swatch != null){
            int startColor = ContextCompat.getColor(ll.getContext(), R.color.black);
            int endColor = swatch.getRgb();

            AnimationUtility.animateBackgroundColorChange(ll, startColor, endColor);
        }
    }
}
