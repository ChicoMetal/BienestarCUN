package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Show_itinerario_circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 26/10/15.
 */
public class CirclesManager {

    TaskExecuteHttpHandler BD;
    String[][] parametros;
    CodMessajes mss = new CodMessajes();
    Context CONTEXTO;
    TaskExecuteSQLSearch userSearch;
    DBManager DB;
    JSONObject indexCircles;
    JSONArray result = null;
    JSONArray resultResponse = null;
    private int SHOW_ITINERARIO_OFF = 0;

    public CirclesManager(Context contexto, DBManager db) {
        this.CONTEXTO = contexto;
        this.DB = db;

    }

    public JSONArray SearchCircles(int servicePetition, ProgressDialog pdialog) { //buscar circulo existente en la BD o los agregados por el usuario
        final String service;

        if( servicePetition == 0){
            service = "circles/GetCirclesExists.php";
        }else{
            service = "circles/SearchCircleAdd.php";
        }


        try {

            result = GetCirclesExists(getIdUser(), service, pdialog);
            resultResponse = result.getJSONArray(0);
            indexCircles = result.getJSONObject(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return resultResponse;


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
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return null;
    }


    public JSONArray GetCirclesExists(String idUser, String service, ProgressDialog pdialog) throws InterruptedException {
    //obtengo el array de objeto con los circulos



        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idUser}
        };

        BD = new TaskExecuteHttpHandler(service, parametros, pdialog);
        String resultado="";
        try {
            resultado = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

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
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
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

            BD = new TaskExecuteHttpHandler(service, parametros, null);
            String resultado="";
            try {
                resultado = BD.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }catch (Exception e){
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server
            }


            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
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

            BD = new TaskExecuteHttpHandler(service, parametros, null);
            String resultado="";
            try {
                resultado = BD.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();

            }catch (Exception e){
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server
            }


            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }

    public void ShowItinerariosCircle(int idCircle, FragmentManager fragmentManager) {

        Bundle args = new Bundle();
        args.putString("", "");

        Fragment fragment;
        fragment =  Show_itinerario_circle.newInstance(idCircle,DB,SHOW_ITINERARIO_OFF,  null);//tercer parametro nulo para q no muestre las asistencias de los itinerarios

        fragment.setArguments(args);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }
}
