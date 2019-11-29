package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.stap.erpstap_avangra.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class HelpActivity extends AppCompatActivity  implements View.OnClickListener {

    private ExpandableLayout expandableLayout0;
    private ExpandableLayout expandableLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ayuda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        expandableLayout0 = findViewById(R.id.expandable_layout_0);
        expandableLayout1 = findViewById(R.id.expandable_layout_1);

        expandableLayout0.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout0", "State: " + state);
            }
        });

        expandableLayout1.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout1", "State: " + state);
            }
        });

        findViewById(R.id.expand_button_0).setOnClickListener(this);
        findViewById(R.id.expand_button_1).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.expand_button_0) {
            if(expandableLayout0.isExpanded()){
                expandableLayout0.collapse();
            }
            else{
                expandableLayout0.expand();
            }

            expandableLayout1.collapse();

        } else {

            if(expandableLayout1.isExpanded()){
                expandableLayout1.collapse();
            }
            else{
                expandableLayout1.expand();
            }
            expandableLayout0.collapse();
        }
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
