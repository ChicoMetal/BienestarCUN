package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.Users;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import org.json.JSONArray;
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
 * Created by root on 30/11/15.
 */
public class GeneralCode {


    DBManager DB;
    Context CONTEXTO;
    TaskExecuteSQLSearch userSearch;
    Constantes mss = new Constantes();
    ServicesPeticion services;

    Spinner lista;
    ImageButton BtnChoseSede;
    String[][] UserDefault;
    TaskExecuteSQLInsert sqliteInsert;
    OkHttpClient okHttpClient;

    public GeneralCode(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;
        services = new ServicesPeticion();

        okHttpClient = new OkHttpClient.Builder()
            .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
            .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
            .build();//asisgnar tiempo de espera a la peticion

    }

    //<editor-fold desc="obtengo el id del usuario logueado">
    public String getIdUser() {

        String idUser="";

        String[] camposSeacrh = new String[]{
                DB.CN_ID_USER_BD,
        };

        userSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            Cursor result = userSearch.execute().get();

            JSONObject jsonUser = new AdapterUserMenu(CONTEXTO, DB)
                                    .CreateObjectResultSQL(result, camposSeacrh);

            if ( jsonUser.length() > 0 ){

                idUser = (String) jsonUser.getJSONObject( "ROW0").get(DB.CN_ID_USER_BD);

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

        return idUser;
    }
    //</editor-fold>

    //<editor-fold desc="obtengo el token de la sesion">
    public String getToken() {

        String token = "";

        String[] camposSeacrh = new String[]{
                DB.CN_TOKEN_LOGIN,
        };

        userSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            Cursor result = userSearch.execute().get();

            JSONObject jsonUser = new AdapterUserMenu(CONTEXTO, DB)
                    .CreateObjectResultSQL(result, camposSeacrh);

            if ( jsonUser.length() > 0 ){

                token = (String) jsonUser.getJSONObject( "ROW0").get(DB.CN_TOKEN_LOGIN);

                return token;

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

        return token;
    }
    //</editor-fold>

    //<editor-fold desc="obtener el nombre del usuario">
    public void getNameUser(final TextView ContentNameUser){

        final String idUser = getIdUser();

        if( idUser != null ){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl( ServerUri.SERVICE_USER )
                    .addConverterFactory(GsonConverterFactory.create())
                    .client( okHttpClient )
                    .build();

            Users user = retrofit.create(Users.class);

            Call<ResponseContent> call = user.getUserName( idUser );

            call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
                @Override
                public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                    ResponseContent data = response.body();

                    if( ValidateStatusResponse( response.code() ) )
                        ValidateResponse( data, ContentNameUser, idUser );


                }

                @Override
                public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                    ManageFailurePetition(t);
                    Log.e( mss.TAG, "error "+ t.toString());

                }
            });
        }


    }
    //</editor-fold>

    //<editor-fold desc="Obtener el tipo de usuario">
    public  String GetTipeUser(){

        String TipeUser="";

        String[] camposSeacrh = new String[]{
                DB.CN_TIPE_USER,
        };

        userSearch = new TaskExecuteSQLSearch(DB.TABLE_NAME_USER,
                camposSeacrh,
                CONTEXTO,
                DB
        ); //busqueda

        try {

            Cursor result = userSearch.execute().get();

            JSONObject jsonUser = new AdapterUserMenu(CONTEXTO, DB)
                    .CreateObjectResultSQL(result, camposSeacrh);

            if ( jsonUser.length() > 0 ){

                TipeUser = (String) jsonUser.getJSONObject( "ROW0").get(DB.CN_TIPE_USER);

                return TipeUser;

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

        return TipeUser;
    }
    //</editor-fold>

    //<editor-fold desc="procesa la respuesta enviada del server">
    private void ValidateResponse(ResponseContent data, TextView ContentNameUser, String idUser) {

        try {

            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                if( idUser.equals( mss.DftUsrId ) )
                    ContentNameUser.setText(mss.DftUsrName);
                else
                    ContentNameUser.setText("");

                Log.i( mss.TAG, data.getBody().toString() );

            }else{

                JSONArray nameUser =  data.getResults();
                JSONObject index = data.getIndex();

                String firtname = ( nameUser != null) ? nameUser.getJSONObject(0).getString( index.getString("0")  ) : "ddd";
                String lastname = (nameUser != null )? nameUser.getJSONObject(0).getString( index.getString("1")  ): "dddd";

                ContentNameUser.setText( firtname+" "+lastname  );



            }

        } catch (JSONException e) {
            e.printStackTrace();
            services.SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }
    //</editor-fold>

    //<editor-fold desc="custom dialog">
    public void ChoseUserDefault(Context activity){

        String user = getIdUser();

        if( user == null || user.equals("") ){//si no existe ningun usuario

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_chose_sede);
            dialog.setCancelable(false);

            lista = (Spinner) dialog.findViewById(R.id.lista_sedes);
            BtnChoseSede = (ImageButton) dialog.findViewById(R.id.btn_send_sede);

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(activity, android.R.layout
                                                        .simple_spinner_item, mss.DftUsrNameSedes);
            lista.setAdapter(adaptador);


            BtnChoseSede.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        UserDefault = new String[][]{
                                {DBManager.CN_ID_USER_BD, mss.DftUsrId
                                        .getString( lista.getSelectedItem().toString() ) },
                                {DBManager.CN_USER, mss.DftUsrName},
                                {DBManager.CN_PASSWORD, mss.DftUsrPass },
                                {DBManager.CN_TIPE_USER, mss.UsrLoginOff },
                                {DBManager.CN_TOKEN_LOGIN, mss.DftUsrToken }
                        };//TODO: revisar que se este insertando el usuario correspondiente a la sede

                        ContentValues UserValues = DB.GenerateValues( UserDefault );

                        sqliteInsert = new TaskExecuteSQLInsert(DBManager.TABLE_NAME_USER,
                                UserValues,
                                CONTEXTO,
                                DB
                        ); //insertar usuario por default



                        sqliteInsert.execute().get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        services.SaveError(e,
                                new Exception().getStackTrace()[0].getMethodName().toString(),
                                this.getClass().getName());//Envio la informacion de la excepcion al server
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();//ocultar dialog
                }
            });


            dialog.show();
        }


    }
    //</editor-fold>

    //<editor-fold desc="Metodo para validar los estados enviados por el server">
    public boolean ValidateStatusResponse( int code ) {


        Log.i(mss.TAG, "Status code response: "+code);

        if( code >= 300 ) {//validar el rango del estado para saber si es exitosa o erronea la peticion
            try {
                Toast.makeText(CONTEXTO.getApplicationContext(),
                        mss.StatusServices.getString( String.valueOf( code ) ),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor
            } catch (JSONException e) {
                e.printStackTrace();
                services.SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server
            }

            return false;
        }else
            return true;
    }
    //</editor-fold>

    //<editor-fold desc="Manejo de fallos al realizar peticiones con retrofit">
    public void ManageFailurePetition( Throwable t ){

        if( isOnlineNet() )
            Toast.makeText(CONTEXTO, t.getMessage(), Toast.LENGTH_SHORT).show();

        else {
            try {

                Toast.makeText(CONTEXTO, mss.msmServices.getString("0"),
                        Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                services.SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server
            }
        }

    }
    //</editor-fold>

    //<editor-fold desc="Verificar si esta activa la net">
    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                CONTEXTO.getSystemService(CONTEXTO.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
    //</editor-fold>

    //<editor-fold desc="Verificar conexion a internet">
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    //</editor-fold>
}
