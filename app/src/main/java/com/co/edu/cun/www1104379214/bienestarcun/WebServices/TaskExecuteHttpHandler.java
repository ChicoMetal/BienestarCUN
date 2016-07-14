package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/12/15.
 */
public class TaskExecuteHttpHandler extends AsyncTask<Void, Void, String> {

    ProgressDialog pDialog;

    CodMessajes messajes = new CodMessajes();

    private final String Server = ServerUri.Server;
    private String SERVICE;
    private String[][] CAMPOS;
    private Context CONTEXTO;
    private String result;

    public TaskExecuteHttpHandler(String service,//nombre del servicio
                                  String[][] campos,
                                Context contesto//contexto
    ) {


        this.SERVICE = service;
        this.CAMPOS = campos;
        this.CONTEXTO = contesto;

    }

    @Override
    protected void onPreExecute() {

        /*pDialog = new ProgressDialog( CONTEXTO );
        pDialog.setMessage("Enviando al servidor, espere...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();*/

    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            result = HttpRequestServer(SERVICE, CAMPOS);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        return result;

    }


    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
       // pDialog.dismiss();

    }

    public String HttpRequestServer( final String services, final String[][] campos ) throws InterruptedException {

        String result = null;

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 10000);

        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost( Server + services);
        httpPost.setHeader("charset", "utf-8");
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
            //HttpEntity res1 = response.getEntity();
            //result = EntityUtils.toString(res1, "UTF-8");

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() >= 300) {
                /*throw new HttpResponseException(statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());*/

                return messajes.ErrorServerPeticion;
            }

            HttpEntity entity = response.getEntity();
            return entity == null ? null : EntityUtils.toString(entity, "UTF-8");

        }catch(Exception e){
            result = messajes.ErrorServicesPeticion;
        }

        return result;
    }


}
