package com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.FileUtils;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ContentResults.ResponseContent;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.Interface.AdminCircles;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.Splash;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EvidenciasActivities extends Fragment implements View.OnClickListener{

    public static int ITINERARIO;
    private static DBManager DB;

    ImageView imagen;
    ImageButton camara, upload;
    TextView idItinerario;

    Uri output;
    String foto;
    File file;
    File EXTERNAL_STORAGE_DIRECTORY;
    String nameFileImagen;

    Constantes mss = new Constantes();
    Splash PDialog = new Splash();
    OkHttpClient okHttpClient;
    GeneralCode code;

    private OnFragmentInteractionListener mListener;


    public static EvidenciasActivities newInstance(DBManager db, int circle1, int itinerario1) {
        EvidenciasActivities fragment = new EvidenciasActivities();
        Bundle args = new Bundle();
        ITINERARIO = itinerario1;
        DB = db;
        fragment.setArguments(args);
        return fragment;
    }

    public EvidenciasActivities() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        okHttpClient = new OkHttpClient.Builder()
            .readTimeout(mss.TIME_LIMIT_WAIT_SERVER_LONG, TimeUnit.SECONDS)
            .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER_LONG, TimeUnit.SECONDS)
            .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_evidencias_activities, container, false);
        code = new GeneralCode(DB, getActivity().getApplicationContext() );

        imagen = (ImageView) root.findViewById( R.id.imgEvidencia);
        idItinerario = (TextView) root.findViewById( R.id.ItinerarioId);
        upload = (ImageButton) root.findViewById(R.id.send_evidencia);
        camara = (ImageButton) root.findViewById(R.id.cam_take_evidencia);
        upload.setOnClickListener(this);
        camara.setOnClickListener(this);

        IconManager icon = new IconManager();
        icon.setBackgroundApp((FrameLayout)root.findViewById(R.id.contentEvidencias));

        TextView contentId = (TextView) root.findViewById(R.id.ItinerarioId);
        contentId.setText( ITINERARIO+"" );

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

        switch (v.getId() ){
            case R.id.cam_take_evidencia:
                getCamara();
                break;

            case R.id.send_evidencia:
                Uploader();
                break;

            default:
                Toast.makeText(getActivity().getApplicationContext(), "ningun boton ", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    static File getDirectory(String variableName, String defaultPath) {
        String path = System.getenv(variableName);
        return path == null ? new File(defaultPath) : new File(path);
    }

    //<editor-fold desc="abre una ventana con la camara para tomar una foto">
    public void getCamara(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        EXTERNAL_STORAGE_DIRECTORY = getDirectory("EXTERNAL_STORAGE", "/sdcard");

        nameFileImagen = "/Evidencia#!#"+idItinerario.getText().toString()+"#!#"+currentDateandTime+".jpg";
        //foto = Environment.getExternalStorageDirectory() + nameFileImagen;
        foto = EXTERNAL_STORAGE_DIRECTORY + nameFileImagen;
        file = new File( foto );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        output = Uri.fromFile( file );
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, 1);

    }
    //</editor-fold>


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//obtiene la foto tomada y la ajusta para subirla al servidor


        ContentResolver cr = getActivity().getContentResolver();

        Bitmap bit;
        try {

            bit = android.provider.MediaStore.Images.Media.getBitmap(cr, output);


            if ( bit != null ) {
                //orientation
                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(
                            file.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) { //dependiendo de la posicion del movil al tomar la foto, la gira
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }

                }catch (Exception e){

                    Toast.makeText(getActivity().getApplicationContext(),
                                            mss.ProcesImgError +e.getMessage(),
                                            Toast.LENGTH_SHORT).show();

                    new ServicesPeticion().SaveError(e,
                            new Exception().getStackTrace()[0].getMethodName().toString(),
                            this.getClass().getName());//Envio la informacion de la excepcion al server

                }


                BitmapFactory.Options options = new BitmapFactory.Options();//obtimiza el trato de las img
                options.inSampleSize = 2;//obtimiza el trato de las img
                bit = BitmapFactory.decodeFile(foto, options);//obtimiza el trato de las img

                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bit = Bitmap.createBitmap(bit , 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);//obtiene la imagen con los nuevos ajustes

                imagen.setImageBitmap(bit);//muestra la imagen en el imageView
            }

        } catch (FileNotFoundException e) {



            Toast.makeText(getActivity().getApplicationContext(),
                    mss.CarryImageError+e.getMessage(), Toast.LENGTH_SHORT).show();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server


        } catch (IOException e) {

            Toast.makeText(getActivity().getApplicationContext(),
                            mss.CarryImageError+e.getMessage(), Toast.LENGTH_SHORT).show();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

        }

    }

    //<editor-fold desc="llamar el metodo que sube la imagen al server y guarda el registro">
    public void Uploader(){

        final ProgressDialog pDialog= PDialog.getpDialog(getActivity());
        pDialog.show();


        //File files = FileUtils.getFile( getActivity(), output );
        File files = file;
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), files);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", files.getName(), requestFile);//se le da el nombre para recibir en php


        String itinerario = idItinerario.getText().toString();

        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), itinerario);//se envia el itinerario

        String nToken = code.getToken();

        RequestBody token =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), nToken);//se envia el token

        String nUser = code.getIdUser();

        RequestBody user =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), nUser);//se envia el usuario


       Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ServerUri.SERVICE_ADMIN_CIRCLE )
                .addConverterFactory(GsonConverterFactory.create())
                .client( okHttpClient )
                .build();

        AdminCircles adminCircle = retrofit.create(AdminCircles.class);

        Call<ResponseContent> call = adminCircle.SaveEvidencia( description, user, token, body );

        call.enqueue(new Callback<ResponseContent>() {//escuchador para obtener la respuesta del servidor
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {//obtener datos

                ResponseContent data = response.body();
                ValidateResponse( data );
                pDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) { //si la peticion falla

                Log.e( mss.TAG, "error "+ t.toString());

                pDialog.dismiss();

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

                file.delete();

            }else{

                Log.i( mss.TAG, data.getBody().toString() );

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
