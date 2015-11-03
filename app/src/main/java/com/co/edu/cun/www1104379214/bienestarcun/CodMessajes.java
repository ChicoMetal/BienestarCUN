package com.co.edu.cun.www1104379214.bienestarcun;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 31/10/15.
 */
public class CodMessajes {

    public static final String TAG = "CONTROL";
    //****************************************
    //****************************************Services
    //****************************************

    public static final JSONObject msmServices = new JSONObject();

    static{
        try {

            msmServices.put("0000", "error al conectar");
            msmServices.put("0001", "error al ejecutar el query");
            msmServices.put("0010", "No existen datos");
            msmServices.put("1000", "Query ejecutado correctamente");
            msmServices.put("0100", "error al seleccionar la tabla");
            msmServices.put("0011", "Error en la instruccion");
            msmServices.put("1100", "Instruccion ejecutada correctamente");
            msmServices.put("1101", "Peticion indeterminada");

            //mensajes de login
            msmServices.put("11111", "Usuario activo");
            msmServices.put("10000", "El usuario no existe");
            msmServices.put("10001", "Usuario bloqueado");
            msmServices.put("10010", "Combinacion de datos erronea");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //*****************************************
    //*****************************************
    //*****************************************



    public static final String ErrorServicesPeticion = "Error al realizar la peticion al server";
    public static final String LocalInsertError = "Error al insertar los datos";
    public static final String LocalInsert = "Se han insertado los datos";
    public static final String LoginWell = " Felicidades, has ingresado ";

}
