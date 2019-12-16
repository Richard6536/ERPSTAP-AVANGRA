package com.stap.erpstap_avangra.Clases;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.stap.erpstap_avangra.Activity.CotizacionesListActivity;
import com.stap.erpstap_avangra.Activity.VerCotizacionDXActivity;
import com.stap.erpstap_avangra.Activity.VistaPreviaCotizacionActivity;
import com.stap.erpstap_avangra.Fragments.CotizacionesListFragment;
import com.stap.erpstap_avangra.Fragments.VerCotizacionFragment;
import com.stap.erpstap_avangra.Session.SessionManager;

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
import java.util.HashMap;
import java.util.List;

public class Cotizacion {
    int id;
    String referencia;
    boolean dividirPorCategoria;
    String codigo;
    String fecha;
    String notasVendedor;
    String moneda;
    float subTotalNeto;
    float descuento;
    String Recargo;
    float totalNeto;
    float iva;
    float totalAPagar;
    String usuarioQueAtendio;
    int position_cardview;

    List<Producto> productos = new ArrayList<>();
    List<Condiciones> condiciones = new ArrayList<>();


    public int getId() {
        return id;
    }

    public String getReferencia() {
        return referencia;
    }

    public boolean isDividirPorCategoria() {
        return dividirPorCategoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getNotasVendedor() {
        return notasVendedor;
    }

    public String getMoneda() {
        return moneda;
    }

    public float getSubTotalNeto() {
        return subTotalNeto;
    }

    public float getDescuento() {
        return descuento;
    }

    public String getRecargo() {
        return Recargo;
    }

    public float getTotalNeto() {
        return totalNeto;
    }

    public float getIva() {
        return iva;
    }

    public float getTotalAPagar() {
        return totalAPagar;
    }

    public String getUsuarioQueAtendio() {
        return usuarioQueAtendio;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Condiciones> getCondiciones() {
        return condiciones;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setDividirPorCategoria(boolean dividirPorCategoria) {
        this.dividirPorCategoria = dividirPorCategoria;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setNotasVendedor(String notasVendedor) {
        this.notasVendedor = notasVendedor;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setSubTotalNeto(float subTotalNeto) {
        this.subTotalNeto = subTotalNeto;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public void setRecargo(String recargo) {
        Recargo = recargo;
    }

    public void setTotalNeto(float totalNeto) {
        this.totalNeto = totalNeto;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public void setTotalAPagar(float totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public void setUsuarioQueAtendio(String usuarioQueAtendio) {
        this.usuarioQueAtendio = usuarioQueAtendio;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void setCondiciones(List<Condiciones> condiciones) {
        this.condiciones = condiciones;
    }

    public int getPosition_cardview() {
        return position_cardview;
    }

    public void setPosition_cardview(int position_cardview) {
        this.position_cardview = position_cardview;
    }

    public void prepararCotizacion(Context context, List<ProductoEnCarro> productoEnCarroList, int idPerfilSeleccionado){
        SessionManager sessionController = new SessionManager(context);

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);
        int idEmpresa = Integer.parseInt(datosUsuario.get(SessionManager.KEY_IDEMPRESA));

        JSONObject datos = new JSONObject();
        JSONArray productosEnCotizacion = new JSONArray();

        try {

            for(ProductoEnCarro pc : productoEnCarroList){

                JSONObject productoJson = new JSONObject();
                productoJson.put("Id", pc.getId());
                productoJson.put("Cantidad", pc.getCantidad());

                productosEnCotizacion.put(productoJson);
            }

            datos.put("IdUser", idUsuario);
            datos.put("IdEmpresa",idEmpresa);
            datos.put("Llave",llave);
            datos.put("Productos", productosEnCotizacion);
            datos.put("IdPerfilCondiciones", idPerfilSeleccionado);

            //Enviar datos al webservice
            new ProductoEnCarro.CrearCotizacion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class ObtenerCotizaciones extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/ListaCotizaciones");
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
                if(activityActual.equals("CotizacionesListFragment"))
                {
                    CotizacionesListFragment cotizacionesListFragment = (CotizacionesListFragment) ControllerActivity.fragmentAbiertoActual;
                    cotizacionesListFragment.CargarCotizaciones(respuestaOdata);
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public static class VerCotizacion extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/VerCotizacion");
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

        protected void onPostExecute(JSONObject respuestaOdata) {
            try {
                String activityActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
                if(activityActual.equals("VerCotizacionFragment")) {
                    VerCotizacionFragment verCot = (VerCotizacionFragment) ControllerActivity.fragmentAbiertoActual;
                    verCot.RecibirCotizacion(respuestaOdata);
                }
            }
            catch (Exception e) {

            }
        }
    }

    public static class ConfirmarCotizacion extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/ConfirmarVistaPreviaCotizacion");
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

        protected void onPostExecute(JSONObject respuestaOdata) {
            try {
                String activityActual = ControllerActivity.activiyAbiertaActual.getClass().getSimpleName();
                if(activityActual.equals("VistaPreviaCotizacionActivity")) {
                    VistaPreviaCotizacionActivity verCot = (VistaPreviaCotizacionActivity) ControllerActivity.activiyAbiertaActual;
                    verCot.confirmarCotizacionRespuesta(respuestaOdata);
                }
            }
            catch (Exception e) {

            }
        }
    }

    public static class RechazarCotizacion extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/CancelarVistaPreviaCotizacion");
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

        protected void onPostExecute(JSONObject respuestaOdata) {
            try {
                String activityActual = ControllerActivity.activiyAbiertaActual.getClass().getSimpleName();
                if(activityActual.equals("VistaPreviaCotizacionActivity")) {
                    VistaPreviaCotizacionActivity verCot = (VistaPreviaCotizacionActivity) ControllerActivity.activiyAbiertaActual;
                    verCot.rechazarCotizacionRespuesta(respuestaOdata);
                }
            }
            catch (Exception e) {

            }
        }
    }
}
