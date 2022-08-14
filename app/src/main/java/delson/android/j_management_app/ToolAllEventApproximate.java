package delson.android.j_management_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolAllEventApproximate extends AppCompatActivity implements KeyboardVisibility.OnKeyboardVisibilityListener  {

    TextView tTotalGames, tTotalBig, tTotalReg, tAllBonus;
    static List<TextView> tTextViews;

    EditText eBB_001, eBB_002, eBB_003, eBB_004, eBB_005, eBB_006, eBB_007, eBB_008, eBB_009, eBB_010,
            eBB_011, eBB_012, eBB_013, eBB_014, eBB_015, eBB_016, eBB_017, eBB_018, eBB_019, eBB_020,
            eBB_021, eBB_022, eBB_023, eBB_024, eBB_025;
    static List<EditText> eBBs;

    EditText eRB_001, eRB_002, eRB_003, eRB_004, eRB_005, eRB_006, eRB_007, eRB_008, eRB_009, eRB_010,
            eRB_011, eRB_012, eRB_013, eRB_014, eRB_015, eRB_016, eRB_017, eRB_018, eRB_019, eRB_020,
            eRB_021, eRB_022, eRB_023, eRB_024, eRB_025;
    static List<EditText> eRBs;

    EditText ePro_001, ePro_002, ePro_003, ePro_004, ePro_005, ePro_006, ePro_007, ePro_008, ePro_009, ePro_010,
            ePro_011, ePro_012, ePro_013, ePro_014, ePro_015, ePro_016, ePro_017, ePro_018, ePro_019, ePro_020,
            ePro_021, ePro_022, ePro_023, ePro_024, ePro_025;
    static List<EditText> ePro;

    // フォーカス関係
    Activity activity;
    InputMethodManager inputMethodManager;
    ConstraintLayout layout;
    ScrollView scrollView;

    // 値を保持する中間変数
    static int saveTotalGames;
    static int saveTotalBig;
    static int saveTotalReg;
    static int saveAllData;

    // 実際に値を保持する変数
    static int resetTotalGames;
    static int resetTotalBig;
    static int resetTotalReg;
    static int resetAllData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool02_all_event_survey_approximate);

        //アクションバーの非表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // フォーカス対応
        activity = this;
        KeyboardVisibility kv = new KeyboardVisibility(activity);
        kv.setKeyboardVisibilityListener(this);

        // 各種ViewにIDをセット
        setFindViewById();

        // TextWatcherをセット
        setTextWatcher();

        // 値の初期化
        saveTotalGames = 0;
        saveTotalBig = 0;
        saveTotalReg = 0;
        saveAllData = 0;
        resetTotalGames = 0;
        resetTotalBig = 0;
        resetTotalReg = 0;
        resetAllData = 0;
        ToolAllEventApproximate.ToolAllEventApproximateWatcher.totalGames = 0;
        ToolAllEventApproximate.ToolAllEventApproximateWatcher.totalBig = 0;
        ToolAllEventApproximate.ToolAllEventApproximateWatcher.totalReg = 0;
        ToolAllEventApproximate.ToolAllEventApproximateWatcher.allData = 0;

        // 初期値をTextViewにセット
        tTotalGames.setText(getResources().getString(R.string.all_total_games, 0));
        tTotalBig.setText(getResources().getString(R.string.all_total_bb, 0, "1/0.00"));
        tTotalReg.setText(getResources().getString(R.string.all_total_rb, 0, "1/0.00"));
        tAllBonus.setText(getResources().getString(R.string.all_total_bonus, 0, "1/0.00"));
    }

    public void setFindViewById() {

        // TextView関係のID設定
        tTotalGames = findViewById(R.id.ApproximateGames);
        tTotalBig = findViewById(R.id.ApproximateBig);
        tTotalReg = findViewById(R.id.ApproximateReg);
        tAllBonus = findViewById(R.id.ApproximateData);
        // static配列に格納
        tTextViews = new ArrayList<>();
        TextView[] textView = {tTotalGames, tTotalBig, tTotalReg, tAllBonus};
        tTextViews.addAll(Arrays.asList(textView));

        ePro_001 = findViewById(R.id.Pro_01);
        ePro_002 = findViewById(R.id.Pro_02);
        ePro_003 = findViewById(R.id.Pro_03);
        ePro_004 = findViewById(R.id.Pro_04);
        ePro_005 = findViewById(R.id.Pro_05);
        ePro_006 = findViewById(R.id.Pro_06);
        ePro_007 = findViewById(R.id.Pro_07);
        ePro_008 = findViewById(R.id.Pro_08);
        ePro_009 = findViewById(R.id.Pro_09);
        ePro_010 = findViewById(R.id.Pro_10);
        ePro_011 = findViewById(R.id.Pro_11);
        ePro_012 = findViewById(R.id.Pro_12);
        ePro_013 = findViewById(R.id.Pro_13);
        ePro_014 = findViewById(R.id.Pro_14);
        ePro_015 = findViewById(R.id.Pro_15);
        ePro_016 = findViewById(R.id.Pro_16);
        ePro_017 = findViewById(R.id.Pro_17);
        ePro_018 = findViewById(R.id.Pro_18);
        ePro_019 = findViewById(R.id.Pro_19);
        ePro_020 = findViewById(R.id.Pro_20);
        ePro_021 = findViewById(R.id.Pro_21);
        ePro_022 = findViewById(R.id.Pro_22);
        ePro_023 = findViewById(R.id.Pro_23);
        ePro_024 = findViewById(R.id.Pro_24);
        ePro_025 = findViewById(R.id.Pro_25);

        eBB_001 = findViewById(R.id.BB_01);
        eBB_002 = findViewById(R.id.BB_02);
        eBB_003 = findViewById(R.id.BB_03);
        eBB_004 = findViewById(R.id.BB_04);
        eBB_005 = findViewById(R.id.BB_05);
        eBB_006 = findViewById(R.id.BB_06);
        eBB_007 = findViewById(R.id.BB_07);
        eBB_008 = findViewById(R.id.BB_08);
        eBB_009 = findViewById(R.id.BB_09);
        eBB_010 = findViewById(R.id.BB_10);
        eBB_011 = findViewById(R.id.BB_11);
        eBB_012 = findViewById(R.id.BB_12);
        eBB_013 = findViewById(R.id.BB_13);
        eBB_014 = findViewById(R.id.BB_14);
        eBB_015 = findViewById(R.id.BB_15);
        eBB_016 = findViewById(R.id.BB_16);
        eBB_017 = findViewById(R.id.BB_17);
        eBB_018 = findViewById(R.id.BB_18);
        eBB_019 = findViewById(R.id.BB_19);
        eBB_020 = findViewById(R.id.BB_20);
        eBB_021 = findViewById(R.id.BB_21);
        eBB_022 = findViewById(R.id.BB_22);
        eBB_023 = findViewById(R.id.BB_23);
        eBB_024 = findViewById(R.id.BB_24);
        eBB_025 = findViewById(R.id.BB_25);

        eRB_001 = findViewById(R.id.RB_01);
        eRB_002 = findViewById(R.id.RB_02);
        eRB_003 = findViewById(R.id.RB_03);
        eRB_004 = findViewById(R.id.RB_04);
        eRB_005 = findViewById(R.id.RB_05);
        eRB_006 = findViewById(R.id.RB_06);
        eRB_007 = findViewById(R.id.RB_07);
        eRB_008 = findViewById(R.id.RB_08);
        eRB_009 = findViewById(R.id.RB_09);
        eRB_010 = findViewById(R.id.RB_10);
        eRB_011 = findViewById(R.id.RB_11);
        eRB_012 = findViewById(R.id.RB_12);
        eRB_013 = findViewById(R.id.RB_13);
        eRB_014 = findViewById(R.id.RB_14);
        eRB_015 = findViewById(R.id.RB_15);
        eRB_016 = findViewById(R.id.RB_16);
        eRB_017 = findViewById(R.id.RB_17);
        eRB_018 = findViewById(R.id.RB_18);
        eRB_019 = findViewById(R.id.RB_19);
        eRB_020 = findViewById(R.id.RB_20);
        eRB_021 = findViewById(R.id.RB_21);
        eRB_022 = findViewById(R.id.RB_22);
        eRB_023 = findViewById(R.id.RB_23);
        eRB_024 = findViewById(R.id.RB_24);
        eRB_025 = findViewById(R.id.RB_25);

        layout = findViewById(R.id.tool_Approximate_layout);
        scrollView = findViewById(R.id.Approximate_scroll);

    }

    public void setTextWatcher() {

        // ゲーム数関係
        ePro = new ArrayList<>();
        EditText[] edit_01 = {ePro_001, ePro_002, ePro_003, ePro_004, ePro_005, ePro_006, ePro_007, ePro_008, ePro_009, ePro_010,
                ePro_011, ePro_012, ePro_013, ePro_014, ePro_015, ePro_016, ePro_017, ePro_018, ePro_019, ePro_020,
                ePro_021, ePro_022, ePro_023, ePro_024, ePro_025};
        for (EditText e : edit_01) {
            e.addTextChangedListener(new ToolAllEventApproximate.ToolAllEventApproximateWatcher(e, getResources(), getApplicationContext(), scrollView));
            ePro.add(e);
        }

        // Big関係
        eBBs = new ArrayList<>();
        EditText[] edit_02 = {eBB_001, eBB_002, eBB_003, eBB_004, eBB_005, eBB_006, eBB_007, eBB_008, eBB_009, eBB_010,
                eBB_011, eBB_012, eBB_013, eBB_014, eBB_015, eBB_016, eBB_017, eBB_018, eBB_019, eBB_020,
                eBB_021, eBB_022, eBB_023, eBB_024, eBB_025};
        for (EditText e : edit_02) {
            e.addTextChangedListener(new ToolAllEventApproximate.ToolAllEventApproximateWatcher(e, getResources(), getApplicationContext(), scrollView));
            eBBs.add(e);
        }

        // Reg関係
        eRBs = new ArrayList<>();
        EditText[] edit_03 = {eRB_001, eRB_002, eRB_003, eRB_004, eRB_005, eRB_006, eRB_007, eRB_008, eRB_009, eRB_010,
                eRB_011, eRB_012, eRB_013, eRB_014, eRB_015, eRB_016, eRB_017, eRB_018, eRB_019, eRB_020,
                eRB_021, eRB_022, eRB_023, eRB_024, eRB_025};
        for (EditText e : edit_03) {
            e.addTextChangedListener(new ToolAllEventApproximate.ToolAllEventApproximateWatcher(e, getResources(), getApplicationContext(), scrollView));
            eRBs.add(e);
        }
    }

    public void resetButton(View view) {

        new AlertDialog.Builder(this)
                .setTitle("入力データのリセット")
                .setMessage("入力したデータをリセットします。なお累計データは保持されます。")
                .setPositiveButton("リセット", (dialogInterface, i) -> {

                    // 保持しておいた値を移動
                    resetTotalGames = saveTotalGames;
                    // ゲーム数関係のEditTextを空にする。
                    EditText[] edit_01 = {ePro_001, ePro_002, ePro_003, ePro_004, ePro_005, ePro_006, ePro_007, ePro_008, ePro_009, ePro_010,
                            ePro_011, ePro_012, ePro_013, ePro_014, ePro_015, ePro_016, ePro_017, ePro_018, ePro_019, ePro_020,
                            ePro_021, ePro_022, ePro_023, ePro_024, ePro_025};
                    for (EditText e : edit_01) {
                        e.removeTextChangedListener(null);
                        e.setText("");
                        e.addTextChangedListener(new ToolAllEventApproximate.ToolAllEventApproximateWatcher(e, getResources(), getApplicationContext(), scrollView));
                    }

                    // 保持しておいた値を移動
                    resetTotalBig = saveTotalBig;
                    // Big関係
                    EditText[] edit_02 = {eBB_001, eBB_002, eBB_003, eBB_004, eBB_005, eBB_006, eBB_007, eBB_008, eBB_009, eBB_010,
                            eBB_011, eBB_012, eBB_013, eBB_014, eBB_015, eBB_016, eBB_017, eBB_018, eBB_019, eBB_020,
                            eBB_021, eBB_022, eBB_023, eBB_024, eBB_025};
                    for (EditText e : edit_02) {
                        e.removeTextChangedListener(null);
                        e.setText("");
                        e.addTextChangedListener(new ToolAllEventApproximate.ToolAllEventApproximateWatcher(e, getResources(), getApplicationContext(), scrollView));
                    }

                    // 保持しておいた値を移動
                    resetTotalReg = saveTotalReg;
                    // Reg関係
                    EditText[] edit_03 = {eRB_001, eRB_002, eRB_003, eRB_004, eRB_005, eRB_006, eRB_007, eRB_008, eRB_009, eRB_010,
                            eRB_011, eRB_012, eRB_013, eRB_014, eRB_015, eRB_016, eRB_017, eRB_018, eRB_019, eRB_020,
                            eRB_021, eRB_022, eRB_023, eRB_024, eRB_025};
                    for (EditText e : edit_03) {
                        e.removeTextChangedListener(null);
                        e.setText("");
                        e.addTextChangedListener(new ToolAllEventApproximate.ToolAllEventApproximateWatcher(e, getResources(), getApplicationContext(), scrollView));
                    }

                    //最初に戻る
                    scrollView.fullScroll(View.FOCUS_UP);
                    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                    layout.requestFocus();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (!visible) {
            //キーボードが非表示になったことを検知した時
            layout.requestFocus();
        }
    }

    // インナークラスで複数のViewにTextWatcherを対応させる
    private static class ToolAllEventApproximateWatcher implements TextWatcher {

        View view;
        Resources resources;
        Context context;
        ScrollView scrollView;

        static int totalGames;
        static int totalBig;
        static int totalReg;
        static int allData;

        private ToolAllEventApproximateWatcher(View view, Resources resources, Context context, ScrollView scrollView) {
            this.view = view;
            this.resources = resources;
            this.context = context;
            this.scrollView = scrollView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {

                // Big関係
                case R.id.BB_01:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(0).requestFocus();
                    }
                    break;

                case R.id.BB_02:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(1).requestFocus();
                    }
                    break;

                case R.id.BB_03:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(2).requestFocus();
                    }
                    break;

                case R.id.BB_04:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(3).requestFocus();
                    }
                    break;

                case R.id.BB_05:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(4).requestFocus();
                    }
                    break;

                case R.id.BB_06:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(5).requestFocus();
                    }
                    break;

                case R.id.BB_07:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(6).requestFocus();
                    }
                    break;

                case R.id.BB_08:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(7).requestFocus();
                    }
                    break;

                case R.id.BB_09:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(8).requestFocus();
                    }
                    break;

                case R.id.BB_10:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(9).requestFocus();
                    }
                    break;


                case R.id.BB_11:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(10).requestFocus();
                    }
                    break;

                case R.id.BB_12:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(11).requestFocus();
                    }
                    break;

                case R.id.BB_13:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(12).requestFocus();
                    }
                    break;

                case R.id.BB_14:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(13).requestFocus();
                    }
                    break;

                case R.id.BB_15:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(14).requestFocus();
                    }
                    break;

                case R.id.BB_16:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(15).requestFocus();
                    }
                    break;

                case R.id.BB_17:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(16).requestFocus();
                    }
                    break;

                case R.id.BB_18:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(17).requestFocus();
                    }
                    break;

                case R.id.BB_19:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(18).requestFocus();
                    }
                    break;

                case R.id.BB_20:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(19).requestFocus();
                    }
                    break;

                case R.id.BB_21:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(20).requestFocus();
                    }
                    break;

                case R.id.BB_22:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(21).requestFocus();
                    }
                    break;

                case R.id.BB_23:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(22).requestFocus();
                    }
                    break;

                case R.id.BB_24:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(23).requestFocus();
                    }
                    break;

                case R.id.BB_25:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(24).requestFocus();
                    }
                    break;

                // Reg関係
                case R.id.RB_01:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(0).requestFocus();
                    }
                    break;

                case R.id.RB_02:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(1).requestFocus();
                    }
                    break;

                case R.id.RB_03:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(2).requestFocus();
                    }
                    break;

                case R.id.RB_04:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(3).requestFocus();
                    }
                    break;

                case R.id.RB_05:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(4).requestFocus();
                    }
                    break;

                case R.id.RB_06:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(5).requestFocus();
                    }
                    break;

                case R.id.RB_07:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(6).requestFocus();
                    }
                    break;

                case R.id.RB_08:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(7).requestFocus();
                    }
                    break;

                case R.id.RB_09:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(8).requestFocus();
                    }
                    break;

                case R.id.RB_10:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(9).requestFocus();
                    }
                    break;

                case R.id.RB_11:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(10).requestFocus();
                    }
                    break;

                case R.id.RB_12:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(11).requestFocus();
                    }
                    break;

                case R.id.RB_13:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(12).requestFocus();
                    }
                    break;

                case R.id.RB_14:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(13).requestFocus();
                    }
                    break;

                case R.id.RB_15:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(14).requestFocus();
                    }
                    break;

                case R.id.RB_16:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(15).requestFocus();
                    }
                    break;

                case R.id.RB_17:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(16).requestFocus();
                    }
                    break;

                case R.id.RB_18:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(17).requestFocus();
                    }
                    break;

                case R.id.RB_19:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(18).requestFocus();
                    }
                    break;

                case R.id.RB_20:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(19).requestFocus();
                    }
                    break;

                case R.id.RB_21:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(20).requestFocus();
                    }
                    break;

                case R.id.RB_22:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(21).requestFocus();
                    }
                    break;

                case R.id.RB_23:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(22).requestFocus();
                    }
                    break;

                case R.id.RB_24:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(23).requestFocus();
                    }
                    break;

                case R.id.RB_25:
                    calTotalReg();
                    if (s.length() == 2) {
                        ePro.get(24).requestFocus();
                    }
                    break;

                // 確率関係
                case R.id.Pro_01:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(1).requestFocus();
                    }
                    break;

                case R.id.Pro_02:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(2).requestFocus();
                    }
                    break;

                case R.id.Pro_03:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(3).requestFocus();
                    }
                    break;

                case R.id.Pro_04:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(4).requestFocus();
                    }
                    break;

                case R.id.Pro_05:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(5).requestFocus();
                    }
                    break;

                case R.id.Pro_06:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(6).requestFocus();
                    }
                    break;

                case R.id.Pro_07:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(7).requestFocus();
                    }
                    break;

                case R.id.Pro_08:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(8).requestFocus();
                    }
                    break;

                case R.id.Pro_09:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(9).requestFocus();
                    }
                    break;

                case R.id.Pro_10:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(10).requestFocus();
                    }
                    break;

                case R.id.Pro_11:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(11).requestFocus();
                    }
                    break;

                case R.id.Pro_12:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(12).requestFocus();
                    }
                    break;

                case R.id.Pro_13:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(13).requestFocus();
                    }
                    break;

                case R.id.Pro_14:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(14).requestFocus();
                    }
                    break;

                case R.id.Pro_15:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(15).requestFocus();
                    }
                    break;

                case R.id.Pro_16:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(16).requestFocus();
                    }
                    break;

                case R.id.Pro_17:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(17).requestFocus();
                    }
                    break;

                case R.id.Pro_18:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(18).requestFocus();
                    }
                    break;

                case R.id.Pro_19:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(19).requestFocus();
                    }
                    break;

                case R.id.Pro_20:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(20).requestFocus();
                    }
                    break;

                case R.id.Pro_21:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(21).requestFocus();
                    }
                    break;

                case R.id.Pro_22:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(22).requestFocus();
                    }
                    break;

                case R.id.Pro_23:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(23).requestFocus();
                    }
                    break;

                case R.id.Pro_24:
                    calTotalGames();
                    if (s.length() == 3) {
                        eBBs.get(24).requestFocus();
                    }
                    break;

                case R.id.Pro_25:
                    calTotalGames();
                    if (s.length() == 3) {
                        //最初に戻る
                        scrollView.fullScroll(View.FOCUS_UP);
                        eBBs.get(0).requestFocus();
                    }
                    break;

            }
        }

        @SuppressLint("DefaultLocale")
        private String setFormat(double probability) {
            return "1/" + String.format("%.2f", probability);
        }

        public void calTotalBig() {

            // Big合計を計算
            totalBig = 0;
            for (int i = 0, size = eBBs.size(); i < size; i++) {
                if (StringUtils.isNotEmpty(eBBs.get(i).getText())) {
                    totalBig += Integer.parseInt(eBBs.get(i).getText().toString());
                }
            }
            // 保持した値を加える
            totalBig += resetTotalBig;
            // 値の保持
            saveTotalBig = totalBig;

            // トータル関係のTextViewに計算結果をセット
            if (totalBig <= 250_000) { //上限は250,000回
                calTotalBigPro();
            } else {
                totalBig = 250_000;
                calTotalBigPro();
                Toast toast = Toast.makeText(context, "BIG回数が上限に達しました", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        public void calTotalBigPro() {
            // Bigの確率をセット
            if (totalGames > 0 && totalBig > 0) {
                double bb_Probability = (double) totalGames / (double) totalBig;
                tTextViews.get(1).setText(resources.getString(R.string.all_total_bb, totalBig, setFormat(bb_Probability)));
            } else {
                tTextViews.get(1).setText(resources.getString(R.string.all_total_bb, totalBig, "1/0.00"));
            }
            // 合算確率をセット
            allData = totalBig + totalReg;
            if (totalGames > 0 && allData > 0) {
                double bonus_Probability = (double) totalGames / (double) allData;
                tTextViews.get(3).setText(resources.getString(R.string.all_total_bonus, allData, setFormat(bonus_Probability)));
            } else {
                tTextViews.get(3).setText(resources.getString(R.string.all_total_bonus, allData, "1/0.00"));
            }
        }

        public void calTotalReg() {

            // Reg合計を計算
            totalReg = 0;
            for (int i = 0, size = eRBs.size(); i < size; i++) {
                if (StringUtils.isNotEmpty(eRBs.get(i).getText())) {
                    totalReg += Integer.parseInt(eRBs.get(i).getText().toString());
                }
            }
            // 保持した値を加える
            totalReg += resetTotalReg;
            // 値の保持
            saveTotalReg = totalReg;

            // トータル関係のTextViewに計算結果をセット
            if (totalReg <= 250_000) { //上限は250,000回
                calTotalRegPro();
            } else {
                totalReg = 250_000;
                calTotalRegPro();
                Toast toast = Toast.makeText(context, "REG回数が上限に達しました", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        public void calTotalRegPro() {
            // Regの確率をセット
            if (totalGames > 0 && totalReg > 0) {
                double rb_Probability = (double) totalGames / (double) totalReg;
                tTextViews.get(2).setText(resources.getString(R.string.all_total_rb, totalReg, setFormat(rb_Probability)));
            } else {
                tTextViews.get(2).setText(resources.getString(R.string.all_total_rb, totalReg, "1/0.00"));
            }
            // 合算確率をセット
            allData = totalBig + totalReg;
            if (totalGames > 0 && allData > 0) {
                double bonus_Probability = (double) totalGames / (double) allData;
                tTextViews.get(3).setText(resources.getString(R.string.all_total_bonus, allData, setFormat(bonus_Probability)));
            } else {
                tTextViews.get(3).setText(resources.getString(R.string.all_total_bonus, allData, "1/0.00"));
            }
        }

        public void calTotalGames() {

            // 総ゲーム数の計算
            totalGames = 0;
            for (int i = 0, size = ePro.size(); i < size; i++) {

                int BB = 0;
                int RB = 0;

                if (StringUtils.isNotEmpty(ePro.get(i).getText())) {

                    //　誤差をなるべく無くすために使用する変数
                    int error;

                    // BB回数取得
                    if (StringUtils.isNotEmpty(eBBs.get(i).getText())){
                        BB += Integer.parseInt(eBBs.get(i).getText().toString());
                    }

                    // RB回数取得
                    if (StringUtils.isNotEmpty(eRBs.get(i).getText())){
                        RB += Integer.parseInt(eRBs.get(i).getText().toString());
                    }

                    // ボーナス数/2の数値を使って誤差を吸収する
                    error = (int)Math.ceil((BB + RB) / 2.0);

                    // ゲーム数を算出
                    totalGames = totalGames + error + (BB + RB) * (Integer.parseInt(ePro.get(i).getText().toString()));
                }
            }

            // 保持した値を加える
            totalGames += resetTotalGames;
            // 値の保持
            saveTotalGames = totalGames;

            // トータル関係のTextViewに計算結果をセット
            if (totalGames <= 100_000_000) { //上限は100,000,000回転
                // 総ゲーム数をセット
                tTextViews.get(0).setText(resources.getString(R.string.all_total_games, totalGames));
                // 各種確率計算
                calPro();
            } else {
                totalGames = 100_000_000;
                tTextViews.get(0).setText(resources.getString(R.string.all_total_games, 100_000_000));
                calPro();
                Toast toast = Toast.makeText(context, "ゲーム数が上限に達しました", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        public void calPro() {
            // Bigの確率をセット
            if (totalBig > 0) {
                double bb_Probability = (double) totalGames / (double) totalBig;
                tTextViews.get(1).setText(resources.getString(R.string.all_total_bb, totalBig, setFormat(bb_Probability)));
            } else {
                tTextViews.get(1).setText(resources.getString(R.string.all_total_bb, totalBig, "1/0.00"));
            }
            // Regの確率をセット
            if (totalReg > 0) {
                double rb_Probability = (double) totalGames / (double) totalReg;
                tTextViews.get(2).setText(resources.getString(R.string.all_total_rb, totalReg, setFormat(rb_Probability)));
            } else {
                tTextViews.get(2).setText(resources.getString(R.string.all_total_rb, totalReg, "1/0.00"));
            }
            // 合算確率をセット
            allData = totalBig + totalReg;
            if (allData > 0) {
                double bonus_Probability = (double) totalGames / (double) allData;
                tTextViews.get(3).setText(resources.getString(R.string.all_total_bonus, allData, setFormat(bonus_Probability)));
            } else {
                tTextViews.get(3).setText(resources.getString(R.string.all_total_bonus, allData, "1/0.00"));
            }
        }






    }
}