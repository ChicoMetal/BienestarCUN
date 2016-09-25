package com.co.edu.cun.www1104379214.bienestarcun;


import android.app.ProgressDialog;
import android.content.Context;

public class Splash {

    ProgressDialog pDialog;

    public ProgressDialog getpDialog(Context contexto) {

        pDialog = new ProgressDialog( contexto );
        pDialog.setMessage("Un momento...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgress(0);


        return pDialog;
    }





}
