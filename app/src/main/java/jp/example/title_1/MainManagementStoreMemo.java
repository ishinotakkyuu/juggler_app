package jp.example.title_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainManagementStoreMemo extends AppCompatActivity implements TextWatcher, KeyboardVisibility.OnKeyboardVisibilityListener {

    // メモ入力用
    EditText eMemo;

    // 前画面から受け取った配列インデックス
    int catchTappedPosition;

    // 各種メモTextやXMLを格納する配列
    String[] memo, memoTagNames;

    // フォーカス
    Activity activity;
    ConstraintLayout memoLayout;
    InputMethodManager inputMethodManager;

    // メニュー表示判定用
    boolean menuFlag = true;

    // 共有データ
    MainApplication mainApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main01_storemanagement03_memo);

        // フォーカス関係のセット
        memoLayout = findViewById(R.id.MemoLayout);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 戻るボタン等でキーボードを非表示にされた時のフォーカス対応
        activity = this;
        KeyboardVisibility kv = new KeyboardVisibility(activity);
        kv.setKeyboardVisibilityListener(this);

        // ストレージの取得
        mainApplication = (MainApplication) this.getApplication();
        ReadXML.readInfo(mainApplication);
        memo = mainApplication.getMemos();
        memoTagNames = CreateXML.getMemosTagName();

        // EditTextの設定
        eMemo = findViewById(R.id.StoreMemo);
        eMemo.setFocusable(false);
        eMemo.setFocusableInTouchMode(false);
        eMemo.addTextChangedListener(this);

        // 受け取ったインデックスを使用して該当のXMLよりメモを取得、EditTextにセット
        Intent intent = getIntent();
        catchTappedPosition = Integer.parseInt(intent.getStringExtra("tappedPosition"));
        setValue(catchTappedPosition);
    }

    // 戻るボタン等でキーボードを非表示にされた時のフォーカス対応
    @Override
    public void onVisibilityChanged(boolean visible) {
        if(!visible){
            //キーボードが非表示になったことを検知した時
            menuFlag = true;
            invalidateOptionsMenu();
            eMemo.setFocusable(false);
            eMemo.setFocusableInTouchMode(false);
            memoLayout.requestFocus();
        }
    }

    // メニューのセット
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.memo_bar, menu);
        return true;
    }

    // フラグ判定でメニュー表示を変更
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(menuFlag) {
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
        }
        return true;
    }

    // 各メニュー選択時の処理振り分け
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:

                // フラグを変更、メニュー表示の更新
                menuFlag = false;
                invalidateOptionsMenu();

                // 編集可能にしてフォーカスを文字列の末尾にセットし、キーボードを表示
                eMemo.setFocusable(true);
                eMemo.setFocusableInTouchMode(true);
                String selectionText = eMemo.getText().toString();
                eMemo.requestFocus();
                eMemo.setSelection(selectionText.length());
                inputMethodManager.showSoftInput(eMemo,0);
                break;

            case R.id.reset:

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.memo_delete_alert))
                        .setMessage(getString(R.string.memo_delete_alert_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {

                            eMemo.setText(getString(R.string.default_memo));
                            memoLayout.requestFocus();

                            Toast toast = Toast.makeText(MainManagementStoreMemo.this, getString(R.string.memo_delete_toast), Toast.LENGTH_LONG);
                            toast.show();

                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                break;

            case R.id.finish:

                // フラグを変更、メニュー表示の更新
                menuFlag = true;
                invalidateOptionsMenu();

                // 編集不可にしてフォーカスを外し、キーボードを閉じる
                eMemo.setFocusable(false);
                eMemo.setFocusableInTouchMode(false);
                inputMethodManager.hideSoftInputFromWindow(memoLayout.getWindowToken(), 0);
                memoLayout.requestFocus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setValue(int catchTappedPosition) {
        if (memo[catchTappedPosition].equals("null")) {
            eMemo.setText(getString(R.string.default_memo));
        } else {
            eMemo.setText(memo[catchTappedPosition]);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        // TODO 前回サロゲートペアチェックした文字列はチェックしないようにして処理の高速化をはかること
        // TODO なお、文字列の途中に文字入力されることも想定すること。また１文字消したり追加したりする処理も考慮すること

        final String[] text = {s.toString(),s.toString()};
        if(text[0].isEmpty()) {
            text[0] = "null";
        }

        // サロゲートペア対応
        for (int i = 0, len = text[0].length(); i < len; i++) {
            char c = text[0].charAt(i);
            int deleteIndex = i;
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.not_register_alert))
                        .setMessage(getString(R.string.not_register_alert_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.advance), (dialog, which) -> {

                            text[0] = text[0].substring(0, deleteIndex);
                            text[1] = text[1].substring(deleteIndex + 2);
                            text[0] = text[0] + text[1];

                            eMemo.setText(text[0]);
                            eMemo.setSelection(text[0].length());
                            CreateXML.updateText(mainApplication, memoTagNames[catchTappedPosition], text[0]);

                        })
                        .show();
                return;
            }
        }
        CreateXML.updateText(mainApplication, memoTagNames[catchTappedPosition], text[0]);
    }

}