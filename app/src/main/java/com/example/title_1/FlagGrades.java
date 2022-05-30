package com.example.title_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView storeText;

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

        // リストビューに表示する要素の設定(仮)　⇒　ここにDBから持ってきた機種名・店舗名・登録日時を新しい順にセットしていく
        ArrayList<FlagGradesListItems> listItems = new ArrayList<>();
        FlagGradesListItems data1 = new FlagGradesListItems("Sアイム土屋ジャグラー","楽園大森店","2022年12月31日12時00分");
        FlagGradesListItems data2 = new FlagGradesListItems("Sアイム松沢ジャグラー","あああああああああああああああああん","2022年12月31日12時00分");
        listItems.add(data1);
        listItems.add(data2);

        FlagGradesAdapter adapter = new FlagGradesAdapter(view.getContext(),R.layout.main03_grades04_item,listItems);
        keepDataList.setAdapter(adapter);

        return view;
    }
}
