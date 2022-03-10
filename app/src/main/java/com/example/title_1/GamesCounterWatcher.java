package com.example.title_1;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//-----------------------------------------------------------------------------------------------
//カウンター機能の制御【メイン処理】
//-----------------------------------------------------------------------------------------------
public class GamesCounterWatcher implements TextWatcher {

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
    static MainApplication mainApplication;

    private TextView view;

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
    @Override
    public void afterTextChanged(Editable s) {

        //TextWatcherを設定したどのViewが操作されたのか、switchを使って振り分ける
        switch (view.getId()){

            //総ゲーム数の場合
            case R.id.total_game:

                //無限ループを回避するために、テキストウォッチャーを解除する
                total.removeTextChangedListener(this);

                //総ゲーム数が空だったら個人ゲーム数に０をセット
                //空の状態でsetText(Integer.valueOf(s.toString()).toString())をやるとクラッシュしちゃう
                if (s.toString().isEmpty()) {
                    individual.setText("0");
                }

                //総ゲーム数が空ではなかった場合
                if (!s.toString().isEmpty()) {
                    //数値変換して文字型に再変換することで０頭を回避
                    total.setText(Integer.valueOf(s.toString()).toString());
                    total.setSelection(total.getText().length());
                    //打ち始めゲーム数が空だった場合
                    if (start.getText().toString().isEmpty()) {
                        //０が入力されていたら、次に入力された数値で上書きする
                        //個人ゲーム数に総ゲーム数をセット
                        int TotalGame = Integer.parseInt(s.toString());
                        individual.setText(String.valueOf(TotalGame));
                        //打ち始めゲーム数に数値が入力されていたら、総ゲーム数-打ち始めゲーム数を算出
                    } else {
                        int totalGame = Integer.parseInt(s.toString());
                        int startGame = Integer.parseInt(start.getText().toString());
                        int individualGame = totalGame - startGame;
                        //差がマイナスだったら０をセット
                        if (individualGame >= 0) {
                            individual.setText(String.valueOf(individualGame));
                        } else {
                            individual.setText("0");
                        }
                    }
                }

                String totalValue = total.getText().toString();
                if(!totalValue.isEmpty()) {
                    // 保存処理
                    mainApplication.setTotal(totalValue);
                    CreateXML.updateText(mainApplication,"total",totalValue);
                }

                //テキストウォッチャーを元に戻す
                total.addTextChangedListener(this);
                break;

            //打ち始めゲーム数の場合
            case R.id.start_game:
                start.removeTextChangedListener(this);
                //打ち始めゲーム数が空の場合
                if(s.toString().isEmpty()){
                    //総ゲーム数が空なら個人ゲーム数に０をセット
                    if(total.getText().toString().isEmpty()){
                        individual.setText("0");
                        //総ゲーム数に数値が入っていたら、個人ゲーム数に総ゲーム数をセット
                    } else {
                        int totalGame = Integer.parseInt(total.getText().toString());
                        individual.setText(String.valueOf(totalGame));
                    }
                }
                //打ち始めゲーム数が空ではない場合
                if( ! s.toString().isEmpty()){
                    start.setText(Integer.valueOf(s.toString()).toString());
                    start.setSelection(start.getText().length());
                    //総ゲーム数が空だった場合
                    if(total.getText().toString().isEmpty()){
                        individual.setText("0");
                    } else {
                        int totalGame = Integer.parseInt(total.getText().toString());
                        int startGame = Integer.parseInt(s.toString());
                        int individualGame = totalGame - startGame;
                        if(individualGame >= 0){
                            individual.setText(String.valueOf(individualGame));
                        } else {
                            individual.setText("0");
                        }
                    }
                }
                String startValue = start.getText().toString();
                if(!startValue.isEmpty()) {
                    // 保存処理
                    mainApplication.setStart(startValue);
                    CreateXML.updateText(mainApplication,"start",startValue);
                }

                start.addTextChangedListener(this);
                break;

            //個人ゲーム数が変更されたら各役物の確率も変更
            case R.id.individual_game:
                if (s.toString().equals("0")) {
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
                view.removeTextChangedListener(this);
                String strAloneBig = s.toString();
                String strCherryBig = view.getText().toString();
                setProbability(strAloneBig,strCherryBig,BB,aB_Probability,cB_Probability,BB_Probability);

                //空の状態でsetText(Integer.valueOf(s.toString()).toString())をやるとクラッシュしちゃう
                if (!s.toString().isEmpty()) {
                    aB.setText(Integer.valueOf(s.toString()).toString());
                    aB.setSelection(aB.getText().length());
                }
                String aBValue = aB.getText().toString();
                // 保存処理
                mainApplication.setaB(aBValue);
                CreateXML.updateText(mainApplication,"aB",aBValue);

                aB.addTextChangedListener(this);
                break;

            case R.id.cB: //チェリービッグ
                cB.removeTextChangedListener(this);
                strAloneBig = aB.getText().toString();
                strCherryBig = s.toString();
                setProbability(strAloneBig,strCherryBig,BB,aB_Probability,cB_Probability,BB_Probability);
                if (!s.toString().isEmpty()) {
                    cB.setText(Integer.valueOf(s.toString()).toString());
                    cB.setSelection(cB.getText().length());
                }
                String cBValue = cB.getText().toString();
                // 保存処理
                mainApplication.setcB(cBValue);
                CreateXML.updateText(mainApplication,"cB",cBValue);

                cB.addTextChangedListener(this);
                break;

            case R.id.BB: //ビッグボーナス
                String totalBig = s.toString();
                String totalReg = RB.getText().toString();
                int additionCount = Integer.parseInt(totalBig) + Integer.parseInt(totalReg);
                addition.setText(String.valueOf(additionCount));
                if( ! total.getText().toString().equals("0")){
                    double ind = Integer.parseInt(individual.getText().toString());
                    if((double) additionCount > 0) {
                        double AdditionProbability = ind / (double) additionCount;
                        addition_Probability.setText(String.valueOf("1/" + String.format("%.2f", AdditionProbability)));
                    } else {
                        addition_Probability.setText("1/0.00");
                    }
                } else {
                    addition_Probability.setText("1/0.00");
                }
                break;

            case R.id.aR: //単独レギュラー
                aR.removeTextChangedListener(this);
                String strAloneReg = s.toString();
                String strCherryReg = cR.getText().toString();
                setProbability(strAloneReg,strCherryReg,RB,aR_Probability,cR_Probability,RB_Probability);
                if (!s.toString().isEmpty()) {
                    aR.setText(Integer.valueOf(s.toString()).toString());
                    aR.setSelection(aR.getText().length());
                }
                String aRValue = aR.getText().toString();
                // 保存処理
                mainApplication.setaR(aRValue);
                CreateXML.updateText(mainApplication,"aR",aRValue);

                aR.addTextChangedListener(this);
                break;

            case R.id.cR: //チェリービッグ
                cR.removeTextChangedListener(this);
                strAloneReg = aR.getText().toString();
                strCherryReg = s.toString();
                setProbability(strAloneReg,strCherryReg,RB,aR_Probability,cR_Probability,RB_Probability);
                if (!s.toString().isEmpty()) {
                    cR.setText(Integer.valueOf(s.toString()).toString());
                    cR.setSelection(cR.getText().length());
                }
                String cRValue = cR.getText().toString();
                // 保存処理
                mainApplication.setcR(cRValue);
                CreateXML.updateText(mainApplication,"cR",cRValue);

                cR.addTextChangedListener(this);
                break;

            case R.id.RB: //レギュラーボーナス
                totalBig = MainCounterActivity.BB.getText().toString();
                totalReg = s.toString();
                additionCount = Integer.parseInt(totalBig) + Integer.parseInt(totalReg);
                addition.setText(String.valueOf(additionCount));
                if( ! total.getText().toString().equals("0")){
                    double ind = Integer.parseInt(individual.getText().toString());
                    if(additionCount > 0) {
                        double AdditionProbability = ind / additionCount;
                        addition_Probability.setText(String.valueOf("1/" + String.format("%.2f", AdditionProbability)));
                    } else {
                        addition_Probability.setText("1/0.00");
                    }
                } else {
                    addition_Probability.setText("1/0.00");
                }
                break;


            case R.id.ch: //非重複チェリー
                ch.removeTextChangedListener(this);
                String strCherry = s.toString();
                setProbability_ch_gr(strCherry,ch_Probability);
                if (!s.toString().isEmpty()) {
                    ch.setText(Integer.valueOf(s.toString()).toString());
                    ch.setSelection(ch.getText().length());
                }
                String chValue = ch.getText().toString();
                // 保存処理
                mainApplication.setCh(chValue);
                CreateXML.updateText(mainApplication,"ch",chValue);

                ch.addTextChangedListener(this);
                break;

            case R.id.gr:
                gr.removeTextChangedListener(this);
                String strGr = s.toString();
                setProbability_ch_gr(strGr,gr_Probability);
                if (!s.toString().isEmpty()) {
                    gr.setText(Integer.valueOf(s.toString()).toString());
                    gr.setSelection(gr.getText().length());
                }
                String grValue = gr.getText().toString();
                // 保存処理
                mainApplication.setGr(grValue);
                CreateXML.updateText(mainApplication,"gr",grValue);

                gr.addTextChangedListener(this);
                break;
        }
    }

    public void setProbability_ch_gr(String smallRole,TextView tv){
        if(smallRole.isEmpty()) {
            tv.setText("1/0.00");
        } else if(smallRole.equals("0")) {
            tv.setText("1/0.00");
        } else {
            double ind = Integer.parseInt(individual.getText().toString());
            double sr = Integer.parseInt(smallRole);
            double sr_Probability = ind / sr;
            tv.setText(String.valueOf("1/" + String.format("%.2f", sr_Probability)));
        }
    }

    public void setCounterBlankProbabilityZero(EditText ed,TextView tv){
        if( ! ed.getText().toString().isEmpty() && ! ed.getText().toString().equals("0")){
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(ed.getText().toString());
            double probability = ind / cnt;
            tv.setText("1/" + String.format("%.2f", probability));
        } else {
            tv.setText("1/0.00");
        }
    }

    public void setProbabilityZero(){
        aB_Probability.setText("1/0.00"); cB_Probability.setText("1/0.00"); BB_Probability.setText("1/0.00");
        aR_Probability.setText("1/0.00"); cR_Probability.setText("1/0.00"); RB_Probability.setText("1/0.00");
        ch_Probability.setText("1/0.00"); gr_Probability.setText("1/0.00"); addition_Probability.setText("1/0.00");
    }

    public void setProbability(String alone,String cherry,EditText bonus,TextView tv1,TextView tv2,TextView tv3){
        //単独カウンターが空、かつ、チェリー重複カウンターが空
        if(alone.isEmpty() && cherry.isEmpty()){
            bonus.setText("0");
            tv1.setText("1/0.00"); tv2.setText("1/0.00"); tv3.setText("1/0.00");

            //単独カウンターが空、かつ、チェリー重複カウンターが０
        } else if(alone.isEmpty() && cherry.equals("0")){
            bonus.setText("0");
            tv1.setText("1/0.00"); tv2.setText("1/0.00"); tv3.setText("1/0.00");

            //単独カウンターが０、かつ、チェリー重複カウンターが空
        } else if(alone.equals("0") && cherry.isEmpty()){
            bonus.setText("0");
            tv1.setText("1/0.00"); tv2.setText("1/0.00"); tv3.setText("1/0.00");

            //単独カウンターが０、かつ、チェリー重複カウンターが０
        } else if(alone.equals("0") && cherry.equals("0")){
            bonus.setText("0");
            tv1.setText("1/0.00"); tv2.setText("1/0.00"); tv3.setText("1/0.00");

            //単独カウンターが空、かつ、チェリー重複カウンターが０より大きい
        } else if(alone.isEmpty() && Integer.parseInt(cherry) > 0){
            bonus.setText(Integer.valueOf(cherry).toString());
            tv1.setText("1/0.00");
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(cherry);
            double probability = ind / cnt;
            tv2.setText(String.valueOf("1/" + String.format("%.2f", probability)));
            tv3.setText(tv2.getText().toString());

            //単独カウンターが０、かつ、チェリー重複カウンターが０より大きい
        } else if(alone.equals("0") && Integer.parseInt(cherry) > 0){
            bonus.setText(Integer.valueOf(cherry).toString());
            tv1.setText("1/0.00");
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(cherry);
            double probability = ind / cnt;
            tv2.setText(String.valueOf("1/" + String.format("%.2f", probability)));
            tv3.setText(tv2.getText().toString());

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが空
        } else if(Integer.parseInt(alone) > 0 && cherry.isEmpty()){
            bonus.setText(Integer.valueOf(alone).toString());
            tv2.setText("1/0.00");
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1.setText(String.valueOf("1/" + String.format("%.2f", probability)));
            tv3.setText(tv1.getText().toString());

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０
        } else if(Integer.parseInt(alone) > 0 && cherry.equals("0")){
            bonus.setText(Integer.valueOf(alone).toString());
            tv2.setText("1/0.00");
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1.setText(String.valueOf("1/" + String.format("%.2f", probability)));
            tv3.setText(tv1.getText().toString());

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０より大きい
        } else {
            double ind = Integer.parseInt(individual.getText().toString());
            int aloneBonus = Integer.parseInt(alone);
            int cherryBonus = Integer.parseInt(cherry);
            int total_Bonus = aloneBonus + cherryBonus;
            bonus.setText(String.valueOf(total_Bonus));

            double cnt_alone = aloneBonus;
            double alone_Probability = ind / cnt_alone;
            tv1.setText(String.valueOf("1/" + String.format("%.2f", alone_Probability)));

            double cnt_cherry = cherryBonus;
            double cherry_Probability = ind / cnt_cherry;
            tv2.setText(String.valueOf("1/" + String.format("%.2f", cherry_Probability)));

            double cnt_totalBonus = total_Bonus;
            double total_Probability = ind / cnt_totalBonus;
            tv3.setText(String.valueOf("1/" + String.format("%.2f", total_Probability)));
        }
    }
}
