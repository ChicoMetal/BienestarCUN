package com.co.edu.cun.www1104379214.bienestarcun.ui;



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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ChatPsicologiaManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.AdapterUserMenu;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Abaut;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.ChatPendientes;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Circles_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.ChatPsicologa_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.CircleAdministration_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Desertion_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Developers;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Help;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.HistoryLaboral_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Home_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.DelActivities_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Itinerarios_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.LaboralStatus;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.LoginUser;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Notifications_app;
import com.co.edu.cun.www1104379214.bienestarcun.ui.frragmentContent.Show_itinerario_circle;


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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment;
    ImageButton camara;
    ImageView imagen;
    ImageButton upload;
    TextView idItinerario;
    Uri output;
    String foto;
    File file;
    String nameFileImagen;

    AdapterUserMenu adapterMenu;//clase con adapterMenu para usar
    Constantes mss = new Constantes();
    ServicesPeticion services;
    IconManager icon;
    GeneralCode code = null;
    DBManager db;

    public static int NUM_COLUMNS;


    NavigationView navigationView;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(); // Setear Toolbar como action bar

        services = new ServicesPeticion();

        BDManager();//crear/instanciar la BD local

        adapterMenu = new AdapterUserMenu( getApplicationContext(), db );

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//instancio menu lateral
        navigationView = (NavigationView) findViewById(R.id.nav_view);//instancio contenedor de menu lateral

        PrepareMenuUser(); // ocultar items de menu a los que el usuario actual no tenga acceso

        if (navigationView != null) { //Agregar fragments dependiendo del ciclo de vida de la app
            setupDrawerContent(navigationView);
        }


        if (savedInstanceState == null) {//Seleccionar item de menu por default o agregar el que tenia si existe
            int id = R.id.nav_home;
            selectItem(Constantes.TitleMenuHome, id);

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        if( code == null )
            code = new GeneralCode(db, getApplicationContext()  );

        try {
            Ejecutar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server
        }


    }

    @Override
    public WindowManager getWindowManager() {
        return super.getWindowManager();
    }


    //********************************************
    //********************************************DrawerLayouts
    //********************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {

            getMenuInflater().inflate(R.menu.main, menu);


            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = "";

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_ebaout:
                fragment =  Abaut.newInstance();
                title = getResources().getString(R.string.item_ebaut);
                break;
            case R.id.action_creation:
                fragment =  Developers.newInstance();
                title = getResources().getString(R.string.item_desarrolladores);
                break;
            case R.id.action_help:
                fragment =  Help.newInstance();
                title = getResources().getString(R.string.item_ayuda);
                break;
        }

        setTitle(title); // Setear título actual
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();

        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle

            ab.setHomeAsUpIndicator(R.drawable.icon_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void PrepareMenuUser(){

        icon = new IconManager();
        icon.SetIconMenu(navigationView);

        adapterMenu.ComproveUser(navigationView);//verficar si existe un usuario logueado
        //navigationView.getMenu().findItem(R.id.nav_cart).setVisible(false);
        //navigationView.getMenu().findItem(R.id.nav_products).setVisible(false);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // menuItem.setChecked(true);// Marcar item presionado
                        // Crear nuevo fragmento

                        String title = menuItem.getTitle().toString();
                        int id = menuItem.getItemId();
                        selectItem(title,id);
                        return true;
                    }


                }

        );
    }


    //<editor-fold desc="Permite la seleccion de un item en el menu lateral">
    public void selectItem(String title, int id) {

        if( id != R.id.nav_logout){ //si se selecciona salir no llama fragmento, solo ejecuta es logout

            ChangeFragment(id);

            drawerLayout.closeDrawers(); // Cerrar drawer

            setTitle(title); // Setear título actual


        }else{
            Logout();
        }

    }
    //</editor-fold>

    //<editor-fold desc="Cambia los fragmentos segun el iten seleccionado del menu lateral">
    private void ChangeFragment(int id){
        // Enviar título como arguemento del fragmento
        //abrir fragments

        Bundle args = new Bundle();
        args.putString("", "");

        fragment =  Home_app.newInstance();

        switch ( id ){
            case R.id.nav_home:
                fragment =  Home_app.newInstance();
                break;

            case R.id.nav_add_activities:
                fragment =  Circles_app.newInstance( db );
                break;
            case R.id.nav_del_activities:
                fragment =  DelActivities_app.newInstance(db, "");
                break;
            case R.id.nav_itinerarios:
                fragment =  Itinerarios_app.newInstance(db, getSupportFragmentManager());
                break;

            case R.id.nav_add_desertion:
                fragment =  Desertion_app.newInstance(db);
                break;

            case R.id.nav_add_laboral:
                fragment =  HistoryLaboral_app.newInstance( db );
                break;
            case R.id.nav_update_laboral_state:
                fragment =  LaboralStatus.newInstance(db);
                break;

            case R.id.nav_show_notifications:
                fragment =  Notifications_app.newInstance(db, "");
                break;

            case R.id.nav_new_chat:
                ChatPsicologiaManager ChatCodeInflateNull = new ChatPsicologiaManager(
                        getApplicationContext(), db );

                if( ChatCodeInflateNull.ComproveUser() ){//dependiendo si el usuario es psicologo o no

                    FragmentManager fragmentManagerChat = getSupportFragmentManager();
                    fragment =  ChatPendientes.newInstance(db, fragmentManagerChat);

                }else{

                    fragment =  ChatPsicologa_app.newInstance( db, null );//se le envia 0 dado que aun no tengo el destinatario
                }
                break;

            case R.id.nav_new_itinerario:
                fragment =  CircleAdministration_app.newInstance( db );
                break;
            case R.id.nav_del_itinerario:
                fragment = Show_itinerario_circle.newInstance(mss.INSTANCE_ITINERARIOS_ADMIN_CIRCLE,
                        db,
                        mss.SHOW_ITINERARIO_CANCEL,
                        getSupportFragmentManager());
                break;
            case R.id.nav_asistencia:
                fragment = Show_itinerario_circle.newInstance(mss.INSTANCE_ITINERARIOS_ADMIN_CIRCLE,
                                                                db,
                                                                mss.SHOW_ITINERARIO_ASISTENCIA,
                                                                getSupportFragmentManager());
                break;
            case R.id.nav_evidencias:
                fragment = Show_itinerario_circle.newInstance(mss.INSTANCE_ITINERARIOS_ADMIN_CIRCLE,
                                                                db,
                                                                mss.SHOW_ITINERARIO_EVIDENCIAS,
                                                                getSupportFragmentManager());
                break;

            case R.id.nav_login:
                fragment =  LoginUser.newInstance(navigationView, this, db);
                break;

        }

        fragment.setArguments(args);
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();


    }
    //</editor-fold>

    //********************************************
    //********************************************Base de datos local
    //********************************************

    public void BDManager(){

        db = new DBManager( getApplicationContext() );//crea la base de datos local o la instancia si esta creada

    }


    //********************************************
    //********************************************fin Base de datos
    //********************************************

    private void Ejecutar() throws InterruptedException {

        setnameUserHead();

        DatosDisplay();

        code.ChoseUserDefault(this);//mostrar lista para seleccionar la sede para los usuarios sin loguear

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .connectTimeout(mss.TIME_LIMIT_WAIT_SERVER, TimeUnit.SECONDS)
                .build();

    }

    //<editor-fold desc="Funcion para obtener dimensiones del display">
    public void DatosDisplay(){

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double Ancho_inches =(double)width/(double)dens;
        double Altura_inches =(double)height/(double)dens;
        double x = Math.pow(Ancho_inches,2);
        double y = Math.pow(Altura_inches,2);
        double screenInches = Math.sqrt(x+y);

        if( Ancho_inches > mss.LIMIT_DISPLAY_CATEGORY )//si la pantalla es grande dos columnas
            NUM_COLUMNS = mss.NUM_COLUMNS_LARGE;
        else//si la pantalla es pequeña, una columna
            NUM_COLUMNS = mss.NUM_COLUMNS_SMALL;
    }
    //</editor-fold>


    //<editor-fold desc="cambiar el nombre del usuario (si es posible) y mostrarlo en el menu lateral">
    public void setnameUserHead() {

        code.getNameUser((TextView) findViewById(R.id.lb_nameUser));//peticion para mostrar nombre de usuario en el header del menu
    }
    //</editor-fold>

    //<editor-fold desc="Trigger logout">
    public void Logout(){
        adapterMenu.ProcessLogout(navigationView, this);
        int id = R.id.nav_home;
        selectItem(Constantes.TitleMenuHome, id);

        TextView nameUser = (TextView) findViewById(R.id.lb_nameUser);
        nameUser.setText(Constantes.DftUsrName);
    }
    //</editor-fold>

    public void SaveAsistenciaItinerario( View v ){//guarda la lista de asistencia del itinerario

        LinearLayout layout = (LinearLayout)findViewById( R.id.contentListAsistentes);
        TextView contentIdItinerario = (TextView) findViewById(R.id.idItinerario);

        String idItinerario = contentIdItinerario.getText().toString();

        new ItinerariosManager( getApplicationContext() ).SaveAsistenciasItinerario(layout, idItinerario);
    }


    //********************************************
    //********************************************
    //********************************************

    //<editor-fold desc="Subir fotos al server">
    public void getCamara(View v){//abre una ventana con la camara para tomar una foto

        imagen = (ImageView) findViewById( R.id.imgEvidencia);
        camara = (ImageButton) findViewById(R.id.cam_take_evidencia);
        upload = (ImageButton) findViewById(R.id.send_evidencia);
        idItinerario = (TextView) findViewById( R.id.ItinerarioId);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//obtiene la foto tomada y la ajusta para subirla al servidor

        ContentResolver cr=this.getContentResolver();
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

                Toast.makeText(MainActivity.this, mss.ProcesImgError +e.getMessage(),
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



            Toast.makeText(MainActivity.this, mss.CarryImageError+e.getMessage(), Toast.LENGTH_SHORT).show();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server


        } catch (IOException e) {

            Toast.makeText(MainActivity.this, mss.CarryImageError+e.getMessage(), Toast.LENGTH_SHORT).show();
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

            Toast.makeText(MainActivity.this, mss.SendImageError + e.getMessage(), Toast.LENGTH_SHORT).show();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

        }catch (IOException e) {

            Toast.makeText(MainActivity.this, mss.SendImageError + e.getMessage(), Toast.LENGTH_SHORT).show();
            new ServicesPeticion().SaveError(e,
                    new Exception().getStackTrace()[0].getMethodName().toString(),
                    this.getClass().getName());//Envio la informacion de la excepcion al server

        }

        return false;

    }
    class ServerUpdate extends AsyncTask<String,String,String>{
        ProgressDialog pDialog;

        @Override
        protected String doInBackground(String... arg0) {

            try {

                UploapFoto( foto );

            } catch (IOException e) {

                Toast.makeText(MainActivity.this, mss.SendImageError+ e.getMessage(), Toast.LENGTH_SHORT).show();
                new ServicesPeticion().SaveError(e,
                        new Exception().getStackTrace()[0].getMethodName().toString(),
                        this.getClass().getName());//Envio la informacion de la excepcion al server

            }

            if( OnInsert() )
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, mss.SendImageWell, Toast.LENGTH_SHORT).show();
                    }
                });
            else
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, mss.SendImageError, Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog( MainActivity.this);
            pDialog.setMessage("Enviando, espere...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
        }


    }

    public void Uploader( View v){ //llamar el metodo que sube la imagen al server y guarda el registro

        if( file.exists() )
            new ServerUpdate().execute();

    }
    //</editor-fold>
}
