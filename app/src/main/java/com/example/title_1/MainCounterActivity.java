package com.example.title_1;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class MainCounterActivity extends AppCompatActivity {

    // レイアウト定義
    ConstraintLayout layout;

    // 機種名選択
    Spinner juggler;

    //ゲーム数関係
    static EditText total; static EditText start; static EditText individual;

    //カウンター関係
    static EditText aB; static EditText cB; static EditText BB;
    static EditText aR; static EditText cR; static EditText RB;
    static EditText ch; static EditText gr; static EditText addition;

    //確率関係
    static TextView aB_Probability; static TextView cB_Probability; static TextView BB_Probability;
    static TextView aR_Probability; static TextView cR_Probability; static TextView RB_Probability;
    static TextView ch_Probability; static TextView gr_Probability; static TextView addition_Probability;

    //オプション関係
    boolean plusMinusCounter = false;
    boolean editorModeCounter = false;
    boolean skeletonCounter = false;

    // 共有データ
    static MainApplication mainApplication = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApplication = (MainApplication) this.getApplication();

        setContentView(R.layout.activity_main_counter);

        // 各viewをfindViewByIdで紐づけるメソッド
        setID();
        // 機種名選択のスピナー登録
        setJuggler();
        // ゲーム数・カウント回数を表示するEditTextにテクストウォッチャーを設定するメソッド
        setTextWatcher();
        // 画面起動時には各EditTextを操作できないようにする
        setEditTextFocusFalse();
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になるメソッド
        actionListenerFocusOut();
        // 内部ストレージ関係
        setValue();
    }

    // オプションメニューを表示するメソッド
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    // オプションメニューのアイテムが選択されたときに呼び出されるメソッド
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1: // 編集モード

                if(editorModeCounter){
                    setEditTextFocusFalse();
                    editorModeCounter = false;
                } else {
                    setEditTextFocusTrue();
                    editorModeCounter = true;
                }
                return true;

            case R.id.item2: // 加減算切り替えモード

                if(plusMinusCounter) {
                    ViewItems.setEditTextColer(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                    ViewItems.setTextViewColer(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                    plusMinusCounter = false;
                } else {
                    if(skeletonCounter) {
                        new AlertDialog.Builder(this)
                                .setTitle("ステルス機能の解除について")
                                .setMessage("減算モードを利用する場合はステルス機能を解除する必要があります")
                                .setPositiveButton("解除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ViewItems.setEditTextColer(ViewItems.getCounterTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                                        ViewItems.setTextViewColer(ViewItems.getProbabilityTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                                        skeletonCounter = false;
                                        plusMinusCounter = true;
                                        Toast toast = Toast.makeText(MainCounterActivity.this, "ステルス機能が解除されました", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                })
                                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        plusMinusCounter = false;
                                    }
                                })
                                .show();
                    } else{
                        ViewItems.setEditTextColer(ViewItems.getCounterTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                        ViewItems.setTextViewColer(ViewItems.getProbabilityTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                        plusMinusCounter = true;
                    }
                }
                return true;

            case R.id.item3: // カウンター非表示モード

                if(skeletonCounter) {
                    ViewItems.setEditTextColer(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                    ViewItems.setTextViewColer(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                    skeletonCounter = false;
                } else {
                    if(plusMinusCounter) {
                        new AlertDialog.Builder(this)
                                .setTitle("減算状態の解除について")
                                .setMessage("ステルス機能を利用する場合は減算状態を解除する必要があります")
                                .setPositiveButton("解除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int skeleton = getResources().getColor(R.color.skeleton);
                                        ViewItems.setEditTextColer(ViewItems.getCounterTextItems(),skeleton,Typeface.DEFAULT);
                                        ViewItems.setTextViewColer(ViewItems.getProbabilityTextItems(),skeleton,Typeface.DEFAULT);
                                        skeletonCounter = true;
                                        plusMinusCounter = false;
                                        Toast toast = Toast.makeText(MainCounterActivity.this, "カウンターは加算されます", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                })
                                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        skeletonCounter = false;
                                    }
                                })
                                .show();
                    } else {
                        int skeleton = getResources().getColor(R.color.skeleton);
                        ViewItems.setEditTextColer(ViewItems.getCounterTextItems(),skeleton,Typeface.DEFAULT);
                        ViewItems.setTextViewColer(ViewItems.getProbabilityTextItems(),skeleton,Typeface.DEFAULT);
                        skeletonCounter = true;
                    }
                }
                return true;

            case R.id.item4:
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

                                mainApplication.init();
                                CreateXML.updateText(mainApplication,"total","");
                                CreateXML.updateText(mainApplication,"start","");

                                ViewItems.setEditTextColer(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                                ViewItems.setTextViewColer(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                                setEditTextFocusFalse();

                                plusMinusCounter = false;
                                editorModeCounter = false;
                                skeletonCounter = false;

                                Toast toast = Toast.makeText(MainCounterActivity.this, "リセットしました", Toast.LENGTH_SHORT);
                                toast.show();

                            }
                        })
                        .setNegativeButton("いいえ",null)
                        .show();
                return true;

            case R.id.item5:
                if(!mainApplication.getStore001().equals("null")){
                    storeRegister();
                } else {
                    notStore();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setID(){
        total = findViewById(R.id.total_game);
        start = findViewById(R.id.start_game);
        individual = findViewById(R.id.individual_game);
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
        layout = findViewById(R.id.counter);
        juggler = findViewById(R.id.juggler);
    }

    public void setTextWatcher(){
        //ゲーム数関係とカウンター関係へGamesCounterWatcherを設定
        EditText[] items = ViewItems.joinEditTexts(ViewItems.getGameTextItems(),ViewItems.getCounterTextItems());
        for (EditText item: items){
            item.addTextChangedListener(new GamesCounterWatcher(item,mainApplication));
        }
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

    public void actionListenerFocusOut(){
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になる

        setImeActionDone(start); setImeActionDone(total);
        setImeActionDone(aB); setImeActionDone(cB);
        setImeActionDone(aR); setImeActionDone(cR);
        setImeActionDone(ch); setImeActionDone(gr);

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

    public void setImeActionDone(EditText editText){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;}});
    }

    public void storeRegister(){
        // 登録店舗名を表示するためのプルダウン(スピナー)を設定
        final Spinner storeSpinner = new Spinner(this);
        List<String> storeNames = new ArrayList<String>();

        //----------------------------------------------------------------------------------------------------------
        // 20店舗分の登録店舗を(nullじゃなかったら)リストにセット
        String[] storeItems = CommonFeature.getStoreItems(mainApplication);
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

    public void notStore(){
        new AlertDialog.Builder(this)
                .setTitle("店舗登録のお願い")
                .setMessage("データを残したい場合は店舗登録を行ってください")
                .setPositiveButton("店舗登録へ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplication(), StoreManagement.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("戻る", null)
                .show();
    }

    public void setEditTextFocusTrue(){
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.a_3_smallrole_edit, null);
        ViewItems.setEditTextFocus(ViewItems.getCounterEditTextItems(),true,true,background);
    }

    public void setEditTextFocusFalse(){
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.a_4_default, null);
        ViewItems.setEditTextFocus(ViewItems.getCounterEditTextItems(),false,false,background);
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

    public void focusOut(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
        layout.requestFocus();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        focusOut();
        return true;
    }


    //-----------------------------------------------------------------------------------------------
    //ここからボタンの制御
    //-----------------------------------------------------------------------------------------------

    public void aloneBigButton(View view) {
        pushButton(aB,R.id.aB,9999);
    }
    public void cherryBigButton(View view) {
        pushButton(cB,R.id.cB,9999);
    }
    public void aloneRegButton(View view){
        pushButton(aR,R.id.aR,9999);
    }
    public void cherryRegButton(View view){
        pushButton(cR,R.id.cR,9999);
    }
    public void cherryButton(View view){
        pushButton(ch,R.id.ch,99999);
    }
    public void grapesButton(View view){
        pushButton(gr,R.id.gr,999999);
    }

    public void pushButton(EditText editText, int id, int limit) {
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        String text = editText.getText().toString();
        int textValue = 0;
        if (StringUtils.isNotEmpty(text)){
            textValue = Integer.parseInt(text);
         }

        if (plusMinusCounter) {

            if (StringUtils.isNotEmpty(text)) {
                if (textValue > 0) {
                    textValue--;
                    colorButton.setFlash(v,id);
                    editText.setText(String.valueOf(textValue));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数下限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        } else {

            if (StringUtils.isNotEmpty(text)) {

                if (textValue < limit) {
                    textValue++;
                    colorButton.setFlash(v,id);
                    editText.setText(String.valueOf(textValue));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.setFlash(v,id);
                editText.setText(String.valueOf(1));
            }
        }
        focusOut();
    }

}

