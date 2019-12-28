package com.stap.erpstap_avangra.Clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipoCaracteristicaProductoBusquedaAvanzada {

    public int id;
    public float valueNumerico;
    public float valueRango;
    public boolean valueCheckbox;
    public String alternativa;
    public int tipoCaract;

    List<TipoCaracteristicaProductoBusquedaAvanzada> tPBAList = new ArrayList<>();
    public static Map<Integer, TipoCaracteristicaProductoBusquedaAvanzada> tipoCaractMap = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValueNumerico() {
        return valueNumerico;
    }

    public void setValueNumerico(float valueNumerico) {
        this.valueNumerico = valueNumerico;
    }

    public float getValueRango() {
        return valueRango;
    }

    public void setValueRango(float valueRango) {
        this.valueRango = valueRango;
    }

    public boolean isValueCheckbox() {
        return valueCheckbox;
    }

    public void setValueCheckbox(boolean valueCheckbox) {
        this.valueCheckbox = valueCheckbox;
    }

    public String getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(String alternativa) {
        this.alternativa = alternativa;
    }

    public int getTipoCaract() {
        return tipoCaract;
    }

    public void setTipoCaract(int tipoCaract) {
        this.tipoCaract = tipoCaract;
    }

    public void clearMapList(){
        tipoCaractMap = new HashMap<>();
    }

    public void agregarSeleccion(int _id, int _positionPager, float _valueNumerico, float _valueRango, boolean _valueCheckbox, String _alternativa, int _tipoCaract){

        TipoCaracteristicaProductoBusquedaAvanzada tp = new TipoCaracteristicaProductoBusquedaAvanzada();
        tp.setId(_id);
        tp.setValueNumerico(_valueNumerico);
        tp.setValueRango(_valueRango);
        tp.setValueCheckbox(_valueCheckbox);
        tp.setAlternativa(_alternativa);
        tp.setTipoCaract(_tipoCaract);

        tipoCaractMap.put(_positionPager, tp);
    }
}
