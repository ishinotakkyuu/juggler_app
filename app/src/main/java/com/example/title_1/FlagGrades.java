package com.example.title_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FlagGrades extends Fragment {

    ConstraintLayout layout;
    ListView keepDataList;
    String judgeToast = null;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main03_grades01,container,false);

        // レイアウト取得
        layout = view.findViewById(R.id.FlagGradesLayout);
        // リストビュー取得
        keepDataList = layout.findViewById(R.id.KeepDataList);





        // リストビューに表示する要素の設定(仮)　⇒　ここにDBから持ってきたID・機種名・店舗名・登録日時を新しい順にセットしていく
        // IDは降順にセットすること。
        ArrayList<FlagGradesListItems> listItems = new ArrayList<>();
        // 実装時、各項目は配列？で処理(DBに複数データがある場合は配列で処理することになると思われる)
        FlagGradesListItems data1 = new FlagGradesListItems("ID2","SアイムジャグラーEX","楽園大森店","2022/12/31(日付)","2022年12月31日12時00分(登録日時)");
        FlagGradesListItems data2 = new FlagGradesListItems("ID1","Sファンキージャグラー2","小手指一番館","2022/12/30(日付)","2022年12月30日12時00分(登録日時)");
        listItems.add(data1);
        listItems.add(data2);





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
            if(judgeToast.equals("削除")){
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

                // クリックされたリストビューを取得
                ListView listView = (ListView) parent;

                // データ詳細画面にデータを引き継ぐ際に使用するIntentを定義
                Intent intent = new Intent(getActivity().getApplicationContext(),DataDetail.class);

                // クリックした位置の各項目(ID・機種名・店舗名・登録日時)を取得
                FlagGradesListItems items = (FlagGradesListItems)listView.getItemAtPosition(position);

                // Intentに引き継ぐIDと機種名をセット
                intent.putExtra("ID",items.getID());
                intent.putExtra("Machine",items.getMachineName());
                intent.putExtra("KeepTime",items.getKeepTime());

                // DataDetail.xmlに遷移させる
                startActivity(intent);

            }
        });
    }



}
