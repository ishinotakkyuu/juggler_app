package delson.android.j_management_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.apache.commons.lang3.StringUtils;

public class DatabaseResultSet {

    protected static void execution(String flg, Context context, String sql) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {

            Cursor cursor = db.rawQuery(sql, null);

            switch (flg) {
                case "FlagStatistics":
                    while (cursor.moveToNext()) {
                        FlagStatisticsSelect(cursor);
                    }
                    // 対象データ数の取得
                    FlagStatistics.dataCount = cursor.getCount();
                    break;

                case "FlagGrades":
                    while (cursor.moveToNext()) {
                        FlagGradesSelect(cursor);
                    }
                    break;

                case "DataDetailSelect":
                    while (cursor.moveToNext()) {
                        DataDetailSelect(cursor);
                    }
                    break;

                case "ToolDesignList":
                    int No = 1;
                    while (cursor.moveToNext()) {
                        ToolDesignListSelect(cursor, No);
                        No++;
                    }
                    break;
            }

        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    protected static void UpdateOrDelete(Context context, String sql) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            // トランザクションの開始
            db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(sql);

            // SQLの実行
            stmt.executeUpdateDelete();

            // コミット
            db.setTransactionSuccessful();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    protected static void FlagStatisticsSelect(Cursor cursor) {

        int index = cursor.getColumnIndex("TOTAL_GAME");
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

    }

    protected static void DataDetailSelect(Cursor cursor) {

        // 稼働日(OPERATION_DATE)は前画面から渡されてきているのでそちらを利用
        // 保存日時(SAVE_DATE)は更新時に取得するので不要
        // 店舗名(STORE_NAME)は前画面から渡されてきているのでそちらを利用

        int index = cursor.getColumnIndex("OPERATION_YEAR");
        DataDetail.dbOperationYearValue = cursor.getInt(index);

        index = cursor.getColumnIndex("OPERATION_MONTH");
        DataDetail.dbOperationMonthValue = cursor.getInt(index);

        index = cursor.getColumnIndex("OPERATION_DAY");
        DataDetail.dbOperationDayValue = cursor.getInt(index);

        index = cursor.getColumnIndex("OPERATION_DAY_DIGIT");
        DataDetail.dbOperationDayDigitValue = cursor.getInt(index);

        index = cursor.getColumnIndex("WEEK_ID");
        DataDetail.dbWeekIdValue = cursor.getInt(index);

        index = cursor.getColumnIndex("DAY_OF_WEEK_IN_MONTH");
        DataDetail.dbDayOfWeek_in_MonthValue = cursor.getInt(index);

        index = cursor.getColumnIndex("DIFFERENCE_NUMBER");
        DataDetail.dbMedalValue = cursor.getInt(index);

        // 機種名(MACHINE_NAME)は前画面から渡されてきているのでそちらを利用

        index = cursor.getColumnIndex("TABLE_NUMBER");
        DataDetail.dbTableNumberValue = cursor.getString(index);

        index = cursor.getColumnIndex("START_GAME");
        DataDetail.dbStartValue = cursor.getInt(index);

        index = cursor.getColumnIndex("TOTAL_GAME");
        DataDetail.dbTotalValue = cursor.getInt(index);

        index = cursor.getColumnIndex("SINGLE_BIG");
        DataDetail.dbSingleBigValue = cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY_BIG");
        DataDetail.dbCherryRegValue = cursor.getInt(index);

        index = cursor.getColumnIndex("SINGLE_REG");
        DataDetail.dbSingleRegValue = cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY_REG");
        DataDetail.dbCherryBigValue = cursor.getInt(index);

        index = cursor.getColumnIndex("CHERRY");
        DataDetail.dbCherryValue = cursor.getInt(index);

        index = cursor.getColumnIndex("GRAPE");
        DataDetail.dbGrapeValue = cursor.getInt(index);

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

        // 台番号
        index = cursor.getColumnIndex("TABLE_NUMBER");
        String tableNumber = cursor.getString(index);

        // 稼働日
        index = cursor.getColumnIndex("OPERATION_DATE");
        String operationDate = cursor.getString(index);

        // 登録日時
        index = cursor.getColumnIndex("SAVE_DATE");
        String saveDate = cursor.getString(index);

        FlagGrades.listItems.add(new FlagGradesListItems(id, machineName, storeName, tableNumber, operationDate, saveDate));
    }

    protected static void ToolDesignListSelect(Cursor cursor, int No) {

        // ID
        int index = cursor.getColumnIndex("ID");
        String id = String.valueOf(cursor.getLong(index));

        // 登録日時
        index = cursor.getColumnIndex("SAVE_DATE");
        String saveDate = cursor.getString(index);

        // 台番号
        index = cursor.getColumnIndex("TABLE_NUMBER");
        String tableNumber = "";
        if (StringUtils.isNotEmpty(cursor.getString(index))) {
            tableNumber = cursor.getString(index) + "番台";
        }

        // 表示する文字列作成
        String designText = " No." + No + "  " + saveDate + "  " + tableNumber;

        // ナンバリング取得
        String no = String.valueOf(No);

        ToolDesignList.designs.add(new ToolDesignListItems(id, no, designText));
    }
}
