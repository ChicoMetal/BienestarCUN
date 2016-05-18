package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.Activity;
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


import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.TaskExecuteHttpHandler;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatPsicologa_app.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatPsicologa_app#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatPsicologa_app extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //TODO: Crear las coneciones para que no se presenten problemas en multiples conversaciones del psicologo

    // TODO: Rename and change types of parameters
    private static long mRemitente;
    private static long mReceptor;

    public TextView TVRemitente, TVReceptor;
    LinearLayout ContentChat;
    ImageButton btn;
    EditText mensaje;


    //inicializacion necesarias para comunicacion con node
    TaskExecuteHttpHandler BD;
    Context CONTEXTO;
    private Handler mUiHandler = new Handler();
    private MyWorkerThread mWorkerThread;
    JSONObject MensajeEntrante;
    JSONArray arrayresult;
    JSONObject indexResult;

    Socket socket;
    private final static String EVENT_SEND_IDSOCKET = "saveIdSocket";
    private final static String EVENT_SEND_GET_MESSAGE = "new message";



    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatPsicologa_app.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatPsicologa_app newInstance(long receptor, long remitente) {
        ChatPsicologa_app fragment = new ChatPsicologa_app();

        mRemitente = remitente;
        mReceptor = receptor;

        return fragment;
    }

    @Override
    public void onResume() {

        super.onResume();
        if( socket.connected() ){
            socket.disconnect();
        }

        socket.connect();

    }

    public ChatPsicologa_app() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat_psicologa_app, container, false);

        TVReceptor = (TextView) root.findViewById(R.id.TVRReceptor);
        TVRemitente = (TextView) root.findViewById(R.id.TVRemitente);
        ContentChat = (LinearLayout) root.findViewById(R.id.SVContentMsm);
        btn = (ImageButton) root.findViewById(R.id.btn_sendMsm);
        mensaje = (EditText) root.findViewById(R.id.etMensajePsicologia);


        IconManager icon = new IconManager();
        icon.SetIconBtnAsistencias((ImageButton) root.findViewById(R.id.btn_sendMsm));
        icon.setBackgroundApp((LinearLayout)root.findViewById(R.id.contentChat));

        btn.setOnClickListener(this);

        CONTEXTO = root.getContext();

        IO.Options opts = new IO.Options();

        opts.forceNew = false;
        opts.reconnection = true;
        try{
            socket = IO.socket(ServerUri.URL_SOCKET, opts);
        }catch (URISyntaxException e){
            e.printStackTrace();
        }

        if( socket.connected() ){
            socket.disconnect();
        }

        socket.connect();

        mWorkerThread = new MyWorkerThread("myWorkerThread");
        mWorkerThread.start();


        socket.on(EVENT_SEND_GET_MESSAGE, new Emitter.Listener(){

            @Override
            public void call(Object... args) {

                MensajeEntrante = (JSONObject) args[0];
                String msm = null;
                try {
                    msm = MensajeEntrante.getString("Mensaje");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if ( !msm.equals("") ){

                    mWorkerThread.prepareHandler();
                    mWorkerThread.postTask(task);
                    //Log.i("RESPONSE", args[0].toString());

                }
            }
        });

        socket.on(EVENT_SEND_IDSOCKET, new Emitter.Listener(){

            @Override
            public void call(Object... args) {

                JSONArray resultMensajes;
                try {

                    final String service = "chatPsicologia/getMensajes.php";

                    resultMensajes = GetMensajesPendientesExists(service, mRemitente+"", mReceptor+"","");

                    if (resultMensajes != null) {//si trae mensajes de vuelta

                        arrayresult = resultMensajes.getJSONArray(0);
                        indexResult = resultMensajes.getJSONObject(1);

                        mWorkerThread.prepareHandler();
                        mWorkerThread.postTask(task2);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    String contenido = "Error desde android #!#";
                    contenido += " Funcion: AddMesagesChat #!#";
                    contenido += "Clase : ChatPsicologiaManager.java #!#";
                    contenido += e.getMessage();
                    new ServicesPeticion(CONTEXTO).SaveError(contenido);
                }

            }
        });





        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){

            public void call(Object... args) {

                JSONObject newConversasion = new JSONObject();
                try{//objeto con la informacion de la conversasion iniciada

                    String socketId = socket.id().toString();

                    newConversasion.put("remitente", mRemitente);
                    newConversasion.put("receptor", mReceptor);
                    newConversasion.put("socket", socketId);

                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }

                // Emit event
                socket.emit(EVENT_SEND_IDSOCKET, newConversasion);//envio la informasion al server para guardar el socket

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){

            public void call(Object... args) {
                mWorkerThread.quit();
                //socket.disconnect();
            }

        });

        return root;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        socket.connect();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        String msm =  mensaje.getText().toString();

        JSONObject sendMensaje = new JSONObject();
        try{//objeto con la informacion de la conversasion iniciada
            sendMensaje.put("Mensaje", msm);
            sendMensaje.put("Remitente", mRemitente);
            sendMensaje.put("Destinatario", mReceptor);

        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
        }


        socket.emit(EVENT_SEND_GET_MESSAGE, sendMensaje);//envio la informasion al server para guardar el socket
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    final Runnable task = new Runnable() {//hilo para mensajes que salen y entran en una conversacion

        @Override
        public void run() {

            mUiHandler.post(new Runnable() {
                @Override
                public void run(){
                    try {

                        TextView msmContent;

                        if( MensajeEntrante.getString("Remitente").equals(mRemitente+"") ){
                            msmContent = GenerarTextView(0, MensajeEntrante.getString("Mensaje") );
                        }else{
                            msmContent = GenerarTextView(1, MensajeEntrante.getString("Mensaje") );

                        }

                        ContentChat.addView( msmContent );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


        }
    };

    final Runnable task2 = new Runnable() {//obtener mensajes sin leer

        @Override
        public void run() {

            mUiHandler.post(new Runnable() {
                @Override
                public void run(){
                    try {


                        for( int c = 0; c < arrayresult.length(); c++){//itero por cada mensaje

                            TextView msmContent;

                            if( arrayresult.getJSONObject(c).getLong( indexResult.getString("0") ) == mRemitente ){
                               msmContent = GenerarTextView(0, arrayresult.getJSONObject(c).getString( indexResult.getString("1") ) );
                            }else{
                               msmContent = GenerarTextView(1, arrayresult.getJSONObject(c).getString( indexResult.getString("1") )  );
                            }

                            ContentChat.addView( msmContent );

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


        }
    };


    private TextView GenerarTextView(int origen, String mensaje){//generar textview

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
        texto.setTextSize(15);
        texto.setText(mensaje);

        return texto;

    }

    public JSONArray GetMensajesPendientesExists(String service, String remitente, String receptor, String mensaje) throws InterruptedException {
        //obtengo el array de objeto con los mensajes

        String[][] values = null;


        JSONArray arrayResponse = null;

        values = new String[][]{ //array parametros a enviar
                {"remitente",remitente},
                {"receptor",receptor},
                {"mensaje",mensaje}
        };

        BD = new TaskExecuteHttpHandler(service, values,CONTEXTO);
        String resultado="";
        try {
            resultado = BD.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetMensajesPendientesExists #!#";
            contenido += "Clase : ChatPsicologa_app.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        try {

            arrayResponse = new JSONArray( resultado ); // obtengo el array con la result del server

            if( arrayResponse.getString(0).toString().equals("msm")  ){

                return null;

            }else {

                return arrayResponse;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: GetMensajesPendientesExists #!#";
            contenido += "Clase : ChatPsicologiaManager.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(CONTEXTO).SaveError(contenido);
        }

        return arrayResponse;

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
