package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearchConditions;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;

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

    TaskExecuteHttpHandler BD;
    ServicesPeticion services = new ServicesPeticion(CONTEXTO);
    CodMessajes mss = new CodMessajes();
    TaskExecuteSQLSearchConditions sqliteSearchconditions;
    TaskExecuteSQLSearch sqliteSearch;
    TaskExecuteSQLInsert sqliteInsert;

    JSONArray resultNotifications;
    JSONObject indexNotifications;
    JSONArray newresultNotifications;

    /**
     **********************
     **********************
     **********************
     * 1 = notificaciones de los circulos
     * 0 = notificaciones de los egresados
     **********************
     **********************
     */
    public Notification(Context contexto, DBManager db){
        this.DB = db;
        this.CONTEXTO = contexto;
    }

    private String getIdUser() {//obtengo el id del usuario logueado

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
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getIdUser #!#";
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return null;
    }

    private JSONObject getNotificationsOld(String[] tipeNotifications){//buscar notificaciones guardadas en sqlite (leidas)

        JSONObject jsonNotifications = null;

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

            Cursor result = sqliteSearchconditions.execute().get();

            jsonNotifications = new AdapterUserMenu(CONTEXTO, DB).CreateObjectResultSQL(result, camposSeacrh);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getNotificationsOld #!#";
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return jsonNotifications;
    }

    public JSONArray GetNotifications(String[] tipeNotifications, String service, ProgressDialog pdialog){//obtener notificaciones en la BD remota

        String[][] parametros;

        JSONArray arrayResponse;

        parametros = new String[][]{ //array parametros a enviar
                {"user",getIdUser()}
        };


        try {

            BD = new TaskExecuteHttpHandler(service, parametros, CONTEXTO, pdialog);
            String resultado="";
            try {
                resultado = BD.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();

            }catch (Exception e){
                String contenido = "Error desde android #!#";
                contenido += " Funcion: GetNotifications #!#";
                contenido += "Clase : Notification.java #!#";
                contenido += e.getMessage();
                new ServicesPeticion(CONTEXTO).SaveError(contenido);
            }

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
            contenido += " Funcion: GetNotifications #!#";
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }
        return null;
    }

    private JSONArray ShowNotificationsNew(JSONArray arrayResponse,
                                      String[] tipeNotifications) {

        newresultNotifications = new JSONArray();

        try{

            resultNotifications = arrayResponse.getJSONArray(0);//array de objetos con los resultados
            indexNotifications = arrayResponse.getJSONObject(1);//objeto con los index de la consulta

            JSONObject notificationOlds = getNotificationsOld(tipeNotifications);//notificaciones guardadas en sqlite

            JSONObject notificationNew = null;

            for(int c = resultNotifications.length()-1; c >= 0; c--  ){

                notificationNew = resultNotifications.getJSONObject(c);

                boolean existst = SearchNotificationsGetExists(tipeNotifications,
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
            return array;

        } catch (JSONException e) {
            e.printStackTrace();
            String contenido = "Error desde android #!#";
            contenido += " Funcion: ShowNotificationsNew #!#";
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }

        return null;
    }

    private boolean SearchNotificationsGetExists(String[] tipeNotifications,
                                                 String idNewNotification,
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
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchNotificationsGetExists #!#";
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }

        return resultExists;
    }


    public boolean SaveNotificationRead( String idNotification, String tipeNotification ){

        boolean resultInsert = false;

        String[][] values = new String[][]{
                {DB.CN_TIPE_NOTIFICATION,tipeNotification},
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
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SaveNotificationRead #!#";
            contenido += "Clase : Notification.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }

        return resultInsert;
    }
}
