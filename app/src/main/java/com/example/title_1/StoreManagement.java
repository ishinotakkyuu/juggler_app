package com.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.lang3.StringUtils;
import java.text.Normalizer;
import java.util.ArrayList;


public final class StoreManagement extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // 登録店舗名を入力するEditText
    EditText storeRegister;

    // 店舗数を表示するTextView
    TextView storeCounter;

    // 登録店舗を表示させるListView,登録店舗名を格納するArrayList,ListViewに登録店舗名をセットする時に使用するadapter
    ListView storeListView;
    ArrayList<String> items = new ArrayList<>();
    ListItemAdapter adapter;

    // フォーカス関係で使用
    ConstraintLayout layout;
    InputMethodManager inputMethodManager;

    // ListViewのどの要素がタップされたかを格納する変数
    int tappedPosition = 0;

    // 共有データ
    MainApplication mainApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main01_storemanagement01);

        //アクションバーの表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // フォーカス関係　他、レイアウトの各オブジェクトのfindViewById
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        layout = findViewById(R.id.main_layout);
        storeCounter = findViewById(R.id.StoreCount);
        storeListView = findViewById(R.id.StoreList);
        storeRegister = findViewById(R.id.EditStoreName);

        // 共有データに保存している店舗名を取得 ⇒ itemsに店舗名を格納 ⇒ adapterを介してListViewにセット
        this.mainApplication = (MainApplication) this.getApplication();
        setStorageListItem();
        adapter = new ListItemAdapter(this, R.layout.main01_storemanagement02_listitem, items);
        storeListView.setAdapter(adapter);

        // 登録店舗数の件数をセット
        storeCounter.setText("登録店舗数：" + adapter.getCount() + "件");

        // ListViewにリスナーを登録
        storeListView.setOnItemClickListener(this);

        // 登録店舗名を入力するEditTextにフォーカスを外すための機能をセット
        actionListenerFocusOut();

    }

    //「追加」ボタンに設定
    public void addText(View view) {

        boolean errorFlag = true;
        int size = items.size();

        // 入力された店舗名を取得
        String newStoreName = storeRegister.getText().toString();
        // Normalizerを使って文字列に含まれる全角スペースを半角に変換(同時に数値は半角・カタカナは全角に変換される)
        newStoreName = Normalizer.normalize(newStoreName,Normalizer.Form.NFKC);
        // 文字列前後の半角スペースを削除
        newStoreName = newStoreName.trim();

        // 文字列内に絵文字が含まれるか判定。ただし絵文字だけに限らず、日常的に使われない文字もはじく使用になってます。
        // R04.05.21時点で適当に入れた中国語・アラビア語・ロシア語は通過。ただ再起動すると文字化けするものもあった。また、古代文字ははじいた。
        for(int i = 0; i < newStoreName.length(); i++){
            char c = newStoreName.charAt(i);
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                Toast toast = Toast.makeText(this, "使用できない文字が含まれています", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }

        // 上記の文字列チェック後、正常値と判定されたらEditText内の入力文字列をクリアする。
        storeRegister.getEditableText().clear();

        // EditTextに文字が入力されていない状態で「追加」ボタンを押しても何も処理されない。
        if (!newStoreName.isEmpty()) {

            // 20店舗対応
            if (size >= 20) {
                //トーストを表示
                Toast toast = Toast.makeText(this, "店舗数が上限に達しました", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            //空白チェック
            if(StringUtils.isBlank(newStoreName)){
                errorFlag = false;
                Toast toast = Toast.makeText(this, "店舗名が空白のため登録できません", Toast.LENGTH_LONG);
                toast.show();
            }

            // 重複チェック
            for(int i = 0; i < size; i++){
                if((items.get(i)).equals(newStoreName)){
                    errorFlag = false;
                    Toast toast = Toast.makeText(this, "すでに登録されている店舗名です", Toast.LENGTH_LONG);
                    toast.show();
                    break;
                }
            }

            if(errorFlag){

                // 店舗名の重複がなければ配列に項目をセットし、内部ストレージにも保存
                items.add(newStoreName);
                setMainApplication(items);

                //出力結果をリストビューに表示
                adapter = new ListItemAdapter(this, R.layout.main01_storemanagement02_listitem, items);
                storeListView.setAdapter(adapter);

                // 登録店舗数の値を更新して、最後にトーストを表示
                storeCounter.setText("登録店舗数：" + adapter.getCount() + "件");
                Toast toast = Toast.makeText(this, newStoreName + "が追加されました", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        focusOut();
    }

    //リストビュー内の項目をタップした時のイベント処理
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        //タップされたリストビューを取得 ⇒ タップされた位置をtappedPositionに保持 ⇒ タップされた店舗名を取得
        ListView listView = (ListView) parent;
        tappedPosition = position;
        String tappedStoreName = (String)listView.getItemAtPosition(position);

        // selectListをセット
        String[] selectList = {"上に移動","下に移動","店舗名編集", "削除", "キャンセル"};

        // アラートダイアグラムの表示 ⇒ 各選択項目ごとの処理を行う
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setItems(selectList, (dialog, idx) -> {
            switch (idx){

                // 上に移動
                case 0:
                    if (tappedPosition > 0){
                        moveAbove(listView,tappedPosition,(String)listView.getItemAtPosition(tappedPosition - 1),tappedStoreName);
                    } else {
                        Toast toast = Toast.makeText(this, "一番上のアイテムのため移動できません", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    break;

                // 下に移動
                case 1:
                    if (tappedPosition < items.size() - 1){
                        moveBelow(listView,tappedPosition,(String)listView.getItemAtPosition(tappedPosition + 1),tappedStoreName);
                    } else {
                        Toast toast = Toast.makeText(this, "一番下のアイテムのため移動できません", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    break;

                // 店舗名編集
                case 2:
                    storeReName(tappedStoreName, listView, position);
                    break;

                // 削除
                case 3:
                    deleteList(tappedStoreName, listView);
                    break;

                // キャンセル
                case 4:
                    break;
            }
        });
        alert.show();
        focusOut();
    }

    public void moveAbove(ListView listView,int parentPosition,String upStoreName,String tappedStoreName){

        // 更新前のadapterを取得
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();

        // 入れ替えを行う
        items.set(parentPosition - 1,tappedStoreName);
        items.set(parentPosition,upStoreName);

        // adapterの更新
        adapter.notifyDataSetChanged();

        // 共有データの更新
        setMainApplication(items);

        // トーストの表示
        Toast toast = Toast.makeText(this, "上に移動しました", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void moveBelow(ListView listView,int parentPosition,String downStoreName, String tappedStoreName){

        // 更新前のadapterを取得
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();

        // 入れ替えを行う
        items.set(parentPosition + 1,tappedStoreName);
        items.set(parentPosition,downStoreName);

        // adapterの更新
        adapter.notifyDataSetChanged();

        // 共有データの更新
        setMainApplication(items);

        // トーストの表示
        Toast toast = Toast.makeText(this, "下に移動しました", Toast.LENGTH_SHORT);
        toast.show();
    }


    public void storeReName(String beforeName, ListView listView, int position) {

        final EditText editText = new EditText(this);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(20);
        editText.setFilters(filters);

        editText.setHint("編集前：" + beforeName);
        new AlertDialog.Builder(this)
                .setTitle("店舗名編集")
                .setMessage("新しい店舗名を入力してください")
                .setView(editText)
                .setPositiveButton("変更", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 更新後の店舗名を取得 ⇒ セットしてadapterを更新
                        String newStoreName = editText.getText().toString();
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();
                        items.set(position,newStoreName);
                        adapter.notifyDataSetChanged();

                        // 共有データも更新
                        setMainApplication(items);

                        // トースト表示
                        Toast toast = Toast.makeText(StoreManagement.this, beforeName + "を" + newStoreName + "に変更しました", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

    public void deleteList(String tappedStoreName, ListView listView) {
        new AlertDialog.Builder(this)
                .setTitle("登録店舗削除")

                .setMessage("「" + tappedStoreName + "」をリストから削除してよろしいですか？")

                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // adapterから店舗名を削除＆リストからも削除 ⇒ adapterを更新
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
                        adapter.remove(tappedStoreName);
                        items.remove(tappedStoreName);

                        // 共有データも更新
                        setMainApplication(items);

                        // 店舗数表示を更新してトースト表示
                        storeCounter.setText("登録店舗数：" + adapter.getCount() + "件");
                        Toast toast = Toast.makeText(StoreManagement.this, "削除しました", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton("いいえ", null)
                .show();
    }

    private void setStorageListItem() {

        // 20店舗対応
        String storeItems[] = CommonFeature.getStoreItems(mainApplication);
        int size = storeItems.length;

        for (int i = 0; i < size; i++) {

            if (!storeItems[i].equals("null")) {
                // xmlの上から順に店舗名を入れる
                String item = storeItems[i];
                this.items.add(item);
            }
        }
    }

    private void setMainApplication(ArrayList<String> items) {

        String storeName;
        // 20店舗対応
        int storeLimit = 20;
        int size = items.size();

        for (int i = 0; i < storeLimit; i++) {

            if (i < size) {
                storeName = items.get(i);
            } else {
                storeName = "null";
            }

            switch (i) {
                case 0:
                    mainApplication.setStore001(storeName);
                    CreateXML.updateText(mainApplication, "store001", storeName);
                    break;
                case 1:
                    mainApplication.setStore002(storeName);
                    CreateXML.updateText(mainApplication, "store002", storeName);
                    break;
                case 2:
                    mainApplication.setStore003(storeName);
                    CreateXML.updateText(mainApplication, "store003", storeName);
                    break;
                case 3:
                    mainApplication.setStore004(storeName);
                    CreateXML.updateText(mainApplication, "store004", storeName);
                    break;
                case 4:
                    mainApplication.setStore005(storeName);
                    CreateXML.updateText(mainApplication, "store005", storeName);
                    break;
                case 5:
                    mainApplication.setStore006(storeName);
                    CreateXML.updateText(mainApplication, "store006", storeName);
                    break;
                case 6:
                    mainApplication.setStore007(storeName);
                    CreateXML.updateText(mainApplication, "store007", storeName);
                    break;
                case 7:
                    mainApplication.setStore008(storeName);
                    CreateXML.updateText(mainApplication, "store008", storeName);
                    break;
                case 8:
                    mainApplication.setStore009(storeName);
                    CreateXML.updateText(mainApplication, "store009", storeName);
                    break;
                case 9:
                    mainApplication.setStore010(storeName);
                    CreateXML.updateText(mainApplication, "store010", storeName);
                    break;
                case 10:
                    mainApplication.setStore011(storeName);
                    CreateXML.updateText(mainApplication, "store011", storeName);
                    break;
                case 11:
                    mainApplication.setStore012(storeName);
                    CreateXML.updateText(mainApplication, "store012", storeName);
                    break;
                case 12:
                    mainApplication.setStore013(storeName);
                    CreateXML.updateText(mainApplication, "store013", storeName);
                    break;
                case 13:
                    mainApplication.setStore014(storeName);
                    CreateXML.updateText(mainApplication, "store014", storeName);
                    break;
                case 14:
                    mainApplication.setStore015(storeName);
                    CreateXML.updateText(mainApplication, "store015", storeName);
                    break;
                case 15:
                    mainApplication.setStore016(storeName);
                    CreateXML.updateText(mainApplication, "store016", storeName);
                    break;
                case 16:
                    mainApplication.setStore017(storeName);
                    CreateXML.updateText(mainApplication, "store017", storeName);
                    break;
                case 17:
                    mainApplication.setStore018(storeName);
                    CreateXML.updateText(mainApplication, "store018", storeName);
                    break;
                case 18:
                    mainApplication.setStore019(storeName);
                    CreateXML.updateText(mainApplication, "store019", storeName);
                    break;
                case 19:
                    mainApplication.setStore020(storeName);
                    CreateXML.updateText(mainApplication, "store020", storeName);
                    break;
            }
        }
    }

    // onTouchEventではアクテビティにしか反応せず、リストビュー上をタッチしても意味がない
    // そこでリストビューをタッチしてもフォーカスが外れるようにするdispatchTouchEventを使う
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        layout.requestFocus();
        return super.dispatchTouchEvent(event);
    }

    public void focusOut() {
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        layout.requestFocus();
    }

    public void actionListenerFocusOut() {
        storeRegister.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    focusOut();
                }
                return false;
            }
        });
    }
}