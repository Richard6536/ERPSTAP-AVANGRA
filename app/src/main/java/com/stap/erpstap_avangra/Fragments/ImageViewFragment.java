package com.stap.erpstap_avangra.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.stap.erpstap_avangra.Clases.ControllerActivity;
import com.stap.erpstap_avangra.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static com.stap.erpstap_avangra.Activity.MainNavigationActivity.toolbar;
import static com.stap.erpstap_avangra.Activity.ServiciosAdicionalesActivity.app_bar_ServiciosAdicionales;

public class ImageViewFragment extends Fragment {

    int position;
    boolean mostrarDescripcion = false;
    String nombre, descripcion;
    List<String> imagenes;
    CarouselView carouselView;
    TextView txtDescripcionCarroCompraImageView, txtTituloImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_view, container, false);
        ControllerActivity.fragmentAbiertoActual = this;

        //toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4b4b4b")));
        carouselView = (CarouselView) view.findViewById(R.id.imgViewProd);

        Bundle bundle = getArguments();
        position = bundle.getInt("Position", 0);
        nombre = bundle.getString("Nombre");
        descripcion = bundle.getString("Descripcion");
        mostrarDescripcion = bundle.getBoolean("MostrarDescripcion");
        toolbar.setTitle("Carro de Compra");
        imagenes = bundle.getStringArrayList("Imagenes");

        txtTituloImageView = view.findViewById(R.id.txtTituloImageView);
        txtTituloImageView.setText(nombre);
        txtDescripcionCarroCompraImageView = view.findViewById(R.id.txtDescripcionCarroCompraImageView);
        txtDescripcionCarroCompraImageView.setText("Descripción:\n" + descripcion);
        ImageView img_close_imageview = view.findViewById(R.id.img_close_imageview);
        img_close_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        // The View with the BottomSheetBehavior
        LinearLayout bottomSheet = view.findViewById(R.id.bottom_sheet_imageview);
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

        return view;
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(getActivity()).load(imagenes.get(position)).into(imageView);
        }
    };

    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }
}
