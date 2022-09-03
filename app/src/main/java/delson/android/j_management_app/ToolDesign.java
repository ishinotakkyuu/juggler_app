package delson.android.j_management_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ToolDesign extends AppCompatActivity implements TextWatcher, KeyboardVisibility.OnKeyboardVisibilityListener {

    // 件数関係
    TextView tCount;
    final String SQL = "select DESIGN_09 from DESIGN;";
    int designCount = 0;

    // 台番号関係
    EditText eNumber;
    EditText eSkipNumber_01, eSkipNumber_02;
    String skipNumber_01 = "";
    String skipNumber_02 = "";

    // 出目領域
    EditText eDesign_01, eDesign_02, eDesign_03, eDesign_04, eDesign_05, eDesign_06, eDesign_07, eDesign_08, eDesign_09;
    List<EditText> eDesigns;

    // モード表示
    TextView tHorizon, tNineBox, tNumberCount;

    // 各種フラグ
    boolean horizon = true;
    boolean nineBox = true;
    boolean plus = true;

    // フォーカス
    Activity activity;
    ConstraintLayout mainLayout;
    InputMethodManager inputMethodManager;

    // バイブ機能
    Vibrator vibrator;
    VibrationEffect vibrationEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool03_design01);

        // フォーカス関係のセット
        mainLayout = findViewById(R.id.design_layout);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 戻るボタン等でキーボードを非表示にされた時のフォーカス対応
        activity = this;
        KeyboardVisibility kv = new KeyboardVisibility(activity);
        kv.setKeyboardVisibilityListener(this);

        // IDセット
        setFindViewById();

        // 残り記録件数の表示(現時点で最大５０件)
        Context context = getApplicationContext();
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        List<String> count = new ArrayList<>();
        Cursor cursor = db.rawQuery(SQL, null);
        while (cursor.moveToNext()) {
            String design_09 = cursor.getString(0);
            count.add(design_09);
        }
        // 現時点での件数を格納
        designCount = count.size();
        tCount.setText(getString(R.string.save_count, count.size()));
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (!visible) {
            //キーボードが非表示になったことを検知した時
            mainLayout.requestFocus();
        }
    }

    // メニューのセット
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.design_menu, menu);
        return true;
    }

    // 各メニュー選択時の処理振り分け
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // 確認画面へ遷移
            case R.id.design_list:
                Intent intent = new Intent(getApplicationContext(), ToolDesignList.class);
                startActivity(intent);
                break;

            // カーソル移動先変更
            case R.id.vh:
                new AlertDialog.Builder(this)
                        .setTitle("カーソル移動方向の変更")
                        .setMessage("縦方向または横方向に変更します。\n現在セットされている出目はリセットされます。")
                        .setPositiveButton("変更", (dialog, which) -> {
                            // フラグ反転
                            horizon = !horizon;
                            // 出目領域を初期化
                            for (int i = 0; i < eDesigns.size(); i++) {
                                if (StringUtils.isNotEmpty(eDesigns.get(i).getText())) {
                                    eDesigns.get(i).setText("");
                                }
                            }
                            // モードを表示
                            if (horizon) {
                                tHorizon.setText("");
                            } else {
                                tHorizon.setText("縦移動");
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                break;

            // 下段のみ
            case R.id.down_line:
                new AlertDialog.Builder(this)
                        .setTitle("保存領域の変更")
                        .setMessage("保存する領域を９つまたは下段の３つのみに変更します。\n現在セットされている出目はリセットされます。")
                        .setPositiveButton("変更", (dialog, which) -> {
                            // フラグ反転
                            nineBox = !nineBox;
                            // 出目領域を初期化
                            for (int i = 0; i < eDesigns.size(); i++) {
                                if (StringUtils.isNotEmpty(eDesigns.get(i).getText())) {
                                    eDesigns.get(i).setText("");
                                }
                                // モードを表示
                                if (nineBox) {
                                    tNineBox.setText("");
                                } else {
                                    tNineBox.setText("下段のみ保存");
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                break;

            // 台番号の自動加減算
            case R.id.number_change:
                new AlertDialog.Builder(this)
                        .setTitle("台番号の加減算変更")
                        .setMessage("台番号入力時の自動加算または自動減算を変更します。")
                        .setPositiveButton("変更", (dialog, which) -> {
                            // フラグ反転
                            plus = !plus;
                            // モードを表示
                            if (plus) {
                                tNumberCount.setText("");
                            } else {
                                tNumberCount.setText("減算モード");
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                break;

            // 飛び番登録
            case R.id.number_skip:
                // ダイアログを定義
                Dialog registerDialog = new Dialog(this);

                // カスタム用のレイアウトをセット
                registerDialog.setContentView(R.layout.tool03_design02_custom_dialog);

                // IDをセット
                eSkipNumber_01 = registerDialog.findViewById(R.id.EditSkipNumber01);
                eSkipNumber_02 = registerDialog.findViewById(R.id.EditSkipNumber02);

                // 飛び番をセット
                eSkipNumber_01.setText(skipNumber_01);
                eSkipNumber_02.setText(skipNumber_02);

                // 登録ボタンにリスナー登録
                registerDialog.findViewById(R.id.SkipNumberRegisterButton).setOnClickListener(view -> {

                    // 飛び番の取得
                    skipNumber_01 = eSkipNumber_01.getText().toString();
                    skipNumber_02 = eSkipNumber_02.getText().toString();

                    // ダイアログを閉じる
                    registerDialog.dismiss();
                });
                // ダイアログを表示
                registerDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (StringUtils.isNotEmpty(s.toString())) {

            // 店舗名取得(Ver3.0.0時点では実装無し)
            String storeName = "";

            // 台番号取得
            String tableNumber = "";
            if (StringUtils.isNotEmpty(eNumber.getText())) {
                tableNumber = String.valueOf(Integer.parseInt(eNumber.getText().toString()));
            }

            // 現在時刻を取得
            Date now = new Date();
            SimpleDateFormat dFormat = new SimpleDateFormat("MM月dd日HH時mm分");
            String saveDate = dFormat.format(now);

            // 出目を取得
            String design_01 = eDesign_01.getText().toString();
            String design_02 = eDesign_02.getText().toString();
            String design_03 = eDesign_03.getText().toString();
            String design_04 = eDesign_04.getText().toString();
            String design_05 = eDesign_05.getText().toString();
            String design_06 = eDesign_06.getText().toString();
            String design_07 = eDesign_07.getText().toString();
            String design_08 = eDesign_08.getText().toString();
            String design_09 = eDesign_09.getText().toString();

            //　データベースへの登録処理
            Context context = getApplicationContext();
            DatabaseHelper helper = new DatabaseHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();

            try {
                String sql =
                        "insert into DESIGN (" +
                                "STORE_NAME," +
                                "TABLE_NUMBER," +
                                "SAVE_DATE," +
                                "DESIGN_01," +
                                "DESIGN_02," +
                                "DESIGN_03," +
                                "DESIGN_04," +
                                "DESIGN_05," +
                                "DESIGN_06," +
                                "DESIGN_07," +
                                "DESIGN_08," +
                                "DESIGN_09" +
                                ") " +
                                "values(" +
                                "'" + storeName + "'," +
                                "'" + tableNumber + "'," +
                                "'" + saveDate + "'," +
                                "'" + design_01 + "'," +
                                "'" + design_02 + "'," +
                                "'" + design_03 + "'," +
                                "'" + design_04 + "'," +
                                "'" + design_05 + "'," +
                                "'" + design_06 + "'," +
                                "'" + design_07 + "'," +
                                "'" + design_08 + "'," +
                                "'" + design_09 + "'" +
                                ")";
                SQLiteStatement stmt = db.compileStatement(sql);
                stmt.executeInsert();

            } catch (Exception ex) {
                Log.e("MemoPad", ex.toString());
            } finally {
                // 残り記録可能件数の更新後、DBを閉じる
                List<String> count = new ArrayList<>();
                Cursor cursor = db.rawQuery(SQL, null);
                while (cursor.moveToNext()) {
                    String design = cursor.getString(0);
                    count.add(design);
                }
                // 上限値の更新
                designCount = count.size();
                tCount.setText(getString(R.string.save_count, count.size()));
                db.close();
            }

            // 出目保存⇒台番号入力⇒出目入力続行、とすると、せっかく入力した台番号が+1または-1されてしまい、
            // ユーザーの意図した処理とならない可能性があるため、右下に出目が格納されている場合は台番号の入力を不可にする。
            eNumber.setFocusableInTouchMode(false);
            setNumberToast();

            Toast toast = Toast.makeText(ToolDesign.this, "出目を保存しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void setFindViewById() {

        tCount = findViewById(R.id.design_count);

        eNumber = findViewById(R.id.design_number);
        eDesign_01 = findViewById(R.id.design_01);
        eDesign_02 = findViewById(R.id.design_02);
        eDesign_03 = findViewById(R.id.design_03);
        eDesign_04 = findViewById(R.id.design_04);
        eDesign_05 = findViewById(R.id.design_05);
        eDesign_06 = findViewById(R.id.design_06);
        eDesign_07 = findViewById(R.id.design_07);
        eDesign_08 = findViewById(R.id.design_08);
        eDesign_09 = findViewById(R.id.design_09);

        eDesign_09.addTextChangedListener(this);

        eDesigns = new ArrayList<>();
        EditText[] designs = {eDesign_01, eDesign_02, eDesign_03, eDesign_04, eDesign_05, eDesign_06, eDesign_07, eDesign_08, eDesign_09};
        eDesigns.addAll(Arrays.asList(designs));

        tHorizon = findViewById(R.id.mode_horizon);
        tNineBox = findViewById(R.id.mode_ninebox);
        tNumberCount = findViewById(R.id.mode_numbercount);

    }

    public void setDesignText(String design) {

        // 保存領域が９つか下段のみの３つかを判定
        if (nineBox) {
            // 横移動か縦移動かを判定
            if (horizon) {
                // 横移動の場合

                // 右下の領域が空欄でなかった場合は全領域を初期化して右下から開始できるようにする
                continueDesign();

                // 出目をセット
                for (int i = 0; i < eDesigns.size(); i++) {
                    if (eDesigns.get(i).getText().toString().isEmpty()) {
                        eDesigns.get(i).setText(design);
                        return;
                    }
                }
            } else {
                // 縦移動の場合

                // 右下の領域が空欄でなかった場合は全領域を初期化して右下から開始できるようにする
                continueDesign();

                List<EditText> verticalDesigns = new ArrayList<>();
                EditText[] designs = {eDesign_01, eDesign_04, eDesign_07, eDesign_02, eDesign_05, eDesign_08, eDesign_03, eDesign_06, eDesign_09};
                verticalDesigns.addAll(Arrays.asList(designs));
                for (int i = 0; i < verticalDesigns.size(); i++) {
                    if (verticalDesigns.get(i).getText().toString().isEmpty()) {
                        verticalDesigns.get(i).setText(design);
                        return;
                    }
                }
            }

        } else {

            // 台番号入力不可のトーストが表示されないようにする
            notNumberToast();
            // 右下の領域が空欄でなかった場合は全領域を初期化して右下から開始できるようにする
            continueDesign();

            // 下段のみの場合
            for (int i = 6; i < 9; i++) {
                if (eDesigns.get(i).getText().toString().isEmpty()) {
                    eDesigns.get(i).setText(design);
                    return;
                }
            }
        }
    }

    public void continueDesign() {
        // 右下の領域が空欄でなかった場合は全領域を初期化して左上から開始できるようにする
        if (StringUtils.isNotEmpty(eDesign_09.getText())) {
            for (int i = 0; i < eDesigns.size(); i++) {
                if (StringUtils.isNotEmpty(eDesigns.get(i).getText())) {
                    eDesigns.get(i).setText("");
                }
            }
            // 台番号入力を可能にする
            eNumber.setFocusableInTouchMode(true);
            // 台番号入力不可のトーストが表示されないようにする
            notNumberToast();
            // 台番号が入力されていたら自動加減算処理を行う
            numberChange();
        }
    }

    public void numberChange() {

        String tableNumber = eNumber.getText().toString();

        if (StringUtils.isNotEmpty(tableNumber)) {

            // 加減算を行う
            if (plus) {
                tableNumber = String.valueOf(Integer.parseInt(tableNumber) + 1);
            } else {
                tableNumber = String.valueOf(Integer.parseInt(tableNumber) - 1);
            }

            // 飛び番判定１回目
            String checkNumber = tableNumber.substring(tableNumber.length() - 1);
            if (checkNumber.equals(skipNumber_01) || checkNumber.equals(skipNumber_02)) {
                if (plus) {
                    tableNumber = String.valueOf(Integer.parseInt(tableNumber) + 1);
                } else {
                    tableNumber = String.valueOf(Integer.parseInt(tableNumber) - 1);
                }
            }

            // 飛び番判定２回目
            checkNumber = tableNumber.substring(tableNumber.length() - 1);
            if (checkNumber.equals(skipNumber_01) || checkNumber.equals(skipNumber_02)) {
                if (plus) {
                    tableNumber = String.valueOf(Integer.parseInt(tableNumber) + 1);
                } else {
                    tableNumber = String.valueOf(Integer.parseInt(tableNumber) - 1);
                }
            }

            // 上限値および下限値判定をしたうえで最終的な台番号をセット
            int border = Integer.parseInt(tableNumber);
            if (0 <= border && border <= 9999) {
                eNumber.setText(tableNumber);
            } else if (border > 9999) {
                eNumber.setText("");
                Toast toast = Toast.makeText(ToolDesign.this, "台番号が上限を越えました", Toast.LENGTH_LONG);
                toast.show();
            } else {
                eNumber.setText("");
                Toast toast = Toast.makeText(ToolDesign.this, "台番号が０未満になりました", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setNumberToast() {
        eNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 画面を押した時に発火
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast toast = Toast.makeText(ToolDesign.this, "保存領域(右下)に出目がある場合は台番号が入力できません", Toast.LENGTH_LONG);
                    toast.show();
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void notNumberToast() {
        eNumber.setOnTouchListener(null);
    }

    // ボタン処理
    public void sevenButton(View view) {
        if (designCount < 50) {
            setDesignText("７");
            vibrator();
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void barButton(View view) {
        if (designCount < 50) {
            setDesignText("BAR");
            vibrator();
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void bellButton(View view) {
        if (designCount < 50) {
            setDesignText("ベル");
            vibrator();
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void pierrotButton(View view) {
        if (designCount < 50) {
            setDesignText("ピエロ");
            vibrator();
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void grapeButton(View view) {
        if (designCount < 50) {
            setDesignText("ぶどう");
            vibrator();
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void cherryButton(View view) {
        if (designCount < 50) {
            setDesignText("チェリー");
            vibrator();
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void replayButton(View view) {
        if (designCount < 50) {
            setDesignText("リプレイ");
            vibrator();
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void backButton(View view) {

        if (designCount < 50) {

            // 右下の領域が削除されたら台番号の自動加減算を行う
            if (StringUtils.isNotEmpty(eDesign_09.getText())) {
                // 空白をセット
                eDesign_09.setText("");
                // 台番号入力を可能にする
                eNumber.setFocusableInTouchMode(true);
                // 台番号入力不可のトーストが表示されないようにする
                notNumberToast();
                // 台番号が入力されていたら自動加減算処理を行う
                numberChange();
                return;
            }

            // 右下以外の領域に関する削除処理は縦方向か横方向のモードによって若干動きが異なる
            if (horizon) {
                // 横方向の場合
                EditText[] designs = {eDesign_08, eDesign_07, eDesign_06, eDesign_05, eDesign_04, eDesign_03, eDesign_02, eDesign_01};
                List<EditText> deleteDesigns = new ArrayList<>(Arrays.asList(designs));
                for (int i = 0; i < deleteDesigns.size(); i++) {
                    if (StringUtils.isNotEmpty(deleteDesigns.get(i).getText())) {
                        deleteDesigns.get(i).setText("");
                        return;
                    }
                }
            } else {
                // 縦方向の場合
                EditText[] designs = {eDesign_06, eDesign_03, eDesign_08, eDesign_05, eDesign_02, eDesign_07, eDesign_04, eDesign_01};
                List<EditText> deleteDesigns = new ArrayList<>(Arrays.asList(designs));
                for (int i = 0; i < deleteDesigns.size(); i++) {
                    if (StringUtils.isNotEmpty(deleteDesigns.get(i).getText())) {
                        deleteDesigns.get(i).setText("");
                        return;
                    }
                }
            }
        } else {
            Toast toast = Toast.makeText(ToolDesign.this, "保存上限に達しました", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void vibrator() {
        vibrationEffect = VibrationEffect.createOneShot(300, -1);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(vibrationEffect);
    }

}