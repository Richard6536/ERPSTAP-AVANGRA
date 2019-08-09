package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

public class VerificacionLoginActivity extends AppCompatActivity {

    SessionManager sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_login);
        ControllerActivity.activiyAbiertaActual = this;
        sessionController = new SessionManager(getApplicationContext());

        if(sessionController.checkLogin() == true) {

            Intent intent = new Intent(VerificacionLoginActivity.this, MenuEmpresaActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(VerificacionLoginActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
