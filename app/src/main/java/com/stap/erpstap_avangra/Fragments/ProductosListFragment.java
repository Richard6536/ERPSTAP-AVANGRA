package com.stap.erpstap_avangra.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.stap.erpstap_avangra.Activity.MainNavigationActivity;
import com.stap.erpstap_avangra.Adapters.CardFragmentPagerAdapter;
import com.stap.erpstap_avangra.Adapters.CardviewAdapterProductos;
import com.stap.erpstap_avangra.Adapters.CategoriaRecyclerViewAdapter;
import com.stap.erpstap_avangra.Adapters.ShadowTransformer;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.Categoria;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.MainActivity;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.searchItem;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;

public class ProductosListFragment extends Fragment {

    CardView cardview_categoria_1, cardview_categoria_2, cardview_categoria_3, cardView_categoria_mas;
    TextView txtCategoria_1, txtCategoria_2, txtCategoria_3;
    TextView txtCategoria_1_id, txtCategoria_2_id, txtCategoria_3_id;

    DrawerLayout drawerLayout;
    LinearLayout mainView;
    ListView listViewCategorias;

    RelativeLayout mensajeCategoriaSinProductos;
    public static TextView txtMensajeNoExistenProductosList;
    SessionManager sessionController;
    public int idSeleccionado = 0;
    public static JSONArray listaProductos;

    RadioGroup radioButton;
    CardView img_filter_options;
    CardView layoutTextTittle;
    TextView txtTituloCategoriaProducto;
    String nombre;
    int precio;
    RelativeLayout loader;
    public RecyclerView recyclerView_productos, rvCategoria;
    public static CardviewAdapterProductos adapterProductosList;
    CategoriaRecyclerViewAdapter adapterCategoria;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_productos_list, container, false);

        ControllerActivity.fragmentAbiertoActual = this;
        ControllerActivity.ProductosListFragment = this;

        sessionController = new SessionManager(getActivity());
        toolbar.setTitle("Productos");

        loader = (RelativeLayout)view.findViewById(R.id.loaderProductos);
        loader.setVisibility(View.VISIBLE);

        recyclerView_productos = (RecyclerView)view.findViewById(R.id.recyclerView_productos);
        recyclerView_productos.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_productos.setLayoutManager(mGridLayoutManager);
        listViewCategorias = view.findViewById(R.id.listViewCategorias);

        mensajeCategoriaSinProductos = (RelativeLayout) view.findViewById(R.id.mensajeCategoriaSinProductos);
        mensajeCategoriaSinProductos.setVisibility(View.GONE);

        txtMensajeNoExistenProductosList = view.findViewById(R.id.txtMensajeNoExistenProductos);
        txtMensajeNoExistenProductosList.setVisibility(View.GONE);

        txtTituloCategoriaProducto = (TextView)view.findViewById(R.id.txtTituloCategoriaProducto);
        layoutTextTittle = (CardView) view.findViewById(R.id.layoutTextTittle);
        layoutTextTittle.setVisibility(View.GONE);

        //MainNavigationActivity.searchItem.setVisible(true);

        img_filter_options = view.findViewById(R.id.img_filter_options);

        img_filter_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.filter_product);

                Button btnAceptar_filtro_productos = dialog.findViewById(R.id.btnAceptar_filtro_productos);
                radioButton = dialog.findViewById(R.id.opciones_orden);

                int checkedOrderSort = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("cbOrder",0);
                if(checkedOrderSort != 0){
                    radioButton.check(checkedOrderSort);
                }

                btnAceptar_filtro_productos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (radioButton.getCheckedRadioButtonId() == R.id.radio_alfabetico) {

                            Producto.productosList = new Producto().ordenarProductos("alphabetic_type", Producto.productosList);
                            radioButton.check(R.id.radio_alfabetico);

                            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("cbOrder", R.id.radio_alfabetico).commit();

                            List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(Categoria.categoriaSeleccionada.getId());
                            agregarProductosAlAdapter(productosPorCategoria);
                        }
                        else if (radioButton.getCheckedRadioButtonId() == R.id.radio_mayor_menor) {

                            Producto.productosList = new Producto().ordenarProductos("higher_price_type", Producto.productosList);
                            radioButton.check(R.id.radio_mayor_menor);

                            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("cbOrder", R.id.radio_mayor_menor).commit();

                            List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(Categoria.categoriaSeleccionada.getId());
                            Collections.reverse(productosPorCategoria);

                            agregarProductosAlAdapter(productosPorCategoria);
                        }
                        else if (radioButton.getCheckedRadioButtonId() == R.id.radio_menor_mayor) {

                            Producto.productosList = new Producto().ordenarProductos("lower_price_type", Producto.productosList);
                            radioButton.check(R.id.radio_menor_mayor);

                            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("cbOrder", R.id.radio_menor_mayor).commit();

                            List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(Categoria.categoriaSeleccionada.getId());
                            agregarProductosAlAdapter(productosPorCategoria);
                        }

                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();

            }
        });


        if(Categoria.categoriaSeleccionada == null){
            llamarListaProductos();
        }
        else{

            List<Producto> productoListCategorias = new Categoria().buscarProductosPorCategoria(Categoria.categoriaSeleccionada.getId());
            txtTituloCategoriaProducto.setText(Categoria.categoriaSeleccionada.getNombre());
            layoutTextTittle.setVisibility(View.VISIBLE);
            agregarProductosAlAdapter(productoListCategorias);
            mostrarCategorias();

        }

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mainView=(LinearLayout) view.findViewById(R.id.content_frame);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mainView.setX(slideOffset * -300);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return view;
    }


    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     * @return
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public void llamarListaProductos(){


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

    public void mostrarProductos() {
        if(listaProductos.length() > 0) {

            Producto.productosList = new ArrayList<>();

            for(int x = 0; x <listaProductos.length(); x++) {

                JSONObject proveedor = null;

                try {

                    proveedor = listaProductos.getJSONObject(x);

                    int id = proveedor.getInt("Id");
                    String nombre = proveedor.getString("Nombre");
                    int precio = proveedor.getInt("Valor");
                    int cantidad = proveedor.getInt("Cantidad");
                    String descripcion = proveedor.getString("Descripcion");
                    int categoriaId = proveedor.getInt("CategoriaId");
                    String categoriaNombre = proveedor.getString("CategoriaNombre");
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
                    producto.setCategoriaId(categoriaId);
                    producto.setCategoriaNombre(categoriaNombre);
                    producto.setImagenes(imageList);

                    Producto.productosList.add(producto);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //agregarProductosAlAdapter(Producto.productosList);

        }
        else
        {
            //txtNombreVehiculo.setText("No se ha podido obtener la lista de vehículos");
        }

    }

    public void agregarProductosAlAdapter(List<Producto> productoList){

        int checkedOrderSort = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("cbOrder",0);

        if(checkedOrderSort != 0){
            if(checkedOrderSort == R.id.radio_alfabetico){
                productoList = new Producto().ordenarProductos("alphabetic_type", productoList);

            }
            else if(checkedOrderSort == R.id.radio_mayor_menor){
                productoList = new Producto().ordenarProductos("higher_price_type", productoList);
                Collections.reverse(productoList);

            }
            else if(checkedOrderSort == R.id.radio_menor_mayor){
                productoList = new Producto().ordenarProductos("lower_price_type", productoList);
            }
        }
        else{

            //Comenzar lista de productos en orden alfabético
            productoList = new Producto().ordenarProductos("alphabetic_type", productoList);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("cbOrder", R.id.radio_alfabetico).commit();

        }

        mensajeCategoriaSinProductos.setVisibility(View.GONE);

        if(productoList.size() == 0){
            mensajeCategoriaSinProductos.setVisibility(View.VISIBLE);
        }

        adapterProductosList = new CardviewAdapterProductos(getActivity(), productoList, new CardviewAdapterProductos.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, int itemPosition, Producto productoSeleccionado) {

                toolbar.collapseActionView();

                idSeleccionado = productoSeleccionado.getId();
                nombre = productoSeleccionado.getNombre();
                precio = productoSeleccionado.getValor();
                int cantidad = productoSeleccionado.getCantidad();
                String descripcion = productoSeleccionado.getDescripcion();
                String categoriaNombre = productoSeleccionado.getCategoriaNombre();
                List<String> imagenes = productoSeleccionado.getImagenes();
                ArrayList<String> imgArray = new ArrayList<>(imagenes);

                    /*
                    Intent intent = new Intent(getActivity(), VerProductoActivity.class);
                    intent.putExtra("Id", idSeleccionado);
                    intent.putExtra("Nombre", nombre);
                    intent.putExtra("Precio", precio);
                    intent.putExtra("Cantidad",cantidad);
                    intent.putStringArrayListExtra("Imagenes",imgArray);
                    startActivity(intent);*/

                VerProductoFragment fragment = new VerProductoFragment();
                Bundle arguments = new Bundle();

                arguments.putInt("Id", idSeleccionado);
                arguments.putString("Nombre", nombre);
                arguments.putString("Precio", precio+"");
                arguments.putInt("Cantidad", cantidad);
                arguments.putString("Descripcion", descripcion);
                arguments.putString("CategoriaNombre", categoriaNombre);
                arguments.putStringArrayList("Imagenes", imgArray);

                fragment.setArguments(arguments);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.add(R.id.fragment_container, fragment);
                ft.commit();
            }
        });


        loader.setVisibility(View.GONE);
        recyclerView_productos.setAdapter(adapterProductosList);
    }

    public void obtenerListaProductos(JSONObject respuesta)
    {
        try {

            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                try {

                    layoutTextTittle.setVisibility(View.VISIBLE);
                    listaProductos = respuesta.getJSONArray("Productos");
                    Categoria.listaCategoriasJsonArray = respuesta.getJSONArray("Categorias");
                    //chipGroup.removeAllViews();

                    mostrarProductos();
                    mostrarCategorias();
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

    public void mostrarCategorias() {
        Categoria.categoriasList = new ArrayList<>();

        new Categoria().ordenarCategoriasPrincipales("ESTUFA");
        new Categoria().ordenarCategoriasPrincipales("CALDERA");
        new Categoria().ordenarCategoriasPrincipales("SERVICIO");

        for(int x = 0; x < Categoria.listaCategoriasJsonArray.length(); x++){

            JSONObject proveedor = null;

            try {

                proveedor = Categoria.listaCategoriasJsonArray.getJSONObject(x);

                final int id = proveedor.getInt("Id");
                final String nombre = proveedor.getString("Nombre");

                if(!nombre.equals("ESTUFA") || !nombre.equals("CALDERA") || !nombre.equals("SERVICIO")){

                    final Categoria categoria = new Categoria();
                    categoria.setId(id);
                    categoria.setPosition(x);
                    categoria.setNombre(nombre);

                    Categoria.categoriasList.add(categoria);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Categoria categoriaTodosLosProductos = new Categoria();
        categoriaTodosLosProductos.setId(-1);
        categoriaTodosLosProductos.setPosition(-1);
        categoriaTodosLosProductos.setNombre("PRODUCTOS");

        Categoria.categoriasList.add(categoriaTodosLosProductos);


        cardview_categoria_1 = view.findViewById(R.id.cardview_categoria_1);
        cardview_categoria_2 = view.findViewById(R.id.cardview_categoria_2);
        cardview_categoria_3 = view.findViewById(R.id.cardview_categoria_3);
        cardView_categoria_mas = view.findViewById(R.id.cardview_categoria_mas);

        txtCategoria_1 = view.findViewById(R.id.txtCategoria_1);
        txtCategoria_2 = view.findViewById(R.id.txtCategoria_2);
        txtCategoria_3 = view.findViewById(R.id.txtCategoria_3);

        txtCategoria_1_id = view.findViewById(R.id.txtCategoria_1_id);
        txtCategoria_2_id = view.findViewById(R.id.txtCategoria_2_id);
        txtCategoria_3_id = view.findViewById(R.id.txtCategoria_3_id);

        if(Categoria.categoriasList.size() != 0){
            List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(Categoria.categoriasList.get(0).getId());
            agregarProductosAlAdapter(productosPorCategoria);

            txtTituloCategoriaProducto.setText(Categoria.categoriasList.get(0).getNombre());
            Categoria.categoriaSeleccionada = Categoria.categoriasList.get(0);

            cardview_categoria_1.setCardBackgroundColor(Color.parseColor("#ededed"));
            cardview_categoria_2.setCardBackgroundColor(Color.parseColor("#ffffff"));
            cardview_categoria_3.setCardBackgroundColor(Color.parseColor("#ffffff"));
            cardView_categoria_mas.setCardBackgroundColor(Color.parseColor("#ffffff"));
            listViewCategorias.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(Categoria.categoriasList.size() > 2){

            txtCategoria_1.setText(Categoria.categoriasList.get(0).getNombre());
            txtCategoria_1_id.setText(Categoria.categoriasList.get(0).getId()+"");

            txtCategoria_2.setText(Categoria.categoriasList.get(1).getNombre());
            txtCategoria_2_id.setText(Categoria.categoriasList.get(1).getId()+"");

            txtCategoria_3.setText(Categoria.categoriasList.get(2).getNombre());
            txtCategoria_3_id.setText(Categoria.categoriasList.get(2).getId()+"");

            cardView_categoria_mas.setVisibility(View.GONE);

            if(Categoria.categoriasList.size() >= 4){
                cardView_categoria_mas.setVisibility(View.VISIBLE);
                //CREAR NAVEGADOR

                List<Categoria> categoriasListOrdenada = new ArrayList<>();

                for(Categoria categoria : Categoria.categoriasList){
                    if(Integer.parseInt(txtCategoria_1_id.getText().toString()) != categoria.getId() &&
                    Integer.parseInt(txtCategoria_2_id.getText().toString()) != categoria.getId() &&
                    Integer.parseInt(txtCategoria_3_id.getText().toString()) != categoria.getId()){
                        categoriasListOrdenada.add(categoria);
                    }
                }

                final CategoriaRecyclerViewAdapter mAdapter = new CategoriaRecyclerViewAdapter(getActivity(), android.R.layout.simple_list_item_1, categoriasListOrdenada);
                mAdapter.notifyDataSetChanged();
                listViewCategorias.setAdapter(mAdapter);
                listViewCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Categoria item = mAdapter.getItem(i);
                        Categoria.categoriaSeleccionada = item;

                        if(esCategoriaTodosLosProductos(item.getId())){
                            loader.setVisibility(View.VISIBLE);
                            agregarProductosAlAdapter(Producto.productosList);
                            txtTituloCategoriaProducto.setText(item.getNombre());
                        }
                        else{
                            List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(item.getId());
                            agregarProductosAlAdapter(productosPorCategoria);

                            txtTituloCategoriaProducto.setText(item.getNombre());

                            cardview_categoria_1.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            cardview_categoria_2.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            cardview_categoria_3.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            cardView_categoria_mas.setCardBackgroundColor(Color.parseColor("#ededed"));

                            //listViewCategorias.getChildAt(i).setBackgroundColor(Color.parseColor("#ededed"));

                        }


                        drawerLayout.closeDrawers();
                    }
                });
            }

        }
        else if(Categoria.categoriasList.size() == 2) {
            txtCategoria_1.setText(Categoria.categoriasList.get(0).getNombre());
            txtCategoria_1_id.setText(Categoria.categoriasList.get(0).getId()+"");
            txtCategoria_2.setText(Categoria.categoriasList.get(1).getNombre());
            txtCategoria_2_id.setText(Categoria.categoriasList.get(1).getId()+"");

            cardview_categoria_3.setVisibility(View.GONE);
            cardView_categoria_mas.setVisibility(View.GONE);
        }
        else if(Categoria.categoriasList.size() == 1){
            txtCategoria_1.setText(Categoria.categoriasList.get(0).getNombre());
            txtCategoria_1_id.setText(Categoria.categoriasList.get(0).getId()+"");

            cardview_categoria_2.setVisibility(View.GONE);
            cardview_categoria_3.setVisibility(View.GONE);
            cardView_categoria_mas.setVisibility(View.GONE);

        }


        cardview_categoria_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //actualizarCategoriaActual(txtCategoria_1_id);

                int idCategoria = Integer.parseInt(txtCategoria_1_id.getText().toString());
                Categoria.categoriaSeleccionada = new Categoria().buscarCategoria(idCategoria);
                txtTituloCategoriaProducto.setText(Categoria.categoriaSeleccionada.getNombre());

                if(esCategoriaTodosLosProductos(idCategoria)){
                    loader.setVisibility(View.VISIBLE);
                    agregarProductosAlAdapter(Producto.productosList);
                }
                else
                {
                    List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(idCategoria);
                    agregarProductosAlAdapter(productosPorCategoria);
                }

                cardview_categoria_1.setCardBackgroundColor(Color.parseColor("#ededed"));
                cardview_categoria_2.setCardBackgroundColor(Color.parseColor("#ffffff"));
                cardview_categoria_3.setCardBackgroundColor(Color.parseColor("#ffffff"));
                cardView_categoria_mas.setCardBackgroundColor(Color.parseColor("#ffffff"));
                listViewCategorias.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });

        cardview_categoria_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCategoria = Integer.parseInt(txtCategoria_2_id.getText().toString());
                Categoria.categoriaSeleccionada = new Categoria().buscarCategoria(idCategoria);
                txtTituloCategoriaProducto.setText(Categoria.categoriaSeleccionada.getNombre());

                if(esCategoriaTodosLosProductos(idCategoria)){
                    loader.setVisibility(View.VISIBLE);
                    agregarProductosAlAdapter(Producto.productosList);
                }
                else
                {
                    List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(idCategoria);
                    agregarProductosAlAdapter(productosPorCategoria);
                }

                cardview_categoria_1.setCardBackgroundColor(Color.parseColor("#ffffff"));
                cardview_categoria_2.setCardBackgroundColor(Color.parseColor("#ededed"));
                cardview_categoria_3.setCardBackgroundColor(Color.parseColor("#ffffff"));
                cardView_categoria_mas.setCardBackgroundColor(Color.parseColor("#ffffff"));
                listViewCategorias.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });

        cardview_categoria_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCategoria = Integer.parseInt(txtCategoria_3_id.getText().toString());
                Categoria.categoriaSeleccionada = new Categoria().buscarCategoria(idCategoria);
                txtTituloCategoriaProducto.setText(Categoria.categoriaSeleccionada.getNombre());

                if(esCategoriaTodosLosProductos(idCategoria)){
                    loader.setVisibility(View.VISIBLE);
                    agregarProductosAlAdapter(Producto.productosList);
                }
                else
                {
                    List<Producto> productosPorCategoria = new Categoria().buscarProductosPorCategoria(idCategoria);
                    agregarProductosAlAdapter(productosPorCategoria);
                }

                cardview_categoria_1.setCardBackgroundColor(Color.parseColor("#ffffff"));
                cardview_categoria_2.setCardBackgroundColor(Color.parseColor("#ffffff"));
                cardview_categoria_3.setCardBackgroundColor(Color.parseColor("#ededed"));
                cardView_categoria_mas.setCardBackgroundColor(Color.parseColor("#ffffff"));
                listViewCategorias.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });

        cardView_categoria_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


    }

    public boolean esCategoriaTodosLosProductos(int idCategoria){
        if(idCategoria == -1){
            return true;
        }

        return false;
    }

}
