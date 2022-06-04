package com.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DataDetail extends AppCompatActivity {

    //ゲーム数関係
    TextView start,total,individual;

    //カウンター関係
    TextView aB,cB,BB,aR,cR,RB,ch,gr,addition;

    //確率関係
    TextView aB_Probability,cB_Probability,BB_Probability,aR_Probability,cR_Probability,RB_Probability,
            ch_Probability,gr_Probability,addition_Probability;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main06_datadetail01);

        //アクションバー非表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // 各ViewのIDをセット
        setFindViewByID();

        // 個別データ画面から引き継がれてきたIDを受け取る
        Intent intent = getIntent();
        // 受け取ったデータを取り出す
        String ID = intent.getStringExtra("ID");


        // 取得したIDを使ってDBから必要な項目を取得するコードをここに記述する


        // 最終的にDBから取得したデータを(String型で)セットする
        start.setText("162");
        total.setText("584");
        individual.setText("422");                  //総ゲーム数-開始ゲーム数で算出
        aB.setText("1");
        aB_Probability.setText("1/422.00");         //確率については個人ゲーム数/該当ボーナスの回数で算出。以下、同様
        cB.setText("2");
        cB_Probability.setText("1/211.00");
        BB.setText("3");                            //単独BIG+チェリー重複BIGで算出
        BB_Probability.setText("1/140.66");
        aR.setText("1");
        aR_Probability.setText("1/422.00");
        cR.setText("0");
        cR_Probability.setText("1/0.00");
        RB.setText("1");                            //単独REG+チェリー重複REGで算出
        RB_Probability.setText("1/422.00");
        ch.setText("10");
        ch_Probability.setText("1/42.20");
        gr.setText("69");
        gr_Probability.setText("1/6.11");
        addition.setText("4");
        addition_Probability.setText("1/105.50");   //BIG+REGで算出

    }

    public void setFindViewByID() {
        total = findViewById(R.id.total_game); start = findViewById(R.id.start_game); individual = findViewById(R.id.individual_game);
        aB = findViewById(R.id.aB); cB = findViewById(R.id.cB); BB = findViewById(R.id.BB);
        aR = findViewById(R.id.aR); cR = findViewById(R.id.cR); RB = findViewById(R.id.RB);
        ch = findViewById(R.id.ch); gr = findViewById(R.id.gr); addition = findViewById(R.id.addition);
        aB_Probability = findViewById(R.id.aB_Probability); cB_Probability = findViewById(R.id.cB_Probability); BB_Probability = findViewById(R.id.BB_Probability);
        aR_Probability = findViewById(R.id.aR_Probability); cR_Probability = findViewById(R.id.cR_Probability); RB_Probability = findViewById(R.id.RB_Probability);
        ch_Probability = findViewById(R.id.ch_Probability); gr_Probability = findViewById(R.id.gr_Probability); addition_Probability = findViewById(R.id.addition_Probability);
    }
}