package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.NewItinerarioManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;


public class CircleAdministration_app extends Fragment implements View.OnClickListener {



    private OnFragmentInteractionListener mListener;
    NewItinerarioManager newItinerario;
    static DBManager DB;

    EditText nameActiviti, detailsActiviti;
    DatePicker fecha;
    TimePicker hora;
    ImageButton btn;


    public static CircleAdministration_app newInstance( DBManager db ) {
        DB = db;
        CircleAdministration_app fragment = new CircleAdministration_app();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CircleAdministration_app() {
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
        View root = inflater.inflate(R.layout.fragment_circle_administration_app, container, false);

        btn = (ImageButton) root.findViewById(R.id.btnAddNewItinerario);

        nameActiviti = (EditText) root.findViewById( R.id.txtNameActivitieNew);
        detailsActiviti = (EditText) root.findViewById( R.id.txtDetailNewActiviti);
        fecha = (DatePicker) root.findViewById( R.id.pickerFechaNewItinerario);
        hora = (TimePicker) root.findViewById( R.id.pickerHoraNewItinerario);
        btn.setOnClickListener(this);

        IconManager icon = new IconManager();
        icon.setBackgroundApp((FrameLayout)root.findViewById(R.id.contentCircleAdmin));


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

    @Override
    public void onClick(View v) {

        if( newItinerario == null)
            newItinerario = new NewItinerarioManager(DB, getActivity().getApplicationContext() );

        newItinerario.SaveNewItinerario(nameActiviti, detailsActiviti, fecha, hora);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
