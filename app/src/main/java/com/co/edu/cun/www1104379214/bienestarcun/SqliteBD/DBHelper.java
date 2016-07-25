package com.co.edu.cun.www1104379214.bienestarcun.SqliteBD;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.co.edu.cun.www1104379214.bienestarcun.CodMessajes;
import com.co.edu.cun.www1104379214.bienestarcun.R;

/**
 * Created by root on 29/10/15.
 */
public class DBHelper extends SQLiteOpenHelper { //creacion de la base de datos

    public static final String DB_NAME = "BienestarLocal.sqlite";
    public static final int DB_SCHEMA_VERSION = 1;



    public DBHelper(Context context ) {
        super(context, DB_NAME, null, DB_SCHEMA_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(DBManager.CREATE_TABLE_NOTIFICATIONS);
        sqLiteDatabase.execSQL(DBManager.CREATE_TABLE_USER);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }





}
