package com.co.edu.cun.www1104379214.bienestarcun.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.co.edu.cun.www1104379214.bienestarcun.Constantes;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ChatPsicologiaManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.GeneralCode;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.IconManager;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.AdapterUserMenu;
import com.co.edu.cun.www1104379214.bienestarcun.Funciones.ItinerariosManager;
import com.co.edu.cun.www1104379214.bienestarcun.R;
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

public class MainActivity extends AppCompatActivity {
    
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment;
    AdapterUserMenu adapterMenu;//clase con adapterMenu para usar
    Constantes mss = new Constantes();
    ServicesPeticion servicios;
    IconManager icon;
    GeneralCode code = null;
    DBManager db;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;

    public static int NUM_COLUMNS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(); // Setear Toolbar como action bar

        servicios = new ServicesPeticion();

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
            servicios.SaveError(e,
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
                fragment =  Help.newInstance( code );
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

    //<editor-fold desc="Guardar asistencia de los itinerarios">
    public void SaveAsistenciaItinerario( View v ){//guarda la lista de asistencia del itinerario

        LinearLayout layout = (LinearLayout)findViewById( R.id.contentListAsistentes);
        TextView contentIdItinerario = (TextView) findViewById(R.id.idItinerario);

        String idItinerario = contentIdItinerario.getText().toString();

        new ItinerariosManager( db, getApplicationContext() ).SaveAsistenciasItinerario(layout, idItinerario);
    }
    //</editor-fold>

    //<editor-fold desc="Confirmar salir de la aplicacion">
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)// sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Salir
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .show();

// Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
// para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }
    //</editor-fold>

}
