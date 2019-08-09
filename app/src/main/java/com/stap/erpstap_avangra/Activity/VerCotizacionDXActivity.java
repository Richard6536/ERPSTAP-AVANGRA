package com.stap.erpstap_avangra.Activity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.stap.erpstap_avangra.Adapters.CardviewAdapterCondiciones;
import com.stap.erpstap_avangra.Adapters.CardviewAdapterProductosCotizacion;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.TextView;

import com.stap.erpstap_avangra.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VerCotizacionDXActivity extends AppCompatActivity {

    SessionManager sessionController;
    RecyclerView recyclerView_productos_cotizacion;
    RecyclerView recyclerView_condiciones_cotizacion;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    TextView txtSubTotalNeto, txtDescuento, txtRecargo,txtTotalNeto, txtIVA, txtTotalAPagar, txtReferencia, txtFechaCot, txtUsuarioQueAtendio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cotizacion_dx);
        sessionController = new SessionManager(getApplicationContext());
        ControllerActivity.activiyAbiertaActual = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //TODO:VIEW:content_productos_cotizacion.xml
        recyclerView_productos_cotizacion = (RecyclerView)findViewById(R.id.recyclerView_productos_cotizacion);
        recyclerView_productos_cotizacion.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView_productos_cotizacion.setLayoutManager(mGridLayoutManager);
        //TODO:VIEW:content_productos_cotizacion.xml

        //TODO:VIEW:content_condiciones_cotizacion.xml
        recyclerView_condiciones_cotizacion = (RecyclerView)findViewById(R.id.recyclerView_condiciones_cotizacion);
        recyclerView_condiciones_cotizacion.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager2 = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView_condiciones_cotizacion.setLayoutManager(mGridLayoutManager2);
        //TODO:VIEW:content_condiciones_cotizacion.xml

        //TODO:VIEW:cardview_producto_cotizacion.xml
        txtSubTotalNeto = (TextView)findViewById(R.id.txtSubTotalNeto);
        txtDescuento = (TextView)findViewById(R.id.txtDescuento);
        txtRecargo = (TextView)findViewById(R.id.txtRecargo);
        txtTotalNeto = (TextView)findViewById(R.id.txtTotalNeto);
        txtIVA = (TextView)findViewById(R.id.txtIVA);
        txtTotalAPagar = (TextView)findViewById(R.id.txtTotalAPagar);
        //TODO:VIEW:cardview_producto_cotizacion.xml

        //TODO:VIEW:cardview_cotizacion_principal.xml
        txtReferencia = (TextView)findViewById(R.id.txtReferencia);
        txtFechaCot = (TextView)findViewById(R.id.txtFechaCot);
        //TODO:VIEW:cardview_cotizacion_principal.xml

        int idCotizacion = getIntent().getIntExtra("Id",-1);

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        int idUsuario = Integer.parseInt(datosUsuario.get(SessionManager.KEY_ID));
        int idEmpresa = Integer.parseInt(datosUsuario.get(SessionManager.KEY_IDEMPRESA));
        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);

        JSONObject datos = new JSONObject();

        try {

            datos.put("IdUser", idUsuario);
            datos.put("IdEmpresa", idEmpresa);
            datos.put("Llave", llave);
            datos.put("IdCotizacion", idCotizacion);

            //Enviar datos al webservice
            new Cotizacion.VerCotizacion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void RecibirCotizacion(JSONObject cotizacionJSONArray){
        try {
            String tipoRespuesta = cotizacionJSONArray.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){

                JSONArray jsonArray = cotizacionJSONArray.getJSONArray("Cotizaciones");
                JSONObject cotizacion = jsonArray.getJSONObject(0);
                MostrarCotizacion(cotizacion);
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = cotizacionJSONArray.getString("Mensaje");
                new DialogBox().CreateDialogError(getApplicationContext(),"Ha ocurrido un problema", errorMensaje);
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

    public void MostrarCotizacion(JSONObject cotizacion){

        try {

            String codigo = cotizacion.getString("Codigo");
            collapsingToolbarLayout.setTitle(codigo);

            JSONArray productos = cotizacion.getJSONArray("Productos");
            JSONArray condiciones = cotizacion.getJSONArray("Condiciones");

            CargarDatosPrincipales(cotizacion);
            CargarProductos(productos);
            CargarValorTotal(cotizacion);
            CargarCondiciones(condiciones);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CargarDatosPrincipales(JSONObject cotizacion){

        try {

            String referencia = cotizacion.getString("Referencia");
            String fecha = cotizacion.getString("Fecha");
            String fechaSplit = fecha.split("T")[0];

            txtReferencia.setText(referencia);
            txtFechaCot.setText(fechaSplit);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CargarProductos(JSONArray listaProductos){
        if(listaProductos.length() > 0)
        {
            ArrayList<Producto> listaProductosAdapter = new ArrayList<>();
            for(int x = 0; x <listaProductos.length(); x++)
            {
                JSONObject proveedor = null;

                try {

                    proveedor = listaProductos.getJSONObject(x);

                    int id = proveedor.getInt("Id");
                    String nombre = proveedor.getString("Nombre");
                    int precio = Integer.parseInt(proveedor.getString("Valor"));
                    int cantidad = proveedor.getInt("Cantidad");
                    JSONArray imagenes = proveedor.getJSONArray("DireccionImagenes");

                    ArrayList<String> imageList = new ArrayList<String>();

                    if (imagenes != null) {
                        int len = imagenes.length();
                        for (int i=0;i<len;i++){
                            imageList.add("http://stap.cl"+imagenes.get(i).toString().substring(1));
                        }
                    }

                    Producto producto = new Producto();
                    producto.setId(id);
                    producto.setNombre(nombre);
                    producto.setValor(precio);
                    producto.setCantidad(cantidad);
                    producto.setImagenes(imageList);

                    listaProductosAdapter.add(producto);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            CardviewAdapterProductosCotizacion adapter = new CardviewAdapterProductosCotizacion(getApplicationContext(), listaProductosAdapter, new CardviewAdapterProductosCotizacion.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, Producto productoSeleccionado) {

                }
            });

            recyclerView_productos_cotizacion.setAdapter(adapter);

        }
        else
        {
            //txtNombreVehiculo.setText("No se ha podido obtener la lista de vehículos");
        }
    }

    public void CargarValorTotal(JSONObject cotizacion){
        try {


            int subTotalNeto = cotizacion.getInt("SubTotalNeto");
            int descuento = cotizacion.getInt("Descuento");
            int recargo = cotizacion.getInt("Recargo");
            int totalNeto = cotizacion.getInt("TotalNeto");
            int iva = cotizacion.getInt("IVA");
            int totalPagar = cotizacion.getInt("TotalAPagar");

            txtSubTotalNeto.setText("$"+ new Moneda().Format(subTotalNeto));
            txtDescuento.setText("$"+ new Moneda().Format(descuento));
            txtRecargo.setText("$"+ new Moneda().Format(recargo));
            txtTotalNeto.setText("$"+ new Moneda().Format(totalNeto));
            txtIVA.setText("$"+ new Moneda().Format(iva));
            txtTotalAPagar.setText("$"+ new Moneda().Format(totalPagar));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void CargarCondiciones(JSONArray condiciones){

        List<String> nombresItems = new Condiciones().obtenerNombresItem(condiciones);
        List<Condiciones> condicionesListCotizacion = new ArrayList<>();

        try
        {

            for(int y = 0; y <nombresItems.size(); y++){

                Condiciones condicionesCotizacionTitulo = new Condiciones();
                condicionesCotizacionTitulo.setNombreItem(nombresItems.get(y));
                condicionesCotizacionTitulo.setEsTitulo(true);
                condicionesCotizacionTitulo.setDescripcion("");
                condicionesListCotizacion.add(condicionesCotizacionTitulo);

                for(int x = 0; x <condiciones.length(); x++){

                    JSONObject condicionJSON = null;
                    condicionJSON = condiciones.getJSONObject(x);
                    String nombreItem = condicionJSON.getString("NombreItem");
                    String descripcion = condicionJSON.getString("Descripcion");

                    if(nombreItem.equals(nombresItems.get(y))){

                        Condiciones condicionesCotizacion = new Condiciones();
                        condicionesCotizacion.setNombreItem("");
                        condicionesCotizacion.setEsTitulo(false);
                        condicionesCotizacion.setDescripcion(descripcion);

                        condicionesListCotizacion.add(condicionesCotizacion);

                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        CardviewAdapterCondiciones adapterCondiciones = new CardviewAdapterCondiciones(getApplicationContext(), condicionesListCotizacion, new CardviewAdapterCondiciones.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, int itemPosition, Condiciones condicionSeleccionada) {

            }
        });

        recyclerView_condiciones_cotizacion.setAdapter(adapterCondiciones);
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

}
