package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.app.ProgressDialog;
import android.content.Context;

import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by root on 23/11/15.
 */
public class CircleNotificationsManager {



    Context CONTEXTO;
    DBManager DB;
    String[] tipeNotification;
    JSONObject indexCircles;
    JSONArray result = null;
    private JSONArray resultResponse = null;

    public CircleNotificationsManager(Context contexto, DBManager db, String[] tipNotification, ProgressDialog pdialog) {
        this.CONTEXTO = contexto;
        this.DB = db;
        this.tipeNotification = tipNotification;
        SearchNotifications(tipeNotification, pdialog);
    }

    public void SearchNotifications(String[] tipeNotification, ProgressDialog pdialog) { //buscar notificaciones existentes en la BD o los agregados por el usuario

        final String service;

        service = "notifications/getNotifications.php";

        Notification getNotification = new Notification(CONTEXTO, DB);

        try {

            result = getNotification.GetNotifications(tipeNotification,service, pdialog);

            if ( result != null){

                resultResponse = result.getJSONArray(0);
                indexCircles = result.getJSONObject(1);

            }

        } catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }

    public JSONObject IndexNotifications() { //retornar los index dell objeto traido de la BD

        return indexCircles;
    }

    public JSONArray getResultResponse() {
        return resultResponse;
    }
}
