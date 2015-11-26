package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.co.edu.cun.www1104379214.bienestarcun.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatPsicologa_app.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatPsicologa_app#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatPsicologa_app extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private static int mRemitente;
    private static int mReceptor;

    public TextView TVRemitente, TVReceptor;



    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatPsicologa_app.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatPsicologa_app newInstance(int receptor, int remitente) {
        ChatPsicologa_app fragment = new ChatPsicologa_app();

        mRemitente = remitente;
        mReceptor = receptor;

        return fragment;
    }

    public ChatPsicologa_app() {
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
        View root = inflater.inflate(R.layout.fragment_chat_psicologa_app, container, false);

        TVReceptor = (TextView) root.findViewById(R.id.TVRReceptor);
        TVRemitente = (TextView) root.findViewById(R.id.TVRemitente);

        TVReceptor.setText(mReceptor+"");
        TVRemitente.setText(mRemitente+"");

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
