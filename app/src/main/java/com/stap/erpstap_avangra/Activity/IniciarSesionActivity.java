package com.stap.erpstap_avangra.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.InternetConnection;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class IniciarSesionActivity extends AppCompatActivity {

    EditText password, email;
    String emailStr, passwordStr;
    ProgressBar progressBar;
    Button btnLogin;
    TextView txtCrearCuenta;
    SessionManager sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        ControllerActivity.activiyAbiertaActual = this;
        sessionController = new SessionManager(getApplicationContext());
        int i = android.os.Build.VERSION_CODES.LOLLIPOP;
        int b = android.os.Build.VERSION.SDK_INT;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            ImageView img_logo_login = (ImageView)findViewById(R.id.img_logo_login);
            img_logo_login.setLayoutParams(lp);
        }

        email = (EditText)findViewById(R.id.txtEmailLogin);
        password = (EditText)findViewById(R.id.txtPasswordLogin);
        progressBar = (ProgressBar)findViewById(R.id.loading);
        btnLogin = (Button)findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });

        txtCrearCuenta = (TextView) findViewById(R.id.txtCrearCuenta);

        txtCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IniciarSesionActivity.this, CrearCuentaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void validarCampos(){
        // Reset errors.
        email.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        emailStr = email.getText().toString();
        passwordStr = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressBar.setVisibility(View.VISIBLE);
            email.setEnabled(false);
            password.setEnabled(false);
            btnLogin.setEnabled(false);

            //Verifica si hay coneción a internet antes de iniciar sesión
            new InternetConnection.hasInternetAccess().execute(getApplicationContext());
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void validar(boolean isConnectionEnable)
    {
        if(isConnectionEnable == false)
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressBar.setVisibility(View.GONE);
            email.setEnabled(true);
            password.setEnabled(true);
            btnLogin.setEnabled(true);

            new DialogBox().CreateDialogError(getApplicationContext(),"Error de Conexión","Compruebe su conexión a Internet");
        }
        else
        {
            JSONObject datos = new JSONObject();

            try {

                datos.put("Email", emailStr);
                datos.put("Password",passwordStr);

                //Enviar datos al webservice
                new SessionManager.ValidarLogin().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void sesionValida(JSONObject respuesta){
        try {

            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                String llave = respuesta.getString("Llave");
                int usuarioId = respuesta.getInt("UsuarioId");
                String nombreUsuario = respuesta.getString("NombreUsuario");

                sessionController.levantarSesion(nombreUsuario,"AVANGRA", emailStr, llave, usuarioId, 1);//ID:Avangra:1, Testing:3

                Intent myIntent = new Intent(IniciarSesionActivity.this, MainNavigationActivity.class);
                startActivity(myIntent);

            }
            else if (tipoRespuesta.equals("ERROR")){
                progressBar.setVisibility(View.GONE);
                email.setEnabled(true);
                password.setEnabled(true);
                btnLogin.setEnabled(true);

                String errorMensaje = respuesta.getString("Mensaje");

                new AlertDialog.Builder(IniciarSesionActivity.this)
                        .setTitle("Error al Iniciar Sesión")
                        .setMessage(errorMensaje)

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                //new DialogBox().CreateDialogError(getApplicationContext(),"Ha ocurrido un problema", errorMensaje);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        //moveTaskToBack(true);
    }
}
