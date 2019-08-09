package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.stap.erpstap_avangra.Adapters.CrearCuentaFragmentsController;
import com.stap.erpstap_avangra.Adapters.CustomViewPager;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.stap.erpstap_avangra.Fragments.CrearCuenta_DatosUsuario_2Fragment.btnFinalizarCrearCuenta;
import static com.stap.erpstap_avangra.Fragments.CrearCuenta_DatosUsuario_2Fragment.progressBar_crearCuenta;

public class CrearCuentaActivity extends AppCompatActivity {

    public static CustomViewPager viewPager;
    public static EditText nombreUs, apellido, nombreFantasia, rut, email, password, telefono, domicilio, razonSocial, giro, ciudad;
    public static int tipoDeCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        ControllerActivity.activiyAbiertaActual = this;

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (CustomViewPager) findViewById(R.id.pager);
        final CrearCuentaFragmentsController adapter = new CrearCuentaFragmentsController (getSupportFragmentManager(), 2);

        viewPager.setAdapter(adapter);
        viewPager.disableScroll(true);
    }

    public void estadoCuenta(JSONObject respuesta){
        try {

            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){

                new DialogBox().CreateDialogSuccess(getApplicationContext(),"Cuenta creada","La cuenta ha sido creada con éxito.");

            }
            else if (tipoRespuesta.equals("ERROR")){
                progressBar_crearCuenta.setVisibility(View.GONE);
                btnFinalizarCrearCuenta.setEnabled(true);

                String errorMensaje = respuesta.getString("Mensaje");
                new DialogBox().CreateDialogError(getApplicationContext(),"Ha ocurrido un problema", errorMensaje);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void validar(boolean isConnectionEnable)
    {
        if(isConnectionEnable == false)
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressBar_crearCuenta.setVisibility(View.GONE);
            btnFinalizarCrearCuenta.setEnabled(true);
            new DialogBox().CreateDialogError(getApplicationContext(),"Error de Conexión","Compruebe su conexión a Internet");
        }
        else
        {
            JSONObject datosCuenta = new JSONObject();

            try {

                datosCuenta.put("Email", email.getText());
                datosCuenta.put("Password", password.getText());
                datosCuenta.put("TipoDeCliente", tipoDeCliente);

                if (tipoDeCliente == 0){

                    datosCuenta.put("Nombre", nombreUs.getText());
                    datosCuenta.put("ApellidoPaterno", apellido.getText());

                    datosCuenta.put("NombreFantasia", "");
                    datosCuenta.put("Giro", "");
                    datosCuenta.put("RazonSocial", "");
                }
                else if(tipoDeCliente == 1){

                    datosCuenta.put("NombreFantasia", nombreUs.getText());
                    datosCuenta.put("Giro", apellido.getText());
                    datosCuenta.put("RazonSocial", razonSocial.getText());

                    datosCuenta.put("Nombre", "");
                    datosCuenta.put("ApellidoPaterno", "");
                }

                datosCuenta.put("Telefono", telefono.getText());
                datosCuenta.put("Rut", rut.getText());
                datosCuenta.put("Domicilio", domicilio.getText());
                datosCuenta.put("Ciudad", ciudad.getText());



            } catch (JSONException e) {
                e.printStackTrace();
            }

            new SessionManager.CrearCuenta().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datosCuenta.toString());
        }
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
}
