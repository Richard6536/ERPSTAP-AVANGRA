package com.stap.erpstap_avangra.Clases;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    int id;
    String nombre;
    int valor;
    int cantidad;

    List<String> imagenes = new ArrayList<>();

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public List<String> getImagenes() {
        return imagenes;
    }


}
