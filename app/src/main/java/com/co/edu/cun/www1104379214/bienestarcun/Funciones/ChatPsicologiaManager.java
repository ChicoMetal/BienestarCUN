package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.ChatPsicologa_app;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 25/11/15.
 */
public class ChatPsicologiaManager {

    Context CONTEXTO;
    DBManager DB;
    TaskExecuteSQLSearch sqliteSearch;
    ServicesPeticion services;


    public ChatPsicologiaManager(Context contexto, DBManager db  ) {//constructor para instanciar metodos que no necesiten de la interfaz

        this.CONTEXTO = contexto;
        this.DB = db;
        services = new ServicesPeticion();

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

            JSONObject user_log = new AdapterUserMenu(CONTEXTO, DB).CreateObjectResultSQL(result,
                                                                    camposSeacrh);//recupero el usuario logueado
            //{"ROW0":{"Usuario":"krlos","id_user":"1104379","Token":"ec4698a66a5096801b66ebeed3eb0064ee6525cb"}}

            if ( user_log.length() > 0 ){

                JSONObject arrayResult = user_log.getJSONObject("ROW0");

                resultado = ( arrayResult.getString(DB.CN_TIPE_USER)
                                        .equals( Constantes.UsrPsicologa ) ) ? true: false;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return resultado;
    }
    //</editor-fold> actualmente


    //<editor-fold desc="obtengo el id del usuario logueado">
    public Long getIdUser() {

        Long idUser;

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

                idUser = Long.parseLong((String) jsonUser.getJSONObject( "ROW0").get(DB.CN_ID_USER_BD));

                return idUser;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return null;
    }
    //</editor-fold>


    //<editor-fold desc="llamar fragmen del chat">
    public void OpenChatPsicologia(Long IdDestinatario, FragmentManager fragmentManager) {

        Bundle args = new Bundle();


        Fragment fragment;
        fragment = ChatPsicologa_app.newInstance( DB, IdDestinatario  );

        fragment.setArguments(args);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();

    }
    //</editor-fold>

}

