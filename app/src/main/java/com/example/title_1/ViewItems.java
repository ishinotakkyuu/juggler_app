package com.example.title_1;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewItems {

    //ゲーム数関係
    public static EditText[] getGameTextItems() {
        return new EditText[]{
                MainCounterActivity.eTotalGames,
                MainCounterActivity.eStartGames,
                MainCounterActivity.eIndividualGames
        };
    }

    //詳細画面のゲーム数関係
    public static EditText[] getDetailGameTextItems() {
        return new EditText[]{
                DataDetail.eTotalGames,
                DataDetail.eStartGames,
                DataDetail.eIndividualGames
        };
    }

    //カウンター関係
    public static EditText[] getCounterTextItems() {
        return new EditText[]{
                MainCounterActivity.eSingleBig,
                MainCounterActivity.eCherryBig,
                MainCounterActivity.eTotalBig,
                MainCounterActivity.eSingleReg,
                MainCounterActivity.eCherryReg,
                MainCounterActivity.eTotalReg,
                MainCounterActivity.eCherry,
                MainCounterActivity.eGrape,
                MainCounterActivity.eTotalBonus
        };
    }

    //詳細画面のカウンター関係
    public static EditText[] getDetailCounterTextItems() {
        return new EditText[]{
                DataDetail.eSingleBig,
                DataDetail.eCherryBig,
                DataDetail.eTotalBig,
                DataDetail.eSingleReg,
                DataDetail.eCherryReg,
                DataDetail.eTotalReg,
                DataDetail.eCherry,
                DataDetail.eGrape,
                DataDetail.eTotalBonus
        };
    }

    //カウンター関係(編集可能なもの)
    public static EditText[] getCounterEditTextItems() {
        return new EditText[]{
                MainCounterActivity.eSingleBig,
                MainCounterActivity.eCherryBig,
                MainCounterActivity.eSingleReg,
                MainCounterActivity.eCherryReg,
                MainCounterActivity.eCherry,
                MainCounterActivity.eGrape
        };
    }

    //詳細画面のカウンター関係(編集可能なもの)
    public static EditText[] getDetailCounterEditTextItems() {
        return new EditText[]{
                DataDetail.eSingleBig,
                DataDetail.eCherryBig,
                DataDetail.eSingleReg,
                DataDetail.eCherryReg,
                DataDetail.eCherry,
                DataDetail.eGrape
        };
    }

    //確率関係
    public static TextView[] getProbabilityTextItems() {
        return new TextView[]{
                MainCounterActivity.tSingleBigProbability,
                MainCounterActivity.tCherryBigProbability,
                MainCounterActivity.tTotalBigProbability,
                MainCounterActivity.tSingleRegProbability,
                MainCounterActivity.tCherryRegProbability,
                MainCounterActivity.tTotalRegProbability,
                MainCounterActivity.tCherryProbability,
                MainCounterActivity.tGrapeProbability,
                MainCounterActivity.tTotalBonusProbability
        };
    }

    public static TextView[] getDetailProbabilityTextItems() {
        return new TextView[]{
                DataDetail.tSingleBigProbability,
                DataDetail.tCherryBigProbability,
                DataDetail.tTotalBigProbability,
                DataDetail.tSingleRegProbability,
                DataDetail.tCherryRegProbability,
                DataDetail.tTotalRegProbability,
                DataDetail.tCherryProbability,
                DataDetail.tGrapeProbability,
                DataDetail.tTotalBonusProbability
        };
    }

    //詳細画面のボタン関係
    public static Button[] getDetailCounterButtonItems() {
        return new Button[]{
                DataDetail.bSingleBig,
                DataDetail.bCherryBig,
                DataDetail.bTotalBig,
                DataDetail.bSingleReg,
                DataDetail.bCherryReg,
                DataDetail.bTotalReg,
                DataDetail.bCherry,
                DataDetail.bGrape,
                DataDetail.bTotalBonus
        };
    }


    //配列結合
    public static EditText[] joinEditTexts(EditText[] items01,EditText[] items02) {
        EditText[] newItems = new EditText[items01.length + items02.length];
        System.arraycopy(items01, 0, newItems, 0, items01.length);
        System.arraycopy(items02, 0, newItems, items01.length, items02.length);
        return newItems;
    }

    //カラー設定(EditText型)
    public static void setEditTextColor(EditText[] items, int textColor, Typeface typeface) {
        for (EditText item: items){
            item.setTextColor(textColor);
            item.setTypeface(typeface);
        }
    }

    //カラー設定(TextView型)
    public static void setTextViewColor(TextView[] items, int textColor, Typeface typeface) {
        for (TextView item: items){
            item.setTextColor(textColor);
            item.setTypeface(typeface);
        }
    }

    //フォーカス設定(EditText型)
    public static void setEditTextFocus(EditText[] items, boolean focusable, boolean focusableInTouchMode, Drawable background) {
        for (EditText item: items){
            item.setFocusable(focusable);
            item.setFocusableInTouchMode(focusableInTouchMode);
            item.setBackground(background);
        }
    }

    //詳細画面のフォーカス設定(EditText型)
    public static void setDetailEditTextFocus(EditText[] items, boolean focusable, boolean focusableInTouchMode, Drawable background) {
        for (EditText item: items){
            item.setFocusable(focusable);
            item.setFocusableInTouchMode(focusableInTouchMode);
            item.setBackground(background);
        }
    }

    //詳細画面のボタン操作設定(Button型)
    public static void setDetailButtonEnabledTrue(Button[] items, boolean enabled) {
        for (Button item: items){
            item.setEnabled(enabled);
        }
    }



}
