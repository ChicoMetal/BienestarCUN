package com.co.edu.cun.www1104379214.bienestarcun.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.ChatPsicologiaManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ChatList;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.adapter.HypedChatAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatPendientes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatPendientes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatPendientes extends Fragment {

    private static DBManager DB;
    ArrayList<ChatList> activities;

    public static final int NUM_COLUMNS = 1;

    private RecyclerView mHyperdChatList;
    private HypedChatAdapter adapter;

    public static FragmentManager fragmentManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatPendientes.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatPendientes newInstance(DBManager db, FragmentManager fragmentManager1) {
        ChatPendientes fragment = new ChatPendientes();
        Bundle args = new Bundle();
        DB = db;
        fragmentManager = fragmentManager1;
        fragment.setArguments(args);
        return fragment;
    }

    public ChatPendientes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HypedChatAdapter(getActivity(),DB,fragmentManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat_pendientes, container, false);

        mHyperdChatList = (RecyclerView) root.findViewById(R.id.hyper_chat_list);

        SetudActivitiesList();

        try {

            CasthConentAdapter();//lleno el adaptador

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: onCreateView #!#";
            contenido += "Clase : ChatPendientes.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(getActivity().getApplicationContext()).SaveError(contenido);
        }


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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void SetudActivitiesList(){

        mHyperdChatList.setLayoutManager(
                new GridLayoutManager(getActivity(),
                        NUM_COLUMNS));



        mHyperdChatList.setAdapter(adapter);
    }

    private void CasthConentAdapter() throws JSONException {

        ChatPsicologiaManager getChats = new ChatPsicologiaManager( getActivity().getApplicationContext(), DB );//busco en BD los circulos existentes

        JSONArray ChatPendientesResult = getChats.SearchChatPendientes();
        JSONObject indexCircles = getChats.IndexChats();

        if( ChatPendientesResult != null ){

            ArrayList<ChatList> chats = new ArrayList<>();

            for (int i=0; i < ChatPendientesResult.length(); i++){

                chats.add(new ChatList(ChatPendientesResult.getString(i), indexCircles));

            }

            adapter.AddAll(chats);

        }
    }
}
