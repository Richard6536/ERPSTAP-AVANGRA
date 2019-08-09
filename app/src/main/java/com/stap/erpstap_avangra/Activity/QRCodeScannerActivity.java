package com.stap.erpstap_avangra.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stap.erpstap_avangra.Adapters.CardviewAdapterEmpresa;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QRCodeScannerActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;
    SessionManager sessionController;
    Button btnQR;
    List<Empresa> arrayItemsEmpresas = new ArrayList<>();
    public RecyclerView recyclerView_Empresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        ControllerActivity.activiyAbiertaActual = this;
        sessionController = new SessionManager(getApplicationContext());

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        recyclerView_Empresas = (RecyclerView)findViewById(R.id.recyclerView_empresas);
        recyclerView_Empresas.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_Empresas.setLayoutManager(mGridLayoutManager);

        btnQR = (Button)findViewById(R.id.btnEscanearQR);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                qrScan.setOrientationLocked(false);
                qrScan.setCameraId(0);
                qrScan.setPrompt("Enfoca el código qr dentro del cuadrado para escanear");
                qrScan.initiateScan();
            }
        });

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        String serializedObject = datosUsuario.get("KEY_EMPRESAS");

        if (serializedObject != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<Empresa>>(){}.getType();
            arrayItemsEmpresas = gson.fromJson(serializedObject, type);

            mostrarEmpresas();
        }
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    String idEmpresa = result.getContents();

                    //Borro el ID de la Empresa que está actualmente en sesión
                    sessionController.levantarSesion("","","","",-1, -1);

                    Intent intent = new Intent(QRCodeScannerActivity.this, MenuEmpresaActivity.class);
                    intent.putExtra("IdEmpresa",idEmpresa);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void mostrarEmpresas(){
        if(arrayItemsEmpresas.size() > 0)
        {
            CardviewAdapterEmpresa adapter = new CardviewAdapterEmpresa(getApplicationContext(), arrayItemsEmpresas, new CardviewAdapterEmpresa.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, Empresa EmpresaSeleccionada) {

                    int idEmpresa = EmpresaSeleccionada.getId();
                    String nombreEmpresa = EmpresaSeleccionada.getNombre();

                    Intent intent = new Intent(QRCodeScannerActivity.this, MenuEmpresaActivity.class);
                    intent.putExtra("IdEmpresa",idEmpresa);
                    intent.putExtra("NombreEmpresa",nombreEmpresa);
                    startActivity(intent);
                }
            });
            recyclerView_Empresas.setAdapter(adapter);
        }
        else
        {
            //txtNombreVehiculo.setText("No se ha podido obtener la lista de vehículos");
        }
    }

}
