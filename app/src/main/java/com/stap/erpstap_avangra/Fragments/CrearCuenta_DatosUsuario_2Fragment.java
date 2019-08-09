package com.stap.erpstap_avangra.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentController;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.apellido;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.email;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.nombreUs;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.password;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.rut;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.telefono;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.domicilio;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.razonSocial;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.ciudad;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.tipoDeCliente;

import com.stap.erpstap_avangra.Activity.CrearCuentaActivity;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.InternetConnection;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class CrearCuenta_DatosUsuario_2Fragment extends Fragment {

    Spinner spinner;
    public static ProgressBar progressBar_crearCuenta;
    public static Button btnFinalizarCrearCuenta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crear_cuenta__datos_usuario_2, container, false);

        rut = (EditText)view.findViewById(R.id.txtRut);
        password = (EditText)view.findViewById(R.id.txtPassword);
        telefono = (EditText)view.findViewById(R.id.txtTelefono);
        domicilio = (EditText)view.findViewById(R.id.txtDomicilio);
        ciudad = (EditText)view.findViewById(R.id.txtCiudad);

        progressBar_crearCuenta = (ProgressBar)view.findViewById(R.id.progressBar_crearCuenta);
        progressBar_crearCuenta.setVisibility(View.GONE);

        btnFinalizarCrearCuenta = (Button)view.findViewById(R.id.btnFinalizarCrearCuenta);

        btnFinalizarCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Reset errors.
                CrearCuentaActivity.password.setError(null);

                boolean cancel = false;
                View focusView = null;

                String passwordStr = CrearCuentaActivity.password.getText().toString();
                String rutStr = CrearCuentaActivity.rut.getText().toString();

                if (TextUtils.isEmpty(passwordStr)) {
                    CrearCuentaActivity.password.setError(getString(R.string.error_field_required));
                    focusView = CrearCuentaActivity.password;
                    cancel = true;

                }

                if(cancel){
                    focusView.requestFocus();
                }
                else{

                    progressBar_crearCuenta.setVisibility(View.VISIBLE);
                    btnFinalizarCrearCuenta.setEnabled(false);

                    //Verifica si hay coneción a internet antes de iniciar sesión
                    new InternetConnection.hasInternetAccess().execute(getActivity());
                }

            }
        });

        return view;
    }
}
