package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

/**
 * Created by root on 4/11/15.
 */


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;

import java.util.concurrent.locks.Condition;


public class TaskExecuteSQLSearchConditions extends AsyncTask<Void, Void, Cursor> {

    Cursor result_search;
    CodMessajes mss = new CodMessajes();

    private String TABLA;
    private String[] CAMPOS; //Array para la busqueda de registros
    private Context CONTEXT;
    private DBManager db;
    private  String[] Conditions;


    public TaskExecuteSQLSearchConditions(String tb,//nombre de la tabla
                                          String[] colunms,//Campos a buscar
                                          Context contesto,//contexto
                                          DBManager db,
                                          String[] conditions
    ) {

        this.TABLA = tb;
        this.CAMPOS =  colunms;
        this.CONTEXT = contesto;
        this.db = db;
        this.Conditions = conditions;

    }

    @Override
    protected void onPreExecute() {

        Toast.makeText(CONTEXT.getApplicationContext(), "Procesando...", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected Cursor doInBackground(Void... voids) {

        if( db != null )
            result_search = db.SearchDBConditions(TABLA, CAMPOS, Conditions);

        return result_search;

    }


    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);



    }


}
