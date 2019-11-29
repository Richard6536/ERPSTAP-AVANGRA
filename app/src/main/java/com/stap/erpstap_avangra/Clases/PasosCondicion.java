package com.stap.erpstap_avangra.Clases;

public class PasosCondicion {

    public int Id;
    public int Orden;
    public String Nombre;
    public String Descripcion;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getOrden() {
        return Orden;
    }

    public void setOrden(int orden) {
        Orden = orden;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public PasosCondicion(int _id, int _orden, String _nombre, String _descripcion) {
        Id = _id;
        Orden = _orden;
        Nombre = _nombre;
        Descripcion = _descripcion;
    }
}
