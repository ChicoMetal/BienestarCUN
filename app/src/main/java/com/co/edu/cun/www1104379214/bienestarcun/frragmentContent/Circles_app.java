package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.CirclesManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.CircleList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.ItemOffsetDecoration;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedActivitiesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Circles_app.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Circles_app#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Circles_app extends Fragment {

    private static DBManager DB;
    ArrayList<CircleList> activities;

    public static final int NUM_COLUMNS = 1;



    private RecyclerView mHyperdActivitiesList;
    private HypedActivitiesAdapter adapter;
    private CodMessajes mss = new CodMessajes();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment Activities_app.
     */
    // TODO: Rename and change types and number of parameters
    public static Circles_app newInstance(DBManager db, String param2) {
        Circles_app fragment = new Circles_app();
        Bundle args = new Bundle();
        DB = db;
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Circles_app() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        adapter = new HypedActivitiesAdapter(getActivity(), DB, 0,null);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_activities_app, container, false);

        mHyperdActivitiesList = (RecyclerView) root.findViewById(R.id.hyper_activities_list);
        IconManager icon = new IconManager();
        icon.setBackgroundApp((LinearLayout)root.findViewById(R.id.contentActivitiesList));


        new InterfaceNoBlock().execute();


        return root;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private void SetudActivitiesList(){

        mHyperdActivitiesList.setLayoutManager(
                new GridLayoutManager(getActivity(),
                        NUM_COLUMNS) );



        mHyperdActivitiesList.setAdapter(adapter);

        mHyperdActivitiesList.addItemDecoration( new ItemOffsetDecoration( getActivity().getApplicationContext(), R.integer.offset ) );
    }

    private void CasthConentAdapter(ProgressDialog pdialog) throws JSONException {

        CirclesManager getCircles = new CirclesManager( getActivity().getApplicationContext(), DB );//busco en BD los circulos existentes

        JSONArray circlesResult = getCircles.SearchCircles(0, pdialog);
        JSONObject indexCircles = getCircles.IndexCircles();

        if( circlesResult != null ){

            ArrayList<CircleList> circles = new ArrayList<>();

            for (int i=0; i < circlesResult.length(); i++){

                circles.add( new CircleList( circlesResult.getString(i), indexCircles ) );

            }

            adapter.AddAll(circles);

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

            SetudActivitiesList();

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
                contenido += "Clase : Activities_app.java #!#";
                contenido += e.getMessage();
                new ServicesPeticion(getActivity().getApplicationContext()).SaveError(contenido);
            }


        }
    }


}
