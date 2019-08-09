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
    public static List<Condiciones> condicionesList;

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


    @Override
    public String toString() {
        return nombreItem;
    }

    public void cargarCondiciones(JSONArray condiciones){
        try {

            condicionesList = new ArrayList<>();
            for(int x = 0; x <condiciones.length(); x++){
                JSONObject condicionJSONObject = null;
                condicionJSONObject = condiciones.getJSONObject(x);

                int id = condicionJSONObject.getInt("Id");
                String nombre = condicionJSONObject.getString("Nombre");

                Condiciones condicion = new Condiciones();
                condicion.setId(id);
                condicion.setNombreItem(nombre);

                condicionesList.add(condicion);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
