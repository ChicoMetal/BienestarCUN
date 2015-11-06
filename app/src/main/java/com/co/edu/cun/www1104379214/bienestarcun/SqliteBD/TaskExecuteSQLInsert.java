package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 4/11/15.
 */
public class TaskExecuteSQLInsert extends AsyncTask<Void, Void, Boolean> {


    CodMessajes mss = new CodMessajes();

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

        return db.InsertBD(VALORES, TABLA);

    }


    @Override
    protected void onPostExecute( Boolean result) {
        super.onPostExecute( result );

    }


}
