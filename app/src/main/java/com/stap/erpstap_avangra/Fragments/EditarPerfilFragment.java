package com.stap.erpstap_avangra.Fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.stap.erpstap_avangra.Clases.ConfiguracionCuenta;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.InternetConnection;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditarPerfilFragment extends Fragment {

    SessionManager sessionController;
    public FloatingActionButton floatingActionButtonEditPerfil;

    public TextInputEditText editEmailPerfil, editNombreOrFantasiaPerfil, editApellidoOrGiroPerfil, editRazonSocialPerfil,
            editRutPerfil, editTelefonoPerfil, editDomicilioPerfil, editCiudadPerfil;

    public TextInputLayout layoutEmailPerfil, layoutNombreOrFantasiaPerfil, layoutApellidoOrGiroPerfil, layoutRazonSocialPerfil,
            layoutRutPerfil, layoutTelefonoPerfil, layoutDomicilioPerfil, layoutCiudadPerfil;

    public ConstraintLayout spinnerTipoClienteLayout;

    public String tipoCliente, nombreCompleto, email,
            nombre, apellidoPaterno, nombreFantasia, razonSocial, giro, telefono, rut, domicilio, ciudad;

    public int tipoDeCliente;
    public Spinner spinnerTipoClienteCuenta;
    public boolean editableMode = true;
    int idUsuario;
    String llave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        ControllerActivity.fragmentAbiertoActual = this;
        sessionController = new SessionManager(getActivity());

        spinnerTipoClienteLayout = view.findViewById(R.id.spinnerTipoClienteLayout);
        spinnerTipoClienteCuenta = view.findViewById(R.id.spinnerTipoClienteCuenta);

        String[] arraySpinner = new String[] {
                "Particular", "Empresa"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoClienteCuenta.setAdapter(adapter);


        //Grupo 1
        editEmailPerfil = view.findViewById(R.id.editEmailPerfil);
        editNombreOrFantasiaPerfil = view.findViewById(R.id.editNombreOrFantasiaPerfil);
        editApellidoOrGiroPerfil = view.findViewById(R.id.editApellidoOrGiroPerfil);
        editRazonSocialPerfil = view.findViewById(R.id.editRazonSocialPerfil);

        //Grupo 2
        editRutPerfil = view.findViewById(R.id.editRutPerfil);
        editTelefonoPerfil = view.findViewById(R.id.editTelefonoPerfil);
        editDomicilioPerfil = view.findViewById(R.id.editDomicilioPerfil);
        editCiudadPerfil = view.findViewById(R.id.editCiudadPerfil);

        //Layout
        layoutEmailPerfil = view.findViewById(R.id.layoutEmailPerfil);
        layoutNombreOrFantasiaPerfil = view.findViewById(R.id.layoutNombreOrFantasiaPerfil);
        layoutApellidoOrGiroPerfil = view.findViewById(R.id.layoutApellidoOrGiroPerfil);
        layoutRazonSocialPerfil = view.findViewById(R.id.layoutRazonSocialPerfil);

        //Layout
        layoutRutPerfil = view.findViewById(R.id.layoutRutPerfil);
        layoutTelefonoPerfil = view.findViewById(R.id.layoutTelefonoPerfil);
        layoutDomicilioPerfil = view.findViewById(R.id.layoutDomicilioPerfil);
        layoutCiudadPerfil = view.findViewById(R.id.layoutCiudadPerfil);

        modifyInputsLayouts("#e1e1e1", true, false);

        floatingActionButtonEditPerfil = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonEditPerfil);
        floatingActionButtonEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableAccountEdit(editableMode);
            }
        });

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
        llave = datosUsuario.get(SessionManager.KEY_LLAVE);

        obtenerUsuarioCliente(idUsuario, llave);

        return view;
    }

    public void modifyInputsLayouts(String color, boolean inputsDisabled, boolean spinnerEnabled){

        colorSpinnerLayout(spinnerTipoClienteLayout, color, spinnerEnabled);

        disableTextInputEditText(editEmailPerfil, true);
        disableTextInputEditText(editNombreOrFantasiaPerfil, inputsDisabled);
        disableTextInputEditText(editApellidoOrGiroPerfil, inputsDisabled);
        disableTextInputEditText(editRazonSocialPerfil, inputsDisabled);

        disableTextInputEditText(editRutPerfil, inputsDisabled);
        disableTextInputEditText(editTelefonoPerfil, inputsDisabled);
        disableTextInputEditText(editDomicilioPerfil, inputsDisabled);
        disableTextInputEditText(editCiudadPerfil, inputsDisabled);

        colorTextInputLayout(layoutNombreOrFantasiaPerfil,color);
        colorTextInputLayout(layoutApellidoOrGiroPerfil,color);
        colorTextInputLayout(layoutRazonSocialPerfil,color);

        colorTextInputLayout(layoutEmailPerfil,"#e1e1e1");
        colorTextInputLayout(layoutRutPerfil,color);
        colorTextInputLayout(layoutTelefonoPerfil,color);
        colorTextInputLayout(layoutDomicilioPerfil,color);
        colorTextInputLayout(layoutCiudadPerfil,color);
    }

    public void disableTextInputEditText(TextInputEditText textInputEditText,  boolean inputsDisabled){
        if(inputsDisabled){
            textInputEditText.setClickable(false);
            textInputEditText.setInputType(InputType.TYPE_NULL);

        }
        else{
            textInputEditText.setClickable(true);
            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    public void colorTextInputLayout(TextInputLayout textInputLayout, String color){
        textInputLayout.setBoxBackgroundColor(Color.parseColor(color));
    }

    public void colorSpinnerLayout(ConstraintLayout spinnerLayout, String color, boolean spinnerEnabled){
        spinnerLayout.setBackgroundColor(Color.parseColor(color));
        spinnerTipoClienteCuenta.setEnabled(spinnerEnabled);
    }

    public void obtenerUsuarioCliente(int id, String llave){

        JSONObject datos = new JSONObject();

        try {

            datos.put("Id", id);
            datos.put("Llave", llave);

            //Enviar datos al webservice
            new ConfiguracionCuenta.ObtenerUsuarioClienteFragment().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void obtenerUsuarioClienteRespuesta(JSONObject respuestaOdata){
        try {

            JSONObject usuarioCliente = respuestaOdata.getJSONObject("UsuarioCliente");

            tipoCliente = usuarioCliente.getString("TipoDeCliente");
            nombreCompleto = usuarioCliente.getString("NombreCompleto");

            email = usuarioCliente.getString("Email");
            nombre = usuarioCliente.getString("Nombre");
            apellidoPaterno = usuarioCliente.getString("ApellidoPaterno");

            nombreFantasia = usuarioCliente.getString("NombreFantasia");
            razonSocial = usuarioCliente.getString("RazonSocial");
            giro = usuarioCliente.getString("Giro");

            telefono = usuarioCliente.getString("Telefono");
            rut = usuarioCliente.getString("Rut");
            domicilio = usuarioCliente.getString("Domicilio");
            ciudad = usuarioCliente.getString("Ciudad");

            editEmailPerfil.setText(email);
            if(tipoCliente.equals("Particular")){
                layoutNombreOrFantasiaPerfil.setHint("Nombre");
                layoutApellidoOrGiroPerfil.setHint("Apellido Paterno");

                spinnerTipoClienteCuenta.setSelection(0);
                editNombreOrFantasiaPerfil.setText(nombre);
                editApellidoOrGiroPerfil.setText(apellidoPaterno);
                layoutRazonSocialPerfil.setVisibility(View.GONE);
            }
            else if(tipoCliente.equals("Empresa")){
                layoutNombreOrFantasiaPerfil.setHint("Nombre Fantasía");
                layoutApellidoOrGiroPerfil.setHint("Giro");

                spinnerTipoClienteCuenta.setSelection(1);
                layoutRazonSocialPerfil.setVisibility(View.VISIBLE);
                editRazonSocialPerfil.setText(razonSocial);
                editNombreOrFantasiaPerfil.setText(nombreFantasia);
                editApellidoOrGiroPerfil.setText(giro);

            }

            editRutPerfil.setText(rut);
            editTelefonoPerfil.setText(telefono);
            editDomicilioPerfil.setText(domicilio);
            editCiudadPerfil.setText(ciudad);

            spinnerTipoClienteCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tipoDeCliente = position;

                    if(tipoDeCliente == 0){

                        layoutNombreOrFantasiaPerfil.setHint("Nombre");
                        layoutApellidoOrGiroPerfil.setHint("Apellido Paterno");

                        if(!tipoCliente.equals("Particular")){
                            editNombreOrFantasiaPerfil.setText("");
                            editApellidoOrGiroPerfil.setText("");
                            editRazonSocialPerfil.setText("");
                        }

                        layoutRazonSocialPerfil.setVisibility(View.GONE);

                    }
                    else if(tipoDeCliente == 1){

                        layoutNombreOrFantasiaPerfil.setHint("Nombre Fantasía");
                        layoutApellidoOrGiroPerfil.setHint("Giro");

                        if(!tipoCliente.equals("Empresa")){
                            editNombreOrFantasiaPerfil.setText("");
                            editApellidoOrGiroPerfil.setText("");
                        }

                        layoutRazonSocialPerfil.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void enableAccountEdit(boolean isEnabled){

        if(isEnabled){

            editableMode = false;
            floatingActionButtonEditPerfil.setImageResource(R.drawable.ic_save_black_24dp);
            modifyInputsLayouts("#ffffff",false, true);
        }
        else
        {
            editableMode = true;
            modifyInputsLayouts("#e1e1e1",true, false);

            updateSaveDataAccount();
        }
    }

    public void updateSaveDataAccount(){
        //Verifica si hay coneción a internet antes de iniciar sesión
        new InternetConnection.hasInternetAccess().execute(getActivity());
    }

    public void validar(boolean isConnectionEnable)
    {
        if(isConnectionEnable == false)
        {
            new DialogBox().CreateDialogError(getContext(),"Error de Conexión","Compruebe su conexión a Internet");
        }
        else
        {
            JSONObject datosCuenta = new JSONObject();

            try {

                datosCuenta.put("Id", idUsuario);
                datosCuenta.put("Llave", llave);
                datosCuenta.put("TipoDeCliente", tipoDeCliente);

                if (tipoDeCliente == 0){

                    datosCuenta.put("Nombre", editNombreOrFantasiaPerfil.getText());
                    datosCuenta.put("ApellidoPaterno", editApellidoOrGiroPerfil.getText());

                    datosCuenta.put("NombreFantasia", "");
                    datosCuenta.put("Giro", "");
                    datosCuenta.put("RazonSocial", "");
                }
                else if(tipoDeCliente == 1){

                    datosCuenta.put("NombreFantasia", editNombreOrFantasiaPerfil.getText());
                    datosCuenta.put("Giro", editApellidoOrGiroPerfil.getText());
                    datosCuenta.put("RazonSocial", editRazonSocialPerfil.getText());

                    datosCuenta.put("Nombre", "");
                    datosCuenta.put("ApellidoPaterno", "");
                }

                datosCuenta.put("Telefono", editTelefonoPerfil.getText());
                datosCuenta.put("Rut", editRutPerfil.getText());
                datosCuenta.put("Domicilio", editDomicilioPerfil.getText());
                datosCuenta.put("Ciudad", editCiudadPerfil.getText());



            } catch (JSONException e) {
                e.printStackTrace();
            }

            new SessionManager.EditarDatosCliente().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datosCuenta.toString());
        }
    }

    public void estadoCuenta(JSONObject respuestaOdata){
        try {

            floatingActionButtonEditPerfil.setImageResource(R.drawable.ic_edit_black_24dp);
            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){

                Toast.makeText(getContext(),"Cuenta Editada", Toast.LENGTH_LONG).show();

            }
            else if (tipoRespuesta.equals("ERROR")){
                backDataAccount();
                String errorMensaje = respuestaOdata.getString("Mensaje");
                new DialogBox().CreateDialogError(getContext(),"Ha ocurrido un problema", errorMensaje);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void backDataAccount(){

        if(tipoCliente.equals("Particular")){

            spinnerTipoClienteCuenta.setSelection(0);
            editNombreOrFantasiaPerfil.setText(nombre);
            editApellidoOrGiroPerfil.setText(apellidoPaterno);
            layoutRazonSocialPerfil.setVisibility(View.GONE);
        }
        else if(tipoCliente.equals("Empresa")){

            spinnerTipoClienteCuenta.setSelection(1);
            layoutRazonSocialPerfil.setVisibility(View.VISIBLE);
            editRazonSocialPerfil.setText(razonSocial);
            editNombreOrFantasiaPerfil.setText(nombreFantasia);
            editApellidoOrGiroPerfil.setText(giro);

        }

        editRutPerfil.setText(rut);
        editTelefonoPerfil.setText(telefono);
        editDomicilioPerfil.setText(domicilio);
        editCiudadPerfil.setText(ciudad);
    }

    public void updateLocalData(){
        if(tipoCliente.equals("Particular")){

            spinnerTipoClienteCuenta.setSelection(0);
            editNombreOrFantasiaPerfil.setText(nombre);
            editApellidoOrGiroPerfil.setText(apellidoPaterno);
            layoutRazonSocialPerfil.setVisibility(View.GONE);
        }
        else if(tipoCliente.equals("Empresa")){

            spinnerTipoClienteCuenta.setSelection(1);
            layoutRazonSocialPerfil.setVisibility(View.VISIBLE);
            editRazonSocialPerfil.setText(razonSocial);
            editNombreOrFantasiaPerfil.setText(nombreFantasia);
            editApellidoOrGiroPerfil.setText(giro);

        }

        editRutPerfil.setText(rut);
        editTelefonoPerfil.setText(telefono);
        editDomicilioPerfil.setText(domicilio);
        editCiudadPerfil.setText(ciudad);
    }
}
