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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ItinerarioList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.ItemOffsetDecoration;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedItinerarioAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Show_itinerario_circle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static int idCirculo;
    private static DBManager DB;
    ArrayList<ItinerarioList> itinerarios;
    public static final int NUM_COLUMNS = 1;
    private RecyclerView mHypedItinerarioAdapter;
    private HypedItinerarioAdapter adapter;
    public static FragmentManager fragmentManager;
    private static int INSTANCE;
    private CodMessajes mss = new CodMessajes();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        new InterfaceNoBlock().execute();

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
        mHypedItinerarioAdapter.addItemDecoration( new ItemOffsetDecoration( getActivity().getApplicationContext(), R.integer.offset ) );
    }

    private void CasthConentAdapter(ProgressDialog pdialog) throws JSONException {

        ItinerariosManager getItinerarios = new ItinerariosManager( getActivity().getApplicationContext() );//busco en BD los circulos existentes

        JSONArray ItinerariosResult = getItinerarios.SearchItinerarios(idCirculo, pdialog);
        JSONObject indexItinerarios = getItinerarios.IndexItinerario();

        if( ItinerariosResult != null ){

            ArrayList<ItinerarioList> itinerarios = new ArrayList<>();

            for (int i=0; i < ItinerariosResult.length(); i++){

                itinerarios.add(new ItinerarioList(ItinerariosResult.getString(i), indexItinerarios));

            }

            adapter.AddAll(itinerarios);

        }
    }

    public class InterfaceNoBlock extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        int a = 1;
        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog( getActivity() );
            pDialog.setMessage("Un momento...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.setProgress(0);
            pDialog.show();

            SetudItinerariosList();

        }


        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep (mss.TiempoEsperaTask);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            //pDialog.dismiss();

            try {

                CasthConentAdapter(pDialog);//lleno el adaptador

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
                String contenido = "Error desde android #!#";
                contenido += " Funcion: onCreateView #!#";
                contenido += "Clase : Itinerarios_app.java #!#";
                contenido += e.getMessage();
                new ServicesPeticion(getActivity().getApplicationContext()).SaveError(contenido);
            }


        }
    }

}
