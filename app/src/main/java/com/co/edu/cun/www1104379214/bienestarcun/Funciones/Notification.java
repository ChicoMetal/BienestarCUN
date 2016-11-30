package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.NewNotificationsContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 23/11/15.
 */
public class Notification {

    DBManager DB;
    Context CONTEXTO;
    ServicesPeticion services;

    TaskExecuteSQLSearch sqliteSearch;
    TaskExecuteSQLInsert sqliteInsert;

    JSONArray resultNotifications;
    JSONObject indexNotifications;
    JSONArray newresultNotifications;


    public Notification(Context contexto, DBManager db){
        this.DB = db;
        this.CONTEXTO = contexto;
        services = new ServicesPeticion();
    }

    public String getIdUser() {//obtengo el id del usuario logueado

        String idUser="";

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD
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
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return null;
    }

    //<editor-fold desc="buscar notificaciones guardadas en sqlite (leidas)">
    private JSONObject getNotificationsOld(){

        JSONObject jsonNotifications = null;

        String[] camposSeacrh = new String[]{
                DB.CN_COD_NOTIFICACION
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_NOTIFICATION,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            Cursor result = sqliteSearch.execute().get();

            jsonNotifications = new AdapterUserMenu(CONTEXTO, DB)
                                    .CreateObjectResultSQL(result, camposSeacrh);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return jsonNotifications;
    }
    //</editor-fold>


    public NewNotificationsContent ShowNotificationsNew(JSONArray arrayResponse) {

        NewNotificationsContent response;

        newresultNotifications = new JSONArray();

        try{

            resultNotifications = arrayResponse.getJSONArray(0);//array de objetos con los resultados
            indexNotifications = arrayResponse.getJSONObject(1);//objeto con los index de la consulta

            JSONObject notificationOlds = getNotificationsOld();//notificaciones guardadas en sqlite

            JSONObject notificationNew = null;

            for(int c = resultNotifications.length()-1; c >= 0; c--  ){

                notificationNew = resultNotifications.getJSONObject(c);

                boolean existst = SearchNotificationsGetExists(
                                                        notificationNew.getString(
                                                                indexNotifications.getString("0")
                                                        ),
                                                        notificationOlds); //verifico el id de la notificacion traida con las guardadas

                if( !existst ){

                    newresultNotifications.put( notificationNew );
                }

            }

            JSONArray array = new JSONArray(); //nuevo array
            array.put(newresultNotifications);
            array.put(indexNotifications);

            response = new NewNotificationsContent( array );

            return response;

        } catch (JSONException e) {
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return null;
    }

    private boolean SearchNotificationsGetExists(String idNewNotification,
                                                 JSONObject notificationOlds){
        //confirmo si las notificaciones traidas del server ya estan guardadas para retirarlas

        boolean resultExists = false;
        try{

            if( notificationOlds != null){

                for(int c = notificationOlds.length() -1; c>=0; c-- ){

                    String key = "ROW"+c;

                    JSONObject rowNotifications = notificationOlds.getJSONObject(key);

                    if( rowNotifications.getString(
                            DB.CN_COD_NOTIFICACION ).equals(idNewNotification)
                    ){
                        resultExists = true;
                        break;
                    }


                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return resultExists;
    }


    public boolean SaveNotificationRead( String idNotification ){

        boolean resultInsert = false;

        String[][] values = new String[][]{
                {DB.CN_COD_NOTIFICACION,idNotification}
        };

        ContentValues UserValues = DB.GenerateValues( values );

        sqliteInsert = new TaskExecuteSQLInsert(DB.TABLE_NAME_NOTIFICATION,
                UserValues,
                CONTEXTO,
                DB
        ); //insertar una notificacon leida

        try {

            resultInsert  = sqliteInsert.execute().get();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return resultInsert;
    }
}
