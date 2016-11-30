package com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Satellite on 29/11/2016.
 */

public class NewNotificationsContent {
    //clase para almacenar y manipular la informacion de las notificaciones nuevas

    private JSONArray notifications;

    public NewNotificationsContent(JSONArray notifications) {
        this.notifications = notifications;
    }


    public JSONArray getResults() {

        try{

            JSONArray array = notifications.getJSONArray(0);//obtener un JsonArray

            return array;

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return null;
        }


    }

    public JSONObject getIndex(){
        try{


            JSONObject objectJson = notifications.getJSONObject(1);//obtener un JsonArray

            return objectJson;

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return null;
        }
    }

    public JSONArray getBody(){

        try{

            return notifications;

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return null;
        }

    }

    public int getCount(){

        try{

            JSONArray array = notifications.getJSONArray(0);//obtener un JsonArray

            return array.length();

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return 0;
        }

    }
}
