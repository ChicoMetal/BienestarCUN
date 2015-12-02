package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/12/15.
 */
public class TaskExecuteHttpHandler extends AsyncTask<Void, Void, String> {

    public static final String modo = "POST";
    public static final String insert = "insert";
    public static final String search = "search";

    CodMessajes messajes = new CodMessajes();

    private Activity activity;

    private static final String Server = "http://10.0.3.2/BienestarCun/core/android/";
    //private static final String Server = "http://192.168.1.107/BienestarCun/core/android/";
    //private static final String Server = "http://ottsincelejo.com/carlos/BienestarCun/core/android/";
    //private static final String Server = "bienestarcun.webcindario.com/core/android/";


    //private Context CONTEXT;
    private String SERVICE;
    private String[][] CAMPOS;
    private String result;

    public TaskExecuteHttpHandler(String service,//nombre del servicio
                                  String[][] campos
                                //Context contesto//contexto
    ) {

        //this.CONTEXT = contesto;
        this.SERVICE = service;
        this.CAMPOS = campos;

    }

    @Override
    protected void onPreExecute() {

        //Toast.makeText(CONTEXT.getApplicationContext(), "Procesando...", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            result = HttpRequestServer(SERVICE, CAMPOS);

        } catch (InterruptedException e) {

            e.printStackTrace();

            /*Toast.makeText(CONTEXT.getApplicationContext(),
                    "Error en la peticion al server",
                    Toast.LENGTH_SHORT).show();*/
        }

        return result;

    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

    public String HttpRequestServer( final String services, final String[][] campos ) throws InterruptedException {

        String result = null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost( Server + services);
        HttpResponse response = null;

        try{

            List<NameValuePair> params = new ArrayList<NameValuePair>(3);

            for(int c = 0; c < campos.length; c++ ){

                params.add(
                        new BasicNameValuePair(
                                campos[c][0],
                                campos[c][1]
                        )
                );
            }

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httpPost, localContext);
            HttpEntity res1 = response.getEntity();
            result = EntityUtils.toString(res1);

        }catch(Exception e){
            result = messajes.ErrorServicesPeticion;
        }

        return result;
    }


}
