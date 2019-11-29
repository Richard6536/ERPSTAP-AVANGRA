package com.stap.erpstap_avangra.Fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Activity.DescriptionActivity;
import com.stap.erpstap_avangra.Activity.ImageviewActivity;
import com.stap.erpstap_avangra.Activity.ServiciosAdicionalesActivity;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.searchItem;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.searchView;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;
import static com.stap.erpstap_avangra.Clases.Condiciones.condicionSeleccionada;
import static com.stap.erpstap_avangra.Clases.ProductoEnCarro.productosEnCarro;

public class VerProductoFragment extends Fragment {

    SessionManager sessionController;
    Button btnAgregarProductoAlCarro;
    CarouselView carouselView;
    TextView txtTituloProducto, txtValor, txtDescripcionProducto, txtCategoriaNombre;
    int id, cantidad, posicionImagenActual;
    String nombre, valor, descripcion, categoriaNombre;
    ArrayList<String> imagenes;
    FrameLayout gradient_view;
    CardView cardView_descripcion_producto;
    FrameLayout img_descrip;

    Button btnCrearCotizacionSeleccionarCondiciones;

    List<Condiciones> condicionesLs = new ArrayList<>();
    SmartMaterialSpinner spinnerCondicionesCarroCompraVerProducto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_producto, container, false);
        ControllerActivity.fragmentAbiertoActual = this;
        sessionController = new SessionManager(getActivity());

        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        txtTituloProducto = (TextView)view.findViewById(R.id.txtTituloProducto);
        txtValor = (TextView)view.findViewById(R.id.txtValor);
        txtDescripcionProducto = (TextView)view.findViewById(R.id.txtDescripcionProducto);
        txtCategoriaNombre = (TextView)view.findViewById(R.id.txtCategoriaNombre);
        btnAgregarProductoAlCarro = (Button)view.findViewById(R.id.btnAgregarProductoAlCarro);

        gradient_view = view.findViewById(R.id.gradient_view);
        cardView_descripcion_producto = view.findViewById(R.id.cardView_descripcion_producto);
        img_descrip = view.findViewById(R.id.img_descrip);
        img_descrip.setVisibility(View.GONE);

        searchItem.setVisible(false);

        Bundle bundle = getArguments();

        id = bundle.getInt("Id",-1);
        cantidad = bundle.getInt("Cantidad",0);
        nombre = bundle.getString("Nombre");
        valor = bundle.getString("Precio");
        descripcion = bundle.getString("Descripcion");
        categoriaNombre = bundle.getString("CategoriaNombre");

        toolbar.setTitle(nombre);

        imagenes = bundle.getStringArrayList("Imagenes");

        txtCategoriaNombre.setText("Categoria: "+ categoriaNombre);
        txtTituloProducto.setText(nombre);
        txtValor.setText("$"+ new Moneda().Format(Integer.parseInt(valor)));

        gradient_view.setVisibility(View.GONE);

        txtDescripcionProducto.setText("Sin Descripción");
        if(descripcion != null && !descripcion.equals("null")){
            String cutDescripcion = descripcion;

            if(descripcion.length() > 200){
                cutDescripcion = descripcion.substring(0, 200);
                gradient_view.setVisibility(View.VISIBLE);

                img_descrip.setVisibility(View.VISIBLE);
                cardView_descripcion_producto.setClickable(true);
                cardView_descripcion_producto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(getActivity(), DescriptionActivity.class);
                        i.putExtra("KEY_NOMBRE", nombre);
                        i.putExtra("KEY_DESCRIPTION", descripcion);
                        startActivity(i);
                    }
                });
            }

            txtDescripcionProducto.setText(cutDescripcion);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                txtDescripcionProducto.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
        }

        //Si el producto no tiene imagenes
        if(imagenes.size() == 0){
            imagenes.add("http://www.losprincipios.org/images/default.jpg");
        }

        carouselView.setPageCount(imagenes.size());
        carouselView.setImageListener(imageListener);

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {

                if(imagenes.size() > 0) {
                    if(!imagenes.get(0).equals("http://www.losprincipios.org/images/default.jpg")){
                        ArrayList<String> imgArray = new ArrayList<>(imagenes);

                        Intent intent = new Intent(getActivity(), ImageviewActivity.class);
                        Bundle arguments = new Bundle();
                        arguments.putString("Nombre", nombre);
                        arguments.putString("Descripcion", "");
                        arguments.putBoolean("MostrarDescripcion", false);
                        arguments.putStringArrayList("Imagenes", imgArray);
                        arguments.putInt("Position", position);
                        intent.putExtras(arguments); //Put your id to your next Intent
                        startActivity(intent);

                        /*
                        ImageViewFragment imageViewFragment = new ImageViewFragment();
                        Bundle arguments = new Bundle();

                        arguments.putString("Nombre", nombre);
                        arguments.putString("Descripcion", "");
                        arguments.putBoolean("MostrarDescripcion", false);
                        arguments.putStringArrayList("Imagenes", imgArray);
                        arguments.putInt("Position", position);

                        imageViewFragment.setArguments(arguments);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.addToBackStack(null);
                        ft.add(R.id.fragment_container, imageViewFragment);
                        ft.commit();*/
                    }
                }
            }
        });

        btnAgregarProductoAlCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkProduct() == true) {
                    controlBorrarProducto();
                }
                else{

                    View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_ver_producto, null);

                    Button btnAgregarAlCarro = (Button)dialogView.findViewById(R.id.btnAgregarAlCarro);
                    //btnCrearCotizacionSeleccionarCondiciones = (Button)dialogView.findViewById(R.id.btnCrearCotizacionSeleccionarCondiciones);
                    //spinnerCondicionesCarroCompraVerProducto = (SmartMaterialSpinner)dialogView.findViewById(R.id.spinnerCondicionesCarroCompraVerProducto);


                    TextView txtTituloBottomSheetProducto = (TextView)dialogView.findViewById(R.id.txtTituloBottomSheetProducto);

                    final EditText editTextCantidadProducto = (EditText) dialogView.findViewById(R.id.editTextCantidadProducto);
                    cantidad = Integer.parseInt(editTextCantidadProducto.getText().toString());

                    Button btnAumentar = (Button)dialogView.findViewById(R.id.btnAumentar);
                    Button btnDisminuir = (Button)dialogView.findViewById(R.id.btnDisminuir);

                    btnAumentar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cantidad++;
                            editTextCantidadProducto.setText(cantidad+"");
                        }
                    });

                    btnDisminuir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(cantidad > 1){
                                cantidad--;
                                editTextCantidadProducto.setText(cantidad+"");
                            }
                        }
                    });

                    final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                    dialog.setContentView(dialogView);
                    dialog.show();

                    txtTituloBottomSheetProducto.setText(nombre);

                    /*
                    btnCrearCotizacionSeleccionarCondiciones.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(sessionController.checkLogin() == true) {

                                controlAgregarProducto();
                                dialog.dismiss();

                                if(productosEnCarro.size() > 0){

                                    if(condicionSeleccionada.getPasosCondicions().size() > 0){

                                        Intent intent = new Intent(getActivity(), ServiciosAdicionalesActivity.class);
                                        intent.putExtra("NombreProd", nombre);
                                        startActivity(intent);
                                    }
                                    else {

                                        //progr.setVisibility(View.VISIBLE);
                                        btnCrearCotizacionSeleccionarCondiciones.setEnabled(false);

                                        new Cotizacion().prepararCotizacion(getActivity(),productosEnCarro, condicionSeleccionada.getId());

                                    }
                                }
                            }
                            else{
                                new DialogBox().IniciarSesionDialog(getActivity(),"Iniciar Sesión","Necesitas Iniciar Sesión para crear una Cotización");
                            }
                        }
                    });*/

                    btnAgregarAlCarro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            controlAgregarProducto();
                            dialog.dismiss();

                            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheets_ir_al_carro, null);

                            Button btnSeguirComprando = (Button)dialogView.findViewById(R.id.btnSeguirComprando);
                            Button btnIrAlCarroCompra = (Button)dialogView.findViewById(R.id.btnIrAlCarroCompra);

                            final BottomSheetDialog dialogCC = new BottomSheetDialog(getContext());
                            dialogCC.setContentView(dialogView);
                            dialogCC.show();

                            btnSeguirComprando.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogCC.dismiss();
                                    new BottomNavigationController().changeItemPosition(R.id.navigation_productos);
                                }
                            });

                            btnIrAlCarroCompra.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogCC.dismiss();
                                    new BottomNavigationController().changeItemPosition(R.id.navigation_carro_compra);

                                }
                            });

                        }
                    });

                    //obtenerListaPerfilCondiciones();
                }
            }
        });


        verificarProductoEnCarro();

        return view;
    }

    /*

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
                    condicionesLs = new Condiciones().cargarCondiciones(listaCondiciones);
                    cargarSpinner(condicionesLs);
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
    public void RespuestaEnvioCotizacion(JSONObject respuesta){

        try {
            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            //progress_circular_carro_compra.setVisibility(View.GONE);
            //btnCrearCotizacionSeleccionarCondiciones.setEnabled(true);

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
    public void cargarSpinner(final List<Condiciones> condiciones){

        spinnerCondicionesCarroCompraVerProducto.setItem(condiciones);

        //condicionSeleccionada = condiciones.get(0);
        spinnerCondicionesCarroCompraVerProducto.setSelection(0, true);

        spinnerCondicionesCarroCompraVerProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                condicionSeleccionada = condiciones.get(position);

                if(condicionSeleccionada.getPasosCondicions().size() > 0){
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_keyboard_arrow_right_black_24dp );
                    btnCrearCotizacionSeleccionarCondiciones.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);

                    int condSize = condicionSeleccionada.getPasosCondicions().size() + 1;
                    btnCrearCotizacionSeleccionarCondiciones.setText("Continuar (Paso 1/"+condSize+")");
                    btnCrearCotizacionSeleccionarCondiciones.setBackgroundColor(Color.parseColor("#4CAF50"));

                }
                else {

                    btnCrearCotizacionSeleccionarCondiciones.setText("Crear Cotización");
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_add_black_24dp );
                    btnCrearCotizacionSeleccionarCondiciones.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                    btnCrearCotizacionSeleccionarCondiciones.setBackgroundColor(Color.parseColor("#EB3323"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
*/

    public void verificarProductoEnCarro(){
        if (checkProduct() == true) {
            btnAgregarProductoAlCarro.setText("Quitar producto del Carro");
            //floatingActionButtonAddToCart.setImageResource(R.drawable.ic_check_black);
            btnAgregarProductoAlCarro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c41e3a")));
        }
    }

    public void controlAgregarProducto(){

        new ProductoEnCarro().crearProductoEnCarro(id, nombre, valor, cantidad, imagenes);

        //floatingActionButtonAddToCart.setImageResource(R.drawable.ic_check_black);
        btnAgregarProductoAlCarro.setText("Quitar producto del Carro");
        btnAgregarProductoAlCarro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c41e3a")));

        new BottomNavigationController().updateCountBadgeCart(ProductoEnCarro.productosEnCarro.size());
    }

    public void controlBorrarProducto(){
        BorrarProductoDelCarro();
        btnAgregarProductoAlCarro.setText("Agregar al Carro");
        //floatingActionButtonAddToCart.setImageResource(R.drawable.ic_add_shopping_cart);
        btnAgregarProductoAlCarro.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#083884")));

        new BottomNavigationController().updateCountBadgeCart(ProductoEnCarro.productosEnCarro.size());
    }

    public Boolean checkProduct(){
        boolean existe = false;
        for(ProductoEnCarro p : ProductoEnCarro.productosEnCarro){
            if(p.getId() == id){
                existe = true;
            }
        }
        return existe;
    }

    public void BorrarProductoDelCarro(){
        for (Iterator<ProductoEnCarro> iter = ProductoEnCarro.productosEnCarro.listIterator(); iter.hasNext(); ) {
            ProductoEnCarro prod = iter.next();
            if (prod.getId() == id) {
                iter.remove();
            }
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(getActivity()).load(imagenes.get(position)).into(imageView);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(nombre);
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("BackServiciosAA", false).commit();
    }
}
