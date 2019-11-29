package com.stap.erpstap_avangra.Clases;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

    public int id;
    public int position;
    public String Nombre;

    public static JSONArray listaCategoriasJsonArray = new JSONArray();
    public static List<Categoria> categoriasList = new ArrayList<>();
    public static Categoria categoriaSeleccionada = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public List<Producto> buscarProductosPorCategoria(int _id){

        List<Producto> productoListCategorias = new ArrayList<>();

        for(Producto producto : Producto.productosList){
            if(producto.getCategoriaId() == _id) {
                productoListCategorias.add(producto);
            }
        }

        return productoListCategorias;
    }

    public Categoria buscarCategoria(int _id){

        Categoria categoriaEncontrada = null;

        for(Categoria categoria : Categoria.categoriasList){
            if(categoria.getId() == _id) {
                categoriaEncontrada = categoria;
                break;
            }
        }
        return categoriaEncontrada;
    }

    public void ordenarCategoriasPrincipales(String nombreCategoria){
        for(int x = 0; x < listaCategoriasJsonArray.length(); x++){

            JSONObject proveedor = null;

            try {

                proveedor = listaCategoriasJsonArray.getJSONObject(x);

                final int id = proveedor.getInt("Id");
                final String nombre = proveedor.getString("Nombre");

                if(nombre.equals(nombreCategoria)){

                    final Categoria categoria = new Categoria();
                    categoria.setId(id);
                    categoria.setPosition(x);
                    categoria.setNombre(nombre);

                    categoriasList.add(categoria);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
