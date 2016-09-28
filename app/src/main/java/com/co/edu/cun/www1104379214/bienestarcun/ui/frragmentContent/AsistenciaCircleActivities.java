package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;


public class AsistenciaCircleActivities extends Fragment {

    IconManager icon = new IconManager();
    private static ItinerariosManager manager;

    private static int CIRCLE;
    public static int ITINERARIO;


    private OnFragmentInteractionListener mListener;



    public static AsistenciaCircleActivities newInstance(int circle1, int itinerario1) {
        AsistenciaCircleActivities fragment = new AsistenciaCircleActivities();
        Bundle args = new Bundle();

        CIRCLE = circle1;
        ITINERARIO = itinerario1;

        fragment.setArguments(args);
        return fragment;
    }

    public AsistenciaCircleActivities() {
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
        View root = inflater.inflate(R.layout.fragment_asistencia_circle_activities, container, false);

        manager = new ItinerariosManager(getActivity().getApplicationContext());

        icon.setBackgroundApp((FrameLayout) root.findViewById(R.id.ContentAsistencias));

        manager.SearchListInscritos(
                (LinearLayout) root.findViewById(R.id.contentListAsistentes),
                CIRCLE);//buscar y generar listado de inscritos

        TextView contentId = (TextView) root.findViewById(R.id.idItinerario);
        contentId.setText( ITINERARIO+"" );

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


}
