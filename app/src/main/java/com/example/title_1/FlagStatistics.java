package com.example.title_1;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

        // 日付関係---------------------------------------------------------------------
        // 具体的な処理内容はonClickメソッド内に記述
        date_01 = statisticsLayout.findViewById(R.id.Date_01);
        date_01.setOnClickListener(this);
        date_02 = statisticsLayout.findViewById(R.id.Date_02);
        date_02.setOnClickListener(this);
        // ----------------------------------------------------------------------------



        // スピナー関係------------------------------------------------------------------
        Spinner storeSpinner = statisticsLayout.findViewById(R.id.StoreSelect);
        Spinner machineSpinner = statisticsLayout.findViewById(R.id.MachineSelect);

        // 各種データを格納するListを定義
        List<String> storeNames = new ArrayList<>();
        List<String> machineNames = new ArrayList<>();

        // 20店舗分の登録店舗を(nullじゃなかったら)リストにセット
        storeNames.add("未選択");
        String[] storeItems = CommonFeature.getStoreItems(mainApplication);
        for(String Item:storeItems){if(!Item.equals("null")){storeNames.add(Item);}}

        // 機種名
        machineNames.add("未選択");
        machineNames.add("SアイムジャグラーEX");
        machineNames.add("Sファンキージャグラー2");
        machineNames.add("Sマイジャグラー5");

        // アダプターを介して登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,storeNames);
        storeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        storeSpinner.setAdapter(storeAdapter);

        // 同様に、機種名一覧リストをセット
        ArrayAdapter<String> machineAdapter = new ArrayAdapter<>(getActivity(),R.layout.custom_spinner_statistics,machineNames);
        machineAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_statistics);
        machineSpinner.setAdapter(machineAdapter);
        // ----------------------------------------------------------------------------






        // リセット関係---------------------------------------------------------------------
        // 具体的な処理内容はonClickメソッド内に記述

        // -------------------------------------------------------------------------------

        return view;
    }


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
        }
    }
}