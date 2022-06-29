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
        int storePosition = FlagStatistics.sStore.getSelectedItemPosition();
        if(storePosition != 0) {
            String storeName = (String)FlagStatistics.sStore.getSelectedItem();

            sql = sql + "STORE_NAME = " + "'" + storeName + "'";
        }

        // 機種名
        int machinePosition = FlagStatistics.sMachine.getSelectedItemPosition();
        if(machinePosition != 0){
            String machineName = (String)FlagStatistics.sMachine.getSelectedItem();
            sql = addAnd(sql);

            sql = sql + "MACHINE_NAME = " + "'" + machineName + "'";
        }

        // 台番号(R04.06.27 松沢記述)
        int tableNumberPosition = FlagStatistics.sTableNumber.getSelectedItemPosition();
        if(tableNumberPosition != 0){
            String tableNumber = (String)FlagStatistics.sTableNumber.getSelectedItem();
            sql = addAnd(sql);

            sql = sql + "TABLE_NUMBER = " + "'" + tableNumber + "'";
        }

        // 特殊1
        int spinner01Position = FlagStatistics.sDayDigit.getSelectedItemPosition();
        if(spinner01Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner01Position - 1);

            sql = sql + "OPERATION_DAY_DIGIT = " + "'" + conditionValue + "'";
        }

        // 特殊2
        int spinner02Position = FlagStatistics.sSpecialDay.getSelectedItemPosition();
        if(spinner02Position == 1){
            // ゾロ目
            sql = addAnd(sql);
            sql = sql + "SUBSTR(OPERATION_DATE,9,1) = SUBSTR(OPERATION_DATE,10,1)";
        }else if(spinner02Position == 2){
            // 月と日が同じ
            sql = addAnd(sql);
            sql = sql + "OPERATION_MONTH = OPERATION_DAY";
        }

        // 特殊3
        int spinner03Position = FlagStatistics.sMonth.getSelectedItemPosition();
        if(spinner03Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner03Position);

            sql = sql + "OPERATION_MONTH = " + "'" + conditionValue + "'";
        }

        // 特殊4
        int spinner04Position = FlagStatistics.sDay.getSelectedItemPosition();
        if(spinner04Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner04Position);

            sql = sql + "OPERATION_DAY = " + "'" + conditionValue + "'";
        }

        // 特殊5
        //保留
        int spinner05Position = FlagStatistics.sDayOfWeek_In_Month.getSelectedItemPosition();

        if(spinner05Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner05Position);

            sql = sql + "DAY_OF_WEEK_IN_MONTH = " + "'" + conditionValue + "'";
        }

        // 特殊6
        int spinner06Position = FlagStatistics.sWeekId.getSelectedItemPosition();
        if(spinner06Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner06Position);

            sql = sql + "WEEK_ID = " + "'" + conditionValue + "'";
        }

        // 特殊7
        int spinner07Position = FlagStatistics.sAttachDay.getSelectedItemPosition();
        if(spinner07Position != 0){
            sql = addAnd(sql);
            String conditionValue =  String.valueOf(spinner07Position - 1);

            sql = sql + "SUBSTR(TABLE_NUMBER,length(TABLE_NUMBER)) =" + "'" + conditionValue + "'";
        }

        // 日付の範囲指定(R04.06.27 松沢記述)
        String eDateStart = FlagStatistics.eDateStart.getText().toString();
        if(eDateStart.isEmpty()){
            eDateStart = "1 / 1 / 1";
        }
        String eDateEnd = FlagStatistics.eDateEnd.getText().toString();
        if(eDateEnd.isEmpty()){
            eDateEnd = "9999 / 12 / 31";
        }
        sql = addAnd(sql);
        sql = sql + "OPERATION_DATE BETWEEN" + "'" +  eDateStart + "'" + "AND" + "'" +  eDateEnd + "'";

        if(sql.isEmpty()) {
            sql = initSql  + end;
        }else{
            sql = initSql + where + sql  + end;
        }

        return sql;
    }


    // 店舗名で機種名を絞るSQL
    public static String store_machine_SQL(String item){
        return "SELECT DISTINCT MACHINE_NAME FROM TEST WHERE STORE_NAME = " + "'" + item + "';";
    }

    // 店舗名で台番号を絞るSQL
    public static String store_tableNumberSQL(String item){
        return "SELECT DISTINCT TABLE_NUMBER FROM TEST WHERE STORE_NAME = " + "'" + item + "';";
    }

    // 機種名で店舗名を絞るSQL
    public static String machine_storeSQL(String item){
        return "SELECT DISTINCT STORE_NAME FROM TEST WHERE MACHINE_NAME = " + "'" + item + "';";
    }

    // 機種名で台番号を絞るSQL
    public static String machine_tableNumberSQL(String item){
        return "SELECT DISTINCT TABLE_NUMBER FROM TEST WHERE MACHINE_NAME = " + "'" + item + "';";
    }

    // 台番号で店舗名を絞るSQL
    public static String tableNumber_storeSQL(String item){
        return "SELECT DISTINCT STORE_NAME FROM TEST WHERE TABLE_NUMBER = " + "'" + item + "';";
    }

    // 台番号で機種名を絞るSQL
    public static String tableNumber_machineSQL(String item){
        return "SELECT DISTINCT MACHINE_NAME FROM TEST WHERE TABLE_NUMBER = " + "'" + item + "';";
    }

    public static String notSelect_storeSQL(){

        String initSql = "SELECT DISTINCT STORE_NAME FROM TEST WHERE ";
        String sql = "";

        // 機種名
        int machinePosition = FlagStatistics.sMachine.getSelectedItemPosition();
        if(machinePosition != 0){
            String machineName = (String)FlagStatistics.sMachine.getSelectedItem();
            sql = sql + "MACHINE_NAME = " + "'" + machineName + "'";
        }

        // 台番号
        int tableNumberPosition = FlagStatistics.sTableNumber.getSelectedItemPosition();
        if(tableNumberPosition != 0){
            String tableNumber = (String)FlagStatistics.sTableNumber.getSelectedItem();
            sql = addAnd(sql);
            sql = sql + "TABLE_NUMBER = " + "'" + tableNumber + "'";
        }

        if(sql.isEmpty()){
            return sql;
        } else {
            return initSql + sql + end;
        }
    }




    public static String FlagGradesSQL() {

        String sql = "select * from TEST ORDER BY OPERATION_DATE desc , SAVE_DATE desc;";

        return sql;
    }
    public static String SelectStoreNameSQL() {

        String sql = "select STORE_NAME from TEST ;";

        return sql;
    }

    public static String DataDetailSelectSQL(String id) {

        String sql = "select * from TEST where ID = '" + id +"';";

        return sql;
    }

    public static String addAnd(String sql){

        if(StringUtils.isNotEmpty(sql)) {
            return sql + and;
        }
        return sql;
    }
}
