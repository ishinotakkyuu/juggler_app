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
import java.util.Date;
import java.util.List;

public final class MainCounterActivity extends AppCompatActivity implements TextWatcher {

    // フォーカス処理に使うレイアウト・ボタン
    ConstraintLayout mainLayout, scrollLayout;
    Button bTotalBig, bTotalReg, bTotalBonus;

    // 機種名選択
    Machines machines;
    Spinner sJuggler;

    //ゲーム数関係
    static EditText eStartGames, eTotalGames, eIndividualGames;

    //カウンター関係
    static EditText eSingleBig, eCherryBig, eTotalBig, eSingleReg, eCherryReg, eTotalReg, eCherry, eGrape, eTotalBonus;

    //確率関係
    static TextView tSingleBigProbability, tCherryBigProbability, tTotalBigProbability,
                    tSingleRegProbability, tCherryRegProbability, tTotalRegProbability,
                    tCherryProbability, tGrapeProbability, tTotalBonusProbability;

    //各種判定用
    boolean judgePlusMinus = false, judgeSkeleton = false, judgeEditorMode = false, judgeVibrator = true;

    // カスタムダイアログ関係
    ConstraintLayout dialogLayout;
    static EditText eTableNumber, eMedal;
    EditText eDate;

    //DB関係
    int dbUserId;
    String dbOperationDate = "";
    String dbSaveDate = "";
    String dbStoreName = "";
    String dbOperationYear;
    String dbOperationMonth;
    String dbOperationDay;
    String dbOperationDayDigit;
    String dbWeekId;
    String dbDayOfWeek_in_Month;
    Integer dbMedal;
    String dbMachineName = "";
    String dbTableNumber;
    int dbStartGames, dbTotalGames, dbSingleBig, dbCherryBig, dbSingleReg, dbCherryReg, dbCherry, dbGrape;

    // バイブ機能
    Vibrator vibrator;
    VibrationEffect vibrationEffect; // API26以上のOSで有効

    // 共有データ
    static MainApplication mainApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApplication = (MainApplication) this.getApplication();
        machines = new Machines(getResources()); //Machineクラスのインスタンス生成
        setContentView(R.layout.main02_counter01);

        // 各viewをfindViewByIdで紐づける
        setFindViewByID();
        // 機種名選択のスピナー登録
        setJuggler();
        // ゲーム数・カウント回数を表示するEditTextにテキストウォッチャーを設定するメソッド
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

                if(judgeEditorMode){
                    setEditTextFocusFalse();
                    judgeEditorMode = false;
                } else {
                    setEditTextFocusTrue();
                    judgeEditorMode = true;
                }
                focusOut();
                return true;

            case R.id.item2: // 加減算切り替えモード

                if(judgePlusMinus) {
                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                    judgePlusMinus = false;
                } else {
                    if(judgeSkeleton) {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.stealth_release_tittle))
                                .setMessage(getString(R.string.stealth_release_message))
                                .setPositiveButton(getString(R.string.release), (dialogInterface, i) -> {
                                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                                    judgeSkeleton = false;
                                    judgePlusMinus = true;
                                    Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.stealth_toast), Toast.LENGTH_LONG);
                                    toast.show();
                                })
                                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> judgePlusMinus = false)
                                .show();
                    } else{
                        ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                        ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.RED,Typeface.DEFAULT_BOLD);
                        judgePlusMinus = true;
                    }
                }
                focusOut();
                return true;

            case R.id.item3: // カウンター非表示モード

                if(judgeSkeleton) {
                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                    judgeSkeleton = false;
                } else {
                    if(judgePlusMinus) {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.plus_minus_release_tittle))
                                .setMessage(getString(R.string.plus_minus_release_message))
                                .setPositiveButton(getString(R.string.release), (dialogInterface, i) -> {
                                    int skeleton = getResources().getColor(R.color.skeleton);
                                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),skeleton,Typeface.DEFAULT);
                                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),skeleton,Typeface.DEFAULT);
                                    judgeSkeleton = true;
                                    judgePlusMinus = false;
                                    Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.plus_minus_toast), Toast.LENGTH_LONG);
                                    toast.show();
                                })
                                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> judgeSkeleton = false)
                                .show();
                    } else {
                        int skeleton = getResources().getColor(R.color.skeleton);
                        ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),skeleton,Typeface.DEFAULT);
                        ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),skeleton,Typeface.DEFAULT);
                        judgeSkeleton = true;
                    }
                }
                focusOut();
                return true;

            case R.id.item4: // バイブ機能ON/Off
                if(judgeVibrator){
                    judgeVibrator = false;
                    Toast toast = Toast.makeText(this, getString(R.string.vib_off), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    judgeVibrator = true;
                    Toast toast = Toast.makeText(this, getString(R.string.vib_on), Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case R.id.item5: // カウンター初期化
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.counter_init_tittle))
                        .setMessage(getString(R.string.counter_init_message))
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {

                            // 各EditTextの値を初期化
                            EditText[] eSet_Counter = {eStartGames,eTotalGames,eSingleBig,eCherryBig,eSingleReg,eCherryReg,eCherry,eGrape};
                            for(EditText e:eSet_Counter){
                                e.setText("0");
                            }

                            // 内部ストレージの初期化
                            mainApplication.init();
                            CreateXML.updateText(mainApplication,"total","0");
                            CreateXML.updateText(mainApplication,"start","0");

                            ViewItems.setEditTextColor(ViewItems.getCounterTextItems(),Color.WHITE,Typeface.DEFAULT);
                            ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(),Color.WHITE,Typeface.DEFAULT);
                            setEditTextFocusFalse();

                            judgePlusMinus = false;
                            judgeEditorMode = false;
                            judgeSkeleton = false;

                            Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.counter_init_toast), Toast.LENGTH_SHORT);
                            toast.show();
                        })
                        .setNegativeButton(getString(R.string.no),null)
                        .show();
                focusOut();
                return true;

            case R.id.item6: // データ保存
                // 個人ゲーム数が0ゲームの状態で登録ボタンを押した際の処理
                int checkedIndividualGames = Integer.parseInt(eIndividualGames.getText().toString());
                if (checkedIndividualGames != 0){
                    if(!mainApplication.getStore001().equals("null")){
                        registerDialog();
                    } else {
                        pleaseAddStore();
                    }
                    focusOut();
                    return true;
                } else {
                    Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.zero_game_toast), Toast.LENGTH_LONG);
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
        dialogLayout = registerDialog.findViewById(R.id.RegisterLayout);
        dialogLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(dialogLayout.getWindowToken(),0);
                dialogLayout.requestFocus();
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
                            eDate = registerDialog.findViewById(R.id.DateEditText);
                            eDate.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                            eDate.setGravity(Gravity.CENTER);

                            //DB登録用
                            dbOperationDate = String.format("%d / %02d / %02d", year, month+1, dayOfMonth);
                            dbOperationYear = Integer.toString(year);
                            dbOperationMonth = Integer.toString(month+1);
                            dbOperationDay = Integer.toString(dayOfMonth);
                            if(dayOfMonth > 9) {
                                dbOperationDayDigit = dbOperationDay.substring(1);
                            } else {
                                dbOperationDayDigit = dbOperationDay;
                            }
                            date.set(year, month, dayOfMonth);
                            dbWeekId = Integer.toString(date.get(Calendar.DAY_OF_WEEK));
                            dbDayOfWeek_in_Month = Integer.toString(date.get(Calendar.DAY_OF_WEEK_IN_MONTH));

                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );
                //dialogを表示
                datePickerDialog.show();
                // キーボードが出ている(例えば差枚数をクリックしてキーボードを出しっぱなし)状態で日付選択をタッチした場合はキーボードを閉じる
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(dialogLayout.getWindowToken(),0);
                dialogLayout.requestFocus();
            }
        });

        // 店舗表示用のスピナーと店舗名のリストを準備
        Spinner sStore = registerDialog.findViewById(R.id.StoreSpinner);
        List<String> sStore_Items = new ArrayList<>();

        // 20店舗分の登録店舗を(nullじゃなかったら)リストにセット
        String[] Storage_Store_Items = CommonFeature.getStoreItems(mainApplication);
        for(String Item:Storage_Store_Items){if(!Item.equals("null")){sStore_Items.add(Item);}}

        // アダプターを介して登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, R.layout.main02_counter05_store_spinner,sStore_Items);
        storeAdapter.setDropDownViewResource(R.layout.main02_counter06_store_spinner_dropdown);
        sStore.setAdapter(storeAdapter);

        // TextWatcherを設定して0頭を回避する
        // なお、台番号については0頭は許容しておく
        eMedal = registerDialog.findViewById(R.id.DifferenceNumber);
        eMedal.addTextChangedListener(this);

        // キーボード確定ボタンを押すとフォーカスが外れる
        eMedal.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i== EditorInfo.IME_ACTION_DONE){
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(dialogLayout.getWindowToken(),0);
                dialogLayout.requestFocus();
            }
            return false;});

        // 登録ボタンにリスナー登録
        registerDialog.findViewById(R.id.RegisterButton).setOnClickListener(view -> {

            // ユーザID
            dbUserId = mainApplication.getUserId();
            // 店舗名
            dbStoreName = (String)sStore.getSelectedItem();
            String medalStr = eMedal.getText().toString();
            // ここでもう1回findViewすることでクラッシュ回避(ダイアログ出現後にやらないとNULLになるらしい)
            eDate = registerDialog.findViewById(R.id.DateEditText);
            String checkedDate = eDate.getText().toString();

            // 日付入力済なら登録処理
            if (StringUtils.isNotEmpty(checkedDate)){

                if (StringUtils.isNotEmpty(medalStr)){
                    dbMedal = Integer.parseInt(medalStr);
                }

                if(!(dbMedal == null || dbMedal == 0)){
                    CheckBox checkBox  = registerDialog.findViewById(R.id.checkBox);
                    if(checkBox.isChecked()) {
                        dbMedal = -dbMedal;
                    }
                }

                // 機種名取得
                dbMachineName = machines.getNowMachineName(mainApplication.getMachinePosition());

                // 各種カウンター値を取得
                dbStartGames = Integer.parseInt(mainApplication.getStartGames());
                dbTotalGames = Integer.parseInt(mainApplication.getTotalGames());
                dbSingleBig = Integer.parseInt(mainApplication.getSingleBig());
                dbCherryBig = Integer.parseInt(mainApplication.getCherryBig());
                dbSingleReg = Integer.parseInt(mainApplication.getSingleReg());
                dbCherryReg = Integer.parseInt(mainApplication.getCherryReg());
                dbCherry = Integer.parseInt(mainApplication.getCherry());
                dbGrape = Integer.parseInt(mainApplication.getGrape());

                // 台番号取得
                eTableNumber = registerDialog.findViewById(R.id.MachineNumber);
                dbTableNumber = eTableNumber.getText().toString();

                // 現在日時を取得
                Date now = new Date();
                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日HH時mm分");
                dbSaveDate = dFormat.format(now);

                //　データベースへの登録処理
                Context context = getApplicationContext();
                DatabaseHelper helper = new DatabaseHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();

                try {
                    String sql =
                            "insert into TEST (" +
                                    "USER_ID," +
                                    "OPERATION_DATE," +
                                    "SAVE_DATE," +
                                    "STORE_NAME," +
                                    "OPERATION_YEAR," +
                                    "OPERATION_MONTH," +
                                    "OPERATION_DAY," +
                                    "OPERATION_DAY_DIGIT," +
                                    "WEEK_ID," +
                                    "DAY_OF_WEEK_IN_MONTH," +
                                    "DIFFERENCE_NUMBER," +
                                    "MACHINE_NAME," +
                                    "TABLE_NUMBER," +
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
                                    "'" + dbUserId + "'," +
                                    "'" + dbOperationDate + "'," +
                                    "'" + dbSaveDate + "'," +
                                    "'" + dbStoreName + "'," +
                                    "'" + dbOperationYear + "'," +
                                    "'" + dbOperationMonth + "'," +
                                    "'" + dbOperationDay + "'," +
                                    "'" + dbOperationDayDigit + "'," +
                                    "'" + dbWeekId + "'," +
                                    "'" + dbDayOfWeek_in_Month + "'," +
                                    "'" + dbMedal + "'," +
                                    "'" + dbMachineName + "'," +
                                    "'" + dbTableNumber + "'," +
                                    "'" + dbStartGames + "'," +
                                    "'" + dbTotalGames + "'," +
                                    "'" + dbSingleBig + "'," +
                                    "'" + dbCherryBig + "'," +
                                    "'" + dbSingleReg + "'," +
                                    "'" + dbCherryReg + "'," +
                                    "'" + dbCherry + "'," +
                                    "'" + dbGrape + "'" +
                                    ")";
                    SQLiteStatement stmt = db.compileStatement(sql);
                    stmt.executeInsert();

                } catch(Exception ex) {
                    Log.e("MemoPad", ex.toString());
                } finally {
                    db.close();
                }

                Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.register_toast), Toast.LENGTH_LONG);
                toast.show();
                registerDialog.dismiss();
                focusOut();
            } else {
                Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.select_date_toast), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // ダイアログを表示
        registerDialog.show();
    }

    public void pleaseAddStore(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_store_tittle))
                .setMessage(getString(R.string.add_store_message))
                .setPositiveButton(getString(R.string.go_add_store), (dialog, which) -> {
                    Intent intent = new Intent(getApplication(), MainManagementStore.class);
                    startActivity(intent);
                })
                .setNegativeButton(getString(R.string.back), null)
                .show();
    }

    public void vibrator(){
        if (judgeVibrator){
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
        eMedal.removeTextChangedListener(this);
        String medalText = s.toString();
        if (StringUtils.isNotEmpty(medalText)) {
            medalText = Integer.valueOf(medalText).toString();
            eMedal.setText(medalText);
            eMedal.setSelection(medalText.length());
        } else {
            eMedal.setText("");
        }
        eMedal.addTextChangedListener(this);
    }

    //***************************************************************************************************************************
    // 何かしらの設定をセットしている処理
    //***************************************************************************************************************************

    public void setFindViewByID(){
        eTotalGames = findViewById(R.id.total_game);
        eStartGames = findViewById(R.id.start_game);
        eIndividualGames = findViewById(R.id.individual_game);
        eSingleBig = findViewById(R.id.aB); eCherryBig = findViewById(R.id.cB); eTotalBig = findViewById(R.id.BB);
        eSingleReg = findViewById(R.id.aR); eCherryReg = findViewById(R.id.cR); eTotalReg = findViewById(R.id.RB);
        eCherry = findViewById(R.id.ch); eGrape = findViewById(R.id.gr); eTotalBonus = findViewById(R.id.addition);
        tSingleBigProbability = findViewById(R.id.aB_Probability);
        tCherryBigProbability = findViewById(R.id.cB_Probability);
        tTotalBigProbability = findViewById(R.id.BB_Probability);
        tSingleRegProbability = findViewById(R.id.aR_Probability);
        tCherryRegProbability = findViewById(R.id.cR_Probability);
        tTotalRegProbability = findViewById(R.id.RB_Probability);
        tCherryProbability = findViewById(R.id.ch_Probability);
        tGrapeProbability = findViewById(R.id.gr_Probability);
        tTotalBonusProbability = findViewById(R.id.addition_Probability);
        mainLayout = findViewById(R.id.counter);
        scrollLayout = findViewById(R.id.TouchCounter);
        bTotalBig = findViewById(R.id.big_bonus);
        bTotalReg = findViewById(R.id.reg_bonus);
        bTotalBonus = findViewById(R.id.bonus_addition);
        sJuggler = findViewById(R.id.juggler);
    }

    //ゲーム数関係とカウンター関係へCounterWatcherを設定
    public void setTextWatcher(){
        EditText[] items = ViewItems.joinEditTexts(ViewItems.getGameTextItems(),ViewItems.getCounterTextItems());
        for (EditText item: items){
            item.addTextChangedListener(new MainCounterWatcher(item,mainApplication));
        }
    }

    public void setJuggler(){
        List<String> jugglerList = machines.getNowJugglerList();
        ArrayAdapter<String> jugglerAdapter = new ArrayAdapter<>(this,R.layout.main02_counter02_juggler_spinner,jugglerList);
        jugglerAdapter.setDropDownViewResource(R.layout.main02_counter03_juggler_spinner_dropdown);
        sJuggler.setAdapter(jugglerAdapter);
        sJuggler.setSelection(mainApplication.getMachinePosition());
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
        eTotalGames.setText(mainApplication.getTotalGames());
        eStartGames.setText(mainApplication.getStartGames());
        eSingleBig.setText(mainApplication.getSingleBig());
        eCherryBig.setText(mainApplication.getCherryBig());
        eSingleReg.setText(mainApplication.getSingleReg());
        eCherryReg.setText(mainApplication.getCherryReg());
        eCherry.setText(mainApplication.getCherry());
        eGrape.setText(mainApplication.getGrape());
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchEvent(){
        scrollLayout.setOnClickListener(v -> focusOut());
        bTotalBig.setOnClickListener(v -> MainCounterActivity.this.focusOut());
        bTotalReg.setOnClickListener(v -> MainCounterActivity.this.focusOut());
        bTotalBonus.setOnClickListener(v -> MainCounterActivity.this.focusOut());
    }

    //***************************************************************************************************************************
    // フォーカス関係の処理
    //***************************************************************************************************************************

    public void actionListenerFocusOut(){
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になる
        EditText[] eSet_Counter = {eStartGames,eTotalGames,eSingleBig,eCherryBig,eSingleReg,eCherryReg,eCherry,eGrape};
        for(EditText e:eSet_Counter){
            e.setOnEditorActionListener((textView, i, keyEvent) -> {
                if(i== EditorInfo.IME_ACTION_DONE){
                    focusOut();}
                return false;});
        }
        sJuggler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                // 保存処理
                mainApplication.setMachinePosition(position);
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
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
        mainLayout.requestFocus();
    }

    //***************************************************************************************************************************
    // ボタンの制御
    //***************************************************************************************************************************

    public void aloneBigButton(View view) {pushButton(eSingleBig,R.id.aB,9999);
        vibrator();}
    public void cherryBigButton(View view) {pushButton(eCherryBig,R.id.cB,9999);
        vibrator();}
    public void aloneRegButton(View view){pushButton(eSingleReg,R.id.aR,9999);
        vibrator();}
    public void cherryRegButton(View view){pushButton(eCherryReg,R.id.cR,9999);
        vibrator();}
    public void cherryButton(View view){pushButton(eCherry,R.id.ch,99999);
        vibrator();}
    public void grapesButton(View view){pushButton(eGrape,R.id.gr,999999);
        vibrator();}
    public void pushButton(EditText editText, int id, int limit) {
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        String text = editText.getText().toString();
        int textValue = 0;
        if (StringUtils.isNotEmpty(text)){
            textValue = Integer.parseInt(text);
         }

        // プラス状態の場合
        if (judgePlusMinus) {
            if (StringUtils.isNotEmpty(text)) {
                if (textValue > 0) {
                    textValue--;
                    colorButton.setFlash(v,id);
                    editText.setText(String.valueOf(textValue));
                } else {
                    Toast toast = Toast.makeText(this, getString(R.string.lower_limit_count_toast), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } else { //マイナス状態の場合
            if (StringUtils.isNotEmpty(text)) {
                if (textValue < limit) {
                    textValue++;
                    colorButton.setFlash(v,id);
                    editText.setText(String.valueOf(textValue));
                } else {
                    Toast toast = Toast.makeText(this, getString(R.string.upper_limit_count_toast), Toast.LENGTH_SHORT);
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

