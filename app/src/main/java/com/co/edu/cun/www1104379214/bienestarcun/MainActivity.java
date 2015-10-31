package com.co.edu.cun.www1104379214.bienestarcun;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.Metodos;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ServicesPeticion services = new ServicesPeticion();
    Metodos metodos = new Metodos();//clase con metodos para usar

    DBManager db;
    Cursor result;

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
            Log.i("ERROR","eroor");
            e.printStackTrace();
        }

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
        // Enviar título como arguemento del fragmento
        /* abrir fragments
            // Enviar título como arguemento del fragmento
            Bundle args = new Bundle();
            args.putString(mx.platzi.navdrawersample.PlaceholderFragment.ARG_SECTION_TITLE, title);

            Fragment fragment;

            if (id == R.id.nav_Example){
                fragment =  second.newInstance(title,"solo ejemplos");
            }else {
                fragment = PlaceholderFragment.newInstance(title);
            }

            fragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
         */
        drawerLayout.closeDrawers(); // Cerrar drawer
        setTitle(title); // Setear título actual
    }

    //********************************************
    //********************************************Base de datos local
    //********************************************



    public void BDManager(){

        db = new DBManager(getApplicationContext());//crea la base de datos

        new BuscarTask().execute();

    }

    private class BuscarTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Ejecutando...", Toast.LENGTH_SHORT).show();
        }



        @Override
        protected Void doInBackground(Void... voids) {
            result = db.SearchDB();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayList<String> mArrayList = new ArrayList<String>();
            result.moveToFirst();
            while(!result.isAfterLast()) {
               /* mArrayList.add(result.getString(
                        result.getColumnIndex(db.CN_COD_NOTIFICACION)
                )); //add the item
                */
                Log.i("RESULT",result.getString(result.getColumnIndex(db.CN_COD_NOTIFICACION)));
                Log.i("RESULT",result.getString(result.getColumnIndex(db.CN_TIPE_NOTIFICATION)));
                result.moveToNext();
            }
            result.close();
            Toast.makeText(getApplicationContext(), "Finalizado...", Toast.LENGTH_SHORT).show();

        }
    } //realizar acciones en segundo plano

    //********************************************
    //********************************************fin Base de datos
    //********************************************

    private void Ejecutar() throws InterruptedException {
        BDManager();
        services.ListPerson(getApplicationContext());
    }
}
