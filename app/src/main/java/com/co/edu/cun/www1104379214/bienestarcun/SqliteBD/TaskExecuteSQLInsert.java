package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;

/**
 * Created by root on 4/11/15.
 */
public class TaskExecuteSQLInsert extends AsyncTask<Void, Void, Boolean> {


    Constantes mss = new Constantes();

    private String TABLA;
    private ContentValues VALORES;
    private Context CONTEXT;
    private DBManager db;

    public TaskExecuteSQLInsert(String tb,//nombre de la tabla
                                ContentValues values, //Valores para insercion
                                Context contesto,//contexto
                                DBManager db
    ) {

        TABLA = tb;
        VALORES =  values;
        CONTEXT = contesto;
        this.db = db;

    }

    @Override
    protected void onPreExecute() {

        Toast.makeText(CONTEXT.getApplicationContext(), "Procesando...", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        if( db != null )
            return db.InsertBD(VALORES, TABLA);
        else
            return null;

    }


    @Override
    protected void onPostExecute( Boolean result) {
        super.onPostExecute( result );

    }


}
