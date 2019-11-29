package com.stap.erpstap_avangra.Clases;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.stap.erpstap_avangra.Activity.CotizacionesListActivity;
import com.stap.erpstap_avangra.Activity.CrearCuentaActivity;
import com.stap.erpstap_avangra.Activity.IniciarSesionActivity;
import com.stap.erpstap_avangra.Activity.MainNavigationActivity;
import com.stap.erpstap_avangra.Fragments.CotizacionesListFragment;
import com.stap.erpstap_avangra.Fragments.VerProductoFragment;
import com.stap.erpstap_avangra.R;

public class DialogBox {
    Dialog dialog;
    ImageView closeImageViewDialog;
    Button btnDialog, btnDialogCancel;
    TextView txtTitulo, txtMensaje;

    public void Create(final Context context, String titulo, String mensaje, final boolean isActivity){

        try{
            dialog = new Dialog(context);

            dialog.setContentView(R.layout.dialogbox_positive);

            closeImageViewDialog = (ImageView) dialog.findViewById(R.id.close_img_dialog);
            btnDialog = (Button) dialog.findViewById(R.id.btn_dialog);
            txtTitulo = (TextView) dialog.findViewById(R.id.txt_titulo_dialog);
            txtMensaje = (TextView) dialog.findViewById(R.id.txt_mensaje_dialog);

            txtTitulo.setText(titulo);
            txtMensaje.setText(mensaje);

            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if(!isActivity){
                        new BottomNavigationController().changeItemPosition(R.id.navigation_cotizaciones);
                    }
                    else {

                        Intent i = new Intent(context, MainNavigationActivity.class);
                        i.putExtra("KEY_COT_CREADA", true);
                        ControllerActivity.activiyAbiertaActual.startActivity(i);
                        ControllerActivity.activiyAbiertaActual.finish();
                    }

                    /*
                    CotizacionesListFragment fragment = new CotizacionesListFragment();
                    final FragmentTransaction ft = ControllerActivity.fragmentAbiertoActual.getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ft.add(R.id.fragment_container, fragment);
                    ft.commit();*/

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public void CreateDialogError(Context context, String titulo, String mensaje){

        try{
            dialog = new Dialog(context);

            dialog.setContentView(R.layout.dialogbox_negative);

            closeImageViewDialog = (ImageView) dialog.findViewById(R.id.close_img_dialog);
            btnDialog = (Button) dialog.findViewById(R.id.btn_dialog_negative);
            txtTitulo = (TextView) dialog.findViewById(R.id.txt_titulo_dialog_negative);
            txtMensaje = (TextView) dialog.findViewById(R.id.txt_mensaje_dialog_negative);

            txtTitulo.setText(titulo);
            txtMensaje.setText(mensaje);

            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public void CreateDialogSuccess(Context context, String titulo, String mensaje){

        try{
            dialog = new Dialog(ControllerActivity.activiyAbiertaActual);

            dialog.setContentView(R.layout.dialogbox_positive);

            closeImageViewDialog = (ImageView) dialog.findViewById(R.id.close_img_dialog);
            btnDialog = (Button) dialog.findViewById(R.id.btn_dialog);
            txtTitulo = (TextView) dialog.findViewById(R.id.txt_titulo_dialog);
            txtMensaje = (TextView) dialog.findViewById(R.id.txt_mensaje_dialog);

            txtTitulo.setText(titulo);
            txtMensaje.setText(mensaje);

            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(ControllerActivity.activiyAbiertaActual, IniciarSesionActivity.class);
                    ControllerActivity.activiyAbiertaActual.startActivity(myIntent);
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public void IniciarSesionDialog(Context context, String titulo, String mensaje){

        try{
            dialog = new Dialog(context);

            dialog.setContentView(R.layout.dialogbox_iniciar_sesion);

            closeImageViewDialog = (ImageView) dialog.findViewById(R.id.close_img_dialog);
            btnDialog = (Button) dialog.findViewById(R.id.btn_dialog_is);
            btnDialogCancel = (Button) dialog.findViewById(R.id.btn_dialog_close_is);
            txtTitulo = (TextView) dialog.findViewById(R.id.txt_titulo_dialog_is);
            txtMensaje = (TextView) dialog.findViewById(R.id.txt_mensaje_dialog_is);

            txtTitulo.setText(titulo);
            txtMensaje.setText(mensaje);

            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(ControllerActivity.activiyAbiertaActual, IniciarSesionActivity.class);
                    ControllerActivity.activiyAbiertaActual.startActivity(myIntent);
                }
            });

            btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public void CreateDialogWarning(Context context, String titulo, String mensaje, final Fragment fragment){

        try{
            dialog = new Dialog(ControllerActivity.fragmentAbiertoActual.getContext());

            dialog.setContentView(R.layout.dialog_warning);

            closeImageViewDialog = (ImageView) dialog.findViewById(R.id.close_img_dialog);
            btnDialog = (Button) dialog.findViewById(R.id.btn_dialog_warning);
            txtTitulo = (TextView) dialog.findViewById(R.id.txt_titulo_dialog_warning);
            txtMensaje = (TextView) dialog.findViewById(R.id.txt_mensaje_dialog_warning);

            txtTitulo.setText(titulo);
            txtMensaje.setText(mensaje);

            btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fragment != null){
                        dialog.dismiss();
                    }
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public void CreateDialogLoading(){

        try{
            dialog = new Dialog(ControllerActivity.activiyAbiertaActual);
            dialog.setContentView(R.layout.dialog_loading);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    public void CancelDialogLoading(){

        try{
            dialog.dismiss();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }
}
