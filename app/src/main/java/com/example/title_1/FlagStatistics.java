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
        // タイトルをセット
        setTittle();
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

                Context context = getActivity().getApplicationContext();
                String sql = CreateSQL.FlagStatisticsSQL();
                DatabaseResultSet.aaa("FlagStatistics",context,sql);

                calTotalBigValue = dbTotalSingleBigValue + dbTotalCherryBigValue;
                calTotalRegValue = dbTotalSingleRegValue + dbTotalCherryRegValue;
                calTotalBonusValue = calTotalBigValue + calTotalRegValue;

                if(dbTotalGamesValue > 0) {
                    calDiscountValue = (((double) dbTotalGamesValue * 3) + (double) dbTotalMedalValue) / ((double) dbTotalGamesValue * 3) * 100;
                    calTotalSingleBigProbabilityValue = (double) dbTotalGamesValue / (double) dbTotalSingleBigValue;
                    calTotalCherryBigProbabilityValue = (double) dbTotalGamesValue / (double) dbTotalSingleBigValue;
                    calTotalBigProbabilityValue = (double) dbTotalGamesValue / (double) calTotalBigValue;
                    calTotalSingleRegProbabilityValue = (double) dbTotalGamesValue / (double) dbTotalSingleRegValue;
                    calTotalCherryRegProbabilityValue = (double) dbTotalGamesValue / (double) dbTotalCherryRegValue;
                    calTotalRegProbabilityValue = (double) dbTotalGamesValue / (double) calTotalRegValue;
                    calTotalBonusProbabilityValue = (double) dbTotalGamesValue / (double) calTotalBonusValue;
                    calTotalCherryProbabilityValue = (double) dbTotalGamesValue / (double) dbTotalCherryValue;
                    calTotalGrapeProbabilityValue = (double) dbTotalGamesValue / (double) dbTotalGrapeValue;
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
        tTittleTotalGames.setText("総回転数");
        tTittleMedal.setText("差枚数");
        tTittleDiscount.setText("機械割");
        tTittleSingleBig.setText("単独BIG");
        tTittleCherryBig.setText("チェBIG");
        tTittleTotalBig.setText("BIG合算");
        tTittleSingleReg.setText("単独REG");
        tTittleCherryReg.setText("チェREG");
        tTittleTotalReg.setText("REG合算");
        tTittleTotalBonus.setText("ボーナス合算");
        tTittleGrape.setText("ぶどう");
        tTittleCherry.setText("非重複チェリー");
    }

    public void setSpinnerData(){

        List<String> Store_Names = new ArrayList<>();
        Store_Names.add("未選択");
        List<String> Machine_Names = new ArrayList<>();
        Machine_Names.add("未選択");
        List<String> Table_Number = new ArrayList<>();
        Table_Number.add("未選択");

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

        // R04.06.03追加
        // 台番号をセット
        ArrayAdapter<String> machineNumberAdapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,Table_Number);
        machineNumberAdapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sTableNumber.setAdapter(machineNumberAdapter);

        // 特殊スピナー①をセット
        final List<String> specialItems01 = new ArrayList<>(Arrays.asList(
                "未選択", "0の付く日", "1の付く日", "2の付く日", "3の付く日", "4の付く日",
                "5の付く日", "6の付く日", "7の付く日", "8の付く日", "9の付く日"));
        ArrayAdapter<String> specialItems01_Adapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,specialItems01);
        specialItems01_Adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sDayDigit.setAdapter(specialItems01_Adapter);

        // 特殊スピナー②をセット
        final List<String> specialItems02 = new ArrayList<>(Arrays.asList("未選択", "ゾロ目", "月と日が同じ"));
        ArrayAdapter<String> specialItems02_Adapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,specialItems02);
        specialItems02_Adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sSpecialDay.setAdapter(specialItems02_Adapter);

        // 特殊スピナー③をセット
        final List<String> specialItems03 = new ArrayList<>(Arrays.asList(
                "未選択", "1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"));
        ArrayAdapter<String> specialItems03_Adapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,specialItems03);
        specialItems03_Adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sMonth.setAdapter(specialItems03_Adapter);

        // 特殊スピナー④をセット
        final List<String> specialItems04 = new ArrayList<>(Arrays.asList(
                "未選択", "1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日",
                "10日", "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日",
                "20日", "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日", "31日"));
        ArrayAdapter<String> specialItems04_Adapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,specialItems04);
        specialItems04_Adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sDay.setAdapter(specialItems04_Adapter);

        // 特殊スピナー⑤をセット
        final List<String> specialItems05 = new ArrayList<>(Arrays.asList(
                "未選択", "第1", "第2", "第3", "第4", "第5"));
        ArrayAdapter<String> specialItems05_Adapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,specialItems05);
        specialItems05_Adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sDayOfWeek_In_Month.setAdapter(specialItems05_Adapter);

        // 特殊スピナー⑥をセット
        final List<String> specialItems06 = new ArrayList<>(Arrays.asList(
                "未選択", "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"));
        ArrayAdapter<String> specialItems06_Adapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,specialItems06);
        specialItems06_Adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sWeekId.setAdapter(specialItems06_Adapter);

        // 特殊スピナー⑦をセット
        final List<String> specialItems07 = new ArrayList<>(Arrays.asList(
                "未選択", "末尾0", "末尾1", "末尾2", "末尾3", "末尾4", "末尾5", "末尾6", "末尾7", "末尾8", "末尾9"));
        ArrayAdapter<String> specialItems07_Adapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,specialItems07);
        specialItems07_Adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sAttachDay.setAdapter(specialItems07_Adapter);
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

}