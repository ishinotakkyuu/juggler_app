package com.example.title_1;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FlagStatistics extends Fragment implements View.OnClickListener{

    // 共有データ
    static MainApplication mainApplication = null;

    // レイアウト
    ConstraintLayout mainLayout;

    // 日付表示用のEditText
    EditText eDateStart, eDateEnd;

    // 各種スピナーとそれぞれに対応するチェックボックス
    static Spinner sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sAttachDay;
    CheckBox cDayDigit, cSpecialDay, cMonth, cDay, cDayOfWeek_In_Month, cWeekId, cAttachDay;

    // データを表示するためのボタン
    Button bDisplay;

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

    // DBから値を取得
    static int dbTotalGamesValue,dbTotalMedalValue,dbTotalSingleBigValue,dbTotalCherryBigValue,
               dbTotalSingleRegValue,dbTotalCherryRegValue,dbTotalCherryValue,dbTotalGrapeValue;

    // DB値から算出するもの
    int calTotalBigValue,calTotalRegValue,calTotalBonusValue;
    double calDiscountValue,calTotalSingleBigProbabilityValue,calTotalCherryBigProbabilityValue,calTotalBigProbabilityValue,
            calTotalSingleRegProbabilityValue,calTotalCherryRegProbabilityValue,calTotalRegProbabilityValue,
            calTotalBonusProbabilityValue,calTotalCherryProbabilityValue,calTotalGrapeProbabilityValue;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main04_statistics01,container,false);

        mainApplication = (MainApplication) getActivity().getApplication();

        // 各種findViewByIdの設定
        setFindViewById(view);
        // クリックリスナーの登録
        setClickListener();
        // 各種スピナーに項目をセット
        setSpinnerData();

        return view;
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Date_01:
                final Calendar calender_01 = Calendar.getInstance();
                DatePickerDialog datePickerDialog_01 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // 選択した日付を取得して日付表示用のEditTextにセット
                                eDateStart.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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
                        eDateEnd.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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

                Context context = getActivity().getApplicationContext();
                String sql = CreateSQL.FlagStatisticsSQL();
                DatabaseResultSet.execution("FlagStatistics",context,sql);

                calTotalBigValue = dbTotalSingleBigValue + dbTotalCherryBigValue;
                calTotalRegValue = dbTotalSingleRegValue + dbTotalCherryRegValue;
                calTotalBonusValue = calTotalBigValue + calTotalRegValue;

                if(dbTotalGamesValue > 0) {
                    calDiscountValue = (division(dbTotalGamesValue * 3 + dbTotalMedalValue,dbTotalGamesValue * 3)) *100;
                    calTotalSingleBigProbabilityValue = division(dbTotalGamesValue,dbTotalSingleBigValue);
                    calTotalCherryBigProbabilityValue = division(dbTotalGamesValue,dbTotalSingleBigValue);
                    calTotalBigProbabilityValue = division(dbTotalGamesValue,calTotalBigValue);
                    calTotalSingleRegProbabilityValue = division(dbTotalGamesValue,dbTotalSingleRegValue);
                    calTotalCherryRegProbabilityValue = division(dbTotalGamesValue,dbTotalCherryRegValue);
                    calTotalRegProbabilityValue = division(dbTotalGamesValue,calTotalRegValue);
                    calTotalBonusProbabilityValue = division(dbTotalGamesValue,calTotalBonusValue);
                    calTotalCherryProbabilityValue = division(dbTotalGamesValue,dbTotalCherryValue);
                    calTotalGrapeProbabilityValue = division(dbTotalGamesValue,dbTotalGrapeValue);
                }

                NumberFormat nfNum = NumberFormat.getNumberInstance();
                nfNum.setMaximumFractionDigits(1);

                // ここにDBデータを記述
                tTotalGames.setText(Math.round(dbTotalGamesValue) + "回転");
                tTotalMedal.setText(Math.round(dbTotalMedalValue) + "枚");
                tDiscount.setText(String.format("%.2f", calDiscountValue) + "%");
                tTotalSingleBig.setText(dbTotalSingleBigValue + "回");
                tTotalSingleBigProbability.setText("1/" + String.format("%.2f", calTotalSingleBigProbabilityValue));
                tTotalCherryBig.setText(dbTotalCherryBigValue + "回");
                tTotalCherryBigProbability.setText("1/" + String.format("%.2f", calTotalCherryBigProbabilityValue));
                tTotalBig.setText(calTotalBigValue + "回");
                tTotalBigProbability.setText("1/" + String.format("%.2f", calTotalBigProbabilityValue));
                tTotalSingleReg.setText(dbTotalSingleRegValue + "回");
                tTotalSingleRegProbability.setText("1/" + String.format("%.2f", calTotalSingleRegProbabilityValue));
                tTotalCherryReg.setText(dbTotalCherryRegValue + "回");
                tTotalCherryRegProbability.setText("1/" + String.format("%.2f", calTotalCherryRegProbabilityValue));
                tTotalReg.setText(calTotalRegValue + "回");
                tTotalRegProbability.setText("1/" + String.format("%.2f", calTotalRegProbabilityValue));
                tTotalBonus.setText(calTotalBonusValue + "回");
                tTotalBonusProbability.setText("1/" + String.format("%.2f", calTotalBonusProbabilityValue));
                tTotalGrape.setText(dbTotalGrapeValue + "回");
                tTotalGrapeProbability.setText("1/" + String.format("%.2f", calTotalGrapeProbabilityValue));
                tTotalCherry.setText(dbTotalCherryValue + "回");
                tTotalCherryProbability.setText("1/" + String.format("%.2f", calTotalCherryProbabilityValue));

                break;

        }
    }


    public void setFindViewById(View view){

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

        // 表示ボタン
        bDisplay = mainLayout.findViewById(R.id.DisplayButton);

    }

    public void setClickListener(){
        eDateStart.setOnClickListener(this);
        eDateEnd.setOnClickListener(this);
        bDisplay.setOnClickListener(this);
    }

    public void setTittle(){
        TextView[] textViews = {tTittleTotalGames,tTittleMedal,tTittleDiscount,tTittleSingleBig,tTittleCherryBig,tTittleTotalBig,
                                tTittleSingleReg,tTittleCherryReg,tTittleTotalReg,tTittleTotalBonus,tTittleGrape,tTittleCherry};
        List<String> tittles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_TITTLES)));
        for(int i = 0,size = tittles.size(); i < size; i++){
            textViews[i].setText(tittles.get(i));
        }
    }

    public void setSpinnerData(){

        List<String> Store_Names = new ArrayList<>();
        Store_Names.add(getString(R.string.not_selection));
        List<String> Machine_Names = new ArrayList<>();
        Machine_Names.add(getString(R.string.not_selection));
        List<String> Table_Number = new ArrayList<>();
        Table_Number.add(getString(R.string.not_selection));

        Context context = getActivity().getApplicationContext();
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String storeListSql = "SELECT DISTINCT STORE_NAME FROM TEST;";
        String machineListSql = "SELECT DISTINCT MACHINE_NAME FROM TEST;";
        // String machineNumberListSql =

        Log.i("SQLITE","storeListSql : " + storeListSql);
        Log.i("SQLITE","machineListSql : " + machineListSql);

        try {

            Cursor cursor = db.rawQuery(storeListSql,null);

            while(cursor.moveToNext()){
                int index = cursor.getColumnIndex("STORE_NAME");
                String store = cursor.getString(index);
                Store_Names.add(store);
            }

            Cursor cursor2 = db.rawQuery(machineListSql,null);

            while(cursor2.moveToNext()){
                int index = cursor2.getColumnIndex("MACHINE_NAME");
                String machine = cursor2.getString(index);
                Machine_Names.add(machine);
            }

        }finally{
            if(db != null) {
                db.close();
            }
        }

        // 店舗名および機種名を格納するListを定義し、20店舗分の登録店舗を(nullじゃなかったら)リストにセット ⇒ 登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,Store_Names);
        storeAdapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sStore.setAdapter(storeAdapter);

        // 機種名一覧リストをセット
        ArrayAdapter<String> machineAdapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,Machine_Names);
        machineAdapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sMachine.setAdapter(machineAdapter);

        // 台番号をセット
        ArrayAdapter<String> machineNumberAdapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,Table_Number);
        machineNumberAdapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sTableNumber.setAdapter(machineNumberAdapter);

        // 特殊スピナー①をセット
        final List<String> spItems01 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_DIGIT)));
        ArrayAdapter<String> adapter01 = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,spItems01);
        adapter01.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sDayDigit.setAdapter(adapter01);

        // 特殊スピナー②をセット
        final List<String> spItems02 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_SPECIAL_DAY)));
        ArrayAdapter<String> adapter02 = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,spItems02);
        adapter02.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sSpecialDay.setAdapter(adapter02);

        // 特殊スピナー③をセット
        final List<String> spItems03 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_MONTH)));
        ArrayAdapter<String> adapter03 = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,spItems03);
        adapter03.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sMonth.setAdapter(adapter03);

        // 特殊スピナー④をセット
        final List<String> spItems04 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_DAY)));
        ArrayAdapter<String> adapter04 = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,spItems04);
        adapter04.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sDay.setAdapter(adapter04);

        // 特殊スピナー⑤をセット
        final List<String> spItems05 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_WEEK_IN_MONTH)));
        ArrayAdapter<String> adapter05 = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,spItems05);
        adapter05.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sDayOfWeek_In_Month.setAdapter(adapter05);

        // 特殊スピナー⑥をセット
        final List<String> spItems06 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_WEEK_ID)));
        ArrayAdapter<String> adapter06 = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,spItems06);
        adapter06.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sWeekId.setAdapter(adapter06);

        // 特殊スピナー⑦をセット
        final List<String> spItems07 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.SET_ATTACH_DAY)));
        ArrayAdapter<String> adapter07 = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,spItems07);
        adapter07.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sAttachDay.setAdapter(adapter07);
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
    public double division(int numerator , int denominator){
        double result = 0;
        if(numerator!=0 && denominator!=0) {
            result = (double)numerator / (double)denominator;
        }
        return result;
    }

}