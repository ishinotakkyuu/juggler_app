package delson.android.j_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ToolDesignList extends AppCompatActivity {

    ListView designLIst;

    static ArrayList<ToolDesignListItems> designs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool03_design03_list);

        designLIst = findViewById(R.id.design_list);

        // リストに項目をセット
        designs = new ArrayList<>();
        Context context = getApplicationContext();
        String sql = "select * from DESIGN;";
        DatabaseResultSet.execution("ToolDesignList",context,sql);

        ToolDesignListAdapter adapter = new ToolDesignListAdapter(this,R.layout.tool03_design04_list_item, designs);
        designLIst.setAdapter(adapter);

        // リストビューにリスナー登録
        setItemClick();
    }

    // メニューのセット
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.design_list_menu, menu);
        return true;
    }


    public void setItemClick(){
        designLIst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // クリックされたリストビューを取得
                ListView listView = (ListView) parent;

                // データ詳細画面にデータを引き継ぐ際に使用するIntentを定義
                Intent intent = new Intent(getApplicationContext(),ToolDesignDetail.class);

                // クリックした位置の各項目(ID・機種名・店舗名・登録日時)を取得
                ToolDesignListItems items = (ToolDesignListItems)listView.getItemAtPosition(position);

                // Intentに引き継ぐIDと機種名をセット
                intent.putExtra("ID",items.getID());
                intent.putExtra("NO",items.getNo());

                // ToolDesignDetail.xmlに遷移させる
                startActivity(intent);
            }
        });
    }







    }