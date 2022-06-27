package com.example.title_1;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SpinnerUpgrade {


    public List<String> machineItemsUpgrade(Context context, String item) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        List<String> newItems = new ArrayList<>();
        newItems.add("未選択");


        String machineNameSql = CreateSQL.upMachineNamesSQL(item);

        try {
            Cursor machineNameCursor = db.rawQuery(machineNameSql, null);
            while (machineNameCursor.moveToNext()) {
                int index = machineNameCursor.getColumnIndex("MACHINE_NAME");
                String machine = machineNameCursor.getString(index);
                newItems.add(machine);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return newItems;
    }

    public List<String> tableNumberItemsUpgrade(Context context, String item) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        List<String> newItems = new ArrayList<>();
        newItems.add("未選択");


        String tableNumberSQL = CreateSQL.upTableNumberSQL(item);

        try {
            Cursor tableNumberCursor = db.rawQuery(tableNumberSQL, null);
            while (tableNumberCursor.moveToNext()) {
                int index = tableNumberCursor.getColumnIndex("TABLE_NUMBER");
                String machine = tableNumberCursor.getString(index);
                newItems.add(machine);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return newItems;
    }


}

