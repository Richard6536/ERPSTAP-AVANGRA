package com.stap.erpstap_avangra.Clases;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Moneda {

    public String Format(int valor){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        DecimalFormat df = (DecimalFormat)nf;
        return df.format(valor);
    }
}
