package com.stap.erpstap_avangra.Clases;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stap.erpstap_avangra.Activity.CotizacionesListActivity;
import com.stap.erpstap_avangra.Activity.CrearCuentaActivity;
import com.stap.erpstap_avangra.Activity.IniciarSesionActivity;
import com.stap.erpstap_avangra.R;

public class DialogBox {
    Dialog dialog;
    ImageView closeImageViewDialog;
    Button btnDialog;
    TextView txtTitulo, txtMensaje;

    public void Create(Context context, String titulo, String mensaje){

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
                    dialog.dismiss();
                    Intent intent = new Intent(ControllerActivity.activiyAbiertaActual, CotizacionesListActivity.class);
                    ControllerActivity.activiyAbiertaActual.startActivity(intent);
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
            dialog = new Dialog(ControllerActivity.activiyAbiertaActual);

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
