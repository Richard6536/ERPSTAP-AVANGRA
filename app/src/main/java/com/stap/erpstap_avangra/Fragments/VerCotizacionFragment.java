package com.stap.erpstap_avangra.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.stap.erpstap_avangra.Adapters.CardviewAdapterCondiciones;
import com.stap.erpstap_avangra.Adapters.CardviewAdapterProductosCotizacion;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.searchItem;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;

public class VerCotizacionFragment extends Fragment {

    SessionManager sessionController;
    RecyclerView recyclerView_productos_cotizacion;
    RecyclerView recyclerView_condiciones_cotizacion;
    RelativeLayout loaderVerCotizacion;

    TextView txtSubTotalNeto, txtDescuento, txtRecargo,txtTotalNeto, txtIVA, txtTotalAPagar, txtReferencia, txtFechaCot, txtUsuarioQueAtendio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_cotizacion, container, false);
        ControllerActivity.fragmentAbiertoActual = this;
        sessionController = new SessionManager(getActivity());

        loaderVerCotizacion = (RelativeLayout)view.findViewById(R.id.loaderVerCotizacion);
        loaderVerCotizacion.setVisibility(View.VISIBLE);

        NestedScrollView scrollViewVerCot = view.findViewById(R.id.scrollViewVerCot);
        ViewCompat.setNestedScrollingEnabled(scrollViewVerCot, false);
        searchItem.setVisible(false);

        //TODO:VIEW:content_productos_cotizacion.xml
        recyclerView_productos_cotizacion = (RecyclerView)view.findViewById(R.id.recyclerView_productos_cotizacion);
        recyclerView_productos_cotizacion.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView_productos_cotizacion.setLayoutManager(mGridLayoutManager);
        //TODO:VIEW:content_productos_cotizacion.xml

        //TODO:VIEW:content_condiciones_cotizacion.xml
        recyclerView_condiciones_cotizacion = (RecyclerView)view.findViewById(R.id.recyclerView_condiciones_cotizacion);
        recyclerView_condiciones_cotizacion.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerView_condiciones_cotizacion.setLayoutManager(mGridLayoutManager2);
        //TODO:VIEW:content_condiciones_cotizacion.xml

        //TODO:VIEW:cardview_producto_cotizacion.xml
        txtSubTotalNeto = (TextView)view.findViewById(R.id.txtSubTotalNeto);
        txtDescuento = (TextView)view.findViewById(R.id.txtDescuento);
        txtRecargo = (TextView)view.findViewById(R.id.txtRecargo);
        txtTotalNeto = (TextView)view.findViewById(R.id.txtTotalNeto);
        txtIVA = (TextView)view.findViewById(R.id.txtIVA);
        txtTotalAPagar = (TextView)view.findViewById(R.id.txtTotalAPagar);
        //TODO:VIEW:cardview_producto_cotizacion.xml

        //TODO:VIEW:cardview_cotizacion_principal.xml
        txtReferencia = (TextView)view.findViewById(R.id.txtReferencia);
        txtFechaCot = (TextView)view.findViewById(R.id.txtFechaCot);
        //TODO:VIEW:cardview_cotizacion_principal.xml

        Bundle bundle = getArguments();
        int idCotizacion = bundle.getInt("Id",-1);

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

        return view;
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
                loaderVerCotizacion.setVisibility(View.GONE);
                String errorMensaje = cotizacionJSONArray.getString("Mensaje");
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

    public void MostrarCotizacion(JSONObject cotizacion){

        try {

            String codigo = cotizacion.getString("Codigo");
            toolbar.setTitle(codigo);

            JSONArray productos = cotizacion.getJSONArray("Productos");
            JSONArray condiciones = cotizacion.getJSONArray("Condiciones");

            CargarDatosPrincipales(cotizacion);
            CargarProductos(productos);
            CargarValorTotal(cotizacion);
            CargarCondiciones(condiciones);

            loaderVerCotizacion.setVisibility(View.GONE);

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

            CardviewAdapterProductosCotizacion adapter = new CardviewAdapterProductosCotizacion(getContext(), listaProductosAdapter, new CardviewAdapterProductosCotizacion.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, Producto productoSeleccionado) {
                    List<String> imagenes = productoSeleccionado.getImagenes();

                    if(imagenes.size() > 0){

                        ArrayList<String> imgArray = new ArrayList<>(imagenes);
                        /*
                        Intent intent = new Intent(getActivity(), ImageviewActivity.class);
                        Bundle arguments = new Bundle();
                        arguments.putString("Nombre",  _productoSeleccionado.getNombre());
                        arguments.putString("Descripcion", _productoSeleccionado.getDescripcion());
                        arguments.putBoolean("MostrarDescripcion", true);
                        arguments.putStringArrayList("Imagenes", imgArray);
                        arguments.putInt("Position", 0);
                        intent.putExtras(arguments); //Put your id to your next Intent
                        startActivity(intent);*/

                        ImageViewFragment imageViewFragment = new ImageViewFragment();
                        Bundle arguments = new Bundle();

                        arguments.putString("Nombre", productoSeleccionado.getNombre());
                        arguments.putString("Descripcion", productoSeleccionado.getDescripcion());
                        arguments.putBoolean("MostrarDescripcion", true);
                        arguments.putStringArrayList("Imagenes", imgArray);
                        arguments.putInt("Position", 0);

                        imageViewFragment.setArguments(arguments);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.addToBackStack(null);
                        ft.add(R.id.fragment_container, imageViewFragment);
                        ft.commit();
                    }
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


        CardviewAdapterCondiciones adapterCondiciones = new CardviewAdapterCondiciones(getContext(), condicionesListCotizacion, new CardviewAdapterCondiciones.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, int itemPosition, Condiciones condicionSeleccionada) {

            }
        });

        recyclerView_condiciones_cotizacion.setAdapter(adapterCondiciones);
    }
}
