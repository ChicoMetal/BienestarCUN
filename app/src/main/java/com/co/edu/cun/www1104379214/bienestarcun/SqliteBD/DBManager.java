package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;

/**
 * Created by root on 29/10/15.
 */
public class DBManager { //Creacion de tablas

    private DBHelper helper;
    private SQLiteDatabase BD;
    CodMessajes mss = new CodMessajes();



    public DBManager(Context context) {

        helper = new DBHelper( context );

        BD = helper.getWritableDatabase();


    }
    //acciones a realizar a la BD local
    public static final String SQ_ACTION_SEARCH = "search";
    public static final String SQ_ACTION_DELETE = "delete";
    public static final String SQ_ACTION_INSERT = "insert";

    public static final String TABLE_NAME_NOTIFICATION = "Notifications";
    public static final String TABLE_NAME_USER = "LogUser";

    //campos tabla notificacion
    public static final String CN_ID_NOTIFICATIONS = "_id";
    public static final String CN_TIPE_NOTIFICATION = "tipe_notification";
    public static final String CN_COD_NOTIFICACION = "cod_notificacion";

    //campos tabla usuario
    public static final String CN_ID_USER = "_id";
    public static final String CN_ID_USER_BD = "id_user";
    public static final String CN_USER = "Usuario";
    public static final String CN_PASSWORD = "Password";
    public static final String CN_TIPE_USER = "Tipo_usuario";
    public static final String CN_TOKEN_LOGIN = "Token";

    //tipos de icon_notificaciones
    public static final int TN_EGRESADO = 1;
    public static final int TN_CIRCLE = 2;


    public static final String CREATE_TABLE_NOTIFICATIONS = " create table "+ TABLE_NAME_NOTIFICATION +
            "( " + CN_ID_NOTIFICATIONS +" integer primary key autoincrement,"+
                CN_TIPE_NOTIFICATION+" integer not null,"+
                CN_COD_NOTIFICACION+" integer not null"+
            ");"; //string de creacion de tables

    public static final String CREATE_TABLE_USER = " create table "+ TABLE_NAME_USER +
            "( " + CN_ID_USER +" integer primary key autoincrement,"+
            CN_ID_USER_BD+" text not null,"+
            CN_USER+" text not null,"+
            CN_PASSWORD+" text not null,"+
            CN_TIPE_USER+" text not null,"+
            CN_TOKEN_LOGIN+" text not null"+
            ");"; //string de creacion de tables


    public Boolean InsertBD(ContentValues valores, String TABLA){ //inserta registro en la BD

       Long result = BD.insert(TABLA, null,valores);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public ContentValues GenerateValues(String values[][]){//Genera un contenedor con los valores para la insercion

        ContentValues valores = new ContentValues();

        for(int c = 0; c < values.length; c++ ){
            valores.put(values[c][0],values[c][1]);
        }

        return valores;
        /*

        valores.put(CN_TIPE_NOTIFICATION,tipe_notification);
        valores.put(CN_COD_NOTIFICACION,cod_notification);


        */
    }

    public Cursor SearchDB(String TABLA, String[] campos){
       // String[] campos = new String[]{CN_TIPE_NOTIFICATION,CN_COD_NOTIFICACION};

        return BD.query(TABLA,campos,null,null,null,null,null);
    }

    public int  DeleteBD(){
        BD.delete(TABLE_NAME_NOTIFICATION, null, null);
        return BD.delete(TABLE_NAME_USER,null,null);

    }

}
