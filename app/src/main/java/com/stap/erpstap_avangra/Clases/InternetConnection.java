package com.stap.erpstap_avangra.Clases;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.stap.erpstap_avangra.Activity.CrearCuentaActivity;
import com.stap.erpstap_avangra.Activity.IniciarSesionActivity;
import com.stap.erpstap_avangra.Activity.MainNavigationActivity;
import com.stap.erpstap_avangra.Fragments.EditarPerfilFragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Richard on 06/03/2018.
 */

public class InternetConnection extends BroadcastReceiver {

    static String LOG_TAG = "InternetAccess";
    public static boolean internetAccess = true;
    Snackbar snackbar;
    public static CoordinatorLayout coordinatorInternet;

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
                    return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);

                } catch (IOException e) {
                }

            } else {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            String activityActual = ControllerActivity.activiyAbiertaActual.getClass().getSimpleName();

            if(activityActual.equals("IniciarSesionActivity")) {
                IniciarSesionActivity login = (IniciarSesionActivity) ControllerActivity.activiyAbiertaActual;
                login.validar(result);
            }
            else if(activityActual.equals("CrearCuentaActivity")) {
                CrearCuentaActivity login = (CrearCuentaActivity) ControllerActivity.activiyAbiertaActual;
                login.validar(result);
            }
            else if(activityActual.equals("MainNavigationActivity")) {
                MainNavigationActivity login = (MainNavigationActivity) ControllerActivity.activiyAbiertaActual;
                login.hasInternet(result);
            }
            else {
                try{
                    String fragmentActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
                    if(fragmentActual.equals("EditarPerfilFragment")) {
                        EditarPerfilFragment login = (EditarPerfilFragment) ControllerActivity.fragmentAbiertoActual;
                        login.validar(result);
                    }
                }
                catch (Exception e){
                    Log.d("Exception: ", e.getMessage());
                }
            }
        }

    }

    public static boolean isNetworkAvailable(Context co) {
        ConnectivityManager connectivityManager = (ConnectivityManager)co.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (("android.net.conn.CONNECTIVITY_CHANGE").equals(action)) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isConnected()){
                InternetConnection.internetAccess = true;
                if(snackbar != null) {
                    snackbar.dismiss();
                }
            }else{
                InternetConnection.internetAccess = false;
                showSnackbar();
            }
        }
    }

    public void showSnackbar(){
        if(coordinatorInternet != null){
            snackbar = Snackbar.make(coordinatorInternet, "Sin acceso a Internet", Snackbar.LENGTH_INDEFINITE);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams();
            params.setMargins(0, 0, 0, 48);
            snackbar.getView().setLayoutParams(params);
            snackbar.show();
        }
    }
}

