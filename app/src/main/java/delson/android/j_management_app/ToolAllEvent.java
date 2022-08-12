package delson.android.j_management_app;

import androidx.appcompat.app.ActionBar;
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

public class ToolAllEvent extends AppCompatActivity implements KeyboardVisibility.OnKeyboardVisibilityListener {

    TextView tTotalGames, tTotalBig, tTotalReg, tAllBonus;
    static List<TextView> tTextViews;

    EditText eGames_001, eGames_002, eGames_003, eGames_004, eGames_005, eGames_006, eGames_007, eGames_008, eGames_009, eGames_010,
            eGames_011, eGames_012, eGames_013, eGames_014, eGames_015, eGames_016, eGames_017, eGames_018, eGames_019, eGames_020,
            eGames_021, eGames_022, eGames_023, eGames_024, eGames_025;
    static List<EditText> eGames;

    EditText eBB_001, eBB_002, eBB_003, eBB_004, eBB_005, eBB_006, eBB_007, eBB_008, eBB_009, eBB_010,
            eBB_011, eBB_012, eBB_013, eBB_014, eBB_015, eBB_016, eBB_017, eBB_018, eBB_019, eBB_020,
            eBB_021, eBB_022, eBB_023, eBB_024, eBB_025;
    static List<EditText> eBBs;

    EditText eRB_001, eRB_002, eRB_003, eRB_004, eRB_005, eRB_006, eRB_007, eRB_008, eRB_009, eRB_010,
            eRB_011, eRB_012, eRB_013, eRB_014, eRB_015, eRB_016, eRB_017, eRB_018, eRB_019, eRB_020,
            eRB_021, eRB_022, eRB_023, eRB_024, eRB_025;
    static List<EditText> eRBs;

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
        setContentView(R.layout.tool01_all_event_survey);

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
        AllEventSurveyWatcher.totalGames = 0;
        AllEventSurveyWatcher.totalBig = 0;
        AllEventSurveyWatcher.totalReg = 0;
        AllEventSurveyWatcher.allData = 0;

        // 初期値をTextViewにセット
        tTotalGames.setText(getResources().getString(R.string.all_total_games, 0));
        tTotalBig.setText(getResources().getString(R.string.all_total_bb, 0, "1/0.00"));
        tTotalReg.setText(getResources().getString(R.string.all_total_rb, 0, "1/0.00"));
        tAllBonus.setText(getResources().getString(R.string.all_total_bonus, 0, "1/0.00"));
    }

    public void setFindViewById() {

        // TextView関係のID設定
        tTotalGames = findViewById(R.id.AllGames);
        tTotalBig = findViewById(R.id.AllBig);
        tTotalReg = findViewById(R.id.AllReg);
        tAllBonus = findViewById(R.id.AllData);
        // static配列に格納
        tTextViews = new ArrayList<>();
        TextView[] textView = {tTotalGames, tTotalBig, tTotalReg, tAllBonus};
        tTextViews.addAll(Arrays.asList(textView));

        eGames_001 = findViewById(R.id.Games_001);
        eGames_002 = findViewById(R.id.Games_002);
        eGames_003 = findViewById(R.id.Games_003);
        eGames_004 = findViewById(R.id.Games_004);
        eGames_005 = findViewById(R.id.Games_005);
        eGames_006 = findViewById(R.id.Games_006);
        eGames_007 = findViewById(R.id.Games_007);
        eGames_008 = findViewById(R.id.Games_008);
        eGames_009 = findViewById(R.id.Games_009);
        eGames_010 = findViewById(R.id.Games_010);
        eGames_011 = findViewById(R.id.Games_011);
        eGames_012 = findViewById(R.id.Games_012);
        eGames_013 = findViewById(R.id.Games_013);
        eGames_014 = findViewById(R.id.Games_014);
        eGames_015 = findViewById(R.id.Games_015);
        eGames_016 = findViewById(R.id.Games_016);
        eGames_017 = findViewById(R.id.Games_017);
        eGames_018 = findViewById(R.id.Games_018);
        eGames_019 = findViewById(R.id.Games_019);
        eGames_020 = findViewById(R.id.Games_020);
        eGames_021 = findViewById(R.id.Games_021);
        eGames_022 = findViewById(R.id.Games_022);
        eGames_023 = findViewById(R.id.Games_023);
        eGames_024 = findViewById(R.id.Games_024);
        eGames_025 = findViewById(R.id.Games_025);

        eBB_001 = findViewById(R.id.BB_001);
        eBB_002 = findViewById(R.id.BB_002);
        eBB_003 = findViewById(R.id.BB_003);
        eBB_004 = findViewById(R.id.BB_004);
        eBB_005 = findViewById(R.id.BB_005);
        eBB_006 = findViewById(R.id.BB_006);
        eBB_007 = findViewById(R.id.BB_007);
        eBB_008 = findViewById(R.id.BB_008);
        eBB_009 = findViewById(R.id.BB_009);
        eBB_010 = findViewById(R.id.BB_010);
        eBB_011 = findViewById(R.id.BB_011);
        eBB_012 = findViewById(R.id.BB_012);
        eBB_013 = findViewById(R.id.BB_013);
        eBB_014 = findViewById(R.id.BB_014);
        eBB_015 = findViewById(R.id.BB_015);
        eBB_016 = findViewById(R.id.BB_016);
        eBB_017 = findViewById(R.id.BB_017);
        eBB_018 = findViewById(R.id.BB_018);
        eBB_019 = findViewById(R.id.BB_019);
        eBB_020 = findViewById(R.id.BB_020);
        eBB_021 = findViewById(R.id.BB_021);
        eBB_022 = findViewById(R.id.BB_022);
        eBB_023 = findViewById(R.id.BB_023);
        eBB_024 = findViewById(R.id.BB_024);
        eBB_025 = findViewById(R.id.BB_025);

        eRB_001 = findViewById(R.id.RB_001);
        eRB_002 = findViewById(R.id.RB_002);
        eRB_003 = findViewById(R.id.RB_003);
        eRB_004 = findViewById(R.id.RB_004);
        eRB_005 = findViewById(R.id.RB_005);
        eRB_006 = findViewById(R.id.RB_006);
        eRB_007 = findViewById(R.id.RB_007);
        eRB_008 = findViewById(R.id.RB_008);
        eRB_009 = findViewById(R.id.RB_009);
        eRB_010 = findViewById(R.id.RB_010);
        eRB_011 = findViewById(R.id.RB_011);
        eRB_012 = findViewById(R.id.RB_012);
        eRB_013 = findViewById(R.id.RB_013);
        eRB_014 = findViewById(R.id.RB_014);
        eRB_015 = findViewById(R.id.RB_015);
        eRB_016 = findViewById(R.id.RB_016);
        eRB_017 = findViewById(R.id.RB_017);
        eRB_018 = findViewById(R.id.RB_018);
        eRB_019 = findViewById(R.id.RB_019);
        eRB_020 = findViewById(R.id.RB_020);
        eRB_021 = findViewById(R.id.RB_021);
        eRB_022 = findViewById(R.id.RB_022);
        eRB_023 = findViewById(R.id.RB_023);
        eRB_024 = findViewById(R.id.RB_024);
        eRB_025 = findViewById(R.id.RB_025);

        layout = findViewById(R.id.tool_AllEvent_layout);
        scrollView = findViewById(R.id.tool_AllEvent_scroll);

    }

    public void setTextWatcher() {

        // ゲーム数関係
        eGames = new ArrayList<>();
        EditText[] edit_01 = {eGames_001, eGames_002, eGames_003, eGames_004, eGames_005, eGames_006, eGames_007, eGames_008, eGames_009, eGames_010,
                eGames_011, eGames_012, eGames_013, eGames_014, eGames_015, eGames_016, eGames_017, eGames_018, eGames_019, eGames_020,
                eGames_021, eGames_022, eGames_023, eGames_024, eGames_025};
        for (EditText e : edit_01) {
            e.addTextChangedListener(new AllEventSurveyWatcher(e, getResources(), getApplicationContext(),scrollView));
            eGames.add(e);
        }

        // Big関係
        eBBs = new ArrayList<>();
        EditText[] edit_02 = {eBB_001, eBB_002, eBB_003, eBB_004, eBB_005, eBB_006, eBB_007, eBB_008, eBB_009, eBB_010,
                eBB_011, eBB_012, eBB_013, eBB_014, eBB_015, eBB_016, eBB_017, eBB_018, eBB_019, eBB_020,
                eBB_021, eBB_022, eBB_023, eBB_024, eBB_025};
        for (EditText e : edit_02) {
            e.addTextChangedListener(new AllEventSurveyWatcher(e, getResources(), getApplicationContext(),scrollView));
            eBBs.add(e);
        }

        // Reg関係
        eRBs = new ArrayList<>();
        EditText[] edit_03 = {eRB_001, eRB_002, eRB_003, eRB_004, eRB_005, eRB_006, eRB_007, eRB_008, eRB_009, eRB_010,
                eRB_011, eRB_012, eRB_013, eRB_014, eRB_015, eRB_016, eRB_017, eRB_018, eRB_019, eRB_020,
                eRB_021, eRB_022, eRB_023, eRB_024, eRB_025};
        for (EditText e : edit_03) {
            e.addTextChangedListener(new AllEventSurveyWatcher(e, getResources(), getApplicationContext(),scrollView));
            eRBs.add(e);
        }
    }

    public void resetButton(View view){

        // 保持しておいた値を移動
        resetTotalGames = saveTotalGames;
        // ゲーム数関係のEditTextを空にする。
        EditText[] edit_01 = {eGames_001, eGames_002, eGames_003, eGames_004, eGames_005, eGames_006, eGames_007, eGames_008, eGames_009, eGames_010,
                eGames_011, eGames_012, eGames_013, eGames_014, eGames_015, eGames_016, eGames_017, eGames_018, eGames_019, eGames_020,
                eGames_021, eGames_022, eGames_023, eGames_024, eGames_025};
        for (EditText e : edit_01) {
            e.removeTextChangedListener(null);
            e.setText("");
            e.addTextChangedListener(new AllEventSurveyWatcher(e, getResources(), getApplicationContext(),scrollView));
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
            e.addTextChangedListener(new AllEventSurveyWatcher(e, getResources(), getApplicationContext(),scrollView));
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
            e.addTextChangedListener(new AllEventSurveyWatcher(e, getResources(), getApplicationContext(),scrollView));
        }

        //最初に戻る
        scrollView.fullScroll(View.FOCUS_UP);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
        layout.requestFocus();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if(!visible){
            //キーボードが非表示になったことを検知した時
            layout.requestFocus();
        }
    }




    // インナークラスで複数のViewにTextWatcherを対応させる
    private static class AllEventSurveyWatcher implements TextWatcher {

        View view;
        Resources resources;
        Context context;
        ScrollView scrollView;

        static int totalGames;
        static int totalBig;
        static int totalReg;
        static int allData;

        private AllEventSurveyWatcher(View view, Resources resources, Context context, ScrollView scrollView) {
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

                // ゲーム数関係

                case R.id.Games_001:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(0).requestFocus();
                    }
                    break;

                case R.id.Games_002:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(1).requestFocus();
                    }
                    break;

                case R.id.Games_003:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(2).requestFocus();
                    }
                    break;

                case R.id.Games_004:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(3).requestFocus();
                    }
                    break;

                case R.id.Games_005:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(4).requestFocus();
                    }
                    break;

                case R.id.Games_006:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(5).requestFocus();
                    }
                    break;

                case R.id.Games_007:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(6).requestFocus();
                    }
                    break;

                case R.id.Games_008:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(7).requestFocus();
                    }
                    break;

                case R.id.Games_009:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(8).requestFocus();
                    }
                    break;

                case R.id.Games_010:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(9).requestFocus();
                    }
                    break;

                case R.id.Games_011:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(10).requestFocus();
                    }
                    break;

                case R.id.Games_012:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(11).requestFocus();
                    }
                    break;

                case R.id.Games_013:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(12).requestFocus();
                    }
                    break;

                case R.id.Games_014:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(13).requestFocus();
                    }
                    break;

                case R.id.Games_015:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(14).requestFocus();
                    }
                    break;

                case R.id.Games_016:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(15).requestFocus();
                    }
                    break;

                case R.id.Games_017:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(16).requestFocus();
                    }
                    break;

                case R.id.Games_018:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(17).requestFocus();
                    }
                    break;

                case R.id.Games_019:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(18).requestFocus();
                    }
                    break;

                case R.id.Games_020:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(19).requestFocus();
                    }
                    break;

                case R.id.Games_021:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(20).requestFocus();
                    }
                    break;

                case R.id.Games_022:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(21).requestFocus();
                    }
                    break;

                case R.id.Games_023:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(22).requestFocus();
                    }
                    break;

                case R.id.Games_024:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(23).requestFocus();
                    }
                    break;

                case R.id.Games_025:
                    calTotalGames();
                    if (s.length() == 4) {
                        eBBs.get(24).requestFocus();
                    }
                    break;

                    // Big関係

                case R.id.BB_001:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(0).requestFocus();
                    }
                    break;

                case R.id.BB_002:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(1).requestFocus();
                    }
                    break;

                case R.id.BB_003:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(2).requestFocus();
                    }
                    break;

                case R.id.BB_004:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(3).requestFocus();
                    }
                    break;

                case R.id.BB_005:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(4).requestFocus();
                    }
                    break;

                case R.id.BB_006:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(5).requestFocus();
                    }
                    break;

                case R.id.BB_007:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(6).requestFocus();
                    }
                    break;

                case R.id.BB_008:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(7).requestFocus();
                    }
                    break;

                case R.id.BB_009:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(8).requestFocus();
                    }
                    break;

                case R.id.BB_010:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(9).requestFocus();
                    }
                    break;


                case R.id.BB_011:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(10).requestFocus();
                    }
                    break;

                case R.id.BB_012:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(11).requestFocus();
                    }
                    break;

                case R.id.BB_013:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(12).requestFocus();
                    }
                    break;

                case R.id.BB_014:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(13).requestFocus();
                    }
                    break;

                case R.id.BB_015:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(14).requestFocus();
                    }
                    break;

                case R.id.BB_016:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(15).requestFocus();
                    }
                    break;

                case R.id.BB_017:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(16).requestFocus();
                    }
                    break;

                case R.id.BB_018:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(17).requestFocus();
                    }
                    break;

                case R.id.BB_019:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(18).requestFocus();
                    }
                    break;

                case R.id.BB_020:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(19).requestFocus();
                    }
                    break;

                case R.id.BB_021:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(20).requestFocus();
                    }
                    break;

                case R.id.BB_022:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(21).requestFocus();
                    }
                    break;

                case R.id.BB_023:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(22).requestFocus();
                    }
                    break;

                case R.id.BB_024:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(23).requestFocus();
                    }
                    break;

                case R.id.BB_025:
                    calTotalBig();
                    if (s.length() == 2) {
                        eRBs.get(24).requestFocus();
                    }
                    break;

                // Reg関係

                case R.id.RB_001:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(1).requestFocus();
                    }
                    break;

                case R.id.RB_002:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(2).requestFocus();
                    }
                    break;

                case R.id.RB_003:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(3).requestFocus();
                    }
                    break;

                case R.id.RB_004:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(4).requestFocus();
                    }
                    break;

                case R.id.RB_005:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(5).requestFocus();
                    }
                    break;

                case R.id.RB_006:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(6).requestFocus();
                    }
                    break;

                case R.id.RB_007:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(7).requestFocus();
                    }
                    break;

                case R.id.RB_008:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(8).requestFocus();
                    }
                    break;

                case R.id.RB_009:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(9).requestFocus();
                    }
                    break;

                case R.id.RB_010:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(10).requestFocus();
                    }
                    break;

                case R.id.RB_011:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(11).requestFocus();
                    }
                    break;

                case R.id.RB_012:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(12).requestFocus();
                    }
                    break;

                case R.id.RB_013:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(13).requestFocus();
                    }
                    break;

                case R.id.RB_014:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(14).requestFocus();
                    }
                    break;

                case R.id.RB_015:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(15).requestFocus();
                    }
                    break;

                case R.id.RB_016:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(16).requestFocus();
                    }
                    break;

                case R.id.RB_017:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(17).requestFocus();
                    }
                    break;

                case R.id.RB_018:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(18).requestFocus();
                    }
                    break;

                case R.id.RB_019:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(19).requestFocus();
                    }
                    break;

                case R.id.RB_020:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(20).requestFocus();
                    }
                    break;

                case R.id.RB_021:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(21).requestFocus();
                    }
                    break;

                case R.id.RB_022:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(22).requestFocus();
                    }
                    break;

                case R.id.RB_023:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(23).requestFocus();
                    }
                    break;

                case R.id.RB_024:
                    calTotalReg();
                    if (s.length() == 2) {
                        eGames.get(24).requestFocus();
                    }
                    break;

                case R.id.RB_025:
                    calTotalReg();
                    if (s.length() == 2) {
                        //最初に戻る
                        scrollView.fullScroll(View.FOCUS_UP);
                        eGames.get(0).requestFocus();
                    }
                    break;

            }
        }

        @SuppressLint("DefaultLocale")
        private String setFormat(double probability) {
            return "1/" + String.format("%.2f", probability);
        }

        public void calTotalGames() {
            // 総ゲーム数の計算
            totalGames = 0;
            for (int i = 0, size = eGames.size(); i < size; i++) {
                if (StringUtils.isNotEmpty(eGames.get(i).getText())) {
                    totalGames += Integer.parseInt(eGames.get(i).getText().toString());
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

    }
}