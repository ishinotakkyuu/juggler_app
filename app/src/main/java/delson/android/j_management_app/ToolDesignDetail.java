package delson.android.j_management_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ToolDesignDetail extends AppCompatActivity {

    String catchID,catchNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool03_design05_detail);



        // 各ViewのIDセットなどを行う



        // 個別データ画面から渡されてきたデータを取得
        Intent intent = getIntent();
        // 渡されてきたデータを取り出す
        catchID = intent.getStringExtra("ID");
        catchNO = intent.getStringExtra("NO");

        TextView textView = findViewById(R.id.design_No);
        textView.setText("No." + catchNO);




    }
}