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

public class FlagStatistics extends Fragment implements View.OnClickListener{

    // 共有データ
    static MainApplication mainApplication = null;

    // レイアウト
    ConstraintLayout statisticsLayout;

    // 日付表示用のEditText
    EditText date_01,date_02;

    // 各種スピナー
    Spinner storeSpinner,machineSpinner,
            specialSpinner_01,specialSpinner_02,specialSpinner_03,specialSpinner_04,specialSpinner_05,specialSpinner_06;

    // 各種チェックボックス
    CheckBox checkBox_01,checkBox_02,checkBox_03,checkBox_04,checkBox_05,checkBox_06;

    // データを表示するためのボタン
    Button display;

    // タイトル表示に使用するTextView
    TextView tittle01,tittle02,tittle03,tittle04,tittle05,tittle06,tittle07,tittle08,tittle09,
             tittle10,tittle11,tittle12;

    // データ表示に使用するTextView
    TextView totalGame,totalMedal,discount,totalAloneBig,totalAloneBigProbability,
             totalCherryBig, totalCherryBigProbability,totalBig,totalBigProbability,totalAloneReg,
             totalAloneRegProbability, totalCherryReg, totalCherryRegProbability,totalReg,totalRegProbability,
             totalBonus,totalBonusProbability,totalGrape,totalGrapeProbability,totalCherry,totalCherryProbability;

    int totalGameValue = 0;
    int totalMedalValue = 0;
    double discountValue = 0;
    //BIG
    int totalSingleBigValue= 0;
    double totalSingleBigProbabilityValue = 0;
    int totalCherryBigValue = 0;
    double totalCherryBigProbabilityValue = 0;
    int totalBigValue = 0;
    double totalBigProbabilityValue = 0;

    // REG
    int totalSingleRegValue = 0;
    double totalSingleRegProbabilityValue = 0;
    int totalCherryRegValue = 0;
    double totalCherryRegProbabilityValue = 0;
    int totalRegValue = 0;
    double totalRegProbabilityValue = 0;

    // 合算
    int totalBonusValue = 0;
    double totalBonusProbabilityValue = 0;

    // 子役
    int totalCherryValue = 0;
    double totalCherryProbabilityValue = 0;
    int totalGrapeValue = 0;
    double totalGrapeProbabilityValue = 0;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flag_statistics,container,false);

        mainApplication = (MainApplication) getActivity().getApplication();

        // 各種findViewByIdの設定とクリックリスナーの登録
        setId(view);
        setClickListener();

        // 店舗名および機種名を格納するListを定義し、20店舗分の登録店舗を(nullじゃなかったら)リストにセット ⇒ 登録店舗一覧リストをセット
        List<String> storeNames = new ArrayList<>();
        storeNames.add("未選択");
        String[] storeItems = CommonFeature.getStoreItems(mainApplication);
        for(String Item:storeItems){if(!Item.equals("null")){storeNames.add(Item);}}
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,storeNames);
        storeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        storeSpinner.setAdapter(storeAdapter);

        // 機種名一覧リストをセット
        List<String> machineNames = new ArrayList<>(Arrays.asList("未選択", "SアイムジャグラーEX", "Sファンキージャグラー2", "Sマイジャグラー5"));
        ArrayAdapter<String> machineAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,machineNames);
        machineAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        machineSpinner.setAdapter(machineAdapter);

        // 特殊スピナー①をセット
        List<String> specialItems01 = new ArrayList<>(Arrays.asList(
                "未選択", "0の付く日", "1の付く日", "2の付く日", "3の付く日", "4の付く日",
                "5の付く日", "6の付く日", "7の付く日", "8の付く日", "9の付く日"));
        ArrayAdapter<String> specialItems01_Adapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,specialItems01);
        specialItems01_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        specialSpinner_01.setAdapter(specialItems01_Adapter);

        // 特殊スピナー②をセット
        List<String> specialItems02 = new ArrayList<>(Arrays.asList("未選択", "ゾロ目", "月と日が同じ"));
        ArrayAdapter<String> specialItems02_Adapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,specialItems02);
        specialItems02_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        specialSpinner_02.setAdapter(specialItems02_Adapter);

        // 特殊スピナー③をセット
        List<String> specialItems03 = new ArrayList<>(Arrays.asList(
                "未選択", "1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"));
        ArrayAdapter<String> specialItems03_Adapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,specialItems03);
        specialItems03_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        specialSpinner_03.setAdapter(specialItems03_Adapter);

        // 特殊スピナー④をセット
        List<String> specialItems04 = new ArrayList<>(Arrays.asList(
                "未選択", "1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日",
                "10日", "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日",
                "20日", "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日", "31日"));
        ArrayAdapter<String> specialItems04_Adapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,specialItems04);
        specialItems04_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        specialSpinner_04.setAdapter(specialItems04_Adapter);

        // 特殊スピナー⑤をセット
        List<String> specialItems05 = new ArrayList<>(Arrays.asList(
                "未選択", "第1", "第2", "第3", "第4", "第5"));
        ArrayAdapter<String> specialItems05_Adapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,specialItems05);
        specialItems05_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        specialSpinner_05.setAdapter(specialItems05_Adapter);

        // 特殊スピナー⑥をセット
        List<String> specialItems06 = new ArrayList<>(Arrays.asList(
                "未選択", "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"));
        ArrayAdapter<String> specialItems06_Adapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,specialItems06);
        specialItems06_Adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        specialSpinner_06.setAdapter(specialItems06_Adapter);

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
                                date_01.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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
                        date_02.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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

                // タイトルをセット
                tittle01.setText("総回転数");
                tittle02.setText("差枚数");
                tittle03.setText("機械割");
                tittle04.setText("単独BIG");
                tittle05.setText("チェBIG");
                tittle06.setText("BIG合算");
                tittle07.setText("単独REG");
                tittle08.setText("チェREG");
                tittle09.setText("REG合算");
                tittle10.setText("ボーナス合算");
                tittle11.setText("ぶどう");
                tittle12.setText("非重複チェリー");


                Context context = getActivity().getApplicationContext();
                DatabaseHelper helper = new DatabaseHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();

                String sql = "select * from " + "TEST" + ";";

                try {

                    Cursor cursor = db.rawQuery("SELECT * FROM test;",null);

                    while(cursor.moveToNext()){

                        int index = cursor.getColumnIndex("ID");
                        String id = String.valueOf(cursor.getLong(index));

                        index = cursor.getColumnIndex("OPERATION_DATE");
                        String operationDate = cursor.getString(index);

                        index = cursor.getColumnIndex("STORE_NAME");
                        String storeName = cursor.getString(index);

                        index = cursor.getColumnIndex("TOTAL_GAME");

                        totalGameValue = totalGameValue + cursor.getInt(index);

                        index = cursor.getColumnIndex("DIFFERENCE_NUMBER");
                        totalMedalValue = totalMedalValue + cursor.getInt(index);

                        index = cursor.getColumnIndex("SINGLE_BIG");
                        totalSingleBigValue = totalSingleBigValue + cursor.getInt(index);

                        index = cursor.getColumnIndex("CHERRY_BIG");
                        totalCherryBigValue = totalCherryBigValue + cursor.getInt(index);

                        index = cursor.getColumnIndex("SINGLE_REG");
                        totalSingleRegValue = totalSingleRegValue + cursor.getInt(index);

                        index = cursor.getColumnIndex("CHERRY_REG");
                        totalCherryRegValue = totalCherryRegValue + cursor.getInt(index);

                        index = cursor.getColumnIndex("CHERRY");
                        totalCherryValue = totalCherryValue + cursor.getInt(index);

                        index = cursor.getColumnIndex("GRAPE");
                        totalGrapeValue = totalGrapeValue + cursor.getInt(index);

                        // ログに出力する(Android Studioの下部にあるログキャットで確認可能)
                        Log.i("SQLITE", "_id : " + id + " " +
                                "OPERATION_DATE : " + operationDate + " " +
                                "STORE_NAME : "+ storeName + " ");
                    }
                }finally{
                    if(db != null) {
                        db.close();
                    }

                }

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
                totalGame.setText(String.valueOf(Math.round(totalGameValue)) + "回転");
                totalMedal.setText(String.valueOf(Math.round(totalMedalValue))+ "枚");
                discount.setText(String.format("%.2f",discountValue) + "%");
                totalAloneBig.setText(String.valueOf(totalSingleBigValue) + "回");
                totalAloneBigProbability.setText("1/" + String.format("%.2f",totalSingleBigProbabilityValue));
                totalCherryBig.setText(String.valueOf(totalCherryBigValue) + "回");
                totalCherryBigProbability.setText("1/" + String.format("%.2f",totalCherryBigProbabilityValue));
                totalBig.setText(String.valueOf(totalBigValue) + "回");
                totalBigProbability.setText("1/" + String.format("%.2f",totalBigProbabilityValue));
                totalAloneReg.setText(String.valueOf(totalSingleRegValue) + "回");
                totalAloneRegProbability.setText("1/" + String.format("%.2f",totalSingleRegProbabilityValue));
                totalCherryReg.setText(String.valueOf(totalCherryRegValue) + "回");
                totalCherryRegProbability.setText("1/" + String.format("%.2f",totalCherryRegProbabilityValue));
                totalReg.setText(String.valueOf(totalRegValue) + "回");
                totalRegProbability.setText("1/" + String.format("%.2f",totalRegProbabilityValue));
                totalBonus.setText(String.valueOf(totalBonusValue) + "回");
                totalBonusProbability.setText("1/" + String.format("%.2f",totalBonusProbabilityValue));
                totalGrape.setText(String.valueOf(totalGrapeValue) + "回");
                totalGrapeProbability.setText("1/" + String.format("%.2f",totalGrapeProbabilityValue));
                totalCherry.setText(String.valueOf(totalCherryValue) + "回");
                totalCherryProbability.setText("1/" + String.format("%.2f",totalCherryProbabilityValue));

                break;

        }
    }

    public void setId(View view){

        // findViewByIdする対象のレイアウトを指定
        statisticsLayout = view.findViewById(R.id.StatisticsLayout);

        // 日付表示用TextView
        date_01 = statisticsLayout.findViewById(R.id.Date_01);
        date_02 = statisticsLayout.findViewById(R.id.Date_02);

        // スピナー関係
        storeSpinner = statisticsLayout.findViewById(R.id.StoreSelect);
        machineSpinner = statisticsLayout.findViewById(R.id.MachineSelect);
        specialSpinner_01 = statisticsLayout.findViewById(R.id.SpecialSpinner_01);
        specialSpinner_02 = statisticsLayout.findViewById(R.id.SpecialSpinner_02);
        specialSpinner_03 = statisticsLayout.findViewById(R.id.SpecialSpinner_03);
        specialSpinner_04 = statisticsLayout.findViewById(R.id.SpecialSpinner_04);
        specialSpinner_05 = statisticsLayout.findViewById(R.id.SpecialSpinner_05);
        specialSpinner_06 = statisticsLayout.findViewById(R.id.SpecialSpinner_06);

        // チェックボックス
        checkBox_01 = statisticsLayout.findViewById(R.id.CheckBox_01);
        checkBox_02 = statisticsLayout.findViewById(R.id.CheckBox_02);
        checkBox_03 = statisticsLayout.findViewById(R.id.CheckBox_03);
        checkBox_04 = statisticsLayout.findViewById(R.id.CheckBox_04);
        checkBox_05 = statisticsLayout.findViewById(R.id.CheckBox_05);
        checkBox_06 = statisticsLayout.findViewById(R.id.CheckBox_06);

        // タイトル表示用TextView
        tittle01 = statisticsLayout.findViewById(R.id.Tittle_01);
        tittle02 = statisticsLayout.findViewById(R.id.Tittle_02);
        tittle03 = statisticsLayout.findViewById(R.id.Tittle_03);
        tittle04 = statisticsLayout.findViewById(R.id.Tittle_04);
        tittle05 = statisticsLayout.findViewById(R.id.Tittle_05);
        tittle06 = statisticsLayout.findViewById(R.id.Tittle_06);
        tittle07 = statisticsLayout.findViewById(R.id.Tittle_07);
        tittle08 = statisticsLayout.findViewById(R.id.Tittle_08);
        tittle09 = statisticsLayout.findViewById(R.id.Tittle_09);
        tittle10 = statisticsLayout.findViewById(R.id.Tittle_10);
        tittle11 = statisticsLayout.findViewById(R.id.Tittle_11);
        tittle12 = statisticsLayout.findViewById(R.id.Tittle_12);

        // データ表示用TextView
        totalGame = statisticsLayout.findViewById(R.id.TotalGame);
        totalMedal = statisticsLayout.findViewById(R.id.TotalMedal);
        discount = statisticsLayout.findViewById(R.id.Discount);
        totalAloneBig = statisticsLayout.findViewById(R.id.TotalAloneBig);
        totalAloneBigProbability = statisticsLayout.findViewById(R.id.TotalAloneBigProbability);
        totalCherryBig = statisticsLayout.findViewById(R.id.TotalCherryBig);
        totalCherryBigProbability = statisticsLayout.findViewById(R.id.TotalCherryBigProbability);
        totalBig = statisticsLayout.findViewById(R.id.TotalBig);
        totalBigProbability = statisticsLayout.findViewById(R.id.TotalBigProbability);
        totalAloneReg = statisticsLayout.findViewById(R.id.TotalAloneReg);
        totalAloneRegProbability = statisticsLayout.findViewById(R.id.TotalAloneRegProbability);
        totalCherryReg = statisticsLayout.findViewById(R.id.TotalCherryReg);
        totalCherryRegProbability = statisticsLayout.findViewById(R.id.TotalCherryRegProbability);
        totalReg = statisticsLayout.findViewById(R.id.TotalReg);
        totalRegProbability = statisticsLayout.findViewById(R.id.TotalRegProbability);
        totalBonus = statisticsLayout.findViewById(R.id.TotalBonus);
        totalBonusProbability = statisticsLayout.findViewById(R.id.TotalBonusProbability);
        totalGrape = statisticsLayout.findViewById(R.id.TotalGrape);
        totalGrapeProbability = statisticsLayout.findViewById(R.id.TotalGrapeProbability);
        totalCherry = statisticsLayout.findViewById(R.id.TotalCherry);
        totalCherryProbability = statisticsLayout.findViewById(R.id.TotalCherryProbability);

        // 表示ボタン
        display = statisticsLayout.findViewById(R.id.DisplayButton);

    }

    public void setClickListener(){
        date_01.setOnClickListener(this);
        date_02.setOnClickListener(this);
        display.setOnClickListener(this);
    }


}