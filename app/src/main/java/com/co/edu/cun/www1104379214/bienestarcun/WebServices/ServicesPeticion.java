package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.SendError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Krlos guzman on 31/10/15.
 */
public class ServicesPeticion {


    String result;
    TaskExecuteHttpHandler BD;
    CodMessajes mss = new CodMessajes();
    DBManager DB;


    //<editor-fold desc="Verificar el usuario en la BD remota">
    public String[][] ConfirmarUser(Context context, String user, String pass)
            throws InterruptedException {

        String[][] values = null;

        final String service = "user/login.php";

        String[][] parametros = new String[][]{ //array parametros a enviar
                {"user",user},
                {"password",pass}
        };


        BD = new TaskExecuteHttpHandler(service, parametros, null);

        try {
            result = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());
        }


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
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());
        }

        return values;
    }
    //</editor-fold>

    //<editor-fold desc="Obtiene los datos del usuario que se loguea">
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
    //</editor-fold>

    //<editor-fold desc="Guarda el login ralizado">
    public String SaveLog( JSONObject valuesJSON, String[] campos)
            throws JSONException, InterruptedException {

        final String service = "user/log_save.php";

        String[][] values = JSONObjectToMatrix(valuesJSON, campos); //obtener matrix desde objeto

        BD = new TaskExecuteHttpHandler(service, values, null);

        try {
           result = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());
        }

        JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la result del server

        return arrayResponse.getString(1).toString();


    }
    //</editor-fold>

    //<editor-fold desc="Generar objeto a partir de una matrix">
    public String[][] JSONObjectToMatrix(JSONObject arrayResult, String[] campos)
            throws JSONException {

        String[][] Matrix = new String[campos.length][2];

        for (int c = 0; c < campos.length; c++) {//pasar JSONArray a matrix


            Matrix[c][0] = campos[c].toString(); //campo
            Matrix[c][1] = arrayResult.getString( campos[c] );//valor

        }

        return Matrix;


    }
    //</editor-fold>

    //<editor-fold desc="Cerrar la sesion del usuario">
    public String LogoutUser( JSONObject valuesJSON)
            throws JSONException, InterruptedException {

        final String service = "user/logout.php";

        JSONObject loginSave = valuesJSON.getJSONObject("ROW0");

        String[][] values = new String[][]{
                {"user",loginSave.getString(DB.CN_ID_USER_BD)},
                {"token",loginSave.getString(DB.CN_TOKEN_LOGIN)}
        };

        BD = new TaskExecuteHttpHandler(service, values,  null);

        try {
            result = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }catch (Exception e){

            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());
        }

        JSONArray arrayResponse = new JSONArray( result ); // obtengo el array con la result del server

        return arrayResponse.getString(1).toString();


    }
    //</editor-fold>

    //<editor-fold desc="Enviar reporte de error">
    public void SaveError( Exception e, String function, String clase ) {


        String contenido = "Error desde android #!#";
        contenido += " Funcion: "+function+" #!#";
        contenido += "Clase : "+clase+".java #!#";
        contenido += e.getMessage();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.Server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SendError enviarError = retrofit.create(SendError.class);



        Call<ResponseContent> call = enviarError.SendError( contenido );


        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos
                ResponseContent data = response.body();

                JSONArray msm = data.getBody();

                try {

                    if( msm.getString(0).toString().equals("msm") )

                        Log.e( mss.TAG1, "Respuesta al guardar debug: "+
                                mss.msmServices.getString( msm.getString(1).toString() ) );

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "Error en la peticion al hacer debug "+ t.toString());

            }
        });



    }
    //</editor-fold>



    /* causar excepciones
        try {
            int n = Integer.parseInt("M");
        }catch ( Exception e){

            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());
        }
     */
}
