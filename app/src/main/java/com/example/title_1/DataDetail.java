package com.example.title_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class DataDetail extends AppCompatActivity implements TextWatcher {

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

    // カスタムダイアログ内で使用
    ConstraintLayout registerLayout;

    //日付
    EditText showDate;
    String operationDate = "";
    String operationYear;
    String operationMonth;
    String operationDay;
    String operationDayDigit;
    String weekId;
    String dayOfWeekinMonth;
    // カスタムダイアログ内にある台番号・差枚数入力用のEditText
    static EditText machineText,medalText;
    // 店舗名
    static String storeName = "";
    // 差枚数
    Integer differenceNumber;
    //機種名
    String machineName = "";
    // 台番号
    String tableNumber;
    // チェックボックス
    CheckBox checkBox;
    int startGame,totalGame,singleBig,cherryBig,singleReg,cherryReg,cherry,grape;

    //DB保存用項目
    static int startValue;
    static int totalValue;
    static int aBValue;
    static int cBValue;
    static int aRValue;
    static int cRValue;
    static int chValue;
    static int grValue;
    static String operationDateValue;
    static String storeNameValue;
    static Integer differenceNumberValue;
    static String tableNumberValue;
    static List<String> storeNames = new ArrayList<>();

    static MainApplication mainApplication = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApplication = (MainApplication) this.getApplication();

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
        TextView keepTimeText = findViewById(R.id.TextKeepTime);
        keepTimeText.setText(keeptime);

        // スピナーを隠すダミーのEditTextに渡されてきた機種名をセット
        dummyText.setText(machine);
        // 機種選択用スピナーのセット
        setJuggler(machine);

        // 取得したIDを使ってDBから必要な項目を取得する
        Context context = this;
        String sql = CreateSQL.DataDetailSelectSQL(ID);
        DatabaseResultSet.aaa("DataDetailSelect",context,sql);

        // DBに存在するすべての店舗名を取得しリストに入れる
        sql = CreateSQL.SelectStoreNameSQL();
        DatabaseResultSet.aaa("DataDetailSelect2",context,sql);

        // DBから取得した各種データを(String型で)セットする
        start.setText(String.valueOf(startValue));
        total.setText(String.valueOf(totalValue));
        aB.setText(String.valueOf(aBValue));
        cB.setText(String.valueOf(cBValue));
        aR.setText(String.valueOf(aRValue));
        cR.setText(String.valueOf(cRValue));
        ch.setText(String.valueOf(chValue));
        gr.setText(String.valueOf(grValue));
        operationDate = operationDateValue;
        differenceNumber = differenceNumberValue;
        storeName = storeNameValue;
        tableNumber = tableNumberValue;
    }

    public void edit_and_back(View view){

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

    public void deleteAndUpdate(View view){

        // 削除ボタン押下時の処理
        if(judge){
            // 注意換気用のアラートを表示
            new AlertDialog.Builder(this)
                    .setTitle("データ削除について")
                    .setMessage("登録データを削除します。よろしいですか？")
                    .setPositiveButton("削除", (dialogInterface, i) -> {

                        // 該当のDB情報を削除する
                        Context context = this;
                        if(!ID.isEmpty()) {
                            String sql = "DELETE FROM TEST WHERE ID = '" + ID + "';";
                            DatabaseResultSet.UpdateOrDelete(context, sql);
                        }

                        Intent intent = new Intent(this,MainGradeInquiry.class);
                        intent.putExtra("TOAST","削除");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    })
                    .setNegativeButton("キャンセル",null)
                    .show();

        } else { //更新ボタンを押下時の処理
            if(!mainApplication.getStore001().equals("null")){
                registerDialog();
            } else {
                notStore();
            }
        }

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

    @SuppressLint("ClickableViewAccessibility")
    public void registerDialog(){

        // ダイアログを定義
        Dialog registerDialog = new Dialog(this);
        // カスタム用のレイアウトをセット
        registerDialog.setContentView(R.layout.main06_datadetail02_custom_dialog);
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

        //日付選択用のEditTextをセット
        showDate = registerDialog.findViewById(R.id.DateEditText);

        // ①日付用EditTextに日付をセット(/の前後は半角スペース)
        // 変更がなされなかった場合でもちゃんとDBに入るのか？ 確認すること
        showDate.setText(operationDateValue);

        // 日付表示用のEditTextにリスナーを登録
        showDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // Calendarインスタンスを取得
                final Calendar date = Calendar.getInstance();

                // DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DataDetail.this,
                        (view1, year, month, dayOfMonth) -> {
                            // 選択した日付を取得して日付表示用のEditTextにセット

                            showDate.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));

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

        // ②20店舗分の登録店舗を(nullじゃなかったら)リストを配列にセット
        // どのように仕様を満たすのかイメージできないので記述しないゴメン
        String[] storeItems = CommonFeature.getStoreItems(mainApplication);
        for(String Item:storeItems){if(!Item.equals("null")){storeNames.add(Item);}}

        // アダプターを介して登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, R.layout.main02_counter05_store_spinner,storeNames);
        storeAdapter.setDropDownViewResource(R.layout.main02_counter06_store_spinner_dropdown);
        storeSpinner.setAdapter(storeAdapter);

        // ③台番号用EditTextに台番号をセットする
        machineText = registerDialog.findViewById(R.id.MachineNumber);
        machineText.setText(tableNumber);

        // ④差枚数用EditTextに差枚数をセット工程は以下のコメントを参照
        medalText = registerDialog.findViewById(R.id.DifferenceNumber);
        checkBox  = registerDialog.findViewById(R.id.checkBox);
        // ④DBから取得した値がマイナスかチェック
        if(differenceNumber < 0){
            differenceNumber = differenceNumber * -1 ;
            checkBox.setChecked(true);
        }
        medalText.setText(String.valueOf(differenceNumber));

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

            // 機種名取得
            machineName = juggler.getSelectedItem().toString();

            // 各種小役を取得すること
            startGame = Integer.parseInt(start.getText().toString());
            totalGame = Integer.parseInt(total.getText().toString());
            singleBig = Integer.parseInt(aB.getText().toString());
            cherryBig = Integer.parseInt(cB.getText().toString());
            singleReg = Integer.parseInt(aR.getText().toString());
            cherryReg = Integer.parseInt(cR.getText().toString());
            cherry = Integer.parseInt(ch.getText().toString());
            grape = Integer.parseInt(gr.getText().toString());

            // チェック用の日付取得
            // DBに格納する値については、前述のonClick(View view)内で取得してある
            String checkShowDate = showDate.getText().toString();

            // 店舗名を取得
            storeName = (String)storeSpinner.getSelectedItem();

            // 台番号取得
            // 現時点でnullがあり得ますので対応すること
            tableNumber = machineText.getText().toString();

            // 差枚数取得
            // null対応は下記でしてるから大丈夫、と思われる
            String differenceNumberStr = medalText.getText().toString();

            //更新日時を取得
            Date now = new Date();
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日HH時mm分");
            String nowDate = dFormat.format(now);


            // 日付入力済なら登録処理
            if (StringUtils.isNotEmpty(checkShowDate)){

                if (StringUtils.isNotEmpty(differenceNumberStr)){
                    differenceNumber = Integer.parseInt(differenceNumberStr);
                }

                // ここで差枚数のnullに対応できている
                if(!(differenceNumber == null || differenceNumber == 0)){
                    if(checkBox.isChecked()) {
                        differenceNumber = -differenceNumber;
                    }
                }

                // ここにDB情報を更新する処理を記述
                Context context = this;
                if(!ID.isEmpty()) {
                    String sql = "UPDATE TEST SET " +
                            "OPERATION_DATE = '" + operationDate + "', " +
                            "STORE_NAME = '" + storeName + "', " +
                            "DIFFERENCE_NUMBER = '" + differenceNumber + "', " +
                            "TABLE_NUMBER = '" + tableNumber + "', " +
                            "START_GAME = '" + startGame + "', " +
                            "TOTAL_GAME = '" + totalGame + "', " +
                            "SINGLE_BIG = '" + singleBig + "', " +
                            "CHERRY_BIG = '" + cherryBig + "', " +
                            "SINGLE_REG = '" + singleReg + "', " +
                            "CHERRY_REG = '" + cherryReg + "', " +
                            "CHERRY = '" + cherry + "', " +
                            "GRAPE = '" + grape + "' " +
                            "WHERE ID = '" + ID + "';";
                    DatabaseResultSet.UpdateOrDelete(context, sql);
                }


                /*

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

                 */

                registerDialog.dismiss();
                focusOut();
                Intent intent = new Intent(this,MainGradeInquiry.class);
                intent.putExtra("TOAST","更新");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(DataDetail.this, "日付を選択してください", Toast.LENGTH_LONG);
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



}
