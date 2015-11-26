package com.co.edu.cun.www1104379214.bienestarcun.WebServices;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by krlos guzman on 26/10/15.
 */
public class ChatList {

    String id_chat;

    public ChatList(String objectCircle, JSONObject indexCircles) { //recivo valores e indices de circulos
        setearObjext( objectCircle, indexCircles );
    }

    public String getIdActiviti() {//retorno id del circulo
        return id_chat;
    }

    private void setearObjext( String objectString, JSONObject index ){
    //extraigo los valores del objeto para asignarlo a las variables globales

        try {

            JSONObject object = new JSONObject( objectString );

            this.id_chat = object.getString( index.getString("0") );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
