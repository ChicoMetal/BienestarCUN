package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import java.io.IOException;

/**
 * Created by root on 15/05/16.
 */
public class AddViewsMSM extends AsyncTask<Void,Void,Void>{
    Context CONTEXTO;
    LinearLayout CONTENTCHAT;
    TextView CONTENTMSM;

    public AddViewsMSM(Context contexto, LinearLayout contentchat, TextView contentmsm ) {
        this.CONTEXTO = contexto;
        this.CONTENTCHAT = contentchat;
        this.CONTENTMSM = contentmsm;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(CONTEXTO, "termina........", Toast.LENGTH_SHORT).show();
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Toast.makeText(CONTEXTO, "Ejecutando........", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(CONTEXTO, "Comienza........", Toast.LENGTH_SHORT).show();
        //CONTENTCHAT.addView( CONTENTMSM );
    }





}
