package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.httpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 19/11/15.
 */
public class ItinerariosManager {

    TaskExecuteHttpHandler BD;
    String[][] parametros;
    CodMessajes mss = new CodMessajes();
    Context CONTEXTO;
    JSONObject indexItinerarios;
    JSONArray result = null;
    JSONArray resultResponse = null;


    public ItinerariosManager(Context contexto) {
        this.CONTEXTO = contexto;

    }

    public JSONArray SearchItinerarios( int idCircle ) { //buscar circulo existente en la BD o los agregados por el usuario

        String service = "circles/GetItinerariosCircle.php";


        try {

            result = GetItinerariosExitst(idCircle, service);
            resultResponse = result.getJSONArray(0);
            indexItinerarios = result.getJSONObject(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchItinerarios #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return resultResponse;


    }

    public JSONArray GetItinerariosExitst(int idCircle1, String service) throws InterruptedException {
        //obtengo el array de objeto con los itinerarios

        String[][] values = null;
        String idCircle = idCircle1+"";

        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"circle",idCircle}
        };

        BD = new TaskExecuteHttpHandler(service, parametros);
        String resultado="";
        try {
            resultado = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetItinerariosExitst #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else {

                return arrayResponse;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetItinerariosExitst #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return arrayResponse;

    }

    public JSONObject IndexItinerario() { //retornar los index dell objeto traido de la BD

        return indexItinerarios;
    }
}
