package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.CirclesApp;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.LogUser;
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



    CodMessajes mss = new CodMessajes();
    DBManager DB;


    //<editor-fold desc="Obtiene los datos del usuario que se loguea">
    public String[][] GenerateUserLoginValue(JSONArray arrayResponse,
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
                    arrayResponse.getString(2)//obtengo el token generado en php y agregado al array principal
                }
        };


        return values;

    }
    //</editor-fold>

    //<editor-fold desc="Guarda el login ralizado">
    public void SaveLog(JSONObject valuesJSON, final Context contexto)
            throws JSONException, InterruptedException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.Server+"user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LogUser logUser = retrofit.create(LogUser.class);

        Call<ResponseContent> call = logUser.SaveLog(valuesJSON.getString(DB.CN_ID_USER_BD),
                                                        valuesJSON.getString(DB.CN_TOKEN_LOGIN));

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                Log.i( mss.TAG1, "error "+ data.getBody().toString() );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "error "+ t.toString());

            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="Cerrar la sesion del usuario">
    public void LogoutUser(JSONObject valuesJSON, final Context contexto)
            throws JSONException, InterruptedException {

        JSONObject loginSave = valuesJSON.getJSONObject("ROW0");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.Server+"user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LogUser logUser = retrofit.create(LogUser.class);

        Call<ResponseContent> call = logUser.LogoutUser(loginSave.getString(DB.CN_ID_USER_BD),
                                                        loginSave.getString(DB.CN_TOKEN_LOGIN) );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                try {

                    if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                        Toast.makeText(contexto.getApplicationContext(),
                                mss.msmServices.getString(data.getBody().getString(1).toString()),
                                Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                    }else{

                        Log.i( mss.TAG1, data.getBody().toString() );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
                }

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "error "+ t.toString());

            }
        });


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
