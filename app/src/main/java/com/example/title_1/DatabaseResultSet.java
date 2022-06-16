package com.example.title_1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseResultSet {

    protected static void execution(String flg, Context context, String sql) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        Log.i("SQLITE","sql : " + sql);

        try {

            Cursor cursor = db.rawQuery(sql,null);

            switch (flg) {
                case "FlagStatistics":
                    while(cursor.moveToNext()) {
                        FlagStatisticsSelect(cursor);
                    }
                    break;

                case "FlagGrades":
                    while(cursor.moveToNext()) {
                        FlagGradesSelect(cursor);
                    }
                    break;

                case "DataDetailSelect":
                    while(cursor.moveToNext()) {
                        DataDetailSelect(cursor);
                    }
                    break;

                case "DataDetailSelect2":
                    while(cursor.moveToNext()) {
                        DataDetailSelect2(cursor);
                    }
                    break;

                case "DataDetailDelete":
                    while(cursor.moveToNext()) {
                        DataDetailDelete(cursor);
                    }
                    break;
            }
        }finally{
            if(db != null) {
                db.close();
            }
        }
    }

    protected static void UpdateOrDelete(Context context, String sql) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        Log.i("SQLITE","sql : " + sql);

        try {
            // トランザクションの開始
            db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(sql);

            // SQLの実行
            stmt.executeUpdateDelete();

            // コミット
            db.setTransactionSuccessful();

        }finally{
            if(db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    protected static void FlagStatisticsSelect(Cursor cursor) {

        int index = cursor.getColumnIndex("ID");
        String id = String.valueOf(cursor.getLong(index));

        index = cursor.getColumnIndex("OPERATION_DATE");
        String operationDate = cursor.getString(index);

        index = cursor.getColumnIndex("STORE_NAME");
        String storeName = cursor.getString(index);

        index = cursor.getColumnIndex("TOTAL_GAME");
        int index2 = cursor.getColumnIndex("START_GAME");
        FlagStatistics.dbTotalGamesValue = FlagStatistics.dbTotalGamesValue + (cursor.getInt(index) - cursor.getInt(index2));

        index = cursor.getColumnIndex("DIFFERENCE_NUMBER");
        FlagStatistics.dbTotalMedalValue = FlagStatistics.dbTotalMedalValue + cursor.getInt(index);

        index = cursor.getColumnIndex("SINGLE_BIG");
        FlagStatistics.dbTotalSingleBigValue = FlagStatistics.dbTotalSingleBigValue + cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY_BIG");
        FlagStatistics.dbTotalCherryBigValue = FlagStatistics.dbTotalCherryBigValue + cursor.getInt(index);

        index = cursor.getColumnIndex("SINGLE_REG");
        FlagStatistics.dbTotalSingleRegValue = FlagStatistics.dbTotalSingleRegValue + cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY_REG");
        FlagStatistics.dbTotalCherryRegValue = FlagStatistics.dbTotalCherryRegValue + cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY");
        FlagStatistics.dbTotalCherryValue = FlagStatistics.dbTotalCherryValue + cursor.getInt(index);

        index = cursor.getColumnIndex("GRAPE");
        FlagStatistics.dbTotalGrapeValue = FlagStatistics.dbTotalGrapeValue + cursor.getInt(index);

        // ログに出力する(Android Studioの下部にあるログキャットで確認可能)
        Log.i("SQLITE", "_id : " + id + " " +
                "OPERATION_DATE : " + operationDate + " " +
                "STORE_NAME : " + storeName + " ");
    }

    protected static void DataDetailSelect(Cursor cursor) {

        // ID
        int index = cursor.getColumnIndex("ID");
        String id = String.valueOf(cursor.getLong(index));

        index = cursor.getColumnIndex("OPERATION_DATE");
        DataDetail.dbOperationDateValue = cursor.getString(index);

        index = cursor.getColumnIndex("DIFFERENCE_NUMBER");
        DataDetail.dbMedalValue = cursor.getInt(index);

        index = cursor.getColumnIndex("TABLE_NUMBER");
        DataDetail.dbTableNumberValue = cursor.getString(index);

        index = cursor.getColumnIndex("START_GAME");
        DataDetail.getDBStartValue = cursor.getInt(index);

        index = cursor.getColumnIndex("TOTAL_GAME");
        DataDetail.getDBTotalValue = cursor.getInt(index);

        index = cursor.getColumnIndex("SINGLE_BIG");
        DataDetail.getDBSingleBigValue = cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY_REG");
        DataDetail.getDBCherryBigValue = cursor.getInt(index);

        index = cursor.getColumnIndex("SINGLE_REG");
        DataDetail.getDBSingleRegValue = cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY_BIG");
        DataDetail.getDBCherryRegValue = cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY");
        DataDetail.getDBCherryValue = cursor.getInt(index);

        index = cursor.getColumnIndex("GRAPE");
        DataDetail.getDBGrapeValue = cursor.getInt(index);

        // ログに出力する(Android Studioの下部にあるログキャットで確認可能)
        Log.i("SQLITE", "_id : " + id + " ");

    }

    protected static void DataDetailSelect2(Cursor cursor) {

        int index = cursor.getColumnIndex("STORE_NAME");
        DataDetail.DB_Store.add(cursor.getString(index));

        // ログに出力する(Android Studioの下部にあるログキャットで確認可能)
        Log.i("SQLITE", "STORE_NAME : " + index + " ");
    }

    protected static void FlagGradesSelect(Cursor cursor) {
        // ID
        int index = cursor.getColumnIndex("ID");
        String id = String.valueOf(cursor.getLong(index));

        // 機種名
        index = cursor.getColumnIndex("MACHINE_NAME");
        String machineName = cursor.getString(index);

        // 店舗名
        index = cursor.getColumnIndex("STORE_NAME");
        String storeName = cursor.getString(index);

        // 稼働日
        index = cursor.getColumnIndex("OPERATION_DATE");
        String operationDate = cursor.getString(index);

        // 登録日時
        index = cursor.getColumnIndex("SAVE_DATE");
        String saveDate = cursor.getString(index);

        // ログに出力する(Android Studioの下部にあるログキャットで確認可能)
        Log.i("SQLITE", "_id : " + id + " ");

        FlagGrades.listItems.add(new FlagGradesListItems(id,machineName,storeName,operationDate,saveDate));
    }

    protected static void DataDetailDelete(Cursor cursor) {


    }
}
