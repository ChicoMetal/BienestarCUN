package com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults;

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
            return null;
        }


    }

    public JSONObject getIndex(){
        try{

            JSONArray arrayResponse = new JSONArray( result );

            return arrayResponse.getJSONObject(1);

        }catch ( Exception e){
            return null;
        }
    }

    public JSONArray getBody(){

        try{

            JSONArray arrayResponse = new JSONArray( result );

            return arrayResponse;

        }catch ( Exception e){
            return null;
        }

    }

    public int getCount(){

        try{

            JSONArray arrayResponse = new JSONArray( result ).getJSONArray(0);

            return arrayResponse.length();

        }catch ( Exception e){
            return 0;
        }

    }


}
