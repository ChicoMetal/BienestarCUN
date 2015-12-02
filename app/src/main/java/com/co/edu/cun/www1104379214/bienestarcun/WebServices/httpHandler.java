package com.co.edu.cun.www1104379214.bienestarcun.WebServices;

import android.app.Activity;
import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
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
 * Created by Krlos guzman on 05/10/2015.
 */
public class httpHandler {

    public static final String modo = "POST";
    public static final String insert = "insert";
    public static final String search = "search";

    CodMessajes messajes = new CodMessajes();

    private Activity activity;

    private static final String Server = "http://10.0.3.2/BienestarCun/core/android/";
    //private static final String Server = "http://192.168.1.107/BienestarCun/core/android/";
    //private static final String Server = "http://ottsincelejo.com/carlos/BienestarCun/core/android/";
    //private static final String Server = "bienestarcun.webcindario.com/core/android/";

/*
    //Metodo para realizar peticiones al server
    public String BuscarBD1( final String service, final String[][] parametros) throws InterruptedException {

        final String[] result = new String[1];

        Thread nt = new Thread(){

            String res;

            @Override
            public void run() {

                try {

                    final String valor;
                    final HttpClient httpClient = new DefaultHttpClient();
                    final HttpContext localContext = new BasicHttpContext();
                    final HttpPost httpPost = new HttpPost(Server + service);

                    List<NameValuePair> params = new ArrayList<NameValuePair>(3);

                    for(int c = 0; c < parametros.length; c++ ){

                        params.add(
                                new BasicNameValuePair(
                                        parametros[c][0],
                                        parametros[c][1]
                                )
                        );
                    }

                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    final HttpResponse response = httpClient.execute(httpPost, localContext);

                    HttpEntity res1 = response.getEntity();
                    res = EntityUtils.toString(res1);
                    result[0] = res;

                } catch (Exception e) {

                    result[0] = messajes.ErrorServicesPeticion;

                }


            }

        };


        synchronized (nt) {
            nt.start();
            nt.join();
        }

        return result[0];

    }


    public String GuardarBD1( final String[][] campos, final String services ) throws InterruptedException {

        final String[] result = new String[1];

        Thread nt = new Thread() {
            String res;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost( Server + services);
            HttpResponse response = null;

            @Override
            public void run() {

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
                    res = EntityUtils.toString(res1);

                    result[0] = res;


                }catch(Exception e){
                    result[0] = messajes.ErrorServicesPeticion;
                }

            }

        };

        synchronized (nt) {
            nt.start();
            nt.join();
        }

        return result[0];

    }

    public String HttpRequestServer( final String services, final String[][] campos ) throws InterruptedException {

        final String[] result = new String[1];

        Thread nt = new Thread() {
            String res;

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost( Server + services);
            HttpResponse response = null;

            @Override
            public void run() {

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
                    res = EntityUtils.toString(res1);

                    result[0] = res;


                }catch(Exception e){
                    result[0] = messajes.ErrorServicesPeticion;
                }

            }

        };

        synchronized (nt) {
            nt.start();
            nt.join(4000);//espera maximo 4 segundos
        }

        return result[0];

    }
*/
}
