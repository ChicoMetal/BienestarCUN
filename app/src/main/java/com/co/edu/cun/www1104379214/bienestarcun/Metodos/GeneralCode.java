package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.database.Cursor;

import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 30/11/15.
 */
public class GeneralCode {


    DBManager DB;
    Context CONTEXTO;

    TaskExecuteSQLSearch userSearch;

    public GeneralCode(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;

    }

    public String getIdUser() {//obtengo el id del usuario logueado

        String idUser="";

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
        };

        userSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            Cursor result = userSearch.execute().get();

            JSONObject jsonUser = new AdapterUserMenu(CONTEXTO, DB).CreateObjectResultSQL(result, camposSeacrh);

            if ( jsonUser.length() > 0 ){

                idUser = (String) jsonUser.getJSONObject( "ROW0").get(DB.CN_ID_USER_BD);

                return idUser;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getIdUser #!#";
            contenido += "Clase : GeneralCode.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return null;
    }


}
