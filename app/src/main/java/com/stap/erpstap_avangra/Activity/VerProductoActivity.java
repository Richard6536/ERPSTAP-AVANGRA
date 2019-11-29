package com.stap.erpstap_avangra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stap.erpstap_avangra.Clases.Moneda;
import com.stap.erpstap_avangra.Clases.ProductoEnCarro;
import com.stap.erpstap_avangra.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Iterator;

public class VerProductoActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButtonAddToCart;
    CarouselView carouselView;
    TextView txtTituloProducto, txtValor;
    int id, cantidad;
    String nombre, valor;
    boolean inicio = true;

    //int[] sampleImages = {R.drawable.ic_add_shopping_cart, R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery};
    ArrayList<String> imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        txtTituloProducto = (TextView)findViewById(R.id.txtTituloProducto);
        txtValor = (TextView)findViewById(R.id.txtValor);
        floatingActionButtonAddToCart = (FloatingActionButton)findViewById(R.id.floatingActionButtonAddToCart);

        id = getIntent().getIntExtra("Id",-1);
        nombre = getIntent().getStringExtra("Nombre");
        valor = getIntent().getStringExtra("Precio");
        cantidad = getIntent().getIntExtra("Cantidad",0);

        inicio = true;
        imagenes = getIntent().getStringArrayListExtra("Imagenes");

        txtTituloProducto.setText(nombre);
        txtValor.setText("$"+ new Moneda().Format(Integer.parseInt(valor)));

        //Si el producto no tiene imagenes
        if(imagenes.size() == 0){
            imagenes.add("http://www.losprincipios.org/images/default.jpg");
        }

        carouselView.setPageCount(imagenes.size());
        carouselView.setImageListener(imageListener);

        floatingActionButtonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarProductoEnCarro(view);
            }
        });

        verificarProductoEnCarro(null);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void verificarProductoEnCarro(View view){

        ProductoEnCarro productoEnCarro = new ProductoEnCarro();
        productoEnCarro.setId(id);
        productoEnCarro.setNombre(nombre);
        productoEnCarro.setValor(valor);
        productoEnCarro.setCantidad(1);
        productoEnCarro.setImagenes(imagenes);
        if(inicio == true){

            if (checkProduct() == true) {
                floatingActionButtonAddToCart.setImageResource(R.drawable.ic_check_black);
                floatingActionButtonAddToCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3eb510")));
            }
        }
        else{

            if (checkProduct() == false) {
                ProductoEnCarro.productosEnCarro.add(productoEnCarro);

                floatingActionButtonAddToCart.setImageResource(R.drawable.ic_check_black);
                floatingActionButtonAddToCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3eb510")));

                Snackbar.make(floatingActionButtonAddToCart, "Producto agregado al carro", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //Toast.makeText(getApplicationContext(),"Producto agregado al carro", Toast.LENGTH_LONG).show();
            }
            else
            {
                BorrarProductoDelCarro();
                floatingActionButtonAddToCart.setImageResource(R.drawable.ic_add_shopping_cart);
                floatingActionButtonAddToCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#636363")));

                Snackbar.make(floatingActionButtonAddToCart, "Producto removido del carro", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //Toast.makeText(getApplicationContext(),"Producto removido del carro", Toast.LENGTH_LONG).show();
            }
        }

        inicio = false;
    }

    public Boolean checkProduct(){
        boolean existe = false;
        for(ProductoEnCarro p : ProductoEnCarro.productosEnCarro){
            if(p.getId() == id){
                existe = true;
            }
        }
        return existe;
    }

    public void BorrarProductoDelCarro(){
        for (Iterator<ProductoEnCarro> iter = ProductoEnCarro.productosEnCarro.listIterator(); iter.hasNext(); ) {
            ProductoEnCarro prod = iter.next();
            if (prod.getId() == id) {
                iter.remove();
            }
        }
    }

    ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.with(getApplicationContext()).load(imagenes.get(position)).fit().into(imageView);
            }
        };

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
