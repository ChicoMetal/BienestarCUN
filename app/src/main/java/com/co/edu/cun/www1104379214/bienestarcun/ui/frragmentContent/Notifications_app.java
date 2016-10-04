package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.Notification;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.Notifications;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.NotificationsList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.ItemOffsetDecoration;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedNotificationsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Notifications_app extends Fragment {

    Notification notificaciones;
    private static DBManager DB;
    ArrayList<NotificationsList> listNotificaciones;

    public static final int NUM_COLUMNS = 1;

    private RecyclerView mHyperdNotificationsList;
    private HypedNotificationsAdapter adapter;

    Constantes mss = new Constantes();

    private OnFragmentInteractionListener mListener;


    public static Notifications_app newInstance(DBManager db, String param2) {
        Notifications_app fragment = new Notifications_app();
        Bundle args = new Bundle();
        DB = db;
        fragment.setArguments(args);
        return fragment;
    }

    public Notifications_app() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HypedNotificationsAdapter(getActivity(), DB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notifications_app, container, false);

        mHyperdNotificationsList = (RecyclerView) root.findViewById(R.id.hyper_notifications_list);
        notificaciones = new Notification( getActivity().getApplicationContext(), DB );

        IconManager icon = new IconManager();
        icon.setBackgroundApp((LinearLayout)root.findViewById(R.id.contentNotifications));

        SetudNotificationList();

        try {

            CasthConentAdapter();//lleno el adaptador

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

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
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

    private void SetudNotificationList(){

        mHyperdNotificationsList.setLayoutManager(
                new GridLayoutManager(getActivity(),
                        NUM_COLUMNS));



        mHyperdNotificationsList.setAdapter(adapter);
        mHyperdNotificationsList.addItemDecoration( new ItemOffsetDecoration( getActivity().getApplicationContext(), R.integer.offset ) );
    }

    //<editor-fold desc="Peticion al server">
    private void CasthConentAdapter() throws JSONException {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.Server+"notifications/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Notifications notificationsResponse = retrofit.create(Notifications.class);

        Call<ResponseContent> call = notificationsResponse.getNotifications( notificaciones.getIdUser() );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                ValidateResponse( data );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "error "+ t.toString());

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
                Log.i(mss.TAG1, data.getBody().toString() );
                ResponseContent showNotifications = notificaciones.ShowNotificationsNew(
                                                        data.getBody() );
                                                        //filtro las notificaciones con las ocultadas

                JSONArray circlesResult = showNotifications.getResults();
                JSONObject indexCircles = showNotifications.getIndex();

                ShowCards(circlesResult, indexCircles);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }
    //</editor-fold>

    //<editor-fold desc="Agregar las cartas de los resultados">
    private void ShowCards( JSONArray notificationsResult, JSONObject indexNotification){


        if( notificationsResult != null ){

            listNotificaciones = new ArrayList<>();

            for (int i=0; i < notificationsResult.length(); i++){

                try {
                    listNotificaciones.add(new NotificationsList(notificationsResult.getString(i), indexNotification));
                } catch (JSONException e) {
                    e.printStackTrace();
                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
                }

            }

            adapter.AddAll( listNotificaciones );

        }

    }
    //</editor-fold>
}
