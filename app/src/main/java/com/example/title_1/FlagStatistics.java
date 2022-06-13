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
    EditText dateStart, dateEnd;

    // 各種スピナーとそれぞれに対応するチェックぼっく
    static Spinner sStore, sMachine, sTableNumber, sDayDigit, sSpecialDay, sMonth, sDay, sDayOfWeek_In_Month, sWeekId, sAttachDay;
    CheckBox cDayDigit, cSpecialDay, cMonth, cDay, cDayOfWeek_In_Month, cWeekId, cAttachDay;

    // データを表示するためのボタン
    Button bDisplay;

    // タイトル表示に使用するTextView
    TextView tittleTotalGames, tittleMedal, tittleDiscount, tittleSingleBig, tittleCherryBig, tittleTotalBig, tittleSingleReg, tittleCherryReg, tittleTotalReg,
            tittleTotalBonus, tittleGrape, tittleCherry;

    // データ表示に使用するTextView
    TextView totalGame,totalMedal,discount,totalAloneBig,totalAloneBigProbability,
             totalCherryBig, totalCherryBigProbability,totalBig,totalBigProbability,totalAloneReg,
             totalAloneRegProbability, totalCherryReg, totalCherryRegProbability,totalReg,totalRegProbability,
             totalBonus,totalBonusProbability,totalGrape,totalGrapeProbability,totalCherry,totalCherryProbability;

    // ゲーム関連
    static int totalGameValue = 0;
    static int totalMedalValue = 0;
    double discountValue = 0;
    //BIG
    static int totalSingleBigValue= 0;
    double totalSingleBigProbabilityValue = 0;
    static int totalCherryBigValue = 0;
    double totalCherryBigProbabilityValue = 0;
    int totalBigValue = 0;
    double totalBigProbabilityValue = 0;

    // REG
    static int totalSingleRegValue = 0;
    double totalSingleRegProbabilityValue = 0;
    static int totalCherryRegValue = 0;
    double totalCherryRegProbabilityValue = 0;
    int totalRegValue = 0;
    double totalRegProbabilityValue = 0;

    // 合算
    int totalBonusValue = 0;
    double totalBonusProbabilityValue = 0;

    // 子役
    static int totalCherryValue = 0;
    double totalCherryProbabilityValue = 0;
    static int totalGrapeValue = 0;
    double totalGrapeProbabilityValue = 0;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main04_statistics01,container,false);

        mainApplication = (MainApplication) getActivity().getApplication();

        // 各種findViewByIdの設定
        setId(view);
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
                                dateStart.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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
                        dateEnd.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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

                totalBigValue = totalSingleBigValue + totalCherryBigValue;
                totalRegValue = totalSingleRegValue + totalCherryRegValue;
                totalBonusValue = totalBigValue + totalRegValue;

                if(totalGameValue > 0) {
                    discountValue = (((double)totalGameValue * 3) + (double)totalMedalValue) / ((double)totalGameValue * 3) * 100;
                    totalSingleBigProbabilityValue = (double)totalGameValue / (double)totalSingleBigValue;
                    totalCherryBigProbabilityValue = (double)totalGameValue / (double)totalSingleBigValue;
                    totalBigProbabilityValue = (double)totalGameValue / (double)totalBigValue;
                    totalSingleRegProbabilityValue = (double)totalGameValue / (double)totalSingleRegValue;
                    totalCherryRegProbabilityValue = (double)totalGameValue / (double)totalCherryRegValue;
                    totalRegProbabilityValue = (double)totalGameValue / (double)totalRegValue;
                    totalBonusProbabilityValue = (double)totalGameValue / (double)totalBonusValue;
                    totalCherryProbabilityValue = (double)totalGameValue / (double)totalCherryValue;
                    totalGrapeProbabilityValue = (double)totalGameValue / (double)totalGrapeValue;
                }
                NumberFormat nfNum = NumberFormat.getNumberInstance();
                nfNum.setMaximumFractionDigits(1);

                // ここにDBデータを記述
                totalGame.setText(Math.round(totalGameValue) + "回転");
                totalMedal.setText(Math.round(totalMedalValue) + "枚");
                discount.setText(String.format("%.2f",discountValue) + "%");
                totalAloneBig.setText(totalSingleBigValue + "回");
                totalAloneBigProbability.setText("1/" + String.format("%.2f",totalSingleBigProbabilityValue));
                totalCherryBig.setText(totalCherryBigValue + "回");
                totalCherryBigProbability.setText("1/" + String.format("%.2f",totalCherryBigProbabilityValue));
                totalBig.setText(totalBigValue + "回");
                totalBigProbability.setText("1/" + String.format("%.2f",totalBigProbabilityValue));
                totalAloneReg.setText(totalSingleRegValue + "回");
                totalAloneRegProbability.setText("1/" + String.format("%.2f",totalSingleRegProbabilityValue));
                totalCherryReg.setText(totalCherryRegValue + "回");
                totalCherryRegProbability.setText("1/" + String.format("%.2f",totalCherryRegProbabilityValue));
                totalReg.setText(totalRegValue + "回");
                totalRegProbability.setText("1/" + String.format("%.2f",totalRegProbabilityValue));
                totalBonus.setText(totalBonusValue + "回");
                totalBonusProbability.setText("1/" + String.format("%.2f",totalBonusProbabilityValue));
                totalGrape.setText(totalGrapeValue + "回");
                totalGrapeProbability.setText("1/" + String.format("%.2f",totalGrapeProbabilityValue));
                totalCherry.setText(totalCherryValue + "回");
                totalCherryProbability.setText("1/" + String.format("%.2f",totalCherryProbabilityValue));

                break;

        }
    }

    public void setId(View view){

        // findViewByIdする対象のレイアウトを指定
        mainLayout = view.findViewById(R.id.StatisticsLayout);

        // 日付表示用TextView
        dateStart = mainLayout.findViewById(R.id.Date_01);
        dateEnd = mainLayout.findViewById(R.id.Date_02);

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
        tittleTotalGames = mainLayout.findViewById(R.id.Tittle_01);
        tittleMedal = mainLayout.findViewById(R.id.Tittle_02);
        tittleDiscount = mainLayout.findViewById(R.id.Tittle_03);
        tittleSingleBig = mainLayout.findViewById(R.id.Tittle_04);
        tittleCherryBig = mainLayout.findViewById(R.id.Tittle_05);
        tittleTotalBig = mainLayout.findViewById(R.id.Tittle_06);
        tittleSingleReg = mainLayout.findViewById(R.id.Tittle_07);
        tittleCherryReg = mainLayout.findViewById(R.id.Tittle_08);
        tittleTotalReg = mainLayout.findViewById(R.id.Tittle_09);
        tittleTotalBonus = mainLayout.findViewById(R.id.Tittle_10);
        tittleGrape = mainLayout.findViewById(R.id.Tittle_11);
        tittleCherry = mainLayout.findViewById(R.id.Tittle_12);

        // データ表示用TextView
        totalGame = mainLayout.findViewById(R.id.TotalGame);
        totalMedal = mainLayout.findViewById(R.id.TotalMedal);
        discount = mainLayout.findViewById(R.id.Discount);
        totalAloneBig = mainLayout.findViewById(R.id.TotalAloneBig);
        totalAloneBigProbability = mainLayout.findViewById(R.id.TotalAloneBigProbability);
        totalCherryBig = mainLayout.findViewById(R.id.TotalCherryBig);
        totalCherryBigProbability = mainLayout.findViewById(R.id.TotalCherryBigProbability);
        totalBig = mainLayout.findViewById(R.id.TotalBig);
        totalBigProbability = mainLayout.findViewById(R.id.TotalBigProbability);
        totalAloneReg = mainLayout.findViewById(R.id.TotalAloneReg);
        totalAloneRegProbability = mainLayout.findViewById(R.id.TotalAloneRegProbability);
        totalCherryReg = mainLayout.findViewById(R.id.TotalCherryReg);
        totalCherryRegProbability = mainLayout.findViewById(R.id.TotalCherryRegProbability);
        totalReg = mainLayout.findViewById(R.id.TotalReg);
        totalRegProbability = mainLayout.findViewById(R.id.TotalRegProbability);
        totalBonus = mainLayout.findViewById(R.id.TotalBonus);
        totalBonusProbability = mainLayout.findViewById(R.id.TotalBonusProbability);
        totalGrape = mainLayout.findViewById(R.id.TotalGrape);
        totalGrapeProbability = mainLayout.findViewById(R.id.TotalGrapeProbability);
        totalCherry = mainLayout.findViewById(R.id.TotalCherry);
        totalCherryProbability = mainLayout.findViewById(R.id.TotalCherryProbability);

        // 表示ボタン
        bDisplay = mainLayout.findViewById(R.id.DisplayButton);

    }

    public void setClickListener(){
        dateStart.setOnClickListener(this);
        dateEnd.setOnClickListener(this);
        bDisplay.setOnClickListener(this);
    }

    public void setTittle(){
        tittleTotalGames.setText("総回転数");
        tittleMedal.setText("差枚数");
        tittleDiscount.setText("機械割");
        tittleSingleBig.setText("単独BIG");
        tittleCherryBig.setText("チェBIG");
        tittleTotalBig.setText("BIG合算");
        tittleSingleReg.setText("単独REG");
        tittleCherryReg.setText("チェREG");
        tittleTotalReg.setText("REG合算");
        tittleTotalBonus.setText("ボーナス合算");
        tittleGrape.setText("ぶどう");
        tittleCherry.setText("非重複チェリー");
    }

    public void setSpinnerData(){

        List<String> storeNames = new ArrayList<>();
        storeNames.add("未選択");
        List<String> machineNames = new ArrayList<>();
        machineNames.add("未選択");

        // R04.06.03追加
        List<String> machineNumber = new ArrayList<>();
        machineNumber.add("未選択");

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
                storeNames.add(store);
            }

            Cursor cursor2 = db.rawQuery(machineListSql,null);

            while(cursor2.moveToNext()){
                int index = cursor2.getColumnIndex("MACHINE_NAME");
                String machine = cursor2.getString(index);
                machineNames.add(machine);
            }


        }finally{
            if(db != null) {
                db.close();
            }

        }

        // 店舗名および機種名を格納するListを定義し、20店舗分の登録店舗を(nullじゃなかったら)リストにセット ⇒ 登録店舗一覧リストをセット
        //　String[] storeItems = CommonFeature.getStoreItems(mainApplication);
        //for(String Item:storeItems){if(!Item.equals("null")){storeNames.add(Item);}}
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,storeNames);
        storeAdapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sStore.setAdapter(storeAdapter);

        // 機種名一覧リストをセット
        ArrayAdapter<String> machineAdapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,machineNames);
        machineAdapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        sMachine.setAdapter(machineAdapter);

        // R04.06.03追加
        // 台番号をセット
        ArrayAdapter<String> machineNumberAdapter = new ArrayAdapter<>(getActivity(),R.layout.main04_statistics02_spinner,machineNumber);
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
        totalGameValue = 0;
        totalMedalValue = 0;
        totalSingleBigValue = 0;
        totalCherryBigValue = 0;
        totalSingleRegValue = 0;
        totalCherryRegValue = 0;
        totalCherryValue = 0;
        totalGrapeValue = 0;
    }

}