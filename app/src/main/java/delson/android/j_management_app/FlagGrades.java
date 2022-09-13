package delson.android.j_management_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlagGrades extends Fragment {

    // IDセットに使用するメインレイアウト
    ConstraintLayout layout;

    // データ数表示およびタッチイベント用
    TextView saveDateCount;

    // アイテムを表示するリストビュー関係
    ListView keepDataList;
    FlagGradesAdapter adapter;
    static ArrayList<FlagGradesListItems> listItems;
    static boolean deleteCheck = false;

    // データ詳細画面で更新または削除した際に表示するトーストの種類を切り替えるためのフラグ
    String judgeToast = null;

    // 表示切り替えフラグ
    boolean selectJudge = true;

    // 共有データ(登録データは存在するが登録店舗が０件だった場合の対応に使用)
    MainApplication mainApplication;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main03_grades01, container, false);

        mainApplication = (MainApplication) this.getActivity().getApplication();

        // レイアウト取得
        layout = view.findViewById(R.id.FlagGradesLayout);

        // リストビュー取得
        keepDataList = layout.findViewById(R.id.KeepDataList);

        // DBからアイテム取得
        listItems = new ArrayList<>();
        Context context = getActivity().getApplicationContext();
        String sql = CreateSQL.FlagGradesSQL();
        DatabaseResultSet.execution("FlagGrades", context, sql);

        // 配列の要素をアダプターを使ってリストビューにセット
        adapter = new FlagGradesAdapter(view.getContext(), R.layout.main03_grades02_item, listItems);
        keepDataList.setAdapter(adapter);

        // 登録データ件数セット
        saveDateCount = view.findViewById(R.id.DataCount);

        // 登録データが存在する場合はカウント表示をタッチするとダイアログが出現する
        setTouchItem();
        if (adapter.getCount() > 0) {
            saveDateCount.setTextColor(Color.GREEN);
            saveDateCount.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        } else {
            saveDateCount.setTextColor(Color.WHITE);
            saveDateCount.setOnTouchListener(null);
        }
        saveDateCount.setText(getString(R.string.save_data_count, adapter.getCount()));

        // リストビューにリスナー登録
        setItemClick();

        // 詳細画面での削除・更新に対応したトースト表示の判定(詳細画面経由しなければ何も表示されない)
        Intent intent = getActivity().getIntent();
        // 渡されてきたデータを取り出す
        judgeToast = intent.getStringExtra("TOAST");
        if (StringUtils.isNotEmpty(judgeToast)) {
            Toast toast;
            if (judgeToast.equals(getString(R.string.delete))) {
                toast = Toast.makeText(getActivity(), "データを削除しました", Toast.LENGTH_LONG);
            } else {
                toast = Toast.makeText(getActivity(), "データを更新しました", Toast.LENGTH_LONG);
            }
            toast.show();
        }

        // staticフラグの初期化
        deleteCheck = false;

        return view;
    }

    public void setItemClick() {
        keepDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!mainApplication.getStore001().equals("null")) {

                    // クリックされたリストビューを取得
                    ListView listView = (ListView) parent;

                    // データ詳細画面にデータを引き継ぐ際に使用するIntentを定義
                    Intent intent = new Intent(getActivity().getApplicationContext(), DataDetail.class);

                    // クリックした位置の各項目(ID・機種名・店舗名・登録日時)を取得
                    FlagGradesListItems items = (FlagGradesListItems) listView.getItemAtPosition(position);

                    // Intentに引き継ぐIDと機種名をセット
                    intent.putExtra("ID", items.getID());
                    intent.putExtra("Date", items.getDate());
                    intent.putExtra("Store", items.getStoreName());
                    intent.putExtra("Machine", items.getMachineName());
                    intent.putExtra("KeepTime", items.getKeepTime());

                    // DataDetail.xmlに遷移させる
                    startActivity(intent);

                } else {
                    pleaseAddStore();
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchItem() {
        saveDateCount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (selectJudge) {
                    // selectListをセット
                    String[] Select_List = {"選択削除", "全削除"};
                    // アラートダイアグラムの表示 ⇒ 各選択項目ごとの処理を行う
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setItems(Select_List, (dialog, idx) -> {

                        switch (idx) {

                            // 選択削除
                            case 0:
                                // フラグ変更
                                selectJudge = false;
                                deleteCheck = true;

                                // チェックボックス表示
                                int a = 0;
                                while (keepDataList.getChildAt(a) != null){
                                    CheckBox checkBox = keepDataList.getChildAt(a).findViewById(R.id.grades_select_box);
                                    checkBox.setVisibility(View.VISIBLE);
                                    a++;
                                }

                                //　TextViewの表示を「キャンセル」に変更
                                saveDateCount.setTextColor(Color.RED);
                                saveDateCount.setText("削除/キャンセル");
                                break;

                                // 全消去
                            case 1:
                                new AlertDialog.Builder(getContext())
                                        .setTitle("登録データ全削除")
                                        .setMessage("データを全て削除します。\n削除後はデータの復元はできません。\n本当に削除しますか？")
                                        .setPositiveButton("削除", (Dialog, which) -> {

                                            // フラグ変更
                                            selectJudge = false;

                                            // 全消去
                                            Context context = getContext();
                                            String sql = "DELETE FROM TEST;";
                                            DatabaseResultSet.execution("ToolDesignList", context, sql);

                                            // リストビュー再更新
                                            listItems = new ArrayList<>();
                                            sql = CreateSQL.FlagGradesSQL();
                                            DatabaseResultSet.execution("FlagGrades", context, sql);
                                            adapter = new FlagGradesAdapter(v.getContext(), R.layout.main03_grades02_item, listItems);
                                            keepDataList.setAdapter(adapter);

                                            // 件数表示初期化
                                            saveDateCount.setTextColor(Color.WHITE);
                                            saveDateCount.setPaintFlags(Paint.CURSOR_AFTER);
                                            saveDateCount.setText(getString(R.string.save_data_count, adapter.getCount()));
                                            saveDateCount.setOnTouchListener(null);

                                            // 統計画面のスピナー更新
                                            setStatisticsSpinnerData();

                                            // トースト表示
                                            Toast toast = Toast.makeText(context, "データを全て削除しました", Toast.LENGTH_LONG);
                                            toast.show();
                                        })
                                        .setNegativeButton(getString(R.string.no), null)
                                        .show();
                                break;
                        }
                    });
                    alert.show();

                } else {

                    new AlertDialog.Builder(getContext())
                            .setTitle("削除/キャンセルの選択")
                            .setMessage("実行する項目を選択してください。\n削除後はデータの復元はできません。\nキャンセル時は初期画面に戻ります。")
                            .setPositiveButton("削除", (dialog, which) -> {

                                // フラグ変更
                                selectJudge = true;
                                deleteCheck = false;

                                // DBから選択されたアイテムを削除
                                int count = adapter.getBoolList().size();
                                Context context = getContext();
                                for (int i = 0; i < count; i++) {
                                    if (adapter.getBoolList().get(i)){
                                        String sql = "DELETE FROM TEST WHERE ID = '" + listItems.get(i).getID() + "';";
                                        DatabaseResultSet.UpdateOrDelete(context, sql);
                                    }
                                }

                                // リストビュー再更新
                                listItems = new ArrayList<>();
                                String sql = CreateSQL.FlagGradesSQL();
                                DatabaseResultSet.execution("FlagGrades", context, sql);
                                adapter = new FlagGradesAdapter(v.getContext(), R.layout.main03_grades02_item, listItems);
                                keepDataList.setAdapter(adapter);

                                // 登録データが存在する場合はカウント表示をタッチするとダイアログが出現する
                                setTouchItem();
                                if (adapter.getCount() > 0) {
                                    saveDateCount.setTextColor(Color.GREEN);
                                    saveDateCount.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    saveDateCount.setTextColor(Color.WHITE);
                                    saveDateCount.setPaintFlags(Paint.CURSOR_AFTER);
                                    saveDateCount.setOnTouchListener(null);
                                }
                                saveDateCount.setText(getString(R.string.save_data_count, adapter.getCount()));

                                // 統計画面のスピナー更新
                                setStatisticsSpinnerData();

                                // トースト表示
                                if (count != adapter.getCount()) {
                                    Toast toast = Toast.makeText(context, "選択項目を削除しました", Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    Toast toast = Toast.makeText(context, "削除する項目がありませんでした", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {

                                // フラグ変更
                                selectJudge = true;
                                deleteCheck = false;

                                // チェックボックス非表示
                                int b = 0;
                                while (keepDataList.getChildAt(b) != null){
                                    CheckBox checkBox = keepDataList.getChildAt(b).findViewById(R.id.grades_select_box);
                                    // チェックが付いていたら外してからViewを消す
                                    if (checkBox.isChecked()) {
                                        checkBox.setChecked(false);
                                    }
                                    checkBox.setVisibility(View.GONE);
                                    b++;
                                }

                                // チェック情報を格納しているリストの初期化
                                adapter.initBoolList(listItems);

                                // TextViewの表示をもとに戻す
                                saveDateCount.setTextColor(Color.GREEN);
                                saveDateCount.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                saveDateCount.setText(getString(R.string.save_data_count, keepDataList.getCount()));
                            })
                            .show();
                }
                return false;
            }
        });
    }

    public void pleaseAddStore() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.add_store_tittle))
                .setMessage("登録データを閲覧するためには登録店舗が１件以上必要です")
                .setPositiveButton(getString(R.string.go_add_store), (dialog, which) -> {
                    Intent intent = new Intent(getActivity().getApplication(), MainManagementStore.class);
                    startActivity(intent);
                })
                .show();
    }



    // 統計画面更新-----------------------------------------------------------------
    public void setStatisticsSpinnerData() {

        // 配列の初期化
        FlagStatistics.Store_Names = new ArrayList<>();
        FlagStatistics.Machine_Names = new ArrayList<>();
        FlagStatistics.Table_Number = new ArrayList<>();
        FlagStatistics.DAY_DIGIT = new ArrayList<>();
        FlagStatistics.SPECIAL_DAY = new ArrayList<>();
        FlagStatistics.MONTH = new ArrayList<>();
        FlagStatistics.DAY = new ArrayList<>();
        FlagStatistics.DayOfWeek_In_Month = new ArrayList<>();
        FlagStatistics.WEEK_ID = new ArrayList<>();
        FlagStatistics.TailNumber = new ArrayList<>();

        // 初期値セット
        FlagStatistics.Store_Names.add(getString(R.string.not_selection));
        FlagStatistics.Machine_Names.add(getString(R.string.not_selection));
        FlagStatistics.Table_Number.add(getString(R.string.not_selection));
        FlagStatistics.DAY_DIGIT.add(getString(R.string.not_selection));
        FlagStatistics.SPECIAL_DAY.add(getString(R.string.not_selection));
        FlagStatistics.MONTH.add(getString(R.string.not_selection));
        FlagStatistics.DAY.add(getString(R.string.not_selection));
        FlagStatistics.DayOfWeek_In_Month.add(getString(R.string.not_selection));
        FlagStatistics.WEEK_ID.add(getString(R.string.not_selection));
        FlagStatistics.TailNumber.add(getString(R.string.not_selection));

        // DBから値を取得
        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            Cursor cursor;
            int index = 0;

            cursor = db.rawQuery(FlagStatistics.storeListSql, null);
            while (cursor.moveToNext()) {
                String store = cursor.getString(index);
                FlagStatistics.Store_Names.add(store);
            }

            cursor = db.rawQuery(FlagStatistics.machineListSql, null);
            while (cursor.moveToNext()) {
                String machine = cursor.getString(index);
                FlagStatistics.Machine_Names.add(machine);
            }

            cursor = db.rawQuery(FlagStatistics.tableNumberListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index);
                // 台番号はnullがあり得るため、nullではなかったら追加
                if (StringUtils.isNotEmpty(table)) {
                    FlagStatistics.Table_Number.add(table);
                }
            }

            cursor = db.rawQuery(FlagStatistics.dayDigitSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index) + "の付く日";
                FlagStatistics.DAY_DIGIT.add(table);
            }

            cursor = db.rawQuery(FlagStatistics.specialDaySql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index);
                if (StringUtils.isNotEmpty(table)) {
                    switch (table) {
                        case "1":
                            FlagStatistics.SPECIAL_DAY.add("ゾロ目");
                            break;
                        case "2":
                            FlagStatistics.SPECIAL_DAY.add("月と日が同じ");
                            break;
                        case "3":
                            FlagStatistics.SPECIAL_DAY = new ArrayList<>();
                            FlagStatistics.SPECIAL_DAY.add(getString(R.string.not_selection));
                            FlagStatistics.SPECIAL_DAY.add("ゾロ目");
                            FlagStatistics.SPECIAL_DAY.add("月と日が同じ");
                            break;
                    }
                }
            }

            cursor = db.rawQuery(FlagStatistics.monthListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index) + "月";
                FlagStatistics.MONTH.add(table);
            }

            cursor = db.rawQuery(FlagStatistics.dayListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index) + "日";
                FlagStatistics.DAY.add(table);
            }

            cursor = db.rawQuery(FlagStatistics.dayOfWeekListSql, null);
            while (cursor.moveToNext()) {
                String table = "第" + cursor.getString(index);
                FlagStatistics.DayOfWeek_In_Month.add(table);
            }

            cursor = db.rawQuery(FlagStatistics.weekIDListSql, null);
            while (cursor.moveToNext()) {
                String table = convertWeekID(cursor.getString(index));
                FlagStatistics.WEEK_ID.add(table);
            }

            cursor = db.rawQuery(FlagStatistics.tailNumberListSql, null);
            while (cursor.moveToNext()) {
                String table = cursor.getString(index);
                // nullがあり得るため、nullではなかったら末尾に加工して追加
                if (StringUtils.isNotEmpty(table)) {
                    // 末尾切り出し
                    table = "末尾" + cursor.getString(index);
                    FlagStatistics.TailNumber.add(table);
                }
            }

        } finally {
            if (db != null) {
                db.close();
            }
        }

        // スピナー更新
        setStatisticsItems(FlagStatistics.Store_Names, FlagStatistics.sStatisticsSpinner.get(0));
        setStatisticsItems(FlagStatistics.Machine_Names, FlagStatistics.sStatisticsSpinner.get(1));
        setStatisticsItems(FlagStatistics.Table_Number, FlagStatistics.sStatisticsSpinner.get(2));
        setStatisticsItems(FlagStatistics.DAY_DIGIT, FlagStatistics.sStatisticsSpinner.get(3));
        setStatisticsItems(FlagStatistics.SPECIAL_DAY, FlagStatistics.sStatisticsSpinner.get(4));
        setStatisticsItems(FlagStatistics.MONTH, FlagStatistics.sStatisticsSpinner.get(5));
        setStatisticsItems(FlagStatistics.DAY, FlagStatistics.sStatisticsSpinner.get(6));
        setStatisticsItems(FlagStatistics.DayOfWeek_In_Month, FlagStatistics.sStatisticsSpinner.get(7));
        setStatisticsItems(FlagStatistics.WEEK_ID, FlagStatistics.sStatisticsSpinner.get(8));
        setStatisticsItems(FlagStatistics.TailNumber, FlagStatistics.sStatisticsSpinner.get(9));
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

    public void setStatisticsItems(List<String> spItems, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.main04_statistics02_spinner, spItems);
        adapter.setDropDownViewResource(R.layout.main04_statistics03_spinner_dropdown);
        spinner.setAdapter(adapter);
    }


}
