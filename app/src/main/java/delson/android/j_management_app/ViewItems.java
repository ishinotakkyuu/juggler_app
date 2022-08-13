package delson.android.j_management_app;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewItems {

    //詳細画面のゲーム数関係
    public static EditText[] getDetailGameTextItems() {
        return new EditText[]{
                DataDetail.eDetailEditGames.get(0),
                DataDetail.eDetailEditGames.get(1),
                DataDetail.eDetailEditGames.get(2)
        };
    }

    //カウンター関係
    public static EditText[] getCounterTextItems() {
        return new EditText[]{
                MainCounterActivity.eCounterEditRolls.get(0),
                MainCounterActivity.eCounterEditRolls.get(1),
                MainCounterActivity.eCounterEditRolls.get(2),
                MainCounterActivity.eCounterEditRolls.get(3),
                MainCounterActivity.eCounterEditRolls.get(4),
                MainCounterActivity.eCounterEditRolls.get(5),
                MainCounterActivity.eCounterEditRolls.get(6),
                MainCounterActivity.eCounterEditRolls.get(7),
                MainCounterActivity.eCounterEditRolls.get(8)
        };
    }

    //詳細画面のカウンター関係
    public static EditText[] getDetailCounterTextItems() {
        return new EditText[]{
                DataDetail.eDetailEditRolls.get(0),
                DataDetail.eDetailEditRolls.get(1),
                DataDetail.eDetailEditRolls.get(2),
                DataDetail.eDetailEditRolls.get(3),
                DataDetail.eDetailEditRolls.get(4),
                DataDetail.eDetailEditRolls.get(5),
                DataDetail.eDetailEditRolls.get(6),
                DataDetail.eDetailEditRolls.get(7),
                DataDetail.eDetailEditRolls.get(8)
        };
    }

    //カウンター関係(編集可能なもの)
    public static EditText[] getCounterEditTextItems() {
        return new EditText[]{
                MainCounterActivity.eCounterEditRolls.get(0),
                MainCounterActivity.eCounterEditRolls.get(1),
                MainCounterActivity.eCounterEditRolls.get(3),
                MainCounterActivity.eCounterEditRolls.get(4),
                MainCounterActivity.eCounterEditRolls.get(6),
                MainCounterActivity.eCounterEditRolls.get(7),
        };
    }

    //詳細画面のカウンター関係(編集可能なもの)
    public static EditText[] getDetailCounterEditTextItems() {
        return new EditText[]{
                DataDetail.eDetailEditRolls.get(0),
                DataDetail.eDetailEditRolls.get(1),
                DataDetail.eDetailEditRolls.get(3),
                DataDetail.eDetailEditRolls.get(4),
                DataDetail.eDetailEditRolls.get(6),
                DataDetail.eDetailEditRolls.get(7)
        };
    }

    //確率関係
    public static TextView[] getProbabilityTextItems() {
        return new TextView[]{
                MainCounterActivity.eCounterTextProbability.get(0),
                MainCounterActivity.eCounterTextProbability.get(1),
                MainCounterActivity.eCounterTextProbability.get(2),
                MainCounterActivity.eCounterTextProbability.get(3),
                MainCounterActivity.eCounterTextProbability.get(4),
                MainCounterActivity.eCounterTextProbability.get(5),
                MainCounterActivity.eCounterTextProbability.get(6),
                MainCounterActivity.eCounterTextProbability.get(7),
                MainCounterActivity.eCounterTextProbability.get(8)
        };
    }

    public static TextView[] getDetailProbabilityTextItems() {
        return new TextView[]{
                DataDetail.eDetailTextProbability.get(0),
                DataDetail.eDetailTextProbability.get(1),
                DataDetail.eDetailTextProbability.get(2),
                DataDetail.eDetailTextProbability.get(3),
                DataDetail.eDetailTextProbability.get(4),
                DataDetail.eDetailTextProbability.get(5),
                DataDetail.eDetailTextProbability.get(6),
                DataDetail.eDetailTextProbability.get(7),
                DataDetail.eDetailTextProbability.get(8)
        };
    }

    //詳細画面のボタン関係
    public static Button[] getDetailCounterButtonItems() {
        return new Button[]{
                DataDetail.bDetailButton.get(0),
                DataDetail.bDetailButton.get(1),
                DataDetail.bDetailButton.get(2),
                DataDetail.bDetailButton.get(3),
                DataDetail.bDetailButton.get(4),
                DataDetail.bDetailButton.get(5),
                DataDetail.bDetailButton.get(6),
                DataDetail.bDetailButton.get(7),
                DataDetail.bDetailButton.get(8)
        };
    }


    //配列結合
    public static EditText[] joinEditTexts(EditText[] items01, EditText[] items02) {
        EditText[] newItems = new EditText[items01.length + items02.length];
        System.arraycopy(items01, 0, newItems, 0, items01.length);
        System.arraycopy(items02, 0, newItems, items01.length, items02.length);
        return newItems;
    }

    //カラー設定(EditText型)
    public static void setEditTextColor(EditText[] items, int textColor, Typeface typeface) {
        for (EditText item : items) {
            item.setTextColor(textColor);
            item.setTypeface(typeface);
        }
    }

    //カラー設定(TextView型)
    public static void setTextViewColor(TextView[] items, int textColor, Typeface typeface) {
        for (TextView item : items) {
            item.setTextColor(textColor);
            item.setTypeface(typeface);
        }
    }

    //フォーカス設定(EditText型)
    public static void setEditTextFocus(EditText[] items, boolean focusable, boolean focusableInTouchMode, Drawable background) {
        for (EditText item : items) {
            item.setFocusable(focusable);
            item.setFocusableInTouchMode(focusableInTouchMode);
            item.setBackground(background);
        }
    }

    //詳細画面のフォーカス設定(EditText型)
    public static void setDetailEditTextFocus(EditText[] items, boolean focusable, boolean focusableInTouchMode, Drawable background) {
        for (EditText item : items) {
            item.setFocusable(focusable);
            item.setFocusableInTouchMode(focusableInTouchMode);
            item.setBackground(background);
        }
    }

    //詳細画面のボタン操作設定(Button型)
    public static void setDetailButtonEnabledTrue(Button[] items, boolean enabled) {
        for (Button item : items) {
            item.setEnabled(enabled);
        }
    }


}
