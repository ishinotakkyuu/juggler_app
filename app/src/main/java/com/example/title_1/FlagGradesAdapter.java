package com.example.title_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FlagGradesAdapter extends ArrayAdapter<FlagGradesListItems> {

    private int fResource;
    private List<FlagGradesListItems> fItems;
    private LayoutInflater fLayoutInflater;

    public FlagGradesAdapter(Context context,int resource,List<FlagGradesListItems> items){
        super(context,resource,items);
        fResource = resource;
        fItems = items;
        fLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View view;

        if (convertView != null){
            view = convertView;
        } else {
            view = fLayoutInflater.inflate(fResource,null);
        }

        // リストビューに表示する要素を取得
        FlagGradesListItems items = fItems.get(position);

        // IDをセット
        TextView textID = view.findViewById(R.id.ID);
        textID.setText(items.getID());

        // 機種名をセット
        TextView textMachineName = view.findViewById(R.id.MachineName);
        textMachineName.setText(items.getMachineName());

        // 店舗名をセット
        TextView textStoreName = view.findViewById(R.id.StoreName);
        textStoreName.setText(items.getStoreName());

        // 保存時間をセット
        TextView textKeepTime = view.findViewById(R.id.KeepTime);
        textKeepTime.setText(items.getKeepTime());

        return view;
    }
}
