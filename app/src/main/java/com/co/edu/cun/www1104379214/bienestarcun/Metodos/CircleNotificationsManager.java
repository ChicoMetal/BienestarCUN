package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.httpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by root on 23/11/15.
 */
public class CircleNotificationsManager {


    httpHandler BD = new httpHandler();
    String[][] parametros;
    CodMessajes mss = new CodMessajes();
    Context CONTEXTO;
    TaskExecuteSQLSearch userSearch;
    DBManager DB;
    JSONObject indexCircles;
    JSONArray result = null;
    private JSONArray resultResponse = null;

    public CircleNotificationsManager(Context contexto, DBManager db) {
        this.CONTEXTO = contexto;
        this.DB = db;

        SearchNotifications();
    }

    public void SearchNotifications() { //buscar notificaciones existentes en la BD o los agregados por el usuario

        final String service;

        service = "notifications/getNotifications.php";

        String[] tipeNotification = new String[]{"1"};

        Notification getNotification = new Notification(CONTEXTO, DB);

        try {

            result = getNotification.GetNotifications(tipeNotification,service);

            if ( result != null){

                resultResponse = result.getJSONArray(0);
                indexCircles = result.getJSONObject(1);

            }

        } catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchNotifications #!#";
            contenido += "Clase : CircleNotificationsManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

    }

    public JSONObject IndexNotifications() { //retornar los index dell objeto traido de la BD

        return indexCircles;
    }

    public JSONArray getResultResponse() {
        return resultResponse;
    }
}
