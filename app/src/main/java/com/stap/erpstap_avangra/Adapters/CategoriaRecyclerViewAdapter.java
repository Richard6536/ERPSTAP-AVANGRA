package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Clases.Categoria;
import com.stap.erpstap_avangra.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriaRecyclerViewAdapter extends ArrayAdapter<Categoria> {

    private Context activity;
    private List<Categoria> lPerson;
    private static LayoutInflater inflater = null;

    public CategoriaRecyclerViewAdapter (Context activity, int textViewResourceId, List<Categoria> _lPerson) {
        super(activity, textViewResourceId, _lPerson);
        try {
            this.activity = activity;
            this.lPerson = _lPerson;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lPerson.size();
    }

    public Categoria getItem(int position) {
        Categoria categoria = lPerson.get(position);
        return categoria;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView txtCategoriaNombreListView;
        public TextView txtCategoriaIdListView;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.item_categoria, null);
                holder = new ViewHolder();

                holder.txtCategoriaNombreListView = (TextView) vi.findViewById(R.id.txtCategoriaNombreListView);
                holder.txtCategoriaIdListView = (TextView) vi.findViewById(R.id.txtCategoriaIdListView);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }



            holder.txtCategoriaNombreListView.setText(lPerson.get(position).getNombre());
            holder.txtCategoriaIdListView.setText(lPerson.get(position).getId());


        } catch (Exception e) {


        }
        return vi;
    }
}
