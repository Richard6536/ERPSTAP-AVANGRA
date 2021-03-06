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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.stap.erpstap_avangra.Adapters.CardviewAdapterCaracteristicasProducto;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.CaracteristicaProducto;
import com.stap.erpstap_avangra.Clases.Condiciones;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Clases.FiltroAvanzado;
import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.Producto;
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
    View view;

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
        view = inflater.inflate(R.layout.fragment_ver_producto, container, false);
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


        if(FiltroAvanzado.is_busqueda_avanzada){
            mostrarCaracteristicas();
        }

        return view;
    }

    public void mostrarCaracteristicas(){
        List<CaracteristicaProducto> caracteristicasProducto = new ArrayList<>();

        for(Producto p : Producto.productosList){
            if(id == p.getId()){
                caracteristicasProducto = p.getCaracteristicasProducto();
            }
        }

        CardView cardView_caracteristicas = (CardView)view.findViewById(R.id.cardView_caracteristicas);
        cardView_caracteristicas.setVisibility(View.VISIBLE);

        //TODO:VIEW:content_caracteristicas_ver_producto.xml
        RecyclerView recyclerView_condiciones_cotizacion = (RecyclerView)view.findViewById(R.id.recyclerView_caracteristicas_ver_producto);
        recyclerView_condiciones_cotizacion.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerView_condiciones_cotizacion.setLayoutManager(mGridLayoutManager2);
        //TODO:VIEW:content_caracteristicas_ver_producto.xml

        CardviewAdapterCaracteristicasProducto adapterCondiciones = new CardviewAdapterCaracteristicasProducto(getContext(), caracteristicasProducto, new CardviewAdapterCaracteristicasProducto.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, int itemPosition, CaracteristicaProducto caracteristicaProducto) {

            }
        });

        recyclerView_condiciones_cotizacion.setAdapter(adapterCondiciones);
    }

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
