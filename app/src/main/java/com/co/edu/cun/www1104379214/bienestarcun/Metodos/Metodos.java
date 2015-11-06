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

    Cursor result;

    //<editor-fold desc="Constructor">
    public Metodos(Context contexto, DBManager db) {

        this.CONTEXTO = contexto;
        this.DB = db;

    }
    //</editor-fold>

    //<editor-fold desc="Login">
    public  void ProcessLogin(EditText user, EditText pass){

        String[][] values;


        try {

            values = services.ConfirmarUser(CONTEXTO,
                    user.getText().toString(),
                    pass.getText().toString()); //Peticion login

            if ( values != null  ){ //si el logueo fue correcto

                DeleteUser();

                sqliteInsert = new TaskExecuteSQLInsert(DB.TABLE_NAME_USER,
                        DB.GenerateValues( values ),
                        CONTEXTO,
                        DB
                ); //insertar usuario logueado en sqlite

                if ( sqliteInsert.execute().get() ){//verifico el guardado del usuario

                    try {

                       if ( Save_log() ) { //envio el registro de login

                           Toast.makeText(CONTEXTO,
                                   mss.LoginWell,
                                   Toast.LENGTH_SHORT).show();

                           //TODO Modificar el menu del usuario

                       }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        String contenido = "Error desde android #!#";
                        contenido += " Funcion: ProcessLogin try 2 #!#";
                        contenido += "Clase : Metodos.java #!#";
                        contenido += e.getMessage();
                        services.SaveError(contenido);

                    }

                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: ProcessLogin try 1 #!#";
            contenido += "Clase : Metodos.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Save log">
    public Boolean Save_log() throws ExecutionException, InterruptedException, JSONException {
                            //Guardar el registro de un procesos de login en mysql

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
                DB.CN_TOKEN_LOGIN
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        result = sqliteSearch.execute().get();

        JSONObject user_log = CreateObjectResultSQL(result, camposSeacrh);//recupero el usuario logueado
        //{"ROW0":{"Usuario":"krlos","id_user":"1104379","Token":"ec4698a66a5096801b66ebeed3eb0064ee6525cb"}}

        if ( user_log.length() > 0 ){

            JSONObject arrayResult = user_log.getJSONObject( "ROW0");

            String resultLog = services.SaveLog(arrayResult, camposSeacrh);

            return true;

        }

        return false;

    }
    //</editor-fold>

    //<editor-fold desc="Logout">
    public void ProcessLogout(){

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
                DB.CN_TOKEN_LOGIN
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            result = sqliteSearch.execute().get();

            JSONObject result_object = CreateObjectResultSQL(result, camposSeacrh);

            if( result_object.length() > 0 ){

                String result_service = services.LogoutUser( result_object );
                //TODO verificar que en mysql se registre el logout y cambiar menu
                DeleteUser();

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString( result_service ),
                        Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();


            String contenido = "Error desde android #!#";
            contenido += " Funcion: ProcessLogout #!#";
            contenido += "Clase : Metodos.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);

        }

    }
    //</editor-fold>

    //<editor-fold desc="Crear objeto con resultados consulta sqlite">
    private JSONObject CreateObjectResultSQL(Cursor result, String[] campos) throws JSONException { //crear JSONObject con los resultados de sqlite

        JSONObject SQL_RESULT_SEARCH = new JSONObject();
        JSONObject subobject = new JSONObject();

        int numero = result.getCount();
        String nameRow = "ROW";
        int contador = 0;

        while ( result.moveToNext() ) {

            try { //creo objeto de resultados consulta

                int size = campos.length;//numero de campos

                for (int c = 0; c < size; c++) {//itero por cada campo enviado para la consulta

                    subobject.put(campos[c],
                            result.getString(
                                    result.getColumnIndex(campos[c])
                            )
                    );

                }

                SQL_RESULT_SEARCH.put(nameRow+contador + "", subobject);

            } catch (JSONException e) {
                e.printStackTrace();


                String contenido = "Error desde android #!#";
                contenido += " Funcion: CreateObjectResultSQL #!#";
                contenido += "Clase : Metodos.java #!#";
                contenido += e.getMessage();
                services.SaveError(contenido);

            }

            contador++;
        }

        return SQL_RESULT_SEARCH;

    }
    //</editor-fold>

    //<editor-fold desc="Eliminar usuario sqlite">
    private void DeleteUser(){


        sqliteDelete = new TaskExecuteSQLDelete(DB.TABLE_NAME_USER,
                CONTEXTO,
                DB
        ); //Eliminacion posibles usuarios logueados


        try {

            sqliteDelete.execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: DeleteUser #!#";
            contenido += "Clase : Metodos.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }

    }
    //</editor-fold>
}
