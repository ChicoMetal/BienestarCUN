package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.co.edu.cun.www1104379214.bienestarcun.Funciones.AdapterUserMenu;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.ui.MainActivity;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;


public class LoginUser extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    AdapterUserMenu adapterMenu;//clase con adapterMenu para usar
    static MainActivity MAIN;
    static DBManager DB;

    static NavigationView NAVIGATIONVIEW;
    private EditText user, pass;
    ImageButton btn;

    public static LoginUser newInstance(NavigationView navigationView, MainActivity main, DBManager db) {
        MAIN = main;
        NAVIGATIONVIEW = navigationView;
        DB = db;
        LoginUser fragment = new LoginUser();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginUser() {
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
        View root =  inflater.inflate(R.layout.fragment_login_user, container, false);
        adapterMenu = new AdapterUserMenu( getActivity().getApplicationContext(), DB );

        user = (EditText) root.findViewById( R.id.et_user_login );
        pass = (EditText) root.findViewById( R.id.et_password_login );
        btn = (ImageButton) root.findViewById(R.id.btn_login_send);
        btn.setOnClickListener(this);

        IconManager icon = new IconManager();
        icon.setBackgroundApp((RelativeLayout) root.findViewById(R.id.contentLogin));
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

        if( adapterMenu != null)
            adapterMenu.ProcessLogin(MAIN, user, pass, NAVIGATIONVIEW );

    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
