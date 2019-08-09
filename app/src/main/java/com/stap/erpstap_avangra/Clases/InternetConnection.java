package com.stap.erpstap_avangra.Clases;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.stap.erpstap_avangra.Activity.CrearCuentaActivity;
import com.stap.erpstap_avangra.Activity.IniciarSesionActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Richard on 06/03/2018.
 */

public class InternetConnection
{

    static String LOG_TAG = "InternetAccess";

    public static class hasInternetAccess extends AsyncTask<Context, Context, Boolean>
    {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Context... c) {
            Context context = c[0];

            if (isNetworkAvailable(context)) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection)(new URL("http://clients3.google.com/generate_204").openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error checking internet connection", e);
                }
            } else {
                Log.d(LOG_TAG, "No network available!");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            String activityActual = ControllerActivity.activiyAbiertaActual.getClass().getSimpleName();
            if(activityActual.equals("IniciarSesionActivity"))
            {
                IniciarSesionActivity login = (IniciarSesionActivity) ControllerActivity.activiyAbiertaActual;
                login.validar(result);
            }
            if(activityActual.equals("CrearCuentaActivity"))
            {
                CrearCuentaActivity login = (CrearCuentaActivity) ControllerActivity.activiyAbiertaActual;
                login.validar(result);
            }
        }

    }

    public static boolean isNetworkAvailable(Context co) {
        ConnectivityManager connectivityManager = (ConnectivityManager)co.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}

