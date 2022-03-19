package com.example.title_1;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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


    //配列結合
    public static EditText[] joinEditTexts(EditText[] items01,EditText[] items02) {
        EditText[] newItems = new EditText[items01.length + items02.length];
        System.arraycopy(items01, 0, newItems, 0, items01.length);
        System.arraycopy(items02, 0, newItems, items01.length, items02.length);
        return newItems;
    }
    //カラー設定(EditText型)
    public static void setEditTextColer(EditText[] items, int textColor, Typeface typeface) {

        for (EditText item: items){
            item.setTextColor(textColor);
            item.setTypeface(typeface);
        }
    }
    //カラー設定(TextView型)
    public static void setTextViewColer(TextView[] items, int textColor, Typeface typeface) {

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
}
