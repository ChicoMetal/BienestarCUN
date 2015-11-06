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

            String contenido = "Error desde android #!#";
            contenido += " Funcion: ListPerson #!#";
            contenido += "Clase : ServicesPeticion.java #!#";
            contenido += e.getMessage();
            SaveError(contenido);

        }

    }

    //<editor-fold desc="Verificar el usuario en la BD remota">
    public String[][] ConfirmarUser(Context context, String user, String pass)
            throws InterruptedException {

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

                return null;

            }else {


                JSONObject objectIndex = arrayResponse.getJSONObject(1); //obtengo los indices de la consulta


                JSONArray arrayResult = arrayResponse.getJSONArray(0);//obtengo el array de objetos con los registros

                values = GenerateUserLoginValue(arrayResponse,
                        objectIndex,
                        arrayResult); //genero los valores de la insercion

                return values;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: ConfirmarUser #!#";
            contenido += "Clase : ServicesPeticion.java #!#";
            contenido += e.getMessage();
            SaveError(contenido);
        }

        return values;
    }
    //</editor-fold>

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

    public String SaveLog( JSONObject valuesJSON, String[] campos)
            throws JSONException, InterruptedException {

        final String service = "user/log_save.php";

        String[][] values = JSONObjectToMatrix(valuesJSON, campos); //obtener matrix desde objeto

        String result = BD.HttpRequestServer(service, values);

        JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la result del server

        return arrayResponse.getString(1).toString();


    }

    public String[][] JSONObjectToMatrix(JSONObject arrayResult, String[] campos)
            throws JSONException {

        String[][] Matrix = new String[campos.length][2];

        Log.i(mss.TAG, arrayResult.getString( campos[0] ));

        for (int c = 0; c < campos.length; c++) {//pasar JSONArray a matrix


            Matrix[c][0] = campos[c].toString(); //campo
            Matrix[c][1] = arrayResult.getString( campos[c] );//valor

        }

        return Matrix;


    }

    public String LogoutUser( JSONObject valuesJSON)
            throws JSONException, InterruptedException {

        final String service = "user/logout.php";

        String[][] values = new String[][]{
                {"json",valuesJSON.toString()}
        };

        String result = BD.HttpRequestServer(service, values);

        JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la result del server

        return arrayResponse.getString(1).toString();


    }

    //<editor-fold desc="Enviar reporte de error">
    public void SaveError( String contenido ) {

        final String service = "saveFatalError.php";

        String[][] values = new String[][]{
                {"contenido",contenido}
        };

        try {
            BD.HttpRequestServer(service, values);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    //</editor-fold>
}
