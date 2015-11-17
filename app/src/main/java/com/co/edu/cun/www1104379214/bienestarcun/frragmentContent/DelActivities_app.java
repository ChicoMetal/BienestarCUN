package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.CirclesManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.CircleList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedActivitiesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DelActivities_app.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DelActivities_app#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DelActivities_app extends Fragment {

    private static DBManager DB;
    ArrayList<CircleList> activities;


    public static final String LOG_TAG = Activities_app.class.getName();
    public static final int NUM_COLUMNS = 1;

    private RecyclerView mHyperdActivitiesList;
    private HypedActivitiesAdapter adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment Itinerario.
     */
    // TODO: Rename and change types and number of parameters
    public static DelActivities_app newInstance(DBManager db, String param2) {
        DelActivities_app fragment = new DelActivities_app();
        Bundle args = new Bundle();
        DB = db;
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DelActivities_app() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam2 = getArguments().getString(ARG_PARAM2);


        }
        adapter = new HypedActivitiesAdapter(getActivity(), DB, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_delactivities_app, container, false);

        mHyperdActivitiesList = (RecyclerView) root.findViewById(R.id.hyper_activities_itinerario);

        SetudActivitiesList();

        try {

            CasthConentAdapter();//lleno el adaptador

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: onCreateView #!#";
            contenido += "Clase : Itinerario.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion().SaveError(contenido);
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

    private void SetudActivitiesList(){

        mHyperdActivitiesList.setLayoutManager(
                new GridLayoutManager(getActivity(),
                        NUM_COLUMNS) );



        mHyperdActivitiesList.setAdapter(adapter);
    }

    private void CasthConentAdapter() throws JSONException {

        CirclesManager getCircles = new CirclesManager( getActivity().getApplicationContext(), DB );//busco en BD los circulos existentes

        JSONArray circlesResult = getCircles.SearchCircles(1);
        JSONObject indexCircles = getCircles.IndexCircles();

        if( circlesResult != null ){

            ArrayList<CircleList> circles = new ArrayList<>();

            for (int i=0; i < circlesResult.length(); i++){

                circles.add( new CircleList( circlesResult.getString(i), indexCircles ) );

            }

            adapter.AddAll(circles);

        }
    }

}