package com.example.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MonOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "myDB.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_SQL = "CREATE TABLE Users("
            + "Email TEXT PRIMARY KEY,"
            + "Password TEXT,"
            + "Firstname TEXT,"
            + "Lastname TEXT);";




    public MonOpenHelper(Context C){
        super(C, DB_NAME, null, DB_VERSION);;


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
