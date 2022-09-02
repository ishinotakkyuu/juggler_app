package delson.android.j_management_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jugglerApp.db";
    private static final int DATABASE_VERSION = 2;

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

        // 出目保存用テーブル
        createDesignTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*
          ネットで調べたところ、onUpgrade内ではバージョンによって実行するDDLを管理することになっている。
          イコールを使っている例をいくつか見かけましたが、実運用に入ると都合が悪いケースがある。
          例えば、アップデートをしばらく保留にしていたユーザーが、DBバージョンを3から5へ一気に上げた場合、
          newVersion == 4と指定していると、そのDDLが実行されないことになる。
         */

        db.beginTransaction();
        try {
            //DBバージョン2でDESIGNテーブル作成。
            createDesignTable(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void createDesignTable(SQLiteDatabase db){

        String design =
                "CREATE TABLE DESIGN ( " +
                        "ID INTEGER PRIMARY KEY," +
                        "STORE_NAME TEXT," +
                        "TABLE_NUMBER TEXT," +
                        "SAVE_DATE TEXT," +
                        "DESIGN_01 TEXT," +
                        "DESIGN_02 TEXT," +
                        "DESIGN_03 TEXT," +
                        "DESIGN_04 TEXT," +
                        "DESIGN_05 TEXT," +
                        "DESIGN_06 TEXT," +
                        "DESIGN_07 TEXT," +
                        "DESIGN_08 TEXT," +
                        "DESIGN_09 TEXT" +
                        ")";

        db.execSQL(design);
    }


}
