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
import java.util.HashSet;
import java.util.List;

public class DataDetail extends AppCompatActivity implements TextWatcher {

    // 前画面から渡されてきた情報を受け取る変数
    String catchID, catchDate, catchStore, catchMachine, catchRegisterDate;

    // レイアウト
    ConstraintLayout mainLayout, scrollLayout;

    //ゲーム数関係
    static EditText eStartGames, eTotalGames, eIndividualGames;

    //カウンター関係
    static EditText eSingleBig, eCherryBig, eTotalBig, eSingleReg, eCherryReg, eTotalReg, eCherry, eGrape, eTotalBonus;

    //確率関係
    static TextView tSingleBigProbability, tCherryBigProbability, tTotalBigProbability,
                    tSingleRegProbability, tCherryRegProbability, tTotalRegProbability,
                    tCherryProbability, tGrapeProbability, tTotalBonusProbability;

    // カウンター用のボタン(操作可能状態に切り替えるためのもの)
    static Button bSingleBig,bCherryBig,bTotalBig,bSingleReg,bCherryReg,bTotalReg,bCherry,bGrape,bTotalBonus;
    // 機能ボタン
    Button bEditAndBack, bDeleteAndUpdate;

    // 判定用
    boolean judge = true,judgePlusMinus = true;

    //機種名関係
    Spinner juggler;
    EditText eDummy;

    // カスタムダイアログ関係
    ConstraintLayout registerLayout;
    EditText eDate;
    static EditText eTableNumber, eMedal;

    //DB関係
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

    // チェックボックス
    CheckBox checkBox;

    // DB初期値
    static String dbStoreNameValue,dbTableNumberValue;
    static int dbStartValue,dbTotalValue,dbSingleBigValue,dbCherryBigValue,
               dbSingleRegValue,dbCherryRegValue,dbCherryValue,dbGrapeValue,
               dbMedalValue,dbOperationYearValue,dbOperationMonthValue,dbOperationDayValue,
               dbOperationDayDigitValue,dbWeekIdValue,dbDayOfWeek_in_MonthValue;

    // DBにある全ての店舗を格納するための配列
    static List<String> DB_Store = new ArrayList<>();

    static MainApplication mainApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApplication = (MainApplication) this.getApplication();

        setContentView(R.layout.main05_datadetail01);

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
        EditText[] items = ViewItems.joinEditTexts(ViewItems.getDetailGameTextItems(), ViewItems.getDetailCounterTextItems());
        for (EditText item : items) {
            item.addTextChangedListener(new DateDetailCounterWatcher(item));
        }

        // 個別データ画面から渡されてきたデータを取得
        Intent intent = getIntent();
        // 渡されてきたデータを取り出す
        catchID = intent.getStringExtra("ID");
        catchDate = intent.getStringExtra("Date");
        catchStore = intent.getStringExtra("Store");
        catchMachine = intent.getStringExtra("Machine");
        catchRegisterDate = intent.getStringExtra("KeepTime");

        // 登録日時をTextViewにセット
        TextView keepTimeText = findViewById(R.id.TextKeepTime);
        keepTimeText.setText(catchRegisterDate);

        // スピナーを隠すダミーのEditTextに渡されてきた機種名をセット
        eDummy.setText(catchMachine);
        // 機種選択用スピナーのセット
        setJuggler(catchMachine);

        // 取得したIDを使ってDBから必要な項目を取得する
        Context context = this;
        String sql = CreateSQL.DataDetailSelectSQL(catchID);
        DatabaseResultSet.execution("DataDetailSelect", context, sql);

        // DBに存在するすべての店舗名を取得しリストに入れる
        sql = CreateSQL.SelectStoreNameSQL();
        DatabaseResultSet.execution("DataDetailSelect2", context, sql);

        // DB初期値で各種データを初期化
        initValue();
    }

    public void edit_and_back(View view) {

        // 「編集」ボタンを押した処理
        if (judge) {

            // 「編集」ボタンの見た目を「マイナス」ボタンに、「削除」ボタンの見た目を「更新」ボタンにする
            bEditAndBack.setText("マイナス");
            bDeleteAndUpdate.setText("更新");
            judge = false;
            judgePlusMinus = false;

            // スピナーを隠すダミーのEditTextを非表示にする
            eDummy.setVisibility(View.GONE);

            // 各種EditTextをフォーカスを外して編集可能にする
            eStartGames.setFocusable(true);
            eStartGames.setFocusableInTouchMode(true);
            eTotalGames.setFocusable(true);
            eTotalGames.setFocusableInTouchMode(true);
            Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.c_maincounteractivity01_gamesediter, null);
            ViewItems.setDetailEditTextFocus(ViewItems.getDetailCounterEditTextItems(), true, true, background);

            // 各種カウンターボタンを操作可能にする
            ViewItems.setDetailButtonEnabledTrue(ViewItems.getDetailCounterButtonItems(), true);


        } else { // ボタンが「マイナス」表示になっている時に押下した場合の処理

            if (!judgePlusMinus) {
                // 「マイナス」ボタンの見た目を「プラス」ボタンにする
                bEditAndBack.setText("プラス");
                judgePlusMinus = true;
                ViewItems.setEditTextColor(ViewItems.getDetailCounterTextItems(), Color.RED, Typeface.DEFAULT_BOLD);
                ViewItems.setTextViewColor(ViewItems.getDetailProbabilityTextItems(), Color.RED, Typeface.DEFAULT_BOLD);
            } else {
                // 「プラス」ボタンの見た目を「マイナス」ボタンにする
                bEditAndBack.setText("マイナス");
                judgePlusMinus = false;
                ViewItems.setEditTextColor(ViewItems.getDetailCounterTextItems(), Color.WHITE, Typeface.DEFAULT);
                ViewItems.setTextViewColor(ViewItems.getDetailProbabilityTextItems(), Color.WHITE, Typeface.DEFAULT);
            }
        }
    }

    public void deleteAndUpdate(View view) {

        // 削除ボタン押下時の処理
        if (judge) {
            // 注意換気用のアラートを表示
            new AlertDialog.Builder(this)
                    .setTitle("データ削除について")
                    .setMessage("登録データを削除します。よろしいですか？")
                    .setPositiveButton("削除", (dialogInterface, i) -> {

                        // 該当のDB情報を削除する
                        Context context = this;
                        if (!catchID.isEmpty()) {
                            String sql = "DELETE FROM TEST WHERE ID = '" + catchID + "';";
                            DatabaseResultSet.UpdateOrDelete(context, sql);
                        }

                        Intent intent = new Intent(this, MainGradeInquiry.class);
                        intent.putExtra("TOAST", "削除");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    })
                    .setNegativeButton("キャンセル", null)
                    .show();

        } else { //更新ボタンを押下時の処理

            int checkedIndividualGames = Integer.parseInt(eIndividualGames.getText().toString());
            if (checkedIndividualGames != 0) {
                registerDialog();
            } else {
                Toast toast = Toast.makeText(DataDetail.this, "０ゲームでの更新はできません", Toast.LENGTH_LONG);
                toast.show();
                focusOut();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void registerDialog() {

        // ダイアログを定義
        Dialog registerDialog = new Dialog(this);
        // カスタム用のレイアウトをセット
        registerDialog.setContentView(R.layout.main05_datadetail02_custom_dialog);
        // ダイアログのレイアウトをタッチするとフォーカスが外れる
        registerLayout = registerDialog.findViewById(R.id.RegisterLayout);
        registerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerLayout.getWindowToken(), 0);
                registerLayout.requestFocus();
                return false;
            }
        });

        // 日付選択用のEditTextをセット
        eDate = registerDialog.findViewById(R.id.DateEditText);
        eDate.setText(catchDate);

        // 日付表示用のEditTextにリスナーを登録
        eDate.setOnClickListener(new View.OnClickListener() {
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
                            eDate.setText(String.format("%d / %02d / %02d", year, month + 1, dayOfMonth));

                            //DB登録用
                            dbOperationDate = String.format("%d / %02d / %02d", year, month + 1, dayOfMonth);
                            dbOperationYear = Integer.toString(year);
                            dbOperationMonth = Integer.toString(month + 1);
                            dbOperationDay = Integer.toString(dayOfMonth);
                            if (dayOfMonth > 9) {
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
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerLayout.getWindowToken(), 0);
                registerLayout.requestFocus();
            }
        });

        // 店舗表示用のスピナーと店舗名のリストを準備
        Spinner storeSpinner = registerDialog.findViewById(R.id.StoreSpinner);

        // 登録店舗を格納するためのList
        List<String> registerStoreItems = new ArrayList<>();
        // 20店舗分の登録店舗を(nullじゃなかったら)リストを配列にセット
        String[] Register_Store = CommonFeature.getStoreItems(mainApplication);
        for (String Item : Register_Store) {
            if (!Item.equals("null")) { registerStoreItems.add(Item); }
        }
        // 登録店舗+DB内店舗の重複削除前リスト作成
        List<String> beforeStoreItems = new ArrayList<>(registerStoreItems);
        beforeStoreItems.addAll(DB_Store);

        // 登録店舗+DB内店舗の重複削除後リスト作成
        List<String> newStoreItems = new ArrayList<>(new HashSet<>(beforeStoreItems));

        // アダプターを介して登録店舗+DB内店舗の重複削除後のリストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, R.layout.main02_counter05_store_spinner, newStoreItems);
        storeAdapter.setDropDownViewResource(R.layout.main02_counter06_store_spinner_dropdown);
        storeSpinner.setAdapter(storeAdapter);

        // スピナーで変更前の店舗名を初期値として選択
        int index = newStoreItems.indexOf(catchStore);
        storeSpinner.setSelection(index);

        // 台番号用EditTextに台番号をセットする
        eTableNumber = registerDialog.findViewById(R.id.MachineNumber);
        eTableNumber.setText(dbTableNumber);

        // 0判定(登録時に差枚数が空欄だった場合の対応。０登録した場合は空欄がセットされる)
        eMedal = registerDialog.findViewById(R.id.DifferenceNumber);
        checkBox = registerDialog.findViewById(R.id.checkBox);
        if(dbMedal != 0) {
            if (dbMedal < 0) { //DBから取得した値がマイナスかチェック
                dbMedal = dbMedal * -1;
                checkBox.setChecked(true);
            }
            eMedal.setText(String.valueOf(dbMedal));
        }
        eMedal.addTextChangedListener(this);

        // キーボード確定ボタンを押すとフォーカスが外れる
        eMedal.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerLayout.getWindowToken(), 0);
                registerLayout.requestFocus();
            }
            return false;
        });

        // 登録ボタンにリスナー登録
        registerDialog.findViewById(R.id.RegisterButton).setOnClickListener(view -> {

                // 機種名取得
                dbMachineName = juggler.getSelectedItem().toString();

                // 各種小役を取得すること
                dbStartGames = Integer.parseInt(eStartGames.getText().toString());
                dbTotalGames = Integer.parseInt(eTotalGames.getText().toString());
                dbSingleBig = Integer.parseInt(eSingleBig.getText().toString());
                dbCherryBig = Integer.parseInt(eCherryBig.getText().toString());
                dbSingleReg = Integer.parseInt(eSingleReg.getText().toString());
                dbCherryReg = Integer.parseInt(eCherryReg.getText().toString());
                dbCherry = Integer.parseInt(eCherry.getText().toString());
                dbGrape = Integer.parseInt(eGrape.getText().toString());

                // 店舗名を取得
                dbStoreName = (String) storeSpinner.getSelectedItem();

                // 台番号取得
                dbTableNumber = eTableNumber.getText().toString();

                // 差枚数取得
                String medalStr = eMedal.getText().toString();
                if (StringUtils.isNotEmpty(medalStr)) {
                    dbMedal = Integer.parseInt(medalStr);
                }
                if (!(dbMedal == null || dbMedal == 0)) {
                    if (checkBox.isChecked()) {
                        dbMedal = -dbMedal;
                    }
                }

                //更新日時を取得
                Date now = new Date();
                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy年MM月dd日HH時mm分");
                dbSaveDate = dFormat.format(now);

                // DB情報を更新する
                Context context = this;
                if (!catchID.isEmpty()) {
                    //DB情報はユーザーID以外全て更新しないといけなかった(要件定義漏れ)
                    String sql = "UPDATE TEST SET " +
                            "OPERATION_DATE = '" + dbOperationDate + "', " +
                            "SAVE_DATE = '" + dbSaveDate + "', " +
                            "STORE_NAME = '" + dbStoreName + "', " +
                            "OPERATION_YEAR = '" + dbOperationYear + "', " +
                            "OPERATION_MONTH = '" + dbOperationMonth + "', " +
                            "OPERATION_DAY = '" + dbOperationDay + "', " +
                            "OPERATION_DAY_DIGIT = '" + dbOperationDayDigit + "', " +
                            "WEEK_ID = '" + dbWeekId + "', " +
                            "DAY_OF_WEEK_IN_MONTH = '" + dbDayOfWeek_in_Month + "', " +
                            "DIFFERENCE_NUMBER = '" + dbMedal + "', " +
                            "MACHINE_NAME = '" + dbMachineName + "', " +
                            "TABLE_NUMBER = '" + dbTableNumber + "', " +
                            "START_GAME = '" + dbStartGames + "', " +
                            "TOTAL_GAME = '" + dbTotalGames + "', " +
                            "SINGLE_BIG = '" + dbSingleBig + "', " +
                            "CHERRY_BIG = '" + dbCherryBig + "', " +
                            "SINGLE_REG = '" + dbSingleReg + "', " +
                            "CHERRY_REG = '" + dbCherryReg + "', " +
                            "CHERRY = '" + dbCherry + "', " +
                            "GRAPE = '" + dbGrape + "' " +
                            "WHERE ID = '" + catchID + "';";
                    DatabaseResultSet.UpdateOrDelete(context, sql);
                }
                registerDialog.dismiss();
                focusOut();
                Intent intent = new Intent(this, MainGradeInquiry.class);
                intent.putExtra("TOAST", "更新");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        });
        // ダイアログを表示
        registerDialog.show();
    }

    public void setFindViewByID() {
        mainLayout = findViewById(R.id.EditLayout);
        scrollLayout = findViewById(R.id.TouchLayout);
        eDummy = findViewById(R.id.DummyText);
        juggler = findViewById(R.id.Juggler);
        eTotalGames = findViewById(R.id.total_game);
        eStartGames = findViewById(R.id.start_game);
        eIndividualGames = findViewById(R.id.individual_game);
        eSingleBig = findViewById(R.id.aB);
        eCherryBig = findViewById(R.id.cB);
        eTotalBig = findViewById(R.id.BB);
        eSingleReg = findViewById(R.id.aR);
        eCherryReg = findViewById(R.id.cR);
        eTotalReg = findViewById(R.id.RB);
        eCherry = findViewById(R.id.ch);
        eGrape = findViewById(R.id.gr);
        eTotalBonus = findViewById(R.id.addition);
        tSingleBigProbability = findViewById(R.id.aB_Probability);
        tCherryBigProbability = findViewById(R.id.cB_Probability);
        tTotalBigProbability = findViewById(R.id.BB_Probability);
        tSingleRegProbability = findViewById(R.id.aR_Probability);
        tCherryRegProbability = findViewById(R.id.cR_Probability);
        tTotalRegProbability = findViewById(R.id.RB_Probability);
        tCherryProbability = findViewById(R.id.ch_Probability);
        tGrapeProbability = findViewById(R.id.gr_Probability);
        tTotalBonusProbability = findViewById(R.id.addition_Probability);
        bEditAndBack = findViewById(R.id.EditButton);
        bDeleteAndUpdate = findViewById(R.id.DeleteButton);
        bSingleBig = findViewById(R.id.alone_big);
        bCherryBig = findViewById(R.id.cherry_big);
        bTotalBig = findViewById(R.id.big_bonus);
        bSingleReg = findViewById(R.id.alone_reg);
        bCherryReg = findViewById(R.id.cherry_reg);
        bTotalReg = findViewById(R.id.reg_bonus);
        bCherry = findViewById(R.id.cherry);
        bGrape = findViewById(R.id.grapes);
        bTotalBonus = findViewById(R.id.bonus_addition);
    }

    public void setJuggler(String machine) {
        List<String> jugglerList = new ArrayList<>(Arrays.asList("SアイムジャグラーEX", "Sファンキージャグラー2", "Sマイジャグラー5"));
        ArrayAdapter<String> jugglerAdapter = new ArrayAdapter<>(this, R.layout.main02_counter02_juggler_spinner, jugglerList);
        jugglerAdapter.setDropDownViewResource(R.layout.main02_counter03_juggler_spinner_dropdown);
        juggler.setAdapter(jugglerAdapter);
        juggler.setSelection(getMachineSelection(machine));
    }

    public int getMachineSelection(String machine) {
        int selection = 0; //デフォルトはSアイムジャグラーEX
        switch (machine) {
            case "Sファンキージャグラー2":
                selection = 1;
                break;
            case "Sマイジャグラー5":
                selection = 2;
                break;
        }
        return selection;
    }

    public void actionListenerFocusOut() {
        // キーボードの確定ボタンを押すと同時にエディットテキストのフォーカスが外れ、キーボードも非表示になる
        setImeActionDone(eStartGames);
        setImeActionDone(eTotalGames);
        setImeActionDone(eSingleBig);
        setImeActionDone(eCherryBig);
        setImeActionDone(eSingleReg);
        setImeActionDone(eCherryReg);
        setImeActionDone(eCherry);
        setImeActionDone(eGrape);

        juggler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                focusOut();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void setImeActionDone(EditText editText) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                focusOut();
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchEvent() {
        scrollLayout.setOnClickListener(v -> focusOut());
        bTotalBig.setOnClickListener(v -> DataDetail.this.focusOut());
        bTotalReg.setOnClickListener(v -> DataDetail.this.focusOut());
        bTotalBonus.setOnClickListener(v -> DataDetail.this.focusOut());
    }

    public void focusOut() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        mainLayout.requestFocus();
    }

    public void singleBigButton(View view) {pushButton(eSingleBig, R.id.aB, 9999);}
    public void cherryBigButton(View view) {pushButton(eCherryBig, R.id.cB, 9999);}
    public void singleRegButton(View view) {pushButton(eSingleReg, R.id.aR, 9999);}
    public void cherryRegButton(View view) {pushButton(eCherryReg, R.id.cR, 9999);}
    public void cherryButton(View view) {pushButton(eCherry, R.id.ch, 99999);}
    public void grapeButton(View view) {pushButton(eGrape, R.id.gr, 999999);}
    public void pushButton(EditText editText, int id, int limit) {
        View v = findViewById(R.id.EditLayout);
        ColorButton colorButton = new ColorButton();
        String text = editText.getText().toString();
        int textValue = 0;
        if (StringUtils.isNotEmpty(text)) {
            textValue = Integer.parseInt(text);
        }

        if (judgePlusMinus) {

            if (StringUtils.isNotEmpty(text)) {
                if (textValue > 0) {
                    textValue--;
                    colorButton.setFlash(v, id);
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
                    colorButton.setFlash(v, id);
                    editText.setText(String.valueOf(textValue));
                } else {
                    Toast toast = Toast.makeText(this, "カウント回数上限に達しました", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.setFlash(v, id);
                editText.setText(String.valueOf(1));
            }
        }
        focusOut();
    }

    // カスタムダイアログ内の差枚数入力用のEditTextにセット
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        eMedal.removeTextChangedListener(this);
        String str = s.toString();
        if (StringUtils.isNotEmpty(str)) {
            str = Integer.valueOf(str).toString();
            eMedal.setText(str);
            eMedal.setSelection(str.length());
        } else {
            eMedal.setText("");
        }
        eMedal.addTextChangedListener(this);
    }

    public void initValue() {
        dbOperationDate = catchDate;
        dbStoreName = catchStore;
        dbOperationYear = Integer.toString(dbOperationYearValue);
        dbOperationMonth = Integer.toString(dbOperationMonthValue);
        dbOperationDay = Integer.toString(dbOperationDayValue);
        dbOperationDayDigit = Integer.toString(dbOperationDayDigitValue);
        dbWeekId = Integer.toString(dbWeekIdValue);
        dbDayOfWeek_in_Month = Integer.toString(dbDayOfWeek_in_MonthValue);
        dbMedal = dbMedalValue;
        dbMachineName = catchMachine;
        dbTableNumber = dbTableNumberValue;

        // ①総ゲーム数と②開始ゲーム数のセットを逆にするとクラッシュします
        // 詳細はMainCounterWatcher.javaのtotal_game処理内に記述してある
        eTotalGames.setText(String.valueOf(dbTotalValue)); //①
        eStartGames.setText(String.valueOf(dbStartValue)); //②
        eSingleBig.setText(String.valueOf(dbSingleBigValue));
        eCherryBig.setText(String.valueOf(dbCherryBigValue));
        eSingleReg.setText(String.valueOf(dbSingleRegValue));
        eCherryReg.setText(String.valueOf(dbCherryRegValue));
        eCherry.setText(String.valueOf(dbCherryValue));
        eGrape.setText(String.valueOf(dbGrapeValue));
    }
}
