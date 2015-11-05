package com.co.edu.cun.www1104379214.bienestarcun.Metodos;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLDelete;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 30/10/15.
 */

public class Metodos {

    Context CONTEXTO;
    DBManager DB;
    ServicesPeticion services = new ServicesPeticion();

    TaskExecuteSQLInsert sqliteInsert;
    TaskExecuteSQLSearch sqliteSearch;
    TaskExecuteSQLDelete sqliteDelete;
    CodMessajes mss = new CodMessajes();

    Cursor result_search;

    public Metodos(Context contexto, DBManager db) {

        this.CONTEXTO = contexto;
        this.DB = db;

    }

    public  void ejemplo(EditText user, EditText pass ){

        String[][] values;


        try {

            values = services.ConfirmarUser(CONTEXTO,
                    user.getText().toString(),
                    pass.getText().toString()); //Peticion login

            if ( values != null){ //si el logueo fue correcto

                sqliteInsert = new TaskExecuteSQLInsert(DB.TABLE_NAME_USER,
                        DB.GenerateValues( values ),
                        CONTEXTO,
                        DB
                ); //insertar usuario logueado en sqlite

                sqliteInsert.execute();

                try {
                    Save_log();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Save_log() throws ExecutionException, InterruptedException, JSONException {

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
                DB.CN_TOKEN_LOGIN
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        Cursor result = sqliteSearch.execute().get();

        JSONObject user_log = CreateObjectResultSQL(result, camposSeacrh);//recupero el usuario logueado
        //{"0":{"Usuario":"krlos","id_user":"1104379","Token":"ec4698a66a5096801b66ebeed3eb0064ee6525cb"}}

        Log.i(mss.TAG, user_log.toString());

        JSONObject arrayResult = user_log.getJSONObject( "0");


        String resultLog = services.SaveLog(arrayResult, camposSeacrh);

        Toast.makeText(CONTEXTO, mss.msmServices.getString(resultLog),
                Toast.LENGTH_SHORT).show();

    }
/*
    private void ProcedureNotifications(Cursor result, String[] campos) throws JSONException { //procedimientos despues de buscar notificaciones

        JSONObject SQL_RESULT_SEARCH_NOTIFICATIONS = new JSONObject();

        if (result_search.getCount() > 0) { //verifico si se encontraron registros
            SQL_RESULT_SEARCH_NOTIFICATIONS = CreateObjectResultSQL(result, campos); //creo objeto con los resultados de consulta
            String s = SQL_RESULT_SEARCH_NOTIFICATIONS.getString("0");
            JSONObject ss = new JSONObject(s);
            Toast.makeText(CONTEXTO, ss.getString(DB.CN_TIPE_NOTIFICATION), Toast.LENGTH_SHORT).show();
            //TODO ACCIONES
        }else{
            //TODO acciones si no encuentra notificaciones
        }
        result_search.close();//cierro el cursor

    }

    private void ProcedureUsers( Cursor result, String[] campos ) { //procedimientos despues de buscar usuarios

        JSONObject SQL_RESULT_SEARCH_NOTIFICATIONS = new JSONObject();

        if (result.getCount() > 0) { //verifico si se encontraron registros

            SQL_RESULT_SEARCH_NOTIFICATIONS = CreateObjectResultSQL(result, campos); //creo objeto con los resultados de consulta
            //TODO ACCIONES
        }else{
            //TODO acciones si no encuentra usuarios
        }
        result.close();//cierro el cursor
    }
*/
    private JSONObject CreateObjectResultSQL(Cursor result, String[] campos) throws JSONException { //crear JSONObject con los resultados de sqlite

        JSONObject SQL_RESULT_SEARCH = new JSONObject();
        JSONObject subobject = new JSONObject();

        int numero = result.getCount();

        int contador = 0;

        while ( result.moveToNext() ) {

            try { //creo objeto de resultados consulta

                int size = campos.length;//numero de campos

                for (int c = 0; c < size; c++) {//itero por cada campo enviado para la consulta
                    subobject.put(campos[c],
                            result.getString(result.getColumnIndex(campos[c])));

                }

                SQL_RESULT_SEARCH.put(contador + "", subobject);

                //Log.i(mss.TAG, SQL_RESULT_SEARCH.getString("0"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return SQL_RESULT_SEARCH;

    }

}
