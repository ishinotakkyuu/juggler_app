package delson.android.j_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ToolDesignListAdapter extends ArrayAdapter<ToolDesignListItems> {

    private int fResource;
    private List<ToolDesignListItems> fItems;
    private LayoutInflater fLayoutInflater;

    public ToolDesignListAdapter(Context context, int resource, List<ToolDesignListItems> items){
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
        ToolDesignListItems items = fItems.get(position);

        // 文字列をセット
        TextView textMachineName = view.findViewById(R.id.ToolDesignItem);
        textMachineName.setText(items.getDesignText());

        return view;
    }

}
