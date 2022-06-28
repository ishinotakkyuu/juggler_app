package com.example.title_1;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SpinnerUpgrade {

    String storeNameSql, machineNameSql, tableNumberSql;

    List<String> init_Store_Names = FlagStatistics.init_Store_Names,
            init_Machine_Names = FlagStatistics.init_Machine_Names,
            init_Table_Number = FlagStatistics.init_Table_Number;

    public List<List<String>> getUpGradeItems(Context context, String item, int id) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // リストの二次元配列生成
        List<List<String>> newItemLists = new ArrayList<>();

        // 各種NewItemsを格納するリスト生成、初期値セット
        List<String> new_Store_Names = new ArrayList<>();
        new_Store_Names.add("未選択");
        List<String> new_Machine_Names = new ArrayList<>();
        new_Machine_Names.add("未選択");
        List<String> new_Table_Number = new ArrayList<>();
        new_Table_Number.add("未選択");

        if (!item.equals("未選択")) {

            switch (id) {
                case R.id.StoreSelect:



                    newItemLists.add(new_Machine_Names);
                    newItemLists.add(new_Table_Number);

                    machineNameSql = CreateSQL.store_machine_SQL(item);
                    tableNumberSql = CreateSQL.store_tableNumberSQL(item);

                    try {

                        Cursor machineNameCursor = db.rawQuery(machineNameSql, null);
                        while (machineNameCursor.moveToNext()) {
                            int index = machineNameCursor.getColumnIndex("MACHINE_NAME");
                            String machine = machineNameCursor.getString(index);
                            newItemLists.get(0).add(machine);
                        }

                        Cursor tableNumberCursor = db.rawQuery(tableNumberSql, null);
                        while (tableNumberCursor.moveToNext()) {
                            int index = tableNumberCursor.getColumnIndex("TABLE_NUMBER");
                            String table = tableNumberCursor.getString(index);
                            newItemLists.get(1).add(table);
                        }

                    } finally {
                        if (db != null) {
                            db.close();
                        }
                    }
                    break;

                case R.id.MachineSelect:

                    newItemLists.add(new_Store_Names);
                    newItemLists.add(new_Table_Number);

                    storeNameSql = CreateSQL.machine_store_SQL(item);
                    tableNumberSql = CreateSQL.machine_tableNumberSQL(item);

                    try {

                        Cursor storeNameCursor = db.rawQuery(storeNameSql, null);
                        while (storeNameCursor.moveToNext()) {
                            int index = storeNameCursor.getColumnIndex("STORE_NAME");
                            String store = storeNameCursor.getString(index);
                            newItemLists.get(0).add(store);
                        }

                        Cursor tableNumberCursor = db.rawQuery(tableNumberSql, null);
                        while (tableNumberCursor.moveToNext()) {
                            int index = tableNumberCursor.getColumnIndex("TABLE_NUMBER");
                            String table = tableNumberCursor.getString(index);
                            newItemLists.get(1).add(table);
                        }

                    } finally {
                        if (db != null) {
                            db.close();
                        }
                    }
                    break;

                case R.id.MachineNumberSelect:

                    newItemLists.add(new_Store_Names);
                    newItemLists.add(new_Machine_Names);

                    storeNameSql = CreateSQL.tableNumber_storeSQL(item);
                    machineNameSql = CreateSQL.tableNumber_machineSQL(item);

                    try {

                        Cursor storeNameCursor = db.rawQuery(storeNameSql, null);
                        while (storeNameCursor.moveToNext()) {
                            int index = storeNameCursor.getColumnIndex("STORE_NAME");
                            String store = storeNameCursor.getString(index);
                            newItemLists.get(0).add(store);
                        }

                        Cursor machineNameCursor = db.rawQuery(machineNameSql, null);
                        while (machineNameCursor.moveToNext()) {
                            int index = machineNameCursor.getColumnIndex("MACHINE_NAME");
                            String machine = machineNameCursor.getString(index);
                            newItemLists.get(1).add(machine);
                        }

                    } finally {
                        if (db != null) {
                            db.close();
                        }
                    }
                    break;

            }
            return newItemLists;

        } else {

            // TODO ここに「未選択」が選択された時の処理を記述する

            return newItemLists;
        }
    }

}

