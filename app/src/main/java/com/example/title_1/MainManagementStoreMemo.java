package com.example.title_1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

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

    public void setValue(int position){
        switch (position){
            case 0:
                if(mainApplication.getMemo001().equals("null") || mainApplication.getMemo001().isEmpty()){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo001());
                }
                break;

            case 1:
                if(mainApplication.getMemo002().equals("null") || mainApplication.getMemo002().isEmpty()){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo002());
                }
                break;

            case 2:
                if(mainApplication.getMemo003().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo003());
                }
                break;

            case 3:
                if(mainApplication.getMemo004().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo004());
                }
                break;

            case 4:
                if(mainApplication.getMemo005().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo005());
                }
                break;

            case 5:
                if(mainApplication.getMemo006().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo006());
                }
                break;

            case 6:
                if(mainApplication.getMemo007().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo007());
                }
                break;

            case 7:
                if(mainApplication.getMemo008().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo008());
                }
                break;

            case 8:
                if(mainApplication.getMemo009().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo009());
                }
                break;

            case 9:
                if(mainApplication.getMemo010().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo010());
                }
                break;

            case 10:
                if(mainApplication.getMemo011().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo011());
                }
                break;

            case 11:
                if(mainApplication.getMemo012().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo012());
                }
                break;

            case 12:
                if(mainApplication.getMemo013().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo013());
                }
                break;

            case 13:
                if(mainApplication.getMemo014().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo014());
                }
                break;

            case 14:
                if(mainApplication.getMemo015().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo015());
                }
                break;

            case 15:
                if(mainApplication.getMemo016().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo016());
                }
                break;

            case 16:
                if(mainApplication.getMemo017().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo017());
                }
                break;

            case 17:
                if(mainApplication.getMemo018().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo018());
                }
                break;

            case 18:
                if(mainApplication.getMemo019().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo019());
                }
                break;

            case 19:
                if(mainApplication.getMemo020().equals("null")){
                    eMemo.setText(getString(R.string.default_memo));
                } else {
                    eMemo.setText(mainApplication.getMemo020());
                }
                break;
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

        String text = s.toString();
        if(text.isEmpty()){
            text = "null";
        }

        for(int i = 0,len = text.length(); i < len; i++){
            char c = text.charAt(i);
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                Toast toast = Toast.makeText(this, getString(R.string.not_string), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        CreateXML.updateText(mainApplication,memoTagNames[catchTappedPosition],text);
    }
}