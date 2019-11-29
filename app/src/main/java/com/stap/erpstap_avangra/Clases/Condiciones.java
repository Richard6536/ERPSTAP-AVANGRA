package com.stap.erpstap_avangra.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Condiciones {

    int id;
    String nombreItem;
    String descripcion;
    boolean esTitulo;
    public static Condiciones condicionSeleccionada = null;

    List<PasosCondicion> pasosCondicions = new ArrayList<>();

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEsTitulo() {
        return esTitulo;
    }

    public void setEsTitulo(boolean esTitulo) { this.esTitulo = esTitulo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }
    public String getNombreItem() {
        return nombreItem;
    }

    public List<PasosCondicion> getPasosCondicions() {
        return pasosCondicions;
    }

    public void setPasosCondicions(List<PasosCondicion> pasosCondicions) {
        this.pasosCondicions = pasosCondicions;
    }

    @Override
    public String toString() {
        return nombreItem;
    }

    public List<Condiciones> cargarCondiciones(JSONArray condiciones){
        try {

            List<Condiciones> condicionesList = new ArrayList<>();
            for(int x = 0; x <condiciones.length(); x++){
                JSONObject condicionJSONObject = null;
                condicionJSONObject = condiciones.getJSONObject(x);

                int id = condicionJSONObject.getInt("Id");
                String nombre = condicionJSONObject.getString("Nombre");

                JSONArray pasosJSONArray = condicionJSONObject.getJSONArray("Pasos");

                Condiciones condicion = new Condiciones();
                condicion.setId(id);
                condicion.setNombreItem(nombre);

                if(pasosJSONArray.length() > 0){
                    List<PasosCondicion> listaPasos = crearPasoCondicion(pasosJSONArray);
                    condicion.setPasosCondicions(listaPasos);
                }

                condicionesList.add(condicion);
            }

            return condicionesList;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<PasosCondicion> crearPasoCondicion(JSONArray pasos){

        try{

            List<PasosCondicion> listaPasosCondicion = new ArrayList<>();

            for(int y = 0; y <pasos.length(); y++){

                JSONObject pasoJSONObject = null;
                pasoJSONObject = pasos.getJSONObject(y);

                int idPaso = pasoJSONObject.getInt("Id");
                int ordenPaso = pasoJSONObject.getInt("Orden");
                String nombrePaso = pasoJSONObject.getString("Nombre");
                String descripcionPaso = pasoJSONObject.getString("Descripcion");

                PasosCondicion pasoCondicion = new PasosCondicion(idPaso, ordenPaso, nombrePaso, descripcionPaso);
                listaPasosCondicion.add(pasoCondicion);
            }

            return listaPasosCondicion;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> obtenerNombresItem(JSONArray condiciones){
        try {
            List<String> nombresItemList = new ArrayList<>();

            for(int x = 0; x <condiciones.length(); x++){
                boolean encontrado = false;
                JSONObject condicionJSONObject = null;
                condicionJSONObject = condiciones.getJSONObject(x);
                String nombreItem = condicionJSONObject.getString("NombreItem");

               for(int y = 0; y <nombresItemList.size(); y++){
                   if(nombreItem.equals(nombresItemList.get(y))){
                       encontrado = true;
                   }
               }

               if(encontrado == false){
                   nombresItemList.add(nombreItem);
               }

            }

            return nombresItemList;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
