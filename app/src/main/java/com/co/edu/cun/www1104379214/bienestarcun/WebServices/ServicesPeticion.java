package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.Users;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.SendError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Krlos guzman on 31/10/15.
 */
public class ServicesPeticion {



    Constantes mss = new Constantes();
    DBManager DB;
    OkHttpClient okHttpClient;
    GeneralCode code;

    public ServicesPeticion() {


        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();//asisgnar tiempo de espera a la peticion

    }

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

        if( code == null )
            code = new GeneralCode(DB, contexto);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_USER )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        Users users = retrofit.create(Users.class);

        Call<ResponseContent> call = users.SaveLog(valuesJSON.getString(DB.CN_ID_USER_BD),
                                                        valuesJSON.getString(DB.CN_TOKEN_LOGIN));

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                if( data != null && code.ValidateStatusResponse( response.code() ) )
                    Log.i( mss.TAG, "error "+ data.getBody().toString() );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                code.ManageFailurePetition(t);
                Log.e( mss.TAG, "error "+ t.toString());

            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="Cerrar la sesion del usuario">
    public void LogoutUser(JSONObject valuesJSON, final Context contexto)
            throws JSONException, InterruptedException {

        if( code == null )
            code = new GeneralCode(DB, contexto);

        JSONObject loginSave = valuesJSON.getJSONObject("ROW0");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_USER )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        Users users = retrofit.create(Users.class);

        Call<ResponseContent> call = users.LogoutUser(loginSave.getString(DB.CN_ID_USER_BD),
                                                        loginSave.getString(DB.CN_TOKEN_LOGIN) );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                if( data != null && code.ValidateStatusResponse( response.code() ) ){
                    try {

                        if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                            Toast.makeText(contexto.getApplicationContext(),
                                    mss.msmServices.getString(data.getBody().getString(1).toString()),
                                    Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                        }else{

                            Log.i( mss.TAG, data.getBody().toString() );
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        new ServicesPeticion().SaveError(e,
                                new Exception().getStackTrace()[0].getMethodName().toString(),
                                this.getClass().getName());//Envio la informacion de la excepcion al server
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                code.ManageFailurePetition(t);
                Log.e( mss.TAG, "error "+ t.toString());

            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="Enviar reporte de error">
    public void SaveError( Exception e, String function, String clase ) {


        String contenido = "Error desde android #!#";
        contenido += " Funcion: "+function+" #!#";
        contenido += "Clase : "+clase+".java #!#";
        contenido += "Dispositivo :( "+getInfoDispositivo()+") #!#";
        contenido += e.getMessage();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        SendError enviarError = retrofit.create(SendError.class);



        Call<ResponseContent> call = enviarError.SendError( contenido );


        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos
                ResponseContent data = response.body();


                if( data != null && response.code() >= 300 ){

                    JSONArray msm = data.getBody();

                    try {

                        if( msm.getString(0).toString().equals("msm") )

                            Log.e( mss.TAG, "Respuesta al guardar debug: "+
                                    mss.msmServices.getString( msm.getString(1).toString() ) );

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                code.ManageFailurePetition(t);
                Log.e( mss.TAG, "Error en la peticion al hacer debug "+ t.toString());

            }
        });



    }
    //</editor-fold>


    //<editor-fold desc="Obtener informacion del dispositivo que ejecuta la aplicacion">
    private String getInfoDispositivo(){

        String info = "";
        info += "BOARD: "+Build.BOARD+" #!#";
        info += "BOOTLOADER: "+Build.BOOTLOADER+" #!#";
        info += "BRAND: "+Build.BRAND+" #!#";
        info += "DEVICE: "+Build.DEVICE+" #!#";
        info += "DISPLAY: "+Build.DISPLAY+" #!#";
        info += "FINGERPRINT: "+Build.FINGERPRINT+" #!#";
        info += "HARDWARE: "+Build.HARDWARE+" #!#";
        info += "HOST: "+Build.HOST+" #!#";
        info += "MODEL: "+Build.MODEL+" #!#";
        info += "PRODUCT: "+Build.PRODUCT+" #!#";
        info += "SERIAL: "+Build.SERIAL+" #!#";

        return info;
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
