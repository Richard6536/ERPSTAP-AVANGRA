package com.stap.erpstap_avangra.Clases;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment;
import com.stap.erpstap_avangra.Fragments.ProductosListFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.stap.erpstap_avangra.Clases.ProductoEnCarro.productosEnCarro;

public class Producto  {
    int id;
    String nombre;
    int valor;
    int cantidad;
    String descripcion;
    int categoriaId;
    String categoriaNombre;
    boolean marcado;
    boolean isChecked;


    List<String> imagenes = new ArrayList<>();

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public static List<Producto> productosList = new ArrayList<>();

    public static class ObtenerProductosPorPaso extends AsyncTask<String,String, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... parametros) {


            String JsonResponse = "";
            String datos = parametros[0];
            HttpURLConnection urlConnection = null;
            //Parámetros
            BufferedReader reader = null;
            OutputStream os = null;

            try {
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/ObtenerProductosPorPasoV2");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.toString().getBytes());
                os.flush();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine = "";
                while ((inputLine = reader.readLine()) != null)
                {
                    buffer.append(inputLine);
                }

                JsonResponse = buffer.toString();
                JSONObject resultadoJSON = new JSONObject(JsonResponse);

                return resultadoJSON;

            } catch (IOException e) {
                e.printStackTrace();

                try{
                    JSONObject jsonObjectError500 = new JSONObject();
                    jsonObjectError500.put("TipoRespuesta","ERROR");
                    jsonObjectError500.put("Mensaje","Error 500. Ha ocurrido un problema con el servidor, intente más tarde.");

                    return  jsonObjectError500;

                }catch (JSONException a){
                    a.getStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

            return null;
        }

        protected void onPostExecute(JSONObject respuestaOdata)
        {
            try
            {
                String activityActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();

                if(activityActual.equals("CarroCompraPasosFragment"))
                {
                    CarroCompraPasosFragment cc = (CarroCompraPasosFragment) ControllerActivity.fragmentAbiertoActual;
                    cc.obtenerProductosPasosRespuesta(respuestaOdata);
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public List<Producto> ordenarProductos(String ordenType, List<Producto> productos){

        if(ordenType.equals("alphabetic_type")){

            Collections.sort(productos, new Comparator<Producto>() {
                @Override
                public int compare(final Producto object1, final Producto object2) {
                    return object1.getNombre().compareTo(object2.getNombre());
                }
            });
        }
        else if(ordenType.equals("higher_price_type")){

            Collections.sort(productos, new Comparator<Producto>() {
                @Override
                public int compare(final Producto object1, final Producto object2) {
                    return ((Integer) object1.getValor()).compareTo(object2.getValor());
                }
            });

        }
        else if(ordenType.equals("lower_price_type")){

            Collections.sort(productos, new Comparator<Producto>() {
                @Override
                public int compare(final Producto object1, final Producto object2) {
                    return ((Integer) object1.getValor()).compareTo(object2.getValor());
                }
            });
        }

            return productos;
    }

}
