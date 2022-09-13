package delson.android.j_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ToolDesignDetail extends AppCompatActivity implements KeyboardVisibility.OnKeyboardVisibilityListener {

    // 渡されてきた情報を受け取る変数
    String catchID, catchNO;

    // 台番号
    EditText eNumber;

    // 出目領域
    EditText eDesign_01, eDesign_02, eDesign_03, eDesign_04, eDesign_05, eDesign_06, eDesign_07, eDesign_08, eDesign_09;
    List<EditText> eDesigns;
    static List<String> strDesigns;

    // ボタン
    Button bSeven, bBar, bBell, bPierrot, bGrape, bCherry, bReplay, bBack;
    Button bNextButton, bBackButton;

    // DBレコード数取得関係
    static List<String> primaryKeys;

    // No.用領域
    TextView tNo;

    // 各種フラグ
    boolean menuFlag = true;
    boolean boxFlag = true;

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
        setContentView(R.layout.tool03_design05_detail);

        // フォーカス関係のセット
        mainLayout = findViewById(R.id.DesignDetail_layout);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 戻るボタン等でキーボードを非表示にされた時のフォーカス対応
        activity = this;
        KeyboardVisibility kv = new KeyboardVisibility(activity);
        kv.setKeyboardVisibilityListener(this);

        // 各ViewのIDセット
        setFindViewById();

        // 個別データ画面から渡されてきたデータを取得
        Intent intent = getIntent();
        // 渡されてきたデータを取り出す
        catchID = intent.getStringExtra("ID");
        catchNO = intent.getStringExtra("NO");

        // DBデータをセット
        Context context = getApplicationContext();
        setDesign(context);
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
        inflater.inflate(R.menu.design_detail_menu, menu);
        return true;
    }

    // フラグ判定でメニュー表示を変更
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (menuFlag) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
        }
        return true;
    }

    // 各メニュー選択時の処理振り分け
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.design_edit:

                String[] Select_List = {"台番号のみ", "出目のみ", "全て編集"};
                // アラートダイアグラムの表示 ⇒ 各選択項目ごとの処理を行う
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setItems(Select_List, (dialog, idx) -> {

                    // フラグを変更してメニューを更新
                    menuFlag = false;
                    invalidateOptionsMenu();

                    switch (idx) {
                        case 0:
                            // 各種ボタンの表示を切り替え
                            bNextButton.setVisibility(View.GONE);
                            bBackButton.setVisibility(View.GONE);
                            tNo.setVisibility(View.GONE);

                            // 台番号領域を編集可能にする
                            eNumber.setFocusable(true);
                            eNumber.setFocusableInTouchMode(true);
                            eNumber.setText("");
                            break;

                        case 1:
                            // 各種ボタンの表示を切り替え
                            Button[] buttons = {bSeven, bBar, bBell, bPierrot, bGrape, bCherry, bReplay, bBack};
                            for (Button b : buttons) {
                                b.setVisibility(View.VISIBLE);
                            }
                            bNextButton.setVisibility(View.GONE);
                            bBackButton.setVisibility(View.GONE);
                            tNo.setVisibility(View.GONE);

                            // 全領域を空白にする
                            for (EditText e : eDesigns) {
                                e.setText("");
                            }
                            break;

                        case 2:
                            // 各種ボタンの表示を切り替え
                            Button[] Buttons = {bSeven, bBar, bBell, bPierrot, bGrape, bCherry, bReplay, bBack};
                            for (Button b : Buttons) {
                                b.setVisibility(View.VISIBLE);
                            }
                            bNextButton.setVisibility(View.GONE);
                            bBackButton.setVisibility(View.GONE);
                            tNo.setVisibility(View.GONE);

                            // 台番号領域を編集可能にする
                            eNumber.setFocusable(true);
                            eNumber.setFocusableInTouchMode(true);
                            eNumber.setText("");

                            // 全領域を空白にする
                            for (EditText e : eDesigns) {
                                e.setText("");
                            }
                            break;
                    }
                });
                alert.show();
                break;

            case R.id.design_edit_cancel:

                focusOut();
                new AlertDialog.Builder(this)
                        .setTitle("編集の中止")
                        .setMessage("編集を中止してよろしいですか？")
                        .setPositiveButton("はい", (dialog, which) -> {

                            // フラグを変更してメニューを更新
                            menuFlag = true;
                            invalidateOptionsMenu();

                            // 各種ボタンの表示を切り替え
                            Button[] Buttons = {bSeven, bBar, bBell, bPierrot, bGrape, bCherry, bReplay, bBack};
                            for (Button b : Buttons) {
                                b.setVisibility(View.GONE);
                            }
                            bNextButton.setVisibility(View.VISIBLE);
                            bBackButton.setVisibility(View.VISIBLE);
                            tNo.setVisibility(View.VISIBLE);

                            // 台番号領域を編集不可にする
                            eNumber.setFocusable(false);
                            eNumber.setFocusableInTouchMode(false);

                            // 編集前の状態に戻す
                            setDesign(getApplicationContext());
                        })
                        .setNegativeButton("いいえ", null)
                        .show();

                break;

            case R.id.design_update:

                focusOut();

                // 右下の領域が空白でなかったらDB更新処理
                if (StringUtils.isNotEmpty(eDesign_09.getText())) {

                    new AlertDialog.Builder(this)
                            .setTitle("データ更新")
                            .setMessage("編集した内容で更新しますか？")
                            .setPositiveButton("はい", (dialog, which) -> {

                                // フラグを変更してメニューを更新
                                menuFlag = true;
                                invalidateOptionsMenu();

                                // 各種ボタンの表示を切り替え
                                Button[] Buttons = {bSeven, bBar, bBell, bPierrot, bGrape, bCherry, bReplay, bBack};
                                for (Button b : Buttons) {
                                    b.setVisibility(View.GONE);
                                }
                                bNextButton.setVisibility(View.VISIBLE);
                                bBackButton.setVisibility(View.VISIBLE);
                                tNo.setVisibility(View.VISIBLE);

                                // 台番号領域を編集不可にする
                                eNumber.setFocusable(false);
                                eNumber.setFocusableInTouchMode(false);

                                // 店舗名取得(Ver3.0.0時点では実装無し)
                                String storeName = "";

                                // 台番号取得
                                String tableNumber = "";
                                if (StringUtils.isNotEmpty(eNumber.getText())) {
                                    tableNumber = String.valueOf(Integer.parseInt(eNumber.getText().toString()));
                                }

                                // 現在時刻を取得
                                Date now = new Date();
                                SimpleDateFormat dFormat = new SimpleDateFormat("HH時mm分ss秒");
                                String saveDate = dFormat.format(now);
                                saveDate = saveDate + "更新";

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

                                // DBを更新する
                                Context context = getApplicationContext();
                                String sql = "UPDATE DESIGN SET " +
                                        "STORE_NAME = '" + storeName + "', " +
                                        "TABLE_NUMBER = '" + tableNumber + "', " +
                                        "SAVE_DATE = '" + saveDate + "', " +
                                        "DESIGN_01 = '" + design_01 + "', " +
                                        "DESIGN_02 = '" + design_02 + "', " +
                                        "DESIGN_03 = '" + design_03 + "', " +
                                        "DESIGN_04 = '" + design_04 + "', " +
                                        "DESIGN_05 = '" + design_05 + "', " +
                                        "DESIGN_06 = '" + design_06 + "', " +
                                        "DESIGN_07 = '" + design_07 + "', " +
                                        "DESIGN_08 = '" + design_08 + "', " +
                                        "DESIGN_09 = '" + design_09 + "' " +
                                        "WHERE ID = '" + catchID + "';";
                                DatabaseResultSet.UpdateOrDelete(context, sql);

                                Toast toast = Toast.makeText(ToolDesignDetail.this, "更新しました", Toast.LENGTH_LONG);
                                toast.show();

                            })
                            .setNegativeButton("いいえ", null)
                            .show();
                } else {
                    Toast toast = Toast.makeText(ToolDesignDetail.this, "最後まで出目を登録してください", Toast.LENGTH_LONG);
                    toast.show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void designChange(String ID, String NO, boolean flag) {

        Context context = getApplicationContext();

        // レコード数を取得(主キーの数で管理)
        primaryKeys = new ArrayList<>();
        DatabaseResultSet.execution("DesignTableCount", context, "select ID from DESIGN;");

        // 以後の処理に使用する各種値を取得
        int index = primaryKeys.indexOf(ID);
        int changeNO = Integer.parseInt(NO);
        int flagCount = primaryKeys.size() - 1;

        // IDが範囲外の場合は処理を抜ける
        if (flag) {
            // 「次へ」ボタン押下時
            index++;
            if (index > flagCount) {
                Toast toast = Toast.makeText(ToolDesignDetail.this, "データがありません", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            changeNO++;
        } else {
            // 「前へ」ボタン押下時
            index--;
            if (index < 0) {
                Toast toast = Toast.makeText(ToolDesignDetail.this, "データがありません", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            changeNO--;
        }

        // IDおよびNOを更新
        catchID = primaryKeys.get(index);
        catchNO = String.valueOf(changeNO);

        // DBデータをセット
        setDesign(context);

    }

    public void setDesign(Context context) {

        strDesigns = new ArrayList<>();
        DatabaseResultSet.execution("ToolDesignDetail", context, "select * from DESIGN where ID = '" + catchID + "';");

        // 取得したデータを台番号および出目領域にセット
        List<EditText> eDesigns = new ArrayList<>();
        EditText[] designs = {eNumber, eDesign_01, eDesign_02, eDesign_03, eDesign_04, eDesign_05, eDesign_06, eDesign_07, eDesign_08, eDesign_09};
        eDesigns.addAll(Arrays.asList(designs));
        for (int i = 0; i < 10; i++) {
            eDesigns.get(i).setText(strDesigns.get(i));
        }

        // No.セット
        tNo.setText("No." + catchNO);

        // 保存領域が９つか３つかでフラグ切り替え
        if (eDesign_01.getText().toString().isEmpty()) {
            boxFlag = false;
        } else {
            boxFlag = true;
        }
    }

    public void setFindViewById() {

        eNumber = findViewById(R.id.DesignDetail_number);
        eDesign_01 = findViewById(R.id.DesignDetail_01);
        eDesign_02 = findViewById(R.id.DesignDetail_02);
        eDesign_03 = findViewById(R.id.DesignDetail_03);
        eDesign_04 = findViewById(R.id.DesignDetail_04);
        eDesign_05 = findViewById(R.id.DesignDetail_05);
        eDesign_06 = findViewById(R.id.DesignDetail_06);
        eDesign_07 = findViewById(R.id.DesignDetail_07);
        eDesign_08 = findViewById(R.id.DesignDetail_08);
        eDesign_09 = findViewById(R.id.DesignDetail_09);

        eDesigns = new ArrayList<>();
        EditText[] designs = {eDesign_01, eDesign_02, eDesign_03, eDesign_04, eDesign_05, eDesign_06, eDesign_07, eDesign_08, eDesign_09};
        eDesigns.addAll(Arrays.asList(designs));

        bSeven = findViewById(R.id.DetailSevenButton);
        bBar = findViewById(R.id.DetailBarButton);
        bBell = findViewById(R.id.DetailBellButton);
        bPierrot = findViewById(R.id.DetailPierrotButton);
        bGrape = findViewById(R.id.DetailGrapeButton);
        bCherry = findViewById(R.id.DetailCherryButton);
        bReplay = findViewById(R.id.DetailReplayButton);
        bBack = findViewById(R.id.DesignDetail_ResetButton);

        bNextButton = findViewById(R.id.NoNextButton);
        bBackButton = findViewById(R.id.NoBackButton);

        tNo = findViewById(R.id.design_No);

    }

    public void setDesignText(String design) {

        focusOut();

        if (boxFlag) {
            // 出目をセット
            for (int i = 0; i < eDesigns.size(); i++) {
                if (eDesigns.get(i).getText().toString().isEmpty()) {
                    eDesigns.get(i).setText(design);
                    return;
                }
            }
        } else {
            // 下段のみの場合
            for (int i = 6; i < 9; i++) {
                if (eDesigns.get(i).getText().toString().isEmpty()) {
                    eDesigns.get(i).setText(design);
                    return;
                }
            }
        }

    }

    public void detailSevenButton(View view) {
        setDesignText("７");
        vibrator();
    }

    public void detailBarButton(View view) {
        setDesignText("BAR");
        vibrator();
    }

    public void detailBellButton(View view) {
        setDesignText("ベル");
        vibrator();
    }

    public void detailPierrotButton(View view) {
        setDesignText("ピエロ");
        vibrator();
    }

    public void detailGrapeButton(View view) {
        setDesignText("ぶどう");
        vibrator();
    }

    public void detailCherryButton(View view) {
        setDesignText("チェリー");
        vibrator();
    }

    public void detailReplayButton(View view) {
        setDesignText("リプレイ");
        vibrator();
    }

    public void detailBackButton(View view) {
        EditText[] designs = {eDesign_09, eDesign_08, eDesign_07, eDesign_06, eDesign_05, eDesign_04, eDesign_03, eDesign_02, eDesign_01};
        List<EditText> deleteDesigns = new ArrayList<>(Arrays.asList(designs));
        for (int i = 0; i < deleteDesigns.size(); i++) {
            if (StringUtils.isNotEmpty(deleteDesigns.get(i).getText())) {
                deleteDesigns.get(i).setText("");
                return;
            }
        }
    }

    public void nextDesignButton(View view) {
        designChange(catchID, catchNO, true);
        vibrator();
    }

    public void backDesignButton(View view) {
        designChange(catchID, catchNO, false);
        vibrator();
    }

    public void vibrator() {
        vibrationEffect = VibrationEffect.createOneShot(300, -1);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(vibrationEffect);
    }

    public void focusOut() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        mainLayout.requestFocus();
    }

}