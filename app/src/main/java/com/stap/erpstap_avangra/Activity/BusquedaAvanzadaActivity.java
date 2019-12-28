package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.stap.erpstap_avangra.Adapters.CustomViewPager;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.FiltroAvanzado;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.Clases.TipoCaracteristicaProductoBusquedaAvanzada;
import com.stap.erpstap_avangra.Fragments.BusquedaAvanzadaFormFragment;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusquedaAvanzadaActivity extends AppCompatActivity {

    SessionManager sessionController;
    public static CustomViewPager viewPagerBusquedaAvanzada;
    ProgressBar progressBarBusquedaAvanzada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_avanzada);

        ControllerActivity.activiyAbiertaActual = this;
        sessionController = new SessionManager(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Búsqueda Avanzada");
        findViewById(R.id.app_bar).bringToFront();

        Drawable drawable = changeDrawableColor(getApplicationContext(),R.drawable.ic_close_black_24dp, Color.WHITE);

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressBarBusquedaAvanzada = (ProgressBar) findViewById(R.id.progressBarBusquedaAvanzada);
        new TipoCaracteristicaProductoBusquedaAvanzada().clearMapList();

        JSONObject datos = new JSONObject();

        try {

            String idUsuario = "0";
            String llave = "";
            String idEmpresa = "1";

            if(sessionController.checkLogin() == true) {

                HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                idUsuario = datosUsuario.get(SessionManager.KEY_ID);
                llave = datosUsuario.get(SessionManager.KEY_LLAVE);
                idEmpresa = datosUsuario.get(SessionManager.KEY_IDEMPRESA);
            }

            datos.put("IdUser", idUsuario);
            datos.put("IdEmpresa",idEmpresa);
            datos.put("Llave",llave);

            //Enviar datos al webservice
            new FiltroAvanzado.ObtenerFormulario().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mostrarFormularioRespuesta(JSONObject respuestaOdata){
        try {

            progressBarBusquedaAvanzada.setVisibility(View.GONE);
            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");

            if(tipoRespuesta.equals("OK")){
                try {

                    obtenerCaracteristicasProducto(respuestaOdata);
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = respuestaOdata.getString("Mensaje");
                new DialogBox().CreateDialogError(ControllerActivity.fragmentAbiertoActual.getContext(),"Ha ocurrido un problema", errorMensaje);
            }
            else if(tipoRespuesta.equals("ERROR_SESION")) {
                sessionController.logoutUser();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cargarTabbed(){

        int count = new FiltroAvanzado().getFiltroAvanzadoCount();
        FragmentPagerItems fragmentPagerItems = new FragmentPagerItems(this);
        for(int i = 0; i < count; i++){
            String nombre = new FiltroAvanzado().filtroAvanzadoList.get(i).getNombre();
            fragmentPagerItems.add(FragmentPagerItem.of(nombre, BusquedaAvanzadaFormFragment.class));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), fragmentPagerItems);


        viewPagerBusquedaAvanzada = (CustomViewPager) findViewById(R.id.viewpager);
        viewPagerBusquedaAvanzada.setAdapter(adapter);
        viewPagerBusquedaAvanzada.disableScroll(true);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPagerBusquedaAvanzada);

    }

    public void obtenerCaracteristicasProducto(JSONObject respuestaOdata) {
        try {
            JSONArray jsonArrayCaracteristicasProducto = respuestaOdata.getJSONArray("CaracteristicasProducto");
            new FiltroAvanzado().filtroAvanzadoList = new ArrayList<>();

            if (jsonArrayCaracteristicasProducto.length() > 0) {
                for (int x = 0; x < jsonArrayCaracteristicasProducto.length(); x++) {

                    JSONObject caracteristicasProducto = null;

                    caracteristicasProducto = jsonArrayCaracteristicasProducto.getJSONObject(x);

                    int id = caracteristicasProducto.getInt("Id");
                    int orden = caracteristicasProducto.getInt("Orden");
                    String nombre = caracteristicasProducto.getString("Nombre");
                    String descripcion = caracteristicasProducto.getString("Descripcion");
                    int tipo = caracteristicasProducto.getInt("Tipo");
                    JSONArray arrayStringsPreguntas = caracteristicasProducto.getJSONArray("ArrayStrings");

                    ArrayList<String> alternativasList = new ArrayList<String>();
                    for (int z = 0; z < arrayStringsPreguntas.length(); z++){

                        String alternativa = arrayStringsPreguntas.getString(z);
                        alternativasList.add(alternativa);
                    }

                    new FiltroAvanzado().crearFiltroAvanzado(id, orden, x, nombre, descripcion, tipo, alternativasList);
                }

                cargarTabbed();

            } else {
                //txtNombreVehiculo.setText("No se ha podido obtener la lista de vehículos");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }
}
