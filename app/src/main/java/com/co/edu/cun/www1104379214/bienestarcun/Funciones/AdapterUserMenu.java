package com.co.edu.cun.www1104379214.bienestarcun.Funciones;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLDelete;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 30/10/15.
 */

public class AdapterUserMenu {

    Context CONTEXTO;
    DBManager DB;
    ServicesPeticion services;

    TaskExecuteSQLInsert sqliteInsert;
    TaskExecuteSQLSearch sqliteSearch;
    TaskExecuteSQLDelete sqliteDelete;
    CodMessajes mss = new CodMessajes();
    GeneralCode code;



    Cursor result;

    //<editor-fold desc="Constructor">
    public AdapterUserMenu(Context contexto, DBManager db) {

        this.CONTEXTO = contexto;
        this.DB = db;

        services = new ServicesPeticion(CONTEXTO);

    }
    //</editor-fold>

    //<editor-fold desc="Login">
    public  boolean ProcessLogin(EditText user, EditText pass, NavigationView menu){
        boolean status = false;
        String[][] values;


        try {

            values = services.ConfirmarUser(CONTEXTO,
                    user.getText().toString(),
                    pass.getText().toString()); //Peticion login

            if ( values != null  ){ //si el logueo fue correcto

                DeleteUser();
                ContentValues UserValues = DB.GenerateValues( values );
                sqliteInsert = new TaskExecuteSQLInsert(DB.TABLE_NAME_USER,
                        UserValues,
                        CONTEXTO,
                        DB
                ); //insertar usuario logueado en sqlite

                if ( sqliteInsert.execute().get() ){//verifico el guardado del usuario

                    try {

                       if ( Save_log() ) { //envio el registro de login

                           PrepareMenuUser(UserValues.getAsString(DB.CN_TIPE_USER), menu);//ajuste menu

                           Toast.makeText(CONTEXTO,
                                   mss.LoginWell,
                                   Toast.LENGTH_SHORT).show();

                           status = true;

                       }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        String contenido = "Error desde android #!#";
                        contenido += " Funcion: ProcessLogin try 2 #!#";
                        contenido += "Clase : Adaptermenu.java #!#";
                        contenido += e.getMessage();
                        services.SaveError(contenido);

                    }

                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: ProcessLogin try 1 #!#";
            contenido += "Clase : Adaptermenu.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }

        return status;
    }
    //</editor-fold>

    //<editor-fold desc="Save log">
    public Boolean Save_log() throws ExecutionException, InterruptedException, JSONException {
                            //Guardar el registro de un procesos de login en mysql

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
                DB.CN_TOKEN_LOGIN
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        result = sqliteSearch.execute().get();

        JSONObject user_log = CreateObjectResultSQL(result, camposSeacrh);//recupero el usuario logueado
        //{"ROW0":{"Usuario":"krlos","id_user":"1104379","Token":"ec4698a66a5096801b66ebeed3eb0064ee6525cb"}}

        if ( user_log.length() > 0 ){

            JSONObject arrayResult = user_log.getJSONObject( "ROW0");

            String resultLog = services.SaveLog(arrayResult, camposSeacrh);

            return true;

        }

        return false;

    }
    //</editor-fold>

    //<editor-fold desc="Logout">
    public void ProcessLogout(NavigationView menu, Context activity){

        code = new GeneralCode(DB, CONTEXTO);

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
                DB.CN_TOKEN_LOGIN
        };

        sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            result = sqliteSearch.execute().get();

            JSONObject result_object = CreateObjectResultSQL(result, camposSeacrh);

            if( result_object.length() > 0 ){

                DeleteUser();

                code.ChoseUserDefault(activity);

                PrepareMenuUser( mss.UsrLoginOff, menu );//ajuste menu

                String result_service = services.LogoutUser( result_object );

                Toast.makeText(CONTEXTO,
                        mss.msmServices.getString( result_service ),
                        Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();


            String contenido = "Error desde android #!#";
            contenido += " Funcion: ProcessLogout #!#";
            contenido += "Clase : Adaptermenu.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);

        }

    }
    //</editor-fold>

    //<editor-fold desc="Crear objeto con resultados consulta sqlite">
    public JSONObject CreateObjectResultSQL(Cursor result, String[] campos) throws JSONException { //crear JSONObject con los resultados de sqlite

        JSONObject SQL_RESULT_SEARCH = new JSONObject();
        JSONObject subobject;

        int numero = result.getCount();
        String nameRow = "ROW";
        int contador = 0;

        while ( result.moveToNext() ) {

            try { //creo objeto de resultados consulta
                subobject = new JSONObject();
                int size = campos.length;//numero de campos

                for (int c = 0; c < size; c++) {//itero por cada campo enviado para la consulta

                    subobject.put(campos[c],
                            result.getString(
                                    result.getColumnIndex(campos[c])
                            )
                    );

                }

                SQL_RESULT_SEARCH.put(nameRow+contador + "", subobject);

            } catch (JSONException e) {
                e.printStackTrace();


                String contenido = "Error desde android #!#";
                contenido += " Funcion: CreateObjectResultSQL #!#";
                contenido += "Clase : Adaptermenu.java #!#";
                contenido += e.getMessage();
                services.SaveError(contenido);

            }

            contador++;
        }

        return SQL_RESULT_SEARCH;

    }
    //</editor-fold>

    //<editor-fold desc="Eliminar usuario sqlite">
    private void DeleteUser(){


        sqliteDelete = new TaskExecuteSQLDelete(DB.TABLE_NAME_USER,
                CONTEXTO,
                DB
        ); //Eliminacion posibles usuarios logueados


        try {

            sqliteDelete.execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: DeleteUser #!#";
            contenido += "Clase : Adaptermenu.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }

    }
    //</editor-fold>

    //<editor-fold desc="Verificar tipo de usuario logueado actualmente">
    public  void ComproveUser(NavigationView menu){

        String[][] values;


        try {

            String[] camposSeacrh = new String[]{
                    DB.CN_TIPE_USER
            };

            sqliteSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                    camposSeacrh,
                    CONTEXTO,
                    DB
            ); //busqueda

            result = sqliteSearch.execute().get();

            JSONObject user_log = CreateObjectResultSQL(result, camposSeacrh);//recupero el usuario logueado
            //{"ROW0":{"Usuario":"krlos","id_user":"1104379","Token":"ec4698a66a5096801b66ebeed3eb0064ee6525cb"}}

            if ( user_log.length() > 0 ){

                JSONObject arrayResult = user_log.getJSONObject("ROW0");

                PrepareMenuUser( arrayResult.getString(DB.CN_TIPE_USER), menu );

            }else{

                PrepareMenuUser( mss.UsrLoginOff, menu );

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: ComproveUser #!#";
            contenido += "Clase : Adaptermenu.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }
    }
    //</editor-fold> actualmente



    //<editor-fold desc="Ocutacion de items de menu segun el usuario">
    public void PrepareMenuUser( String targetUser, NavigationView menu ){//adapto el menu del usuario
        switch ( targetUser ){
            case CodMessajes.UsrLoginOff:
                MenuLoginOf(menu);
                break;

            case CodMessajes.UsrStudent:
                MenuStudent(menu);
                break;

            case CodMessajes.UsrExStudent:
                MenuExStudent(menu);
                break;

            case CodMessajes.UsrPsicologa:
                MenuPsicologa(menu);
                break;

            case CodMessajes.UsrDocente:
                MenuDocente(menu);
                break;

            case CodMessajes.UsrAdCircle:
                MenuAdCircle(menu);
                break;

            case CodMessajes.UsrAd:
                MenuAd(menu);
                break;

            case CodMessajes.UsrSuperAd:
                MenuAd(menu);
                break;

        }

    }


    private void MenuLoginOf(NavigationView menu){

        menu.getMenu().findItem(R.id.nav_group_activities).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_desertion).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_laboral).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_notifications).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_chat).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_circle).setVisible(false);
        menu.getMenu().findItem(R.id.nav_login).setEnabled(true);
        menu.getMenu().findItem(R.id.nav_logout).setEnabled(false);

    }

    private void MenuPsicologa(NavigationView menu){

        menu.getMenu().findItem(R.id.nav_group_activities).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_desertion).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_laboral).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_notifications).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_chat).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_circle).setVisible(false);
        menu.getMenu().findItem(R.id.nav_login).setEnabled(false);
        menu.getMenu().findItem(R.id.nav_logout).setEnabled(true);
    }

    private void MenuStudent(NavigationView menu){

        menu.getMenu().findItem(R.id.nav_group_activities).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_desertion).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_laboral).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_notifications).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_chat).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_circle).setVisible(false);
        menu.getMenu().findItem(R.id.nav_login).setEnabled(false);
        menu.getMenu().findItem(R.id.nav_logout).setEnabled(true);
    }

    private void MenuExStudent(NavigationView menu){

        menu.getMenu().findItem(R.id.nav_group_activities).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_desertion).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_laboral).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_notifications).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_chat).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_circle).setVisible(false);
        menu.getMenu().findItem(R.id.nav_login).setEnabled(false);
        menu.getMenu().findItem(R.id.nav_logout).setEnabled(true);
    }

    private void MenuDocente(NavigationView menu){

        menu.getMenu().findItem(R.id.nav_group_activities).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_desertion).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_laboral).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_notifications).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_chat).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_circle).setVisible(false);
        menu.getMenu().findItem(R.id.nav_login).setEnabled(false);
        menu.getMenu().findItem(R.id.nav_logout).setEnabled(true);
    }

    private void MenuAdCircle(NavigationView menu){

        menu.getMenu().findItem(R.id.nav_group_activities).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_desertion).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_laboral).setVisible(false);
        menu.getMenu().findItem(R.id.nav_group_notifications).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_chat).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_circle).setVisible(true);
        menu.getMenu().findItem(R.id.nav_login).setEnabled(false);
        menu.getMenu().findItem(R.id.nav_logout).setEnabled(true);
    }


    private void MenuAd(NavigationView menu){

        menu.getMenu().findItem(R.id.nav_group_activities).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_desertion).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_laboral).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_notifications).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_chat).setVisible(true);
        menu.getMenu().findItem(R.id.nav_group_circle).setVisible(true);
        menu.getMenu().findItem(R.id.nav_login).setEnabled(false);
        menu.getMenu().findItem(R.id.nav_logout).setEnabled(true);
    }



    //</editor-fold>


}
