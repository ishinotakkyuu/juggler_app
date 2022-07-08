package com.example.title_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FlagGrades extends Fragment {

    ConstraintLayout layout;
    ListView keepDataList;
    String judgeToast = null;
    static ArrayList<FlagGradesListItems> listItems = new ArrayList<>();

    // 共有データ
    MainApplication mainApplication = null;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main03_grades01,container,false);

        mainApplication = (MainApplication)this.getActivity().getApplication();

        // レイアウト取得
        layout = view.findViewById(R.id.FlagGradesLayout);
        // リストビュー取得
        keepDataList = layout.findViewById(R.id.KeepDataList);

        listItems = new ArrayList<>();

        Context context = getActivity().getApplicationContext();
        String sql = CreateSQL.FlagGradesSQL();
        DatabaseResultSet.execution("FlagGrades",context,sql);

        // 配列の要素をアダプターを使ってリストビューにセット
        FlagGradesAdapter adapter = new FlagGradesAdapter(view.getContext(),R.layout.main03_grades02_item,listItems);
        keepDataList.setAdapter(adapter);

        // リストビューにリスナー登録
        setItemClick();

        // 詳細画面での削除・更新に対応したトースト表示の判定(詳細画面経由しなければ何も表示されない)
        Intent intent = getActivity().getIntent();
        // 渡されてきたデータを取り出す
        judgeToast = intent.getStringExtra("TOAST");
        if(StringUtils.isNotEmpty(judgeToast)){
            Toast toast;
            if(judgeToast.equals(getString(R.string.delete))){
                toast = Toast.makeText(getActivity(), "データを削除しました", Toast.LENGTH_LONG);
            } else {
                toast = Toast.makeText(getActivity(), "データを更新しました", Toast.LENGTH_LONG);
            }
            toast.show();
        }

        return view;
    }

    public void setItemClick(){
        keepDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!mainApplication.getStore001().equals("null")){

                    // クリックされたリストビューを取得
                    ListView listView = (ListView) parent;

                    // データ詳細画面にデータを引き継ぐ際に使用するIntentを定義
                    Intent intent = new Intent(getActivity().getApplicationContext(),DataDetail.class);

                    // クリックした位置の各項目(ID・機種名・店舗名・登録日時)を取得
                    FlagGradesListItems items = (FlagGradesListItems)listView.getItemAtPosition(position);

                    // Intentに引き継ぐIDと機種名をセット
                    intent.putExtra("ID",items.getID());
                    intent.putExtra("Date",items.getDate());
                    intent.putExtra("Store",items.getStoreName());
                    intent.putExtra("Machine",items.getMachineName());
                    intent.putExtra("KeepTime",items.getKeepTime());

                    // DataDetail.xmlに遷移させる
                    startActivity(intent);

                } else {
                    pleaseAddStore();
                }
            }
        });
    }

    public void pleaseAddStore(){
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.add_store_tittle))
                .setMessage("登録データを閲覧するためには登録店舗が１件以上必要です。")
                .setPositiveButton(getString(R.string.go_add_store), (dialog, which) -> {
                    Intent intent = new Intent(getActivity().getApplication(), MainManagementStore.class);
                    startActivity(intent);
                })
                .show();
    }

}
