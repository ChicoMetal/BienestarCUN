package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Xml;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.AsistenciaCircleActivities;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.EvidenciasActivities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 19/11/15.
 */
public class ItinerariosManager {

    private String ITINERARIO_ASISTENCIA_TRUE = "111";
    private String ITINERARIO_ASISTENCIA_FALSE = "000";

    TaskExecuteHttpHandler BD;
    CodMessajes mss = new CodMessajes();
    Context CONTEXTO;
    JSONObject indexItinerarios;
    JSONArray result = null;
    JSONArray resultResponse = null;

    public ItinerariosManager(Context contexto) {
        this.CONTEXTO = contexto;

    }

    public JSONArray SearchItinerarios( int idCircle ) { //buscar circulo existente en la BD o los agregados por el usuario

        String service = "circles/GetItinerariosCircle.php";


        try {

            result = GetItinerariosExitst(idCircle, service);
            resultResponse = result.getJSONArray(0);
            indexItinerarios = result.getJSONObject(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchItinerarios #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return resultResponse;


    }

    public JSONArray GetItinerariosExitst(int idCircle1, String service) throws InterruptedException {
        //obtengo el array de objeto con los itinerarios

        String[][] values;
        String idCircle = idCircle1+"";

        JSONArray arrayResponse = null;

        values = new String[][]{ //array parametros a enviar
                {"circle",idCircle}
        };

        BD = new TaskExecuteHttpHandler(service, values, CONTEXTO);
        String resultado="";
        try {
            resultado = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetItinerariosExitst #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else {

                return arrayResponse;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetItinerariosExitst #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return arrayResponse;

    }

    public JSONObject IndexItinerario() { //retornar los index dell objeto traido de la BD

        return indexItinerarios;
    }

    public void ShowAsistenciaItinerarios(int idItinerario, int idCircle, int INSTANCE, FragmentManager fragmentManager) {

        Bundle args = new Bundle();
        args.putString("", "");

        Fragment fragment;

        if( INSTANCE == 1)
            fragment =  AsistenciaCircleActivities.newInstance(idCircle, idItinerario);
        else
            fragment =  EvidenciasActivities.newInstance(idCircle, idItinerario);


        fragment.setArguments(args);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();


    }

    public void SearchListInscritos( LinearLayout contentList, int idCircle1 ){//buscar listado de inscritos en el circulo

        String service = "adminCircle/getInscritosCircle.php";
        String[][] values;
        String idCircle = idCircle1+"";

        JSONArray arrayResponse = null;

        values = new String[][]{ //array parametros a enviar
                {"circle",idCircle}
        };

        BD = new TaskExecuteHttpHandler(service, values,CONTEXTO);
        String resultado = "";
        try {

            resultado = BD.execute().get();

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else {

                JSONArray arrayResult = arrayResponse.getJSONArray( 0 );
                JSONObject index = arrayResponse.getJSONObject( 1 );

                for( int c = arrayResult.length() -1; c >= 0; c-- ){

                    String id = arrayResult.getJSONObject(c).getString( index.getString("0") );
                    String name = arrayResult.getJSONObject(c).getString( index.getString("1") );

                    contentList.addView( GenerateComponentsList(c, name, id) );

                }

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchListInscritos #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

    }

    private CheckBox GenerateComponentsList( int idCheck, String nameInscrito, String id ){//generar los checkBox dinamicos para la asistencia


        CheckBox check = new CheckBox( CONTEXTO );

        check.setTextColor(CONTEXTO.getResources().getColor(R.color.textBlack));
        check.setId( idCheck );
        check.setTextSize(20);
        check.setText( nameInscrito );
        check.setContentDescription( id );

        return check;

    }

    public int SearchCircleOfAdmin( String user ){//buscar el circulo al cual esta encargado

        String service = "adminCircle/getCircle.php";
        String[][] values;


        JSONArray arrayResponse;

        values = new String[][]{ //array parametros a enviar
                {"user",user}
        };

        BD = new TaskExecuteHttpHandler(service, values, CONTEXTO);
        String resultado;
        try {

            resultado = BD.execute().get();

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else {

                JSONArray arrayResult = arrayResponse.getJSONArray( 0 );
                JSONObject index = arrayResponse.getJSONObject(1);

                String idCircle = arrayResult.getJSONObject(0).getString( index.getString("0") );

                return Integer.parseInt( idCircle );


            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchCircleOfAdmin #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return 0;

    }

    public void SaveAsistenciasItinerario( LinearLayout layout, String idItinerario){//armo objeto de los datos del listado para enviar al server

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
                    suscriptor.put( index.getString("item1"), ITINERARIO_ASISTENCIA_TRUE );
                else
                    suscriptor.put( index.getString("item1"), ITINERARIO_ASISTENCIA_FALSE );

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
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SaveAsistenciasItinerario #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

    }

    public void SendAsistenciaServer( JSONArray listObject ){//buscar el circulo al cual esta encargado

        String service = "adminCircle/saveListAsitencia.php";
        String[][] values;


        JSONArray arrayResponse;

        values = new String[][]{ //array parametros a enviar
                {"listObject",listObject.toString() }
        };

        BD = new TaskExecuteHttpHandler(service, values, CONTEXTO);
        String resultado;
        try {

            resultado = BD.execute().get();

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
            contenido += " Funcion: SearchCircleOfAdmin #!#";
            contenido += "Clase : ItinerariosManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

    }

}
