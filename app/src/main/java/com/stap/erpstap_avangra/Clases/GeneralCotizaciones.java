package com.stap.erpstap_avangra.Clases;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.stap.erpstap_avangra.ClasesAbstractas.ListDataCotizaciones;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GeneralCotizaciones extends ListDataCotizaciones {
    private Cotizacion cotizacion;

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }

    public HashMap<String, List<Cotizacion>> groupDataIntoHashMap(List<Cotizacion> listOfPojosOfJsonArray) {
        HashMap<String, List<Cotizacion>> groupedHashMap = new HashMap<>();

        for (Cotizacion cotizacion : listOfPojosOfJsonArray) {

            String hashMapKey = cotizacion.getFecha();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // La Key ya está en el HashMap; agrego el objeto
                groupedHashMap.get(hashMapKey).add(cotizacion);
            } else {
                // La Key no existe en el HashMap; creo una nueva key-value
                List<Cotizacion> list = new ArrayList<>();
                list.add(cotizacion);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }

    public List<ListDataCotizaciones> ordenarListaCotizacionesHashMap(HashMap<String, List<Cotizacion>> groupedHashMap) {
        List<ListDataCotizaciones> listDataCotizaciones = new ArrayList<>();

        Map<String, List<Cotizacion>> reverseSortedMap = new TreeMap<String, List<Cotizacion>>(Collections.reverseOrder());
        reverseSortedMap.putAll(groupedHashMap);

        for (String date : reverseSortedMap.keySet()) {
            List<ListDataCotizaciones> listDataCotizacionesTemporal = new ArrayList<>();
            String temporalDate = date;

            CotizacionesDate dateItem = new CotizacionesDate();

            if(isSameDay(date)){
                date = "Hoy";
            }

            dateItem.setDate(date);
            listDataCotizaciones.add(dateItem);


            for (Cotizacion cotizacion : reverseSortedMap.get(temporalDate)) {
                GeneralCotizaciones generalItem = new GeneralCotizaciones();
                generalItem.setCotizacion(cotizacion);//setBookingDataTabs(bookingDataTabs);

                listDataCotizacionesTemporal.add(generalItem);
            }

            Collections.reverse(listDataCotizacionesTemporal);
            listDataCotizaciones.addAll(listDataCotizacionesTemporal);
        }

        return listDataCotizaciones;
    }

    public boolean isSameDay(String date){

        try {

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

            Date dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Date actualTime = new SimpleDateFormat("yyyy-MM-dd").parse(timeStamp);

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(dateTime);
            cal2.setTime(actualTime);

            boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

            return sameDay;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
