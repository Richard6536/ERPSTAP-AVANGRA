package com.stap.erpstap_avangra.Clases;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.stap.erpstap_avangra.Fragments.CotizacionesListFragment;

import java.util.Calendar;
import java.util.Date;

import static com.stap.erpstap_avangra.Fragments.CotizacionesListFragment.txtFechaToolbarDesde;
import static com.stap.erpstap_avangra.Fragments.CotizacionesListFragment.txtFechaToolbarHasta;

public class CalendarViewFilter  implements DatePickerDialog.OnDateSetListener {

    String fechaInicio;
    String fechaFinal;
    int posicion;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void createCalendar(Activity activity, int _posicion){

        posicion = _posicion;

        Date c = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(c);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,CalendarViewFilter.this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        int mes = i1+1;
        if(posicion == 0){
            txtFechaToolbarDesde.setText(i + "-" + mes + "-" + i2);
        }
        else if(posicion == 1){
            txtFechaToolbarHasta.setText(i + "-" + mes + "-" + i2);
        }
    }
}
