package com.stap.erpstap_avangra.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;

public class ImageviewActivity extends AppCompatActivity {

    int position;
    boolean mostrarDescripcion = false;
    String nombre, descripcion;
    List<String> imagenes;
    CarouselView carouselView;
    TextView txtDescripcionCarroCompraImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        ControllerActivity.activiyAbiertaActual = this;

        //toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4b4b4b")));
        carouselView = (CarouselView) findViewById(R.id.imgViewProd);

        Bundle bundle = getIntent().getExtras();

        position = bundle.getInt("Position", 0);
        descripcion = bundle.getString("Descripcion");
        mostrarDescripcion = bundle.getBoolean("MostrarDescripcion");
        toolbar.setTitle(nombre);
        imagenes = bundle.getStringArrayList("Imagenes");

        txtDescripcionCarroCompraImageView = findViewById(R.id.txtDescripcionCarroCompraImageView);
        txtDescripcionCarroCompraImageView.setText("Descripción:\n" + descripcion);
        ImageView img_close_imageview = findViewById(R.id.img_close_imageview);
        img_close_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        // The View with the BottomSheetBehavior
        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet_imageview);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        if(mostrarDescripcion){
            txtDescripcionCarroCompraImageView.setText("Sin Descripción");
            if(descripcion != null && !descripcion.equals("null")){
                txtDescripcionCarroCompraImageView.setText(descripcion);
            }
        }
        else {
            bottomSheet.setVisibility(View.GONE);
        }


        //Si el producto no tiene imagenes
        if(imagenes.size() == 0){
            imagenes.add("http://www.losprincipios.org/images/default.jpg");
        }


        if(imagenes.size() != 0){
            carouselView.setPageCount(imagenes.size());
            carouselView.setImageListener(imageListener);
            carouselView.setCurrentItem(position);
        }
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(getApplicationContext()).load(imagenes.get(position)).into(imageView);
        }
    };
}
