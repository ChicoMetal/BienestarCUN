package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;


public class LoginUser extends Fragment{

    private OnFragmentInteractionListener mListener;
    private EditText user, pass;

    public static LoginUser newInstance(String param1, String param2) {
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

        user = (EditText) root.findViewById( R.id.et_user_login );
        pass = (EditText) root.findViewById( R.id.et_password_login );

        IconManager icon = new IconManager();
        icon.setBackgroundApp((RelativeLayout) root.findViewById(R.id.contentLogin));
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
        public void onFragmentInteraction(Uri uri);
    }

}
