package delson.android.j_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ToolDesignListAdapter extends ArrayAdapter<ToolDesignListItems> {

    private int fResource;
    private List<ToolDesignListItems> fItems;
    private LayoutInflater fLayoutInflater;
    private List<Boolean> boolList;

    public ToolDesignListAdapter(Context context, int resource, List<ToolDesignListItems> items){
        super(context,resource,items);
        fResource = resource;
        fItems = items;
        fLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // チェック判定に使用するリスト作成
        boolList = new ArrayList<>();
        int size = items.size();
        for (int i = 0; i < size; i++){
            boolList.add(false);
        }
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

        // フラグによってチェックボックスの表示有無切り替え
        CheckBox checkBox = view.findViewById(R.id.delete_check);
        if (ToolDesignList.check){
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setChecked(false);
            checkBox.setVisibility(View.GONE);
        }
        checkBox.setChecked(boolList.get(position));

        // チェックボックスにリスナーセット
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!boolList.get(position)){
                    boolList.set(position,true);
                    checkBox.setChecked(true);
                } else {
                    boolList.set(position,false);
                    checkBox.setChecked(false);
                }
            }
        });

        return view;
    }

    public void initBoolList(List<ToolDesignListItems> items){
        // チェック判定に使用するリストを初期化
        boolList = new ArrayList<>();
        int size = items.size();
        for (int i = 0; i < size; i++){
            boolList.add(false);
        }
    }

    public List<Boolean> getBoolList(){
        return this.boolList;
    }


}
