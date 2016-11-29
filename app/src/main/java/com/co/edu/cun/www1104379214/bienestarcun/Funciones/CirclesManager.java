package com.co.edu.cun.www1104379214.bienestarcun.Funciones;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.CirclesApp;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Show_itinerario_circle;
import org.json.JSONException;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CirclesManager {

    Constantes mss = new Constantes();
    Context CONTEXTO;
    TaskExecuteSQLSearch userSearch;
    DBManager DB;
    private int SHOW_ITINERARIO_OFF = 0;
    OkHttpClient okHttpClient;
    GeneralCode code;

    public CirclesManager(Context contexto, DBManager db) {
        this.CONTEXTO = contexto;
        this.DB = db;

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();//asisgnar tiempo de espera a la peticion

        code = new GeneralCode(DB, CONTEXTO );

    }

    //<editor-fold desc="agrego el usuario al circulo">
    public void SaveCircleUser( int idCircle) {


        String idUser = code.getIdUser();
        String token = code.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_CIRCLES )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        CirclesApp actividades = retrofit.create(CirclesApp.class);

        Call<ResponseContent> call = actividades.SaveActivityUser(idUser, token, idCircle);

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                if( data != null && code.ValidateStatusResponse( response.code() ) )
                    ValidateResponse( data );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                code.ManageFailurePetition(t);
                Log.e( mss.TAG, "error "+ t.toString());

            }
        });

    }
    //</editor-fold>

    //<editor-fold desc="elimino el usuario del circulo">
    public void DeleteCircleUser( int idCircle) {


        String idUser = code.getIdUser();
        String token = code.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_CIRCLES )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        CirclesApp actividades = retrofit.create(CirclesApp.class);

        Call<ResponseContent> call = actividades.DeleteActivityUser(idUser, token, idCircle);

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                if( data != null && code.ValidateStatusResponse( response.code() ) )
                    ValidateResponse( data );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                code.ManageFailurePetition(t);
                Log.e( mss.TAG, "error "+ t.toString());

            }
        });

    }
    //</editor-fold>

    //<editor-fold desc="Metodo invocado al tocar una card de circulo/actividad, para mostrar los itinerarios correspondientes">
    public void ShowItinerariosCircle(int idCircle, FragmentManager fragmentManager) {

        Bundle args = new Bundle();
        args.putString("", "");

        Fragment fragment;
        fragment =  Show_itinerario_circle.newInstance(idCircle,DB,SHOW_ITINERARIO_OFF,  null);//tercer parametro nulo para q no muestre las asistencias de los itinerarios

        fragment.setArguments(args);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }
    //</editor-fold>

    //<editor-fold desc="procesa la respuesta enviada del server">
    private void ValidateResponse(ResponseContent data) {

        try {

            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                Toast.makeText(CONTEXTO.getApplicationContext(),
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
    //</editor-fold>
}
