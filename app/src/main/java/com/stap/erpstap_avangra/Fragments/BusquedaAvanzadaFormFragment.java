package com.stap.erpstap_avangra.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.stap.erpstap_avangra.Activity.BusquedaAvanzadaActivity;
import com.stap.erpstap_avangra.Activity.MainNavigationActivity;
import com.stap.erpstap_avangra.Adapters.BusquedaAvanzadaProductoTipoAdapter;
import com.stap.erpstap_avangra.Clases.Categoria;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.FiltroAvanzado;
import com.stap.erpstap_avangra.Clases.Producto;
import com.stap.erpstap_avangra.Clases.TipoCaracteristicaProductoBusquedaAvanzada;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stap.erpstap_avangra.Activity.BusquedaAvanzadaActivity.viewPagerBusquedaAvanzada;

public class BusquedaAvanzadaFormFragment extends Fragment {

    View view;
    SessionManager sessionController;
    BusquedaAvanzadaProductoTipoAdapter busquedaAvanzadaProductoTipoAdapter;
    public ButtonFlat btnSiguienteBusquedaAvanzada, btnOmitirBusquedaAvanzada;
    RelativeLayout loaderSubmitForm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_busqueda_avanzada_form, container, false);
        ControllerActivity.fragmentAbiertoActual = this;
        sessionController = new SessionManager(getContext());


        TextView title = (TextView) view.findViewById(R.id.item_description);
        CheckBox checkboxForm = (CheckBox)view.findViewById(R.id.checkboxForm);
        EditText editTextRangoForm = (EditText) view.findViewById(R.id.editTextRangoForm);
        EditText editTextNumericoForm = (EditText) view.findViewById(R.id.editTextNumericoForm);
        loaderSubmitForm = (RelativeLayout) view.findViewById(R.id.loaderSubmitForm);

        editTextNumericoForm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextRangoForm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        int position = FragmentPagerItem.getPosition(getArguments());
        int finalPosition = new FiltroAvanzado().getFiltroAvanzadoCount();

        FiltroAvanzado filtroAvanzado = new FiltroAvanzado().filtroAvanzadoList.get(position);


        switch (filtroAvanzado.getTipo()){
            case 0:
                title.setText(filtroAvanzado.getDescripcion());
                editTextNumericoForm.setVisibility(View.VISIBLE);

                editTextRangoForm.setVisibility(View.GONE);
                checkboxForm.setVisibility(View.GONE);
                break;

            case 1:
                title.setText(filtroAvanzado.getDescripcion());
                editTextRangoForm.setVisibility(View.VISIBLE);

                editTextNumericoForm.setVisibility(View.GONE);
                checkboxForm.setVisibility(View.GONE);
                break;

            case 2:
                title.setText(filtroAvanzado.getDescripcion());
                checkboxForm.setVisibility(View.VISIBLE);

                editTextNumericoForm.setVisibility(View.GONE);
                editTextRangoForm.setVisibility(View.GONE);
                break;

            case 3:
                title.setText(filtroAvanzado.getDescripcion());

                checkboxForm.setVisibility(View.GONE);
                editTextNumericoForm.setVisibility(View.GONE);
                editTextRangoForm.setVisibility(View.GONE);

                cargarTiposAlAdapter(filtroAvanzado.getAlternativas(), position);

                break;
        }

        btnSiguienteBusquedaAvanzada = (ButtonFlat) view.findViewById(R.id.btnSiguienteBusquedaAvanzada);
        btnOmitirBusquedaAvanzada = (ButtonFlat) view.findViewById(R.id.btnOmitirBusquedaAvanzada);

        btnSiguienteBusquedaAvanzada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  switch (filtroAvanzado.getTipo()){
                    case 0:

                        if(!editTextNumericoForm.getText().toString().trim().equalsIgnoreCase("")){
                            float numText = Float.parseFloat(editTextNumericoForm.getText().toString());
                            new TipoCaracteristicaProductoBusquedaAvanzada().agregarSeleccion(filtroAvanzado.getId(), position, numText, -1, false, "", filtroAvanzado.getTipo());

                            int pos = position + 1;
                            if(pos == finalPosition){
                                submitForm();
                            }
                            else{
                                viewPagerBusquedaAvanzada.setCurrentItem(pos);
                                setProgresoText(pos, finalPosition);
                            }
                        }
                        else{
                            editTextNumericoForm.setError("Este campo no puede estar en blanco");
                        }

                        break;

                    case 1:

                        if(!editTextRangoForm.getText().toString().trim().equalsIgnoreCase("")){
                            float rangoText = Float.parseFloat(editTextRangoForm.getText().toString());
                            new TipoCaracteristicaProductoBusquedaAvanzada().agregarSeleccion(filtroAvanzado.getId(), position, -1, rangoText, false, "", filtroAvanzado.getTipo());

                            if((position + 1) == finalPosition){
                                submitForm();
                            }
                            else{
                                viewPagerBusquedaAvanzada.setCurrentItem(position+1);
                                setProgresoText(position+1, finalPosition);
                            }
                        }
                        else{
                            editTextRangoForm.setError("Este campo no puede estar en blanco");
                        }

                        break;

                    case 2:

                        boolean checkbox = checkboxForm.isChecked();
                        new TipoCaracteristicaProductoBusquedaAvanzada().agregarSeleccion(filtroAvanzado.getId(), position, -1, -1, checkbox, "", filtroAvanzado.getTipo());

                        if((position + 1) == finalPosition){
                            submitForm();
                        }
                        else{
                            viewPagerBusquedaAvanzada.setCurrentItem(position+1);
                            setProgresoText(position+1, finalPosition);
                        }

                        break;

                    case 3:

                        if((position + 1) == finalPosition){
                            submitForm();
                        }
                        else{
                            viewPagerBusquedaAvanzada.setCurrentItem(position+1);
                            setProgresoText(position+1, finalPosition);
                        }

                        break;
                }

            }
        });
        btnOmitirBusquedaAvanzada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((position + 1) == finalPosition){
                    submitForm();
                }
                else{
                    viewPagerBusquedaAvanzada.setCurrentItem(position+1);
                    setProgresoText(position+1, finalPosition);
                }
            }
        });

        return view;
    }

    public void setProgresoText(int position, int finalPosition){
        BusquedaAvanzadaActivity.txtProgresoBA.setText("Progreso: "+(position+1)+"/"+finalPosition);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BusquedaAvanzadaActivity.actualPositionBA = FragmentPagerItem.getPosition(getArguments());
        int finalPosition = new FiltroAvanzado().getFiltroAvanzadoCount();

        int pos = BusquedaAvanzadaActivity.actualPositionBA;
        int finalPos = finalPosition - 1;

        if(pos == finalPos){
            btnSiguienteBusquedaAvanzada.setText("Realizar Búsqueda");
        }
    }

    public void submitForm(){

        loaderSubmitForm.setVisibility(View.VISIBLE);
        JSONObject datos = new JSONObject();
        JSONArray jsonArrayRespuestas = new JSONArray();

        try {

            for(Map.Entry<Integer, TipoCaracteristicaProductoBusquedaAvanzada> entry : TipoCaracteristicaProductoBusquedaAvanzada.tipoCaractMap.entrySet()) {
                int key = entry.getKey();
                TipoCaracteristicaProductoBusquedaAvanzada value = entry.getValue();
                JSONObject respuesta = new JSONObject();

                respuesta.put("Id", value.getId());
                respuesta.put("RespuestaNum", value.getValueNumerico());
                respuesta.put("RespuestaRango", value.getValueRango());
                respuesta.put("RespuestaBool", value.isValueCheckbox());
                respuesta.put("RespuestaAlternativa", value.getAlternativa());

                jsonArrayRespuestas.put(respuesta);

            }

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
            datos.put("Respuestas",jsonArrayRespuestas);

            //Enviar datos al webservice
            new FiltroAvanzado.SubmitFormulario().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cargarTiposAlAdapter(List<String> tipos, int positionPager){

        RecyclerView recyclerView_busqueda_avanzada = (RecyclerView)view.findViewById(R.id.recyclerView_busqueda_avanzada_tipo);
        recyclerView_busqueda_avanzada.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);

        recyclerView_busqueda_avanzada.setLayoutManager(mGridLayoutManager);

        busquedaAvanzadaProductoTipoAdapter = new BusquedaAvanzadaProductoTipoAdapter(getActivity(), tipos, new BusquedaAvanzadaProductoTipoAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, int itemPosition, String tipoSeleccionado, List<String> listaTipos) {

                FiltroAvanzado filtroAvanzado = new FiltroAvanzado().filtroAvanzadoList.get(positionPager);
                new TipoCaracteristicaProductoBusquedaAvanzada().agregarSeleccion(filtroAvanzado.getId(), positionPager, -1, -1, false, tipoSeleccionado, 3);
            }
        });

        recyclerView_busqueda_avanzada.setAdapter(busquedaAvanzadaProductoTipoAdapter);
        recyclerView_busqueda_avanzada.getRecycledViewPool().setMaxRecycledViews(0,tipos.size());
        recyclerView_busqueda_avanzada.setItemViewCacheSize(tipos.size());
    }

    public void submitFormularioRespuesta(JSONObject respuestaOdata){
        try {

            String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");

            if(tipoRespuesta.equals("OK")){
                try {
                    Producto.listaProductos = respuestaOdata.getJSONArray("Productos");
                    Categoria.listaCategoriasJsonArray = respuestaOdata.getJSONArray("Categorias");

                    Intent i = new Intent(getContext(), MainNavigationActivity.class);
                    i.putExtra("KEY_COT_CREADA", false);
                    i.putExtra("KEY_COT_RECHAZADA", false);
                    i.putExtra("KEY_BUSQUEDA_AVANZADA", true);
                    getActivity().startActivity(i);
                    getActivity().finish();
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
