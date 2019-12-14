package com.stap.erpstap_avangra.Fragments.CarroCompra;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.stap.erpstap_avangra.Activity.ServiciosAdicionalesActivity;
import com.stap.erpstap_avangra.Activity.VistaPreviaCotizacionActivity;
import com.stap.erpstap_avangra.Adapters.CardviewAdapterCarroCompra;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.Categoria;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;
import static com.stap.erpstap_avangra.Clases.Condiciones.condicionSeleccionada;
import static com.stap.erpstap_avangra.Clases.ProductoEnCarro.productosEnCarro;

public class CarroCompraMainFragment extends Fragment {

    //public static List<ProductoEnCarro> productoEnCarroList;
    SessionManager sessionController;
    public static RecyclerView recyclerView_carro_compra;
    public static LinearLayout no_prod_layout;
    public static Button btnCrearCotizacion;
    int cantidadActual = 1;
    CardviewAdapterCarroCompra adapter;
    ConstraintLayout spinnerLayout;
    //DialogView
    TextView txtCantidadProducto;
    Dialog dialog;
    SmartMaterialSpinner spinnerCondicionesCarroCompra;
    public static ProgressBar progress_circular_carro_compra;
    List<Condiciones> condiciones = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carro_compra_main, container, false);
        sessionController = new SessionManager(getActivity());
        ControllerActivity.fragmentAbiertoActual = this;
        condicionSeleccionada = null;

        toolbar.setTitle("Carro de Compra");
        //No existe productos en Carro
        spinnerLayout = (ConstraintLayout)view.findViewById(R.id.spinnerLayout);
        no_prod_layout = (LinearLayout)view.findViewById(R.id.no_prod_layout);
        no_prod_layout.setVisibility(View.GONE);

        progress_circular_carro_compra = (ProgressBar)view.findViewById(R.id.progress_circular_carro_compra);
        progress_circular_carro_compra.setVisibility(View.GONE);

        spinnerCondicionesCarroCompra = (SmartMaterialSpinner)view.findViewById(R.id.spinnerCondicionesCarroCompra);

        /*
        spinnerCondicionesCarroCompra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Condiciones condicion = Condiciones.condicionesList.get(position);
                idPerfilSeleccionado = condicion.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        Button btnBuscaMasProductos = (Button) view.findViewById(R.id.btnBuscaMasProductos);
        btnBuscaMasProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Categoria.categoriaSeleccionada = null;
                new BottomNavigationController().changeItemPosition(R.id.navigation_productos);
            }
        });


        //txtCantidadCardview = (TextView)findViewById(R.id.txtCant);
        btnCrearCotizacion = (Button)view.findViewById(R.id.btnCrearCotizacion);
        btnCrearCotizacion.setBackgroundColor(Color.parseColor("#767676"));

        recyclerView_carro_compra = (RecyclerView)view.findViewById(R.id.recyclerView_carro_compra);
        recyclerView_carro_compra.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_carro_compra.setLayoutManager(mGridLayoutManager);

        btnCrearCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionController.checkLogin() == true) {
                    if(productosEnCarro.size() > 0){
                        if(condicionSeleccionada != null){
                            if(condicionSeleccionada.getPasosCondicions().size() > 0){

                                //ArrayList<ProductoEnCarro> productoEnCarroArrayList = new ArrayList<>(productosEnCarro.size());
                                //productoEnCarroArrayList.addAll(productosEnCarro);
                                ProductoEnCarro.productosEnCarroTemporalList = productosEnCarro;

                                Intent intent = new Intent(getActivity(), ServiciosAdicionalesActivity.class);
                                startActivity(intent);
                            }
                            else {

                                progress_circular_carro_compra.setVisibility(View.VISIBLE);
                                btnCrearCotizacion.setEnabled(false);

                                Intent intent = new Intent(getContext(), VistaPreviaCotizacionActivity.class);
                                intent.putExtra("IdCondicionSeleccionada", condicionSeleccionada.getId());
                                startActivity(intent);

                            }
                        }
                        else{
                            Toast.makeText(getContext(),"Seleccione un servicio", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    new DialogBox().IniciarSesionDialog(getActivity(),"Iniciar Sesión","Necesitas Iniciar Sesión para crear una Cotización");
                }

            }
        });

        //Compuebo si vengo desde un fragment de "Pasos"
        new ProductoEnCarro().limpiarListaProductosEnCarro();
        obtenerListaPerfilCondiciones();

        return view;
    }


    public void cargarSpinner(final List<Condiciones> condiciones) {

        if( condiciones != null && condiciones.size() > 0){

            spinnerCondicionesCarroCompra.setItem(condiciones);

            //condicionSeleccionada = condiciones.get(0);
            //spinnerCondicionesCarroCompra.setSelection(0, true);

            spinnerCondicionesCarroCompra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                    condicionSeleccionada = condiciones.get(position);

                    if(condicionSeleccionada.getPasosCondicions().size() > 0){
                        Drawable img = getContext().getResources().getDrawable( R.drawable.ic_keyboard_arrow_right_black_24dp );
                        btnCrearCotizacion.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);

                        if(productosEnCarro.size() > 0){
                            int condSize = condicionSeleccionada.getPasosCondicions().size() + 1;
                            btnCrearCotizacion.setText("Continuar (Paso 1/"+condSize+")");
                            btnCrearCotizacion.setBackgroundColor(Color.parseColor("#4CAF50"));
                        }

                    }
                    else {

                        btnCrearCotizacion.setText("Crear Cotización");
                        Drawable img = getContext().getResources().getDrawable( R.drawable.ic_add_black_24dp );
                        btnCrearCotizacion.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                        btnCrearCotizacion.setBackgroundColor(Color.parseColor("#EB3323"));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    public void obtenerListaPerfilCondiciones(){



        JSONObject datos = new JSONObject();

        try {

            String idUsuario = "0";
            String llave = "";
            String idEmpresa = "1";

            if(sessionController.checkLogin() == true) {

                HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
                idUsuario = datosUsuario.get(SessionManager.KEY_ID);
                llave = datosUsuario.get(SessionManager.KEY_LLAVE);
                idEmpresa = datosUsuario.get(SessionManager.KEY_IDEMPRESA);
            }

            datos.put("IdUser", idUsuario);
            datos.put("IdEmpresa",idEmpresa);
            datos.put("Llave",llave);

            //Enviar datos al webservice
            new Empresa.ObtenerEmpresa().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void obtenerListaPerfilCondicionesRespuesta(JSONObject respuesta)
    {
        try {

            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                try {

                    JSONArray listaCondiciones = respuesta.getJSONArray("PerfilesCondicionesCotizacion");
                    condiciones = new Condiciones().cargarCondiciones(listaCondiciones);
                    cargarSpinner(condiciones);
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = respuesta.getString("Mensaje");
                new DialogBox().CreateDialogError(ControllerActivity.fragmentAbiertoActual.getContext(),"Ha ocurrido un problema", errorMensaje);
            }
            else if(tipoRespuesta.equals("ERROR_SESION")) {
                sessionController.logoutUser();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mostrarProductos()
    {
        if(productosEnCarro.size() > 0)
        {
            no_prod_layout.setVisibility(View.GONE);
            ArrayList<ProductoEnCarro> listaProductosAdapter = new ArrayList<>();
            for(ProductoEnCarro pc : productosEnCarro)
            {
                ProductoEnCarro productoEnCarro = new ProductoEnCarro();
                productoEnCarro.setId(pc.getId());
                productoEnCarro.setNombre(pc.getNombre());
                productoEnCarro.setValor(pc.getValor());
                productoEnCarro.setCantidad(pc.getCantidad());
                productoEnCarro.setImagenes(pc.getImagenes());
                listaProductosAdapter.add(productoEnCarro);
            }

            adapter = new CardviewAdapterCarroCompra(getContext(), listaProductosAdapter, new CardviewAdapterCarroCompra.OnItemClickListener() {
                @Override
                public void onItemClicked(final int position, int itemPosition, final ProductoEnCarro productoSeleccionado) {

                }
            });

            recyclerView_carro_compra.setAdapter(adapter);
        }
        else
        {
            hideElements();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        boolean BackServiciosAA = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("BackServiciosAA",false);
        if(!BackServiciosAA){
            mostrarProductos();
        }
        else{
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("BackServiciosAA", false).commit();
        }

    }

    public void hideElements(){
        recyclerView_carro_compra.setAdapter(null);
        no_prod_layout.setVisibility(View.VISIBLE);
        btnCrearCotizacion.setVisibility(View.GONE);
        spinnerLayout.setVisibility(View.GONE);
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
                    new BottomNavigationController().badgeCartRemove();
                    dialog.Create(ControllerActivity.fragmentAbiertoActual.getContext(), "Cotización Enviada con Éxito", "La nueva cotización se registrará en su lista de cotizaciones.", false);
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = respuesta.getString("Mensaje");
                new DialogBox().CreateDialogError(ControllerActivity.fragmentAbiertoActual.getContext(),"Ha ocurrido un problema", errorMensaje);
            }
            else if(tipoRespuesta.equals("ERROR_SESION")) {
                sessionController.logoutUser();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void BorrarProductoDelCarro(){
        for (Iterator<ProductoEnCarro> iter = productosEnCarro.listIterator(); iter.hasNext(); ) {
            ProductoEnCarro prod = iter.next();
            iter.remove();
        }
    }
}
