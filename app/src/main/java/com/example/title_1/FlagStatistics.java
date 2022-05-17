package com.example.title_1;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FlagStatistics extends Fragment implements View.OnClickListener{

    // 共有データ
    static MainApplication mainApplication = null;

    // 日付表示用のEditText
    EditText date_01,date_02;

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

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flag_statistics,container,false);

        mainApplication = (MainApplication) getActivity().getApplication();

        // findViewByIdする対象のレイアウトを指定
        ConstraintLayout statisticsLayout = view.findViewById(R.id.StatisticsLayout);

        // 日付
        // 具体的な処理内容はonClickメソッド内に記述
        date_01 = statisticsLayout.findViewById(R.id.Date_01);
        date_01.setOnClickListener(this);
        date_02 = statisticsLayout.findViewById(R.id.Date_02);
        date_02.setOnClickListener(this);

        // スピナー関係
        Spinner storeSpinner = statisticsLayout.findViewById(R.id.StoreSelect);
        Spinner machineSpinner = statisticsLayout.findViewById(R.id.MachineSelect);

        // 店舗名および機種名を格納するListを定義し、20店舗分の登録店舗を(nullじゃなかったら)リストにセット
        List<String> storeNames = new ArrayList<>();
        List<String> machineNames = new ArrayList<>();
        storeNames.add("未選択");
        String[] storeItems = CommonFeature.getStoreItems(mainApplication);
        for(String Item:storeItems){if(!Item.equals("null")){storeNames.add(Item);}}

        // アダプターを介して登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,storeNames);
        storeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        storeSpinner.setAdapter(storeAdapter);

        // 機種名
        machineNames.add("未選択");
        machineNames.add("SアイムジャグラーEX");
        machineNames.add("Sファンキージャグラー2");
        machineNames.add("Sマイジャグラー5");

        // 同様に、機種名一覧リストをセット
        ArrayAdapter<String> machineAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,machineNames);
        machineAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        machineSpinner.setAdapter(machineAdapter);

        // 表示ボタン
        // 具体的な処理内容はonClickメソッド内に記述
        display = statisticsLayout.findViewById(R.id.DisplayButton);
        display.setOnClickListener(this);

        // タイトル表示用TextViewのfindViewById
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

        // データ表示用TextViewのfindViewById
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

        return view;
    }


    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
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

                // ここにDBデータを記述
                totalGame.setText("1234567890123456回転");
                totalMedal.setText("10000000000000枚");
                discount.setText("100.00%");
                totalAloneBig.setText("12345678回");
                totalAloneBigProbability.setText("1/250000.00");
                totalCherryBig.setText("12345678回");
                totalCherryBigProbability.setText("1/250000.00");
                totalBig.setText("12345678回");
                totalBigProbability.setText("1/250000.00");
                totalAloneReg.setText("12345678回");
                totalAloneRegProbability.setText("1/250000.00");
                totalCherryReg.setText("12345678回");
                totalCherryRegProbability.setText("1/250000.00");
                totalReg.setText("12345678回");
                totalRegProbability.setText("1/250000.00");
                totalBonus.setText("12345678回");
                totalBonusProbability.setText("1/250000.00");
                totalGrape.setText("12345678回");
                totalGrapeProbability.setText("1/250000.00");
                totalCherry.setText("12345678回");
                totalCherryProbability.setText("1/250000.00");

                break;




        }
    }
}