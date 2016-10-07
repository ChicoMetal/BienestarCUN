package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ChatPsicologiaManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.ChatPsicologia;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatPsicologa_app extends Fragment implements View.OnClickListener {

    private static long mRemitente;
    private static long mReceptor;

    public TextView TVRemitente, TVReceptor;
    LinearLayout ContentChat;
    ImageButton btn;
    EditText mensaje;

    Context CONTEXTO;
    private Constantes mss = new Constantes();
    ChatPsicologiaManager ChatCodeInflateNull;
    static DBManager DB;
    static Retrofit retrofit;
    static ChatPsicologia chatsPsicologia;
    private Handler mUiHandler = new Handler();
    private MyWorkerThread mWorkerThread;
    JSONObject MensajeEntrante;

    Socket socket;


    public static ChatPsicologa_app newInstance(DBManager db, Long receptor) {

        ChatPsicologa_app fragment = new ChatPsicologa_app();
        DB = db;

        if( receptor != null )
            mReceptor = receptor;


        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat_psicologa_app, container, false);

        TVReceptor = (TextView) root.findViewById(R.id.TVRReceptor);
        TVRemitente = (TextView) root.findViewById(R.id.TVRemitente);
        ContentChat = (LinearLayout) root.findViewById(R.id.SVContentMsm);
        btn = (ImageButton) root.findViewById(R.id.btn_sendMsm);
        mensaje = (EditText) root.findViewById(R.id.etMensajePsicologia);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();//asisgnar tiempo de espera a la peticion

        retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_CHAT )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        chatsPsicologia = retrofit.create( ChatPsicologia.class );

        ChatCodeInflateNull = new ChatPsicologiaManager(
                getActivity().getApplicationContext(), DB );

        mRemitente = ChatCodeInflateNull.getIdUser();

        if( ! ChatCodeInflateNull.ComproveUser() ){//dependiendo si el usuario es psicologo o no

            getIdPsicologiaUser();

        }else{
            StartListenNode();
        }


        IconManager icon = new IconManager();
        icon.setBackgroundApp((LinearLayout)root.findViewById(R.id.contentChat));

        btn.setOnClickListener(this);

        CONTEXTO = root.getContext();

        return root;

    }


    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onClick(View view) {
        String msm =  mensaje.getText().toString();
        JSONObject sendMensaje = new JSONObject();

        if( mReceptor != 0 ){

            try{//objeto con la informacion de la conversasion iniciada
                sendMensaje.put("Mensaje", msm);
                sendMensaje.put("Remitente", mRemitente+"");
                sendMensaje.put("Destinatario", mReceptor+"");

            }catch(Exception e){
                Log.i("ERROR", e.getMessage());
            }

            socket.emit(Constantes.EVENT_SEND_GET_MESSAGE, sendMensaje);//envio la informasion al server para guardar el socket

            mensaje.setText("");

        }
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    //<editor-fold desc="busco el id del usuario encargado del area de psicologia">
    public void getIdPsicologiaUser() {

        Call<ResponseContent> call = chatsPsicologia.getPsicologiaUser( mRemitente );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                try {

                    if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                        Toast.makeText(getActivity().getApplicationContext(),
                                mss.msmServices.getString(data.getBody().getString(1).toString()),
                                Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

                    }else{

                        JSONObject results = data.getResults().getJSONObject(0);
                        JSONObject index = data.getIndex();

                        mReceptor = results.getLong( index.getString("0") );

                        StartListenNode();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
                }

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "Error "+ t.toString());

            }

        });

    }
    //</editor-fold>

    //<editor-fold desc="hilo para mensajes que salen y entran en una conversacion">
    final Runnable task = new Runnable() {

        @Override
        public void run() {

            mUiHandler.post(new Runnable() {
                @Override
                public void run(){
                    try {

                        TextView msmContent;

                        if( MensajeEntrante.getString("Remitente").equals(mRemitente+"") ){

                            msmContent = GenerarTextView(Constantes.INDICADOR_MENSAJE_REMITENTE,
                                                            MensajeEntrante.getString("Mensaje") );

                        }else{

                            msmContent = GenerarTextView(Constantes.INDICADOR_MENSAJE_RECEPTOR,
                                                            MensajeEntrante.getString("Mensaje") );

                        }

                        ContentChat.addView( msmContent );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


        }
    };
    //</editor-fold>

    //<editor-fold desc="generar textview">
    private TextView GenerarTextView(int origen, String mensaje){

        TextView texto = new TextView(CONTEXTO);
        if( origen == 0){

            texto.setBackgroundResource(R.color.accent_transparency_remitente);
            texto.setGravity(Gravity.LEFT);

        }else{
            texto.setBackgroundResource(R.color.accent_transparency_receptor);
            texto.setGravity(Gravity.RIGHT);
        }

        texto.setTextColor(CONTEXTO.getResources().getColor(R.color.textBlack));
        texto.setPadding(30, 5, 30, 5);
        texto.setTextSize(20);
        texto.setText(mensaje);

        return texto;

    }
    //</editor-fold>

    //<editor-fold desc="obtengo el array de objeto con los mensajes">
    public void GetMensajesPendientesExists( String remitente, String receptor) throws InterruptedException {

        Call<ResponseContent> call = chatsPsicologia.getMensajesPendientes( remitente, receptor );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                ValidateResponse( data );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "Error "+ t.toString());

            }

        });
    }
    //</editor-fold>

    //<editor-fold desc="procesa la respuesta enviada del server">
    private void ValidateResponse(ResponseContent data) {

        try {

            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                Toast.makeText(getActivity().getApplicationContext(),
                        mss.msmServices.getString(data.getBody().getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else{

                JSONArray circlesResult = data.getResults();
                JSONObject indexMensajes = data.getIndex();

                for( int c = 0; c < circlesResult.length(); c++){//itero por cada mensaje

                    TextView msmContent;

                    if( circlesResult.getJSONObject(c)
                            .getLong( indexMensajes.getString("0") ) == mRemitente ){

                        msmContent = GenerarTextView(Constantes.INDICADOR_MENSAJE_REMITENTE,
                                            circlesResult.getJSONObject(c)
                                                .getString( indexMensajes.getString("1") ) );
                    }else{
                        msmContent = GenerarTextView(Constantes.INDICADOR_MENSAJE_RECEPTOR,
                                                circlesResult.getJSONObject(c)
                                                    .getString( indexMensajes.getString("1") )  );
                    }

                    ContentChat.addView( msmContent );


                }

                UpdateStatusMsmPendientes();


            }

        } catch (JSONException e) {
            e.printStackTrace();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }
    //</editor-fold>

    //<editor-fold desc="Actualizar el estado de los mensajes pendientes ya entregados">
    private void UpdateStatusMsmPendientes(){
        Call<ResponseContent> call = chatsPsicologia.setMensajesPendientes( mRemitente, mReceptor );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                Log.i(  mss.TAG1, data.getBody().toString() );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "Error "+ t.toString());

            }

        });
    }
    //</editor-fold>

    //<editor-fold desc="Una vez se tenga el remitente y el receptor se crea la conexion con node">
    private void StartListenNode(){


        try{

            GetMensajesPendientesExists( mRemitente+"", mReceptor+"");

            IO.Options opts = new IO.Options();

            opts.forceNew = false;
            opts.reconnection = true;

            socket = IO.socket(ServerUri.URL_SOCKET, opts);

            socket.connect();

            mWorkerThread = new MyWorkerThread("myWorkerThread");
            mWorkerThread.start();

        }catch (URISyntaxException e){
            e.printStackTrace();
        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }


        //<editor-fold desc="evento escuchar los nuevos mensajes">
        socket.on(Constantes.EVENT_SEND_GET_MESSAGE, new Emitter.Listener(){

            @Override
            public void call(Object... args) {

                MensajeEntrante = (JSONObject) args[0];
                String msm = null;
                try {
                    msm = MensajeEntrante.getString("Mensaje");

                    if ( !msm.equals("") ){

                        mWorkerThread.prepareHandler();
                        mWorkerThread.postTask(task);
                        //Users.i("RESPONSE", args[0].toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
                }

            }
        });
        //</editor-fold>

        //<editor-fold desc="evento identificar la conexion en el servidor">
        socket.on(Constantes.EVENT_SEND_IDSOCKET, new Emitter.Listener(){

            @Override
            public void call(Object... args) {

                Log.i( mss.TAG1, "Se ha guardado el socket de la conexion en node" );

            }
        });
        //</editor-fold>

        //<editor-fold desc="eventos de conexion y desconexion">
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){


            public void call(Object... args) {

                JSONObject newConversasion = new JSONObject();
                try{//objeto con la informacion de la conversasion iniciada

                    String socketId = socket.id().toString();

                    newConversasion.put("remitente", mRemitente+"");
                    newConversasion.put("receptor", mReceptor+"");
                    newConversasion.put("socket", socketId);

                    // Emit event
                    socket.emit(Constantes.EVENT_SEND_IDSOCKET, newConversasion);//envio la informasion al server para guardar el socket

                }catch(Exception e){
                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
                }



            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){

            public void call(Object... args) {
                mWorkerThread.quit();
                //socket.disconnect();
            }

        });
        //</editor-fold>

    }
    //</editor-fold>

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        socket.disconnect();
    }


}


class MyWorkerThread extends HandlerThread {

    private Handler mWorkerHandler;


    public MyWorkerThread(String name) {
        super(name);

    }

    public void postTask(Runnable task){
        mWorkerHandler.post(task);

    }

    public void prepareHandler(){
        mWorkerHandler = new Handler(getLooper());

    }

}
