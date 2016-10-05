package com.co.edu.cun.www1104379214.bienestarcun.Funciones;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

    Spinner lista;
    ImageButton BtnChoseSede;
    String[][] UserDefault;
    TaskExecuteSQLInsert sqliteInsert;

    public GeneralCode(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;

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
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

        return null;
    }
    //</editor-fold>


    //<editor-fold desc="obtener el nombre del usuario">
    public void getNameUser(final TextView ContentNameUser){

        final String idUser = getIdUser();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.Server+"user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Users user = retrofit.create(Users.class);

        Call<ResponseContent> call = user.getUserName( idUser );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                ValidateResponse( data, ContentNameUser, idUser );


            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "error "+ t.toString());

            }
        });

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

                Log.i( mss.TAG1, data.getBody().toString() );

            }else{

                JSONArray nameUser = data.getResults();
                JSONObject index = data.getIndex();

                ContentNameUser.setText(
                        nameUser.getJSONObject(0).getString( index.getString("0")  )+" "+
                                nameUser.getJSONObject(0).getString( index.getString("1")  )
                );



            }

        } catch (JSONException e) {
            e.printStackTrace();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }
    //</editor-fold>

    //<editor-fold desc="custom dialog">
    public void ChoseUserDefault(Context activity){

        String user = getIdUser();

        if( user == null ){//si no existe ningun usuario

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_chose_sede);

            lista = (Spinner) dialog.findViewById(R.id.lista_sedes);
            BtnChoseSede = (ImageButton) dialog.findViewById(R.id.btn_send_sede);

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, mss.DftUsrNameSedes);
            lista.setAdapter(adaptador);


            BtnChoseSede.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        UserDefault = new String[][]{
                                {DBManager.CN_ID_USER_BD, mss.DftUsrId.getString( lista.getSelectedItem().toString() ) },
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
                        new ServicesPeticion().SaveError(e,
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


}
