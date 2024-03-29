package delson.android.j_management_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.icu.util.Calendar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class MainCounterActivity extends AppCompatActivity implements TextWatcher, KeyboardVisibility.OnKeyboardVisibilityListener {

    // フォーカス処理に使うレイアウト・ボタン等
    Activity activity;
    InputMethodManager inputMethodManager;
    ConstraintLayout mainLayout, scrollLayout;
    Button bTotalBig, bTotalReg, bTotalBonus;

    // 機種名選択
    Machines machines;
    Spinner sJuggler;

    //ゲーム数関係
    EditText eStartGames, eTotalGames, eIndividualGames;
    static List<EditText> eCounterEditGames;

    //カウンター関係
    EditText eSingleBig, eCherryBig, eTotalBig, eSingleReg, eCherryReg, eTotalReg, eCherry, eGrape, eTotalBonus;
    static List<EditText> eCounterEditRolls;

    //確率関係
    TextView tSingleBigProbability, tCherryBigProbability, tTotalBigProbability,
            tSingleRegProbability, tCherryRegProbability, tTotalRegProbability,
            tCherryProbability, tGrapeProbability, tTotalBonusProbability;
    static List<TextView> eCounterTextProbability;

    //各種判定用
    boolean judgePlusMinus = false, judgeSkeleton = false, judgeEditorMode = false, judgeVibrator = true;

    // カスタムダイアログ関係
    ConstraintLayout dialogLayout;
    EditText eTableNumber, eMedal;
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
    String dbSpecialDay;
    String dbWeekId;
    String dbDayOfWeek_in_Month;
    Integer dbMedal;
    String dbMachineName = "";
    String dbTableNumber;
    String dbSingleNumber = "";

    int dbStartGames, dbTotalGames, dbSingleBig, dbCherryBig, dbSingleReg, dbCherryReg, dbCherry, dbGrape;

    // バイブ機能
    Vibrator vibrator;
    VibrationEffect vibrationEffect; // API26以上のOSで有効

    // フラッシュ機能
    boolean flashJudge = true;

    // 共有データ(店舗用)
    MainApplication mainApplication;

    // 共有データ(ゲーム数および小役数用)
    SharedPreferences counterDate;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main02_counter01);

        // R04.09.23 登録店舗の絡みもあるので一旦残しておく。それ以外の部分は全てSharedPreferencesに変更。
        mainApplication = (MainApplication) this.getApplication();

        // R04.09.23 追加。SharedPreferencesの準備
        counterDate = getSharedPreferences("CounterDate", MODE_PRIVATE);
        editor = counterDate.edit();

        // Machineクラスのインスタンス生成
        machines = new Machines(getResources());

        // 戻るボタン等でキーボードを非表示にされた時のフォーカス対応
        activity = this;
        KeyboardVisibility kv = new KeyboardVisibility(activity);
        kv.setKeyboardVisibilityListener(this);

        // 各viewをfindViewByIdで紐づける
        setFindViewByID();
        // static対応(メモリリーク回避のためVer2.0.0から実装)
        setViewsArrayList();
        // 機種名選択のスピナー登録
        setJuggler();
        // ゲーム数・カウント回数を表示するEditTextにテキストウォッチャーを設定するメソッド
        setTextWatcher();
        // 画面起動時には各EditTextを操作できないようにする
        setEditTextFocusFalse();
        // キーボード出現状態で機種を選択した場合はキーボードを閉じてフォーカスを外す
        itemSelectFocusOut();
        // 内部ストレージ関係
        setValue();
        // タッチイベント設定
        setTouchEvent();
    }

    public void setViewsArrayList() {

        // ゲーム数関係
        EditText[] eGames = {eStartGames, eTotalGames, eIndividualGames};
        eCounterEditGames = new ArrayList<>();
        eCounterEditGames.addAll(Arrays.asList(eGames));

        // 小役関係
        EditText[] eRolls = {eSingleBig, eCherryBig, eTotalBig, eSingleReg, eCherryReg, eTotalReg, eCherry, eGrape, eTotalBonus};
        eCounterEditRolls = new ArrayList<>();
        eCounterEditRolls.addAll(Arrays.asList(eRolls));

        // 確率関係
        TextView[] tProbability = {tSingleBigProbability, tCherryBigProbability, tTotalBigProbability,
                tSingleRegProbability, tCherryRegProbability, tTotalRegProbability,
                tCherryProbability, tGrapeProbability, tTotalBonusProbability};
        eCounterTextProbability = new ArrayList<>();
        eCounterTextProbability.addAll(Arrays.asList(tProbability));

    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (!visible) {
            //キーボードが非表示になったことを検知した時
            mainLayout.requestFocus();
        }
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

                if (judgeEditorMode) {
                    setEditTextFocusFalse();
                    judgeEditorMode = false;
                } else {
                    setEditTextFocusTrue();
                    judgeEditorMode = true;
                }
                focusOut();
                return true;

            case R.id.item2: // 加減算切り替えモード

                if (judgePlusMinus) {
                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(), Color.WHITE, Typeface.DEFAULT);
                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(), Color.WHITE, Typeface.DEFAULT);
                    judgePlusMinus = false;
                } else {
                    if (judgeSkeleton) {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.stealth_release_tittle))
                                .setMessage(getString(R.string.stealth_release_message))
                                .setPositiveButton(getString(R.string.release), (dialogInterface, i) -> {
                                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(), Color.RED, Typeface.DEFAULT_BOLD);
                                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(), Color.RED, Typeface.DEFAULT_BOLD);
                                    judgeSkeleton = false;
                                    judgePlusMinus = true;
                                    Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.stealth_toast), Toast.LENGTH_LONG);
                                    toast.show();
                                })
                                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> judgePlusMinus = false)
                                .show();
                    } else {
                        ViewItems.setEditTextColor(ViewItems.getCounterTextItems(), Color.RED, Typeface.DEFAULT_BOLD);
                        ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(), Color.RED, Typeface.DEFAULT_BOLD);
                        judgePlusMinus = true;
                    }
                }
                focusOut();
                return true;

            case R.id.item3: // カウンター非表示モード

                if (judgeSkeleton) {
                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(), Color.WHITE, Typeface.DEFAULT);
                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(), Color.WHITE, Typeface.DEFAULT);
                    judgeSkeleton = false;
                } else {
                    if (judgePlusMinus) {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.plus_minus_release_tittle))
                                .setMessage(getString(R.string.plus_minus_release_message))
                                .setPositiveButton(getString(R.string.release), (dialogInterface, i) -> {
                                    int skeleton = getResources().getColor(R.color.skeleton, getTheme());
                                    ViewItems.setEditTextColor(ViewItems.getCounterTextItems(), skeleton, Typeface.DEFAULT);
                                    ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(), skeleton, Typeface.DEFAULT);
                                    judgeSkeleton = true;
                                    judgePlusMinus = false;
                                    Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.plus_minus_toast), Toast.LENGTH_LONG);
                                    toast.show();
                                })
                                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> judgeSkeleton = false)
                                .show();
                    } else {
                        int skeleton = getResources().getColor(R.color.skeleton, getTheme());
                        ViewItems.setEditTextColor(ViewItems.getCounterTextItems(), skeleton, Typeface.DEFAULT);
                        ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(), skeleton, Typeface.DEFAULT);
                        judgeSkeleton = true;
                    }
                }
                focusOut();
                return true;

            case R.id.item4: // バイブ機能ON/Off
                if (judgeVibrator) {
                    judgeVibrator = false;
                    Toast toast = Toast.makeText(this, getString(R.string.vib_off), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    judgeVibrator = true;
                    Toast toast = Toast.makeText(this, getString(R.string.vib_on), Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case R.id.item5: // フラッシュON/Off
                if (flashJudge) {
                    flashJudge = false;
                    Toast toast = Toast.makeText(this, getString(R.string.flash_off), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    flashJudge = true;
                    Toast toast = Toast.makeText(this, getString(R.string.flash_on), Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;

            case R.id.item6: // カウンター初期化
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.counter_init_tittle))
                        .setMessage(getString(R.string.counter_init_message))
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {

                            // 各EditTextの値を初期化
                            EditText[] eSet_Counter = {eStartGames, eTotalGames, eSingleBig, eCherryBig, eSingleReg, eCherryReg, eCherry, eGrape};
                            for (EditText e : eSet_Counter) {
                                e.setText("0");
                            }

                            // ストレージの初期化
                            initSharedPreferences();

                            ViewItems.setEditTextColor(ViewItems.getCounterTextItems(), Color.WHITE, Typeface.DEFAULT);
                            ViewItems.setTextViewColor(ViewItems.getProbabilityTextItems(), Color.WHITE, Typeface.DEFAULT);
                            setEditTextFocusFalse();

                            judgePlusMinus = false;
                            judgeEditorMode = false;
                            judgeSkeleton = false;

                            Toast toast = Toast.makeText(MainCounterActivity.this, getString(R.string.counter_init_toast), Toast.LENGTH_SHORT);
                            toast.show();
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
                focusOut();
                return true;

            case R.id.item7: // データ保存
                // 個人ゲーム数が0ゲームの状態で登録ボタンを押した際の処理
                int checkedIndividualGames = Integer.parseInt(eIndividualGames.getText().toString());
                if (checkedIndividualGames != 0) {

                    // R04.11.19 下記コードでヌルポ発生確認。valueOfで変換することで強制的に文字列nullに変換してやる
                    // if (!mainApplication.getStore001().equals("null")) {

                    if (!String.valueOf(mainApplication.getStore001()).equals("null")) {
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
    public void registerDialog() {

        // ダイアログを定義
        Dialog registerDialog = new Dialog(this);
        // カスタム用のレイアウトをセット
        registerDialog.setContentView(R.layout.main02_counter04_custom_dialog);
        // ダイアログのレイアウトをタッチするとフォーカスが外れる
        dialogLayout = registerDialog.findViewById(R.id.RegisterLayout);
        dialogLayout.setOnTouchListener((v, event) -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialogLayout.getWindowToken(), 0);
            dialogLayout.requestFocus();
            return false;
        });

        // 日付表示用のEditTextにリスナーを登録
        registerDialog.findViewById(R.id.DateEditText).setOnClickListener(view -> {
            // Calendarインスタンスを取得
            final Calendar date = Calendar.getInstance();

            // DatePickerDialogインスタンスを取得
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainCounterActivity.this,
                    (view1, year, month, dayOfMonth) -> {
                        // 選択した日付を取得して日付表示用のEditTextにセット
                        eDate = registerDialog.findViewById(R.id.DateEditText);
                        eDate.setText(String.format("%d / %02d / %02d", year, month + 1, dayOfMonth));
                        eDate.setGravity(Gravity.CENTER);

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
            inputMethodManager.hideSoftInputFromWindow(dialogLayout.getWindowToken(), 0);
            dialogLayout.requestFocus();
        });

        // 店舗表示用のスピナーと店舗名のリストを準備
        Spinner sStore = registerDialog.findViewById(R.id.StoreSpinner);
        List<String> sStore_Items = new ArrayList<>();

        // 20店舗分の登録店舗を(nullじゃなかったら)リストにセット
        String[] Storage_Store_Items = CommonFeature.getStoreItems(mainApplication);
        for (String Item : Storage_Store_Items) {
            if (!Item.equals("null")) {
                sStore_Items.add(Item);
            }
        }

        // アダプターを介して登録店舗一覧リストをセット
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, R.layout.main02_counter05_store_spinner, sStore_Items);
        storeAdapter.setDropDownViewResource(R.layout.main02_counter06_store_spinner_dropdown);
        sStore.setAdapter(storeAdapter);

        // TextWatcherを設定して0頭を回避する
        // なお、台番号については0頭は許容しておく
        eMedal = registerDialog.findViewById(R.id.DifferenceNumber);
        eMedal.addTextChangedListener(this);

        // キーボード確定ボタンを押すとフォーカスが外れる
        eMedal.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(dialogLayout.getWindowToken(), 0);
                dialogLayout.requestFocus();
            }
            return false;
        });

        // 登録ボタンにリスナー登録
        registerDialog.findViewById(R.id.RegisterButton).setOnClickListener(view -> {

            // ユーザID
            dbUserId = mainApplication.getUserId();
            // 店舗名
            dbStoreName = (String) sStore.getSelectedItem();
            String medalStr = eMedal.getText().toString();
            // ここでもう1回findViewすることでクラッシュ回避(ダイアログ出現後にやらないとNULLになるらしい)
            eDate = registerDialog.findViewById(R.id.DateEditText);

            // 日付入力済なら登録処理
            String checkedDate = eDate.getText().toString();
            if (StringUtils.isNotEmpty(checkedDate)) {

                dbMedal = 0;
                if (StringUtils.isNotEmpty(medalStr)) {
                    dbMedal = Integer.parseInt(medalStr);
                }
                if (!(dbMedal == null || dbMedal == 0)) {
                    CheckBox checkBox = registerDialog.findViewById(R.id.checkBox);
                    if (checkBox.isChecked()) {
                        dbMedal = -dbMedal;
                    }
                }

                // 機種名取得
                dbMachineName = machines.getNowMachineName(counterDate.getInt("MachinePosition", 0));

                // 各種カウンター値を取得
                dbStartGames = Integer.parseInt(counterDate.getString("StartGames", "0"));
                dbTotalGames = Integer.parseInt(counterDate.getString("TotalGames", "0"));
                dbSingleBig = Integer.parseInt(counterDate.getString("SingleBig", "0"));
                dbCherryBig = Integer.parseInt(counterDate.getString("CherryBig", "0"));
                dbSingleReg = Integer.parseInt(counterDate.getString("SingleReg", "0"));
                dbCherryReg = Integer.parseInt(counterDate.getString("CherryReg", "0"));
                dbCherry = Integer.parseInt(counterDate.getString("Cherry", "0"));
                dbGrape = Integer.parseInt(counterDate.getString("Grape", "0"));

                // ゾロ目
                dbSpecialDay = "";
                if (dbOperationDay.equals("11") || dbOperationDay.equals("22")) {
                    dbSpecialDay = "1";
                }
                // 月と日が同じ
                if (dbOperationMonth.equals(dbOperationDay)) {
                    dbSpecialDay = "2";
                }
                // 両方
                if (dbOperationMonth.equals("11") && dbOperationDay.equals("11")) {
                    dbSpecialDay = "3";
                }

                // 台番号および台番号末尾取得
                eTableNumber = registerDialog.findViewById(R.id.MachineNumber);
                dbTableNumber = eTableNumber.getText().toString();
                dbSingleNumber = "";
                if (StringUtils.isNotEmpty(dbTableNumber)) {
                    if (!Integer.valueOf(dbTableNumber).toString().equals("0")) {
                        dbSingleNumber = dbTableNumber.substring(dbTableNumber.length() - 1);
                    }
                }

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
                                    "SPECIAL_DAY," +
                                    "WEEK_ID," +
                                    "DAY_OF_WEEK_IN_MONTH," +
                                    "DIFFERENCE_NUMBER," +
                                    "MACHINE_NAME," +
                                    "TABLE_NUMBER," +
                                    "TAIL_NUMBER," +
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
                                    "'" + dbSpecialDay + "'," +
                                    "'" + dbWeekId + "'," +
                                    "'" + dbDayOfWeek_in_Month + "'," +
                                    "'" + dbMedal + "'," +
                                    "'" + dbMachineName + "'," +
                                    "'" + dbTableNumber + "'," +
                                    "'" + dbSingleNumber + "'," +
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

                } catch (Exception ex) {
                    Log.e("MemoPad", ex.toString());
                } finally {
                    db.close();
                }

                registerDialog.dismiss();
                focusOut();

                // アプリ内レビュー
                startReviewInfo();

            } else {
                Toast.makeText(MainCounterActivity.this, getString(R.string.select_date_toast), Toast.LENGTH_LONG).show();
            }
        });

        // ダイアログを表示
        registerDialog.show();
    }

    public void initSharedPreferences() {
        editor.putString("TotalGames", "0");
        editor.putString("StartGames", "0");
        editor.putString("SingleBig", "0");
        editor.putString("CherryBig", "0");
        editor.putString("SingleReg", "0");
        editor.putString("CherryReg", "0");
        editor.putString("Cherry", "0");
        editor.putString("Grape", "0");
        editor.apply();
    }

    public void pleaseAddStore() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_store_tittle))
                .setMessage(getString(R.string.add_store_message))
                .setPositiveButton(getString(R.string.go_add_store), (dialog, which) -> {
                    Intent intent = new Intent(getApplication(), MainManagementStore.class);
                    startActivity(intent);
                })
                .show();
    }

    public void vibrator() {
        if (judgeVibrator) {
            vibrationEffect = VibrationEffect.createOneShot(300, -1);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(vibrationEffect);
        }
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

    public void setFindViewByID() {
        eTotalGames = findViewById(R.id.eTotalGames);
        eStartGames = findViewById(R.id.eStartGames);
        eIndividualGames = findViewById(R.id.eIndividualGames);
        eSingleBig = findViewById(R.id.eSingleBig);
        eCherryBig = findViewById(R.id.eCherryBig);
        eTotalBig = findViewById(R.id.eTotalBig);
        eSingleReg = findViewById(R.id.eSingleReg);
        eCherryReg = findViewById(R.id.eCherryReg);
        eTotalReg = findViewById(R.id.eTotalReg);
        eCherry = findViewById(R.id.eCherry);
        eGrape = findViewById(R.id.eGrape);
        eTotalBonus = findViewById(R.id.eTotalBonus);
        tSingleBigProbability = findViewById(R.id.tSingleBigProbability);
        tCherryBigProbability = findViewById(R.id.tCherryBigProbability);
        tTotalBigProbability = findViewById(R.id.tTotalBigProbability);
        tSingleRegProbability = findViewById(R.id.tSingleRegProbability);
        tCherryRegProbability = findViewById(R.id.tCherryRegProbability);
        tTotalRegProbability = findViewById(R.id.tTotalRegProbability);
        tCherryProbability = findViewById(R.id.tCherryProbability);
        tGrapeProbability = findViewById(R.id.tGrapeProbability);
        tTotalBonusProbability = findViewById(R.id.tTotalBonusProbability);
        mainLayout = findViewById(R.id.counter);
        scrollLayout = findViewById(R.id.TouchCounter);
        bTotalBig = findViewById(R.id.bTotalBig);
        bTotalReg = findViewById(R.id.bTotalReg);
        bTotalBonus = findViewById(R.id.bTotalBonus);
        sJuggler = findViewById(R.id.sJuggler);
    }

    //ゲーム数関係とカウンター関係へCounterWatcherを設定
    public void setTextWatcher() {

        // ゲーム数関係
        EditText[] eGames = {eStartGames, eTotalGames, eIndividualGames};
        for (EditText e : eGames) {
            e.addTextChangedListener(new MainCounterWatcher(e, editor));
        }
        // 役物関係
        EditText[] eRolls = {eSingleBig, eCherryBig, eTotalBig, eSingleReg, eCherryReg, eTotalReg, eCherry, eGrape, eTotalBonus};
        for (EditText e : eRolls) {
            e.addTextChangedListener(new MainCounterWatcher(e, editor));
        }
    }

    public void setJuggler() {
        List<String> jugglerList = machines.getNowJugglerList();
        ArrayAdapter<String> jugglerAdapter = new ArrayAdapter<>(this, R.layout.main02_counter02_juggler_spinner, jugglerList);
        jugglerAdapter.setDropDownViewResource(R.layout.main02_counter03_juggler_spinner_dropdown);
        sJuggler.setAdapter(jugglerAdapter);
        sJuggler.setSelection(counterDate.getInt("MachinePosition", 0));
    }

    public void setEditTextFocusTrue() {
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.c_maincounteractivity01_gamesediter, null);
        ViewItems.setEditTextFocus(ViewItems.getCounterEditTextItems(), true, true, background);
    }

    public void setEditTextFocusFalse() {
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.c_maincounteractivity02_focusfalse, null);
        ViewItems.setEditTextFocus(ViewItems.getCounterEditTextItems(), false, false, background);
    }

    private void setValue() {

        // SharedPreferencesから値を取得してセット
        // R04.09.23変更
        eTotalGames.setText(counterDate.getString("TotalGames", "0"));
        eStartGames.setText(counterDate.getString("StartGames", "0"));
        eSingleBig.setText(counterDate.getString("SingleBig", "0"));
        eCherryBig.setText(counterDate.getString("CherryBig", "0"));
        eSingleReg.setText(counterDate.getString("SingleReg", "0"));
        eCherryReg.setText(counterDate.getString("CherryReg", "0"));
        eCherry.setText(counterDate.getString("Cherry", "0"));
        eGrape.setText(counterDate.getString("Grape", "0"));
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchEvent() {
        scrollLayout.setOnClickListener(v -> focusOut());
        bTotalBig.setOnClickListener(v -> MainCounterActivity.this.focusOut());
        bTotalReg.setOnClickListener(v -> MainCounterActivity.this.focusOut());
        bTotalBonus.setOnClickListener(v -> MainCounterActivity.this.focusOut());
    }

    //***************************************************************************************************************************
    // フォーカス関係の処理
    //***************************************************************************************************************************

    public void itemSelectFocusOut() {
        sJuggler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 保存処理
                editor.putInt("MachinePosition", position);
                editor.apply();
                focusOut();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void focusOut() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        mainLayout.requestFocus();
    }

    //***************************************************************************************************************************
    // ボタンの制御
    //***************************************************************************************************************************

    public void mSingleBigButton(View view) {
        pushButton(eSingleBig, R.id.eSingleBig, 9999);
        vibrator();
    }

    public void mCherryBigButton(View view) {
        pushButton(eCherryBig, R.id.eCherryBig, 9999);
        vibrator();
    }

    public void mSingleRegButton(View view) {
        pushButton(eSingleReg, R.id.eSingleReg, 9999);
        vibrator();
    }

    public void mCherryRegButton(View view) {
        pushButton(eCherryReg, R.id.eCherryReg, 9999);
        vibrator();
    }

    public void mCherryButton(View view) {
        pushButton(eCherry, R.id.eCherry, 99999);
        vibrator();
    }

    public void mGrapeButton(View view) {
        pushButton(eGrape, R.id.eGrape, 999999);
        vibrator();
    }

    public void pushButton(EditText editText, int id, int limit) {
        View v = findViewById(R.id.counter);
        ColorButton colorButton = new ColorButton();
        String text = editText.getText().toString();
        int textValue = 0;
        if (StringUtils.isNotEmpty(text)) {
            textValue = Integer.parseInt(text);
        }

        // プラス状態の場合
        if (judgePlusMinus) {
            if (StringUtils.isNotEmpty(text)) {
                if (textValue > 0) {
                    textValue--;
                    colorButton.setFlash(v, id, flashJudge);
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
                    colorButton.setFlash(v, id, flashJudge);
                    editText.setText(String.valueOf(textValue));
                } else {
                    Toast toast = Toast.makeText(this, getString(R.string.upper_limit_count_toast), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                colorButton.setFlash(v, id, flashJudge);
                editText.setText(String.valueOf(1));
            }
        }
        focusOut();
    }

    private void startReviewInfo() {
        ReviewManager reviewManager = ReviewManagerFactory.create(getApplicationContext());
        Task<ReviewInfo> manager = reviewManager.requestReviewFlow();
        manager.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                if (reviewInfo != null) {
                    Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                    flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            Toast.makeText(MainCounterActivity.this, getString(R.string.register_toast), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(MainCounterActivity.this, getString(R.string.register_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }


}




