package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.LaboralAdd;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;


public class HistoryLaboral_app extends Fragment implements View.OnClickListener{

    LaboralAdd newlaboral;
    static DBManager DB;

    EditText nameEmpresa, cargoEmpresa;
    DatePicker fechaStart, fechaEnd;
    CheckBox working;
    ImageButton btn;


    private OnFragmentInteractionListener mListener;

    public static HistoryLaboral_app newInstance( DBManager db ) {
        DB = db;
        HistoryLaboral_app fragment = new HistoryLaboral_app();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryLaboral_app() {
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
        View root = inflater.inflate(R.layout.fragment_history_laboral_app, container, false);
        newlaboral = new LaboralAdd(DB, getActivity().getApplicationContext() );

        working = (CheckBox) root.findViewById( R.id.chk_continua_trabajando);
        fechaEnd = (DatePicker) root.findViewById( R.id.pickerFechaLaboralEnd );
        nameEmpresa = (EditText) root.findViewById( R.id.txt_nameEmpresa);
        cargoEmpresa = (EditText) root.findViewById( R.id.txt_cargoEmpresa);
        fechaStart = (DatePicker) root.findViewById( R.id.pickerFechaLaboralStart);
        btn = (ImageButton) root.findViewById( R.id.btn_sendLaboral);
        btn.setOnClickListener(this);

        working.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if( working.isChecked() )
                    fechaEnd.setVisibility(View.INVISIBLE);
                else
                    fechaEnd.setVisibility(View.VISIBLE);


            }
        });//evento del checkbox

        IconManager icon = new IconManager();
        icon.setBackgroundApp((FrameLayout)root.findViewById(R.id.contentHistoryLaboral));
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

    @Override
    public void onClick(View v) {

        newlaboral.SaveNewHistoryLaboral(nameEmpresa, cargoEmpresa, fechaStart, working, fechaEnd);

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }



}
