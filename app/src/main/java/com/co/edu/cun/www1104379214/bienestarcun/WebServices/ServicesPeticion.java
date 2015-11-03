package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.content.ContentValues;
import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.httpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Krlos guzman on 31/10/15.
 */
public class ServicesPeticion {

    String[][] parametros;
    String result;
    httpHandler BD = new httpHandler();
    CodMessajes mss = new CodMessajes();


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

        parametros = new String[][]{ //array parametros a enviar
                {"modo",BD.modo},
                {"accion",BD.search}
        };


        result = BD.BuscarBD(service, parametros);


        try {

            JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(v.getApplicationContext(),
                        mss.msmServices.getString( arrayResponse.getString(1).toString() ) ,
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor


            }else {


                JSONObject objectIndex = arrayResponse.getJSONObject(1); //obtengo los indices de la consulta


                JSONArray arrayResult = arrayResponse.getJSONArray(0);//obtengo el array de objetos con los registros

                //String[] arrayResult = new String[]{ arrayResponse.getJSONArray(0) }; //obtengo el array con los resultados


                String[] array = new String[arrayResult.length()];

                for (int c = 0; c < arrayResult.length(); c++) {

                    JSONObject registro = arrayResult.getJSONObject(c);

                    array[c] =
                            objectIndex.getString("1") + ": " +
                                    registro.getString(objectIndex.getString("1")) + "\r\n" +
                            objectIndex.getString("2") + ": " +
                                    registro.getString(objectIndex.getString("2")) + "\r\n" +
                            objectIndex.getString("3") + ": " +
                                    registro.getString(objectIndex.getString("3")); //obtengo el array con los resultados


                }

                Toast.makeText(v, array[0].toString(), Toast.LENGTH_SHORT).show();
                ArrayAdapter<String> adaptador;
                //adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, array );

                //listPerson.setAdapter(adaptador);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String[][] ConfirmarUser(Context context, String user, String pass) throws InterruptedException {

        String[][] values = null;

        final String service = "user/login.php";

        parametros = new String[][]{ //array parametros a enviar
                {"user",user},
                {"password",pass}
        };


        result = BD.HttpRequestServer(service, parametros);


        try {

            JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(context.getApplicationContext(),
                        mss.msmServices.getString( arrayResponse.getString(1).toString() ) ,
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                return values;

            }else {


                JSONObject objectIndex = arrayResponse.getJSONObject(1); //obtengo los indices de la consulta


                JSONArray arrayResult = arrayResponse.getJSONArray(0);//obtengo el array de objetos con los registros

                //String[] arrayResult = new String[]{ arrayResponse.getJSONArray(0) }; //obtengo el array con los resultados


                //String[] array = new String[arrayResult.length()];

                values = GenerateUserLoginValue(arrayResponse,
                        objectIndex,
                        arrayResult); //genero los valores de la insercion

                return values;
/*
                for (int c = 0; c < arrayResult.length(); c++) {

                    JSONObject registro = arrayResult.getJSONObject(c);

                    array[c] =
                            objectIndex.getString("1") + ": " +
                                    registro.getString(objectIndex.getString("1")) + "\r\n" +
                                    objectIndex.getString("2") + ": " +
                                    registro.getString(objectIndex.getString("2")) + "\r\n" +
                                    objectIndex.getString("3") + ": " +
                                    registro.getString(objectIndex.getString("3")); //obtengo el array con los resultados


                }

                Toast.makeText(context, array[0].toString(), Toast.LENGTH_SHORT).show();

*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    private String[][] GenerateUserLoginValue(JSONArray arrayResponse,
                                                 JSONObject objectIndex,
                                                 JSONArray arrayResult) throws JSONException {
                                                 //crear matrix para insercion de usuario en sqlite


        String[][] values = new String[][]{
                {
                    DBManager.CN_ID_USER_BD,
                    arrayResult.getJSONObject(0).getString(
                            objectIndex.getString("0")
                    )
                },                {
                DBManager.CN_USER,
                    arrayResult.getJSONObject(0).getString(
                            objectIndex.getString("1")
                    )
                },
                {
                    DBManager.CN_PASSWORD,
                    arrayResult.getJSONObject(0).getString(
                            objectIndex.getString("2")
                    )
                },
                {
                    DBManager.CN_TIPE_USER,
                    arrayResult.getJSONObject(0).getString(
                            objectIndex.getString("3")
                    )
                },
                {
                    DBManager.CN_TOKEN_LOGIN,
                    arrayResponse.getString(2)
                }
        };


        return values;

    }

    public Boolean SaveLog(String[][] values) throws InterruptedException, JSONException {

        final String service = "user/log_save.php";

        String result = BD.HttpRequestServer(service, values);

        JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la result del server

        if( arrayResponse.getString(1).toString().equals("1000")  ){//compruebo si se guardo correctamente el log

            return true;

        }else{

            return false;

        }

    }
}
