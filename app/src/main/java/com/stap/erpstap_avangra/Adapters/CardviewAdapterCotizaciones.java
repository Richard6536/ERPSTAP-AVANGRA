package com.stap.erpstap_avangra.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.CotizacionesDate;
import com.stap.erpstap_avangra.Clases.GeneralCotizaciones;
import com.stap.erpstap_avangra.ClasesAbstractas.ListDataCotizaciones;
import com.stap.erpstap_avangra.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class CardviewAdapterCotizaciones extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    List<ListDataCotizaciones> listDataCotizaciones = new ArrayList<>();
    List<ListDataCotizaciones> listDataCotizacionesFull = new ArrayList<>();
    Cotizacion cotizacion;

    private OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, Cotizacion cotizacion);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAdapterCotizaciones(Context mCtx, List<ListDataCotizaciones> lista, CardviewAdapterCotizaciones.OnItemClickListener onItemClickListener) {
        //Collections.reverse(lista);

        this.mCtx = mCtx;
        this.listDataCotizaciones = lista;
        this.onItemClickListener = onItemClickListener;

        listDataCotizacionesFull.addAll(listDataCotizaciones);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListDataCotizaciones.TYPE_GENERAL:
                View v1 = inflater.inflate(R.layout.cardview_cotizaciones, parent,
                        false);
                viewHolder = new GeneralViewHolder(v1);
                break;

            case ListDataCotizaciones.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.cardview_fecha_cotizacion, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        /*
        cotizacion = listaCotizaciones.get(position);
        if(first){
            first = false;

            TextView tv = new TextView(ControllerActivity.fragmentAbiertoActual.getContext());
            tv.setText("hallo hallo");
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            holder.linearLayout.addView(tv);

            holder.cardview_cotizaciones.setBackgroundColor(Color.parseColor("#ffe085"));
            holder.constraintLayoutCardViewCotizaciones.setVisibility(View.GONE);
        }
        else
        {
            holder.txtCountCardCotizaciones.setText(cotizacion.getPosition_cardview()+"");
            holder.txtCotizacionCodigo.setText(cotizacion.getCodigo());
            holder.txtFechaCotizacion.setText(cotizacion.getFecha());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cotizacion cotizacionSeleccionada = listaCotizaciones.get(position);
                    onItemClickListener.onItemClicked(position, 0, cotizacionSeleccionada);
                }
            });
        }
        */
        switch (viewHolder.getItemViewType()) {

            case ListDataCotizaciones.TYPE_GENERAL:

                final GeneralCotizaciones generalItem   = (GeneralCotizaciones) listDataCotizaciones.get(position);
                GeneralViewHolder generalViewHolder = (GeneralViewHolder) viewHolder;

                generalViewHolder.txtCotizacionCodigo.setText(generalItem.getCotizacion().getCodigo());
                generalViewHolder.txtFechaCotizacion.setText(generalItem.getCotizacion().getFecha());
                generalViewHolder.txtCountCardCotizaciones.setText(String.valueOf(generalItem.getCotizacion().getPosition_cardview()));

                generalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cotizacion cotizacionSeleccionada = generalItem.getCotizacion();
                        onItemClickListener.onItemClicked(position, 0, cotizacionSeleccionada);
                    }
                });
                break;

            case ListDataCotizaciones.TYPE_DATE:
                CotizacionesDate dateItem = (CotizacionesDate) listDataCotizaciones.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;

                dateViewHolder.txtTituloFecha.setText(dateItem.getDate());

                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return listDataCotizaciones.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return listDataCotizaciones != null ? listDataCotizaciones.size() : 0;
    }

    /*
    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtCotizacionCodigo, txtFechaCotizacion, txtCountCardCotizaciones;
        CardView cardview_cotizaciones;
        ConstraintLayout constraintLayoutCardViewCotizaciones;
        LinearLayout linearLayout;

        public CardViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            constraintLayoutCardViewCotizaciones = itemView.findViewById(R.id.constraintLayoutCardViewCotizaciones);
            cardview_cotizaciones = itemView.findViewById(R.id.cardview_cotizaciones);
            txtCotizacionCodigo = itemView.findViewById(R.id.txtCotizacionCodigo);
            txtFechaCotizacion = itemView.findViewById(R.id.txtFechaCotizacion);
            txtCountCardCotizaciones = itemView.findViewById(R.id.txtCountCardCotizaciones);
        }
    }*/

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Cotizacion> filterCotizacion = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filterCotizacion.add((Cotizacion) listDataCotizaciones);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Object object : listDataCotizacionesFull){
                    String nameClass = object.getClass().getName();
                    if(nameClass.equals("com.stap.erpstap_avangra.Clases.GeneralCotizaciones")){
                        GeneralCotizaciones generalCotizaciones = (GeneralCotizaciones)object;
                        if(generalCotizaciones.getCotizacion().getCodigo().toLowerCase().contains(filterPattern)){
                            filterCotizacion.add(generalCotizaciones.getCotizacion());
                        }
                    }

                }
            }

            HashMap<String, List<Cotizacion>> groupedHashMap = new GeneralCotizaciones().groupDataIntoHashMap(filterCotizacion);
            List<ListDataCotizaciones> listDataCotizaciones = new GeneralCotizaciones().ordenarListaCotizacionesHashMap(groupedHashMap);

            FilterResults results = new FilterResults();
            results.values = listDataCotizaciones;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(filterResults.values != null){
                listDataCotizaciones.clear();
                listDataCotizaciones.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
            else {
                listDataCotizaciones.clear();
                listDataCotizaciones.addAll(listDataCotizacionesFull);
                notifyDataSetChanged();
            }
        }
    };

    public void DateFilter(String desde, String hasta){

        //2019-08-15 ---- 2019-08-17
        List<Cotizacion> filterCotizacion = new ArrayList<>();

        try {
            Date fechaDesde = new SimpleDateFormat("yyyy-MM-dd").parse(desde);
            Date fechaHasta = new SimpleDateFormat("yyyy-MM-dd").parse(hasta);

            for(Object object : listDataCotizacionesFull){

                String nameClass = object.getClass().getName();
                if(nameClass.equals("com.stap.erpstap_avangra.Clases.GeneralCotizaciones")){

                    GeneralCotizaciones generalCotizaciones = (GeneralCotizaciones)object;
                    Date fechaCotizacion = new SimpleDateFormat("yyyy-MM-dd").parse(generalCotizaciones.getCotizacion().getFecha());

                    int resultDate = fechaCotizacion.compareTo(fechaDesde);
                    int resultDate2 = fechaCotizacion.compareTo(fechaHasta);

                    if(resultDate >= 0 && resultDate2 <= 0){
                        filterCotizacion.add(generalCotizaciones.getCotizacion());
                    }
                }
            }

            HashMap<String, List<Cotizacion>> groupedHashMap = new GeneralCotizaciones().groupDataIntoHashMap(filterCotizacion);
            List<ListDataCotizaciones> listDataCotizacionesResult = new GeneralCotizaciones().ordenarListaCotizacionesHashMap(groupedHashMap);

            if(listDataCotizacionesResult != null){
                listDataCotizaciones.clear();
                listDataCotizaciones.addAll((List) listDataCotizacionesResult);
                notifyDataSetChanged();
            }
            else {
                listDataCotizaciones.clear();
                listDataCotizaciones.addAll(listDataCotizacionesFull);
                notifyDataSetChanged();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void resetList(){
        listDataCotizaciones.clear();
        listDataCotizaciones.addAll(listDataCotizacionesFull);
        notifyDataSetChanged();
    }

    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTituloFecha;

        public DateViewHolder(View v) {
            super(v);
            this.txtTituloFecha = (TextView) v.findViewById(R.id.txtFechaTitulo);

        }
    }

    // View holder for general row item
    class GeneralViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtCotizacionCodigo, txtFechaCotizacion, txtCountCardCotizaciones;

        public GeneralViewHolder(View v) {
            super(v);
            this.txtCotizacionCodigo = (TextView) v.findViewById(R.id.txtCotizacionCodigo);
            this.txtFechaCotizacion = (TextView) v.findViewById(R.id.txtFechaCotizacion);
            this.txtCountCardCotizaciones = (TextView) v.findViewById(R.id.txtCountCardCotizaciones);
        }
    }
}
