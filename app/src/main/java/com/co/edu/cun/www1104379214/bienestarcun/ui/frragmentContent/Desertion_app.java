package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.DesertionManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.ReporteDesercion;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Desertion_app extends Fragment implements View.OnClickListener{

    static DBManager DB;
    Constantes mss = new Constantes();

    private OnFragmentInteractionListener mListener;
    EditText idEstudiante;
    Spinner ListFacultades;
    EditText Description;
    RadioGroup horario;
    ImageButton btn;

    JSONObject idFacultades = new JSONObject();
    private String[] facultadesName;
    JSONArray facultadesResult;
    JSONObject indexFacultades;



    public static Desertion_app newInstance(DBManager db) {
        DB = db;
        Desertion_app fragment = new Desertion_app();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Desertion_app() {
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
        View root = inflater.inflate(R.layout.fragment_desertion_app, container, false);
        SearchFacultades();
        ListFacultades = (Spinner) root.findViewById(R.id.listFacultades);
        idEstudiante = (EditText) root.findViewById( R.id.et_idDesertor);
        Description = (EditText) root.findViewById( R.id.edt_description_desertioen);
        horario = (RadioGroup) root.findViewById( R.id.rbg_horario);
        btn = (ImageButton) root.findViewById( R.id.btn_sendDesertion );
        btn.setOnClickListener(this);

        IconManager icon = new IconManager();
        icon.setBackgroundApp(getActivity().getResources(),
                (FrameLayout)root.findViewById(R.id.contentDesertion));
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

        String facultad = null;

        if( ListFacultades != null ){
            try {
                facultad = idFacultades.getString( ListFacultades.getSelectedItem().toString() );//busco el codigo de la facultad elejida
            } catch (JSONException e) {
                e.printStackTrace();
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server
            }

            if( facultad != null ){//valido la eleccion de una facultad
                DesertionManager desertion = new DesertionManager(DB, getActivity().getApplicationContext() );
                desertion.SendReportDesertion(idEstudiante, facultad, Description, horario);
            }else{
                Toast.makeText(getActivity().getApplicationContext(),
                        "Debe seleccionar una facultad", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    //<editor-fold desc="Buscar las facultades para mostrar en la lista">
    private void SearchFacultades() {

        GeneralCode code = new GeneralCode( DB, getActivity().getApplicationContext() );

        String token = code.getToken();
        String user = code.getIdUser();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();//asisgnar tiempo de espera a la peticion

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_DESERTION )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        ReporteDesercion facultades = retrofit.create(ReporteDesercion.class);

        Call<ResponseContent> call = facultades.getFacultades( user, token );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();

                ValidateResponse( data );

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG, "error "+ t.toString());

            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="procesa la respuesta enviada del server">
    private void ValidateResponse(ResponseContent data) {

        try {

            if( data.getBody().getString(0).toString().equals("msm") ){//verifico si es un mensaje

                Toast.makeText(getActivity().getApplicationContext(),
                        mss.msmServices.getString(data.getBody().getString(1).toString()),
                        Toast.LENGTH_SHORT).show(); // muestro mensaje enviado desde el servidor

            }else{

                facultadesResult = data.getResults();
                indexFacultades = data.getIndex();


                facultadesName = new String[ data.getCount()+1 ];
                facultadesName[ 0 ] = mss.hintListFacultades;

                for (int c = 0; c < data.getCount(); c++){

                    String Name = facultadesResult.getJSONObject( c )
                                        .getString( indexFacultades.getString("1") );

                    facultadesName[ c+1 ] = Name;

                    idFacultades.put( Name, facultadesResult.getJSONObject( c )
                                            .getString( indexFacultades.getString("0") ));
                }//agrego los nombres de las facultades a un array para el spiner


                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(),
                                            android.R.layout.simple_spinner_item,
                        facultadesName);
                ListFacultades.setAdapter(adaptador);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }

    }
    //</editor-fold>
}
