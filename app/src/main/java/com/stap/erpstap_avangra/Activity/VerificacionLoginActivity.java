package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

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

        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                Intent intent = new Intent(VerificacionLoginActivity.this, MainNavigationActivity.class);
                startActivity(intent);
                finish();

                /*
                if(sessionController.checkLogin() == true) {

                    Intent intent = new Intent(VerificacionLoginActivity.this, MainNavigationActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(VerificacionLoginActivity.this, IniciarSesionActivity.class);
                    startActivity(intent);
                    finish();
                }*/

            }
        }.start();

    }
}
