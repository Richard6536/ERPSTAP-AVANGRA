package com.stap.erpstap_avangra.Clases;

public class CaracteristicaProducto {
    int id;
    int idCatacteristica;
    String nombre;
    String descripcion;
    String valor;
    boolean coincideBusqueda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCatacteristica() {
        return idCatacteristica;
    }

    public void setIdCatacteristica(int idCatacteristica) {
        this.idCatacteristica = idCatacteristica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean isCoincideBusqueda() {
        return coincideBusqueda;
    }

    public void setCoincideBusqueda(boolean coincideBusqueda) {
        this.coincideBusqueda = coincideBusqueda;
    }
}
