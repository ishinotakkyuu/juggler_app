package delson.android.j_management_app;

import android.text.InputFilter;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Z_PendingFeature {

    //ゲーム数関係
    EditText total; EditText start; EditText individual;

    //カウンター関係
    EditText aB; EditText cB; EditText BB;
    EditText aR; EditText cR; EditText RB;
    EditText ch; EditText gr; EditText addition;

    public void setInputFilter(){
        //文字制限６桁の設定
        List<InputFilter> filtersSix = new ArrayList<>(Arrays.asList(total.getFilters()));
        filtersSix.add((source, start, end, dest, dstart, dend) -> {
            //0が先頭のとき後続に数値を入れられない
            if ((dest.toString() + source).matches("^0\\d+")) {
                return "";
            }
            //それ以外は、普通に追加
            return source;
        });
        total.setFilters(filtersSix.toArray(new InputFilter[0]));
        start.setFilters(filtersSix.toArray(new InputFilter[0]));
        gr.setFilters(filtersSix.toArray(new InputFilter[0]));

        //文字制限５桁の設定
        List<InputFilter> filtersFive = new ArrayList<>(Arrays.asList(ch.getFilters()));
        filtersFive.add((source, start, end, dest, dstart, dend) -> {
            if ((dest.toString() + source).matches("^0\\d+")) {
                return "";
            }
            return source;
        });
        ch.setFilters(filtersFive.toArray(new InputFilter[0]));

        //文字制限４桁の設定
        List<InputFilter> filtersFour = new ArrayList<>(Arrays.asList(aB.getFilters()));
        filtersFour.add((source, start, end, dest, dstart, dend) -> {
            if ((dest.toString() + source).matches("^0\\d+")) {
                return "";
            }
            return source;
        });
        aB.setFilters(filtersFour.toArray(new InputFilter[0]));
        cB.setFilters(filtersFour.toArray(new InputFilter[0]));
        aR.setFilters(filtersFour.toArray(new InputFilter[0]));
        cR.setFilters(filtersFour.toArray(new InputFilter[0]));
    }
}
