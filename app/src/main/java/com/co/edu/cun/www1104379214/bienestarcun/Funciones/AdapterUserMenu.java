package com.co.edu.cun.www1104379214.bienestarcun.Funciones;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.Users;
import com.co.edu.cun.www1104379214.bienestarcun.ui.MainActivity;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLDelete;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 30/10/15.
 */

public class AdapterUserMenu {

    Context CONTEXTO;
    DBManager DB;
    ServicesPeticion services;
    MainActivity MAIN;

    TaskExecuteSQLInsert sqliteInsert;
    TaskExecuteSQLSearch sqliteSearch;
    TaskExecuteSQLDelete sqliteDelete;
    Constantes mss = new Constantes();
    GeneralCode code;
    OkHttpClient okHttpClient;



    Cursor result;

    //<editor-fold desc="Constructor">
    public AdapterUserMenu( Context contexto, DBManager db) {

        this.CONTEXTO = contexto;
        this.DB = db;

        services = new ServicesPeticion();

        okHttpClient = new OkHttpClient.Builder()
            .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
            .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
            .build();//asisgnar tiempo de espera a la peticion

    }
    //</editor-fold>

    //<editor-fold desc="Login">
    public  boolean ProcessLogin(MainActivity main, EditText user,
                                            EditText pass, NavigationView menu ){
        boolean status = false;

        this.MAIN = main;

        try {

            ConfirmarUser(menu,
                    user.getText().toString(),
                    pass.getText().toString()); //Peticion login


        }catch (Exception e){
            e.printStackTrace();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return status;
    }
    //</editor-fold>

    //<editor-fold desc="Verificar el usuario en la BD remota">
    public void ConfirmarUser(final NavigationView menu,
                              String user,
                              final String pass)

            throws InterruptedException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_USER )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        Users users = retrofit.create(Users.class);

        Call<ResponseContent> call = users.LoginUser( user, pass );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                ValidateResponseLogin( data, menu );//continua login

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG, "error "+ t.toString());

            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="validar la informacion obtenida">
    private void ValidateResponseLogin(ResponseContent data, final NavigationView menu){
        try {


            if( data.getBody().getString(0).toString().equals("msm")   ){

                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.msmServices.getString(data.getBody().getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor


            }else {


                String[][] values = services.GenerateUserLoginValue(data.getBody(),
                        data.getIndex(),
                        data.getResults()); //genero los valores de la insercion en la BD local

                ProcedureConfirmLogin( values, menu );

            }

        }catch (Exception e){
            e.printStackTrace();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());
        }
    }
    //</editor-fold>

    //<editor-fold desc="una vez confirmado el usuario, continua esta secuencia">
    private void ProcedureConfirmLogin( String[][] values, final NavigationView menu ){

        if ( values != null  ){ //Si se obtubo la informacion correctamente

            try{

                DeleteUser();
                ContentValues UserValues = DB.GenerateValues( values );
                sqliteInsert = new TaskExecuteSQLInsert(DB.TABLE_NAME_USER,
                        UserValues,
                        CONTEXTO,
                        DB
                ); //insertar usuario logueado en sqlite

                if ( sqliteInsert.execute().get() ){//verifico el guardado del usuario


                    if ( Save_log() ) { //envio el registro de login

                        PrepareMenuUser(UserValues.getAsString(DB.CN_TIPE_USER), menu);//ajuste menu

                        Toast.makeText(CONTEXTO,
                                mss.LoginWell,
                                Toast.LENGTH_SHORT).show();

                        MAIN.selectItem(mss.TitleMenuHome, R.id.nav_home);
                        MAIN.setnameUserHead();

                    }


                }
            }catch (Exception e){
                e.printStackTrace();
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());
            }

        }

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

            JSONObject arrayResult = user_log.getJSONObject( "ROW0");//

            services.SaveLog(arrayResult, CONTEXTO);//envio el registro de log a la BD remota

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

                services.LogoutUser( result_object, CONTEXTO );

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

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

                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server

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
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }
    //</editor-fold>

    //<editor-fold desc="Verificar tipo de usuario logueado actualmente">
    public  void ComproveUser(NavigationView menu){

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
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }
    }
    //</editor-fold> actualmente

    //<editor-fold desc="Ocultacion de items de menu segun el usuario">
    public void PrepareMenuUser( String targetUser, NavigationView menu ){//adapto el menu del usuario
        switch ( targetUser ){
            case Constantes.UsrLoginOff:
                MenuLoginOf(menu);
                break;

            case Constantes.UsrStudent:
                MenuStudent(menu);
                break;

            case Constantes.UsrExStudent:
                MenuExStudent(menu);
                break;

            case Constantes.UsrPsicologa:
                MenuPsicologa(menu);
                break;

            case Constantes.UsrDocente:
                MenuDocente(menu);
                break;

            case Constantes.UsrAdCircle:
                MenuAdCircle(menu);
                break;

            case Constantes.UsrAd:
                MenuAd(menu);
                break;

            case Constantes.UsrSuperAd:
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
