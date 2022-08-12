package delson.android.j_management_app;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "jugglerApp.db";
    private static final int DATABASE_VERSION = 1;
    Activity activity;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // メインテーブル
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE TEST ( " +
                        "ID INTEGER PRIMARY KEY," +
                        "USER_ID INTEGER NOT NULL," +
                        "OPERATION_DATE TEXT NOT NULL," +
                        "SAVE_DATE TEXT NOT NULL," +
                        "STORE_NAME TEXT NOT NULL," +
                        "OPERATION_YEAR INTEGER," +
                        "OPERATION_MONTH INTEGER," +
                        "OPERATION_DAY INTEGER," +
                        "OPERATION_DAY_DIGIT INTEGER," +
                        "SPECIAL_DAY INTEGER," +
                        "WEEK_ID INTEGER," +
                        "DAY_OF_WEEK_IN_MONTH INTEGER," +
                        "DIFFERENCE_NUMBER INTEGER," +
                        "MACHINE_NAME TEXT," +
                        "TABLE_NUMBER TEXT," +
                        "TAIL_NUMBER TEXT," +
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
