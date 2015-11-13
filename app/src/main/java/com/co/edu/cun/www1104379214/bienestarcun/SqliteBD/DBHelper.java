package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;

/**
 * Created by root on 29/10/15.
 */
public class DBHelper extends SQLiteOpenHelper { //creacion de la base de datos

    CodMessajes mss = new CodMessajes();

    public static final String DB_NAME = "BienestarLocal.sqlite";
    public static final int DB_SCHEMA_VERSION = 1;

    String[][] UserDefault = new String[][]{
            {DBManager.CN_ID_USER_BD, mss.DftUsrId},
            {DBManager.CN_USER, mss.DftUsrName},
            {DBManager.CN_PASSWORD, mss.DftUsrPass },
            {DBManager.CN_TIPE_USER, mss.UsrLoginOff },
            {DBManager.CN_TOKEN_LOGIN, mss.DftUsrToken }
    };

    public DBHelper(Context context ) {
        super(context, DB_NAME, null, DB_SCHEMA_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBManager.CREATE_TABLE_NOTIFICATIONS);
        sqLiteDatabase.execSQL(DBManager.CREATE_TABLE_USER);

        sqLiteDatabase.insert(DBManager.TABLE_NAME_USER,
                null,
                GenerateUserDefault(UserDefault));//inserto user por default
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public ContentValues GenerateUserDefault(String values[][]){//Genera un contenedor con los valores para la insercion

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

}
