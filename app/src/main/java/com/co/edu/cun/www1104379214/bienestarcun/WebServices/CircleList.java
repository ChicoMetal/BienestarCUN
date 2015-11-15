package com.co.edu.cun.www1104379214.bienestarcun.WebServices;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by krlos guzman on 26/10/15.
 */
public class CircleList {

    String id_actividad;
    String name_actividad;
    String admin_actividad;
    String description_actividad;


    public CircleList( String objectCircle, JSONObject indexCircles ) { //recivo valores e indices de circulos
        setearObjext( objectCircle, indexCircles );
    }

    public String getNameActiviti() { //retorno nombre del circulo
        return name_actividad;
    }

    public String getAdminActiviti() {//retorno admin del circulo
        return admin_actividad;
    }

    public String getDescriptionActiviti() {//retorno descripcion del circulo
        return description_actividad;
    }
    public String getIdActiviti() {//retorno id del circulo
        return id_actividad;
    }

    private void setearObjext( String objectString, JSONObject index ){
    //extraigo los valores del objeto para asignarlo a las variables globales

        try {

            JSONObject object = new JSONObject( objectString );

            this.id_actividad = object.getString( index.getString("0") );
            this.name_actividad = object.getString( index.getString("1") );
            this.description_actividad = object.getString( index.getString("2")  );
            this.admin_actividad = object.getString( index.getString("3") );


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
