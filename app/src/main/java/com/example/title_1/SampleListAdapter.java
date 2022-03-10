package com.example.title_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SampleListAdapter extends ArrayAdapter<SampleListItem> {

    private int mResource;
    private List<SampleListItem> mItems;
    private LayoutInflater mInflater;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource リソースID
     * @param items リストビューの要素
     */
    public SampleListAdapter(Context context, int resource, List<SampleListItem> items) {
        super(context, resource, items);

        mResource = resource;
        mItems = items;
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

        // リストビューに表示する要素を取得
        SampleListItem item = mItems.get(position);

        // サムネイル画像を設定
        //ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        //thumbnail.setImageBitmap(item.getThumbnail());

        // タイトルを設定
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(item.getTitle());

        //XMLで定義したアニメーションを読み込む
        //Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.item_motion);
        //view.startAnimation(anim);

        return view;
    }
}