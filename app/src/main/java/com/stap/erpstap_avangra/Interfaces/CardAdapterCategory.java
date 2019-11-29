package com.stap.erpstap_avangra.Interfaces;

import androidx.cardview.widget.CardView;

public interface CardAdapterCategory {

    public final int MAX_ELEVATION_FACTOR = 8;
    float getBaseElevation();
    CardView getCardViewAt(int position);
    int getCount();
}
