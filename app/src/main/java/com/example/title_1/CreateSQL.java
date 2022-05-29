package com.example.title_1;

import org.apache.commons.lang3.StringUtils;

public class CreateSQL {

    static final String where = " WHERE ";
    static final String end = ";";
    static final String and = " AND ";

    public static String FlagStatisticsSQL(){

        String initSql = "select * from " + "TEST";
        String sql = "";

        // 店舗名
        int storePosition = FlagStatistics.storeSpinner.getSelectedItemPosition();

        // 店舗名を選択していた場合
        if(storePosition != 0) {
            String storeName = (String)FlagStatistics.storeSpinner.getSelectedItem();

            sql = sql + "STORE_NAME = " + "'" + storeName + "'";
        }

        // 機種名
        int machinePosition = FlagStatistics.machineSpinner.getSelectedItemPosition();

        if(machinePosition != 0){
            String machineName = (String)FlagStatistics.machineSpinner.getSelectedItem();
            sql = addAnd(sql);

            sql = sql + "MACHINE_NAME = " + "'" + machineName + "'";
        }

        // 特殊1
        int spinner01Position = FlagStatistics.specialSpinner_01.getSelectedItemPosition();
        if(spinner01Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner01Position - 1);

            sql = sql + "OPERATION_DAY_DIGIT = " + "'" + conditionValue + "'";
        }

        // 特殊2
        int spinner02Position = FlagStatistics.specialSpinner_02.getSelectedItemPosition();
        if(spinner02Position == 1){
            // ぞろめ
            sql = addAnd(sql);
            sql = sql + "SUBSTR(OPERATION_DATE,9,1) = SUBSTR(OPERATION_DATE,10,1)";
        }else if(spinner02Position == 2){
            // 月と日が同じ
            sql = addAnd(sql);
            sql = sql + "OPERATION_MONTH = OPERATION_DAY";
        }

        // 特殊3
        int spinner03Position = FlagStatistics.specialSpinner_03.getSelectedItemPosition();
        if(spinner03Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner03Position);

            sql = sql + "OPERATION_MONTH = " + "'" + conditionValue + "'";
        }

        // 特殊4
        int spinner04Position = FlagStatistics.specialSpinner_04.getSelectedItemPosition();
        if(spinner04Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner04Position);

            sql = sql + "OPERATION_DAY = " + "'" + conditionValue + "'";
        }

        // 特殊5
        //保留
        int spinner05Position = FlagStatistics.specialSpinner_05.getSelectedItemPosition();

        if(spinner05Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner05Position);

            sql = sql + "DAY_OF_WEEK_IN_MONTH = " + "'" + conditionValue + "'";
        }

        // 特殊6
        int spinner06Position = FlagStatistics.specialSpinner_06.getSelectedItemPosition();
        if(spinner06Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner06Position);

            sql = sql + "WEEK_ID = " + "'" + conditionValue + "'";
        }

        if(sql.isEmpty()) {
            sql = initSql  + end;
        }else{
            sql = initSql + where + sql  + end;
        }

        return sql;
    }

    public static String addAnd(String sql){

        if(StringUtils.isNotEmpty(sql)) {
            return sql + and;
        }
        return sql;
    }
}
