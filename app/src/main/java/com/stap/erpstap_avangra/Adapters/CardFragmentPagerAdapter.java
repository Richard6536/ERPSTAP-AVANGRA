package com.stap.erpstap_avangra.Adapters;

import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.gson.JsonArray;
import com.stap.erpstap_avangra.Clases.Categoria;
import com.stap.erpstap_avangra.Fragments.CardFragment;
import com.stap.erpstap_avangra.Interfaces.CardAdapterCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapterCategory {
    private List<CardFragment> fragments;
    private float baseElevation;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, JSONArray categorias) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;

        for(int x = 0; x < categorias.length(); x++) {

            JSONObject proveedor = null;

            try {

                proveedor = categorias.getJSONObject(x);

                int id = proveedor.getInt("Id");
                String nombre = proveedor.getString("Nombre");

                Categoria categoria = new Categoria();
                categoria.setId(id);
                categoria.setPosition(x);
                categoria.setNombre(nombre);

                Categoria.categoriasList.add(categoria);

                addCardFragment(new CardFragment());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CardFragment.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        fragments.add(fragment);
    }

}
