package com.stap.erpstap_avangra.Adapters;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter {
    int id;
    String nombre;
    String valor;
    String cantidad;
    List<String> imagenes = new ArrayList<>();

    public SpinnerAdapter(int id, String nombre, String valor, String cantidad, List<String> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.valor = valor;
        this.cantidad = cantidad;
        this.imagenes = imagenes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setValor(String precio) {
        this.valor = valor;
    }

    public void setCantidad(String descripcion) {
        this.cantidad = cantidad;
    }

    public String getValor() {
        return valor;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

}
