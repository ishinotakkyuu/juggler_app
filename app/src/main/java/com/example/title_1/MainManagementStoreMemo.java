package com.example.title_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

public class MainManagementStoreMemo extends AppCompatActivity implements TextWatcher {


    EditText eMemo;
    int catchTappedPosition;
    String[] memo,memoTagNames;

    // 共有データ
    MainApplication mainApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main01_storemanagement03_memo);

        mainApplication = (MainApplication) this.getApplication();
        ReadXML.readInfo(mainApplication); //ここでこれをしないと更新されたtextがセットされない……のでやってます。
        memo = mainApplication.getMemos();
        memoTagNames = CreateXML.getMemosTagName();

        eMemo = findViewById(R.id.StoreMemo);
        eMemo.addTextChangedListener(this);

        Intent intent = getIntent();
        catchTappedPosition = Integer.parseInt(intent.getStringExtra("tappedPosition"));
        setValue(catchTappedPosition);
    }

    public void setValue(int catchTappedPosition){
        if(memo[catchTappedPosition].equals("null")){
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

        final String[] text = {s.toString()};
        if(text[0].isEmpty()){
            text[0] = "null";
        }

        // サロゲートペア対応
        for(int i = 0, len = text[0].length(); i < len; i++){
            char c = text[0].charAt(i);
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.not_register_alert))
                        .setMessage(getString(R.string.not_register_alert_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.not_register_alert_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 末尾のみサロゲートペア対応できてる状態
                                // 末尾にサロゲートペアが来なかったら対応できないため、後々改善すること

                                eMemo.removeTextChangedListener(MainManagementStoreMemo.this);

                                text[0] = text[0].substring(0, text[0].length() - 2);
                                eMemo.setText(text[0]);
                                eMemo.setSelection(text[0].length());

                                eMemo.addTextChangedListener(MainManagementStoreMemo.this);

                            }
                        })
                        .show();
                return;
            }
        }
            CreateXML.updateText(mainApplication,memoTagNames[catchTappedPosition], text[0]);
    }
}