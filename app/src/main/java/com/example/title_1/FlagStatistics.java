package com.example.title_1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FlagStatistics extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Context context;
    View view;

    // 共有データ
    static MainApplication mainApplication = null;

    // レイアウト
    ConstraintLayout mainLayout;

    // 日付表示用のEditText
    static EditText eDateStart, eDateEnd;

    // 各種スピナーとそれぞれに対応するチェックボックス
    static Spinner sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber;

    // データを表示するためのボタン
    Button bDisplay, bClear;

    // タイトル表示に使用するTextView
    TextView tTittleTotalGames, tTittleMedal, tTittleDiscount, tTittleSingleBig, tTittleCherryBig, tTittleTotalBig,
            tTittleSingleReg, tTittleCherryReg, tTittleTotalReg, tTittleTotalBonus, tTittleGrape, tTittleCherry;

    // データ表示に使用するTextView
    TextView tTotalGames, tTotalMedal, tDiscount,
            tTotalSingleBig, tTotalSingleBigProbability,
            tTotalCherryBig, tTotalCherryBigProbability,
            tTotalBig, tTotalBigProbability,
            tTotalSingleReg, tTotalSingleRegProbability,
            tTotalCherryReg, tTotalCherryRegProbability,
            tTotalReg, tTotalRegProbability,
            tTotalBonus, tTotalBonusProbability,
            tTotalGrape, tTotalGrapeProbability,
            tTotalCherry, tTotalCherryProbability;

    // 画面下部のスクロール固定処理用(ダークモード対応)
    ScrollView scrollView;

    // DBから値を取得
    static int dbTotalGamesValue, dbTotalMedalValue, dbTotalSingleBigValue, dbTotalCherryBigValue,
            dbTotalSingleRegValue, dbTotalCherryRegValue, dbTotalCherryValue, dbTotalGrapeValue;

    // DB値から算出するもの
    int calTotalBigValue, calTotalRegValue, calTotalBonusValue;
    double calDiscountValue, calTotalSingleBigProbabilityValue, calTotalCherryBigProbabilityValue, calTotalBigProbabilityValue,
            calTotalSingleRegProbabilityValue, calTotalCherryRegProbabilityValue, calTotalRegProbabilityValue,
            calTotalBonusProbabilityValue, calTotalCherryProbabilityValue, calTotalGrapeProbabilityValue;

    // 各種スピナーにセットする配列
    List<String> Store_Names, Machine_Names, Table_Number, DAY_DIGIT, SPECIAL_DAY, MONTH, DAY, DayOfWeek_In_Month, WEEK_ID, TailNumber;
    // 配列初期値格納用
    List<String> init_Store_Names, init_Machine_Names, init_Table_Number, init_DAY_DIGIT, init_SPECIAL_DAY,
            init_MONTH, init_DAY, init_DayOfWeek_In_Month, init_WEEK_ID, init_TailNumber;

    // スピナーに設定するリスナー
    AdapterView.OnItemSelectedListener listener = this;

    // 定数
    final String FORMAT = "%.2f";
    final String TIMES = "回";
    final String NUMERATOR = "1/";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main04_statistics01, container, false);

        mainApplication = (MainApplication) getActivity().getApplication();

        context = getActivity().getApplicationContext();

        // パフォーマンスを考慮し、画面起動時に必要最小限ViewにIDを設定
        setFindViewById_01(view);
        // 各種スピナーに項目をセット
        setSpinnerData();
        // 初回起動時の「未選択」処理回避のためFocusableをfalseにしておく
        setFocusable();
        // クリックリスナーの登録
        setClickListener();
        // スピナーにリスナー登録
        setItemSelectedListener();
        // 画面下部のスクロール固定
        setScrollEnable(false);

        return view;
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Date_01:
                final Calendar calender_01 = Calendar.getInstance();
                DatePickerDialog datePickerDialog_01 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 選択した日付を取得して日付表示用のEditTextにセット
                        eDateStart.setText(String.format("%d / %02d / %02d", year, month + 1, dayOfMonth));
                    }
                },
                        calender_01.get(Calendar.YEAR),
                        calender_01.get(Calendar.MONTH),
                        calender_01.get(Calendar.DATE)
                );
                //dialogを表示
                datePickerDialog_01.show();
                break;

            case R.id.Date_02:
                final Calendar calender_02 = Calendar.getInstance();
                DatePickerDialog datePickerDialog_02 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 選択した日付を取得して日付表示用のEditTextにセット
                        eDateEnd.setText(String.format("%d / %02d / %02d", year, month + 1, dayOfMonth));
                    }
                },
                        calender_02.get(Calendar.YEAR),
                        calender_02.get(Calendar.MONTH),
                        calender_02.get(Calendar.DATE)
                );
                //dialogを表示
                datePickerDialog_02.show();
                break;

            case R.id.DisplayButton:

                // 統計表示に必要なViewのIDを設定
                if (tTittleTotalGames == null) {
                    setFindViewById_02(view);
                }

                initValue();
                setTittle();
                setScrollEnable(true);

                String sql = CreateSQL.FlagStatisticsSQL();
                DatabaseResultSet.execution("FlagStatistics", context, sql);

                calTotalBigValue = dbTotalSingleBigValue + dbTotalCherryBigValue;
                calTotalRegValue = dbTotalSingleRegValue + dbTotalCherryRegValue;
                calTotalBonusValue = calTotalBigValue + calTotalRegValue;

                if (dbTotalGamesValue > 0) {
                    calDiscountValue = (division(dbTotalGamesValue * 3 + dbTotalMedalValue, dbTotalGamesValue * 3)) * 100;
                    calTotalSingleBigProbabilityValue = division(dbTotalGamesValue, dbTotalSingleBigValue);
                    calTotalCherryBigProbabilityValue = division(dbTotalGamesValue, dbTotalSingleBigValue);
                    calTotalBigProbabilityValue = division(dbTotalGamesValue, calTotalBigValue);
                    calTotalSingleRegProbabilityValue = division(dbTotalGamesValue, dbTotalSingleRegValue);
                    calTotalCherryRegProbabilityValue = division(dbTotalGamesValue, dbTotalCherryRegValue);
                    calTotalRegProbabilityValue = division(dbTotalGamesValue, calTotalRegValue);
                    calTotalBonusProbabilityValue = division(dbTotalGamesValue, calTotalBonusValue);
                    calTotalCherryProbabilityValue = division(dbTotalGamesValue, dbTotalCherryValue);
                    calTotalGrapeProbabilityValue = division(dbTotalGamesValue, dbTotalGrapeValue);
                }

                NumberFormat nfNum = NumberFormat.getNumberInstance();
                nfNum.setMaximumFractionDigits(1);

                // 値を各Viewにセット
                tTotalGames.setText(Math.round(dbTotalGamesValue) + "回転");
                tTotalMedal.setText(Math.round(dbTotalMedalValue) + "枚");
                if (calDiscountValue > 0) {
                    tDiscount.setText(String.format(FORMAT, calDiscountValue) + "%");
                } else {
                    tDiscount.setText("0.00%");
                }
                tTotalSingleBig.setText(dbTotalSingleBigValue + TIMES);
                tTotalSingleBigProbability.setText(NUMERATOR + String.format(FORMAT, calTotalSingleBigProbabilityValue));
                tTotalCherryBig.setText(dbTotalCherryBigValue + TIMES);
                tTotalCherryBigProbability.setText(NUMERATOR + String.format(FORMAT, calTotalCherryBigProbabilityValue));
                tTotalBig.setText(calTotalBigValue + TIMES);
                tTotalBigProbability.setText(NUMERATOR + String.format(FORMAT, calTotalBigProbabilityValue));
                tTotalSingleReg.setText(dbTotalSingleRegValue + TIMES);
                tTotalSingleRegProbability.setText(NUMERATOR + String.format(FORMAT, calTotalSingleRegProbabilityValue));
                tTotalCherryReg.setText(dbTotalCherryRegValue + TIMES);
                tTotalCherryRegProbability.setText(NUMERATOR + String.format(FORMAT, calTotalCherryRegProbabilityValue));
                tTotalReg.setText(calTotalRegValue + TIMES);
                tTotalRegProbability.setText(NUMERATOR + String.format(FORMAT, calTotalRegProbabilityValue));
                tTotalBonus.setText(calTotalBonusValue + TIMES);
                tTotalBonusProbability.setText(NUMERATOR + String.format(FORMAT, calTotalBonusProbabilityValue));
                tTotalGrape.setText(dbTotalGrapeValue + TIMES);
                tTotalGrapeProbability.setText(NUMERATOR + String.format(FORMAT, calTotalGrapeProbabilityValue));
                tTotalCherry.setText(dbTotalCherryValue + TIMES);
                tTotalCherryProbability.setText(NUMERATOR + String.format(FORMAT, calTotalCherryProbabilityValue));
                break;

            case R.id.DateClear:

                new AlertDialog.Builder(getContext())

                        // TODO スピナー項目変更対応が必要

                        .setTitle(getString(R.string.reset_dialog_tittle))
                        .setMessage(getString(R.string.reset_dialog_message))
                        .setPositiveButton(getString(R.string.reset_dialog_all), (dialog, which) -> {

                            eDateStart.getEditableText().clear();
                            eDateEnd.getEditableText().clear();
                            Spinner[] spinner = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber};
                            for (Spinner s : spinner) {
                                s.setSelection(0);
                            }
                        })
                        .setNegativeButton(getString(R.string.reset_dialog_date), (dialog, which) -> {

                            eDateStart.getEditableText().clear();
                            eDateEnd.getEditableText().clear();

                        })
                        .show();
                break;
        }
    }

    public void setSpinnerData() {

        Store_Names = new ArrayList<>();
        Store_Names.add(getString(R.string.not_selection));

        Machine_Names = new ArrayList<>();
        Machine_Names.add(getString(R.string.not_selection));

        Table_Number = new ArrayList<>();
        Table_Number.add(getString(R.string.not_selection));

        DAY_DIGIT = new ArrayList<>();
        DAY_DIGIT.add(getString(R.string.not_selection));

        SPECIAL_DAY = new ArrayList<>();
        SPECIAL_DAY.add(getString(R.string.not_selection));

        MONTH = new ArrayList<>();
        MONTH.add(getString(R.string.not_selection));

        DAY = new ArrayList<>();
        DAY.add(getString(R.string.not_selection));

        DayOfWeek_In_Month = new ArrayList<>();
        DayOfWeek_In_Month.add(getString(R.string.not_selection));

        WEEK_ID = new ArrayList<>();
        WEEK_ID.add(getString(R.string.not_selection));

        TailNumber = new ArrayList<>();
        TailNumber.add(getString(R.string.not_selection));


        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String storeListSql = "SELECT DISTINCT STORE_NAME FROM TEST ORDER BY STORE_NAME;";
        String machineListSql = "SELECT DISTINCT MACHINE_NAME FROM TEST ORDER BY MACHINE_NAME;";
        String tableNumberListSql = "SELECT DISTINCT TABLE_NUMBER FROM TEST ORDER BY TABLE_NUMBER;";
        String dayDigitSql = "SELECT DISTINCT OPERATION_DAY_DIGIT FROM TEST ORDER BY OPERATION_DAY_DIGIT;";
        String specialDaySql = "SELECT DISTINCT SPECIAL_DAY FROM TEST ORDER BY SPECIAL_DAY;";
        String monthListSql = "SELECT DISTINCT OPERATION_MONTH FROM TEST ORDER BY OPERATION_MONTH;";
        String dayListSql = "SELECT DISTINCT OPERATION_DAY FROM TEST ORDER BY OPERATION_DAY;";
        String dayOfWeekListSql = "SELECT DISTINCT DAY_OF_WEEK_IN_MONTH FROM TEST ORDER BY DAY_OF_WEEK_IN_MONTH;";
        String weekIDListSql = "SELECT DISTINCT WEEK_ID FROM TEST ORDER BY WEEK_ID;";
        String tailNumberListSql = "SELECT DISTINCT TAIL_NUMBER FROM TEST ORDER BY TAIL_NUMBER;";


        Log.i("SQLITE", "storeListSql : " + storeListSql);
        Log.i("SQLITE", "machineListSql : " + machineListSql);
        Log.i("SQLITE", "tableNumberListSql : " + tableNumberListSql);
        Log.i("SQLITE", "dayDigitSql : " + dayDigitSql);
        Log.i("SQLITE", "specialDaySql : " + specialDaySql);
        Log.i("SQLITE", "monthListSql : " + monthListSql);
        Log.i("SQLITE", "dayListSql : " + dayListSql);
        Log.i("SQLITE", "dayOfWeekListSql : " + dayOfWeekListSql);
        Log.i("SQLITE", "weekIDListSql : " + weekIDListSql);
        Log.i("SQLITE", "tailNumberListSql : " + tailNumberListSql);

        try {
            int index = 0;

            Cursor storeNameCursor = db.rawQuery(storeListSql, null);
            while (storeNameCursor.moveToNext()) {
                String store = storeNameCursor.getString(index);
                Store_Names.add(store);
            }

            Cursor machineNameCursor = db.rawQuery(machineListSql, null);
            while (machineNameCursor.moveToNext()) {
                String machine = machineNameCursor.getString(index);
                Machine_Names.add(machine);
            }

            Cursor tableNumberCursor = db.rawQuery(tableNumberListSql, null);
            while (tableNumberCursor.moveToNext()) {
                String table = tableNumberCursor.getString(index);
                // 台番号はnullがあり得るため、nullではなかったら追加
                if (StringUtils.isNotEmpty(table)) {
                    Table_Number.add(table);
                }
            }

            Cursor dayDigitCursor = db.rawQuery(dayDigitSql, null);
            while (dayDigitCursor.moveToNext()) {
                String table = dayDigitCursor.getString(index) + "の付く日";
                DAY_DIGIT.add(table);
            }

            Cursor specialDayCursor = db.rawQuery(specialDaySql, null);
            while (specialDayCursor.moveToNext()) {
                String table = specialDayCursor.getString(index);
                if (StringUtils.isNotEmpty(table)) {
                    switch (table) {
                        case "1":
                            SPECIAL_DAY.add("ゾロ目");
                            break;
                        case "2":
                            SPECIAL_DAY.add("月と日が同じ");
                            break;
                        case "3":
                            SPECIAL_DAY = new ArrayList<>();
                            SPECIAL_DAY.add(getString(R.string.not_selection));
                            SPECIAL_DAY.add("ゾロ目");
                            SPECIAL_DAY.add("月と日が同じ");
                            break;
                    }
                }
            }

            Cursor monthCursor = db.rawQuery(monthListSql, null);
            while (monthCursor.moveToNext()) {
                String table = monthCursor.getString(index) + "月";
                MONTH.add(table);
            }

            Cursor dayCursor = db.rawQuery(dayListSql, null);
            while (dayCursor.moveToNext()) {
                String table = dayCursor.getString(index) + "日";
                DAY.add(table);
            }

            Cursor dayOfWeekCursor = db.rawQuery(dayOfWeekListSql, null);
            while (dayOfWeekCursor.moveToNext()) {
                String table = "第" + dayOfWeekCursor.getString(index);
                DayOfWeek_In_Month.add(table);
            }

            Cursor weekIDCursor = db.rawQuery(weekIDListSql, null);
            while (weekIDCursor.moveToNext()) {
                String table = convertWeekID(weekIDCursor.getString(index));
                WEEK_ID.add(table);
            }

            Cursor tailNumberCursor = db.rawQuery(tailNumberListSql, null);
            while (tailNumberCursor.moveToNext()) {
                String table = tailNumberCursor.getString(index);
                // nullがあり得るため、nullではなかったら末尾に加工して追加
                if (StringUtils.isNotEmpty(table)) {
                    // 末尾切り出し
                    table = "末尾" + tailNumberCursor.getString(index);
                    TailNumber.add(table);
                }
            }

        } finally {
            if (db != null) {
                db.close();
            }
        }

        // 初期値をセット
        init_Store_Names = new ArrayList<>(Store_Names);
        init_Machine_Names = new ArrayList<>(Machine_Names);
        init_Table_Number = new ArrayList<>(Table_Number);
        init_DAY_DIGIT = new ArrayList<>(DAY_DIGIT);
        init_SPECIAL_DAY = new ArrayList<>(SPECIAL_DAY);
        init_MONTH = new ArrayList<>(MONTH);
        init_DAY = new ArrayList<>(DAY);
        init_DayOfWeek_In_Month = new ArrayList<>(DayOfWeek_In_Month);
        init_WEEK_ID = new ArrayList<>(WEEK_ID);
        init_TailNumber = new ArrayList<>(TailNumber);

        // 店舗一覧をセット
        setItems(Store_Names, sStore);

        // 機種名一覧リストをセット
        setItems(Machine_Names, sMachine);

        // 台番号をセット
        setItems(Table_Number, sTableNumber);

        // 特殊スピナー①をセット
        setItems(DAY_DIGIT, sDayDigit);

        // 特殊スピナー②をセット
        setItems(SPECIAL_DAY, sSpecialDay);

        // 特殊スピナー③をセット
        setItems(MONTH, sMonth);

        // 特殊スピナー④をセット
        setItems(DAY, sDay);

        // 特殊スピナー⑤をセット
        setItems(DayOfWeek_In_Month, sDayOfWeek_In_Month);

        // 特殊スピナー⑥をセット
        setItems(WEEK_ID, sWeekId);

        // 特殊スピナー⑦をセット
        setItems(TailNumber, sTailNumber);
    }

    public void initValue() {
        // 初期化処理
        dbTotalGamesValue = 0;
        dbTotalMedalValue = 0;
        dbTotalSingleBigValue = 0;
        dbTotalCherryBigValue = 0;
        dbTotalSingleRegValue = 0;
        dbTotalCherryRegValue = 0;
        dbTotalCherryValue = 0;
        dbTotalGrapeValue = 0;
    }

    // ダブル型で割り算するメソッド
    public double division(int numerator, int denominator) {
        double result = 0;
        if (numerator != 0 && denominator != 0) {
            result = (double) numerator / (double) denominator;
        }
        return result;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // 操作スピナーを捕獲
        Spinner pSpinner = (Spinner) parent;

        // 初回起動時の「未選択」無限ループ回避
        if (!pSpinner.isFocusable()) {
            pSpinner.setFocusable(true);
            return;
        }

        // 以降は画面起動後にスピナーで項目を選択した場合に発生する処理

        // スピナー配列作成
        // TODO ここに追加
        Spinner[] spinners = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber};
        int size = spinners.length;

        // 全てスピナーの選択値を取得した配列作成
        String[] initStrings = new String[size];
        for (int i = 0; i < size; i++) {
            initStrings[i] = spinners[i].getSelectedItem().toString();
        }

        // DBカラム配列
        // TODO ここに追加
        String[] columnName = {"STORE_NAME", "MACHINE_NAME", "TABLE_NUMBER", "OPERATION_DAY_DIGIT", "SPECIAL_DAY",
                "OPERATION_MONTH", "OPERATION_DAY", "DAY_OF_WEEK_IN_MONTH", "WEEK_ID", "TAIL_NUMBER"};

        // 更新項目を格納するリスト作成
        // TODO ここに追加
        List<String> new_Store_Names = new ArrayList<>();
        List<String> new_Machine_Names = new ArrayList<>();
        List<String> new_Table_Number = new ArrayList<>();
        List<String> new_DAY_DIGIT = new ArrayList<>();
        List<String> new_SPECIAL_DAY = new ArrayList<>();
        List<String> new_MONTH = new ArrayList<>();
        List<String> new_DAY = new ArrayList<>();
        List<String> new_DAY_OF_WEEK_IN_MONTH = new ArrayList<>();
        List<String> new_WEEK_ID = new ArrayList<>();
        List<String> new_TailNumber = new ArrayList<>();

        // 更新項目を格納するリストの二次元配列生成
        // TODO ここに追加
        List<List<String>> newItemLists = new ArrayList<>();
        newItemLists.add(new_Store_Names);
        newItemLists.add(new_Machine_Names);
        newItemLists.add(new_Table_Number);
        newItemLists.add(new_DAY_DIGIT);
        newItemLists.add(new_SPECIAL_DAY);
        newItemLists.add(new_MONTH);
        newItemLists.add(new_DAY);
        newItemLists.add(new_DAY_OF_WEEK_IN_MONTH);
        newItemLists.add(new_WEEK_ID);
        newItemLists.add(new_TailNumber);

        // 初期値項目リストの二次元配列生成
        // TODO ここに追加
        List<List<String>> initItemLists = new ArrayList<>();
        initItemLists.add((ArrayList) init_Store_Names);
        initItemLists.add((ArrayList) init_Machine_Names);
        initItemLists.add((ArrayList) init_Table_Number);
        initItemLists.add((ArrayList) init_DAY_DIGIT);
        initItemLists.add((ArrayList) init_SPECIAL_DAY);
        initItemLists.add((ArrayList) init_MONTH);
        initItemLists.add((ArrayList) init_DAY);
        initItemLists.add((ArrayList) init_DayOfWeek_In_Month);
        initItemLists.add((ArrayList) init_WEEK_ID);
        initItemLists.add((ArrayList) init_TailNumber);

        // 更新項目を格納するリストに初期値「未選択」をセット
        for (int i = 0; i < size; i++) {
            newItemLists.get(i).add("未選択");
        }

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // 全てのリスナーを一旦解除
        notItemSelectedListener();

        // SQLの格納
        String[] SQL = new String[size];
        for (int i = 0; i < size; i++) {
            SQL[i] = CreateSQL.selectSpinnerItemSQL(columnName[i]);
        }

        Cursor cursor;
        try {
            int index = 0;
            for (int i = 0; i < size; i++) {
                // SQLが空だった場合
                if (SQL[i].isEmpty()) {

                    // 初期値をセット
                    newItemLists.set(i, new ArrayList<>(initItemLists.get(i)));

                } else {

                    switch (i) {
                        case 0:
                        case 1:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item = cursor.getString(index);
                                newItemLists.get(i).add(item);
                            }
                            break;

                        case 2: //台番号はnull対応
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item = cursor.getString(index);
                                if (StringUtils.isNotEmpty(item)) {
                                    newItemLists.get(i).add(item);
                                }
                            }
                            break;

                        case 3:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item = cursor.getString(index) + "の付く日";
                                newItemLists.get(i).add(item);
                            }
                            break;

                        case 4:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String table = cursor.getString(index);
                                if (StringUtils.isNotEmpty(table)) {
                                    switch (table) {
                                        case "1":
                                            SPECIAL_DAY.add("ゾロ目");
                                            break;
                                        case "2":
                                            SPECIAL_DAY.add("月と日が同じ");
                                            break;
                                        case "3":
                                            SPECIAL_DAY = new ArrayList<>();
                                            SPECIAL_DAY.add(getString(R.string.not_selection));
                                            SPECIAL_DAY.add("ゾロ目");
                                            SPECIAL_DAY.add("月と日が同じ");
                                            break;
                                    }
                                }
                            }
                            break;

                        case 5:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item = cursor.getString(index) + "月";
                                newItemLists.get(i).add(item);
                            }
                            break;

                        case 6:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item = cursor.getString(index) + "日";
                                newItemLists.get(i).add(item);
                            }
                            break;

                        case 7:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item = "第" + cursor.getString(index);
                                newItemLists.get(i).add(item);
                            }
                            break;

                        case 8:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item = convertWeekID(cursor.getString(index));
                                newItemLists.get(i).add(item);
                            }
                            break;

                        case 9:
                            cursor = db.rawQuery(SQL[i], null);
                            while (cursor.moveToNext()) {
                                String item =cursor.getString(index);
                                if (StringUtils.isNotEmpty(item)) {
                                    newItemLists.get(i).add("末尾" + item);
                                }
                            }
                            break;
                    }
                }
            }

        } finally {
            if (db != null) {
                db.close();
            }
        }

        // スピナーの項目を更新
        for (int i = 0; i < size; i++) {
            setItems(newItemLists.get(i), spinners[i]);
        }

        // スピナーの選択値を元の値でセット
        for (int i = 0; i < size; i++) {

            // 各スピナーに格納されている要素数を取得
            int itemPieces = newItemLists.get(i).size();

            for (int j = 0; j < itemPieces; j++) {

                if (newItemLists.get(i).get(j).equals(initStrings[i])) {
                    spinners[i].setSelection(j, false);
                }
            }
        }

        // 全てのリスナーを元に戻す
        setItemSelectedListener();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String convertWeekID(String item) {
        String convertItem;
        switch (item) {
            case "1":
                convertItem = "日曜日";
                break;
            case "2":
                convertItem = "月曜日";
                break;
            case "3":
                convertItem = "火曜日";
                break;
            case "4":
                convertItem = "水曜日";
                break;
            case "5":
                convertItem = "木曜日";
                break;
            case "6":
                convertItem = "金曜日";
                break;
            case "7":
                convertItem = "土曜日";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item);
        }
        return convertItem;
    }

    public void setItems(List<String> spItems, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.main04_statistics02_spinner, spItems);
        adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        spinner.setAdapter(adapter);
    }

    // TODO ここに追加
    public void setItemSelectedListener() {
        sStore.setOnItemSelectedListener(listener);
        sMachine.setOnItemSelectedListener(listener);
        sTableNumber.setOnItemSelectedListener(listener);
        sDayDigit.setOnItemSelectedListener(listener);
        sSpecialDay.setOnItemSelectedListener(listener);
        sMonth.setOnItemSelectedListener(listener);
        sDay.setOnItemSelectedListener(listener);
        sDayOfWeek_In_Month.setOnItemSelectedListener(listener);
        sWeekId.setOnItemSelectedListener(listener);
        sTailNumber.setOnItemSelectedListener(listener);
    }

    // TODO ここに追加
    public void notItemSelectedListener() {
        sStore.setOnItemSelectedListener(null);
        sMachine.setOnItemSelectedListener(null);
        sTableNumber.setOnItemSelectedListener(null);
        sDayDigit.setOnItemSelectedListener(null);
        sSpecialDay.setOnItemSelectedListener(null);
        sMonth.setOnItemSelectedListener(null);
        sDay.setOnItemSelectedListener(null);
        sDayOfWeek_In_Month.setOnItemSelectedListener(null);
        sWeekId.setOnItemSelectedListener(null);
        sTailNumber.setOnItemSelectedListener(null);
    }

    // TODO ここに追加
    public void setFocusable() {
        Spinner[] spinners = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber};
        for (Spinner s : spinners) {
            s.setFocusable(false);
        }
    }

    public void setFindViewById_01(View view) {

        // findViewByIdする対象のレイアウトを指定
        mainLayout = view.findViewById(R.id.StatisticsLayout);

        // 日付表示用TextView
        eDateStart = mainLayout.findViewById(R.id.Date_01);
        eDateEnd = mainLayout.findViewById(R.id.Date_02);

        // スピナー関係
        sStore = mainLayout.findViewById(R.id.StoreSelect);
        sMachine = mainLayout.findViewById(R.id.MachineSelect);
        sTableNumber = mainLayout.findViewById(R.id.MachineNumberSelect);
        sDayDigit = mainLayout.findViewById(R.id.SpecialSpinner_01);
        sSpecialDay = mainLayout.findViewById(R.id.SpecialSpinner_02);
        sMonth = mainLayout.findViewById(R.id.SpecialSpinner_03);
        sDay = mainLayout.findViewById(R.id.SpecialSpinner_04);
        sDayOfWeek_In_Month = mainLayout.findViewById(R.id.SpecialSpinner_05);
        sWeekId = mainLayout.findViewById(R.id.SpecialSpinner_06);
        sTailNumber = mainLayout.findViewById(R.id.SpecialSpinner_07);

        // ボタン
        bDisplay = mainLayout.findViewById(R.id.DisplayButton);
        bClear = mainLayout.findViewById(R.id.DateClear);

        // スクロールビュー
        scrollView = mainLayout.findViewById(R.id.ScrollView02);
    }

    public void setFindViewById_02(View view) {

        // タイトル表示用TextView
        tTittleTotalGames = mainLayout.findViewById(R.id.Tittle_01);
        tTittleMedal = mainLayout.findViewById(R.id.Tittle_02);
        tTittleDiscount = mainLayout.findViewById(R.id.Tittle_03);
        tTittleSingleBig = mainLayout.findViewById(R.id.Tittle_04);
        tTittleCherryBig = mainLayout.findViewById(R.id.Tittle_05);
        tTittleTotalBig = mainLayout.findViewById(R.id.Tittle_06);
        tTittleSingleReg = mainLayout.findViewById(R.id.Tittle_07);
        tTittleCherryReg = mainLayout.findViewById(R.id.Tittle_08);
        tTittleTotalReg = mainLayout.findViewById(R.id.Tittle_09);
        tTittleTotalBonus = mainLayout.findViewById(R.id.Tittle_10);
        tTittleGrape = mainLayout.findViewById(R.id.Tittle_11);
        tTittleCherry = mainLayout.findViewById(R.id.Tittle_12);

        // データ表示用TextView
        tTotalGames = mainLayout.findViewById(R.id.TotalGame);
        tTotalMedal = mainLayout.findViewById(R.id.TotalMedal);
        tDiscount = mainLayout.findViewById(R.id.Discount);
        tTotalSingleBig = mainLayout.findViewById(R.id.TotalAloneBig);
        tTotalSingleBigProbability = mainLayout.findViewById(R.id.TotalAloneBigProbability);
        tTotalCherryBig = mainLayout.findViewById(R.id.TotalCherryBig);
        tTotalCherryBigProbability = mainLayout.findViewById(R.id.TotalCherryBigProbability);
        tTotalBig = mainLayout.findViewById(R.id.TotalBig);
        tTotalBigProbability = mainLayout.findViewById(R.id.TotalBigProbability);
        tTotalSingleReg = mainLayout.findViewById(R.id.TotalAloneReg);
        tTotalSingleRegProbability = mainLayout.findViewById(R.id.TotalAloneRegProbability);
        tTotalCherryReg = mainLayout.findViewById(R.id.TotalCherryReg);
        tTotalCherryRegProbability = mainLayout.findViewById(R.id.TotalCherryRegProbability);
        tTotalReg = mainLayout.findViewById(R.id.TotalReg);
        tTotalRegProbability = mainLayout.findViewById(R.id.TotalRegProbability);
        tTotalBonus = mainLayout.findViewById(R.id.TotalBonus);
        tTotalBonusProbability = mainLayout.findViewById(R.id.TotalBonusProbability);
        tTotalGrape = mainLayout.findViewById(R.id.TotalGrape);
        tTotalGrapeProbability = mainLayout.findViewById(R.id.TotalGrapeProbability);
        tTotalCherry = mainLayout.findViewById(R.id.TotalCherry);
        tTotalCherryProbability = mainLayout.findViewById(R.id.TotalCherryProbability);
    }

    public void setClickListener() {
        eDateStart.setOnClickListener(this);
        eDateEnd.setOnClickListener(this);
        bDisplay.setOnClickListener(this);
        bClear.setOnClickListener(this);
    }

    public void setTittle() {
        TextView[] textViews = {tTittleTotalGames, tTittleMedal, tTittleDiscount, tTittleSingleBig, tTittleCherryBig, tTittleTotalBig,
                tTittleSingleReg, tTittleCherryReg, tTittleTotalReg, tTittleTotalBonus, tTittleGrape, tTittleCherry};
        List<String> tittles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_TITTLES)));
        for (int i = 0, size = tittles.size(); i < size; i++) {
            textViews[i].setText(tittles.get(i));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setScrollEnable(boolean enable) {
        if (enable) {
            scrollView.setOnTouchListener(null);
        } else {
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }


}