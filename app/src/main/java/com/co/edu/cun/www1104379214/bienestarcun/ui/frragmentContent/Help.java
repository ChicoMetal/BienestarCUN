package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;


public class Help extends Fragment {

    private OnFragmentInteractionListener mListener;
    static GeneralCode CODE;

    LinearLayout activities, desertion, laboral,
            notification, psicologia_title, psicologia, psicologia_complement, admin_activities;

    public Help() {
        // Required empty public constructor
    }


    public static Help newInstance( GeneralCode code ) {
        Help fragment = new Help();
        Bundle args = new Bundle();
        CODE = code;

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        activities = (LinearLayout) root.findViewById( R.id.help_activities );
        desertion = (LinearLayout) root.findViewById( R.id.help_desertion );
        laboral = (LinearLayout) root.findViewById( R.id.help_laboral );
        notification = (LinearLayout) root.findViewById( R.id.help_notification );
        psicologia_title = (LinearLayout) root.findViewById( R.id.help_psicologia_title );
        psicologia = (LinearLayout) root.findViewById( R.id.help_psicologia );
        psicologia_complement = (LinearLayout) root.findViewById( R.id.help_psicologia_complement );
        admin_activities = (LinearLayout) root.findViewById( R.id.help_admin_circle );

        AdapterHelpView();
        
        IconManager icon = new IconManager();
        icon.setBackgroundApp(getActivity().getResources(),
                (FrameLayout)root.findViewById(R.id.content_information_help));
        return root;

        
    }

    //<editor-fold desc="Validar rol del usuario para mostrar las guias correspondientes">
    private void AdapterHelpView() {

        String tipeUser = CODE.GetTipeUser();

        switch ( tipeUser ){
            case Constantes.UsrLoginOff:
                HideAll();
                break;

            case Constantes.UsrStudent:
                ShowStudent();
                break;

            case Constantes.UsrExStudent:
                ShowExStudent();
                break;

            case Constantes.UsrPsicologa:
                ShowPsicologa();
                break;

            case Constantes.UsrDocente:
                ShowDocente();
                break;

            case Constantes.UsrAdCircle:
                ShowAdCircle();
                break;

            case Constantes.UsrAd:
                ShowAd();
                break;

            case Constantes.UsrSuperAd:
                ShowAd();
                break;

        }

    }
    //</editor-fold>

    //<editor-fold desc="Adaptar las guias segun el rol del usuario, ocultando y mostrando informacion">
    private void ShowAd() {
        activities.setVisibility(getView().VISIBLE );
        desertion.setVisibility(getView().VISIBLE );
        laboral.setVisibility(getView().VISIBLE );
        notification.setVisibility(getView().VISIBLE );
        psicologia_title.setVisibility(getView().VISIBLE );
        psicologia.setVisibility(getView().VISIBLE );
        psicologia_complement.setVisibility(getView().VISIBLE );
        admin_activities.setVisibility(getView().VISIBLE );
    }

    private void ShowAdCircle() {
        activities.setVisibility(getView().GONE );
        desertion.setVisibility(getView().GONE );
        laboral.setVisibility(getView().GONE );
        notification.setVisibility(getView().VISIBLE );
        psicologia_title.setVisibility(getView().VISIBLE );
        psicologia.setVisibility(getView().GONE );
        psicologia_complement.setVisibility(getView().VISIBLE );
        admin_activities.setVisibility(getView().VISIBLE );
    }

    private void ShowDocente() {
        activities.setVisibility(getView().GONE );
        desertion.setVisibility(getView().VISIBLE );
        laboral.setVisibility(getView().GONE );
        notification.setVisibility(getView().VISIBLE );
        psicologia_title.setVisibility(getView().VISIBLE );
        psicologia.setVisibility(getView().GONE );
        psicologia_complement.setVisibility(getView().VISIBLE );
        admin_activities.setVisibility(getView().GONE );
    }

    private void ShowPsicologa() {
        activities.setVisibility(getView().GONE );
        desertion.setVisibility(getView().GONE );
        laboral.setVisibility(getView().GONE );
        notification.setVisibility(getView().VISIBLE );
        psicologia_title.setVisibility(getView().VISIBLE );
        psicologia.setVisibility(getView().VISIBLE );
        psicologia_complement.setVisibility(getView().VISIBLE );
        admin_activities.setVisibility(getView().GONE );
    }

    private void ShowExStudent() {
        activities.setVisibility(getView().VISIBLE );
        desertion.setVisibility(getView().GONE );
        laboral.setVisibility(getView().VISIBLE);
        notification.setVisibility(getView().VISIBLE);
        psicologia_title.setVisibility(getView().VISIBLE );
        psicologia.setVisibility(getView().GONE );
        psicologia_complement.setVisibility(getView().VISIBLE );
        admin_activities.setVisibility(getView().GONE );
    }

    private void ShowStudent() {
        activities.setVisibility(getView().VISIBLE);
        desertion.setVisibility(getView().GONE );
        laboral.setVisibility(getView().GONE );
        notification.setVisibility(getView().VISIBLE );
        psicologia_title.setVisibility(getView().VISIBLE );
        psicologia.setVisibility(getView().GONE );
        psicologia_complement.setVisibility(getView().VISIBLE );
        admin_activities.setVisibility(getView().GONE );
    }

    private void HideAll() {

        activities.setVisibility(getView().GONE );
        desertion.setVisibility(getView().GONE );
        laboral.setVisibility(getView().GONE );
        notification.setVisibility(getView().VISIBLE );
        psicologia_title.setVisibility(getView().GONE );
        psicologia.setVisibility(getView().GONE );
        psicologia_complement.setVisibility(getView().GONE );
        admin_activities.setVisibility(getView().GONE );

    }
    //</editor-fold>


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
