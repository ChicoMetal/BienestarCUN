package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.ChatPsicologa_app;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 25/11/15.
 */
public class ChatPsicologiaManager {




    Context CONTEXTO;
    DBManager DB;
    TaskExecuteHttpHandler BD;
    CodMessajes mss = new CodMessajes();
    LinearLayout CONTENTCHAT;

    TaskExecuteSQLSearch sqliteSearch;
    JSONObject indexChats;
    JSONArray result = null;
    JSONArray resultResponse = null;


    public ChatPsicologiaManager(Context contexto, DBManager db, LinearLayout contentchat ) {
        //constructor de la clase para cuando se necesite la interfaz y eventos del chat

        this.CONTEXTO = contexto;
        this.DB = db;
        this.CONTENTCHAT = contentchat;



    }





    public ChatPsicologiaManager(Context contexto, DBManager db  ) {//constructor para instanciar metodos que no necesiten de la interfaz

        this.CONTEXTO = contexto;
        this.DB = db;

    }



    //<editor-fold desc="Verificar tipo de usuario logueado actualmente">
    public  boolean ComproveUser(){

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

                resultado = ( arrayResult.getString(DB.CN_TIPE_USER).equals(CodMessajes.UsrPsicologa) ) ? true: false;

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
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return resultado;
    }
    //</editor-fold> actualmente

    public JSONArray SearchChatPendientes() { //Mensajes sin leer
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
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return resultResponse;


    }

    public JSONArray GetChatPendientesExists(String service) throws InterruptedException {
        //obtengo el array de objeto con los circulos

        String[][] values = null;


        JSONArray arrayResponse = null;

        values = new String[][]{ //array parametros a enviar
                {"tipuser",CodMessajes.UsrPsicologa}
        };

        BD = new TaskExecuteHttpHandler(service, values,CONTEXTO);
        String resultado="";
        try {
            resultado = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetChatPendientesExists #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

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
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return arrayResponse;

    }

    public JSONObject IndexChats() { //retornar los index dell objeto traido de la BD

        return indexChats;
    }

    public String getIdUser() {//obtengo el id del usuario logueado

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
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
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


    public void AddMesagesChat(EditText Contentmensaje,
                               TextView ContentRemitente,
                               TextView ContentReceptor){//Agregar los textView con los mensajes y traer


        String Remitente;
        String Receptor;
        String Mensaje;

        Remitente = (String) ContentRemitente.getText();
        Receptor = (String) ContentReceptor.getText();
        Mensaje = Contentmensaje.getText().toString();



        final String service = "chatPsicologia/getMensajes.php";

        try {
            if( !Mensaje.equals("") ) {

             /*JSONArray resultMensajes;

                resultMensajes = GetMensajesPendientesExists(service, Remitente, Receptor,Mensaje);


                if (resultMensajes != null) {//si trae mensajes de vuelta

                    JSONArray arrayresult = resultMensajes.getJSONArray(0);
                    JSONObject indexResult = resultMensajes.getJSONObject(1);


                    for( int c = 0; c < arrayresult.length(); c++){//itero por cada mensaje

                        String MensajeRecibido = arrayresult.getJSONObject(c).getString( indexResult.getString("1") );

                        contentChat.addView( GenerarTextView(1,MensajeRecibido) );

                    }

                } */

            }

        /*} catch (InterruptedException e) {
            e.printStackTrace();*/
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: AddMesagesChat #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }



    }

    public JSONArray GetMensajesPendientesExists(String service, String remitente, String receptor, String mensaje) throws InterruptedException {
        //obtengo el array de objeto con los mensajes

        String[][] values = null;


        JSONArray arrayResponse = null;

        values = new String[][]{ //array parametros a enviar
                {"remitente",remitente},
                {"receptor",receptor},
                {"mensaje",mensaje}
        };

        BD = new TaskExecuteHttpHandler(service, values,CONTEXTO);
        String resultado="";
        try {
            resultado = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetMensajesPendientesExists #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

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
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return arrayResponse;

    }



    public String getIdPsicologiaUser(String idUser) {
        //busco el id del usuario encargado del area de psicologia

        String[][] values;
        String service = "chatPsicologia/getUserPsicologo.php";


        JSONArray arrayResponse = null;

        values = new String[][]{ //array parametros a enviar
                {"usuario",idUser}
        };

        BD = new TaskExecuteHttpHandler(service, values,CONTEXTO);
        String resultado="";
        try {
            resultado = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getIdPsicologiaUser #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString(arrayResponse.getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                return null;

            }else {

                JSONObject result = arrayResponse.getJSONArray(0).getJSONObject(0);
                JSONObject index = arrayResponse.getJSONObject(1);

                return result.getString( index.getString("0") );

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getIdPsicologiaUser #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return null;

    }
}

