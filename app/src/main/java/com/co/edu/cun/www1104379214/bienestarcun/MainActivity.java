package com.co.edu.cun.www1104379214.bienestarcun;



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
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.ChatPsicologiaManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.DesertionManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.AdapterUserMenu;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.LaboralAdd;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.NewItinerarioManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.ServerUri;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.ChatPendientes;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Circles_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.ChatPsicologa_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.CircleAdministration_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Desertion_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.HistoryLaboral_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Home_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.DelActivities_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Itinerarios_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.LaboralStatus;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.LoginUser;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Notifications_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Show_itinerario_circle;


import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

public class MainActivity extends AppCompatActivity {

    ImageButton camara;
    ImageView imagen;
    ImageButton upload;
    TextView idItinerario;
    Uri output;
    String foto;
    File file;
    String nameFileImagen;


    AdapterUserMenu metodos;//clase con metodos para usar
    CodMessajes mss = new CodMessajes();
    ServicesPeticion services;
    IconManager icon;
    GeneralCode code = null;
    DBManager db;


    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String drawerTitle;


    private int SHOW_ITINERARIO_ASISTENCIA = 1;
    private int SHOW_ITINERARIO_EVIDENCIAS = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(); // Setear Toolbar como action bar

        services = new ServicesPeticion( getApplicationContext() );

        try {
            Ejecutar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            String contenido = "Error desde android #!#";
            contenido += " Funcion: onCreate #!#";
            contenido += "Clase : MainActivity.java #!#";
            contenido += e.getMessage();
            services.SaveError(contenido);
        }

        metodos = new AdapterUserMenu( getApplicationContext(), db );


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//instancio menu lateral
        navigationView = (NavigationView) findViewById(R.id.nav_view);//instancio contenedor de menu lateral

        PrepareMenuUser(); // ocultar items de menu a los que el usuario actual no tenga acceso

        if (navigationView != null) { //Agregar fragments dependiendo del ciclo de vida de la app
            setupDrawerContent(navigationView);
        }


        drawerTitle = "Inicio";
        if (savedInstanceState == null) {//Seleccionar item de menu por default o agregar el que tenia si existe
            int id = R.id.nav_home;
            selectItem(drawerTitle, id);

        }


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
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
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

        metodos.ComproveUser(navigationView);//verficar si existe un usuario logueado
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


    private void selectItem(String title, int id) {

        if( id != R.id.nav_logout){ //si se selecciona salir no llama fragmento, solo ejecuta es logout

            ChangeFragment(id);

            drawerLayout.closeDrawers(); // Cerrar drawer

            setTitle(title); // Setear título actual


        }else{
            Logout();
        }

    }

    private void ChangeFragment(int id){
        // Enviar título como arguemento del fragmento
        //abrir fragments

        Bundle args = new Bundle();
        args.putString("", "");

        Fragment fragment;
        fragment =  Home_app.newInstance("", "");

        switch ( id ){
            case R.id.nav_home:
                fragment =  Home_app.newInstance("", "");
                break;

            case R.id.nav_add_activities:
                fragment =  Circles_app.newInstance(db, "");
                break;

            case R.id.nav_add_desertion:
                fragment =  Desertion_app.newInstance("", "");
                break;

            case R.id.nav_del_activities:
                fragment =  DelActivities_app.newInstance(db, "");
                break;

            case R.id.nav_itinerarios:
                fragment =  Itinerarios_app.newInstance(db, getSupportFragmentManager());
                break;

            case R.id.nav_asistencia:

                fragment = Show_itinerario_circle.newInstance(getCircleOfAdmin(),
                        db,
                        SHOW_ITINERARIO_ASISTENCIA,
                        getSupportFragmentManager());
                break;

            case R.id.nav_evidencias:

                fragment = Show_itinerario_circle.newInstance(getCircleOfAdmin(),
                        db,
                        SHOW_ITINERARIO_EVIDENCIAS,
                        getSupportFragmentManager());
                break;

            case R.id.nav_add_laboral:
                fragment =  HistoryLaboral_app.newInstance("", "");
                break;

            case R.id.nav_update_laboral_state:
                fragment =  LaboralStatus.newInstance("", "");
                break;

            case R.id.nav_show_notifications:
                fragment =  Notifications_app.newInstance(db, "");
                break;

            case R.id.nav_new_chat:

                ChatPsicologiaManager chatCod = new ChatPsicologiaManager(getApplicationContext(), db);

                if( chatCod.ComproveUser() ){//dependiendo si el usuario es psicologo o no
                    FragmentManager fragmentManagerChat = getSupportFragmentManager();
                    fragment =  ChatPendientes.newInstance(db, fragmentManagerChat);
                }else{

                    fragment =  ChatPsicologa_app.newInstance( getPsicologiaUser() , Integer.parseInt( chatCod.getIdUser() ));//se le envia 0 dado que aun no tengo el destinatario
                }

                break;

            case R.id.nav_new_itinerario:
                fragment =  CircleAdministration_app.newInstance("", "");
                break;

            case R.id.nav_login:
                fragment =  LoginUser.newInstance("", "");
                break;

        }

        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();


    }

    //********************************************
    //********************************************Base de datos local
    //********************************************

    public void BDManager(){

        db = new DBManager( getApplicationContext() );//crea la base de datos local

    }

    //********************************************
    //********************************************fin Base de datos
    //********************************************

    private void Ejecutar() throws InterruptedException {

        BDManager();

        setnameUserHead();

        //services.ListPerson(getApplicationContext());
    }

    private void setnameUserHead() {
        if( code == null)
            code = new GeneralCode(db, getApplicationContext());

        code.getNameUser((TextView) findViewById(R.id.lb_nameUser));//peticion para mostrar nombre de usuario en el header del menu
    }

    public void Login(View v){ //ingresar usuario

        EditText user, pass;

        user = (EditText) findViewById(R.id.et_user_login);
        pass = (EditText) findViewById(R.id.et_password_login);

        if ( metodos.ProcessLogin(user, pass, navigationView) ){

            drawerTitle = "Inicio";
            selectItem(drawerTitle, R.id.nav_home);
            setnameUserHead();

        }


    }

    public void Logout(){
        metodos.ProcessLogout(navigationView);
        int id = R.id.nav_home;
        drawerTitle = "Inicio";
        selectItem(drawerTitle, id);

        TextView nameUser = (TextView) findViewById(R.id.lb_nameUser);
        nameUser.setText("User");
    }


    public void AddMensaje(View v){//comenzar chat, usuarios distintos a psicolog@


        TextView ContenRemitente, ContentReceptor;
        EditText mensaje;
        LinearLayout contentChat;

        ContenRemitente = (TextView) findViewById(R.id.TVRemitente);
        ContentReceptor = (TextView) findViewById(R.id.TVRReceptor);
        mensaje = (EditText) findViewById(R.id.etMensajePsicologia);
        contentChat = (LinearLayout) findViewById(R.id.SVContentMsm);

        ChatPsicologiaManager chatMensajes = new ChatPsicologiaManager(getApplicationContext(), db);

        chatMensajes.AddMesagesChat(mensaje, ContenRemitente, ContentReceptor, contentChat);


    }

    public void SaveReportDesertion( View v ){//guadar reporte de desercio

        DesertionManager desertion = new DesertionManager(db, getApplicationContext() );

        EditText idEstudiante;
        RadioGroup horario;

        idEstudiante = (EditText) findViewById( R.id.et_idDesertor);
        horario = (RadioGroup) findViewById( R.id.rbg_horario);

        desertion.SendReportDesertion(idEstudiante, horario);


    }

    public void SaveItinerarioNew( View v ){//Guardar un nuevo itinerario

        NewItinerarioManager newItinerario = new NewItinerarioManager(db, getApplicationContext() );

        EditText nameActiviti, detailsActiviti;
        DatePicker fecha;
        TimePicker hora;


        nameActiviti = (EditText) findViewById( R.id.txtNameActivitieNew);
        detailsActiviti = (EditText) findViewById( R.id.txtDetailNewActiviti);
        fecha = (DatePicker) findViewById( R.id.pickerFechaNewItinerario);
        hora = (TimePicker) findViewById( R.id.pickerHoraNewItinerario);

        newItinerario.SaveNewItinerario(nameActiviti, detailsActiviti, fecha, hora);


    }

    public void SaveHistoryLaboral( View v ){//Guardar un nuevo itinerario

        LaboralAdd newlaboral = new LaboralAdd(db, getApplicationContext() );

        EditText nameEmpresa, cargoEmpresa;
        DatePicker fechaStart, fechaEnd;

        nameEmpresa = (EditText) findViewById( R.id.txt_nameEmpresa);
        cargoEmpresa = (EditText) findViewById( R.id.txt_cargoEmpresa);
        fechaStart = (DatePicker) findViewById( R.id.pickerFechaLaboralStart);
        fechaEnd = (DatePicker) findViewById( R.id.pickerFechaLaboralEnd);

        newlaboral.SaveNewHistoryLaboral(nameEmpresa, cargoEmpresa, fechaStart, fechaEnd);

    }

    public int getCircleOfAdmin(){//buscar el circulo que el usuario tiene a cargo

        String user = code.getIdUser();

        int circle = new ItinerariosManager( getApplicationContext() ).SearchCircleOfAdmin(user);

        return circle;

    }

    public void SaveAsistenciaItinerario( View v ){//guarda la lista de asistencia del itinerario

        LinearLayout layout = (LinearLayout)findViewById( R.id.contentListAsistentes);
        TextView contentIdItinerario = (TextView) findViewById(R.id.idItinerario);

        String idItinerario = contentIdItinerario.getText().toString();

        new ItinerariosManager( getApplicationContext() ).SaveAsistenciasItinerario(layout, idItinerario);
    }

    private long getPsicologiaUser(){
        if( code == null )
            code = new GeneralCode(db, getApplicationContext());

        String idUserPsicologia =  new ChatPsicologiaManager(getApplicationContext(), db).getIdPsicologiaUser( code.getIdUser() );

        if( idUserPsicologia != null )
            return Long.parseLong( idUserPsicologia );
        else
            return 0;
    }

    //********************************************
    //********************************************Subir fotos al server
    //********************************************

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

                String contenido = "Error desde android #!#";
                contenido += " Funcion: onActivityResul( try 2 change position) #!#";
                contenido += "Clase : MainActivity.java #!#";
                contenido += e.getMessage();
                new ServicesPeticion(getApplicationContext()).SaveError(contenido);

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
            String contenido = "Error desde android #!#";
            contenido += " Funcion: onActivityResult(try 1 get image FileNotFoundException) #!#";
            contenido += "Clase : MainActivity.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(getApplicationContext()).SaveError(contenido);


        } catch (IOException e) {

            Toast.makeText(MainActivity.this, mss.CarryImageError+e.getMessage(), Toast.LENGTH_SHORT).show();
            String contenido = "Error desde android #!#";
            contenido += " Funcion: onActivityResult(try 1 get image IOException) #!#";
            contenido += "Clase : MainActivity.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(getApplicationContext()).SaveError(contenido);

        }


    }


    private void UploapFoto( String imag ) throws IOException {

        String url = ServerUri.Server;
        String service = "adminCircle/SaveEvidencia.php";

        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(url+service);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody foto = new FileBody( file , "image/jpeg");//tipo de contenido q se envia
        mpEntity.addPart("fotoUp", foto);//nombre con que se recibe en el server
        httppost.setEntity(mpEntity);
        httpclient.execute(httppost);
        httpclient.getConnectionManager().shutdown();

    }

    private  boolean OnInsert(){

        String url = ServerUri.Server;
        String service = "adminCircle/imgSaveDB.php";

        HttpClient httpclient;
        List<NameValuePair> nameValuesPairs;
        HttpPost httppost;
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost(url+service);
        nameValuesPairs = new ArrayList<NameValuePair>(1);
        nameValuesPairs.add(new BasicNameValuePair("evidencia", nameFileImagen));
        nameValuesPairs.add(new BasicNameValuePair("itinerario", idItinerario.getText().toString()));

        try{

            httppost.setEntity(new UrlEncodedFormEntity(nameValuesPairs));
            httpclient.execute(httppost);
            return true;

        } catch (UnsupportedEncodingException e) {

            Toast.makeText(MainActivity.this, mss.SendImageError + e.getMessage(), Toast.LENGTH_SHORT).show();
            String contenido = "Error desde android #!#";
            contenido += " Funcion: OnInsert(UnsupportedEncodingException) #!#";
            contenido += "Clase : MainActivity.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(getApplicationContext()).SaveError(contenido);

        }catch (IOException e) {

            Toast.makeText(MainActivity.this, mss.SendImageError + e.getMessage(), Toast.LENGTH_SHORT).show();
            String contenido = "Error desde android #!#";
            contenido += " Funcion: OnInsert(IOException) #!#";
            contenido += "Clase : MainActivity.java #!#";
            contenido += e.getMessage();
            new ServicesPeticion(getApplicationContext()).SaveError(contenido);

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
                String contenido = "Error desde android #!#";
                contenido += " Funcion: doInBackground #!#";
                contenido += "Clase : MainActivity.java ServerUpdate#!#";
                contenido += e.getMessage();
                new ServicesPeticion(getApplicationContext()).SaveError(contenido);

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
}
