package delson.android.j_management_app;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;

public class DateDetailCounterWatcher implements TextWatcher {

    final String INIT_VALUE = "1/0.00";

    //ゲーム数関係
    EditText start = DataDetail.eDetailEditGames.get(0);
    EditText total = DataDetail.eDetailEditGames.get(1);
    EditText individual = DataDetail.eDetailEditGames.get(2);

    //カウンター関係
    EditText aB = DataDetail.eDetailEditRolls.get(0);
    EditText cB = DataDetail.eDetailEditRolls.get(1);
    EditText BB = DataDetail.eDetailEditRolls.get(2);
    EditText aR = DataDetail.eDetailEditRolls.get(3);
    EditText cR = DataDetail.eDetailEditRolls.get(4);
    EditText RB = DataDetail.eDetailEditRolls.get(5);
    EditText ch = DataDetail.eDetailEditRolls.get(6);
    EditText gr = DataDetail.eDetailEditRolls.get(7);
    EditText addition = DataDetail.eDetailEditRolls.get(8);

    //確率関係
    TextView aB_Probability = DataDetail.eDetailTextProbability.get(0);
    TextView cB_Probability = DataDetail.eDetailTextProbability.get(1);
    TextView BB_Probability = DataDetail.eDetailTextProbability.get(2);
    TextView aR_Probability = DataDetail.eDetailTextProbability.get(3);
    TextView cR_Probability = DataDetail.eDetailTextProbability.get(4);
    TextView RB_Probability = DataDetail.eDetailTextProbability.get(5);
    TextView ch_Probability = DataDetail.eDetailTextProbability.get(6);
    TextView gr_Probability = DataDetail.eDetailTextProbability.get(7);
    TextView addition_Probability = DataDetail.eDetailTextProbability.get(8);

    TextView view;

    public DateDetailCounterWatcher(TextView view) {
        this.view = view;
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
        switch (view.getId()) {

            //総ゲーム数の場合
            case R.id.eTotalGames:

                //無限ループを回避するために、テキストウォッチャーを解除する
                total.removeTextChangedListener(this);

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
                total.setText(strTotal);
                total.setSelection(strTotal.length());
                individual.setText(individualValue);

                // テキストウォッチャーを元に戻す
                total.addTextChangedListener(this);
                break;

            //打ち始めゲーム数の場合
            case R.id.eStartGames:

                start.removeTextChangedListener(this);

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
                start.setText(strStart);
                start.setSelection(strStart.length());
                individual.setText(individualValue);

                start.addTextChangedListener(this);
                break;

            //個人ゲーム数が変更されたら各役物の確率も変更
            case R.id.eIndividualGames:
                if (textString.equals("0")) {
                    setProbabilityZero();
                } else {
                    setCounterBlankProbabilityZero(aB, aB_Probability);
                    setCounterBlankProbabilityZero(cB, cB_Probability);
                    setCounterBlankProbabilityZero(BB, BB_Probability);
                    setCounterBlankProbabilityZero(aR, aR_Probability);
                    setCounterBlankProbabilityZero(cR, cR_Probability);
                    setCounterBlankProbabilityZero(RB, RB_Probability);
                    setCounterBlankProbabilityZero(ch, ch_Probability);
                    setCounterBlankProbabilityZero(gr, gr_Probability);
                    setCounterBlankProbabilityZero(addition, addition_Probability);
                }
                break;

            //--------------------------------------------------------------------------------------------------------------------
            //ここからボーナスカウンターボタン押下時及びカウンターを直接編集した時の挙動振り分け
            //--------------------------------------------------------------------------------------------------------------------

            case R.id.eSingleBig: //単独ビッグ

                aB.removeTextChangedListener(this);
                // 入力値を更新
                strAloneBig = textString;

                // 確率の再計算
                setProbability(strAloneBig, strCherryBig, BB, aB_Probability, cB_Probability, BB_Probability);

                // 0頭回避処理
                strAloneBig = Integer.valueOf(strAloneBig).toString();

                aB.setText(strAloneBig);
                aB.setSelection(strAloneBig.length());

                aB.addTextChangedListener(this);
                break;

            case R.id.eCherryBig: //チェリービッグ

                cB.removeTextChangedListener(this);
                //入力値を更新
                strCherryBig = textString;

                // 確率の再計算
                setProbability(strAloneBig, strCherryBig, BB, aB_Probability, cB_Probability, BB_Probability);

                // 0頭回避処理
                strCherryBig = Integer.valueOf(strCherryBig).toString();

                cB.setText(strCherryBig);
                cB.setSelection(strCherryBig.length());

                cB.addTextChangedListener(this);
                break;

            case R.id.eTotalBig: //ビッグボーナス

                //入力値を更新
                strTotalBig = textString;
                int additionCount = Integer.parseInt(strTotalBig) + Integer.parseInt(strTotalReg);
                addition.setText(String.valueOf(additionCount));

                //総ゲーム数が0以外 かつ ボーナスが0以上だった場合
                if (!strTotal.equals("0") && additionCount > 0) {
                    double AdditionProbability = ind / additionCount;
                    addition_Probability.setText(setFormat(AdditionProbability));
                }
                break;

            case R.id.eSingleReg: //単独レギュラー

                aR.removeTextChangedListener(this);
                //入力値を更新
                strAloneReg = textString;

                // 確率の再計算
                setProbability(strAloneReg, strCherryReg, RB, aR_Probability, cR_Probability, RB_Probability);

                // 0頭回避処理
                strAloneReg = Integer.valueOf(strAloneReg).toString();

                aR.setText(strAloneReg);
                aR.setSelection(strAloneReg.length());

                aR.addTextChangedListener(this);
                break;

            case R.id.eCherryReg: //チェリービッグ

                cR.removeTextChangedListener(this);
                //入力値を更新
                strCherryReg = textString;

                // 確率の再計算
                setProbability(strAloneReg, strCherryReg, RB, aR_Probability, cR_Probability, RB_Probability);

                // 0頭回避処理
                strCherryReg = Integer.valueOf(strCherryReg).toString();

                cR.setText(strCherryReg);
                cR.setSelection(strCherryReg.length());

                cR.addTextChangedListener(this);
                break;

            case R.id.eTotalReg: //レギュラーボーナス
                //入力値を更新
                strTotalReg = textString;
                additionCount = Integer.parseInt(strTotalBig) + Integer.parseInt(strTotalReg);
                addition.setText(String.valueOf(additionCount));
                if (!strTotal.equals("0") && additionCount > 0) {
                    double AdditionProbability = ind / additionCount;
                    addition_Probability.setText(setFormat(AdditionProbability));
                }
                break;

            case R.id.eCherry: //非重複チェリー

                ch.removeTextChangedListener(this);
                //入力値を更新
                strCherry = textString;

                // 確率の再計算
                setProbability_ch_gr(strCherry, ch_Probability);

                // 0頭回避処理
                strCherry = Integer.valueOf(strCherry).toString();

                ch.setText(strCherry);
                ch.setSelection(strCherry.length());

                ch.addTextChangedListener(this);
                break;

            case R.id.eGrape: //ぶどう

                gr.removeTextChangedListener(this);
                //入力値を更新
                strGr = textString;

                // 確率の再計算
                setProbability_ch_gr(strGr, gr_Probability);

                // 0頭回避処理
                strGr = Integer.valueOf(strGr).toString();

                gr.setText(strGr);
                gr.setSelection(strGr.length());

                gr.addTextChangedListener(this);
                break;
        }
    }

    public void setProbability_ch_gr(String smallRole, TextView tv) {

        if (StringUtils.isEmpty(smallRole) || smallRole.equals("0") || smallRole.equals("00")) {
            tv.setText(INIT_VALUE);
        } else {
            double ind = Double.parseDouble(individual.getText().toString());
            double sr = Double.parseDouble(smallRole);
            double sr_Probability = ind / sr;
            tv.setText(setFormat(sr_Probability));
        }
    }

    public void setCounterBlankProbabilityZero(EditText ed, TextView tv) {

        String edString = ed.getText().toString();
        String individualString = individual.getText().toString();

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
        aB_Probability.setText(INIT_VALUE);
        cB_Probability.setText(INIT_VALUE);
        BB_Probability.setText(INIT_VALUE);
        aR_Probability.setText(INIT_VALUE);
        cR_Probability.setText(INIT_VALUE);
        RB_Probability.setText(INIT_VALUE);
        ch_Probability.setText(INIT_VALUE);
        gr_Probability.setText(INIT_VALUE);
        addition_Probability.setText(INIT_VALUE);
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
            double ind = Integer.parseInt(individual.getText().toString());
            double probability = ind / cherryValue;

            bonusValue = Integer.valueOf(cherry).toString();
            tv2Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０、かつ、チェリー重複カウンターが０より大きい
        } else if (aloneValue == 0 && cherryValue > 0) {
            bonusValue = Integer.valueOf(cherry).toString();
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(cherry);
            double probability = ind / cnt;

            tv2Value = setFormat(probability);
            tv3Value = setFormat(probability);
            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが空
        } else if (aloneValue > 0 && cherry.isEmpty()) {
            bonusValue = Integer.valueOf(alone).toString();
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０
        } else if (aloneValue > 0 && cherryValue == 0) {
            bonusValue = Integer.valueOf(alone).toString();
            double ind = Integer.parseInt(individual.getText().toString());
            double cnt = Integer.parseInt(alone);
            double probability = ind / cnt;
            tv1Value = setFormat(probability);
            tv3Value = setFormat(probability);

            //単独カウンターが０より大きい、かつ、チェリー重複カウンターが０より大きい
        } else if (aloneValue > 0 && cherryValue > 0) {
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
    private String setFormat(double probability) {
        return "1/" + String.format("%.2f", probability);
    }
}