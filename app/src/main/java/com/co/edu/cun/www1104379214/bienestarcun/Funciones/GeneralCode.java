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

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by root on 30/11/15.
 */
public class GeneralCode {


    DBManager DB;
    Context CONTEXTO;

    TaskExecuteSQLSearch userSearch;

    CodMessajes mss = new CodMessajes();
    String[][] parametros;
    TaskExecuteHttpHandler BD;

    Spinner lista;
    ImageButton BtnChoseSede;
    String[][] UserDefault;
    TaskExecuteSQLInsert sqliteInsert;
    ServicesPeticion services = new ServicesPeticion(CONTEXTO);

    public GeneralCode(DBManager db, Context contexto) {

        this.DB = db;
        this.CONTEXTO = contexto;

    }

    public String getIdUser() {//obtengo el id del usuario logueado

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
            String contenido = "Error desde android #!#";
            contenido += " Funcion: getIdUser #!#";
            contenido += "Clase : GeneralCode.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return null;
    }


    public void getNameUser(TextView ContentNameUser){//obtener el nombre del usuario

        String idUser = getIdUser();

        final String service = "user/getNameUser.php";
        JSONArray arrayResponse = null;

        parametros = new String[][]{ //array parametros a enviar
                {"user",idUser}
        };


        try {

            BD = new TaskExecuteHttpHandler(service, parametros, CONTEXTO);
            String resultado="";


            resultado = BD.execute().get();


            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                if( idUser.equals( mss.DftUsrId ) )
                    ContentNameUser.setText(mss.DftUsrName);
                else
                    ContentNameUser.setText("");

            }else{

                JSONArray nameUser =  arrayResponse.getJSONArray(0);
                JSONObject index = arrayResponse.getJSONObject(1);

                ContentNameUser.setText(
                        nameUser.getJSONObject(0).getString( index.getString("0")  )+" "+
                        nameUser.getJSONObject(0).getString( index.getString("1")  )
                );

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){

            String contenido = "Error desde android #!#";
            contenido += " Funcion: getNameUser try 1 #!#";
            contenido += "Clase : GeneralCode.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);

        }


    }

    public void ChoseUserDefault(Context activity){
        // custom dialog

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
                        };

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
                        e.printStackTrace();
                        String contenido = "Error desde android #!#";
                        contenido += " Funcion: ChoseUserDefault #!#";
                        contenido += "Clase : GeneralCode.java #!#";
                        contenido += e.getMessage();
                        services.SaveError(contenido);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();//ocultar dialog
                }
            });


            dialog.show();
        }


    }



}
