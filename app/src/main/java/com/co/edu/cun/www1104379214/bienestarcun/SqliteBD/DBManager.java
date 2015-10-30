package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by root on 29/10/15.
 */
public class DBManager { //Creacion de tablas

    private DBHelper helper;
    private SQLiteDatabase BD;

    public DBManager(Context context) {

        helper = new DBHelper( context );

        BD = helper.getWritableDatabase();
    }

    public static final String TABLE_NAME = "Notifications";

    public static final String CN_ID = "_id";
    public static final String CN_TIPE_NOTIFICATION = "tipe_notification";
    public static final String CN_COD_NOTIFICACION = "cod_notificacion";

    public static final String CREATE_TABLE_NOTIFICATIONS = " create table "+TABLE_NAME+
            "( " +CN_ID+" integer primary key autoincrement,"+
                CN_TIPE_NOTIFICATION+" integer not null,"+
                CN_COD_NOTIFICACION+" integer not null"+
            ");"; //string de creacion de tables



    public Boolean InsertBD(int tipe_notification, int cod_notification){ //inserta registro en la BD

       Long result = BD.insert(TABLE_NAME, null, GenerateValues(tipe_notification, cod_notification));

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    private ContentValues GenerateValues(int tipe_notification, int cod_notification){//Genera un contenedor con los valores para la insercion

        ContentValues valores = new ContentValues();

        valores.put(CN_TIPE_NOTIFICATION,tipe_notification);
        valores.put(CN_COD_NOTIFICACION,cod_notification);

        return valores;
    }

    public Cursor SearchDB(){
        String[] campos = new String[]{CN_TIPE_NOTIFICATION,CN_COD_NOTIFICACION};

        return BD.query(TABLE_NAME,campos,null,null,null,null,null);
    }

}
