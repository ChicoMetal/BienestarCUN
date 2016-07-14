package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.Activity;
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
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.CircleNotificationsManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.NotificationsList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.ItemOffsetDecoration;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedNotificationsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Notifications_app.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Notifications_app#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notifications_app extends Fragment {

    private static DBManager DB;
    ArrayList<NotificationsList> activities;

    public static final int NUM_COLUMNS = 1;

    private RecyclerView mHyperdNotificationsList;
    private HypedNotificationsAdapter adapter;

    CodMessajes mss = new CodMessajes();


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
     * @return A new instance of fragment Notifications_app.
     */
    // TODO: Rename and change types and number of parameters
    public static Notifications_app newInstance(DBManager db, String param2) {
        Notifications_app fragment = new Notifications_app();
        Bundle args = new Bundle();
        DB = db;
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Notifications_app() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        adapter = new HypedNotificationsAdapter(getActivity(), DB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notifications_app, container, false);

        mHyperdNotificationsList = (RecyclerView) root.findViewById(R.id.hyper_notifications_list);

        IconManager icon = new IconManager();
        icon.setBackgroundApp((LinearLayout)root.findViewById(R.id.contentNotifications));

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

    private void SetudNotificationList(){

        mHyperdNotificationsList.setLayoutManager(
                new GridLayoutManager(getActivity(),
                        NUM_COLUMNS));



        mHyperdNotificationsList.setAdapter(adapter);
        mHyperdNotificationsList.addItemDecoration( new ItemOffsetDecoration( getActivity().getApplicationContext(), R.integer.offset ) );
    }

    private void CasthConentAdapter() throws JSONException {

        CircleNotificationsManager getNotifications = new CircleNotificationsManager( getActivity().getApplicationContext(), DB,mss.tipeNotification[1] );//busco en BD los circulos existentes

        JSONArray notificationsResult = getNotifications.getResultResponse();
        JSONObject indexCircles = getNotifications.IndexNotifications();

        if( notificationsResult != null ){

            ArrayList<NotificationsList> notifications = new ArrayList<>();

            for (int i=0; i < notificationsResult.length(); i++){

                notifications.add(new NotificationsList(notificationsResult.getString(i), indexCircles));

            }

            adapter.AddAll(notifications);

        }
    }

    public class InterfaceNoBlock extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        int a = 1;
        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog( getActivity() );
            pDialog.setMessage("Un momento...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();

            SetudNotificationList();

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

            pDialog.dismiss();

            try {

                CasthConentAdapter();//lleno el adaptador

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
                String contenido = "Error desde android #!#";
                contenido += " Funcion: onCreateView #!#";
                contenido += "Clase : Notifications_app.java #!#";
                contenido += e.getMessage();
                new ServicesPeticion(getActivity().getApplicationContext()).SaveError(contenido);
            }


        }
    }

}
