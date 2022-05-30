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

        View view = inflater.inflate(R.layout.flag_grades,container,false);

        // レイアウト取得
        layout = view.findViewById(R.id.FlagGradesLayout);
        // リストビュー取得および店舗名表示用TextViewを取得
        keepDataList = layout.findViewById(R.id.KeepDataList);
        storeText = layout.findViewById(R.id.StoreName);

        // リストビューに表示する要素の設定
        ArrayList<FlagGradesListItems> listItems = new ArrayList<>();
        FlagGradesListItems data = new FlagGradesListItems("Sアイム土屋ジャグラー","楽園大森店","2022年12月31日 12時00分");
        listItems.add(data);

        FlagGradesAdapter adapter = new FlagGradesAdapter(view.getContext(),R.layout.flag_grades_item,listItems);
        keepDataList.setAdapter(adapter);

        return view;
    }
}
