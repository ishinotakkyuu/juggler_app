package jp.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private SplashHandler spHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main00_main00_splash);
        //アクションバーの非表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 3秒後に設定する
        handler = new Handler();
        spHandler = new SplashHandler();
        handler.postDelayed(spHandler, 1500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = null;
        handler.removeCallbacks(spHandler);
    }

    // 一定時間後にスプラッシュ画面からスタート画面に自動遷移するためのサブクラス
    class SplashHandler implements Runnable {
        @Override
        public void run() {
            //画面遷移処理
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();       //アクティビティを破棄。
        }
    }
}