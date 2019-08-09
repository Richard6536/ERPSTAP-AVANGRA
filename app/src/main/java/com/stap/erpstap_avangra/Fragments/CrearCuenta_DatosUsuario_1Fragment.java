package com.stap.erpstap_avangra.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.stap.erpstap_avangra.Activity.CrearCuentaActivity;
import com.stap.erpstap_avangra.R;

import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.apellido;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.email;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.razonSocial;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.nombreUs;

import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.tipoDeCliente;
import static com.stap.erpstap_avangra.Activity.CrearCuentaActivity.viewPager;

public class CrearCuenta_DatosUsuario_1Fragment extends Fragment {

    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_cuenta__datos_usuario_1, container, false);

        nombreUs = (EditText)view.findViewById(R.id.txtNombreOfantasia);
        apellido = (EditText)view.findViewById(R.id.txtGiroOapellido);
        razonSocial = (EditText)view.findViewById(R.id.txtRazonSocial);
        email = (EditText)view.findViewById(R.id.txtEmail);

        razonSocial.setVisibility(View.GONE);

        spinner = (Spinner) view.findViewById(R.id.spinnerCrearCuenta);
        cargarSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoDeCliente = position;

                if(tipoDeCliente == 0){
                    nombreUs.setHint("Nombre");
                    apellido.setHint("Apellido Paterno");
                    razonSocial.setVisibility(View.GONE);
                    apellido.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_priority_high_black_24dp, 0);
                }
                else if(tipoDeCliente == 1){
                    nombreUs.setHint("Nombre Fantasia");
                    apellido.setHint("Giro");
                    apellido.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    razonSocial.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button btnContinuarCrearCuenta = (Button)view.findViewById(R.id.btnContinuarCrearCuenta);
        btnContinuarCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors.
                CrearCuentaActivity.nombreUs.setError(null);
                CrearCuentaActivity.apellido.setError(null);
                CrearCuentaActivity.email.setError(null);

                boolean cancel = false;
                View focusView = null;

                //new InternetConnection.hasInternetAccess().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getContext());
                String nombreStr = CrearCuentaActivity.nombreUs.getText().toString();
                String apellidoStr = CrearCuentaActivity.apellido.getText().toString();
                String emailStr = CrearCuentaActivity.email.getText().toString();

                if (TextUtils.isEmpty(emailStr)) {
                    CrearCuentaActivity.email.setError(getString(R.string.error_field_required));
                    focusView = CrearCuentaActivity.email;
                    cancel = true;

                } else if (TextUtils.isEmpty(nombreStr)) {
                    CrearCuentaActivity.nombreUs.setError(getString(R.string.error_field_required));
                    focusView = CrearCuentaActivity.nombreUs;
                    cancel = true;

                }else if (TextUtils.isEmpty(apellidoStr)) {
                    if(tipoDeCliente == 0){
                        CrearCuentaActivity.apellido.setError(getString(R.string.error_field_required));
                        focusView = CrearCuentaActivity.apellido;
                        cancel = true;
                    }
                }

                if(cancel){
                    focusView.requestFocus();

                } else {
                    viewPager.setCurrentItem(1);
                }
            }
        });

        return view;
    }


    public void cargarSpinner(){

        String[] arraySpinner = new String[] {
                "Particular", "Empresa"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
