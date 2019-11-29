package com.stap.erpstap_avangra.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stap.erpstap_avangra.Adapters.CardviewAdapterCotizaciones;
import com.stap.erpstap_avangra.Clases.CalendarViewFilter;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.Cotizacion;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.GeneralCotizaciones;
import com.stap.erpstap_avangra.ClasesAbstractas.ListDataCotizaciones;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;
import com.tuyenmonkey.mkloader.MKLoader;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.searchItem;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;

public class CotizacionesListFragment extends Fragment {

    SessionManager sessionController;
    public RecyclerView recyclerView_Cotizaciones;
    public static JSONArray listaCotizaciones;
    public TextView mensajeNoCotizaciones;
    MKLoader loader;
    public static CardviewAdapterCotizaciones adapterCotizacionesList;
    public static ExpandableLayout expandable_layout;
    public static EditText txtFechaToolbarDesde, txtFechaToolbarHasta;
    public static Button btnBuscarEntreFechas;
    public LinearLayout no_cot_registradas_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cotizaciones_list, container, false);
        sessionController = new SessionManager(getActivity());
        ControllerActivity.fragmentAbiertoActual = this;
        toolbar.setTitle("Cotizaciones");

        loader = (MKLoader)view.findViewById(R.id.loaderCotizaciones);
        loader.setVisibility(View.VISIBLE);
        no_cot_registradas_layout = view.findViewById(R.id.no_cot_registradas_layout);
        mensajeNoCotizaciones = view.findViewById(R.id.mensajeNoCotizaciones);
        no_cot_registradas_layout.setVisibility(View.GONE);
        mensajeNoCotizaciones.setVisibility(View.GONE);

        searchItem.setVisible(false);

        recyclerView_Cotizaciones = (RecyclerView)view.findViewById(R.id.recyclerView_Cotizaciones);
        recyclerView_Cotizaciones.setHasFixedSize(true);

        btnBuscarEntreFechas = view.findViewById(R.id.btnBuscarEntreFechas);
        txtFechaToolbarDesde = view.findViewById(R.id.txtFechaToolbar1);
        txtFechaToolbarHasta = view.findViewById(R.id.txtFechaToolbar2);

        txtFechaToolbarDesde.setFocusable(false);
        txtFechaToolbarDesde.setClickable(true);

        txtFechaToolbarHasta.setFocusable(false);
        txtFechaToolbarHasta.setClickable(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_Cotizaciones.setLayoutManager(mGridLayoutManager);

        ViewCompat.setNestedScrollingEnabled(recyclerView_Cotizaciones, false);

        expandable_layout = (ExpandableLayout)view.findViewById(R.id.expandable_layout);

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
            new Cotizacion.ObtenerCotizaciones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtFechaToolbarDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if no view has focus:
                View viewFocus = getActivity().getCurrentFocus();
                if (viewFocus != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
                }

                new CalendarViewFilter().createCalendar(getActivity(), 0);

            }
        });

        txtFechaToolbarHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if no view has focus:
                View viewFocus = getActivity().getCurrentFocus();
                if (viewFocus != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
                }

                new CalendarViewFilter().createCalendar(getActivity(), 1);
            }
        });

        btnBuscarEntreFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fechaInicio = txtFechaToolbarDesde.getText().toString();
                String fechaFinal = txtFechaToolbarHasta.getText().toString();
                adapterCotizacionesList.DateFilter(fechaInicio, fechaFinal);
            }
        });

        return view;
    }


    public void CargarCotizaciones(JSONObject respuesta){
        try {
            String tipoRespuesta = respuesta.getString("TipoRespuesta");
            if(tipoRespuesta.equals("OK")){
                try {
                    listaCotizaciones = respuesta.getJSONArray("Cotizaciones");
                    mostrarCotizaciones();
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

    public void mostrarCotizaciones(){
        if(listaCotizaciones.length() > 0)
        {
            int cotizacionesSize = 0;
            ArrayList<Cotizacion> listaCotizacionesAdapter = new ArrayList<>();
            for(int x = 0; x <listaCotizaciones.length(); x++)
            {

                JSONObject proveedor = null;

                try {

                    proveedor = listaCotizaciones.getJSONObject(x);

                    int id = proveedor.getInt("Id");
                    String codigo = proveedor.getString("Codigo");
                    String fecha = proveedor.getString("Fecha");
                    String fechaSplit = fecha.split("T")[0];

                    cotizacionesSize = cotizacionesSize + 1;

                    Cotizacion cotizacion = new Cotizacion();
                    cotizacion.setId(id);
                    cotizacion.setReferencia("");
                    cotizacion.setDividirPorCategoria(false);
                    cotizacion.setCodigo(codigo);
                    cotizacion.setFecha(fechaSplit);
                    cotizacion.setNotasVendedor("");
                    cotizacion.setMoneda("");
                    cotizacion.setSubTotalNeto(-1);
                    cotizacion.setDescuento(-1);
                    cotizacion.setReferencia("");
                    cotizacion.setTotalNeto(-1);
                    cotizacion.setIva(-1);
                    cotizacion.setTotalAPagar(-1);
                    cotizacion.setUsuarioQueAtendio("");
                    cotizacion.setProductos(null);
                    cotizacion.setCondiciones(null);
                    cotizacion.setPosition_cardview(cotizacionesSize);

                    listaCotizacionesAdapter.add(cotizacion);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            HashMap<String, List<Cotizacion>> groupedHashMap = new GeneralCotizaciones().groupDataIntoHashMap(listaCotizacionesAdapter);
            List<ListDataCotizaciones> listDataCotizaciones = new GeneralCotizaciones().ordenarListaCotizacionesHashMap(groupedHashMap);

            adapterCotizacionesList = new CardviewAdapterCotizaciones(getActivity(), listDataCotizaciones, new CardviewAdapterCotizaciones.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, Cotizacion cotizacionSeleccionada) {

                    toolbar.collapseActionView();

                    VerCotizacionFragment fragment = new VerCotizacionFragment();
                    Bundle arguments = new Bundle();

                    arguments.putInt("Id", cotizacionSeleccionada.getId());

                    fragment.setArguments(arguments);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fragment_container, fragment);
                    ft.commit();

                }
            });

            searchItem.setVisible(true);
            loader.setVisibility(View.GONE);
            recyclerView_Cotizaciones.setAdapter(adapterCotizacionesList);
        }
        else
        {
            if(sessionController.checkLogin() == true){
                no_cot_registradas_layout.setVisibility(View.VISIBLE);
                mensajeNoCotizaciones.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
            else{
                no_cot_registradas_layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                mensajeNoCotizaciones.setVisibility(View.VISIBLE);
                mensajeNoCotizaciones.setText("Necesitas iniciar sesión para ver tus Cotizaciones");
            }

        }
    }

}
