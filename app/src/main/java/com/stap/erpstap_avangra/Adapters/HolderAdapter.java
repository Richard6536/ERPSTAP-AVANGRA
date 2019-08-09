package com.stap.erpstap_avangra.Adapters;

import java.util.ArrayList;
import java.util.List;

public class HolderAdapter {
    public CardviewAdapterCarroCompra.CardViewHolder holder;
    public int position;

    public void setHolder(CardviewAdapterCarroCompra.CardViewHolder holder) {
        this.holder = holder;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CardviewAdapterCarroCompra.CardViewHolder getHolder() {
        return holder;
    }

    public int getPosition() {
        return position;
    }
}
