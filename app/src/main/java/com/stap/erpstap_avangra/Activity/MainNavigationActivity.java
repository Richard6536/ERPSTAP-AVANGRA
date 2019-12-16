package com.stap.erpstap_avangra.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.stap.erpstap_avangra.Clases.BottomNavigationController;
import com.stap.erpstap_avangra.Clases.Categoria;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.Clases.DialogBox;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraMainFragment;
import com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment;
import com.stap.erpstap_avangra.Fragments.CotizacionesListFragment;
import com.stap.erpstap_avangra.Fragments.MenuEmpresaFragment;
import com.stap.erpstap_avangra.Fragments.ProductosListFragment;
import com.stap.erpstap_avangra.R;
import com.stap.erpstap_avangra.Session.SessionManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.stap.erpstap_avangra.Clases.Condiciones.condicionSeleccionada;
import static com.stap.erpstap_avangra.Fragments.CarroCompra.CarroCompraPasosFragment.pasoUS;
import static com.stap.erpstap_avangra.Fragments.CotizacionesListFragment.adapterCotizacionesList;
import static com.stap.erpstap_avangra.Fragments.CotizacionesListFragment.expandable_layout;
import static com.stap.erpstap_avangra.Fragments.ProductosListFragment.adapterProductosList;
import static com.stap.erpstap_avangra.Fragments.ProductosListFragment.drawerLayout;

public class MainNavigationActivity extends AppCompatActivity {

    public static MenuItem searchItem, userItem;
    public static SearchView searchView;
    public static Toolbar toolbar;
    public static int actualFragment;
    public Button btnAcceder;

    SessionManager sessionController;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    actualFragment = 0;

                    toolbar.collapseActionView();
                    searchItem.setVisible(false);
                    hideElementLoguotUser();
                    fragment = new MenuEmpresaFragment();
                    break;

                case R.id.navigation_cotizaciones:
                    actualFragment = 1;

                    toolbar.collapseActionView();
                    searchItem.setVisible(true);
                    hideElementLoguotUser();
                    fragment = new CotizacionesListFragment();
                    break;

                case R.id.navigation_carro_compra:
                    actualFragment = 2;

                    toolbar.collapseActionView();
                    searchItem.setVisible(false);
                    userItem.setVisible(false);
                    fragment = new CarroCompraMainFragment();
                    break;

                case R.id.navigation_productos:
                    actualFragment = 3;

                    toolbar.collapseActionView();

                    searchItem.setVisible(true);
                    hideElementLoguotUser();
                    Categoria.categoriaSeleccionada = null;

                    fragment = new ProductosListFragment();
                    break;
            }

            if (!verificarVsitaCarroCompra()) {
                return loadFragment(fragment);
            }
            else{

                //Compuebo si vengo desde un fragment de "Pasos"
                new ProductoEnCarro().limpiarListaProductosEnCarro();

                return loadFragment(fragment);
                //DialogBox
                //CreateDialogWarning(getApplicationContext(),"","¿Desea cancelar la Cotización?", fragment, item.getItemId());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        ControllerActivity.mainNavigationActivityController = this;
        //ControllerActivity.activiyAbiertaActual = this;

        sessionController = new SessionManager(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AVANGRA");
        findViewById(R.id.app_bar).bringToFront();

        btnAcceder = findViewById(R.id.btnAcceder);
        btnAcceder.setVisibility(View.GONE);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        new BottomNavigationController().setNavigationBottomView(navView);

        if(getSupportActionBar()!=null){
            Drawable drawable= getResources().getDrawable(R.drawable.avanga_icon);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 30, 30, true));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(newdrawable);

        }

        if(savedInstanceState == null) {
            actualFragment = 0;
            loadFragment(new MenuEmpresaFragment());
            //new BottomNavigationController().changeItemPosition(R.id.navigation_productos);
        }
        else{
            actualFragment = savedInstanceState.getInt("currentFragment");
            switch(actualFragment){
                case 0:
                    loadFragment(new MenuEmpresaFragment());
                    break;
                case 1:
                    loadFragment(new CotizacionesListFragment());
                    break;
                case 2:
                    loadFragment(new CarroCompraMainFragment());
                    break;
                case 3:
                    loadFragment(new ProductosListFragment());
                    break;
            }
        }

        if(sessionController.checkLogin() == false){
            try{
                btnAcceder.setVisibility(View.VISIBLE);
                btnAcceder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), IniciarSesionActivity.class);
                        startActivity(intent);
                    }
                });
            }
            catch (Exception e){

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean KEY_COT_CREADA = extras.getBoolean("KEY_COT_CREADA");
            boolean KEY_COT_RECHAZADA = extras.getBoolean("KEY_COT_RECHAZADA");

            if(KEY_COT_CREADA){
                getIntent().removeExtra("KEY_COT_CREADA");
                getIntent().removeExtra("KEY_COT_RECHAZADA");

                new BottomNavigationController().changeItemPosition(R.id.navigation_cotizaciones);
            }
            else if(KEY_COT_RECHAZADA){
                getIntent().removeExtra("KEY_COT_CREADA");
                getIntent().removeExtra("KEY_COT_RECHAZADA");

                new BottomNavigationController().changeItemPosition(R.id.navigation_carro_compra);
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        userItem = menu.findItem(R.id.action_user);

        hideElementLoguotUser();

        searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.search_view_background));
                ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.black));
                ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text)).setTextColor(getResources().getColor(R.color.black));

                if(ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName().equals("ProductosListFragment")){
                    adapterProductosList.getFilter().filter(newText);
                }
                else if(ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName().equals("CotizacionesListFragment")){
                    adapterCotizacionesList.getFilter().filter(newText);
                }

                return false;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                if(ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName().equals("CotizacionesListFragment")){
                    expandable_layout.setExpanded(true,true);
                }

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if(ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName().equals("CotizacionesListFragment")){
                    expandable_layout.collapse(true);
                    adapterCotizacionesList.resetList();
                }

                return true;
            }
        });

        if(ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName().equals("MenuEmpresaFragment")){
            searchItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_user) {

            if(sessionController.checkLogin() == true){
                if (verificarVsitaCarroCompra()) {
                    //Compuebo si vengo desde un fragment de "Pasos"
                    new ProductoEnCarro().limpiarListaProductosEnCarro();
                }

                Intent intent = new Intent(MainNavigationActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int backStackCount = fm.getBackStackEntryCount();

        String fragmentActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
        if(fragmentActual.equals("ProductosListFragment")) {
            if(ProductosListFragment.drawerLayoutIsOpen){
                ProductosListFragment.drawerLayout.closeDrawers();
            }
            else{
                controlCerrarFragment(backStackCount, fm, fragmentActual);
            }
        }
        else{
            controlCerrarFragment(backStackCount, fm, fragmentActual);
        }

    }

    public void controlCerrarFragment(int backStackCount, FragmentManager fm, String fragmentActual){
        if (backStackCount < 1) {
            moveTaskToBack(true);
        }
        else {

            fm.popBackStack();

            if(fragmentActual.equals("VerProductoFragment")) {

                searchItem.setVisible(true);
                getSupportActionBar().setTitle("Productos");

                if(ControllerActivity.ProductosListFragment != null){
                    ControllerActivity.fragmentAbiertoActual = ControllerActivity.ProductosListFragment;
                }

            }
        }
        String activityActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
        if(activityActual.equals("CarroCompraPasosFragment")) {
            if(condicionSeleccionada != null){
                pasoUS = pasoUS - 1;
                int condsize = condicionSeleccionada.getPasosCondicions().size() + 1;
                toolbar.setTitle("Carro de Compra - Paso "+ pasoUS +"/"+ condsize);
            }


        }
    }

    public boolean verificarVsitaCarroCompra(){
        String activityActual = ControllerActivity.fragmentAbiertoActual.getClass().getSimpleName();
        if(activityActual.equals("CarroCompraPasosFragment")) {
            return true;
        }

        return false;
    }

    public void hideElementLoguotUser(){
        if(sessionController.checkLogin() == false){
            userItem.setVisible(false);
            btnAcceder.setVisibility(View.VISIBLE);
        }
        else{
            userItem.setVisible(true);
            btnAcceder.setVisibility(View.GONE);
        }
    }
}
