package com.co.edu.cun.www1104379214.bienestarcun;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.co.edu.cun.www1104379214.bienestarcun.Metodos.Metodos;
import com.co.edu.cun.www1104379214.bienestarcun.WebServices.ServicesPeticion;
import com.co.edu.cun.www1104379214.bienestarcun.SqliteBD.DBManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ServicesPeticion services = new ServicesPeticion();
    Metodos metodos = new Metodos();//clase con metodos para usar
    CodMessajes mss = new CodMessajes();

    DBManager db;
    Cursor result;

    JSONObject SQL_RESULT_SEARCH = new JSONObject();

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

            if (id == R.id.nav_login){
                fragment =  LoginUser.newInstance("", "");

            }else {
                fragment = LoginUser.newInstance("", "");
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

        db = new DBManager(getApplicationContext());//crea la base de datos
/*
        String[][] valores = new String[][]{
                {db.CN_TIPE_NOTIFICATION,db.TN_EGRESADO+""},
                {db.CN_COD_NOTIFICACION,"1"}
        };

         new TaskExecuteSQL(db.TABLE_NAME_NOTIFICATION,
                db.GenerateValues(valores),
                null,
                db.SQ_ACTION_INSERT,
                getApplication()
                ).execute(); //insercion
*/
/*
        String[] camposSeacrh = new String[]{db.CN_TIPE_NOTIFICATION, db.CN_COD_NOTIFICACION};

        new TaskExecuteSQL(db.TABLE_NAME_NOTIFICATION,
                null,
                camposSeacrh,
                db.SQ_ACTION_SEARCH,
                getApplication()
        ).execute(); //busqueda

*/

/*
        new TaskExecuteSQL(db.TABLE_NAME_NOTIFICATION,
                null,
                null,
                db.SQ_ACTION_DELETE,
                getApplication()
        ).execute(); //Eliminacion
*/
    }


     private class TaskExecuteSQL extends AsyncTask<Void, Void, Void> {

            private String TABLA;
            private ContentValues VALORES;
            private String ACCION;
            private String[] CAMPOS; //Array para la busqueda de registros
            private Context CONTEXT;

            private String result_search;

            public TaskExecuteSQL(String tb,//nombre de la tabla
                                  ContentValues values, //Valores para insercion
                                  String[] colunms,//Campos a buscar
                                  String action,//accion a realizar
                                  Context contesto//contexto
            ) {

                TABLA = tb;
                VALORES = (values != null) ? values : null;
                CAMPOS = (colunms != null) ? colunms : null;
                ACCION = action;
                CONTEXT = contesto;

            }

            @Override
            protected void onPreExecute() {
                Toast.makeText(CONTEXT.getApplicationContext(), "Ejecutando...", Toast.LENGTH_SHORT).show();
            }


            @Override
            protected Void doInBackground(Void... voids) {

                switch (ACCION) { //compruebo la accion a realizar a sqlite
                    case DBManager.SQ_ACTION_DELETE:
                        int resultado = db.DeleteBD();
                        Log.i(mss.TAG, resultado + "");
                        break;
                    case DBManager.SQ_ACTION_INSERT:
                        result_search = db.InsertBD(VALORES, TABLA);

                        break;
                    case DBManager.SQ_ACTION_SEARCH:
                        result = db.SearchDB(TABLA, CAMPOS);
                        break;
                }

                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                switch (ACCION) { //en caso de ser insercion realizo esta accion
                    //al terminar la ejecucion
                    case DBManager.SQ_ACTION_SEARCH:

                        switch (TABLA){
                            case DBManager.TABLE_NAME_NOTIFICATION:
                                try {
                                    ProcedureNotifications(); //procedimientos para las consultas de notificaciones
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DBManager.TABLE_NAME_USER:
                                ProcedureUsers();//procedimientos para las consultas de usuarios
                                break;
                        }

                        break;
                    case DBManager.SQ_ACTION_INSERT:
                        Toast.makeText(CONTEXT.getApplicationContext(),
                                result_search,
                                Toast.LENGTH_SHORT).show();
                        break;

                }


            }

            private void CreateObjectResultSQL(){
                result.moveToFirst();
                int contador = 0;
                while (!result.isAfterLast()) {

                    try { //creo objeto de resultados consulta

                        String SubObject = "{"; //inicio la construccion del subobjeto

                        int size = CAMPOS.length;//numero de campos

                        for (int c = 0; c < size; c++) {//itero por cada campo enviado para la consulta
                            SubObject = SubObject + CAMPOS[c] + ":" +
                                    result.getString(result.getColumnIndex(CAMPOS[c])); //agrego clave y valor

                            if (!(c == (size - 1))) {//confirmo si no es la ultima columna
                                SubObject = SubObject + ","; //añado una coma al final del subObjeto para agregar el siguiente
                            }

                        }
                        SubObject = SubObject + "}";//finalizo subojeto

                        SQL_RESULT_SEARCH.put(contador + "", SubObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result.moveToNext();
                }

            }

            //metodos para realizar las acciones correspondientes a las busquedas de usuaro o notificaciones
            //***************************
            //***************************

            private void ProcedureNotifications() throws JSONException { //procedimientos despues de buscar notificaciones

                if (result.getCount() > 0) { //verifico si se encontraron registros
                    CreateObjectResultSQL(); //creo objeto con los resultados de consulta
                    String s = SQL_RESULT_SEARCH.getString("0");
                    JSONObject ss = new JSONObject(s);
                    Toast.makeText(CONTEXT.getApplicationContext(),ss.getString(db.CN_TIPE_NOTIFICATION) , Toast.LENGTH_SHORT).show();
                    //TODO ACCIONES
                }else{
                    //TODO acciones si no encuentra notificaciones
                }
                result.close();//cierro el cursor
            }

            private void ProcedureUsers() { //procedimientos despues de buscar usuarios
             if (result.getCount() > 0) { //verifico si se encontraron registros

                 CreateObjectResultSQL(); //creo objeto con los resultados de consulta
                //TODO ACCIONES
             }else{
                 //TODO acciones si no encuentra usuarios
             }
             result.close();//cierro el cursor
         }
        } //realizar acciones en segundo plano Notificaciones



    //********************************************
    //********************************************fin Base de datos
    //********************************************

    private void Ejecutar() throws InterruptedException {

        BDManager();
        //services.ListPerson(getApplicationContext());
    }

    public void Login(View v){

        EditText user, pass;

        user = (EditText) findViewById(R.id.et_user_login);
        pass = (EditText) findViewById(R.id.et_password_login);

        try {

            services.ConfirmarUser(getApplication(),
                    user.getText().toString(),
                    pass.getText().toString()); //Peticion login

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
