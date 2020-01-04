package com.stap.erpstap_avangra.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.islamkhsh.CardSliderViewPager;
import com.stap.erpstap_avangra.Activity.BusquedaAvanzadaActivity;
import com.stap.erpstap_avangra.Activity.HelpActivity;
import com.stap.erpstap_avangra.Activity.MainNavigationActivity;
import com.stap.erpstap_avangra.Adapters.AnunciosAdapter;
import com.stap.erpstap_avangra.Clases.Anuncio;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Clases.FiltroAvanzado;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;

public class MenuEmpresaFragment extends Fragment {


    SessionManager sessionController;
    String nombreEmpresa;
    CardView cardView_cot, cardView_cc, cardView_mc, cardView_help, cv_filtro_avanzado;
    ProgressBar progressBarAnuncio;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_empresa, container, false);

        ControllerActivity.fragmentAbiertoActual = this;
        sessionController = new SessionManager(getActivity());
        toolbar.setTitle("AVANGRA");

        //txtNombreEmpresa = (TextView)findViewById(R.id.txtNombreEmpresa);
        cardView_cot = (CardView)view.findViewById(R.id.cv_cot);
        cardView_cc = (CardView)view.findViewById(R.id.cv_cc);
        cardView_mc = (CardView)view.findViewById(R.id.cv_mc);
        cardView_help = (CardView)view.findViewById(R.id.cv_help);
        cv_filtro_avanzado = (CardView)view.findViewById(R.id.cv_filtro_avanzado);
        cv_filtro_avanzado.setVisibility(View.GONE);

        progressBarAnuncio = (ProgressBar) view.findViewById(R.id.progressBarAnuncio);
        progressBarAnuncio.setVisibility(View.VISIBLE);

        cardView_cot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadFragment(new ProductosListFragment());
                new BottomNavigationController().changeItemPosition(R.id.navigation_productos);
            }
        });

        cardView_cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new CarroCompraMainFragment());
                new BottomNavigationController().changeItemPosition(R.id.navigation_carro_compra);
            }
        });

        cardView_mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new CotizacionesListFragment());
                new BottomNavigationController().changeItemPosition(R.id.navigation_cotizaciones);
            }
        });

        cardView_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });

        cv_filtro_avanzado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BusquedaAvanzadaActivity.class);
                startActivity(intent);
            }
        });

        if(sessionController.checkLogin() == true) {
            HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
            nombreEmpresa = datosUsuario.get(SessionManager.KEY_NOMBREEMPRESA);
        }

        llamarObtenerAnuncios();

        if(!FiltroAvanzado.is_busqueda_avanzada){
            obtenerVersionAPP();
        }

        return view;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Bundle arguments = new Bundle();
            arguments.putBoolean("is_busqueda_avanzada", FiltroAvanzado.is_busqueda_avanzada);
            fragment.setArguments(arguments);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void llamarObtenerAnuncios(){
        JSONObject datos = new JSONObject();

        try {

            datos.put("IdEmpresa", 1);

            //Enviar datos al webservice
            new Anuncio.ObtenerAnuncios().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void obtenerVersionAPP(){


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

    public void obtenerVersionAPPRespuesta(JSONObject respuestaOdata){
        try {

            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");

            if(tipoRespuesta.equals("OK")){
                try {

                    int version = respuestaOdata.getInt("VersionAPP");
                    boolean mostrarCotizador = respuestaOdata.getBoolean("MostrarCotizador");

                    mostrarActualizacionApp(version);
                    mostrarOcultarCotizador(mostrarCotizador);

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

    public void respuestaObtenerAnuncios(JSONObject respuestaOdata){
        try {

            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
            progressBarAnuncio.setVisibility(View.GONE);

            if(tipoRespuesta.equals("OK")){
                try {

                    JSONArray listaAnuncios = respuestaOdata.getJSONArray("Anuncios");
                    mostrarAnunciosAdapter(listaAnuncios);

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

    public void mostrarAnunciosAdapter(JSONArray anunciosJsonArray){
        if(anunciosJsonArray.length() > 0) {

            ArrayList<Anuncio> anunciosList = new ArrayList<>();

            for(int x = 0; x <anunciosJsonArray.length(); x++) {

                JSONObject proveedor = null;

                try {

                    proveedor = anunciosJsonArray.getJSONObject(x);

                    int id = proveedor.getInt("Id");
                    String titulo = proveedor.getString("Titulo");
                    String descripcion = proveedor.getString("Descripcion");
                    String imagen = proveedor.getString("Imagen");

                    if (imagen != null) {
                        imagen = "http://stap.cl"+imagen.substring(1);
                    }

                    Anuncio anuncio = new Anuncio();
                    anuncio.setId(id);
                    anuncio.setTitulo(titulo);
                    anuncio.setDescripcion(descripcion);
                    anuncio.setUrlImage(imagen);

                    anunciosList.add(anuncio);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            CardSliderViewPager cardSliderViewPager = (CardSliderViewPager) view.findViewById(R.id.viewPager);
            cardSliderViewPager.setAdapter(new AnunciosAdapter(anunciosList, getContext()));

            cardSliderViewPager.setSmallScaleFactor(0.9f);
            cardSliderViewPager.setSmallAlphaFactor(0.5f);

        }
    }

    public void mostrarOcultarCotizador(boolean mostrarCotizador){
        if(mostrarCotizador){
            cv_filtro_avanzado.setVisibility(View.VISIBLE);
        }
        else{
            cv_filtro_avanzado.setVisibility(View.GONE);
        }
    }

    public void mostrarActualizacionApp(int version){

        String v = getResources().getString(R.string.APP_VERSION_CODE);
        int versionActual = Integer.parseInt(v);

        if(version > versionActual){

            boolean firstrun = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
            if (firstrun){
                AlertDialogCotizacion("Una nueva versión de AVANGRA está disponible.","Actualización");
                getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
            }

        }
    }

    public void AlertDialogCotizacion(String mensaje, String titulo){

        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setMessage(mensaje)
                .setTitle(titulo)
                .setCancelable(false)
                .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.stap.erpstap"));
                        startActivity(browserIntent);

                    }})
                .setNegativeButton("Después", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
