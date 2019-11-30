package com.stap.erpstap_avangra.Clases;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment;
import com.stap.erpstap_avangra.Fragments.VerProductoFragment;

import org.json.JSONArray;
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
import java.util.Iterator;
import java.util.List;

public class ProductoEnCarro {
    int id;
    String nombre;
    String valor;
    int cantidad;
    int paso;
    List<String> imagenes = new ArrayList<>();

    public static List<ProductoEnCarro> productosEnCarro = new ArrayList<>();
    public static List<ProductoEnCarro> productosEnCarroTemporalList = new ArrayList<>();

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

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getValor() {
        return valor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public int getPaso() {
        return paso;
    }

    public void setPaso(int paso) {
        this.paso = paso;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void crearProductoEnCarro(int _id, String _nombre, String _valor, int _cantidad, List<String> _imagenes){
        ProductoEnCarro productoEnCarro = new ProductoEnCarro();
        productoEnCarro.setId(_id);
        productoEnCarro.setNombre(_nombre);
        productoEnCarro.setCantidad(_cantidad);
        productoEnCarro.setValor(_valor);
        productoEnCarro.setImagenes(_imagenes);
        productoEnCarro.setPaso(-1);

        productosEnCarro.add(productoEnCarro);
    }

    public static void limpiarListaProductosEnCarro(){

        for(Iterator<ProductoEnCarro> it = productosEnCarro.iterator(); it.hasNext();) {
            ProductoEnCarro element = it.next();
            if(element.getPaso() > -1) {
                it.remove();
            }
        }
    }
    public static class CrearCotizacion extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/CrearCotizacion");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.toString().getBytes());
                os.flush();

                int status = urlConnection.getResponseCode();
                InputStream inputStream = urlConnection.getInputStream();
                int status2 = urlConnection.getResponseCode();

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
                if(activityActual.equals("CarroCompraMainFragment"))
                {
                    CarroCompraMainFragment carroCompraActivity = new CarroCompraMainFragment();
                    carroCompraActivity.RespuestaEnvioCotizacion(respuestaOdata);
                }
                else if(activityActual.equals("CarroCompraPasosFragment")){
                    CarroCompraPasosFragment carroCompraActivity = new CarroCompraPasosFragment();
                    carroCompraActivity.RespuestaEnvioCotizacion(respuestaOdata);
                }
            }
            catch (Exception e)
            {

            }
        }

    }


    public JSONArray obtenerIdsProductos(){

        JSONArray idsProductosJsonArray = new JSONArray();

        for(ProductoEnCarro pc : productosEnCarro){
            idsProductosJsonArray.put(pc.getId());
        }

        return idsProductosJsonArray;
    }
}
