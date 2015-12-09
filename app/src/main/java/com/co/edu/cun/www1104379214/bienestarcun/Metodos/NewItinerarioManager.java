package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 2/12/15.
 */
public class NewItinerarioManager {

    DBManager DB;
    Context CONTEXTO;

    String[][] parametros;
    TaskExecuteHttpHandler BD;
    CodMessajes mss = new CodMessajes();

    public NewItinerarioManager(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;

    }


    public void SaveNewItinerario( TextView contentNameActivitie,
                                   TextView contentDetailActivitie,
                                   DatePicker contentFecha,
                                   TimePicker contentHora
    ){//guardar en la BD los datos del nuevo itinerario

        GeneralCode code = new GeneralCode( DB, CONTEXTO );

        String idDocente = code.getIdUser();
        String name = contentNameActivitie.getText().toString();
        String detail = contentDetailActivitie.getText().toString();
        String fecha = contentFecha.getYear()+"-"+( contentFecha.getMonth() + 1 ) +"-"+contentFecha.getDayOfMonth();
        String hora = contentHora.getCurrentHour()+":"+contentHora.getCurrentMinute()+":00";

        if( !idDocente.equals("") && !name.equals("") && !detail.equals("") && !fecha.equals("") && !hora.equals("") )
            SendServerItinerario( idDocente, name, detail, fecha, hora);
        else
            Toast.makeText(CONTEXTO, mss.FormError, Toast.LENGTH_SHORT).show();


    }

    private void SendServerItinerario( String idDocente,
                                       String name,
                                       String details,
                                       String fecha,
                                       String hora) {//Enviar datos al server


        final String service = "adminCircle/saveNewItinerario.php";
        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idDocente},
                {"nameActiviti",name},
                {"detailActivitie", details},
                {"date", fecha},
                {"hour", hora}
        };


        try {

            BD = new TaskExecuteHttpHandler(service, parametros, CONTEXTO);
            String resultado="";
            try {
                resultado = BD.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();

            }catch (Exception e){
                String contenido = "Error desde android #!#";
                contenido += " Funcion: SendServerItinerario try 2#!#";
                contenido += "Clase : NewItinerarioManager.java #!#";
                contenido += e.getMessage();
                new ServicesPeticion(CONTEXTO).SaveError(contenido);
            }


            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){

            String contenido = "Error desde android #!#";
            contenido += " Funcion: SendServerItinerario try 1 #!#";
            contenido += "Clase : SendServerItinerario.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);

        }

    }
}
