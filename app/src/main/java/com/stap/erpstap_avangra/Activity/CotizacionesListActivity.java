package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.stap.erpstap_avangra.Adapters.CardviewAdapterCotizaciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.NavigationViewHeader;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CotizacionesListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SessionManager sessionController;
    public RecyclerView recyclerView_Cotizaciones;
    public static JSONArray listaCotizaciones;
    MKLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizaciones_list);
        sessionController = new SessionManager(getApplicationContext());
        ControllerActivity.activiyAbiertaActual = this;

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        new NavigationViewHeader().NavHeaderText(navigationView, sessionController);
        navigationView.setNavigationItemSelectedListener(this);

        loader = (MKLoader)findViewById(R.id.loaderCotizaciones);
        loader.setVisibility(View.VISIBLE);

        recyclerView_Cotizaciones = (RecyclerView)findViewById(R.id.recyclerView_Cotizaciones);
        recyclerView_Cotizaciones.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_Cotizaciones.setLayoutManager(mGridLayoutManager);

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        String idUsuario = datosUsuario.get(SessionManager.KEY_ID);
        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);
        String idEmpresa = datosUsuario.get(SessionManager.KEY_IDEMPRESA);

        JSONObject datos = new JSONObject();

        try {

            datos.put("IdUser", idUsuario);
            datos.put("IdEmpresa",idEmpresa);
            datos.put("Llave",llave);

            //Enviar datos al webservice
            new Cotizacion.ObtenerCotizaciones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CargarCotizaciones(JSONObject respuesta){
        try {
            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                try {
                    listaCotizaciones = respuesta.getJSONArray("Cotizaciones");
                    mostrarCotizaciones();
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = respuesta.getString("Mensaje");
                new DialogBox().CreateDialogError(getApplicationContext(),"Ha ocurrido un problema", errorMensaje);
            }
            else if(tipoRespuesta.equals("ERROR_SESION")) {
                sessionController.logoutUser();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mostrarCotizaciones(){
        if(listaCotizaciones != null || listaCotizaciones.length() > 0)
        {
            ArrayList<Cotizacion> listaCotizacionesAdapter = new ArrayList<>();
            for(int x = 0; x <listaCotizaciones.length(); x++)
            {
                JSONObject proveedor = null;

                try {

                    proveedor = listaCotizaciones.getJSONObject(x);

                    int id = proveedor.getInt("Id");
                    String codigo = proveedor.getString("Codigo");
                    String fecha = proveedor.getString("Fecha");
                    String fechaSplit = fecha.split("T")[0];

                    listaCotizacionesAdapter.add(new Cotizacion(id,"",false, codigo, fechaSplit, "","",-1,-1,"", -1,-1,-1,"", null, null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Collections.reverse(listaCotizacionesAdapter);

            CardviewAdapterCotizaciones adapter = new CardviewAdapterCotizaciones(getApplicationContext(), listaCotizacionesAdapter, new CardviewAdapterCotizaciones.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, Cotizacion cotizacionSeleccionada) {

                    Intent intent = new Intent(CotizacionesListActivity.this, VerCotizacionDXActivity.class);
                    intent.putExtra("Id", cotizacionSeleccionada.getId());
                    startActivity(intent);

                }
            });

            loader.setVisibility(View.GONE);
            recyclerView_Cotizaciones.setAdapter(adapter);
        }
        else
        {
            loader.setVisibility(View.GONE);
            //txtNombreVehiculo.setText("No se ha podido obtener la lista de vehículos");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                Intent intent = new Intent(CotizacionesListActivity.this, MenuEmpresaActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(CotizacionesListActivity.this, MenuEmpresaActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Intent intent = new Intent(CotizacionesListActivity.this, MenuEmpresaActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_home) {
            Intent intent = new Intent(CotizacionesListActivity.this, CarroCompraActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_productos){
            Intent intent = new Intent(CotizacionesListActivity.this, ProductosListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(CotizacionesListActivity.this, CotizacionesListActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_share) {
            sessionController.logoutUser();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
