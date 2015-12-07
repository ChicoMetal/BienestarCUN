package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AsistenciaCircleActivities.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AsistenciaCircleActivities#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AsistenciaCircleActivities extends Fragment {

    IconManager icon = new IconManager();
    private static ItinerariosManager manager;

    private static int CIRCLE;
    public static int ITINERARIO;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_asistencia_circle_activities, container, false);

        manager = new ItinerariosManager(getActivity().getApplicationContext());

        icon.SetIconBtnAsistencias((ImageButton) root.findViewById(R.id.btn_sendAsistencias));
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


}
