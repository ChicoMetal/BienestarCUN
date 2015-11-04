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
public class TaskExecuteSQLDelete extends AsyncTask<Void, Void, Integer> {


    CodMessajes mss = new CodMessajes();

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

        resultado = db.DeleteBD();

        return resultado;

    }


    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

    }



}
