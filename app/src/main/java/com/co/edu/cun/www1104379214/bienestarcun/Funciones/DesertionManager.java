package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.Context;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.ReporteDesercion;
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
 * Created by root on 30/11/15.
 */
public class DesertionManager {

    DBManager DB;
    Context CONTEXTO;

    Constantes mss = new Constantes();

    public DesertionManager(DBManager db, Context contexto) {
        this.DB = db;
        this.CONTEXTO = contexto;

    }


    //<editor-fold desc="Valida los datos del formulario desercion">
    public void SendReportDesertion( TextView contentIdDesertor,
                                     String facultad,
                                     TextView contentDescription,
                                     RadioGroup groupHorario){

        GeneralCode code = new GeneralCode( DB, CONTEXTO );

        String idDocente = code.getIdUser();
        String token = code.getToken();
        String idUser = contentIdDesertor.getText().toString();
        String Descripcion = contentDescription.getText().toString();
        String horario = null;

        switch ( groupHorario.getCheckedRadioButtonId() ){//obtengo el radiobutton seleccionado

            case R.id.rb_Diurno:
                horario = "Diurno";
                break;

            case R.id.rb_nocturne:
                horario = "Nocturno";
                break;

        }

        if( horario != null && !idUser.equals("") && !Descripcion.equals("") && facultad != null )
            SendServerDesertion(idDocente, token, facultad, idUser, Descripcion, horario);
        else
            Toast.makeText(CONTEXTO, mss.FormError, Toast.LENGTH_SHORT).show();


    }
    //</editor-fold>

    //<editor-fold desc="Envia los datos del reporte de la desercion">
    private void SendServerDesertion( String idDocente, String token, String facultad,
                                      String idUser, String descripcion, String horario) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();//asisgnar tiempo de espera a la peticion

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_DESERTION )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        ReporteDesercion NewReporte = retrofit.create(ReporteDesercion.class);

        Call<ResponseContent> call = NewReporte
                .SendReporte(idDocente, token,  facultad, idUser, descripcion, horario);

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                ValidateResponse( data );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "error "+ t.toString());

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

                Log.i( mss.TAG1,data.getBody().toString() );

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
