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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.MainActivity;
import com.co.edu.cun.www1104379214.bienestarcun.ui.Splash;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EvidenciasActivities extends Fragment implements View.OnClickListener{

    private static int CIRCLE;
    public static int ITINERARIO;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "itinerario";
    private static final String ARG_PARAM2 = "param2";

    ImageView imagen;
    ImageButton camara, upload;
    TextView idItinerario;
    Uri output;
    String foto;
    File file;
    String nameFileImagen;
    Constantes mss = new Constantes();
    Splash PDialog = new Splash();

    private OnFragmentInteractionListener mListener;


    public static EvidenciasActivities newInstance(int circle1, int itinerario1) {
        EvidenciasActivities fragment = new EvidenciasActivities();
        Bundle args = new Bundle();
        CIRCLE = circle1;
        ITINERARIO = itinerario1;
        args.putString(ARG_PARAM1, itinerario1+"");
        fragment.setArguments(args);
        return fragment;
    }

    public EvidenciasActivities() {
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
        View root = inflater.inflate(R.layout.fragment_evidencias_activities, container, false);
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

    //<editor-fold desc="Subir fotos al server">
    public void getCamara(){//abre una ventana con la camara para tomar una foto

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        nameFileImagen = "/Evidencia#!#"+idItinerario.getText().toString()+"#!#"+currentDateandTime+".jpg";

        foto = Environment.getExternalStorageDirectory() + nameFileImagen;
        file = new File(foto);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        output = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, 1);

    }


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


    private void UploapFoto(String imag ) throws IOException {

        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost( ServerUri.SERVICE_SAVE_EVIDENCIA );
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody foto = new FileBody( file , "image/jpeg");//tipo de contenido q se envia
        mpEntity.addPart("fotoUp", foto);//nombre con que se recibe en el server
        httppost.setEntity(mpEntity);
        httpclient.execute(httppost);
        httpclient.getConnectionManager().shutdown();

    }

    private  boolean OnInsert(){

        HttpClient httpclient;
        List<NameValuePair> nameValuesPairs;
        HttpPost httppost;
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost( ServerUri.SERVICE_CHARGE_IMG );
        nameValuesPairs = new ArrayList<NameValuePair>(1);
        nameValuesPairs.add(new BasicNameValuePair("evidencia", nameFileImagen));
        nameValuesPairs.add(new BasicNameValuePair("itinerario", idItinerario.getText().toString()));

        try{

            httppost.setEntity(new UrlEncodedFormEntity(nameValuesPairs));
            httpclient.execute(httppost);
            return true;

        } catch (UnsupportedEncodingException e) {

            Toast.makeText(getActivity().getApplicationContext(),
                                mss.SendImageError + e.getMessage(), Toast.LENGTH_SHORT).show();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

        }catch (IOException e) {

            Toast.makeText(getActivity().getApplicationContext(),
                        mss.SendImageError + e.getMessage(), Toast.LENGTH_SHORT).show();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

        }

        return false;

    }



    public void Uploader(){ //llamar el metodo que sube la imagen al server y guarda el registro

        if( file.exists() )
            new ServerUpdate().execute();

    }

    class ServerUpdate extends AsyncTask<String,String,String> {
        final ProgressDialog pDialog= PDialog.getpDialog(getActivity());

        @Override
        protected String doInBackground(String... arg0) {

            try {

                UploapFoto( foto );

            } catch (IOException e) {

                Toast.makeText(getActivity().getApplicationContext(),
                        mss.SendImageError+ e.getMessage(), Toast.LENGTH_SHORT).show();
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server

            }

            if( OnInsert() )
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                mss.SendImageWell, Toast.LENGTH_SHORT).show();
                    }
                });
            else
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                mss.SendImageError, Toast.LENGTH_SHORT).show();
                    }
                });
            return null;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
        }


    }
    //</editor-fold>

}
