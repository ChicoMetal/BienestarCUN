package com.co.edu.cun.www1104379214.bienestarcun.WebServices;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by krlos guzman on 26/10/15.
 */
public class ItinerarioList {

    String id_actividad;
    String name_actividad;
    String date;
    String status;
    String details;


    public ItinerarioList(String objectCircle, JSONObject indexCircles) { //recivo valores e indices de circulos
        setearObjext( objectCircle, indexCircles );
    }

    public String getIdItinerario() {//retorno id del circulo
        return id_actividad;
    }

    public String getNameItinerario() { //retorno nombre del circulo
        return name_actividad;
    }

    public String getDateItinerario() {//retorno admin del circulo
        return date;
    }

    public String getStatusItinerario() {//retorno descripcion del circulo
        return status;
    }

    public String getDetailsItinerario() {//retorno descripcion del circulo
        return details;
    }

    private void setearObjext( String objectString, JSONObject index ){
    //extraigo los valores del objeto para asignarlo a las variables globales

        try {

            JSONObject object = new JSONObject( objectString );

            this.id_actividad = object.getString( index.getString("0") );
            this.name_actividad = object.getString( index.getString("1") );
            this.date = object.getString( index.getString("2")  );
            this.status = object.getString( index.getString("3") );
            this.details = object.getString( index.getString("4") );


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
