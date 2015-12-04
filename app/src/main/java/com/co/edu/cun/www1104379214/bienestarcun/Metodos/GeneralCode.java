package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 30/11/15.
 */
public class GeneralCode {


    DBManager DB;
    Context CONTEXTO;

    TaskExecuteSQLSearch userSearch;

    CodMessajes mss = new CodMessajes();
    String[][] parametros;
    TaskExecuteHttpHandler BD;

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


    public void getNameUser(TextView ContentNameUser){//obtener el nombre del usuario

        String idUser = getIdUser();

        final String service = "user/getNameUser.php";
        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idUser}
        };


        try {

            BD = new TaskExecuteHttpHandler(service, parametros);
            String resultado="";


            resultado = BD.execute().get();


            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                if( idUser.equals( mss.DftUsrId ) )
                    ContentNameUser.setText(mss.DftUsrName);
                else
                    ContentNameUser.setText("");

            }else{

                JSONArray nameUser =  arrayResponse.getJSONArray(0);
                JSONObject index = arrayResponse.getJSONObject(1);

                ContentNameUser.setText(
                        nameUser.getJSONObject(0).getString( index.getString("0")  )+" "+
                        nameUser.getJSONObject(0).getString( index.getString("1")  )
                );

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){

            String contenido = "Error desde android #!#";
            contenido += " Funcion: getNameUser try 1 #!#";
            contenido += "Clase : GeneralCode.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);

        }


    }
}
