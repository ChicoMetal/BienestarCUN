package com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class ResponseContent {

    private List result = new ArrayList();



    //<editor-fold desc="Obtener el array de objetos correspondientes a los registros">
    public JSONArray getResults() {

        try{

            Gson gson = new Gson();

            String json = gson.toJson( result );// obtiene un string formato json
            JSONArray arrayResponse = new JSONArray( json );
            JSONArray array = arrayResponse.getJSONArray(0);//obtener un JsonArray

            return array;

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return null;
        }


    }
    //</editor-fold>

    //<editor-fold desc="Obtener los indices de los datos">
    public JSONObject getIndex(){
        try{

            Gson gson = new Gson();

            String json = gson.toJson( result );// obtiene un string formato json
            JSONArray arrayResponse = new JSONArray(  json );
            JSONObject objectJson = arrayResponse.getJSONObject(1);//obtener un JsonArray

            return objectJson;

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return null;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Obtener el array completo de resultados e indices">
    public JSONArray getBody(){

        try{

            Gson gson = new Gson();

            String json = gson.toJson( result );// obtiene un string formato json
            JSONArray arrayResponse = new JSONArray( json );

            return arrayResponse;

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return null;
        }

    }
    //</editor-fold>

    //<editor-fold desc="Obtener el tamaño del array de los resultados">
    public int getCount(){

        try{

            Gson gson = new Gson();

            String json = gson.toJson( result );// obtiene un string formato json
            JSONArray arrayResponse = new JSONArray(  json ).getJSONArray(0);

            return arrayResponse.length();

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return 0;
        }

    }
    //</editor-fold>


}
