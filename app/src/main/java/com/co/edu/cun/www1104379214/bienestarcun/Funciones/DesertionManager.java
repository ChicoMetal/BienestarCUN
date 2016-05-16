package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.Context;
import android.widget.RadioGroup;
import android.widget.TextView;
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
 * Created by root on 30/11/15.
 */
public class DesertionManager {

    DBManager DB;
    Context CONTEXTO;

    String[][] parametros;
    TaskExecuteHttpHandler BD;
    CodMessajes mss = new CodMessajes();

    public DesertionManager(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;

    }


    public void SendReportDesertion( TextView contentIdDesertor, RadioGroup groupHorario){

        GeneralCode code = new GeneralCode( DB, CONTEXTO );

        String idDocente = code.getIdUser();
        String idUser = contentIdDesertor.getText().toString();
        String horario = null;

        switch ( groupHorario.getCheckedRadioButtonId() ){//obtengo el radiobutton seleccionado

            case R.id.rb_Diurno:
                horario = "Diurno";
                break;

            case R.id.rb_nocturne:
                horario = "Nocturno";
                break;

        }

        if( horario != null && !idUser.equals("") )
            SendServerDesertion(idDocente, idUser, horario);
        else
            Toast.makeText(CONTEXTO, mss.FormError, Toast.LENGTH_SHORT).show();


    }

    private void SendServerDesertion( String idDocente, String idUser, String horario) {


        final String service = "desertion/saveDesertion.php";
        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idDocente},
                {"desertor",idUser},
                {"horario", horario}
        };



        try {

            BD = new TaskExecuteHttpHandler(service, parametros,CONTEXTO);
            String resultado="";
            try {
                resultado = BD.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();

            }catch (Exception e){
                String contenido = "Error desde android #!#";
                contenido += " Funcion: SendServerDesertion #!#";
                contenido += "Clase : DesertionManager.java #!#";
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
            contenido += " Funcion: SendServerDesertion #!#";
            contenido += "Clase : DesertionManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);

        }

    }
}
