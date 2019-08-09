package com.stap.erpstap_avangra.Clases;

import android.view.View;
import android.widget.TextView;

import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class NavigationViewHeader {

    public void NavHeaderText(NavigationView navigationView, SessionManager sessionController){

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        String usernameSession = datosUsuario.get(SessionManager.KEY_NOMBREUSUARIO);
        String emailSession = datosUsuario.get(SessionManager.KEY_EMAIL);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.txtNavHeaderNombreUsuario);
        TextView navEmail = (TextView) headerView.findViewById(R.id.txtNavHeaderEmail);
        navUsername.setText(usernameSession);
        navEmail.setText(emailSession);
    }
}
