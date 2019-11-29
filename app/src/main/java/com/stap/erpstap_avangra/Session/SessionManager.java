package com.stap.erpstap_avangra.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.stap.erpstap_avangra.Activity.CrearCuentaActivity;
import com.stap.erpstap_avangra.Activity.IniciarSesionActivity;
import com.stap.erpstap_avangra.Activity.PerfilActivity;
import com.stap.erpstap_avangra.Activity.VerificacionLoginActivity;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.google.gson.Gson;
import com.stap.erpstap_avangra.Fragments.EditarPerfilFragment;
import com.stap.erpstap_avangra.Fragments.PasswordEditFragment;

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
import java.util.HashMap;
import java.util.List;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    SharedPreferences prefEmpresas;
    SharedPreferences.Editor editorEmpresas;

    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "AndroidExamplePref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_IDEMPRESA = "idEmpresa";
    public static final String KEY_NOMBREUSUARIO = "nombreUsuario";
    public static final String KEY_NOMBREEMPRESA = "nombreEmpresa";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LLAVE = "llave";

    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public <T> void setListEmpresas(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json);
    }

    public void set(String key, String value) {
        editorEmpresas.putString(key, value);
        editorEmpresas.commit();

    }

    public void levantarSesion(String nombreUsuario, String nombreEmpresa, String email, String llave, int id, int idEmpresa)
    {

        if(id != -1){
            editor.putBoolean(IS_USER_LOGIN, true);
            editor.putString(KEY_ID, id+"");
            editor.putString(KEY_IDEMPRESA, idEmpresa+"");
            editor.putString(KEY_NOMBREEMPRESA, nombreEmpresa);
            editor.putString(KEY_NOMBREUSUARIO, nombreUsuario);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_LLAVE, llave);
        }

        editor.commit();
    }

    public boolean checkLogin()
    {
        if(!this.isUserLoggedIn())
        {
            // user is not logged in redirect him to Login Activity
            //Intent i = new Intent(_context, IniciarSesionActivity.class);
            // Closing all the Activities from stack
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            //_context.startActivity(i);

            return false;
        }

        return true;
    }

    public boolean checkLogin2()
    {
        if(!this.isUserLoggedIn())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public HashMap<String, String> obtenerDetallesUsuario()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_IDEMPRESA, pref.getString(KEY_IDEMPRESA, null));
        user.put(KEY_NOMBREUSUARIO, pref.getString(KEY_NOMBREUSUARIO, null));
        user.put(KEY_NOMBREEMPRESA, pref.getString(KEY_NOMBREEMPRESA, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_LLAVE, pref.getString(KEY_LLAVE, null));
        user.put("KEY_EMPRESAS", pref.getString("KEY_EMPRESAS", null));
        return user;
    }

    public HashMap<String, String> obtenerEmpresas()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("KEY_EMPRESAS", pref.getString("KEY_EMPRESAS", null));
        return user;
    }

    public void logoutUser()
    {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, VerificacionLoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public static class ValidarLogin extends AsyncTask<String,String, JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/IniciarSesion");
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
                if(activityActual.equals("IniciarSesionActivity"))
                {
                    IniciarSesionActivity login = (IniciarSesionActivity) ControllerActivity.activiyAbiertaActual;
                    login.sesionValida(respuestaOdata);
                }
                else if(activityActual.equals("SeleccionVehiculoActivity"))
                {
                    //SeleccionVehiculoActivity login = (SeleccionVehiculoActivity) ControllerActivity.activiyAbiertaActual;
                    //login.estadoValidacion(respuestaOdata);
                }

            }
            catch (Exception e)
            {

            }
        }
    }

    public static class CrearCuenta extends AsyncTask<String,String,JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/CrearCuenta");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.getBytes());
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
                CrearCuentaActivity cu = (CrearCuentaActivity) ControllerActivity.activiyAbiertaActual;
                cu.estadoCuenta(respuestaOdata);

            }
            catch (Exception e)
            {

            }
        }
    }

    public static class EditarDatosCliente extends AsyncTask<String,String,JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/EditarDatosCliente");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.getBytes());
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
                EditarPerfilFragment cu = (EditarPerfilFragment) ControllerActivity.fragmentAbiertoActual;
                cu.estadoCuenta(respuestaOdata);

            }
            catch (Exception e)
            {

            }
        }
    }

    public static class EditarPassword extends AsyncTask<String,String,JSONObject>
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
                URL url = new URL("http://stap.cl/odata/UsuariosClientes/CambiarPassword");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.getBytes());
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
                PasswordEditFragment cu = (PasswordEditFragment) ControllerActivity.fragmentAbiertoActual;
                cu.estadoPassword(respuestaOdata);

            }
            catch (Exception e)
            {

            }
        }
    }
}
