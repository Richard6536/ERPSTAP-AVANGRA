package com.stap.erpstap_avangra.Clases;

import android.os.AsyncTask;
import android.util.Log;

import com.stap.erpstap_avangra.Activity.CarroCompraActivity;

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
import java.util.List;

public class ProductoEnCarro {
    int id;
    String nombre;
    String valor;
    int cantidad;
    List<String> imagenes = new ArrayList<>();

    public static List<ProductoEnCarro> productosEnCarro = new ArrayList<>();

    public ProductoEnCarro(int id, String nombre, String valor, int cantidad, List<String> imagenes) {
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

    public List<String> getImagenes() {
        return imagenes;
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

            Log.d("Session", "params: "+datos);
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
                Log.d("Session", "pass1: " + "pass");

                int status = urlConnection.getResponseCode();
                InputStream inputStream = urlConnection.getInputStream();
                int status2 = urlConnection.getResponseCode();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                }
                Log.d("Session", "pass2: " + "pass2");
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine = "";
                while ((inputLine = reader.readLine()) != null)
                {
                    buffer.append(inputLine);
                }

                JsonResponse = buffer.toString();
                JSONObject resultadoJSON = new JSONObject(JsonResponse);
                Log.d("Session", "resultadoJSON: " + resultadoJSON);

                return resultadoJSON;

            } catch (IOException e) {
                Log.d("Session", "Error1: " + e.getMessage());
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
                Log.d("Session", "Error2: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.d("Session", "Error3: " + e.getMessage());
                    }
                }
            }

            return null;
        }

        protected void onPostExecute(JSONObject respuestaOdata)
        {
            try
            {
                String activityActual = ControllerActivity.activiyAbiertaActual.getClass().getSimpleName();
                if(activityActual.equals("CarroCompraActivity"))
                {
                    CarroCompraActivity carroCompraActivity = new CarroCompraActivity();
                    carroCompraActivity.RespuestaEnvioCotizacion(respuestaOdata);
                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
