package com.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import org.w3c.dom.Document;

public class MainActivity extends AppCompatActivity {

    // 共有データ
    MainApplication mainApplication = null;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main00_main01);

        // 共有データ取り込み
        mainApplication = (MainApplication) this.getApplication();

        // お作法
        Context context = getApplicationContext();
        mainApplication.setContext(context);

        //アクションバーの非表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // XMLよみこみ
        Document document = ReadXML.readXML(context);

        // XMLが存在しなければ生成
        if (document == null) {
            // 初期セット
            mainApplication.init();
            // XML生成
            document = CreateXML.execution(mainApplication,context);
        }
        mainApplication.setDocument(document);
        ReadXML.readInfo(mainApplication);
    }

    public void storeManagement(View view){
        Intent intent = new Intent(getApplication(), StoreManagement.class);
        startActivity(intent);
    }

    public void small_role_Counter(View view){
        Intent intent = new Intent(getApplication(), MainCounterActivity.class);
        startActivity(intent);
    }

    public void gradeInquiry(View view){
        Intent intent = new Intent(getApplication(), MainGradeInquiry.class);
        startActivity(intent);
    }



}