package com.co.edu.cun.www1104379214.bienestarcun.Metodos;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.httpHandler;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.ChatPsicologa_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 25/11/15.
 */
public class ChatPsicologiaManager {

    Context CONTEXTO;
    DBManager DB;
    httpHandler BD = new httpHandler();
    CodMessajes mss = new CodMessajes();

    String[][] parametros;

    TaskExecuteSQLSearch sqliteSearch;
    JSONObject indexChats;
    JSONArray result = null;
    JSONArray resultResponse = null;

    public ChatPsicologiaManager(Context contexto, DBManager db) {

        this.CONTEXTO = contexto;
        this.DB = db;

    }

    //<editor-fold desc="Verificar tipo de usuario logueado actualmente">
    public  boolean ComproveUser(){

        String[][] values;
        boolean resultado = false;

        try {

            String[] camposSeacrh = new String[]{
                    DB.CN_TIPE_USER
            };

            sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                    camposSeacrh,
                    CONTEXTO,
                    DB
            ); //busqueda

            Cursor result = sqliteSearch.execute().get();

            JSONObject user_log = new AdapterUserMenu(CONTEXTO, DB).CreateObjectResultSQL(result, camposSeacrh);//recupero el usuario logueado
            //{"ROW0":{"Usuario":"krlos","id_user":"1104379","Token":"ec4698a66a5096801b66ebeed3eb0064ee6525cb"}}

            if ( user_log.length() > 0 ){

                JSONObject arrayResult = user_log.getJSONObject("ROW0");

                resultado = ( arrayResult.getString(DB.CN_TIPE_USER).equals("1001") ) ? true: false;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: ComproveUser #!#";
            contenido += "Clase : ChatPsicologia.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return resultado;
    }
    //</editor-fold> actualmente

    public JSONArray SearchChatPendientes() { //buscar circulo existente en la BD o los agregados por el usuario
        final String service;

        service = "chatPsicologia/getChatPendientes.php";

        try {

            result = GetChatPendientesExists(service);
            resultResponse = result.getJSONArray(0);
            indexChats = result.getJSONObject(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: SearchChatPendientes #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return resultResponse;


    }

    public JSONArray GetChatPendientesExists(String service) throws InterruptedException {
        //obtengo el array de objeto con los circulos

        String[][] values = null;


        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"tipuser","1001"}
        };

        String resultado = BD.HttpRequestServer(service, parametros);

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                return null;

            }else {

                return arrayResponse;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetChatPendientesExists #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return arrayResponse;

    }

    public JSONObject IndexChats() { //retornar los index dell objeto traido de la BD

        return indexChats;
    }

    private String getIdUser() {//obtengo el id del usuario logueado

        String idUser="";

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            Cursor result = sqliteSearch.execute().get();

            JSONObject jsonUser = new AdapterUserMenu(CONTEXTO, DB).CreateObjectResultSQL(result, camposSeacrh);

            if ( jsonUser.length() > 0 ){

                idUser = (String) jsonUser.getJSONObject( "ROW0").get(DB.CN_ID_USER_BD);

                return idUser;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getIdUser #!#";
            contenido += "Clase : CircleList.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return null;
    }


    public void OpenChatPsicologia(int IdDestinatario, FragmentManager fragmentManager) {//llamar fragmen del chat

        Bundle args = new Bundle();
        args.putString("", "");
        String IdRemitente = getIdUser();

        if( IdRemitente != null ) {

            Fragment fragment;
            fragment = ChatPsicologa_app.newInstance(IdDestinatario,  Integer.parseInt( IdRemitente ) );

            fragment.setArguments(args);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();

        }
    }


    public void AddMesagesChat(EditText Contentmensaje, TextView ContentRemitente, TextView ContentReceptor, ScrollView contentChat){

        String Remitente;
        String Receptor;
        String Mensaje;

        Remitente = (String) ContentRemitente.getText();
        Receptor = (String) ContentReceptor.getText();
        Mensaje = Contentmensaje.getText().toString();

        final String service = "chatPsicologia/getMensajes.php";

        try {

            JSONArray resultMensajes;

            resultMensajes = GetMensajesPendientesExists(service, Remitente, Receptor);
            Log.i(mss.TAG, resultMensajes.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
        String contenido = "Error desde android #!#";
        contenido += " Funcion: AddMesagesChat #!#";
        contenido += "Clase : ChatPsicologiaManager.java #!#";
        contenido += e.getMessage();
        new ServicesPeticion().SaveError(contenido);
    }

    }

    public JSONArray GetMensajesPendientesExists(String service, String remitente, String receptor) throws InterruptedException {
        //obtengo el array de objeto con los circulos

        String[][] values = null;


        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"remitente",remitente},
                {"receptor",receptor}
        };

        String resultado = BD.HttpRequestServer(service, parametros);

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                return null;

            }else {

                return arrayResponse;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetMensajesPendientesExists #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
        }

        return arrayResponse;

    }

}
