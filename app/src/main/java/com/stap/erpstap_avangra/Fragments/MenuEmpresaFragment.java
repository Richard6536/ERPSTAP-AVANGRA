package com.stap.erpstap_avangra.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.islamkhsh.CardSliderViewPager;
import com.stap.erpstap_avangra.Activity.HelpActivity;
import com.stap.erpstap_avangra.Adapters.AnunciosAdapter;
import com.stap.erpstap_avangra.Clases.Anuncio;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.Empresa;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;

public class MenuEmpresaFragment extends Fragment {


    SessionManager sessionController;
    String nombreEmpresa;
    CardView cardView_cot, cardView_cc, cardView_mc, cardView_help;
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

        if(sessionController.checkLogin() == true) {
            HashMap<String, String> datosUsuario = sessionController.obtenerDetallesUsuario();
            nombreEmpresa = datosUsuario.get(SessionManager.KEY_NOMBREEMPRESA);
        }

        llamarObtenerAnuncios();

        return view;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
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

    public void respuestaObtenerAnuncios(JSONObject respuestaOdata){
        try {

            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
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
}
