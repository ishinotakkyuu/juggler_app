package com.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainCounterActivity extends AppCompatActivity {

    //テスト１



    //ゲーム数関係
    static EditText total; static EditText start; static EditText individual;

    // 機種名選択
    Spinner juggler;

    //カウンター関係
    static EditText aB; static EditText cB; static EditText BB;
    static EditText aR; static EditText cR; static EditText RB;
    static EditText ch; static EditText gr; static EditText addition;

    //確率関係
    static TextView aB_Probability; static TextView cB_Probability; static TextView BB_Probability;
    static TextView aR_Probability; static TextView cR_Probability; static TextView RB_Probability;
    static TextView ch_Probability; static TextView gr_Probability; static TextView addition_Probability;

    // フォーカス関係
    ConstraintLayout layout;    // オプション関係の一部でも使用
    InputMethodManager inputMethodManager;

    //オプション関係
    ToggleButton counterEdit;
    ToggleButton PlusMinus;
    int Plus_Minus_Counter = 0;

    // 共有データ
    static MainApplication mainApplication = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mainApplication = (MainApplication) this.getApplication();

        setContentView(R.layout.activity_main_counter);
        //アクションバーの非表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // 各viewをfindViewByIdで紐づけるメソッド
         setID();
        // 機種名選択のスピナー登録
         setJuggler();
        // ゲーム数・カウント回数を表示するEditTextにテクストウォッチャーを設定するメソッド
         setTextWatcher();
        // 画面起動時には各EditTextを操作できないようにするメソッド
         setEditTextFocusFalse();
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になるメソッド
         actionListenerFocusOut();
        // 各EditTextを操作できるようにするためのメソッドをトグルボタンに設定
         toggleCounterEdit();
        // カウンターボタンを押すとプラス/マイナスを切り替えられるメソッド
         togglePlusMinus();
        // 内部ストレージ関係
         setValue();

        //0頭だった場合、次の入力値で上書きされる仕様になったため使用しない
        //単純に0頭の後ろに数値を入力させない仕様にするときはこれを使用する
        //setInputFilter();
    }

    public void setID(){
        layout = findViewById(R.id.counter);
        total = findViewById(R.id.total_game);
        start = findViewById(R.id.start_game);
        juggler = findViewById(R.id.juggler);
        individual = findViewById(R.id.individual_game);
        counterEdit = findViewById(R.id.togglecounteredit);
        PlusMinus = findViewById(R.id.toggleplusminus);
        aB = findViewById(R.id.aB); cB = findViewById(R.id.cB); BB = findViewById(R.id.BB);
        aR = findViewById(R.id.aR); cR = findViewById(R.id.cR); RB = findViewById(R.id.RB);
        ch = findViewById(R.id.ch); gr = findViewById(R.id.gr); addition = findViewById(R.id.addition);
        aB_Probability = findViewById(R.id.aB_Probability);
        cB_Probability = findViewById(R.id.cB_Probability);
        BB_Probability = findViewById(R.id.BB_Probability);
        aR_Probability = findViewById(R.id.aR_Probability);
        cR_Probability = findViewById(R.id.cR_Probability);
        RB_Probability = findViewById(R.id.RB_Probability);
        ch_Probability = findViewById(R.id.ch_Probability);
        gr_Probability = findViewById(R.id.gr_Probability);
        addition_Probability = findViewById(R.id.addition_Probability);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void setTextWatcher(){
        total.addTextChangedListener(new gamesCounterWatcher(total,mainApplication));
        start.addTextChangedListener(new gamesCounterWatcher(start,mainApplication));
        individual.addTextChangedListener(new gamesCounterWatcher(individual,mainApplication));
        aB.addTextChangedListener(new gamesCounterWatcher(aB,mainApplication));
        cB.addTextChangedListener(new gamesCounterWatcher(cB,mainApplication));
        BB.addTextChangedListener(new gamesCounterWatcher(BB,mainApplication));
        aR.addTextChangedListener(new gamesCounterWatcher(aR,mainApplication));
        cR.addTextChangedListener(new gamesCounterWatcher(cR,mainApplication));
        RB.addTextChangedListener(new gamesCounterWatcher(RB,mainApplication));
        ch.addTextChangedListener(new gamesCounterWatcher(ch,mainApplication));
        gr.addTextChangedListener(new gamesCounterWatcher(gr,mainApplication));
    }

    public void setJuggler(){
        List<String> jugglerList = new ArrayList<>();
            jugglerList.add("SアイムジャグラーEX");
            jugglerList.add("Sファンキージャグラー2");
            jugglerList.add("Sマイジャグラー5");
        ArrayAdapter<String> jugglerAdapter = new ArrayAdapter<>(this,R.layout.custom_spinner,jugglerList);
        jugglerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        juggler.setAdapter(jugglerAdapter);
        juggler.setSelection(mainApplication.getMachineName());
    }

    public void setEditTextFocusFalse(){
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.a_4_default, null);
        aB.setFocusable(false); aB.setFocusableInTouchMode(false); aB.setBackground(background);
        cB.setFocusable(false); cB.setFocusableInTouchMode(false); cB.setBackground(background);
        aR.setFocusable(false); aR.setFocusableInTouchMode(false); aR.setBackground(background);
        cR.setFocusable(false); cR.setFocusableInTouchMode(false); cR.setBackground(background);
        ch.setFocusable(false); ch.setFocusableInTouchMode(false); ch.setBackground(background);
        gr.setFocusable(false); gr.setFocusableInTouchMode(false); gr.setBackground(background);
    }

    public void setEditTextFocusTrue(){
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.a_3_smallrole_edit, null);
        aB.setFocusable(true); aB.setFocusableInTouchMode(true); aB.setBackground(background);
        cB.setFocusable(true); cB.setFocusableInTouchMode(true); cB.setBackground(background);
        aR.setFocusable(true); aR.setFocusableInTouchMode(true); aR.setBackground(background);
        cR.setFocusable(true); cR.setFocusableInTouchMode(true); cR.setBackground(background);
        ch.setFocusable(true); ch.setFocusableInTouchMode(true); ch.setBackground(background);
        gr.setFocusable(true); gr.setFocusableInTouchMode(true); gr.setBackground(background);
    }

    public void actionListenerFocusOut(){
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になる
        start.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        total.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        aB.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        cB.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        aR.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        cR.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        ch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        gr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
        juggler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                // 保存処理
                mainApplication.setMachineName(position);
                CreateXML.updateText(mainApplication,"machineName",String.valueOf(position));
                focusOut();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void storeRegister(View view){
        if(!mainApplication.getStore001().equals("null")){
            storeRegister();
        } else {
            noStore();
        }
    }

    public void storeRegister(){
        // 登録店舗名を表示するためのプルダウン(スピナー)を設定
            final Spinner storeSpinner = new Spinner(this);
            List<String> storeNames = new ArrayList<String>();

        //----------------------------------------------------------------------------------------------------------
        // 20店舗分の登録店舗を(nullじゃなかったら)リストにセット
        String storeItems[] = CommonFeature.getStoreItems(mainApplication);
        for(String Item:storeItems){
            if(!Item.equals("null")){
                storeNames.add(Item);
            }
        }
        //----------------------------------------------------------------------------------------------------------

        // アダプターを介して登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,storeNames);
        storeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(storeAdapter);

        new AlertDialog.Builder(this)
                .setTitle("データ登録")
                .setMessage("カウンターデータを登録します")
                .setView(storeSpinner)
                .setPositiveButton("登録", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // ここにデータベースへの保存処理を記述する

                        Toast toast = Toast.makeText(MainCounterActivity.this, "データを登録しました", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

    public void noStore(){
        new AlertDialog.Builder(this)
                .setTitle("店舗登録のお願い")
                .setMessage("データを残したい場合は店舗登録を行ってください")
                .setPositiveButton("店舗登録へ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplication(), TenpoKanriActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("戻る", null)
                .show();
    }

    public void resetButton(View view){
        new AlertDialog.Builder(this)
                .setTitle("カウンター初期化")
                .setMessage("カウンターを全てリセットしますか？")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        start.setText(""); total.setText("");
                        aB.setText("0"); cB.setText("0");
                        aR.setText("0"); cR.setText("0");
                        ch.setText("0"); gr.setText("0");
                        addition_Probability.setText("1/0.00");

                        mainApplication.init();
                        CreateXML.updateText(mainApplication,"total","");
                        CreateXML.updateText(mainApplication,"start","");

                        counterEdit.setChecked(false);
                        PlusMinus.setChecked(false);

                        Toast toast = Toast.makeText(MainCounterActivity.this, "リセットしました", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                .setNegativeButton("いいえ",null)
                .show();
    }

    public void toggleCounterEdit(){
        counterEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setEditTextFocusTrue();
                } else {
                    setEditTextFocusFalse();
                    inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
                }
            }
        });
    }

    public void togglePlusMinus(){
        PlusMinus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Plus_Minus_Counter++;
                    aB.setTextColor(Color.RED); aB.setTypeface(Typeface.DEFAULT_BOLD);
                    cB.setTextColor(Color.RED); cB.setTypeface(Typeface.DEFAULT_BOLD);
                    aR.setTextColor(Color.RED); aR.setTypeface(Typeface.DEFAULT_BOLD);
                    cR.setTextColor(Color.RED); cR.setTypeface(Typeface.DEFAULT_BOLD);
                    ch.setTextColor(Color.RED); ch.setTypeface(Typeface.DEFAULT_BOLD);
                    gr.setTextColor(Color.RED); gr.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    Plus_Minus_Counter--;
                    aB.setTextColor(Color.WHITE); aB.setTypeface(Typeface.DEFAULT);
                    cB.setTextColor(Color.WHITE); cB.setTypeface(Typeface.DEFAULT);
                    aR.setTextColor(Color.WHITE); aR.setTypeface(Typeface.DEFAULT);
                    cR.setTextColor(Color.WHITE); cR.setTypeface(Typeface.DEFAULT);
                    ch.setTextColor(Color.WHITE); ch.setTypeface(Typeface.DEFAULT);
                    gr.setTextColor(Color.WHITE); gr.setTypeface(Typeface.DEFAULT);
                }
            }
        });
    }

    private void setValue() {
        // 共有データから値をセット
        // 総ゲームと開始ゲームは空白が許容されるためnullチェックする
        if(mainApplication.getTotal() != null){
            total.setText(mainApplication.getTotal());
        }
        if(mainApplication.getStart() != null){
            start.setText(mainApplication.getStart());
        }
        aB.setText(mainApplication.getaB());
        cB.setText(mainApplication.getcB());
        aR.setText(mainApplication.getaR());
        cR.setText(mainApplication.getcR());
        ch.setText(mainApplication.getCh());
        gr.setText(mainApplication.getGr());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // onTouchEventではアクテビティにしか反応せず、リストビュー上をタッチしても意味がない
        // そこでリストビューをタッチしてもフォーカスが外れるようにするには、dispatchTouchEventを使う
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        layout.requestFocus();
        return super.dispatchTouchEvent(event);
    }

    public void focusOut(){
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
        layout.requestFocus();
    }

    //-----------------------------------------------------------------------------------------------
    //ここからボタンの制御
    //-----------------------------------------------------------------------------------------------

    public void aloneBigButton(View view) {
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        if (Plus_Minus_Counter == 0) {
            if (!aB.getText().toString().isEmpty()) {
                int inAB = Integer.parseInt(aB.getText().toString());
                if (inAB < 9999) {
                    inAB++;
                    colorButton.aloneBig(v);
                    aB.setText(String.valueOf(inAB));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.aloneBig(v);
                aB.setText(String.valueOf(1));
            }
        } else {
            if (!aB.getText().toString().isEmpty()) {
                int inAB = Integer.parseInt(aB.getText().toString());
                if (inAB > 0) {
                    inAB--;
                    colorButton.aloneBig(v);
                    aB.setText(String.valueOf(inAB));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数下限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        focusOut();
    }

    public void cherryBigButton(View view) {
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        if (Plus_Minus_Counter == 0) {
            if (!cB.getText().toString().isEmpty()) {
                int inCB = Integer.parseInt(cB.getText().toString());
                if (inCB < 9999) {
                    inCB++;
                    colorButton.cherryBig(v);
                    cB.setText(String.valueOf(inCB));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.cherryBig(v);
                cB.setText(String.valueOf(1));
            }
        } else {
            if (!cB.getText().toString().isEmpty()) {
                int inCB = Integer.parseInt(cB.getText().toString());
                if (inCB > 0) {
                    inCB--;
                    colorButton.cherryBig(v);
                    cB.setText(String.valueOf(inCB));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数下限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        focusOut();
    }

    public void aloneRegButton(View view){
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        if (Plus_Minus_Counter == 0) {
            if (!aR.getText().toString().isEmpty()) {
                int inAR = Integer.parseInt(aR.getText().toString());
                if (inAR < 9999) {
                    inAR++;
                    colorButton.aloneReg(v);
                    aR.setText(String.valueOf(inAR));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.aloneReg(v);
                aR.setText(String.valueOf(1));
            }
        } else {
            if (!aR.getText().toString().isEmpty()) {
                int inAR = Integer.parseInt(aR.getText().toString());
                if (inAR > 0) {
                    inAR--;
                    colorButton.aloneReg(v);
                    aR.setText(String.valueOf(inAR));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数下限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        focusOut();
    }

    public void cherryRegButton(View view){
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        if (Plus_Minus_Counter == 0) {
            if (!cR.getText().toString().isEmpty()) {
                int inCR = Integer.parseInt(cR.getText().toString());
                if (inCR < 9999) {
                    inCR++;
                    colorButton.cherryReg(v);
                    cR.setText(String.valueOf(inCR));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.cherryReg(v);
                cR.setText(String.valueOf(1));
            }
        } else {
            if (!cR.getText().toString().isEmpty()) {
                int inCR = Integer.parseInt(cR.getText().toString());
                if (inCR > 0) {
                    inCR--;
                    colorButton.cherryReg(v);
                    cR.setText(String.valueOf(inCR));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数下限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        focusOut();
    }

    public void cherryButton(View view){
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        if (Plus_Minus_Counter == 0) {
            if (!ch.getText().toString().isEmpty()) {
                int inCH = Integer.parseInt(ch.getText().toString());
                if (inCH < 99999) {
                    inCH++;
                    colorButton.cherry(v);
                    ch.setText(String.valueOf(inCH));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.cherry(v);
                ch.setText(String.valueOf(1));
            }
        } else {
            if (!ch.getText().toString().isEmpty()) {
                int inCH = Integer.parseInt(ch.getText().toString());
                if (inCH > 0) {
                    inCH--;
                    colorButton.cherry(v);
                    ch.setText(String.valueOf(inCH));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数下限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        focusOut();
    }

    public void grapesButton(View view){
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        if (Plus_Minus_Counter == 0) {
            if (!gr.getText().toString().isEmpty()) {
                int inGR = Integer.parseInt(gr.getText().toString());
                if (inGR < 999999) {
                    inGR++;
                    colorButton.grapes(v);
                    gr.setText(String.valueOf(inGR));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.grapes(v);
                gr.setText(String.valueOf(1));
            }
        } else {
            if (!gr.getText().toString().isEmpty()) {
                int inGR = Integer.parseInt(gr.getText().toString());
                if (inGR > 0) {
                    inGR--;
                    colorButton.grapes(v);
                    gr.setText(String.valueOf(inGR));
                } else {
                    lowerLimit();
                }
            }
        }
        focusOut();
    }

    public void upperLimit(){
        Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void lowerLimit(){
        Toast toast = Toast.makeText(this, "カウント回数下限に達しました", Toast.LENGTH_SHORT);
        toast.show();
    }
}

