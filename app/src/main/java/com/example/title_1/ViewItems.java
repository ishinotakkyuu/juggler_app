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
                MainCounterActivity.total,
                MainCounterActivity.start,
                MainCounterActivity.individual
        };
    }

    //詳細画面のゲーム数関係
    public static EditText[] getDetailGameTextItems() {
        return new EditText[]{
                DataDetail.total,
                DataDetail.start,
                DataDetail.individual
        };
    }

    //カウンター関係
    public static EditText[] getCounterTextItems() {
        return new EditText[]{
                MainCounterActivity.aB,
                MainCounterActivity.cB,
                MainCounterActivity.BB,
                MainCounterActivity.aR,
                MainCounterActivity.cR,
                MainCounterActivity.RB,
                MainCounterActivity.ch,
                MainCounterActivity.gr,
                MainCounterActivity.addition
        };
    }

    //詳細画面のカウンター関係
    public static EditText[] getDetailCounterTextItems() {
        return new EditText[]{
                DataDetail.aB,
                DataDetail.cB,
                DataDetail.BB,
                DataDetail.aR,
                DataDetail.cR,
                DataDetail.RB,
                DataDetail.ch,
                DataDetail.gr,
                DataDetail.addition
        };
    }

    //カウンター関係(編集可能なもの)
    public static EditText[] getCounterEditTextItems() {
        return new EditText[]{
                MainCounterActivity.aB,
                MainCounterActivity.cB,
                MainCounterActivity.aR,
                MainCounterActivity.cR,
                MainCounterActivity.ch,
                MainCounterActivity.gr
        };
    }

    //詳細画面のカウンター関係(編集可能なもの)
    public static EditText[] getDetailCounterEditTextItems() {
        return new EditText[]{
                DataDetail.aB,
                DataDetail.cB,
                DataDetail.aR,
                DataDetail.cR,
                DataDetail.ch,
                DataDetail.gr
        };
    }

    //確率関係
    public static TextView[] getProbabilityTextItems() {
        return new TextView[]{
                MainCounterActivity.aB_Probability,
                MainCounterActivity.cB_Probability,
                MainCounterActivity.BB_Probability,
                MainCounterActivity.aR_Probability,
                MainCounterActivity.cR_Probability,
                MainCounterActivity.RB_Probability,
                MainCounterActivity.ch_Probability,
                MainCounterActivity.gr_Probability,
                MainCounterActivity.addition_Probability
        };
    }

    public static TextView[] getDetailProbabilityTextItems() {
        return new TextView[]{
                DataDetail.aB_Probability,
                DataDetail.cB_Probability,
                DataDetail.BB_Probability,
                DataDetail.aR_Probability,
                DataDetail.cR_Probability,
                DataDetail.RB_Probability,
                DataDetail.ch_Probability,
                DataDetail.gr_Probability,
                DataDetail.addition_Probability
        };
    }

    //詳細画面のボタン関係
    public static Button[] getDetailCounterButtonItems() {
        return new Button[]{
                DataDetail.sbButton,
                DataDetail.cbButton,
                DataDetail.big,
                DataDetail.srButton,
                DataDetail.crButton,
                DataDetail.reg,
                DataDetail.chButton,
                DataDetail.grButton,
                DataDetail.bonus
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
