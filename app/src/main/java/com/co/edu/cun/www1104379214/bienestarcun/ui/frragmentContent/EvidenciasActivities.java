package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;


public class EvidenciasActivities extends Fragment {

    private static int CIRCLE;
    public static int ITINERARIO;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "itinerario";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public static EvidenciasActivities newInstance(int circle1, int itinerario1) {
        EvidenciasActivities fragment = new EvidenciasActivities();
        Bundle args = new Bundle();
        CIRCLE = circle1;
        ITINERARIO = itinerario1;
        args.putString(ARG_PARAM1, itinerario1+"");
        fragment.setArguments(args);
        return fragment;
    }

    public EvidenciasActivities() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_evidencias_activities, container, false);

        IconManager icon = new IconManager();
        icon.setBackgroundApp((FrameLayout)root.findViewById(R.id.contentEvidencias));

        TextView contentId = (TextView) root.findViewById(R.id.ItinerarioId);
        contentId.setText( ITINERARIO+"" );

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

}
