package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.httpHandler;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Show_itinerario_circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 26/10/15.
 */
public class CirclesManager {

    httpHandler BD = new httpHandler();
    String[][] parametros;
    CodMessajes mss = new CodMessajes();
    Context CONTEXTO;
    TaskExecuteSQLSearch userSearch;
    DBManager DB;
    JSONObject indexCircles;
    JSONArray result = null;
    JSONArray resultResponse = null;


    public CirclesManager(Context contexto, DBManager db) {
        this.CONTEXTO = contexto;
        this.DB = db;

    }

    public JSONArray SearchCircles( int servicePetition) { //buscar circulo existente en la BD o los agregados por el usuario
        final String service;

        if( servicePetition == 0){
            service = "circles/GetChatPendientesExists.php";
        }else{
            service = "circles/SearchCircleAdd.php";
        }


        try {

            result = GetCirclesExists(getIdUser(), service);
            resultResponse = result.getJSONArray(0);
            indexCircles = result.getJSONObject(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchChatPendientes #!#";
            contenido += "Clase : CircleList.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return resultResponse;


    }



    private String getIdUser() {//obtengo el id del usuario logueado

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
            contenido += "Clase : CircleList.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return null;
    }


    public JSONArray GetCirclesExists(String idUser, String service) throws InterruptedException {
    //obtengo el array de objeto con los circulos

        String[][] values = null;


        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idUser}
        };

        String resultado = BD.HttpRequestServer(service, parametros);

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                return null;

            }else {

                return arrayResponse;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetChatPendientesExists #!#";
            contenido += "Clase : CircleManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return arrayResponse;

    }

    public JSONObject IndexCircles() { //retornar los index dell objeto traido de la BD

        return indexCircles;
    }

    public void SaveCircleUser( int idCircle1) {//agrego el usuario al circulo

        String idCircle = idCircle1+"";
        final String service = "circles/saveAddCircle.php";
        JSONArray arrayResponse = null;

        String idUser = getIdUser();

        parametros = new String[][]{ //array parametros a enviar
                {"user",idUser},
                {"circle", idCircle}
        };



        try {

            String resultado = BD.HttpRequestServer(service, parametros);

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SaveCircleUser #!#";
            contenido += "Clase : CircleManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

    }

    public void DeleteCircleUser( int idCircle) {//elimino el usuario del circulo

        final String service = "circles/DeleteCircleUser.php";
        JSONArray arrayResponse = null;

        String idUser = getIdUser();

        parametros = new String[][]{ //array parametros a enviar
                {"user",idUser},
                {"circle",idCircle+""}
        };



        try {

            String resultado = BD.HttpRequestServer(service, parametros);

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SaveCircleUser #!#";
            contenido += "Clase : CircleManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

    }

    public void ShowItinerariosCircle(int idCircle, FragmentManager fragmentManager) {

        Bundle args = new Bundle();
        args.putString("", "");

        Fragment fragment;
        fragment =  Show_itinerario_circle.newInstance(idCircle, "");

        fragment.setArguments(args);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }
}
