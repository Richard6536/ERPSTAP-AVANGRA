package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.stap.erpstap_avangra.Adapters.CardviewAdapterProductos;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductosListActivity extends AppCompatActivity {

    SessionManager sessionController;
    public int idSeleccionado = 0, precio;
    public static JSONArray listaProductos;
    public static JSONArray listaCondiciones;
    String nombre;
    MKLoader loader;
    public RecyclerView recyclerView_productos;
    public static CardviewAdapterProductos adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_list);
        sessionController = new SessionManager(getApplicationContext());

        ControllerActivity.activiyAbiertaActual = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        new NavigationViewHeader().NavHeaderText(navigationView, sessionController);
        navigationView.setNavigationItemSelectedListener(this);
        */

        loader = (MKLoader)findViewById(R.id.loaderProductos);
        loader.setVisibility(View.VISIBLE);

        recyclerView_productos = (RecyclerView)findViewById(R.id.recyclerView_productos);
        recyclerView_productos.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_productos.setLayoutManager(mGridLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        String idUsuario = datosUsuario.get(SessionManager.KEY_ID);
        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);
        String idEmpresa = datosUsuario.get(SessionManager.KEY_IDEMPRESA);

        JSONObject datos = new JSONObject();

        try {

            datos.put("IdUser", idUsuario);
            datos.put("IdEmpresa",idEmpresa);
            datos.put("Llave",llave);

            //Enviar datos al webservice
            new Empresa.ObtenerEmpresa().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mostrarProductos() {
        if(listaProductos.length() > 0) {
            ArrayList<Producto> listaProductosAdapter = new ArrayList<>();
            for(int x = 0; x <listaProductos.length(); x++) {

                JSONObject proveedor = null;

                try {

                    proveedor = listaProductos.getJSONObject(x);

                    int id = proveedor.getInt("Id");
                    String nombre = proveedor.getString("Nombre");
                    int precio = proveedor.getInt("Valor");
                    int cantidad = proveedor.getInt("Cantidad");
                    String descripcion = proveedor.getString("Descripcion");
                    JSONArray imagenes = proveedor.getJSONArray("DireccionImagenes");

                    ArrayList<String> imageList = new ArrayList<String>();

                    if (imagenes != null) {
                        int len = imagenes.length();
                        for (int i = 0; i < len; i++){
                            imageList.add("http://stap.cl"+imagenes.get(i).toString().substring(1));
                        }
                    }

                    Producto producto = new Producto();
                    producto.setId(id);
                    producto.setNombre(nombre);
                    producto.setValor(precio);
                    producto.setCantidad(cantidad);
                    producto.setDescripcion(descripcion);
                    producto.setImagenes(imageList);

                    listaProductosAdapter.add(producto);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter = new CardviewAdapterProductos(getApplicationContext(), listaProductosAdapter, new CardviewAdapterProductos.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, Producto productoSeleccionado) {

                    idSeleccionado = productoSeleccionado.getId();
                    nombre = productoSeleccionado.getNombre();
                    precio = productoSeleccionado.getValor();
                    int cantidad = productoSeleccionado.getCantidad();

                    List<String> imagenes = productoSeleccionado.getImagenes();
                    ArrayList<String> imgArray = new ArrayList<>(imagenes);

                    Intent intent = new Intent(ProductosListActivity.this, VerProductoActivity.class);

                    intent.putExtra("Id", idSeleccionado);
                    intent.putExtra("Nombre", nombre);
                    intent.putExtra("Precio", precio+"");
                    intent.putExtra("Cantidad",cantidad);

                    intent.putStringArrayListExtra("Imagenes",imgArray);
                    startActivity(intent);
                }
            });

            loader.setVisibility(View.GONE);
            recyclerView_productos.setAdapter(adapter);

        }
        else
        {
            //txtNombreVehiculo.setText("No se ha podido obtener la lista de vehículos");
        }

    }

    public void obtenerListaProductos(JSONObject respuesta)
    {
        try {

            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                try {
                    listaProductos = respuesta.getJSONArray("Productos");
                    mostrarProductos();

                    listaCondiciones = respuesta.getJSONArray("PerfilesCondicionesCotizacion");
                    new Condiciones().cargarCondiciones(listaCondiciones);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });

        return true;
    }
}
