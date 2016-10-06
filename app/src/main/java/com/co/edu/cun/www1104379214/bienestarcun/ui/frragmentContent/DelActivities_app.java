package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.CirclesManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.CircleList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.CirclesApp;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.ItemOffsetDecoration;
import com.co.edu.cun.www1104379214.bienestarcun.ui.MainActivity;
import com.co.edu.cun.www1104379214.bienestarcun.ui.Splash;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedActivitiesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class DelActivities_app extends Fragment {

    private static DBManager DB;
    ArrayList<CircleList> activities;
    private RecyclerView mHyperdActivitiesList;
    private HypedActivitiesAdapter adapter;
    private Constantes mss = new Constantes();
    CirclesManager getCircles;
    Splash PDialog = new Splash();

    private OnFragmentInteractionListener mListener;

    public static DelActivities_app newInstance(DBManager db, String param2) {
        DelActivities_app fragment = new DelActivities_app();
        Bundle args = new Bundle();
        DB = db;

        fragment.setArguments(args);
        return fragment;
    }

    public DelActivities_app() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HypedActivitiesAdapter(getActivity(), DB, 1,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_delactivities_app, container, false);

        mHyperdActivitiesList = (RecyclerView) root.findViewById(R.id.hyper_delactivities);
        getCircles = new CirclesManager( getActivity().getApplicationContext(), DB );

        IconManager icon = new IconManager();
        icon.setBackgroundApp((LinearLayout)root.findViewById(R.id.contentActivitiesListDel));

        SetudActivitiesList();

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

    private void SetudActivitiesList(){

        mHyperdActivitiesList.setLayoutManager(
                new GridLayoutManager(getActivity(),
                        MainActivity.NUM_COLUMNS) );


        mHyperdActivitiesList.setAdapter(adapter);
        mHyperdActivitiesList.addItemDecoration( new ItemOffsetDecoration( getActivity().getApplicationContext(), R.integer.offset ) );

    }

    private void CasthConentAdapter() throws JSONException {

        final ProgressDialog pDialog= PDialog.getpDialog(getActivity());
        pDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.Server+"circles/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CirclesApp actividades = retrofit.create(CirclesApp.class);

        Call<ResponseContent> call = actividades.getActivitiesAdd(getCircles.getIdUser());

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                ValidateResponse( data );

                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG1, "error "+ t.toString());

                pDialog.dismiss();

            }
        });
    }

    private void ValidateResponse(ResponseContent data) {
        //procesa la respuesta enviada del server

        try {

            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                Toast.makeText(getActivity().getApplicationContext(),
                        mss.msmServices.getString(data.getBody().getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else{

                JSONArray circlesResult = data.getResults();
                JSONObject indexCircles = data.getIndex();

                ShowCards(circlesResult, indexCircles);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }

    private void ShowCards( JSONArray circlesResult, JSONObject indexCircles){
        //Agregar las cartas de los resultados

        if( circlesResult != null ){

            activities = new ArrayList<>();

            for (int i=0; i < circlesResult.length(); i++){

                try {
                    activities.add( new CircleList( circlesResult.getString(i), indexCircles ) );
                } catch (JSONException e) {
                    e.printStackTrace();
                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
                }

            }

            adapter.AddAll( activities );

        }

    }


}
