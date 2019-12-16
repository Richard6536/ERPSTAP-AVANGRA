package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.Fragments.VerCotizacionFragment;
import com.stap.erpstap_avangra.MainActivity;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import static com.stap.erpstap_avangra.Clases.ProductoEnCarro.productosEnCarro;

public class VistaPreviaCotizacionActivity extends AppCompatActivity implements VerCotizacionFragment.OnDataPass {

    SessionManager sessionController;
    String cotizacionId;
    Button btnRechazarCotizacion, btnConfirmarCotizacion;


    @Override
    public void onDataPass(String data) {
        Log.d("LOG","hello " + data);
        cotizacionId = data;

        btnRechazarCotizacion.setBackgroundColor(Color.parseColor("#F44336"));
        btnConfirmarCotizacion.setBackgroundColor(Color.parseColor("#4CAF50"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_previa_cotizacion);
        ControllerActivity.activiyAbiertaActual = this;
        sessionController = new SessionManager(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        int condicionSeleccionada = 0;

        if (extras != null) {
            condicionSeleccionada = extras.getInt("IdCondicionSeleccionada", -1);
        }

        btnRechazarCotizacion = findViewById(R.id.btnRechazarCotizacion);
        btnConfirmarCotizacion = findViewById(R.id.btnConfirmarCotizacion);

        btnRechazarCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialogCotizacion("¿Está seguro de que desea rechazar la Cotizacion?", "Rechazar Cotización");

            }
        });

        btnConfirmarCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enableDisableButtons(false);

                HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
                int idEmpresa = Integer.parseInt(datosUsuario.get(SessionManager.KEY_IDEMPRESA));
                String llave = datosUsuario.get(SessionManager.KEY_LLAVE);

                JSONObject datos = new JSONObject();

                try {

                    datos.put("IdUser", idUsuario);
                    datos.put("IdEmpresa", idEmpresa);
                    datos.put("Llave", llave);
                    datos.put("IdCotizacion", cotizacionId);

                    //Enviar datos al webservice
                    new Cotizacion.ConfirmarCotizacion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        VerCotizacionFragment vc = new VerCotizacionFragment();
        loadFragment(vc, condicionSeleccionada);
    }

    private boolean loadFragment(Fragment fragment, int condicionSeleccionada) {
        //switching fragment
        if (fragment != null) {

            Bundle arguments = new Bundle();
            arguments.putInt("Id", -1);
            arguments.putBoolean("FromVistaPrevia", true);
            arguments.putInt("IdCondicionSeleccionada", condicionSeleccionada);

            fragment.setArguments(arguments);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.containerVerCotizacion, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void confirmarCotizacionRespuesta(JSONObject respuestaOdata){
        try {
            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
            String errorMensaje = respuestaOdata.getString("Mensaje");

            if(tipoRespuesta.equals("OK")){

                DialogBox dialog = new DialogBox();

                BorrarProductoDelCarro();
                //new BottomNavigationController().badgeCartRemove();
                dialog.Create(VistaPreviaCotizacionActivity.this, "Cotización Enviada con Éxito", "La nueva cotización se registrará en su lista de cotizaciones.", true);

            }
            else if(tipoRespuesta.equals("ERROR")){

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

    public void rechazarCotizacionRespuesta(JSONObject respuestaOdata){
        try {
            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
            String errorMensaje = respuestaOdata.getString("Mensaje");

            if(tipoRespuesta.equals("OK")){

                new BottomNavigationController().badgeCartRemove();
                BorrarProductoDelCarro();

                Intent i = new Intent(getApplication(), MainNavigationActivity.class);
                i.putExtra("KEY_COT_CREADA", false);
                i.putExtra("KEY_COT_RECHAZADA", true);
                ControllerActivity.activiyAbiertaActual.startActivity(i);
                ControllerActivity.activiyAbiertaActual.finish();

            }
            else if(tipoRespuesta.equals("ERROR")){

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

    public void BorrarProductoDelCarro(){
        for (Iterator<ProductoEnCarro> iter = productosEnCarro.listIterator(); iter.hasNext(); ) {
            ProductoEnCarro prod = iter.next();
            iter.remove();
        }
    }

    public void AlertDialogCotizacion(String mensaje, String titulo){

        AlertDialog.Builder builder= new AlertDialog.Builder(VistaPreviaCotizacionActivity.this);
        builder.setMessage(mensaje)
                .setTitle(titulo)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        enableDisableButtons(false);

                        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                        int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
                        int idEmpresa = Integer.parseInt(datosUsuario.get(SessionManager.KEY_IDEMPRESA));
                        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);

                        JSONObject datos = new JSONObject();

                        try {

                            datos.put("IdUser", idUsuario);
                            datos.put("IdEmpresa", idEmpresa);
                            datos.put("Llave", llave);
                            datos.put("IdCotizacion", cotizacionId);

                            //Enviar datos al webservice
                            new Cotizacion.RechazarCotizacion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }})
                .setNegativeButton(" NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialogCotizacion("¿Está seguro de que desea rechazar la Cotizacion?", "Rechazar Cotización");

    }

    public void enableDisableButtons(boolean enable){

        btnConfirmarCotizacion.setEnabled(enable);
        btnRechazarCotizacion.setEnabled(enable);
        btnRechazarCotizacion.setBackgroundColor(Color.parseColor("#7A9B9B9B"));
        btnConfirmarCotizacion.setBackgroundColor(Color.parseColor("#7A9B9B9B"));
    }
}
