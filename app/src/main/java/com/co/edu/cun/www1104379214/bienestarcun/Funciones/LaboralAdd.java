package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
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
public class LaboralAdd {

    DBManager DB;
    Context CONTEXTO;

    String[][] parametros;
    TaskExecuteHttpHandler BD;
    Constantes mss = new Constantes();

    public LaboralAdd(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;

    }


    public void SaveNewHistoryLaboral( TextView contentNameActivitie,
                                   TextView contentDetailActivitie,
                                   DatePicker contentFechaStart,
                                   CheckBox working,
                                   DatePicker contentFechaEnd
    ){//guardar en la BD los datos del nuevo historial laboral

        GeneralCode code = new GeneralCode( DB, CONTEXTO );

        String idDocente = code.getIdUser();
        String nameEmpresa = contentNameActivitie.getText().toString();
        String cargoEmpresa = contentDetailActivitie.getText().toString();
        String fechaStart = contentFechaStart.getYear()+"-"+( contentFechaStart.getMonth() + 1 ) +"-"+contentFechaStart.getDayOfMonth();
        String fechaEnd = "";

        if( !working.isChecked() )
            fechaEnd = contentFechaEnd.getYear() + "-"+( contentFechaEnd.getMonth() + 1 ) +"-"+contentFechaEnd.getDayOfMonth();

        if( !idDocente.equals("") && !nameEmpresa.equals("") && !cargoEmpresa.equals("") && !fechaStart.equals("")  )
            SendServerNewLaboral(idDocente, nameEmpresa, cargoEmpresa, fechaStart, fechaEnd);
        else
            Toast.makeText(CONTEXTO, mss.FormError, Toast.LENGTH_SHORT).show();


    }

    private void SendServerNewLaboral(String idDocente,
                                      String empresa,
                                      String cargo,
                                      String dateStart,
                                      String dateEnd) {//Enviar datos al server


        final String service = "laboral/saveNewLaboralHistory.php";
        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idDocente},
                {"empresa", empresa},
                {"cargo", cargo},
                {"dateStart", dateStart},
                {"dateEnd", dateEnd}
        };


        try {

            BD = new TaskExecuteHttpHandler(service, parametros, null);
            String resultado="";
            try {
                resultado = BD.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();

            }catch (Exception e){
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server
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

            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

        }

    }


    public void SaveNewHistoryLaboral( RadioGroup ContentStatus){//guardar en la BD los datos del nuevo historial laboral

        GeneralCode code = new GeneralCode( DB, CONTEXTO );
        String status = "";

        String idDocente = code.getIdUser();
        switch ( ContentStatus.getCheckedRadioButtonId() ){//obtengo el radiobutton seleccionado

            case R.id.rbStatusYes:
                status = "1";
                break;

            case R.id.rbStatusNot:
                status = "0";
                break;

        }

        if( !status.equals("") )
            SendServerNewLaboralStatus(idDocente, status);
        else
            Toast.makeText(CONTEXTO, mss.FormError, Toast.LENGTH_SHORT).show();


    }

    private void SendServerNewLaboralStatus(String idDocente, String status) {//Enviar datos al server


        final String service = "laboral/updateStatusLaboral.php";
        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idDocente},
                {"status", status}
        };


        try {

            BD = new TaskExecuteHttpHandler(service, parametros, null);
            String resultado="";
            try {
                resultado = BD.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();

            }catch (Exception e){
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server
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

            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

        }

    }
}

