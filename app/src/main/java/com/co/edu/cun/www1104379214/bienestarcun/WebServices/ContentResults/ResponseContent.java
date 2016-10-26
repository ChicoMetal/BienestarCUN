package com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONArray;
import org.json.JSONException;
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

            return arrayResponse.getJSONArray(0);

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

            return arrayResponse.getJSONObject(1);

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
