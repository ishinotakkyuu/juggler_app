package delson.android.j_management_app;

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

    // サロゲートチェックを開始する文字位置
    int startPosition = 0;

    // 編集完了後に再度編集を開始した時のカーソル位置(前回編集を終えた場所にカーソルを戻す)
    int cursorPosition;

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
    MainApplication mainApplication;
    Context context;

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
        context = getApplicationContext();
        ReadXML.readInfo(mainApplication);
        memo = mainApplication.getMemos();
        memoTagNames = CreateXML.getMemosTagName();

        // EditTextの設定
        eMemo = findViewById(R.id.StoreMemo);
        eMemo.setFocusable(false);
        eMemo.setFocusableInTouchMode(false);

        // 受け取ったインデックスを使用して該当のXMLよりメモを取得、EditTextにセット
        Intent intent = getIntent();
        catchTappedPosition = Integer.parseInt(intent.getStringExtra("tappedPosition"));
        setValue(catchTappedPosition);

        // TextWatcherを設定
        eMemo.addTextChangedListener(this);

        // 文字数を取得
        cursorPosition = eMemo.getText().length();
    }

    // 戻るボタン等でキーボードを非表示にされた時のフォーカス対応
    @Override
    public void onVisibilityChanged(boolean visible) {
        if(!visible){

            //キーボードが非表示になったことを検知した時
            menuFlag = true;
            invalidateOptionsMenu();

            // カーソルの位置を保持
            cursorPosition = eMemo.getSelectionEnd();

            // 編集不可にしてフォーカスを外し、キーボードを閉じる
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
                eMemo.requestFocus();
                eMemo.setSelection(cursorPosition);
                inputMethodManager.showSoftInput(eMemo,0);
                break;

            case R.id.reset:

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.memo_delete_alert))
                        .setMessage(getString(R.string.memo_delete_alert_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {

                            // 初期化時はサロゲートチェック不要なのでTextWatcherを一旦解除
                            eMemo.removeTextChangedListener(this);

                            eMemo.setText(getString(R.string.default_memo));
                            cursorPosition = eMemo.getText().length();
                            eMemo.setSelection(cursorPosition);
                            memoLayout.requestFocus();

                            // TextWatcher再セット
                            eMemo.addTextChangedListener(this);

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

                // カーソルの位置を保持
                cursorPosition = eMemo.getSelectionEnd();

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

        final String[] text = {s.toString(),s.toString()};

        // カーソル位置を取得
        cursorPosition = eMemo.getSelectionEnd();

        // メモが空白になった際の対応
        if(text[0].isEmpty()) {
            text[0] = "null";
            CreateXML.updateText(mainApplication, memoTagNames[catchTappedPosition], text[0],context);
            return;
        }

        // 文字列末尾に入力後に入力済み文字列を編集した場合の対応
        if(startPosition > cursorPosition){
            startPosition = 0;
        }

        // サロゲートペア対応
        for (int i = startPosition,len = s.length(); i < len; i++) {
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
                            eMemo.setSelection(deleteIndex);
                            cursorPosition = deleteIndex;
                            CreateXML.updateText(mainApplication, memoTagNames[catchTappedPosition], text[0],context);

                        })
                        .show();
                return;
            }
        }

        // 特殊文字がなければそのまま保存
        CreateXML.updateText(mainApplication, memoTagNames[catchTappedPosition], text[0],context);

        // 括弧が入力されたらカーソルを括弧内に移動
        if(cursorPosition >= 2){
            int bracketsPosition = innerBrackets(text[0].substring(cursorPosition - 2,cursorPosition));
            if(bracketsPosition > 0){
                eMemo.setSelection(bracketsPosition);
            }
        }

        // 処理後のカーソル位置を保持(次回サロゲートチェック処理の開始位置になる)
        startPosition = eMemo.getSelectionEnd();
    }

    public int innerBrackets(String str){
        if(str.equals("()") || str.equals("（）") || str.equals("【】") || str.equals("『』") ||
                str.equals("「」") || str.equals("[]") || str.equals("<>") || str.equals("＜＞") ||
                str.equals("《》") || str.equals("≪≫") || str.equals("{}") || str.equals("｛｝") ||
                str.equals("〔〕") || str.equals("｟｠") || str.equals("〖〗") || str.equals("〈〉") ||
                str.equals("［］") || str.equals("〚〛") || str.equals("〘〙") || str.equals("«»") ||
                str.equals("‹›") || str.equals("\"\"") || str.equals("””")){
            return cursorPosition - 1;
        }
        return 0;
    }

}