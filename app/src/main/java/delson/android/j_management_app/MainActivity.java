package delson.android.j_management_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 共有データ
    MainApplication mainApplication;

    // R04.09.23 データ移行用
    SharedPreferences counterDate;
    SharedPreferences.Editor editor;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        Context context = getApplicationContext();

        // XMLよみこみ
        Document document = ReadXML.readXML(context);

        // XMLが存在しなければ生成
        if (document == null) {
            // 初期セット
            mainApplication.init();
            // XML生成
            document = CreateXML.execution(mainApplication, context);
        }
        mainApplication.setDocument(document);
        ReadXML.readInfo(mainApplication);

        // R04.09.23 mainApplicationからSharedPreferencesへの移行処理
        counterDate = getSharedPreferences("CounterDate", MODE_PRIVATE);
        editor = counterDate.edit();
        if (counterDate.getBoolean("Migration",true)){
            // 移行フラグが存在しなければ１度だけ以下の処理が走る
            editor.putString("TotalGames", mainApplication.getTotalGames());
            editor.putString("StartGames", mainApplication.getStartGames());
            editor.putString("SingleBig", mainApplication.getSingleBig());
            editor.putString("CherryBig", mainApplication.getCherryBig());
            editor.putString("SingleReg", mainApplication.getSingleReg());
            editor.putString("CherryReg", mainApplication.getCherryReg());
            editor.putString("Cherry", mainApplication.getCherry());
            editor.putString("Grape", mainApplication.getGrape());
            // 移行フラグをfalseにしてデータ保存
            editor.putBoolean("Migration",false);
            editor.apply();
        }
    }

    public void MainManagementStore(View view) {
        Intent intent = new Intent(getApplication(), MainManagementStore.class);
        startActivity(intent);
    }

    public void MainCounter(View view) {
        Intent intent = new Intent(getApplication(), MainCounterActivity.class);
        startActivity(intent);
    }

    public void gradeInquiry(View view) {
        Intent intent = new Intent(getApplication(), MainGradeInquiry.class);
        startActivity(intent);
    }

    public void tools(View view) {
        Intent intent = new Intent(getApplication(), ToolList.class);
        startActivity(intent);
    }

}