package com.stap.erpstap_avangra.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Clases.NavigationViewHeader;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuEmpresaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager sessionController;
    String idEmpresa = "";
    TextView txtNombreEmpresa;
    String nombreEmpresa;
    CardView cardView_cot, cardView_cc, cardView_mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empresa);
        ControllerActivity.activiyAbiertaActual = this;
        sessionController = new SessionManager(getApplicationContext());

        //txtNombreEmpresa = (TextView)findViewById(R.id.txtNombreEmpresa);
        cardView_cot = (CardView)findViewById(R.id.cv_cot);
        cardView_cc = (CardView)findViewById(R.id.cv_cc);
        cardView_mc = (CardView)findViewById(R.id.cv_mc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AVANGRA/AYN");
        getSupportActionBar().setElevation(0);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        new NavigationViewHeader().NavHeaderText(navigationView, sessionController);
        navigationView.setNavigationItemSelectedListener(this);

        cardView_cot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuEmpresaActivity.this, ProductosListActivity.class);
                startActivity(intent);
            }
        });

        cardView_cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuEmpresaActivity.this, CarroCompraActivity.class);
                startActivity(intent);
            }
        });

        cardView_mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuEmpresaActivity.this, CotizacionesListActivity.class);
                startActivity(intent);
            }
        });

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        nombreEmpresa = datosUsuario.get(SessionManager.KEY_NOMBREEMPRESA);
        //txtNombreEmpresa.setText(nombreEmpresa);

        /*
        if(existeIdEmpresa == -1) {

            String idUsuario = datosUsuario.get(SessionManager.KEY_ID);
            String llave = datosUsuario.get(SessionManager.KEY_LLAVE);
            idEmpresa = getIntent().getStringExtra("IdEmpresa");

            JSONObject datos = new JSONObject();

            try {

                datos.put("IdUser", idUsuario);
                datos.put("IdEmpresa",idEmpresa);
                datos.put("Llave",llave);

                //Enviar datos al webservice
                new Empresa.ObtenerEmpresa().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else{
            int idEmpresa = getIntent().getIntExtra("IdEmpresa",-1);
            String nombreEmpresa = getIntent().getStringExtra("NombreEmpresa");

            if(idEmpresa != -1){
                sessionController.levantarSesion("",nombreEmpresa,"","",-1, idEmpresa);
            }

            datosUsuario = sessionController.obtenerDetallesUsuario();
            nombreEmpresa = datosUsuario.get(SessionManager.KEY_NOMBREEMPRESA);
            txtNombreEmpresa.setText(nombreEmpresa);
        }
        */
    }

    /*
    public void ValidacionEmpresa(JSONObject respuesta){

        try {
            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                int idEmpresaInteger = Integer.parseInt(idEmpresa);
                nombreEmpresa = respuesta.getString("NombreEmpresa");
                sessionController.levantarSesion("",nombreEmpresa,"","",-1, idEmpresaInteger);

                AgregarEmpresaALista(idEmpresaInteger);

                txtNombreEmpresa.setText(nombreEmpresa);
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
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void AgregarEmpresaALista(int idEmpresaInteger){

        //Agregar la nueva empresa a la lista de la sesión
        HashMap<String, String> empresas = sessionController.obtenerEmpresas();
        boolean existeEmpresa = false;
        List<Empresa> arrayItems = new ArrayList<>();
        String serializedObject = empresas.get("KEY_EMPRESAS");
        if (serializedObject != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<Empresa>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        Empresa empresa = new Empresa();
        empresa.setId(idEmpresaInteger);
        empresa.setNombre(nombreEmpresa);

        for(Empresa emp : arrayItems){
            if(emp.Id == empresa.Id){
                existeEmpresa = true;
            }
        }

        if(existeEmpresa == false){
            arrayItems.add(empresa);
            sessionController.setListEmpresas("KEY_EMPRESAS", arrayItems);
        }
    }
    */

    //Todo:----------------------------------------------------------------
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empresa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Intent intent = new Intent(MenuEmpresaActivity.this, MenuEmpresaActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_home) {
            Intent intent = new Intent(MenuEmpresaActivity.this, CarroCompraActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_productos){
            Intent intent = new Intent(MenuEmpresaActivity.this, ProductosListActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MenuEmpresaActivity.this, CotizacionesListActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_share) {
            sessionController.logoutUser();

        }
        /*
        else if(id == R.id.nav_editar_cuenta){

        }*/


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
