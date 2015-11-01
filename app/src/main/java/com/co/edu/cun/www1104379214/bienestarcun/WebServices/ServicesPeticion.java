package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.WebServices.httpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 31/10/15.
 */
public class ServicesPeticion {


    httpHandler BD = new httpHandler();

    public void GuardarPerson(String name, String lastname, String edad) throws InterruptedException {

        final String Service = "services.php";
        String respuesta;

        String[][] Datos = new String[][]{
                {"nombres", name},
                {"apellidos", lastname},
                {"edad", edad},
                {"modo", BD.modo},
                {"accion",BD.insert}
        };

        respuesta = BD.GuardarBD(Datos, Service);

    }

    public void ListPerson(Context v) throws InterruptedException {

        final String service = "services.php";
        String result;

        String[][] parametros = new String[][]{ //array parametros a enviar
                {"modo",BD.modo},
                {"accion",BD.search}
        };


        result = BD.BuscarBD(service, parametros);


        try {

            JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la respuesta del server

            JSONObject objectIndex = arrayResponse.getJSONObject(1); //obtengo los indices de la consulta


            JSONArray arrayResult = arrayResponse.getJSONArray(0);//obtengo el array de objetos con los registros

            //String[] arrayResult = new String[]{ arrayResponse.getJSONArray(0) }; //obtengo el array con los resultados


            String[] array = new String[arrayResult.length()];

            for( int c=0; c < arrayResult.length(); c++ ){

                JSONObject registro = arrayResult.getJSONObject(c);

                array[c] =
                        objectIndex.getString("1")+": "+registro.getString( objectIndex.getString("1") )+"\r\n"+
                                objectIndex.getString("2")+": "+registro.getString( objectIndex.getString("2") )+"\r\n"+
                                objectIndex.getString("3")+": "+registro.getString( objectIndex.getString("3") ); //obtengo el array con los resultados


            }
            Log.i("ARRAYDATOS", array.toString());
            Toast.makeText(v, array[0].toString(), Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adaptador;
            //adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, array );

            //listPerson.setAdapter(adaptador);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
