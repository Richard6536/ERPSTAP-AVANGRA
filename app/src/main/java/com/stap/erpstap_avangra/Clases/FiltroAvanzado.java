package com.stap.erpstap_avangra.Clases;

import android.os.AsyncTask;

import com.stap.erpstap_avangra.Activity.BusquedaAvanzadaActivity;
import com.stap.erpstap_avangra.Fragments.BusquedaAvanzadaFormFragment;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment;

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
import java.util.List;

public class FiltroAvanzado {

    public int id;
    public int orden;
    public int position;
    public String nombre;
    public String descripcion;
    public int tipo;
    public List<String> alternativas = new ArrayList<>();

    public static List<FiltroAvanzado> filtroAvanzadoList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<String> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<String> alternativas) {
        this.alternativas = alternativas;
    }

    public static boolean is_busqueda_avanzada = false;

    public void crearFiltroAvanzado(int _id, int _orden, int _position, String _nombre, String _descripcion, int _tipo, ArrayList<String> _alternativasList){

        FiltroAvanzado filtroAvanzado = new FiltroAvanzado();
        filtroAvanzado.setId(_id);
        filtroAvanzado.setOrden(_orden);
        filtroAvanzado.setPosition(_position);
        filtroAvanzado.setNombre(_nombre);
        filtroAvanzado.setDescripcion(_descripcion);
        filtroAvanzado.setTipo(_tipo);
        filtroAvanzado.setAlternativas(_alternativasList);

        filtroAvanzadoList.add(filtroAvanzado);
    }

    public int getFiltroAvanzadoCount(){
        return filtroAvanzadoList.size();
    }

    public static class ObtenerFormulario extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/ObtenerFormularioCotizador");
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
                String activityActual = ControllerActivity.activiyAbiertaActual.getClass().getSimpleName();
                if(activityActual.equals("BusquedaAvanzadaActivity"))
                {
                    BusquedaAvanzadaActivity busquedaAvanzadaActivity = (BusquedaAvanzadaActivity) ControllerActivity.activiyAbiertaActual;
                    busquedaAvanzadaActivity.mostrarFormularioRespuesta(respuestaOdata);
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public static class SubmitFormulario extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/SubmitFormularioCotizador");
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
                String fragmentActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
                if(fragmentActual.equals("BusquedaAvanzadaFormFragment"))
                {
                    BusquedaAvanzadaFormFragment cc = (BusquedaAvanzadaFormFragment) ControllerActivity.fragmentAbiertoActual;
                    cc.submitFormularioRespuesta(respuestaOdata);
                }

            }
            catch (Exception e)
            {

            }
        }
    }
}
