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
public class GamesCounterWatcher implements TextWatcher {

    final String INIT_VALUE = "1/0.00";

    //ゲーム数関係
    EditText total = MainCounterActivity.total;
    EditText start = MainCounterActivity.start;
    EditText individual = MainCounterActivity.individual;

    //カウンター関係
    EditText aB = MainCounterActivity.aB;
    EditText cB = MainCounterActivity.cB;
    EditText BB = MainCounterActivity.BB;
    EditText aR = MainCounterActivity.aR;
    EditText cR = MainCounterActivity.cR;
    EditText RB = MainCounterActivity.RB;
    EditText ch = MainCounterActivity.ch;
    EditText gr = MainCounterActivity.gr;
    EditText addition = MainCounterActivity.addition;

    //確率関係
    TextView aB_Probability = MainCounterActivity.aB_Probability;
    TextView cB_Probability = MainCounterActivity.cB_Probability;
    TextView BB_Probability = MainCounterActivity.BB_Probability;
    TextView aR_Probability = MainCounterActivity.aR_Probability;
    TextView cR_Probability = MainCounterActivity.cR_Probability;
    TextView RB_Probability = MainCounterActivity.RB_Probability;
    TextView ch_Probability = MainCounterActivity.ch_Probability;
    TextView gr_Probability = MainCounterActivity.gr_Probability;
    TextView addition_Probability = MainCounterActivity.addition_Probability;

    // 共有データ
    MainApplication mainApplication;

    TextView view;

    public GamesCounterWatcher(TextView view, MainApplication mainApplication){
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

        //入力値
        String textString = s.toString();

        String strTotal = total.getText().toString();
        String strStart = start.getText().toString();
        int totalGame;
        int startGame;

        //ビッグ
        String strAloneBig = aB.getText().toString();
        String strCherryBig = cB.getText().toString();
        String strTotalBig = BB.getText().toString();

        //レギュラー
        String strAloneReg = aR.getText().toString();
        String strCherryReg = cR.getText().toString();
        String strTotalReg = RB.getText().toString();

        //小役
        String strCherry;
        String strGr;

        //差分ゲーム数
        double ind = Integer.parseInt(individual.getText().toString());
        String individualValue = "0";

        //TextWatcherを設定したどのViewが操作されたのか、switchを使って振り分ける
        switch (view.getId()){

            //総ゲーム数の場合
            case R.id.total_game:

                //無限ループを回避するために、テキストウォッチャーを解除する
                total.removeTextChangedListener(this);
                //入力値を更新
                strTotal = textString;

                //総ゲーム数が空ではなかった場合
                if (StringUtils.isNotEmpty(strTotal)) {
                    //数値変換して文字型に再変換することで０頭を回避
                    strTotal = Integer.valueOf(strTotal).toString();
                    totalGame = Integer.parseInt(strTotal);
                    //打ち始めゲーム数が空だった場合
                    if (strStart.isEmpty()) {
                        //０が入力されていたら、次に入力された数値で上書きする
                        //個人ゲーム数に総ゲーム数をセット
                        individualValue = String.valueOf(totalGame);
                        //打ち始めゲーム数に数値が入力されていたら、総ゲーム数-打ち始めゲーム数を算出
                    } else {
                        startGame = Integer.parseInt(strStart);
                        int individualGame = totalGame - startGame;
                        //差がプラスだったら差分をセット
                        if (individualGame >= 0) {
                            individualValue = String.valueOf(individualGame);
                        }
                    }
                    // 値のセット
                    total.setText(strTotal);
                    total.setSelection(strTotal.length());
                }
                individual.setText(individualValue);

                if(StringUtils.isNotEmpty(strTotal)) {
                    // 保存処理
                    mainApplication.setTotal(strTotal);
                    CreateXML.updateText(mainApplication,"total",strTotal);
                } else {
                    mainApplication.setTotal("0");
                    CreateXML.updateText(mainApplication,"total","0");
                    // 空白に戻す
                    // mainApplication.setTotal("");
                }

                //テキストウォッチャーを元に戻す
                total.addTextChangedListener(this);
                break;

            //打ち始めゲーム数の場合
            case R.id.start_game:
                start.removeTextChangedListener(this);
                //入力値を更新
                strStart = textString;

                if(StringUtils.isEmpty(strStart)){
                    //打ち始めゲーム数が空、総ゲーム数に値が入っていた場合
                    if(StringUtils.isNotEmpty(strTotal)){
                        totalGame = Integer.parseInt(strTotal);
                        //個人ゲーム数に総ゲーム数を格納
                        individualValue = String.valueOf(totalGame);
                    }
                } else {
                    //打ち始めゲーム数、総ゲーム数に値が入っていた場合
                    if(StringUtils.isNotEmpty(strTotal)){
                        totalGame = Integer.parseInt(strTotal);
                        strStart = Integer.valueOf(strStart).toString();
                        startGame = Integer.parseInt(strStart);
                        int individualGame = totalGame - startGame;

                        if(individualGame >= 0) {
                            individualValue = String.valueOf(individualGame);
                        }
                    } else {
                        //総ゲーム数が空、打ち始めゲーム数に値が入っていた場合
                        strStart = Integer.valueOf(strStart).toString();
                    }
                }

                // 値のセット
                start.setText(strStart);
                start.setSelection(strStart.length());
                individual.setText(individualValue);


                if(StringUtils.isNotEmpty(strStart)) {
                    // 保存処理
                    mainApplication.setStart(strStart);
                    CreateXML.updateText(mainApplication,"start",strStart);
                } else {
                    mainApplication.setStart("0");
                    CreateXML.updateText(mainApplication,"start","0");
                    // 空白に戻す
                    // mainApplication.setStart("");
                }

                start.addTextChangedListener(this);
                break;

            //個人ゲーム数が変更されたら各役物の確率も変更
            case R.id.individual_game:
                if (textString.equals("0")) {
                    setProbabilityZero();
                } else {
                    setCounterBlankProbabilityZero(aB,aB_Probability);
                    setCounterBlankProbabilityZero(cB,cB_Probability);
                    setCounterBlankProbabilityZero(BB,BB_Probability);
                    setCounterBlankProbabilityZero(aR,aR_Probability);
                    setCounterBlankProbabilityZero(cR,cR_Probability);
                    setCounterBlankProbabilityZero(RB,RB_Probability);
                    setCounterBlankProbabilityZero(ch,ch_Probability);
                    setCounterBlankProbabilityZero(gr,gr_Probability);
                    setCounterBlankProbabilityZero(addition,addition_Probability);
                }
                break;

            //--------------------------------------------------------------------------------------------------------------------
            //ここからボーナスカウンターボタン押下時及びカウンターを直接編集した時の挙動振り分け
            //--------------------------------------------------------------------------------------------------------------------

            case R.id.aB: //単独ビッグ
                aB.removeTextChangedListener(this);
                //入力値を更新
                strAloneBig = textString;

                setProbability(strAloneBig,strCherryBig,BB,aB_Probability,cB_Probability,BB_Probability);

                //空の状態でsetText(Integer.valueOf(textString).toString())をやるとクラッシュしちゃう
                if (StringUtils.isNotEmpty(strAloneBig)) {
                    strAloneBig = Integer.valueOf(strAloneBig).toString();
                    aB.setText(strAloneBig);
                    aB.setSelection(strAloneBig.length());
                } else {
                    aB.setText("0");
                    aB.setSelection(1);
                }

                // 保存処理
                mainApplication.setaB(strAloneBig);
                CreateXML.updateText(mainApplication,"aB",strAloneBig);

                aB.addTextChangedListener(this);
                break;

            case R.id.cB: //チェリービッグ
                cB.removeTextChangedListener(this);
                //入力値を更新
                strCherryBig = textString;
                setProbability(strAloneBig,strCherryBig,BB,aB_Probability,cB_Probability,BB_Probability);
                if (StringUtils.isNotEmpty(strCherryBig)) {
                    strCherryBig = Integer.valueOf(strCherryBig).toString();
                    cB.setText(strCherryBig);
                    cB.setSelection(strCherryBig.length());
                } else {
                    cB.setText("0");
                    cB.setSelection(1);
                }

                // 保存処理
                mainApplication.setcB(strCherryBig);
                CreateXML.updateText(mainApplication,"cB",strCherryBig);

                cB.addTextChangedListener(this);
                break;

            case R.id.BB: //ビッグボーナス
                //入力値を更新
                strTotalBig = textString;
                int additionCount = Integer.parseInt(strTotalBig) + Integer.parseInt(strTotalReg);
                addition.setText(String.valueOf(additionCount));

                //総ゲーム数が0以外 かつ ボーナスが0以上だった場合
                if( ! strTotal.equals("0") && additionCount > 0){
                    double AdditionProbability = ind / additionCount;
                    addition_Probability.setText(setFormat(AdditionProbability));
                }
                break;

            case R.id.aR: //単独レギュラー
                aR.removeTextChangedListener(this);
                //入力値を更新
                strAloneReg = textString;

                setProbability(strAloneReg,strCherryReg,RB,aR_Probability,cR_Probability,RB_Probability);

                if (StringUtils.isNotEmpty(strAloneReg)) {
                    strAloneReg = Integer.valueOf(strAloneReg).toString();
                    aR.setText(strAloneReg);
                    aR.setSelection(strAloneReg.length());
                } else {
                    aR.setText("0");
                    aR.setSelection(1);
                }

                // 保存処理
                mainApplication.setaR(strAloneReg);
                CreateXML.updateText(mainApplication,"aR",strAloneReg);

                aR.addTextChangedListener(this);
                break;

            case R.id.cR: //チェリービッグ
                cR.removeTextChangedListener(this);
                //入力値を更新
                strCherryReg = textString;

                setProbability(strAloneReg,strCherryReg,RB,aR_Probability,cR_Probability,RB_Probability);

                if (StringUtils.isNotEmpty(strCherryReg)) {
                    strCherryReg = Integer.valueOf(strCherryReg).toString();
                    cR.setText(strCherryReg);
                    cR.setSelection(strCherryReg.length());
                } else {
                    cR.setText("0");
                    cR.setSelection(1);
                }

                // 保存処理
                mainApplication.setcR(strCherryReg);
                CreateXML.updateText(mainApplication,"cR",strCherryReg);

                cR.addTextChangedListener(this);
                break;

            case R.id.RB: //レギュラーボーナス
                //入力値を更新
                strTotalReg = textString;
                additionCount = Integer.parseInt(strTotalBig) + Integer.parseInt(strTotalReg);
                addition.setText(String.valueOf(additionCount));
                if( ! strTotal.equals("0") && additionCount > 0){
                    double AdditionProbability = ind / additionCount;
                    addition_Probability.setText(setFormat(AdditionProbability));
                }
                break;

            case R.id.ch: //非重複チェリー
                ch.removeTextChangedListener(this);
                //入力値を更新
                strCherry = textString;

                setProbability_ch_gr(strCherry,ch_Probability);

                if (StringUtils.isNotEmpty(strCherry)) {
                    strCherry = Integer.valueOf(strCherry).toString();
                    ch.setText(strCherry);
                    ch.setSelection(strCherry.length());
                } else {
                    ch.setText("0");
                    ch.setSelection(1);
                }

                // 保存処理
                mainApplication.setCh(strCherry);
                CreateXML.updateText(mainApplication,"ch",strCherry);

                ch.addTextChangedListener(this);
                break;

            case R.id.gr:
                gr.removeTextChangedListener(this);
                //入力値を更新
                strGr = textString;

                setProbability_ch_gr(strGr,gr_Probability);

                if (StringUtils.isNotEmpty(strGr)) {
                    strGr = Integer.valueOf(strGr).toString();
                    gr.setText(strGr);
                    gr.setSelection(strGr.length());
                } else {
                    gr.setText("0");
                    gr.setSelection(1);
                }

                // 保存処理
                mainApplication.setGr(strGr);
                CreateXML.updateText(mainApplication,"gr",strGr);

                gr.addTextChangedListener(this);
                break;
        }
    }

    public void setProbability_ch_gr(String smallRole,TextView tv){

        if(StringUtils.isEmpty(smallRole) || smallRole.equals("0") || smallRole.equals("00")){
            tv.setText(INIT_VALUE);
        } else {
            double ind = Double.parseDouble(individual.getText().toString());
            double sr = Double.parseDouble(smallRole);
            double sr_Probability = ind / sr;
            tv.setText(setFormat(sr_Probability));
        }
    }

    public void setCounterBlankProbabilityZero(EditText ed,TextView tv){

        String edString = ed.getText().toString();
        String individualString = individual.getText().toString();

        // テキストが空もしくは0だった場合は初期値を格納
        if(StringUtils.isEmpty(edString) || edString.equals("0")){
            tv.setText(INIT_VALUE);
        } else {
            double ind = Double.parseDouble(individualString);
            double cnt = Double.parseDouble(edString);
            double probability = ind / cnt;
            tv.setText(setFormat(probability));
        }
    }

    public void setProbabilityZero(){
        aB_Probability.setText(INIT_VALUE); cB_Probability.setText(INIT_VALUE); BB_Probability.setText(INIT_VALUE);
        aR_Probability.setText(INIT_VALUE); cR_Probability.setText(INIT_VALUE); RB_Probability.setText(INIT_VALUE);
        ch_Probability.setText(INIT_VALUE); gr_Probability.setText(INIT_VALUE); addition_Probability.setText(INIT_VALUE);
    }

    public void setProbability(String alone,String cherry,EditText bonus,TextView tv1,TextView tv2,TextView tv3){

        String bonusValue = "0";
        String tv1Value = INIT_VALUE;
        String tv2Value = INIT_VALUE;
        String tv3Value = INIT_VALUE;
        int aloneValue = 0;
        int cherryValue = 0;

        if(StringUtils.isNotEmpty(alone)) {
            aloneValue = Integer.parseInt(alone);
        }
        if(StringUtils.isNotEmpty(cherry)){
            cherryValue = Integer.parseInt(cherry);
        }

            //単独カウンターが空、かつ、チェリー重複カウンターが０
        if(alone.isEmpty() && cherryValue > 0){
            double ind = Integer.parseInt(individual.getText().toString());
            double probability = ind / cherryValue;

            bonusValue = Integer.valueOf(cherry).toString();
            tv2Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０、かつ、チェリー重複カウンターが０より大きい
        } else if(aloneValue == 0 && cherryValue > 0){
            bonusValue = Integer.valueOf(cherry).toString();
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(cherry);
            double probability = ind / cnt;

            tv2Value = setFormat(probability);
            tv3Value = setFormat(probability);
            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが空
        } else if(aloneValue > 0 && cherry.isEmpty()){
            bonusValue = Integer.valueOf(alone).toString();
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０
        } else if(aloneValue > 0 && cherryValue == 0){
            bonusValue = Integer.valueOf(alone).toString();
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０より大きい
        } else if(aloneValue > 0 && cherryValue > 0) {
            double ind = Integer.parseInt(individual.getText().toString());
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
    private String setFormat(double probability){
        return "1/" + String.format("%.2f", probability);
    }
}
