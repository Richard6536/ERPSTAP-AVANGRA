package com.stap.erpstap_avangra.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stap.erpstap_avangra.Activity.HelpActivity;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import java.util.HashMap;

import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;

public class MenuEmpresaFragment extends Fragment {


    SessionManager sessionController;
    String nombreEmpresa;
    CardView cardView_cot, cardView_cc, cardView_mc, cardView_help;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_empresa, container, false);

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
}
