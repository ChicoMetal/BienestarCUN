package com.co.edu.cun.www1104379214.bienestarcun;



import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.ChatPsicologiaManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.CirclesManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.DesertionManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.AdapterUserMenu;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.LaboralAdd;
import com.co.edu.cun.www1104379214.bienestarcun.Metodos.NewItinerarioManager;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLDelete;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.AsistenciaCircleActivities;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.ChatPendientes;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Circles_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.ChatPsicologa_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.CircleAdministration_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Desertion_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.EvidenciasActivities;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.HistoryLaboral_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Home_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.DelActivities_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Itinerarios_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.LaboralStatus;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.LoginUser;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Notifications_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Show_itinerario_circle;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    AdapterUserMenu metodos;//clase con metodos para usar
    CodMessajes mss = new CodMessajes();
    ServicesPeticion services = new ServicesPeticion();
    IconManager icon;
    GeneralCode code;
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
                if( chatCod.ComproveUser() ){
                    FragmentManager fragmentManagerChat = getSupportFragmentManager();
                    fragment =  ChatPendientes.newInstance(db, fragmentManagerChat);
                }else{
                    //TODO buscar id de la psicologa
                    fragment =  ChatPsicologa_app.newInstance( 7, Integer.parseInt( chatCod.getIdUser() ));//se le envia 0 dado que aun no tengo el destinatario
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

        db = new DBManager(getApplicationContext());//crea la base de datos local

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

        new ItinerariosManager( getApplicationContext() ).SaveAsistenciasItinerario( layout, idItinerario );
    }

}
