package com.example.title_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.icu.util.Calendar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class MainCounterActivity extends AppCompatActivity implements TextWatcher {

    // フォーカス処理に使うレイアウト・ボタン
    ConstraintLayout layout,touchLayout,registerLayout;
    Button big,reg,bonus;

    // 機種名選択
    Spinner juggler;

    //ゲーム数関係
    static EditText start,total,individual;

    //カウンター関係
    static EditText aB,cB,BB,aR,cR,RB,ch,gr,addition;

    //確率関係
    static TextView aB_Probability,cB_Probability,BB_Probability,aR_Probability,cR_Probability,RB_Probability,
                    ch_Probability,gr_Probability,addition_Probability;

    //オプション関係
    boolean plusMinusCounter = false;
    boolean editorModeCounter = false;
    boolean skeletonCounter = false;

    //日付
    String operationDate = "";
    String operationYear;
    String operationMonth;
    String operationDay;
    String operationDayDigit;
    String weekId;
    String dayOfWeekinMonth;

    //店舗名
    String storeName = "";

    // 差枚数
    Integer differenceNumber;

    // 機種
    int machineNameValue;
    String machineName = "";

    // カスタムダイアログ内にある台番号・差枚数入力用のEditText
    static EditText machineText,medalText;

    // バイブ機能に使用
    boolean checkVibrate = true;
    Vibrator vibrator;
    VibrationEffect vibrationEffect; // API26以上のOSで有効

    int startGame,totalGame,singleBig,cherryBig,singleReg,cherryReg,cherry,grape;

    // 共有データ
    static MainApplication mainApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApplication = (MainApplication) this.getApplication();

        setContentView(R.layout.main02_counter01);

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
        // タッチイベント設定
        setTouchEvent();
    }

    //***************************************************************************************************************************
    // オプションメニューに関する処理
    //***************************************************************************************************************************

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
                focusOut();
                return true;

            case R.id.item2: // 加減算切り替えモード

                if(plusMinusCounter) {
                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                    plusMinusCounter = false;
                } else {
                    if(skeletonCounter) {
                        new AlertDialog.Builder(this)
                                .setTitle("ステルス機能の解除について")
                                .setMessage("減算モードを利用する場合はステルス機能を解除する必要があります")
                                .setPositiveButton("解除", (dialogInterface, i) -> {
                                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                                    skeletonCounter = false;
                                    plusMinusCounter = true;
                                    Toast toast = Toast.makeText(MainCounterActivity.this, "ステルス機能が解除されました", Toast.LENGTH_LONG);
                                    toast.show();
                                })
                                .setNegativeButton("キャンセル", (dialogInterface, i) -> plusMinusCounter = false)
                                .show();
                    } else{
                        ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                        ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                        plusMinusCounter = true;
                    }
                }
                focusOut();
                return true;

            case R.id.item3: // カウンター非表示モード

                if(skeletonCounter) {
                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                    skeletonCounter = false;
                } else {
                    if(plusMinusCounter) {
                        new AlertDialog.Builder(this)
                                .setTitle("減算状態の解除について")
                                .setMessage("ステルス機能を利用する場合は減算状態を解除する必要があります")
                                .setPositiveButton("解除", (dialogInterface, i) -> {
                                    int skeleton = getResources().getColor(R.color.skeleton);
                                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),skeleton,Typeface.DEFAULT);
                                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),skeleton,Typeface.DEFAULT);
                                    skeletonCounter = true;
                                    plusMinusCounter = false;
                                    Toast toast = Toast.makeText(MainCounterActivity.this, "カウンターは加算されます", Toast.LENGTH_LONG);
                                    toast.show();
                                })
                                .setNegativeButton("キャンセル", (dialogInterface, i) -> skeletonCounter = false)
                                .show();
                    } else {
                        int skeleton = getResources().getColor(R.color.skeleton);
                        ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),skeleton,Typeface.DEFAULT);
                        ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),skeleton,Typeface.DEFAULT);
                        skeletonCounter = true;
                    }
                }
                focusOut();
                return true;

            case R.id.item4: // バイブ機能ON/Off
                if(checkVibrate){
                    checkVibrate = false;
                    Toast toast = Toast.makeText(this, "バイブ機能をOFFにしました", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    checkVibrate = true;
                    Toast toast = Toast.makeText(this, "バイブ機能をONにしました", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case R.id.item5: // カウンター初期化
                new AlertDialog.Builder(this)
                        .setTitle("カウンター初期化")
                        .setMessage("カウンターを全てリセットしますか？")
                        .setPositiveButton("はい", (dialogInterface, i) -> {
                            start.setText("0"); total.setText("0");
                            aB.setText("0"); cB.setText("0");
                            aR.setText("0"); cR.setText("0");
                            ch.setText("0"); gr.setText("0");

                            mainApplication.init();
                            CreateXML.updateText(mainApplication,"total","0");
                            CreateXML.updateText(mainApplication,"start","0");

                            ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                            ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                            setEditTextFocusFalse();

                            plusMinusCounter = false;
                            editorModeCounter = false;
                            skeletonCounter = false;

                            Toast toast = Toast.makeText(MainCounterActivity.this, "リセットしました", Toast.LENGTH_SHORT);
                            toast.show();
                        })
                        .setNegativeButton("いいえ",null)
                        .show();
                focusOut();
                return true;

            case R.id.item6: // データ保存
                // 個人ゲーム数が0ゲームの状態で登録ボタンを押した際の処理
                int checkIndividual = Integer.parseInt(individual.getText().toString());
                if (checkIndividual != 0){
                    if(!mainApplication.getStore001().equals("null")){
                        registerDialog();
                    } else {
                        notStore();
                    }
                    focusOut();
                    return true;
                } else {
                    Toast toast = Toast.makeText(MainCounterActivity.this, "０ゲームでの登録はできません", Toast.LENGTH_LONG);
                    toast.show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void registerDialog(){

        // ダイアログを定義
        Dialog registerDialog = new Dialog(this);
        // カスタム用のレイアウトをセット
        registerDialog.setContentView(R.layout.main02_counter04_custom_dialog);
        // ダイアログのレイアウトをタッチするとフォーカスが外れる
        registerLayout = registerDialog.findViewById(R.id.RegisterLayout);
        registerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerLayout.getWindowToken(),0);
                registerLayout.requestFocus();
                return false;
            }
        });

        // 日付表示用のEditTextにリスナーを登録
        registerDialog.findViewById(R.id.DateEditText).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // Calendarインスタンスを取得
                final Calendar date = Calendar.getInstance();

                // DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainCounterActivity.this,
                        (view1, year, month, dayOfMonth) -> {
                            // 選択した日付を取得して日付表示用のEditTextにセット
                            EditText showDate = registerDialog.findViewById(R.id.DateEditText);
                            showDate.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                            showDate.setGravity(Gravity.CENTER);

                            //DB登録用
                            operationDate = String.format("%d-%02d-%02d", year, month+1, dayOfMonth);
                            operationYear = Integer.toString(year);
                            operationMonth = Integer.toString(month+1);
                            operationDay = Integer.toString(dayOfMonth);
                            if(dayOfMonth > 9) {
                                operationDayDigit = operationDay.substring(1);
                            } else {
                                operationDayDigit = operationDay;
                            }
                            date.set(year, month, dayOfMonth);
                            weekId = Integer.toString(date.get(Calendar.DAY_OF_WEEK));
                            dayOfWeekinMonth = Integer.toString(date.get(Calendar.DAY_OF_WEEK_IN_MONTH));

                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );
                //dialogを表示
                datePickerDialog.show();
                // キーボードが出ている(例えば差枚数をクリックしてキーボードを出しっぱなし)状態で日付選択をタッチした場合はキーボードを閉じる
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerLayout.getWindowToken(),0);
                registerLayout.requestFocus();
            }
        });

        // 店舗表示用のスピナーと店舗名のリストを準備
        Spinner storeSpinner = registerDialog.findViewById(R.id.StoreSpinner);
        List<String> storeNames = new ArrayList<>();

        // 20店舗分の登録店舗を(nullじゃなかったら)リストにセット
        String[] storeItems = CommonFeature.getStoreItems(mainApplication);
        for(String Item:storeItems){if(!Item.equals("null")){storeNames.add(Item);}}

        // アダプターを介して登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, R.layout.main02_counter05_store_spinner,storeNames);
        storeAdapter.setDropDownViewResource(R.layout.main02_counter06_store_spinner_dropdown);
        storeSpinner.setAdapter(storeAdapter);

        // numberTextにTextWatcherを設定して0頭を回避する
        // なお、台番号については0頭は許容しておく
        medalText = registerDialog.findViewById(R.id.DifferenceNumber);
        medalText.addTextChangedListener(this);

        // キーボード確定ボタンを押すとフォーカスが外れる
        medalText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i== EditorInfo.IME_ACTION_DONE){
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerLayout.getWindowToken(),0);
                registerLayout.requestFocus();
            }
            return false;});

        // 登録ボタンにリスナー登録
        registerDialog.findViewById(R.id.RegisterButton).setOnClickListener(view -> {
            storeName = (String)storeSpinner.getSelectedItem();
            String differenceNumberStr = medalText.getText().toString();
            EditText showDate = registerDialog.findViewById(R.id.DateEditText);
            String checkShowDate = showDate.getText().toString();

            // 日付入力済なら登録処理
            if (StringUtils.isNotEmpty(checkShowDate)){

                if (StringUtils.isNotEmpty(differenceNumberStr)){
                    differenceNumber = Integer.parseInt(differenceNumberStr);
                }

                if(!(differenceNumber == null || differenceNumber == 0)){
                    CheckBox checkBox  = registerDialog.findViewById(R.id.checkBox);
                    if(checkBox.isChecked()) {
                        differenceNumber = -differenceNumber;
                    }
                }

                machineNameValue = mainApplication.getMachineName();
                switch (machineNameValue) {
                    case 0:
                        machineName = "SアイムジャグラーEX";
                        break;
                    case 1:
                        machineName = "Sファンキージャグラー2";
                        break;
                    case 2:
                        machineName = "Sマイジャグラー5";
                        break;
                }

                startGame = Integer.parseInt(mainApplication.getStart());
                totalGame = Integer.parseInt(mainApplication.getTotal());
                singleBig = Integer.parseInt(mainApplication.getaB());
                cherryBig = Integer.parseInt(mainApplication.getcB());
                singleReg = Integer.parseInt(mainApplication.getaR());
                cherryReg = Integer.parseInt(mainApplication.getcR());
                cherry = Integer.parseInt(mainApplication.getCh());
                grape = Integer.parseInt(mainApplication.getGr());

                //　R04.06.02 台番号取得
                machineText = registerDialog.findViewById(R.id.MachineNumber);
                String machineNumber = machineText.getText().toString();
                // R04.06.03　現在日時を取得
                Date now = new Date();
                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日HH時mm分");
                String nowDate = dFormat.format(now);

                //　データベースへの登録処理
                Context context = getApplicationContext();
                DatabaseHelper helper = new DatabaseHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();








                // DB項目にユーザーID(int型)を追加すること
                // DB項目に台番号(String型)を追加すること
                // DB項目に保存日時(String型)を追加すること







                try {
                    String sql =
                            "insert into TEST (" +
                                    "OPERATION_DATE," +
                                    "STORE_NAME," +
                                    "OPERATION_YEAR," +
                                    "OPERATION_MONTH," +
                                    "OPERATION_DAY," +
                                    "OPERATION_DAY_DIGIT," +
                                    "WEEK_ID," +
                                    "DAY_OF_WEEK_IN_MONTH," +
                                    "DIFFERENCE_NUMBER," +
                                    "MACHINE_NAME," +
                                    "START_GAME," +
                                    "TOTAL_GAME," +
                                    "SINGLE_BIG," +
                                    "CHERRY_BIG," +
                                    "SINGLE_REG," +
                                    "CHERRY_REG," +
                                    "CHERRY," +
                                    "GRAPE" +
                                    ") " +
                                    "values(" +
                                    "'" + operationDate + "'," +
                                    "'" + storeName + "'," +
                                    "'" + operationYear + "'," +
                                    "'" + operationMonth + "'," +
                                    "'" + operationDay + "'," +
                                    "'" + operationDayDigit + "'," +
                                    "'" + weekId + "'," +
                                    "'" + dayOfWeekinMonth + "'," +
                                    "'" + differenceNumber + "'," +
                                    "'" + machineName + "'," +
                                    "'" + startGame + "'," +
                                    "'" + totalGame + "'," +
                                    "'" + singleBig + "'," +
                                    "'" + cherryBig + "'," +
                                    "'" + singleReg + "'," +
                                    "'" + cherryReg + "'," +
                                    "'" + cherry + "'," +
                                    "'" + grape + "'" +
                                    ")";
                    SQLiteStatement stmt = db.compileStatement(sql);
                    stmt.executeInsert();

                } catch(Exception ex) {
                    Log.e("MemoPad", ex.toString());
                } finally {
                    db.close();
                }

                Toast toast = Toast.makeText(MainCounterActivity.this, "データを登録しました", Toast.LENGTH_LONG);
                toast.show();
                registerDialog.dismiss();
                focusOut();
            } else {
                Toast toast = Toast.makeText(MainCounterActivity.this, "日付を選択してください", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // ダイアログを表示
        registerDialog.show();
    }

    public void notStore(){
        new AlertDialog.Builder(this)
                .setTitle("店舗登録のお願い")
                .setMessage("データを残したい場合は店舗登録を行ってください")
                .setPositiveButton("店舗登録へ", (dialog, which) -> {
                    Intent intent = new Intent(getApplication(), StoreManagement.class);
                    startActivity(intent);
                })
                .setNegativeButton("戻る", null)
                .show();
    }

    public void vibrator(){
        if (checkVibrate){
            // API26以上だったら
            if (Build.VERSION.SDK_INT >= 26){
                vibrationEffect = VibrationEffect.createOneShot(300,-1);
                vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(vibrationEffect);
            } else {
                vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(300);
            }
        }
    }

    // カスタムダイアログ内の差枚数入力用のEditTextにセット
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        medalText.removeTextChangedListener(this);
        String str = s.toString();
        if (StringUtils.isNotEmpty(str)) {
            str = Integer.valueOf(str).toString();
            medalText.setText(str);
            medalText.setSelection(str.length());
        } else {
            medalText.setText("");
        }
        medalText.addTextChangedListener(this);
    }

    //***************************************************************************************************************************
    // 何かしらの設定をセットしている処理
    //***************************************************************************************************************************

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
        touchLayout = findViewById(R.id.TouchCounter);
        big = findViewById(R.id.big_bonus);
        reg = findViewById(R.id.reg_bonus);
        bonus = findViewById(R.id.bonus_addition);
        juggler = findViewById(R.id.juggler);
    }

    //ゲーム数関係とカウンター関係へGamesCounterWatcherを設定
    public void setTextWatcher(){
        EditText[] items = ViewItems.joinEditTexts(ViewItems.getGameTextItems(),ViewItems.getCounterTextItems());
        for (EditText item: items){
            item.addTextChangedListener(new GamesCounterWatcher(item,mainApplication));
        }
    }

    public void setJuggler(){
        List<String> jugglerList = new ArrayList<>(Arrays.asList("SアイムジャグラーEX", "Sファンキージャグラー2", "Sマイジャグラー5"));
        ArrayAdapter<String> jugglerAdapter = new ArrayAdapter<>(this,R.layout.main02_counter02_juggler_spinner,jugglerList);
        jugglerAdapter.setDropDownViewResource(R.layout.main02_counter03_juggler_spinner_dropdown);
        juggler.setAdapter(jugglerAdapter);
        juggler.setSelection(mainApplication.getMachineName());
    }

    public void setImeActionDone(EditText editText){
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i== EditorInfo.IME_ACTION_DONE){
                focusOut();}
            return false;});
    }

    public void setEditTextFocusTrue(){
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.c_maincounteractivity01_gamesediter, null);
        ViewItems.setEditTextFocus(ViewItems.getCounterEditTextItems(),true,true,background);
    }

    public void setEditTextFocusFalse(){
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.c_maincounteractivity02_focusfalse, null);
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

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchEvent(){
        touchLayout.setOnClickListener(v -> focusOut());
        big.setOnClickListener(v -> MainCounterActivity.this.focusOut());
        reg.setOnClickListener(v -> MainCounterActivity.this.focusOut());
        bonus.setOnClickListener(v -> MainCounterActivity.this.focusOut());
    }

    //***************************************************************************************************************************
    // フォーカス関係の処理
    //***************************************************************************************************************************

    public void actionListenerFocusOut(){
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になる
        setImeActionDone(start); setImeActionDone(total); setImeActionDone(aB); setImeActionDone(cB);
        setImeActionDone(aR); setImeActionDone(cR); setImeActionDone(ch); setImeActionDone(gr);

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

    public void focusOut(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
        layout.requestFocus();
    }

    //***************************************************************************************************************************
    // ボタンの制御
    //***************************************************************************************************************************

    public void aloneBigButton(View view) {pushButton(aB,R.id.aB,9999);
        vibrator();}
    public void cherryBigButton(View view) {pushButton(cB,R.id.cB,9999);
        vibrator();}
    public void aloneRegButton(View view){pushButton(aR,R.id.aR,9999);
        vibrator();}
    public void cherryRegButton(View view){pushButton(cR,R.id.cR,9999);
        vibrator();}
    public void cherryButton(View view){pushButton(ch,R.id.ch,99999);
        vibrator();}
    public void grapesButton(View view){pushButton(gr,R.id.gr,999999);
        vibrator();}
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

