package com.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataDetail extends AppCompatActivity {

    // 前画面から渡されてきた情報を受け取る変数
    String ID,machine,date,keeptime;

    // レイアウト
    ConstraintLayout layout,touchLayout;

    //ゲーム数関係
    static EditText start,total,individual;

    //カウンター関係
    static EditText aB,cB,BB,aR,cR,RB,ch,gr,addition;

    //確率関係
    static TextView aB_Probability,cB_Probability,BB_Probability,aR_Probability,cR_Probability,RB_Probability,
            ch_Probability,gr_Probability,addition_Probability;

    //機種名
    Spinner juggler;
    EditText dummyText;

    // 機能ボタン
    Button edit_and_back_Button, delete_and_update_Button;
    // 判定用
    boolean judge = true;
    boolean plusMinusCounter = true;

    // カウンター用のボタン
    static Button sbButton,cbButton,big,srButton,crButton,reg,chButton,grButton,bonus;





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
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になるメソッド
        actionListenerFocusOut();
        // タッチイベント設定
        setTouchEvent();
        // 各種EditTextにTextWatcherをセット
        EditText[] items = ViewItems.joinEditTexts(ViewItems.getDetailGameTextItems(),ViewItems.getDetailCounterTextItems());
        for (EditText item: items){
            item.addTextChangedListener(new DetailCounterWatcher(item));
        }


        // 個別データ画面から渡されてきたデータを取得
        Intent intent = getIntent();
        // 渡されてきたデータを取り出す
        ID = intent.getStringExtra("ID");
        machine = intent.getStringExtra("Machine");
        date = intent.getStringExtra("Date");
        keeptime = intent.getStringExtra("KeepTime");

        // 日付と登録日時をTextViewにセット
        TextView dateText = findViewById(R.id.TextDate);
        dateText.setText(date);
        TextView keepTimeText = findViewById(R.id.TextKeepTime);
        keepTimeText.setText(keeptime);

        // スピナーを隠すダミーのEditTextに渡されてきた機種名をセット
        dummyText.setText(machine);
        // 機種選択用スピナーのセット
        setJuggler(machine);







        // 取得したIDを使ってDBから必要な項目を取得するコードをここに記述する







        // DBから取得した各種データを(String型で)セットする
        start.setText("162");
        total.setText("584");
        aB.setText("1");
        cB.setText("2");
        BB.setText("3");                            //単独BIG+チェリー重複BIGで算出
        aR.setText("1");
        cR.setText("0");
        RB.setText("1");                            //単独REG+チェリー重複REGで算出
        ch.setText("10");
        gr.setText("69");
        addition.setText("4");
    }

    public void edit_amd_back(View view){

        // 「編集」ボタンを押した処理
        if(judge){

            // 「編集」ボタンの見た目を「マイナス」ボタンに、「削除」ボタンの見た目を「更新」ボタンにする
            edit_and_back_Button.setText("マイナス");
            delete_and_update_Button.setText("更新");
            judge = false;
            plusMinusCounter = false;

            // スピナーを隠すダミーのEditTextを非表示にする
            dummyText.setVisibility(View.GONE);

            // 各種EditTextをフォーカスを外して編集可能にする
            start.setFocusable(true); start.setFocusableInTouchMode(true);
            total.setFocusable(true); total.setFocusableInTouchMode(true);
            Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.c_maincounteractivity01_gamesediter, null);
            ViewItems.setDetailEditTextFocus(ViewItems.getDetailCounterEditTextItems(),true,true,background);

            // 各種カウンターボタンを操作可能にする
            ViewItems.setDetailButtonEnabledTrue(ViewItems.getDetailCounterButtonItems(),true);


        } else { // ボタンが「マイナス」表示になっている時に押下した場合の処理

            if (!plusMinusCounter){
                // 「マイナス」ボタンの見た目を「プラス」ボタンにする
                edit_and_back_Button.setText("プラス");
                plusMinusCounter = true;
                ViewItems.setEditTextColor(ViewItems.getDetailCounterTextItems(), Color.RED, Typeface.DEFAULT_BOLD);
                ViewItems.setTextViewColor(ViewItems.getDetailProbabilityTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
            } else {
                // 「プラス」ボタンの見た目を「マイナス」ボタンにする
                edit_and_back_Button.setText("マイナス");
                plusMinusCounter = false;
                ViewItems.setEditTextColor(ViewItems.getDetailCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                ViewItems.setTextViewColor(ViewItems.getDetailProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
            }
        }
    }

    public void delete_and_update(View view){








        // ここに該当のDB情報を削除する処理を記述
        // ここにDB情報を更新する処理を記述












    }

    public void setFindViewByID() {
        layout = findViewById(R.id.EditLayout); touchLayout = findViewById(R.id.TouchLayout);
        dummyText = findViewById(R.id.DummyText); juggler = findViewById(R.id.Juggler);
        total = findViewById(R.id.total_game); start = findViewById(R.id.start_game); individual = findViewById(R.id.individual_game);
        aB = findViewById(R.id.aB); cB = findViewById(R.id.cB); BB = findViewById(R.id.BB);
        aR = findViewById(R.id.aR); cR = findViewById(R.id.cR); RB = findViewById(R.id.RB);
        ch = findViewById(R.id.ch); gr = findViewById(R.id.gr); addition = findViewById(R.id.addition);
        aB_Probability = findViewById(R.id.aB_Probability); cB_Probability = findViewById(R.id.cB_Probability); BB_Probability = findViewById(R.id.BB_Probability);
        aR_Probability = findViewById(R.id.aR_Probability); cR_Probability = findViewById(R.id.cR_Probability); RB_Probability = findViewById(R.id.RB_Probability);
        ch_Probability = findViewById(R.id.ch_Probability); gr_Probability = findViewById(R.id.gr_Probability); addition_Probability = findViewById(R.id.addition_Probability);
        edit_and_back_Button = findViewById(R.id.EditButton);
        delete_and_update_Button = findViewById(R.id.DeleteButton);
        sbButton = findViewById(R.id.alone_big); cbButton = findViewById(R.id.cherry_big); big = findViewById(R.id.big_bonus);
        srButton = findViewById(R.id.alone_reg); crButton = findViewById(R.id.cherry_reg); reg = findViewById(R.id.reg_bonus);
        chButton = findViewById(R.id.cherry); grButton = findViewById(R.id.grapes); bonus = findViewById(R.id.bonus_addition);
    }

    public void setJuggler(String machine){
        List<String> jugglerList = new ArrayList<>(Arrays.asList("SアイムジャグラーEX", "Sファンキージャグラー2", "Sマイジャグラー5"));
        ArrayAdapter<String> jugglerAdapter = new ArrayAdapter<>(this,R.layout.main02_counter02_juggler_spinner,jugglerList);
        jugglerAdapter.setDropDownViewResource(R.layout.main02_counter03_juggler_spinner_dropdown);
        juggler.setAdapter(jugglerAdapter);
        juggler.setSelection(getMachineSelection(machine));
    }

    public int getMachineSelection(String machine){
        int selection = 0; //デフォルトはSアイムジャグラーEX
        switch (machine){
            case "Sファンキージャグラー2":
                selection = 1;
                break;
            case "Sマイジャグラー5":
                selection = 2;
                break;
        }
        return selection;
    }

    public void actionListenerFocusOut(){
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になる
        setImeActionDone(start); setImeActionDone(total); setImeActionDone(aB); setImeActionDone(cB);
        setImeActionDone(aR); setImeActionDone(cR); setImeActionDone(ch); setImeActionDone(gr);

        juggler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                focusOut();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void setImeActionDone(EditText editText){
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i== EditorInfo.IME_ACTION_DONE){
                focusOut();}
            return false;});
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchEvent(){
        touchLayout.setOnClickListener(v -> focusOut());
        big.setOnClickListener(v -> DataDetail.this.focusOut());
        reg.setOnClickListener(v -> DataDetail.this.focusOut());
        bonus.setOnClickListener(v -> DataDetail.this.focusOut());
    }

    public void focusOut(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
        layout.requestFocus();
    }

    public void aloneBigButton(View view) {pushButton(aB,R.id.aB,9999);}
    public void cherryBigButton(View view) {pushButton(cB,R.id.cB,9999);}
    public void aloneRegButton(View view){pushButton(aR,R.id.aR,9999);}
    public void cherryRegButton(View view){pushButton(cR,R.id.cR,9999);}
    public void cherryButton(View view){pushButton(ch,R.id.ch,99999);}
    public void grapesButton(View view){pushButton(gr,R.id.gr,999999);}
    public void pushButton(EditText editText, int id, int limit) {
        View v = findViewById(R.id.EditLayout);
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
