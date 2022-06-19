package com.example.title_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MainManagementStoreAdapter extends ArrayAdapter<String> {

    private int mResource;
    private List<String> items;
    private LayoutInflater mInflater;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource リソースID
     * @param items リストビューの要素
     */
    public MainManagementStoreAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);

        mResource = resource;
        this.items = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = mInflater.inflate(mResource, null);
        }

        // タイトルを設定
        TextView title = view.findViewById(R.id.ListItem);
        title.setText(items.get(position));

        return view;
    }
}