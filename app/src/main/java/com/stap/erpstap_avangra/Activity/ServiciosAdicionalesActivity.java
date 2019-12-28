package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment;
import com.stap.erpstap_avangra.R;

import java.util.ArrayList;
import java.util.List;

public class ServiciosAdicionalesActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static AppBarLayout app_bar_ServiciosAdicionales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_adicionales);
        ControllerActivity.activiyAbiertaActual = this;

        app_bar_ServiciosAdicionales = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar_servicios_adicionales);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Carro de Compra");

        /*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String KEY_NOMBRE_PRODUCTO = extras.getString("NombreProd");
            getSupportActionBar().setTitle(KEY_NOMBRE_PRODUCTO);
        }*/

        Drawable drawable = changeDrawableColor(getApplicationContext(),R.drawable.ic_close_black_24dp, Color.WHITE);

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("BackServiciosAA", true).commit();
                finish();
            }
        });

        CarroCompraPasosFragment fragment = new CarroCompraPasosFragment();
        loadFragment(fragment, 0, 1);
    }

    private boolean loadFragment(Fragment fragment, int paso, int pasoUS) {
        //switching fragment
        if (fragment != null) {

            Bundle arguments = new Bundle();
            arguments.putInt("Paso", 0);
            arguments.putInt("PasoUS",1);

            fragment.setArguments(arguments);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_servicios_adicionales, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int backStackCount = fm.getBackStackEntryCount();

        if (backStackCount < 1) {

            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("BackServiciosAA", true).commit();

            finish();
        }
        else{
            fm.popBackStack();
        }
        //moveTaskToBack(true);
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }
}
