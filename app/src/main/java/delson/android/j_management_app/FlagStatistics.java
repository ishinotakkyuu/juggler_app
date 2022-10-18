package delson.android.j_management_app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FlagStatistics extends Fragment implements TextWatcher, View.OnClickListener, AdapterView.OnItemSelectedListener {

    Context context;
    View view;

    // 共有データ
    MainApplication mainApplication;

    // レイアウト
    ConstraintLayout mainLayout;

    // 日付表示用のEditText
    EditText eDateStart, eDateEnd;
    static List<EditText> eStatisticsEditDate;

    // 各種スピナー
    Spinner sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber;
    static List<Spinner> sStatisticsSpinner;

    // データを表示するためのボタン
    Button bDisplay, bClear;

    // タイトル表示に使用するTextView
    TextView tTittleTotalGames, tTittleMedal, tTittleDiscount, tTittleSingleBig, tTittleCherryBig, tTittleTotalBig,
            tTittleSingleReg, tTittleCherryReg, tTittleTotalReg, tTittleTotalBonus, tTittleGrape, tTittleCherry;

    // データ表示に使用するTextView
    TextView tDataCount,
            tTotalGames, tTotalMedal, tDiscount,
            tTotalSingleBig, tTotalSingleBigProbability,
            tTotalCherryBig, tTotalCherryBigProbability,
            tTotalBig, tTotalBigProbability,
            tTotalSingleReg, tTotalSingleRegProbability,
            tTotalCherryReg, tTotalCherryRegProbability,
            tTotalReg, tTotalRegProbability,
            tTotalBonus, tTotalBonusProbability,
            tTotalGrape, tTotalGrapeProbability,
            tTotalCherry, tTotalCherryProbability;

    // 画面下部のスクロール
    ScrollView scrollView;
    boolean alpha = true;

    // DBから値を取得
    static int dbTotalGamesValue, dbTotalMedalValue, dbTotalSingleBigValue, dbTotalCherryBigValue,
            dbTotalSingleRegValue, dbTotalCherryRegValue, dbTotalCherryValue, dbTotalGrapeValue;
    static int dataCount;

    // スピナーにセットする初期項目を格納した配列
    static List<String> Store_Names, Machine_Names, Table_Number, DAY_DIGIT, SPECIAL_DAY, MONTH, DAY, DayOfWeek_In_Month, WEEK_ID, TailNumber;

    // スピナーに設定するリスナー
    AdapterView.OnItemSelectedListener listener = this;

    // 定数
    final String FORMAT = "%.2f";
    final String TIMES = "回";
    final String NUMERATOR = "1/";

    static final String storeListSql = "SELECT DISTINCT STORE_NAME FROM TEST ORDER BY STORE_NAME;";
    static final String machineListSql = "SELECT DISTINCT MACHINE_NAME FROM TEST ORDER BY MACHINE_NAME;";
    static final String tableNumberListSql = "SELECT DISTINCT TABLE_NUMBER FROM TEST ORDER BY TABLE_NUMBER;";
    static final String dayDigitSql = "SELECT DISTINCT OPERATION_DAY_DIGIT FROM TEST ORDER BY OPERATION_DAY_DIGIT;";
    static final String specialDaySql = "SELECT DISTINCT SPECIAL_DAY FROM TEST ORDER BY SPECIAL_DAY;";
    static final String monthListSql = "SELECT DISTINCT OPERATION_MONTH FROM TEST ORDER BY OPERATION_MONTH;";
    static final String dayListSql = "SELECT DISTINCT OPERATION_DAY FROM TEST ORDER BY OPERATION_DAY;";
    static final String dayOfWeekListSql = "SELECT DISTINCT DAY_OF_WEEK_IN_MONTH FROM TEST ORDER BY DAY_OF_WEEK_IN_MONTH;";
    static final String weekIDListSql = "SELECT DISTINCT WEEK_ID FROM TEST ORDER BY WEEK_ID;";
    static final String tailNumberListSql = "SELECT DISTINCT TAIL_NUMBER FROM TEST ORDER BY TAIL_NUMBER;";

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
        // static対応(メモリリーク回避のためVer2.0.0から実装)
        setViewsArrayList();
        // 各種スピナーに項目をセット
        setSpinnerData();
        // 初回起動時の「未選択」処理回避のためFocusableをfalseにしておく
        setFocusable();
        // 日付用EditTextにクリックリスナーの登録
        setClickListener();
        // スピナーにリスナー登録
        setItemSelectedListener();
        // 画面下部のスクロール固定
        setScrollEnable(false);

        return view;
    }

    public void setViewsArrayList() {

        // 日付関係
        EditText[] eDate = {eDateStart, eDateEnd};
        eStatisticsEditDate = new ArrayList<>();
        eStatisticsEditDate.addAll(Arrays.asList(eDate));

        // スピナー関係
        Spinner[] sSpinners = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber};
        sStatisticsSpinner = new ArrayList<>();
        sStatisticsSpinner.addAll(Arrays.asList(sSpinners));

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
                    setFindViewById_02();
                }

                initValue();
                setTittle();
                setScrollEnable(true);

                // 各種データを表示するTextViewの透過度を判定するフラグをtrueにして透過を無くす
                alpha = true;
                setTextAlpha();

                String sql = CreateSQL.FlagStatisticsSQL(getActivity());
                DatabaseResultSet.execution("FlagStatistics", context, sql);

                int calTotalBigValue = dbTotalSingleBigValue + dbTotalCherryBigValue;
                int calTotalRegValue = dbTotalSingleRegValue + dbTotalCherryRegValue;
                int calTotalBonusValue = calTotalBigValue + calTotalRegValue;

                double calDiscountValue = (division(dbTotalGamesValue * 3 + dbTotalMedalValue, dbTotalGamesValue * 3)) * 100;
                double calTotalSingleBigProbabilityValue = division(dbTotalGamesValue, dbTotalSingleBigValue);
                double calTotalCherryBigProbabilityValue = division(dbTotalGamesValue, dbTotalCherryBigValue);
                double calTotalBigProbabilityValue = division(dbTotalGamesValue, calTotalBigValue);
                double calTotalSingleRegProbabilityValue = division(dbTotalGamesValue, dbTotalSingleRegValue);
                double calTotalCherryRegProbabilityValue = division(dbTotalGamesValue, dbTotalCherryRegValue);
                double calTotalRegProbabilityValue = division(dbTotalGamesValue, calTotalRegValue);
                double calTotalBonusProbabilityValue = division(dbTotalGamesValue, calTotalBonusValue);
                double calTotalCherryProbabilityValue = division(dbTotalGamesValue, dbTotalCherryValue);
                double calTotalGrapeProbabilityValue = division(dbTotalGamesValue, dbTotalGrapeValue);

                NumberFormat nfNum = NumberFormat.getNumberInstance();
                nfNum.setMaximumFractionDigits(1);

                // 値を各Viewにセット
                tDataCount.setText(getString(R.string.tittle_data_count, dataCount));
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

                if (tTittleTotalGames == null) {
                    setFindViewById_02();
                }

                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.reset_dialog_tittle))
                        .setMessage(getString(R.string.reset_dialog_message))
                        .setPositiveButton(getString(R.string.reset_dialog_all), (dialog, which) -> {

                            // スピナーからリスナーを一旦解除
                            notItemSelectedListener();

                            // EditText消去
                            eDateStart.getEditableText().clear();
                            eDateEnd.getEditableText().clear();

                            // スピナーの項目を初期値に戻して「未選択」を選択
                            List<List<String>> initItemLists = new ArrayList<>();
                            initItemLists.add(Store_Names);
                            initItemLists.add(Machine_Names);
                            initItemLists.add(Table_Number);
                            initItemLists.add(DAY_DIGIT);
                            initItemLists.add(SPECIAL_DAY);
                            initItemLists.add(MONTH);
                            initItemLists.add(DAY);
                            initItemLists.add(DayOfWeek_In_Month);
                            initItemLists.add(WEEK_ID);
                            initItemLists.add(TailNumber);

                            Spinner[] spinners = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber};
                            for (int i = 0, len = spinners.length; i < len; i++) {
                                setItems(initItemLists.get(i), spinners[i]);
                                spinners[i].setSelection(0, false);
                            }

                            // スピナーにリスナーを再セット
                            setItemSelectedListener();
                            // テキストを非表示にしてからScrollをトップに戻して固定
                            resetScroll();
                            // アプリ内レビュー
                            startReviewInfo();

                        })
                        .setNegativeButton(getString(R.string.reset_dialog_date), (dialog, which) -> {

                            eDateStart.getEditableText().clear();
                            eDateEnd.getEditableText().clear();

                            // テキストを非表示にしてからScrollをトップに戻して固定
                            resetScroll();
                            // アプリ内レビュー
                            startReviewInfo();
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

        try {
            Cursor cursor;
            int index = 0;

            cursor = db.rawQuery(storeListSql, null);
            while (cursor.moveToNext()) {
                String store = cursor.getString(index);
                Store_Names.add(store);
            }

            cursor = db.rawQuery(machineListSql, null);
            while (cursor.moveToNext()) {
                String machine = cursor.getString(index);
                Machine_Names.add(machine);
            }

            cursor = db.rawQuery(tableNumberListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index);
                // 台番号はnullがあり得るため、nullではなかったら追加
                if (StringUtils.isNotEmpty(table)) {
                    Table_Number.add(table);
                }
            }

            cursor = db.rawQuery(dayDigitSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index) + "の付く日";
                DAY_DIGIT.add(table);
            }

            cursor = db.rawQuery(specialDaySql, null);
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

            cursor = db.rawQuery(monthListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index) + "月";
                MONTH.add(table);
            }

            cursor = db.rawQuery(dayListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index) + "日";
                DAY.add(table);
            }

            cursor = db.rawQuery(dayOfWeekListSql, null);
            while (cursor.moveToNext()) {
                String table = "第" + cursor.getString(index);
                DayOfWeek_In_Month.add(table);
            }

            cursor = db.rawQuery(weekIDListSql, null);
            while (cursor.moveToNext()) {
                String table = convertWeekID(cursor.getString(index));
                WEEK_ID.add(table);
            }

            cursor = db.rawQuery(tailNumberListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index);
                // nullがあり得るため、nullではなかったら末尾に加工して追加
                if (StringUtils.isNotEmpty(table)) {
                    // 末尾切り出し
                    table = "末尾" + cursor.getString(index);
                    TailNumber.add(table);
                }
            }

        } finally {
            if (db != null) {
                db.close();
            }
        }

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
        dataCount = 0;
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

        // 全てのリスナーを一旦解除
        notItemSelectedListener();

        // 後述するループ処理回数を取得
        Spinner[] spinners = {sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sTailNumber};
        int size = spinners.length;

        // 全スピナーの選択値を取得した配列作成
        String[] initStrings = new String[size];
        for (int i = 0; i < size; i++) {
            initStrings[i] = spinners[i].getSelectedItem().toString();
        }

        // DBカラム配列
        String[] columnName = {"STORE_NAME", "MACHINE_NAME", "TABLE_NUMBER", "OPERATION_DAY_DIGIT", "SPECIAL_DAY",
                "OPERATION_MONTH", "OPERATION_DAY", "DAY_OF_WEEK_IN_MONTH", "WEEK_ID", "TAIL_NUMBER"};

        // 更新項目を格納するリスト作成
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
        List<List<String>> initItemLists = new ArrayList<>();
        initItemLists.add(Store_Names);
        initItemLists.add(Machine_Names);
        initItemLists.add(Table_Number);
        initItemLists.add(DAY_DIGIT);
        initItemLists.add(SPECIAL_DAY);
        initItemLists.add(MONTH);
        initItemLists.add(DAY);
        initItemLists.add(DayOfWeek_In_Month);
        initItemLists.add(WEEK_ID);
        initItemLists.add(TailNumber);

        // 更新項目を格納するリストに初期値「未選択」をセット
        for (int i = 0; i < size; i++) {
            newItemLists.get(i).add("未選択");
        }

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // SQLの格納
        String[] SQL = new String[size];
        for (int i = 0; i < size; i++) {
            SQL[i] = CreateSQL.selectSpinnerItemSQL(columnName[i], getActivity());
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
                                String item = cursor.getString(index);
                                if (StringUtils.isNotEmpty(item)) {
                                    switch (item) {
                                        case "1":
                                            newItemLists.get(i).add("ゾロ目");
                                            break;
                                        case "2":
                                            newItemLists.get(i).add("月と日が同じ");
                                            break;
                                        case "3":

                                            // 既存のSPECIAL_DAYリストを削除
                                            newItemLists.remove(i);

                                            // 次に新規のSPECIAL_DAYを作成・追加
                                            List<String> renew_SPECIAL_DAY = new ArrayList<>();
                                            newItemLists.add(i, renew_SPECIAL_DAY);

                                            // 新たに項目を追加
                                            newItemLists.get(i).add(getString(R.string.not_selection));
                                            newItemLists.get(i).add("ゾロ目");
                                            newItemLists.get(i).add("月と日が同じ");
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
                                String item = cursor.getString(index);
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
        // 当初選択値がなかった場合は前処理のスピナー項目更新処理による強制「未選択」選択状態が生かされる
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
        eDateStart.addTextChangedListener(this);
        eDateEnd = mainLayout.findViewById(R.id.Date_02);
        eDateEnd.addTextChangedListener(this);

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

    public void setFindViewById_02() {

        // 対象データ件数表示用TextView
        tDataCount = mainLayout.findViewById(R.id.Tittle_DataCount);

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

    public void resetScroll() {
        // テキストを非表示にしてからScrollをトップに戻して固定
        alpha = false;
        setTextAlpha();
        scrollView.fullScroll(View.FOCUS_UP);
        setScrollEnable(false);
    }

    public void setTextAlpha() {
        TextView[] textViews = {tDataCount, tTittleTotalGames, tTittleMedal, tTittleDiscount, tTittleSingleBig, tTittleCherryBig,
                tTittleTotalBig, tTittleSingleReg, tTittleCherryReg, tTittleTotalReg, tTittleTotalBonus, tTittleGrape, tTittleCherry,
                tTotalGames, tTotalMedal, tDiscount, tTotalSingleBig, tTotalSingleBigProbability, tTotalCherryBig, tTotalCherryBigProbability,
                tTotalBig, tTotalBigProbability, tTotalSingleReg, tTotalSingleRegProbability, tTotalCherryReg, tTotalCherryRegProbability,
                tTotalReg, tTotalRegProbability, tTotalBonus, tTotalBonusProbability, tTotalGrape, tTotalGrapeProbability,
                tTotalCherry, tTotalCherryProbability};

        if (alpha) {
            for (TextView textView : textViews) {
                textView.setAlpha(1);
            }
        } else {
            for (TextView textView : textViews) {
                textView.setAlpha(0);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        // 動的スピナー発動前の店舗スピナーのItemを取得
        String selectItem = sStore.getSelectedItem().toString();

        // わざと初期項目に更新して動的スピナー発動(スピナーに格納している項目を更新するとリスナーが呼び出される仕様)
        // この時点で項目更新による強制インデックス0選択がされる(つまり「未選択」が強制選択される)
        setItems(Store_Names, sStore);

        // 初期項目内から先ほどまで選択していたItemのインデックスを取得
        int itemIndex = Store_Names.indexOf(selectItem);

        // 強制的に「未選択」にされてしまった選択値を当初選択されていたItemに戻す
        sStore.setSelection(itemIndex);
    }

    private void startReviewInfo() {
        ReviewManager reviewManager = ReviewManagerFactory.create(getContext());
        Task<ReviewInfo> manager = reviewManager.requestReviewFlow();
        manager.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                if (reviewInfo != null) {
                    Task<Void> flow = reviewManager.launchReviewFlow(getActivity(), reviewInfo);
                    flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                        }
                    });
                }
            }
        });
    }

}