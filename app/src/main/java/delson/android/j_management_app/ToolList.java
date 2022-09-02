package delson.android.j_management_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ToolList extends AppCompatActivity {

    ListView toolList;
    List<String> listItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool00_main_tool_list01);

        //アクションバーの非表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // ツールリストに表示する項目をセット
        listItem = new ArrayList<>();
        listItem.add("全台合算");
        listItem.add("全台合算(概算版)");
        listItem.add("出目保存");

        /*
         * リストに項目を加えた際はsetItemClick()でページ遷移を振り分けているインデックスも同時に変更すること
         */

        listItem.add("アプリの使い方");

        // リストに項目をセット
        ToolListAdapter adapter = new ToolListAdapter(this, R.layout.tool00_main_tool_list_item02, listItem);
        toolList = findViewById(R.id.ToolList);
        toolList.setAdapter(adapter);

        setItemClick();
    }

    public void setItemClick() {

        toolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0: //全台合算
                        Intent intent = new Intent(getApplicationContext(), ToolAllEvent.class);
                        startActivity(intent);
                        break;

                    case 1: //全台合算(概算版)
                        intent = new Intent(getApplicationContext(), ToolAllEventApproximate.class);
                        startActivity(intent);
                        break;

                    case 2: //出目保存
                        intent = new Intent(getApplicationContext(), ToolDesign.class);
                        startActivity(intent);
                        break;

                    case 3: //アプリの使い方
                        intent = new Intent(getApplicationContext(), MainManual.class);
                        startActivity(intent);
                        break;


                }
            }
        });
    }
}