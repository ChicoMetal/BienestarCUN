package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;

/**
 * Created by root on 4/11/15.
 */
public class TaskExecuteSQLDelete extends AsyncTask<Void, Void, Integer> {


    Constantes mss = new Constantes();

    private String TABLA;
    private Context CONTEXT;
    private DBManager db;

    private String result_insert;

    public TaskExecuteSQLDelete(String tb,//nombre de la tabla
                                Context contesto,//contexto
                                DBManager db
    ) {

        TABLA = tb;
        CONTEXT = contesto;
        this.db = db;

    }

    @Override
    protected void onPreExecute() {

        Toast.makeText(CONTEXT.getApplicationContext(), "Procesando...", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected Integer doInBackground(Void... voids) {

        Integer resultado = null;

        if( db != null )
            resultado = db.DeleteBD();

        return resultado;

    }


    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

    }



}
