package com.stap.erpstap_avangra.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stap.erpstap_avangra.Clases.ConfiguracionCuenta;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Fragments.EditarPerfilFragment;
import com.stap.erpstap_avangra.Fragments.PasswordEditFragment;
import com.stap.erpstap_avangra.Fragments.PoliticasPrivacidadFragment;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConfiguracionActivity extends AppCompatActivity {

    public int actualFragment = 0;
    SessionManager sessionController;

    ProgressBar progress_perfil;
    TextView txtNombreCompletoPerfil, txtEmailPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        ControllerActivity.activiyAbiertaActual = this;
        sessionController = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();

                int backStackCount = fm.getBackStackEntryCount();
                if (backStackCount == 1) {
                    fm.popBackStack();

                    String fagmentActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
                    if(fagmentActual.equals("EditarPerfilFragment")) {
                        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                        int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
                        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);

                        txtNombreCompletoPerfil.setVisibility(View.GONE);
                        txtEmailPerfil.setVisibility(View.GONE);
                        progress_perfil.setVisibility(View.VISIBLE);

                        obtenerUsuarioCliente(idUsuario, llave);
                    }
                }
                else{
                    finish();
                }
            }
        });

        findViewById(R.id.app_bar).bringToFront();

        CardView cardview_perfil_misdatos = findViewById(R.id.cardview_perfil_misdatos);
        cardview_perfil_misdatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new EditarPerfilFragment());
            }
        });

        CardView cardview_perfil_seguridad = findViewById(R.id.cardview_perfil_seguridad);
        cardview_perfil_seguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new PasswordEditFragment());
            }
        });

        CardView cardview_perfil_about_privacidad = findViewById(R.id.cardview_perfil_about_privacidad);
        cardview_perfil_about_privacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new PoliticasPrivacidadFragment());
            }
        });

        CardView cardview_perfil_cerrarSesion = findViewById(R.id.cardview_perfil_cerrarSesion);
        cardview_perfil_cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionController.logoutUser();
            }
        });


        progress_perfil = findViewById(R.id.progress_perfil);
        progress_perfil.setVisibility(View.VISIBLE);

        txtNombreCompletoPerfil = findViewById(R.id.txtNombreCompletoPerfil);
        txtNombreCompletoPerfil.setVisibility(View.GONE);

        txtEmailPerfil = findViewById(R.id.txtEmailPerfil);
        txtEmailPerfil.setVisibility(View.GONE);

        if(savedInstanceState == null) {
            //actualFragment = 0;
            //loadFragment(new EditarPerfilFragment());
        }
        else{

            /*
            actualFragment = savedInstanceState.getInt("currentFragment");
            switch(actualFragment){
                case 0:
                    loadFragment(new EditarPerfilFragment());
                    break;
                case 1:
                    loadFragment(new PasswordEditFragment());
                    break;
            }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);

        obtenerUsuarioCliente(idUsuario, llave);

    }

    public void obtenerUsuarioCliente(int id, String llave){

        JSONObject datos = new JSONObject();

        try {

            datos.put("Id", id);
            datos.put("Llave", llave);

            //Enviar datos al webservice
            new ConfiguracionCuenta.ObtenerUsuarioCliente().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void obtenerUsuarioClienteRespuesta(JSONObject respuestaOdata){
        try {

            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){

                JSONObject usuarioCliente = respuestaOdata.getJSONObject("UsuarioCliente");

                String nombreCompleto = usuarioCliente.getString("NombreCompleto");
                String email = usuarioCliente.getString("Email");

                txtNombreCompletoPerfil.setText(nombreCompleto);
                txtEmailPerfil.setText(email);

                progress_perfil.setVisibility(View.GONE);

                txtNombreCompletoPerfil.setVisibility(View.VISIBLE);
                txtEmailPerfil.setVisibility(View.VISIBLE);

            }
            else if (tipoRespuesta.equals("ERROR")){
                progress_perfil.setVisibility(View.GONE);
                //String errorMensaje = respuestaOdata.getString("Mensaje");
                Toast.makeText(getApplicationContext(),"No es posible cargar tus datos", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container_perfil, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        int backStackCount = fm.getBackStackEntryCount();
        if (backStackCount == 1) {
            fm.popBackStack();

            String fagmentActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
            if(fagmentActual.equals("EditarPerfilFragment")) {
                HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
                String llave = datosUsuario.get(SessionManager.KEY_LLAVE);

                txtNombreCompletoPerfil.setVisibility(View.GONE);
                txtEmailPerfil.setVisibility(View.GONE);

                progress_perfil.setVisibility(View.VISIBLE);

                obtenerUsuarioCliente(idUsuario, llave);
            }
        }
        else{
            finish();
        }

    }
}
