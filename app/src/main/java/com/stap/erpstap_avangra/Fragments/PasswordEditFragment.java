package com.stap.erpstap_avangra.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.stap.erpstap_avangra.Activity.IniciarSesionActivity;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class PasswordEditFragment extends Fragment {

    SessionManager sessionController;
    TextInputEditText editPasswordActual, editNuevaPassword, editRepetirNuevaPassword;
    Button btnCambiarPassword;
    ProgressBar progress_circular_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_edit, container, false);

        ControllerActivity.fragmentAbiertoActual = this;
        sessionController = new SessionManager(getActivity());

        editPasswordActual = view.findViewById(R.id.editPasswordActual);
        editNuevaPassword = view.findViewById(R.id.editNuevaPassword);
        editRepetirNuevaPassword = view.findViewById(R.id.editRepetirNuevaPassword);
        progress_circular_password = view.findViewById(R.id.progress_circular_password);

        btnCambiarPassword = view.findViewById(R.id.btnCambiarPassword);
        btnCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editPasswordActual == null || editPasswordActual.getText().toString().equals("")
                || editNuevaPassword == null || editNuevaPassword.getText().toString().equals("")
                || editRepetirNuevaPassword == null || editRepetirNuevaPassword.getText().toString().equals("")){

                    Toast.makeText(getContext(),"Debe completar todos los campos", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(editNuevaPassword.getText().toString().equals(editRepetirNuevaPassword.getText().toString())){

                        lockItems(true);

                        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                        String idUsuario = datosUsuario.get(SessionManager.KEY_ID);
                        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);

                        JSONObject datos = new JSONObject();

                        try {

                            datos.put("Id", idUsuario);
                            datos.put("Llave",llave);
                            datos.put("AntiguaPassword", editPasswordActual.getText().toString());
                            datos.put("NuevaPassword", editNuevaPassword.getText().toString());

                            //Enviar datos al webservice
                            new SessionManager.EditarPassword().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getContext(),"La nueva contraseña no coincide", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        lockItems(false);

        return view;
    }

    public void estadoPassword(JSONObject respuestaOdata){
        try {

            lockItems(false);
            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");

            if(tipoRespuesta.equals("OK")){
                Dialog dialog = new Dialog(ControllerActivity.fragmentAbiertoActual.getContext());
                dialog.setContentView(R.layout.dialog_password);

                Button btn_dialog_password = (Button) dialog.findViewById(R.id.btn_dialog_password);
                btn_dialog_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sessionController.logoutUser();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = respuestaOdata.getString("Mensaje");
                new DialogBox().CreateDialogError(getContext(),"Ha ocurrido un problema", errorMensaje);
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

    public void lockItems(boolean isDisabled){

        if(isDisabled){
            progress_circular_password.setVisibility(View.VISIBLE);
            btnCambiarPassword.setEnabled(false);
            //btnCambiarPassword.setBackgroundColor(Color.parseColor("#e1e1e1"));
        }
        else{
            progress_circular_password.setVisibility(View.GONE);
            btnCambiarPassword.setEnabled(true);
            //btnCambiarPassword.setBackgroundColor(Color.parseColor("#fff"));
        }
    }
}
