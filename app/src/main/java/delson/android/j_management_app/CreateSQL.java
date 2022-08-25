package delson.android.j_management_app;

import android.app.Activity;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateSQL {

    static final String where = " WHERE ";
    static final String end = ";";
    static final String and = " AND ";
    static final String order = " ORDER BY ";
    static final String asc = " ASC ";

    public static String FlagStatisticsSQL(Activity activity) {

        String initSql = "select * from " + "TEST";
        String sql = "";

        // 店舗名
        int storePosition = FlagStatistics.sStatisticsSpinner.get(0).getSelectedItemPosition();
        if (storePosition != 0) {
            String storeName = (String) FlagStatistics.sStatisticsSpinner.get(0).getSelectedItem();
            sql = sql + "STORE_NAME = " + "'" + storeName + "'";
        }

        // 機種名
        int machinePosition = FlagStatistics.sStatisticsSpinner.get(1).getSelectedItemPosition();
        if (machinePosition != 0) {
            String machineName = (String) FlagStatistics.sStatisticsSpinner.get(1).getSelectedItem();
            sql = addAnd(sql);
            sql = sql + "MACHINE_NAME = " + "'" + machineName + "'";
        }

        // 台番号
        int tableNumberPosition = FlagStatistics.sStatisticsSpinner.get(2).getSelectedItemPosition();
        if (tableNumberPosition != 0) {
            String tableNumber = (String) FlagStatistics.sStatisticsSpinner.get(2).getSelectedItem();
            sql = addAnd(sql);

            sql = sql + "TABLE_NUMBER = " + "'" + tableNumber + "'";
        }

        // 特殊1
        int spinner01Position = FlagStatistics.sStatisticsSpinner.get(3).getSelectedItemPosition();
        if (spinner01Position != 0) {
            sql = addAnd(sql);

            List<String> DAY_DIGIT = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_DIGIT)));
            String searchItem = FlagStatistics.sStatisticsSpinner.get(3).getSelectedItem().toString();
            String conditionValue = String.valueOf(DAY_DIGIT.indexOf(searchItem));

            sql = sql + "OPERATION_DAY_DIGIT = " + "'" + conditionValue + "'";
        }

        // 特殊2
        String spinner02Item = FlagStatistics.sStatisticsSpinner.get(4).getSelectedItem().toString();
        if(!spinner02Item.equals("未選択")){
            sql = addAnd(sql);
            switch(spinner02Item){
                case "ゾロ目":
                    sql = sql + "(SPECIAL_DAY = '1' OR SPECIAL_DAY = '3')";
                    break;
                case "月と日が同じ":
                    sql = sql + "(SPECIAL_DAY = '2' OR SPECIAL_DAY = '3')";
                    break;
            }
        }

        // 特殊3
        int spinner03Position = FlagStatistics.sStatisticsSpinner.get(5).getSelectedItemPosition();
        if (spinner03Position != 0) {
            sql = addAnd(sql);

            List<String> MONTH = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_MONTH)));
            String searchItem = FlagStatistics.sStatisticsSpinner.get(5).getSelectedItem().toString();
            String conditionValue = String.valueOf(MONTH.indexOf(searchItem) + 1);

            sql = sql + "OPERATION_MONTH = " + "'" + conditionValue + "'";
        }

        // 特殊4
        int spinner04Position = FlagStatistics.sStatisticsSpinner.get(6).getSelectedItemPosition();
        if (spinner04Position != 0) {
            sql = addAnd(sql);

            List<String> DAY = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_DAY)));
            String searchItem = FlagStatistics.sStatisticsSpinner.get(6).getSelectedItem().toString();
            String conditionValue = String.valueOf(DAY.indexOf(searchItem) + 1);

            sql = sql + "OPERATION_DAY = " + "'" + conditionValue + "'";
        }

        // 特殊5
        int spinner05Position = FlagStatistics.sStatisticsSpinner.get(7).getSelectedItemPosition();
        if (spinner05Position != 0) {
            sql = addAnd(sql);

            List<String> DAY_OF_WEEK_IN_MONTH = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_WEEK_IN_MONTH)));
            String searchItem = FlagStatistics.sStatisticsSpinner.get(7).getSelectedItem().toString();
            String conditionValue = String.valueOf(DAY_OF_WEEK_IN_MONTH.indexOf(searchItem) + 1);

            sql = sql + "DAY_OF_WEEK_IN_MONTH = " + "'" + conditionValue + "'";
        }

        // 特殊6
        int spinner06Position = FlagStatistics.sStatisticsSpinner.get(8).getSelectedItemPosition();
        if (spinner06Position != 0) {
            sql = addAnd(sql);

            List<String> WEEK_ID = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_WEEK_ID)));
            String searchItem = FlagStatistics.sStatisticsSpinner.get(8).getSelectedItem().toString();
            String conditionValue = String.valueOf(WEEK_ID.indexOf(searchItem) + 1);

            sql = sql + "WEEK_ID = " + "'" + conditionValue + "'";
        }

        // 特殊7
        int spinner07Position = FlagStatistics.sStatisticsSpinner.get(9).getSelectedItemPosition();
        if (spinner07Position != 0) {
            sql = addAnd(sql);

            List<String> TailNumber = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_TAIL_NUMBER)));
            String searchItem = FlagStatistics.sStatisticsSpinner.get(9).getSelectedItem().toString();
            String conditionValue = String.valueOf(TailNumber.indexOf(searchItem));

            sql = sql + "TAIL_NUMBER = " + "'" + conditionValue + "'";
        }


        // 日付の範囲指定(R04.06.27 松沢記述)
        String eDateStart = FlagStatistics.eStatisticsEditDate.get(0).getText().toString();
        if (eDateStart.isEmpty()) {
            eDateStart = "1 / 1 / 1";
        }
        String eDateEnd = FlagStatistics.eStatisticsEditDate.get(1).getText().toString();
        if (eDateEnd.isEmpty()) {
            eDateEnd = "9999 / 12 / 31";
        }
        sql = addAnd(sql);
        sql = sql + "OPERATION_DATE BETWEEN " + "'" + eDateStart + "'" + and + "'" + eDateEnd + "'";

        if (sql.isEmpty()) {
            sql = initSql + end;
        } else {
            sql = initSql + where + sql + end;
        }

        return sql;
    }

    public static String selectSpinnerItemSQL(String columnName,Activity activity) {

        String initSql = "SELECT DISTINCT " + columnName + " FROM TEST WHERE ";
        String sql = "";

        // 店舗名
        if (!columnName.equals("STORE_NAME")) {
            int storePosition = FlagStatistics.sStatisticsSpinner.get(0).getSelectedItemPosition();
            if (storePosition != 0) {
                String storeName = (String) FlagStatistics.sStatisticsSpinner.get(0).getSelectedItem();
                sql = sql + "STORE_NAME = " + "'" + storeName + "'";
            }
        }

        // 機種名
        if (!columnName.equals("MACHINE_NAME")) {
            int machinePosition = FlagStatistics.sStatisticsSpinner.get(1).getSelectedItemPosition();
            if (machinePosition != 0) {
                String machineName = (String) FlagStatistics.sStatisticsSpinner.get(1).getSelectedItem();
                sql = addAnd(sql);
                sql = sql + "MACHINE_NAME = " + "'" + machineName + "'";
            }
        }

        // 台番号
        if (!columnName.equals("TABLE_NUMBER")) {
            int tableNumberPosition = FlagStatistics.sStatisticsSpinner.get(2).getSelectedItemPosition();
            if (tableNumberPosition != 0) {
                String tableNumber = (String) FlagStatistics.sStatisticsSpinner.get(2).getSelectedItem();
                sql = addAnd(sql);
                sql = sql + "TABLE_NUMBER = " + "'" + tableNumber + "'";
            }
        }

        // 特殊1
        if (!columnName.equals("OPERATION_DAY_DIGIT")) {
            int spinner01Position = FlagStatistics.sStatisticsSpinner.get(3).getSelectedItemPosition();
            if (spinner01Position != 0) {
                sql = addAnd(sql);

                List<String> DAY_DIGIT = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_DIGIT)));
                String searchItem = FlagStatistics.sStatisticsSpinner.get(3).getSelectedItem().toString();
                String conditionValue = String.valueOf(DAY_DIGIT.indexOf(searchItem));

                sql = sql + "OPERATION_DAY_DIGIT = " + "'" + conditionValue + "'";

            }
        }

        // 特殊2
        if (!columnName.equals("SPECIAL_DAY")) {
            String spinner02Item = FlagStatistics.sStatisticsSpinner.get(4).getSelectedItem().toString();
            if(!spinner02Item.equals("未選択")){
                sql = addAnd(sql);
                switch(spinner02Item){
                    case "ゾロ目":
                        sql = sql + "(SPECIAL_DAY = '1' OR SPECIAL_DAY = '3')";
                        break;
                    case "月と日が同じ":
                        sql = sql + "(SPECIAL_DAY = '2' OR SPECIAL_DAY = '3')";
                        break;
                }

            }
        }

        // 特殊3
        if (!columnName.equals("OPERATION_MONTH")) {
            int spinner03Position = FlagStatistics.sStatisticsSpinner.get(5).getSelectedItemPosition();
            if (spinner03Position != 0) {
                sql = addAnd(sql);

                List<String> MONTH = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_MONTH)));
                String searchItem = FlagStatistics.sStatisticsSpinner.get(5).getSelectedItem().toString();
                String conditionValue = String.valueOf(MONTH.indexOf(searchItem) + 1);

                sql = sql + "OPERATION_MONTH = " + "'" + conditionValue + "'";

            }
        }

        // 特殊4
        if (!columnName.equals("OPERATION_DAY")) {
            int spinner04Position = FlagStatistics.sStatisticsSpinner.get(6).getSelectedItemPosition();
            if (spinner04Position != 0) {
                sql = addAnd(sql);

                List<String> DAY = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_DAY)));
                String searchItem = FlagStatistics.sStatisticsSpinner.get(6).getSelectedItem().toString();
                String conditionValue = String.valueOf(DAY.indexOf(searchItem) + 1);

                sql = sql + "OPERATION_DAY = " + "'" + conditionValue + "'";

            }
        }

        // 特殊5
        if (!columnName.equals("DAY_OF_WEEK_IN_MONTH")) {
            int spinner05Position = FlagStatistics.sStatisticsSpinner.get(7).getSelectedItemPosition();
            if (spinner05Position != 0) {
                sql = addAnd(sql);

                List<String> DAY_OF_WEEK_IN_MONTH = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_WEEK_IN_MONTH)));
                String searchItem = FlagStatistics.sStatisticsSpinner.get(7).getSelectedItem().toString();
                String conditionValue = String.valueOf(DAY_OF_WEEK_IN_MONTH.indexOf(searchItem) + 1);

                sql = sql + "DAY_OF_WEEK_IN_MONTH = " + "'" + conditionValue + "'";

            }
        }

        // 特殊6
        if (!columnName.equals("WEEK_ID")) {
            int spinner06Position = FlagStatistics.sStatisticsSpinner.get(8).getSelectedItemPosition();
            if (spinner06Position != 0) {
                sql = addAnd(sql);

                List<String> WEEK_ID = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_WEEK_ID)));
                String searchItem = FlagStatistics.sStatisticsSpinner.get(8).getSelectedItem().toString();
                String conditionValue = String.valueOf(WEEK_ID.indexOf(searchItem) + 1);

                sql = sql + "WEEK_ID = " + "'" + conditionValue + "'";

            }
        }

        // 特殊7
        if (!columnName.equals("TAIL_NUMBER")) {
            int spinner07Position = FlagStatistics.sStatisticsSpinner.get(9).getSelectedItemPosition();
            if (spinner07Position != 0) {
                sql = addAnd(sql);

                List<String> TailNumber = new ArrayList<>(Arrays.asList(activity.getResources().getStringArray(R.array.SET_TAIL_NUMBER)));
                String searchItem = FlagStatistics.sStatisticsSpinner.get(9).getSelectedItem().toString();
                String conditionValue = String.valueOf(TailNumber.indexOf(searchItem));

                sql = sql + "TAIL_NUMBER = " + "'" + conditionValue + "'";

            }
        }

        // 日付
        String eDateStart = FlagStatistics.eStatisticsEditDate.get(0).getText().toString();
        String eDateEnd = FlagStatistics.eStatisticsEditDate.get(1).getText().toString();
        if(StringUtils.isNotEmpty(eDateStart) || StringUtils.isNotEmpty(eDateEnd)){
            if (eDateStart.isEmpty()) {
                eDateStart = "1 / 1 / 1";
            }
            if (eDateEnd.isEmpty()) {
                eDateEnd = "9999 / 12 / 31";
            }
            sql = addAnd(sql);
            sql = sql + "OPERATION_DATE BETWEEN " + "'" + eDateStart + "'" + and + "'" + eDateEnd + "'";
        }



        if (sql.isEmpty()) {
            return sql;
        } else {
            return initSql + sql + order + columnName + asc + end;
        }
    }

    public static String FlagGradesSQL() {

        String sql = "select * from TEST ORDER BY OPERATION_DATE desc , SAVE_DATE desc;";

        return sql;
    }

    public static String DataDetailSelectSQL(String id) {

        String sql = "select * from TEST where ID = '" + id + "';";

        return sql;
    }

    public static String addAnd(String sql) {

        if (StringUtils.isNotEmpty(sql)) {
            return sql + and;
        }
        return sql;
    }


}
