package com.example.title_1;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

//-----------------------------------------------------------------------------------------------
//カウンター機能の制御【メイン処理】
//-----------------------------------------------------------------------------------------------
public class MainCounterWatcher implements TextWatcher {

    final String INIT_VALUE = "1/0.00";

    //ゲーム数関係
    EditText eTotalGames = MainCounterActivity.eTotalGames;
    EditText eStartGames = MainCounterActivity.eStartGames;
    EditText eIndividualGames = MainCounterActivity.eIndividualGames;

    //カウンター関係
    EditText eSingleBig = MainCounterActivity.eSingleBig;
    EditText eCherryBig = MainCounterActivity.eCherryBig;
    EditText eTotalBig = MainCounterActivity.eTotalBig;
    EditText eSingleReg = MainCounterActivity.eSingleReg;
    EditText eCherryReg = MainCounterActivity.eCherryReg;
    EditText eTotalReg = MainCounterActivity.eTotalReg;
    EditText eCherry = MainCounterActivity.eCherry;
    EditText eGrape = MainCounterActivity.eGrape;
    EditText eTotalBonus = MainCounterActivity.eTotalBonus;

    //確率関係
    TextView tSingleBigProbability = MainCounterActivity.tSingleBigProbability;
    TextView tCherryBigProbability = MainCounterActivity.tCherryBigProbability;
    TextView tTotalBigProbability = MainCounterActivity.tTotalBigProbability;
    TextView tSingleRegProbability = MainCounterActivity.tSingleRegProbability;
    TextView tCherryRegProbability = MainCounterActivity.tCherryRegProbability;
    TextView tTotalRegProbability = MainCounterActivity.tTotalRegProbability;
    TextView tCherryProbability = MainCounterActivity.tCherryProbability;
    TextView tGrapeProbability = MainCounterActivity.tGrapeProbability;
    TextView tTotalBonusProbability = MainCounterActivity.tTotalBonusProbability;

    // 共有データ
    MainApplication mainApplication;

    TextView view;

    public MainCounterWatcher(TextView view, MainApplication mainApplication) {
        this.view = view;
        this.mainApplication = mainApplication;
    }

    //以下３つのメソッドはTextWatcherを実装するためにオーバーライド必須
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void afterTextChanged(Editable s) {

        //空が入ってきたら0に置き換える
        String textString = s.toString();
        if(textString.isEmpty()){
            textString = "0";
        }

        String strTotal = eTotalGames.getText().toString();
        String strStart = eStartGames.getText().toString();
        int totalGame;
        int startGame;

        //ビッグ
        String strAloneBig = eSingleBig.getText().toString();
        String strCherryBig = eCherryBig.getText().toString();
        String strTotalBig = eTotalBig.getText().toString();

        //レギュラー
        String strAloneReg = eSingleReg.getText().toString();
        String strCherryReg = eCherryReg.getText().toString();
        String strTotalReg = eTotalReg.getText().toString();

        //小役
        String strCherry;
        String strGr;

        //差分ゲーム数
        double ind = Integer.parseInt(eIndividualGames.getText().toString());
        String individualValue = "0";

        //TextWatcherを設定したどのViewが操作されたのか、switchを使って振り分ける
        switch (view.getId()) {

            //総ゲーム数の場合
            case R.id.eTotalGames:

                //無限ループを回避するために、テキストウォッチャーを解除する
                eTotalGames.removeTextChangedListener(this);
                //入力値を更新
                strTotal = textString;

                // 0頭回避処理
                strTotal = Integer.valueOf(strTotal).toString();

                totalGame = Integer.parseInt(strTotal);
                // MainCounterActivity.javaのsetValue()で「総ゲーム数」から内部ストレージの値を
                // セットしている関係でまずtotal_gameのテキストウォッチャーが発動する
                // そのため、必ず「開始ゲーム数」は空白となる。以下のif文はその空白対応のためにある
                if(strStart.equals("")){
                    strStart = "0";
                }
                startGame = Integer.parseInt(strStart);

                // 個人ゲーム数を算出。0以上だったらその値を個人ゲーム数にセット。マイナスだったら0をセット
                int individualGame = totalGame - startGame;
                if (individualGame >= 0) {
                    individualValue = String.valueOf(individualGame);
                } else {
                    individualValue = "0";
                }

                // 値のセット
                eTotalGames.setText(strTotal);
                eTotalGames.setSelection(strTotal.length());
                eIndividualGames.setText(individualValue);

                // 保存処理
                mainApplication.setTotalGames(strTotal);
                CreateXML.updateText(mainApplication, "total", strTotal);

                // テキストウォッチャーを元に戻す
                eTotalGames.addTextChangedListener(this);
                break;

            //打ち始めゲーム数の場合
            case R.id.eStartGames:

                eStartGames.removeTextChangedListener(this);
                // 入力値を更新
                strStart = textString;

                // 0頭回避処理
                strStart = Integer.valueOf(strStart).toString();

                totalGame = Integer.parseInt(strTotal);
                // MainCounterActivity.javaのsetValue()で「総ゲーム数」から内部ストレージの値を
                // セットしている関係でまずtotal_gameのテキストウォッチャーが発動する
                // そのため、start_gameに関しては0がすでにセットされているのでtotal_gameのような影響は受けない。よってif文での対応は不要
                startGame = Integer.parseInt(strStart);

                // 個人ゲーム数を算出。0以上だったらその値を個人ゲーム数にセット。マイナスだったら0をセット
                individualGame = totalGame - startGame;
                if (individualGame >= 0) {
                    individualValue = String.valueOf(individualGame);
                } else {
                    individualValue = "0";
                }

                // 値のセット
                eStartGames.setText(strStart);
                eStartGames.setSelection(strStart.length());
                eIndividualGames.setText(individualValue);

                // 保存処理
                mainApplication.setStartGames(strStart);
                CreateXML.updateText(mainApplication, "start", strStart);

                eStartGames.addTextChangedListener(this);
                break;

            //個人ゲーム数が変更されたら各役物の確率も変更
            case R.id.eIndividualGames:
                if (textString.equals("0")) {
                    setProbabilityZero();
                } else {
                    setCounterBlankProbabilityZero(eSingleBig, tSingleBigProbability);
                    setCounterBlankProbabilityZero(eCherryBig, tCherryBigProbability);
                    setCounterBlankProbabilityZero(eTotalBig, tTotalBigProbability);
                    setCounterBlankProbabilityZero(eSingleReg, tSingleRegProbability);
                    setCounterBlankProbabilityZero(eCherryReg, tCherryRegProbability);
                    setCounterBlankProbabilityZero(eTotalReg, tTotalRegProbability);
                    setCounterBlankProbabilityZero(eCherry, tCherryProbability);
                    setCounterBlankProbabilityZero(eGrape, tGrapeProbability);
                    setCounterBlankProbabilityZero(eTotalBonus, tTotalBonusProbability);
                }
                break;

            //--------------------------------------------------------------------------------------------------------------------
            //ここからボーナスカウンターボタン押下時及びカウンターを直接編集した時の挙動振り分け
            //--------------------------------------------------------------------------------------------------------------------

            case R.id.eSingleBig: //単独ビッグ

                eSingleBig.removeTextChangedListener(this);
                // 入力値を更新
                strAloneBig = textString;

                // 確率の再計算
                setProbability(strAloneBig, strCherryBig, eTotalBig, tSingleBigProbability, tCherryBigProbability, tTotalBigProbability);

                // 0頭回避処理
                strAloneBig = Integer.valueOf(strAloneBig).toString();

                eSingleBig.setText(strAloneBig);
                eSingleBig.setSelection(strAloneBig.length());

                // 保存処理
                mainApplication.setSingleBig(strAloneBig);
                CreateXML.updateText(mainApplication, "aB", strAloneBig);

                eSingleBig.addTextChangedListener(this);
                break;

            case R.id.eCherryBig: //チェリービッグ

                eCherryBig.removeTextChangedListener(this);
                //入力値を更新
                strCherryBig = textString;

                // 確率の再計算
                setProbability(strAloneBig, strCherryBig, eTotalBig, tSingleBigProbability, tCherryBigProbability, tTotalBigProbability);

                // 0頭回避処理
                strCherryBig = Integer.valueOf(strCherryBig).toString();

                eCherryBig.setText(strCherryBig);
                eCherryBig.setSelection(strCherryBig.length());

                // 保存処理
                mainApplication.setCherryBig(strCherryBig);
                CreateXML.updateText(mainApplication, "cB", strCherryBig);

                eCherryBig.addTextChangedListener(this);
                break;

            case R.id.eTotalBig: //ビッグボーナス

                //入力値を更新
                strTotalBig = textString;
                int additionCount = Integer.parseInt(strTotalBig) + Integer.parseInt(strTotalReg);
                eTotalBonus.setText(String.valueOf(additionCount));

                //総ゲーム数が0以外 かつ ボーナスが0以上だった場合
                if (!strTotal.equals("0") && additionCount > 0) {
                    double AdditionProbability = ind / additionCount;
                    tTotalBonusProbability.setText(setFormat(AdditionProbability));
                }
                break;

            case R.id.eSingleReg: //単独レギュラー

                eSingleReg.removeTextChangedListener(this);
                //入力値を更新
                strAloneReg = textString;

                // 確率の再計算
                setProbability(strAloneReg, strCherryReg, eTotalReg, tSingleRegProbability, tCherryRegProbability, tTotalRegProbability);

                // 0頭回避処理
                strAloneReg = Integer.valueOf(strAloneReg).toString();

                eSingleReg.setText(strAloneReg);
                eSingleReg.setSelection(strAloneReg.length());

                // 保存処理
                mainApplication.setSingleReg(strAloneReg);
                CreateXML.updateText(mainApplication, "aR", strAloneReg);

                eSingleReg.addTextChangedListener(this);
                break;

            case R.id.eCherryReg: //チェリービッグ

                eCherryReg.removeTextChangedListener(this);
                //入力値を更新
                strCherryReg = textString;

                // 確率の再計算
                setProbability(strAloneReg, strCherryReg, eTotalReg, tSingleRegProbability, tCherryRegProbability, tTotalRegProbability);

                // 0頭回避処理
                strCherryReg = Integer.valueOf(strCherryReg).toString();

                eCherryReg.setText(strCherryReg);
                eCherryReg.setSelection(strCherryReg.length());

                // 保存処理
                mainApplication.setCherryReg(strCherryReg);
                CreateXML.updateText(mainApplication, "cR", strCherryReg);

                eCherryReg.addTextChangedListener(this);
                break;

            case R.id.eTotalReg: //レギュラーボーナス

                //入力値を更新
                strTotalReg = textString;
                additionCount = Integer.parseInt(strTotalBig) + Integer.parseInt(strTotalReg);
                eTotalBonus.setText(String.valueOf(additionCount));
                if (!strTotal.equals("0") && additionCount > 0) {
                    double AdditionProbability = ind / additionCount;
                    tTotalBonusProbability.setText(setFormat(AdditionProbability));
                }
                break;

            case R.id.eCherry: //非重複チェリー

                eCherry.removeTextChangedListener(this);
                //入力値を更新
                strCherry = textString;

                // 確率の再計算
                setProbability_ch_gr(strCherry, tCherryProbability);

                // 0頭回避処理
                strCherry = Integer.valueOf(strCherry).toString();

                eCherry.setText(strCherry);
                eCherry.setSelection(strCherry.length());

                // 保存処理
                mainApplication.setCherry(strCherry);
                CreateXML.updateText(mainApplication, "ch", strCherry);

                eCherry.addTextChangedListener(this);
                break;

            case R.id.eGrape: //ぶどう

                eGrape.removeTextChangedListener(this);
                //入力値を更新
                strGr = textString;

                // 確率の再計算
                setProbability_ch_gr(strGr, tGrapeProbability);

                // 0頭回避処理
                strGr = Integer.valueOf(strGr).toString();

                eGrape.setText(strGr);
                eGrape.setSelection(strGr.length());

                // 保存処理
                mainApplication.setGrape(strGr);
                CreateXML.updateText(mainApplication, "gr", strGr);

                eGrape.addTextChangedListener(this);
                break;
        }
    }

    public void setProbability_ch_gr(String smallRole, TextView tv) {

        if (StringUtils.isEmpty(smallRole) || smallRole.equals("0") || smallRole.equals("00")) {
            tv.setText(INIT_VALUE);
        } else {
            double ind = Double.parseDouble(eIndividualGames.getText().toString());
            double sr = Double.parseDouble(smallRole);
            double sr_Probability = ind / sr;
            tv.setText(setFormat(sr_Probability));
        }
    }

    public void setCounterBlankProbabilityZero(EditText ed, TextView tv) {

        String edString = ed.getText().toString();
        String individualString = eIndividualGames.getText().toString();

        // テキストが空もしくは0だった場合は初期値を格納
        if (StringUtils.isEmpty(edString) || edString.equals("0")) {
            tv.setText(INIT_VALUE);
        } else {
            double ind = Double.parseDouble(individualString);
            double cnt = Double.parseDouble(edString);
            double probability = ind / cnt;
            tv.setText(setFormat(probability));
        }
    }

    public void setProbabilityZero() {
        tSingleBigProbability.setText(INIT_VALUE);
        tCherryBigProbability.setText(INIT_VALUE);
        tTotalBigProbability.setText(INIT_VALUE);
        tSingleRegProbability.setText(INIT_VALUE);
        tCherryRegProbability.setText(INIT_VALUE);
        tTotalRegProbability.setText(INIT_VALUE);
        tCherryProbability.setText(INIT_VALUE);
        tGrapeProbability.setText(INIT_VALUE);
        tTotalBonusProbability.setText(INIT_VALUE);
    }

    public void setProbability(String alone, String cherry, EditText bonus, TextView tv1, TextView tv2, TextView tv3) {

        String bonusValue = "0";
        String tv1Value = INIT_VALUE;
        String tv2Value = INIT_VALUE;
        String tv3Value = INIT_VALUE;
        int aloneValue = 0;
        int cherryValue = 0;

        if (StringUtils.isNotEmpty(alone)) {
            aloneValue = Integer.parseInt(alone);
        }
        if (StringUtils.isNotEmpty(cherry)) {
            cherryValue = Integer.parseInt(cherry);
        }

        //単独カウンターが空、かつ、チェリー重複カウンターが０
        if (alone.isEmpty() && cherryValue > 0) {
            double ind = Integer.parseInt(eIndividualGames.getText().toString());
            double probability = ind / cherryValue;

            bonusValue = Integer.valueOf(cherry).toString();
            tv2Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０、かつ、チェリー重複カウンターが０より大きい
        } else if (aloneValue == 0 && cherryValue > 0) {
            bonusValue = Integer.valueOf(cherry).toString();
            double ind = Integer.parseInt(eIndividualGames.getText().toString());
            double cnt = Integer.parseInt(cherry);
            double probability = ind / cnt;

            tv2Value = setFormat(probability);
            tv3Value = setFormat(probability);
            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが空
        } else if (aloneValue > 0 && cherry.isEmpty()) {
            bonusValue = Integer.valueOf(alone).toString();
            double ind = Integer.parseInt(eIndividualGames.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０
        } else if (aloneValue > 0 && cherryValue == 0) {
            bonusValue = Integer.valueOf(alone).toString();
            double ind = Integer.parseInt(eIndividualGames.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０より大きい
        } else if (aloneValue > 0 && cherryValue > 0) {
            double ind = Integer.parseInt(eIndividualGames.getText().toString());
            int aloneBonus = Integer.parseInt(alone);
            int cherryBonus = Integer.parseInt(cherry);
            int total_Bonus = aloneBonus + cherryBonus;
            bonusValue = String.valueOf(total_Bonus);

            double alone_Probability = ind / aloneBonus;
            tv1Value = setFormat(alone_Probability);

            double cherry_Probability = ind / cherryBonus;
            tv2Value = setFormat(cherry_Probability);

            double total_Probability = ind / total_Bonus;
            tv3Value = setFormat(total_Probability);
        }
        bonus.setText(bonusValue);
        tv1.setText(tv1Value);
        tv2.setText(tv2Value);
        tv3.setText(tv3Value);
    }

    @SuppressLint("DefaultLocale")
    private String setFormat(double probability) {
        return "1/" + String.format("%.2f", probability);
    }
}
