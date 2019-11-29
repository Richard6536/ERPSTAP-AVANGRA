package com.stap.erpstap_avangra.Activity;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import com.stap.erpstap_avangra.Fragments.EditarPerfilFragment;
import com.stap.erpstap_avangra.Fragments.PasswordEditFragment;
import com.stap.erpstap_avangra.R;

public class PerfilActivity extends AppCompatActivity {

    public int actualFragment = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_perfil_edit:
                    actualFragment = 0;
                    fragment = new EditarPerfilFragment();
                    break;

                case R.id.navigation_password_edit:
                    actualFragment = 1;
                    fragment = new PasswordEditFragment();
                    break;

                case R.id.navigation_about:
                    actualFragment = 2;
                    fragment = new PasswordEditFragment();
                    break;
            }

            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        findViewById(R.id.app_bar).bringToFront();

        BottomNavigationView navView = findViewById(R.id.nav_view_perfil);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState == null) {
            actualFragment = 0;
            loadFragment(new EditarPerfilFragment());
        }
        else{
            actualFragment = savedInstanceState.getInt("currentFragment");
            switch(actualFragment){
                case 0:
                    loadFragment(new EditarPerfilFragment());
                    break;
                case 1:
                    loadFragment(new PasswordEditFragment());
                    break;
            }
        }

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_perfil, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
