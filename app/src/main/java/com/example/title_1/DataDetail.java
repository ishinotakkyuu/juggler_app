package com.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DataDetail extends AppCompatActivity {

    TextView textView;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main06_datadetail01);

        //アクションバーの表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // 個別データ画面から引き継がれてきたIDを受け取る
        Intent intent = getIntent();
        // 受け取ったデータを取り出す
        ID = intent.getStringExtra("ID");



        // 取得したIDを使ってDBから必要な項目を取得するコードをここに記述する



        // 取得したデータを各TextViewにセット(R04.06.01時点では仮として、IDのみをセットする)
        textView = findViewById(R.id.textView);
        textView.setText(ID);

    }

    public void takeOverID(View view){
        // MainCounterActivityを遷移先にセット
        Intent intent = new Intent(this,MainCounterActivity.class);
        // Intentに引き継ぐIDをセット
        intent.putExtra("ID",ID);
        // MainCounterActivityに遷移させる
        startActivity(intent);

    }



}