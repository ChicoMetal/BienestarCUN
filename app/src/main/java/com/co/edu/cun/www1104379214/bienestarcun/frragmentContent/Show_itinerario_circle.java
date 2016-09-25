package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.CircleList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.CirclesApp;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ItinerarioList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.ItemOffsetDecoration;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedItinerarioAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Show_itinerario_circle extends Fragment {


    public static int idCirculo;
    private static DBManager DB;
    ArrayList<ItinerarioList> itinerarios;
    public static final int NUM_COLUMNS = 1;
    private RecyclerView mHypedItinerarioAdapter;
    private HypedItinerarioAdapter adapter;
    public static FragmentManager fragmentManager;
    private static int INSTANCE;
    private CodMessajes mss = new CodMessajes();


    private OnFragmentInteractionListener mListener;


    public static Show_itinerario_circle newInstance(int circulo, DBManager db, int instance, FragmentManager fragments) {
        Show_itinerario_circle fragment = new Show_itinerario_circle();
        Bundle args = new Bundle();
        idCirculo = circulo;
        DB = db;
        fragmentManager = fragments;
        INSTANCE = instance;
        fragment.setArguments(args);
        return fragment;
    }

    public Show_itinerario_circle() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HypedItinerarioAdapter( getActivity(), DB, INSTANCE, idCirculo, fragmentManager );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_show_itinerario_circle, container, false);

        mHypedItinerarioAdapter = (RecyclerView) root.findViewById(R.id.hyper_show_itinerario_circle);

        IconManager icon = new IconManager();
        icon.setBackgroundApp((LinearLayout)root.findViewById(R.id.contentShowItinerarios));

        SetudItinerariosList();

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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void SetudItinerariosList(){

        mHypedItinerarioAdapter.setLayoutManager(
                new GridLayoutManager(getActivity(),
                        NUM_COLUMNS) );

        mHypedItinerarioAdapter.setAdapter(adapter);
        mHypedItinerarioAdapter.addItemDecoration( new ItemOffsetDecoration(
                                                            getActivity().getApplicationContext(),
                                                            R.integer.offset ) );
    }

    private void CasthConentAdapter() throws JSONException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUri.Server+"circles/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CirclesApp actividades = retrofit.create(CirclesApp.class);

        Call<ResponseContent> call = actividades.getItinerariosActivity( idCirculo );

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

    private void ShowCards( JSONArray ItinerariosResult, JSONObject indexCircles){
        //Agregar las cartas de los resultados

        if( ItinerariosResult != null ){

            itinerarios = new ArrayList<>();

            for (int i=0; i < ItinerariosResult.length(); i++){

                try {
                    itinerarios.add(new ItinerarioList(ItinerariosResult.getString(i), indexCircles));
                } catch (JSONException e) {
                    e.printStackTrace();
                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server
                }

            }

            adapter.AddAll( itinerarios );

        }

    }

}
