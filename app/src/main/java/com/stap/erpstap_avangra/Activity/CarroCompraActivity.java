package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.stap.erpstap_avangra.Adapters.CardviewAdapterCarroCompra;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CarroCompraActivity extends AppCompatActivity {
    public static List<ProductoEnCarro> productoEnCarroList;
    SessionManager sessionController;
    public RecyclerView recyclerView_carro_compra;
    public static RelativeLayout no_prod_layout;
    public static Button btnCrearCotizacion;
    int cantidadActual = 1;
    CardviewAdapterCarroCompra adapter;
    //DialogView
    TextView txtCantidadProducto, txtCantidadCardview;
    Dialog dialog;
    Spinner spinnerCondicionesCarroCompra;
    int idPerfilSeleccionado = 0;
    public static ProgressBar progress_circular_carro_compra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro_compra);
        sessionController = new SessionManager(getApplicationContext());
        ControllerActivity.activiyAbiertaActual = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        no_prod_layout = (RelativeLayout)findViewById(R.id.no_prod_layout);
        no_prod_layout.setVisibility(View.GONE);
        progress_circular_carro_compra = (ProgressBar)findViewById(R.id.progress_circular_carro_compra);
        progress_circular_carro_compra.setVisibility(View.GONE);

        //List<Condiciones> condiciones = Condiciones.condicionesList;
        spinnerCondicionesCarroCompra = (Spinner)findViewById(R.id.spinnerCondicionesCarroCompra);
        //cargarSpinner(condiciones);


        spinnerCondicionesCarroCompra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Condiciones condicion = Condiciones.condicionesList.get(position);
                //idPerfilSeleccionado = condicion.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //txtCantidadCardview = (TextView)findViewById(R.id.txtCant);
        btnCrearCotizacion = (Button)findViewById(R.id.btnCrearCotizacion);

        recyclerView_carro_compra = (RecyclerView)findViewById(R.id.recyclerView_carro_compra);
        recyclerView_carro_compra.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_carro_compra.setLayoutManager(mGridLayoutManager);

        btnCrearCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(productoEnCarroList.size() > 0){

                    progress_circular_carro_compra.setVisibility(View.VISIBLE);
                    btnCrearCotizacion.setEnabled(false);

                    HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                    int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
                    String llave = datosUsuario.get(SessionManager.KEY_LLAVE);
                    int idEmpresa = Integer.parseInt(datosUsuario.get(SessionManager.KEY_IDEMPRESA));

                    JSONObject datos = new JSONObject();
                    JSONArray productosEnCotizacion = new JSONArray();

                    try {

                        for(ProductoEnCarro pc : productoEnCarroList){

                            JSONObject productoJson = new JSONObject();
                            productoJson.put("Id", pc.getId());
                            productoJson.put("Cantidad", pc.getCantidad());

                            productosEnCotizacion.put(productoJson);
                        }

                        datos.put("IdUser", idUsuario);
                        datos.put("IdEmpresa",idEmpresa);
                        datos.put("Llave",llave);
                        datos.put("Productos", productosEnCotizacion);
                        datos.put("IdPerfilCondiciones", idPerfilSeleccionado);

                        //Enviar datos al webservice
                        new ProductoEnCarro.CrearCotizacion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void cargarSpinner(List<Condiciones> condiciones){

        if( condiciones != null && condiciones.size() > 0){
            ArrayAdapter<Condiciones> dataAdapter = new ArrayAdapter<Condiciones>(getApplicationContext(), android.R.layout.simple_spinner_item, condiciones);
            spinnerCondicionesCarroCompra.setAdapter(dataAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        productoEnCarroList = ProductoEnCarro.productosEnCarro;
        productoEnCarroList.size();

        mostrarProductos();
    }

    public void mostrarProductos()
    {
        if(productoEnCarroList.size() > 0)
        {
            ArrayList<ProductoEnCarro> listaProductosAdapter = new ArrayList<>();
            for(ProductoEnCarro pc : productoEnCarroList)
            {
                ProductoEnCarro productoEnCarro = new ProductoEnCarro();
                productoEnCarro.setId(pc.getId());
                productoEnCarro.setNombre(pc.getNombre());
                productoEnCarro.setValor(pc.getValor());
                productoEnCarro.setCantidad(pc.getCantidad());
                productoEnCarro.setImagenes(pc.getImagenes());
                listaProductosAdapter.add(productoEnCarro);
            }

            adapter = new CardviewAdapterCarroCompra(getApplicationContext(), listaProductosAdapter, new CardviewAdapterCarroCompra.OnItemClickListener() {
                @Override
                public void onItemClicked(final int position, int itemPosition, final ProductoEnCarro productoSeleccionado) {

                    dialog = new Dialog(CarroCompraActivity.this);
                    dialog.setContentView(R.layout.dialog_quantity_product);

                    txtCantidadProducto = (TextView)dialog.findViewById(R.id.txtCantidadProducto);

                    cantidadActual = productoSeleccionado.getCantidad();
                    txtCantidadProducto.setText(cantidadActual+"");
                    //txtCantidadCardview.setText("Cantidad: "+cantidadActual+" (cambiar)");

                    Button btn_aceptar_cantidad = (Button)dialog.findViewById(R.id.btn_aceptar_cantidad);
                    ImageButton imgButtonAdd = (ImageButton)dialog.findViewById(R.id.imgButtonAdd);
                    ImageButton imgButtonMinus = (ImageButton)dialog.findViewById(R.id.imgButtonMinus);

                    imgButtonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cantidadActual = cantidadActual + 1;
                            txtCantidadProducto.setText(cantidadActual+"");
                        }
                    });

                    imgButtonMinus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cantidadActual = cantidadActual - 1;
                            if(cantidadActual >= 1){
                                txtCantidadProducto.setText(cantidadActual+"");
                            }

                        }
                    });

                    btn_aceptar_cantidad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            for(ProductoEnCarro productoEnCarro : productoEnCarroList){
                                if(productoEnCarro.getId() == productoSeleccionado.getId()){
                                    productoEnCarro.setCantidad(cantidadActual);
                                }
                            }

                            RecyclerView.ViewHolder holder = recyclerView_carro_compra.findViewHolderForAdapterPosition(position);
                            adapter.notifyAdapter(position, cantidadActual);
                            dialog.dismiss();
                        }
                    });

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.show();
                }
            });
            recyclerView_carro_compra.setAdapter(adapter);
        }
        else
        {
            hideElements();
        }

    }

    public void hideElements(){
        recyclerView_carro_compra.setAdapter(null);
        no_prod_layout.setVisibility(View.VISIBLE);
        btnCrearCotizacion.setVisibility(View.GONE);
        spinnerCondicionesCarroCompra.setVisibility(View.GONE);
    }

    public void RespuestaEnvioCotizacion(JSONObject respuesta){

        try {
           String tipoRespuesta = respuesta.getString("TipoRespuesta");
            progress_circular_carro_compra.setVisibility(View.GONE);
            btnCrearCotizacion.setEnabled(true);

            if(tipoRespuesta.equals("OK")){
                try {
                    DialogBox dialog = new DialogBox();
                    BorrarProductoDelCarro();
                    dialog.Create(this, "Cotización Enviada con Éxito", "La nueva cotización se registrará en su lista de cotizaciones.", false);
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = respuesta.getString("Mensaje");
                new DialogBox().CreateDialogError(getApplicationContext(),"Ha ocurrido un problema", errorMensaje);
            }
            else if(tipoRespuesta.equals("ERROR_SESION")) {
                sessionController.logoutUser();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void BorrarProductoDelCarro(){
        for (Iterator<ProductoEnCarro> iter = ProductoEnCarro.productosEnCarro.listIterator(); iter.hasNext(); ) {
            ProductoEnCarro prod = iter.next();
            iter.remove();
        }
    }

}
