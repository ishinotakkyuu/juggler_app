package com.example.title_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "jugglerApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db ) {

        String SQL_CREATE_ENTRIES =
                "CREATE TABLE TEST ( " +
                        "ID INTEGER PRIMARY KEY," +
                        "OPERATION_DATE TEXT NOT NULL," +
                        "STORE_NAME TEXT NOT NULL," +
                        "OPERATION_YEAR INTEGER," +
                        "OPERATION_MONTH INTEGER," +
                        "OPERATION_DAY INTEGER," +
                        "OPERATION_DAY_DIGIT INTEGER," +
                        "WEEK_ID INTEGER," +
                        "DIFFERENCE_NUMBER INTEGER," +
                        "MACHINE_NAME TEXT," +
                        "START_GAME INTEGER," +
                        "TOTAL_GAME INTEGER," +
                        "SINGLE_BIG INTEGER," +
                        "CHERRY_BIG INTEGER," +
                        "SINGLE_REG INTEGER," +
                        "CHERRY_REG INTEGER," +
                        "CHERRY INTEGER," +
                        "GRAPE INTEGER" +
                        ")";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}