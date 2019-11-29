package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stap.erpstap_avangra.R;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DescriptionActivity extends AppCompatActivity {

    TextView txtDescripcionProductoCompleto;
    String nombreProducto, descripcionProducto;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        toolbar = findViewById(R.id.toolbar_description);
        setSupportActionBar(toolbar);


        Drawable drawable = changeDrawableColor(getApplicationContext(),R.drawable.ic_close_black_24dp, Color.WHITE);

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtDescripcionProductoCompleto = findViewById(R.id.txtDescripcionProductoCompleto);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            nombreProducto = extras.getString("KEY_NOMBRE");
            descripcionProducto = extras.getString("KEY_DESCRIPTION");
        }

        getSupportActionBar().setTitle(nombreProducto);
        txtDescripcionProductoCompleto.setText(descripcionProducto);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtDescripcionProductoCompleto.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }
}
