package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.ui.MainActivity;

import static com.co.edu.cun.www1104379214.bienestarcun.Constantes.TAG1;


public class Home_app extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static Home_app newInstance() {
        Home_app fragment = new Home_app();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public Home_app() {
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

        View view = inflater.inflate(R.layout.fragment_home_app, container, false);

        // Ubicar argumento en el text view de section_fragment.xml

        ImageView img = (ImageView) view.findViewById(R.id.svgimg);
        img.setImageResource(R.drawable.cunnegro);


        return view;
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
