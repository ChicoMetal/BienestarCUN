package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.LaboralAdd;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;


public class LaboralStatus extends Fragment implements View.OnClickListener{

    LaboralAdd newlaboral;
    static DBManager DB;

    private OnFragmentInteractionListener mListener;
    ImageButton btn;
    RadioGroup radioBtn;

    public static LaboralStatus newInstance(DBManager db) {
        DB = db;
        LaboralStatus fragment = new LaboralStatus();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LaboralStatus() {
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
        View root = inflater.inflate(R.layout.fragment_laboral_status, container, false);
        newlaboral = new LaboralAdd(DB, getActivity().getApplicationContext() );

        radioBtn = (RadioGroup) root.findViewById( R.id.rbg_statusLaboral);
        btn = (ImageButton) root.findViewById( R.id.btnUpdateStatusLaboral);
        btn.setOnClickListener( this );

        IconManager icon = new IconManager();
        icon.setBackgroundApp(getActivity().getResources(),
                (FrameLayout)root.findViewById(R.id.contentStatusLaboral));

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
        newlaboral.SaveNewHistoryLabora( radioBtn );
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
