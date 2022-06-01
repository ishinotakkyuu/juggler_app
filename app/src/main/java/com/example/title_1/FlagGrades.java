package com.example.title_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FlagGrades extends Fragment {

    ConstraintLayout layout;
    ListView keepDataList;

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
        FlagGradesListItems data1 = new FlagGradesListItems("ID2","Sアイムジャグラー","楽園大森店","2022年12月31日12時00分");
        FlagGradesListItems data2 = new FlagGradesListItems("ID1","Sファンキージャグラー","小手指一番館","2022年11月11日12時00分");
        listItems.add(data1);
        listItems.add(data2);






        FlagGradesAdapter adapter = new FlagGradesAdapter(view.getContext(),R.layout.main03_grades02_item,listItems);
        keepDataList.setAdapter(adapter);

        // リストビューにリスナー登録
        setItemClick();

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

                // Intentに引き継ぐIDをセット
                intent.putExtra("ID",items.getID());

                // DataDetail.xmlに遷移させる
                startActivity(intent);

            }
        });
    }



}
