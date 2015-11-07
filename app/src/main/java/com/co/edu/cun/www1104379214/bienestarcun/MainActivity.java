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
import android.widget.EditText;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.Metodos;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLDelete;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLInsert;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.TaskExecuteSQLSearch;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Activities_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.ChatPsicologa_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.CircleAdministration_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Desertion_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.HistoryLaboral_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Home_app;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.LoginUser;
import com.co.edu.cun.www1104379214.bienestarcun.frragmentContent.Notifications_app;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    Metodos metodos;//clase con metodos para usar
    CodMessajes mss = new CodMessajes();
    ServicesPeticion services = new ServicesPeticion();

    DBManager db;


    JSONObject SQL_RESULT_SEARCH = new JSONObject();
    TaskExecuteSQLInsert sqliteInsert;
    TaskExecuteSQLSearch sqliteSearch;
    TaskExecuteSQLDelete sqliteDelete;

    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String drawerTitle;

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

        metodos = new Metodos( getApplicationContext(), db );


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
            ab.setHomeAsUpIndicator(R.drawable.abc_ic_menu_share_mtrl_alpha);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void PrepareMenuUser(){
        metodos.ComproveUser(navigationView);
        //navigationView.getMenu().findItem(R.id.nav_cart).setVisible(false);
        //navigationView.getMenu().findItem(R.id.nav_products).setVisible(false);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
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

        ChangeFragment(id);

        drawerLayout.closeDrawers(); // Cerrar drawer
        setTitle(title); // Setear título actual
    }

    private void ChangeFragment(int id){
        // Enviar título como arguemento del fragmento
        //abrir fragments
            // Enviar título como arguemento del fragmento

            Bundle args = new Bundle();
            args.putString("", "");

            Fragment fragment;
            fragment =  Home_app.newInstance("", "");

            switch ( id ){
                case R.id.nav_home:
                    fragment =  Home_app.newInstance("", "");
                    break;

                case R.id.nav_activities:
                    fragment =  Activities_app.newInstance("", "");
                    break;

                case R.id.nav_desertion:
                    fragment =  Desertion_app.newInstance("", "");
                    break;

                case R.id.nav_history_laboral:
                    fragment =  HistoryLaboral_app.newInstance("", "");
                    break;

                case R.id.nav_notifications:
                    fragment =  Notifications_app.newInstance("", "");
                    break;

                case R.id.nav_chat:
                    fragment =  ChatPsicologa_app.newInstance("", "");
                    break;

                case R.id.nav_circle_administration:
                    fragment =  CircleAdministration_app.newInstance("", "");
                    break;

                case R.id.nav_login:
                    fragment =  LoginUser.newInstance("", "");
                    break;

                case R.id.nav_logout:
                    metodos.ProcessLogout(navigationView);
                    drawerTitle = "Inicio";
                    selectItem(drawerTitle,  R.id.nav_home);
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

        //services.ListPerson(getApplicationContext());
    }

    public void Login(View v){ //ingresar usuario

        EditText user, pass;

        user = (EditText) findViewById(R.id.et_user_login);
        pass = (EditText) findViewById(R.id.et_password_login);

        metodos.ProcessLogin(user, pass, navigationView);

        drawerTitle = "Inicio";
        selectItem(drawerTitle, R.id.nav_home);

    }


}
