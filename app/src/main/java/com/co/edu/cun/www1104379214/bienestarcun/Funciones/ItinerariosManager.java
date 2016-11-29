package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.AdminCircles;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.AsistenciaCircleActivities;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.EvidenciasActivities;
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
 * Created by root on 19/11/15.
 */
public class ItinerariosManager {

    Constantes mss = new Constantes();
    Context CONTEXTO;
    DBManager DB;
    OkHttpClient okHttpClient;
    GeneralCode code;
    ServicesPeticion services;

    public ItinerariosManager(DBManager db, Context contexto) {
        this.CONTEXTO = contexto;
        this.DB = db;
        services = new ServicesPeticion();

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();//asisgnar tiempo de espera a la peticion

        code = new GeneralCode(DB, CONTEXTO );
    }


    //<editor-fold desc="Mostrar el listado de inscritos al circulo/actividad, para tomar asistencia">
    public void ShowAsistenciaItinerarios(int idItinerario, int idCircle, int INSTANCE,
                                                    FragmentManager fragmentManager) {

        Bundle args = new Bundle();
        args.putString("", "");

        Fragment fragment = null;

        if( INSTANCE == 1)
            fragment =  AsistenciaCircleActivities.newInstance(DB, idCircle, idItinerario);
        else if( INSTANCE == 2)
            fragment =  EvidenciasActivities.newInstance(DB, idCircle, idItinerario);


        if ( fragment != null ) {

            fragment.setArguments(args);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }


    }
    //</editor-fold>

    //<editor-fold desc="buscar listado de inscritos en el circulo">
    public void SearchListInscritos(final LinearLayout contentList, int idCircle1 ){

        String user = code.getIdUser();
        String token = code.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_ADMIN_CIRCLE )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        AdminCircles adminCircle = retrofit.create(AdminCircles.class);

        Call<ResponseContent> call = adminCircle.GetUsuariosCircle(user, token, idCircle1 );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                if( data != null && code.ValidateStatusResponse( response.code() ) ){
                    boolean answer = ValidateResponse( data  );

                    if( answer ){

                        try{

                            JSONArray arrayResult = data.getResults();
                            JSONObject index = data.getIndex();

                            for( int c = arrayResult.length() -1; c >= 0; c-- ){

                                String id = arrayResult.getJSONObject(c)
                                                    .getString( index.getString("0") );
                                String name = arrayResult.getJSONObject(c)
                                                    .getString( index.getString("1") );

                                contentList.addView( GenerateComponentsList(c, name, id) );

                            }
                        }catch (Exception e){
                            services.SaveError(e,
                                    new Exception().getStackTrace()[0].getMethodName().toString(),
                                    this.getClass().getName());
                        }

                    }else{
                        Log.i( mss.TAG, data.getBody().toString() );
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

    //<editor-fold desc="generar los checkBox dinamicos para la asistencia">
    private CheckBox GenerateComponentsList( int idCheck, String nameInscrito, String id ){


        CheckBox check = new CheckBox( CONTEXTO );

        check.setTextColor(CONTEXTO.getResources().getColor(R.color.textBlack));
        check.setId( idCheck );
        check.setTextSize(20);
        check.setText( nameInscrito );
        check.setContentDescription( id );

        return check;

    }
    //</editor-fold>

    //<editor-fold desc="armo objeto de los datos del listado para enviar al server">
    public void SaveAsistenciasItinerario( LinearLayout layout, String idItinerario){

        try{

            JSONObject index = new JSONObject();
            index.put("item1","asistencia");
            index.put("item0","id");

            JSONObject suscriptor;
            JSONArray listAsistencia = new JSONArray();
            JSONArray objectAsistencia = new JSONArray();

            for(int i=0;i<layout.getChildCount();i++)
            {
                suscriptor = new JSONObject();

                CheckBox check =  (CheckBox)layout.getChildAt(i);

                if( check.isChecked() )//dependiendo de s esta seleccionado agrego el codigo
                    suscriptor.put( index.getString("item1"), mss.ITINERARIO_ASISTENCIA_TRUE );
                else
                    suscriptor.put( index.getString("item1"), mss.ITINERARIO_ASISTENCIA_FALSE );

                suscriptor.put( index.getString("item0"), check.getContentDescription() );

                listAsistencia.put(suscriptor);

            }

            objectAsistencia.put(listAsistencia);
            objectAsistencia.put(index);
            objectAsistencia.put( idItinerario );

            if( objectAsistencia != null )
                SendAsistenciaServer( objectAsistencia );


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());
        }

    }
    //</editor-fold>

    //<editor-fold desc="elimino el usuario del circulo">
    public void DeleteItinerario( int idItinerario ) {

        String user = code.getIdUser();
        String token = code.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_ADMIN_CIRCLE )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        AdminCircles adminCircle = retrofit.create(AdminCircles.class);

        Call<ResponseContent> call = adminCircle.delItinerario( user, token, idItinerario );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                try {
                    ResponseContent data = response.body();

                    if( data != null && code.ValidateStatusResponse( response.code() ) ){
                        boolean answer = ValidateResponse( data  );

                        if( answer ){

                            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                                Toast.makeText(CONTEXTO.getApplicationContext(),
                                        mss.msmServices.getString(data.getBody().getString(1).toString()),
                                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                            }else{

                                Log.i( mss.TAG, data.getBody().toString() );

                            }

                        }else{
                            Log.i( mss.TAG, data.getBody().toString() );
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    services.SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
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

    //<editor-fold desc="guardar la asistencia tomada del itinerario">
    public void SendAsistenciaServer( JSONArray listObject ){

        String user = code.getIdUser();
        String token = code.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_ADMIN_CIRCLE )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        AdminCircles adminCircle = retrofit.create(AdminCircles.class);

        Call<ResponseContent> call = adminCircle.saveAsistenciaItinerario(user, token,
                                                                        listObject.toString() );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                try{

                    ResponseContent data = response.body();

                    if( data != null && code.ValidateStatusResponse( response.code() ) ){
                        boolean answer = ValidateResponse( data  );

                        if( answer ){

                            Toast.makeText(CONTEXTO.getApplicationContext(),
                                    mss.msmServices.getString(data.getBody().getString(1).toString()),
                                    Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                        }else{
                            Log.i( mss.TAG, data.getBody().toString() );
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    services.SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
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

    //<editor-fold desc="procesa la respuesta enviada del server">
    private boolean ValidateResponse(ResponseContent data) {

        try {

            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(data.getBody().getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                return false;

            }else{

                return true;

            }

        } catch (JSONException e) {
            e.printStackTrace();
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return false;

    }
    //</editor-fold>

}
