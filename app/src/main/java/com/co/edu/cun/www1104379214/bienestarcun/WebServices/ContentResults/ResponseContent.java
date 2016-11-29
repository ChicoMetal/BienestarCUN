package com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ResponseContent {

    private List result = new ArrayList();


    public void setContent(List content) {
        this.result = content;
    }

    public JSONArray getResults() {

        try{

            JSONArray arrayResponse = new JSONArray( result );

            //<editor-fold desc="Conversion para android < API 19 ">
            Object results = arrayResponse.get(0); //obtiene una lista de los resultados
            Gson gson = new Gson();
            String json = gson.toJson( results ); // obtiene un string formato json
            JSONArray array = new JSONArray( json );//obtener un JsonArray
            //</editor-fold>

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

            JSONArray arrayResponse = new JSONArray( result );

            //<editor-fold desc="Conversion para android < API 19 ">
            Object results = arrayResponse.get(1);//obtener una lista de los indices
            Gson gson = new Gson();
            String json = gson.toJson(results);// obtiene un string formato json
            JSONObject objectJson = new JSONObject( json );//obtener un JsonArray
            //</editor-fold>

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

            JSONArray arrayResponse = new JSONArray( result );

            return arrayResponse;

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return null;
        }

    }

    public int getCount(){

        try{

            JSONArray arrayResponse = new JSONArray( result ).getJSONArray(0);

            return arrayResponse.length();

        }catch ( Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
            return 0;
        }

    }


}
