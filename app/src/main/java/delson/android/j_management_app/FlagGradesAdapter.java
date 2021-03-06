package delson.android.j_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;
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

        // 機種名をセット
        TextView textMachineName = view.findViewById(R.id.MachineName);
        textMachineName.setText(items.getMachineName());

        // 日付をセット
        TextView textDate = view.findViewById(R.id.Date);
        textDate.setText(items.getDate());

        // 店舗名をセット
        TextView textStoreName = view.findViewById(R.id.StoreName);
        textStoreName.setText(items.getStoreName());

        // 台番号をセット
        TextView textTableNumber  = view.findViewById(R.id.TableNumber);
        if(StringUtils.isNotEmpty(items.getTableNumber())){
            textTableNumber.setText(items.getTableNumber() + "番台");
        }

        return view;
    }
}
