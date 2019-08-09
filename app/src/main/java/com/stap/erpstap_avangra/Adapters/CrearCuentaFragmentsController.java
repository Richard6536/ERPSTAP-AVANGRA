package com.stap.erpstap_avangra.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.stap.erpstap_avangra.Fragments.CrearCuenta_DatosUsuario_1Fragment;
import com.stap.erpstap_avangra.Fragments.CrearCuenta_DatosUsuario_2Fragment;

public class CrearCuentaFragmentsController extends FragmentPagerAdapter {

    int mNumOfTabs;

    public CrearCuentaFragmentsController(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CrearCuenta_DatosUsuario_1Fragment crearCuenta_datosUsuario_1Fragment = new CrearCuenta_DatosUsuario_1Fragment();
                return crearCuenta_datosUsuario_1Fragment;
            case 1:
                CrearCuenta_DatosUsuario_2Fragment crearCuenta_datosUsuario_2Fragment = new CrearCuenta_DatosUsuario_2Fragment();
                return crearCuenta_datosUsuario_2Fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
