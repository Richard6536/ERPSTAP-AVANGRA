package com.stap.erpstap_avangra.Fragments.CarroCompra;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.stap.erpstap_avangra.Activity.ImageviewActivity;
import com.stap.erpstap_avangra.Activity.VistaPreviaCotizacionActivity;
import com.stap.erpstap_avangra.Adapters.CardviewAdapterCarroCompraPasos;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.PasosCondicion;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.Fragments.ImageViewFragment;
import com.stap.erpstap_avangra.Fragments.VerProductoFragment;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;
import static com.stap.erpstap_avangra.Clases.Condiciones.condicionSeleccionada;
import static com.stap.erpstap_avangra.Clases.ProductoEnCarro.productosEnCarro;

public class CarroCompraPasosFragment extends Fragment {

    int pasoActual = 0;
    public static int pasoUS;
    RelativeLayout loader;

    SessionManager sessionController;
    public static RecyclerView recyclerView_carro_compra_pasos;
    public static CardviewAdapterCarroCompraPasos adapterCarroCompraPasos;
    public static Button btnContinuarCrearCotizacion;
    TextView txtDescripcionCarroCompraPasos;
    public static ProgressBar progress_circular_carro_compra_pasos;
    public static List<Producto> productosTemporalEnCarroFragment = new ArrayList<>();
    Producto productoSeleccionado;
    View view;
    public static int cantidadProducto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_carro_compra_pasos, container, false);
        ControllerActivity.fragmentAbiertoActual = this;
        sessionController = new SessionManager(getActivity());

        Bundle bundle = getArguments();
        pasoActual = bundle.getInt("Paso",-1);
        pasoUS = bundle.getInt("PasoUS",-1) + 1;

        //Comprueba y Borrar los productos del paso actual cuando se presiona en volver atrás
        //borrarProductosAlVolverAtras();
        //productosEnCarroTemporal = new ArrayList<>();
        progress_circular_carro_compra_pasos = view.findViewById(R.id.progress_circular_carro_compra_pasos);
        progress_circular_carro_compra_pasos.setVisibility(View.GONE);

        btnContinuarCrearCotizacion = view.findViewById(R.id.btnContinuarCrearCotizacion);
        Drawable img0 = getContext().getResources().getDrawable( R.drawable.ic_keyboard_arrow_right_black_24dp );
        btnContinuarCrearCotizacion.setCompoundDrawablesWithIntrinsicBounds( null, null, img0, null);
        btnContinuarCrearCotizacion.setVisibility(View.GONE);

        loader = (RelativeLayout) view.findViewById(R.id.loaderPasosCarroCompra);
        loader.setVisibility(View.VISIBLE);

        int condsize = condicionSeleccionada.getPasosCondicions().size() + 1;
        btnContinuarCrearCotizacion.setText("Continuar (Paso "+ pasoUS +"/"+ condsize+")");
        btnContinuarCrearCotizacion.setBackgroundColor(Color.parseColor("#4CAF50"));

        txtDescripcionCarroCompraPasos = view.findViewById(R.id.txtDescripcionCarroCompraPasos);

        if(pasoFinal(condicionSeleccionada.getPasosCondicions().size(), pasoActual)){
            btnContinuarCrearCotizacion.setText("Crear Cotización");
            Drawable img1 = getContext().getResources().getDrawable( R.drawable.ic_add_black_24dp );
            btnContinuarCrearCotizacion.setCompoundDrawablesWithIntrinsicBounds( null, null, img1, null);
            btnContinuarCrearCotizacion.setBackgroundColor(Color.parseColor("#EB3323"));
        }

        btnContinuarCrearCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean existeProductoEnCarro = false;

                for(Iterator<Producto> itProd = productosTemporalEnCarroFragment.iterator(); itProd.hasNext();) {
                    Producto elementItProd = itProd.next();

                    for(Iterator<ProductoEnCarro> itProdCarro = ProductoEnCarro.productosEnCarro.iterator(); itProdCarro.hasNext();) {
                        ProductoEnCarro elementItProdCarro = itProdCarro.next();

                        if(elementItProdCarro.getId() == elementItProd.getId()){
                            if(elementItProd.isChecked() == false){
                                itProdCarro.remove();
                            }
                            else {

                                elementItProdCarro.setCantidad(elementItProd.getCantidad());
                                elementItProdCarro.setPaso(pasoActual);
                            }

                            existeProductoEnCarro = true;
                            break;
                        }
                    }

                    if(existeProductoEnCarro == false){
                        if(elementItProd.isChecked()){
                            ProductoEnCarro nuevoProductoEnCarro = new ProductoEnCarro();
                            nuevoProductoEnCarro.setId(elementItProd.getId());
                            nuevoProductoEnCarro.setNombre(elementItProd.getNombre());
                            nuevoProductoEnCarro.setCantidad(elementItProd.getCantidad());
                            nuevoProductoEnCarro.setValor(elementItProd.getValor()+"");
                            nuevoProductoEnCarro.setImagenes(elementItProd.getImagenes());
                            nuevoProductoEnCarro.setPaso(pasoActual);

                            ProductoEnCarro.productosEnCarro.add(nuevoProductoEnCarro);
                        }
                    }

                    existeProductoEnCarro = false;

                }

                if(pasoFinal(condicionSeleccionada.getPasosCondicions().size(), pasoActual)){

                    progress_circular_carro_compra_pasos.setVisibility(View.GONE);
                    btnContinuarCrearCotizacion.setEnabled(false);

                    Intent intent = new Intent(getContext(), VistaPreviaCotizacionActivity.class);
                    intent.putExtra("IdCondicionSeleccionada", condicionSeleccionada.getId());
                    startActivity(intent);
                    //new Cotizacion().prepararCotizacion(getActivity(),productosEnCarro, condicionSeleccionada.getId());

                }
                else{

                    //ArrayList<ProductoEnCarro> productoEnCarroArrayList = new ArrayList<>(productosEnCarro.size());
                    //productoEnCarroArrayList.addAll(productosEnCarro);

                    CarroCompraPasosFragment fragment = new CarroCompraPasosFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt("Paso", pasoActual+1);
                    arguments.putInt("PasoUS", pasoUS);
                    //arguments.putSerializable("productosEnCarroList", productoEnCarroArrayList);
                    fragment.setArguments(arguments);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fragment_container_servicios_adicionales, fragment);
                    ft.commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView_carro_compra_pasos = (RecyclerView)view.findViewById(R.id.recyclerView_carro_compra_pasos);
        recyclerView_carro_compra_pasos.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_carro_compra_pasos.setLayoutManager(mGridLayoutManager);


        llamarListaProductos();
    }

    public void llamarListaProductos(){

        HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
        String idUsuario = datosUsuario.get(SessionManager.KEY_ID);
        String llave = datosUsuario.get(SessionManager.KEY_LLAVE);
        String idEmpresa = datosUsuario.get(SessionManager.KEY_IDEMPRESA);

        int idPerfil = condicionSeleccionada.getId();
        PasosCondicion pasoActualCondicion = condicionSeleccionada.getPasosCondicions().get(pasoActual);
        int idPaso = pasoActualCondicion.getId();
        txtDescripcionCarroCompraPasos.setText(pasoActualCondicion.getNombre() + ": \n" + pasoActualCondicion.getDescripcion());

        JSONArray idsProductos = new ProductoEnCarro().obtenerIdsProductos();

        JSONObject datos = new JSONObject();

        try {

            datos.put("IdUser", idUsuario);
            datos.put("Llave",llave);
            datos.put("IdEmpresa",idEmpresa);
            datos.put("IdPerfil",idPerfil);
            datos.put("IdPaso",idPaso);
            datos.put("Productos", idsProductos);

            //Enviar datos al webservice
            new Producto.ObtenerProductosPorPaso().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void obtenerProductosPasosRespuesta(JSONObject respuestaOdata){
        try {

            loader.setVisibility(View.GONE);
            btnContinuarCrearCotizacion.setVisibility(View.VISIBLE);

            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                try {
                    JSONArray listaProductosJSONArray = respuestaOdata.getJSONArray("Productos");
                    mostrarProductos(listaProductosJSONArray);

                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
            else if(tipoRespuesta.equals("ERROR")){
                String errorMensaje = respuestaOdata.getString("Mensaje");
                new DialogBox().CreateDialogError(ControllerActivity.fragmentAbiertoActual.getContext(),"Ha ocurrido un problema", errorMensaje);
            }
            else if(tipoRespuesta.equals("ERROR_SESION")) {
                sessionController.logoutUser();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mostrarProductos(JSONArray listaProductosJSONArray) {

        if(listaProductosJSONArray.length() > 0) {

            ArrayList<Producto> listaProductosAdapter = new ArrayList<>();
            for(int x = 0; x <listaProductosJSONArray.length(); x++) {

                JSONObject proveedor = null;

                try {

                    proveedor = listaProductosJSONArray.getJSONObject(x);

                    int id = proveedor.getInt("Id");
                    String nombre = proveedor.getString("Nombre");
                    int precio = proveedor.getInt("Valor");
                    int cantidad = proveedor.getInt("Cantidad");
                    boolean marcado = proveedor.getBoolean("Marcado");
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
                    producto.setCantidad(1);
                    producto.setMarcado(marcado);
                    producto.setDescripcion(descripcion);
                    producto.setImagenes(imageList);
                    producto.setChecked(false);

                    listaProductosAdapter.add(producto);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapterCarroCompraPasos = new CardviewAdapterCarroCompraPasos(getActivity(), listaProductosAdapter, new CardviewAdapterCarroCompraPasos.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, Producto _productoSeleccionado, List<Producto> listaProductos, boolean isImage) {

                    if(isImage){
                        List<String> imagenes = _productoSeleccionado.getImagenes();

                        if(imagenes.size() > 0){

                            ArrayList<String> imgArray = new ArrayList<>(imagenes);

                            ImageViewFragment imageViewFragment = new ImageViewFragment();
                            Bundle arguments = new Bundle();

                            arguments.putString("Nombre", _productoSeleccionado.getNombre());
                            arguments.putString("Descripcion", _productoSeleccionado.getDescripcion());
                            arguments.putBoolean("MostrarDescripcion", true);
                            arguments.putStringArrayList("Imagenes", imgArray);
                            arguments.putInt("Position", 0);

                            imageViewFragment.setArguments(arguments);
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.addToBackStack(null);
                            ft.add(R.id.fragment_container_servicios_adicionales, imageViewFragment);
                            ft.commit();
                        }
                    }
                    else{

                        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_ver_producto, null);

                        Button btnAgregarAlCarro = (Button)dialogView.findViewById(R.id.btnAgregarAlCarro);
                        TextView txtTituloBottomSheetProducto = (TextView)dialogView.findViewById(R.id.txtTituloBottomSheetProducto);

                        EditText editTextCantidadProducto = (EditText) dialogView.findViewById(R.id.editTextCantidadProducto);
                        cantidadProducto = _productoSeleccionado.getCantidad();
                        editTextCantidadProducto.setText(String.valueOf(cantidadProducto));

                        Button btnAumentar = (Button)dialogView.findViewById(R.id.btnAumentar);
                        Button btnDisminuir = (Button)dialogView.findViewById(R.id.btnDisminuir);

                        btnAumentar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cantidadProducto = cantidadProducto + 1;
                                editTextCantidadProducto.setText(String.valueOf(cantidadProducto));
                            }
                        });

                        btnDisminuir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(cantidadProducto > 1){
                                    cantidadProducto = cantidadProducto - 1;
                                    editTextCantidadProducto.setText(String.valueOf(cantidadProducto));
                                }
                            }
                        });

                        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                        dialog.setContentView(dialogView);
                        dialog.show();

                        txtTituloBottomSheetProducto.setText(_productoSeleccionado.getNombre());

                        btnAgregarAlCarro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                _productoSeleccionado.setCantidad(cantidadProducto);

                                listaProductos.set(position, _productoSeleccionado);
                                productosTemporalEnCarroFragment = listaProductos;

                                adapterCarroCompraPasos.updateHolderContent(cantidadProducto);

                                dialog.dismiss();
                            }
                        });
                    }
                }
            });

            recyclerView_carro_compra_pasos.setAdapter(adapterCarroCompraPasos);
            recyclerView_carro_compra_pasos.getRecycledViewPool().setMaxRecycledViews(0,listaProductosAdapter.size());
            recyclerView_carro_compra_pasos.setItemViewCacheSize(listaProductosAdapter.size());
        }
        else
        {
            //txtNombreVehiculo.setText("No se ha podido obtener la lista de vehículos");
        }

    }

    public void RespuestaEnvioCotizacion(JSONObject respuesta){

        try {
            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            //progress_circular_carro_compra.setVisibility(View.GONE);
            btnContinuarCrearCotizacion.setEnabled(true);
            progress_circular_carro_compra_pasos.setVisibility(View.GONE);

            if(tipoRespuesta.equals("OK")){
                try {

                    BorrarProductoDelCarro();

                    new BottomNavigationController().badgeCartRemove();
                    new DialogBox().Create(ControllerActivity.fragmentAbiertoActual.getContext(), "Cotización Enviada con Éxito", "La nueva cotización se registrará en su lista de cotizaciones.", true);


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

    public boolean pasoFinal(int totalPasos, int pasoActual){
        if((pasoActual + 1) == totalPasos){
            return true;
        }
        else{
            return false;
        }
    }

    public void BorrarProductoDelCarro(){
        for (Iterator<ProductoEnCarro> iter = productosEnCarro.listIterator(); iter.hasNext(); ) {
            ProductoEnCarro prod = iter.next();
            iter.remove();
        }
    }

    public void borrarProductosAlVolverAtras(){
        for(ProductoEnCarro productoEnCarro : ProductoEnCarro.productosEnCarro){
            if(productoEnCarro.getPaso() == pasoActual){
                ProductoEnCarro.productosEnCarro.remove(productoEnCarro);
            }
        }
    }

}
