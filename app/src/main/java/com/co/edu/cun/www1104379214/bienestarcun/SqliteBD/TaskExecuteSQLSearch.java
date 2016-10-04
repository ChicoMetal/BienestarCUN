package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

/**
 * Created by root on 4/11/15.
 */



import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;
import com.co.edu.cun.www1104379214.bienestarcun.Constantes;


public class TaskExecuteSQLSearch extends AsyncTask<Void, Void, Cursor> {

    Cursor result_search;
    Constantes mss = new Constantes();

    private String TABLA;
    private String[] CAMPOS; //Array para la busqueda de registros
    private Context CONTEXT;
    private DBManager db;


    public TaskExecuteSQLSearch(String tb,//nombre de la tabla
                                String[] colunms,//Campos a buscar
                                Context contesto,//contexto
                                DBManager db
    ) {

        TABLA = tb;
        CAMPOS =  colunms;
        CONTEXT = contesto;
        this.db = db;

    }

    @Override
    protected void onPreExecute() {

        Toast.makeText(CONTEXT.getApplicationContext(), "Procesando...", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected Cursor doInBackground(Void... voids) {

        if( db != null)
            result_search = db.SearchDB(TABLA, CAMPOS);

        return result_search;

    }


    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);



    }


}
