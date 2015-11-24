package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.ArrayRes;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearchConditions;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.httpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 23/11/15.
 */
public class Notification {

    DBManager DB;
    Context CONTEXTO;

    httpHandler server = new httpHandler();
    CodMessajes mss = new CodMessajes();
    TaskExecuteSQLSearchConditions sqliteSearchconditions;
    TaskExecuteSQLSearch sqliteSearch;

    JSONArray resultNotifications;
    JSONObject indexNotifications;
    JSONArray newresultNotifications;

    public Notification(Context contexto, DBManager db){
        this.DB = db;
        this.CONTEXTO = contexto;
    }

    private String getIdUser() {//obtengo el id del usuario logueado

        String idUser="";

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            Cursor result = sqliteSearch.execute().get();

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
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return null;
    }

    private JSONObject getNotificationsOld(String[] tipeNotifications){//buscar notificaciones guardadas en sqlite (leidas)


        String[] camposSeacrh = new String[]{
                DB.CN_COD_NOTIFICACION,
                DB.CN_TIPE_NOTIFICATION
        };

        sqliteSearchconditions = new TaskExecuteSQLSearchConditions(DB.TABLE_NAME_NOTIFICATION,
                camposSeacrh,
                CONTEXTO,
                DB,
                tipeNotifications
        ); //busqueda

        try {

            Cursor result = sqliteSearch.execute().get();

            JSONObject jsonNotifications = new AdapterUserMenu(CONTEXTO, DB).CreateObjectResultSQL(result, camposSeacrh);

            if ( jsonNotifications.length() > 0 ){

                return jsonNotifications;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getNotificationsOld #!#";
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return null;
    }

    public JSONArray GetNotifications(String[] tipeNotifications, String service){//obtener notificaciones en la BD remota

        String[][] values = null;
        String[][] parametros;

        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",getIdUser()}
        };


        try {

            String resultado = server.HttpRequestServer(service, parametros);

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor


            }else {

                return ShowNotificationsNew( arrayResponse, tipeNotifications );

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetCirclesExists #!#";
            contenido += "Clase : CircleManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }
        return null;
    }

    private JSONArray ShowNotificationsNew(JSONArray arrayResponse,
                                      String[] tipeNotifications) throws JSONException {

        JSONObject notificationOlds = getNotificationsOld(tipeNotifications);//notificaciones guardadas en sqlite


        if( notificationOlds != null){

            for(int c = notificationOlds.length() -1; c>=0; c-- ){

                String key = "ROW"+c;

                JSONObject rowNotifications = notificationOlds.getJSONObject(key);

                SerachNotificationsGetExists(
                    arrayResponse,
                    rowNotifications.getString(
                            DB.CN_COD_NOTIFICACION
                    )
                );


            }

        }else{//si no existen notificaciones almacenadas retorno el mismo array de objetos con los resultados

            newresultNotifications = arrayResponse.getJSONArray(0);
            indexNotifications = arrayResponse.getJSONObject(1);

        }

        JSONArray array = new JSONArray(); //nuevo array
        array.put(newresultNotifications);
        array.put(indexNotifications);

        return array;
    }

    private void SerachNotificationsGetExists( JSONArray arrayResponse,
                                               String idNotificationOld) throws JSONException {
        //confirmo si las notificaciones traidas del server ya estan guardadas para retirarlas

        resultNotifications = arrayResponse.getJSONArray(0);//array de objetos con los resultados
        indexNotifications = arrayResponse.getJSONObject(1);//objeto con los index de la consulta

        for(int c = resultNotifications.length(); c >= 0; c--  ){

            JSONObject notificationNew = resultNotifications.getJSONObject(c);

            if( !notificationNew.getString(
                    indexNotifications.getString("1")
                ).equals( idNotificationOld )
            ){
                newresultNotifications.put( notificationNew );
            }

        }


    }

}
