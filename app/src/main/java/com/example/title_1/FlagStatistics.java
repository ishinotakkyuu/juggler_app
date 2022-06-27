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
import android.widget.CheckBox;
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

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FlagStatistics extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Context context;

    // 共有データ
    static MainApplication mainApplication = null;

    // レイアウト
    ConstraintLayout mainLayout;

    // 日付表示用のEditText
    static EditText eDateStart, eDateEnd;

    // 各種スピナーとそれぞれに対応するチェックボックス
    static Spinner sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sAttachDay;
    CheckBox cDayDigit, cSpecialDay, cMonth, cDay, cDayOfWeek_In_Month, cWeekId, cAttachDay;

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
    List<String> Store_Names,Machine_Names,Table_Number;
    // 配列初期値格納用
    static List<String> init_Store_Names,init_Machine_Names,init_Table_Number;

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

        View view = inflater.inflate(R.layout.main04_statistics01, container, false);

        mainApplication = (MainApplication) getActivity().getApplication();

        context = getActivity().getApplicationContext();

        // 各種findViewByIdの設定
        setFindViewById(view);
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
                        .setTitle(getString(R.string.reset_dialog_tittle))
                        .setMessage(getString(R.string.reset_dialog_message))
                        .setPositiveButton(getString(R.string.reset_dialog_all), (dialog, which) -> {

                            eDateStart.getEditableText().clear();
                            eDateEnd.getEditableText().clear();
                            Spinner[] spinner = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sAttachDay};
                            for (Spinner s : spinner) {
                                s.setSelection(0);
                            }
                            CheckBox[] checkBox = {cDayDigit, cSpecialDay, cMonth, cDay, cDayOfWeek_In_Month, cWeekId, cAttachDay};
                            for (CheckBox c : checkBox) {
                                c.setChecked(false);
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

    public void setFindViewById(View view) {

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
        sAttachDay = mainLayout.findViewById(R.id.SpecialSpinner_07);

        // チェックボックス
        cDayDigit = mainLayout.findViewById(R.id.CheckBox_01);
        cSpecialDay = mainLayout.findViewById(R.id.CheckBox_02);
        cMonth = mainLayout.findViewById(R.id.CheckBox_03);
        cDay = mainLayout.findViewById(R.id.CheckBox_04);
        cDayOfWeek_In_Month = mainLayout.findViewById(R.id.CheckBox_05);
        cWeekId = mainLayout.findViewById(R.id.CheckBox_06);
        cAttachDay = mainLayout.findViewById(R.id.CheckBox_07);

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

        // ボタン
        bDisplay = mainLayout.findViewById(R.id.DisplayButton);
        bClear = mainLayout.findViewById(R.id.DateClear);

        // スクロールビュー
        scrollView = mainLayout.findViewById(R.id.ScrollView02);
    }

    public void setClickListener() {
        eDateStart.setOnClickListener(this);
        eDateEnd.setOnClickListener(this);
        bDisplay.setOnClickListener(this);
        bClear.setOnClickListener(this);
    }

    public void setItemSelectedListener() {
        sStore.setOnItemSelectedListener(this);
        sMachine.setOnItemSelectedListener(this);
        sTableNumber.setOnItemSelectedListener(this);

//        Spinner[] spinner = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sAttachDay};
//        for(Spinner s : spinner){
//            s.setOnItemSelectedListener(this);
//        }
    }

    public void setFocusable(){
        Spinner[] spinners = {sStore, sMachine, sTableNumber};
        for(Spinner s:spinners){
            s.setFocusable(false);
        }
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

    public void setSpinnerData() {

        Store_Names = new ArrayList<>();
        Store_Names.add(getString(R.string.not_selection));

        Machine_Names = new ArrayList<>();
        Machine_Names.add(getString(R.string.not_selection));

        Table_Number = new ArrayList<>();
        Table_Number.add(getString(R.string.not_selection));

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String storeListSql = "SELECT DISTINCT STORE_NAME FROM TEST;";
        String machineListSql = "SELECT DISTINCT MACHINE_NAME FROM TEST;";
        String tableNumberListSql = "SELECT DISTINCT TABLE_NUMBER FROM TEST;";

        Log.i("SQLITE", "storeListSql : " + storeListSql);
        Log.i("SQLITE", "machineListSql : " + machineListSql);
        Log.i("SQLITE", "tableNumberListSql : " + tableNumberListSql);

        try {

            Cursor storeNameCursor = db.rawQuery(storeListSql, null);

            while (storeNameCursor.moveToNext()) {
                int index = storeNameCursor.getColumnIndex("STORE_NAME");
                String store = storeNameCursor.getString(index);
                Store_Names.add(store);
            }

            Cursor machineNameCursor = db.rawQuery(machineListSql, null);

            while (machineNameCursor.moveToNext()) {
                int index = machineNameCursor.getColumnIndex("MACHINE_NAME");
                String machine = machineNameCursor.getString(index);
                Machine_Names.add(machine);
            }

            Cursor tableNumberCursor = db.rawQuery(tableNumberListSql, null);

            while (tableNumberCursor.moveToNext()) {
                int index = tableNumberCursor.getColumnIndex("TABLE_NUMBER");
                String table = tableNumberCursor.getString(index);
                Table_Number.add(table);
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

        // 店舗一覧をセット
        setItems(Store_Names, sStore);

        // 機種名一覧リストをセット
        setItems(Machine_Names, sMachine);

        // 台番号をセット
        setItems(Table_Number, sTableNumber);

        // 特殊スピナー①をセット
        final List<String> spItems01 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_DIGIT)));
        setItems(spItems01, sDayDigit);

        // 特殊スピナー②をセット
        final List<String> spItems02 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_SPECIAL_DAY)));
        setItems(spItems02, sSpecialDay);

        // 特殊スピナー③をセット
        final List<String> spItems03 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_MONTH)));
        setItems(spItems03, sMonth);

        // 特殊スピナー④をセット
        final List<String> spItems04 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_DAY)));
        setItems(spItems04, sDay);

        // 特殊スピナー⑤をセット
        final List<String> spItems05 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_WEEK_IN_MONTH)));
        setItems(spItems05, sDayOfWeek_In_Month);

        // 特殊スピナー⑥をセット
        final List<String> spItems06 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_WEEK_ID)));
        setItems(spItems06, sWeekId);

        // 特殊スピナー⑦をセット
        final List<String> spItems07 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_ATTACH_DAY)));
        setItems(spItems07, sAttachDay);
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

        // 対象のスピナーを捕獲
        Spinner pSpinner = (Spinner) parent;

        // 初回起動時の「未選択」処理回避
        if (!pSpinner.isFocusable()) {
            pSpinner.setFocusable(true);
            return;
        }

        // 以降は画面起動後にスピナーで項目を選択した場合に発生する処理

        String item = pSpinner.getSelectedItem().toString();
        int pId = pSpinner.getId();

        Spinner[] spinners = {sStore, sMachine, sTableNumber};
        spinners = ArrayUtils.removeElements(spinners,pSpinner);

        SpinnerUpgrade su = new SpinnerUpgrade();
        List<List<String>> newItemLists = su.getUpGradeItems(context,item,pId);

        for(int i = 0; i < newItemLists.size(); i++){
            setItems(newItemLists.get(i),spinners[i]);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // 各スピナーに項目をセットするメソッド
    public void setItems(List<String> spItems, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.main04_statistics02_spinner, spItems);
        adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        spinner.setAdapter(adapter);
    }

}