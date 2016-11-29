package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.Itinerarios;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONException;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 2/12/15.
 */
public class NewItinerarioManager {

    DBManager DB;
    Context CONTEXTO;
    Constantes mss = new Constantes();
    GeneralCode code;

    OkHttpClient okHttpClient;

    public NewItinerarioManager(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();//asisgnar tiempo de espera a la peticion

        code = new GeneralCode( DB, CONTEXTO );

    }


    //<editor-fold desc="guardar en la BD los datos del nuevo itinerario">
    public void SaveNewItinerario( TextView contentNameActivitie,
                                   TextView contentDetailActivitie,
                                   DatePicker contentFecha,
                                   TimePicker contentHora ){

        String idDocente = code.getIdUser();
        String token = code.getToken();
        String name = contentNameActivitie.getText().toString();
        String detail = contentDetailActivitie.getText().toString();
        String fecha = contentFecha.getYear()+"-"+( contentFecha.getMonth() + 1 )
                                                            +"-"+contentFecha.getDayOfMonth();
        String hora = contentHora.getCurrentHour()+":"+contentHora.getCurrentMinute()+":00";

        if( !idDocente.equals("") && !name.equals("") && !detail.equals("")
                                                    && !fecha.equals("") && !hora.equals("") )
            SendServerItinerario( idDocente, token, name, detail, fecha, hora);
        else
            Toast.makeText(CONTEXTO, mss.FormError, Toast.LENGTH_SHORT).show();


    }
    //</editor-fold>

    //<editor-fold desc="Enviar datos al server">
    private void SendServerItinerario( String idDocente,
                                       String token,
                                       String name,
                                       String details,
                                       String fecha,
                                       String hora) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_ADMIN_CIRCLE )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        Itinerarios NewItinerario = retrofit.create(Itinerarios.class);

        Call<ResponseContent> call = NewItinerario.SaveNewItinerario( idDocente, token,
                                                                        name, details, fecha, hora);

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

    //<editor-fold desc="procesa la respuesta enviada del server">
    private void ValidateResponse(ResponseContent data) {

        try {

            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(data.getBody().getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else{

                Log.i( mss.TAG,data.getBody().toString() );

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
