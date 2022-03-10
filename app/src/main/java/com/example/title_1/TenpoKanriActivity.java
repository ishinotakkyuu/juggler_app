package com.example.title_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ibm.icu.text.Transliterator;
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
import java.util.ArrayList;
import java.util.List;


public class TenpoKanriActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<SampleListItem> listItems = new ArrayList<>();
    SampleListAdapter adapter;
    int count;
    EditText editText;

    // フォーカス関係
    ConstraintLayout layout;
    InputMethodManager inputMethodManager;

    // 共有データ
    MainApplication mainApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenpo_kanri);

        this.mainApplication = (MainApplication) this.getApplication();
        setSampleListItem();

        //アクションバーの表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //    actionBar.setTitle("店 舗 管 理");
            actionBar.hide();
        }

        // フォーカス関係
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        layout = findViewById(R.id.main_layout);

        ListView listView = findViewById(R.id.listview);
        editText = findViewById(R.id.editText);

        //出力結果をリストビューに表示
        adapter = new SampleListAdapter(this, R.layout.tenpolist_item, listItems);
        listView.setAdapter(adapter);

        setStoreCount();

        listView.setOnItemClickListener(this);

        actionListenerFocusOut();

    }

    public void addText(View view) {
        //「追加」ボタンに設定
        // 店舗名入力 ⇒ 追加ボタン押下 ⇒ アクティビティ下部のリストビューに追加した店舗名のセルがセットされる。

        //EditTextに入力された文字列を取得
        String store = getEditText();
        ListView listView = findViewById(R.id.listview);
        boolean errorFlag = true;

        // EditTextに文字が入力されていない状態で「追加」ボタンを押しても何も処理されない。
        if (!store.isEmpty()) {

            // 文字列前後の空白は削除した新たな文字列を全て半角にしたものをnewStoreに入れる。
            Transliterator fullToHalf = Transliterator.getInstance("Fullwidth-Halfwidth");
            String newStore = fullToHalf.transliterate(store.replaceFirst("^[\\h]+", "").replaceFirst("[\\h]+$", ""));

            // 20店舗対応
            if (listItems.size() >= 20) {
                //トーストを表示
                Toast toast = Toast.makeText(this, "店舗数が上限に達しました", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            //R4.1.31現在、画像(bmp)は使用していない
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.seven);
            SampleListItem item = new SampleListItem(bmp, newStore);

            //空白チェック
            if(StringUtils.isBlank(newStore)){
                errorFlag = false;
                Toast toast = Toast.makeText(this, "店舗名が空白のため登録できません", Toast.LENGTH_LONG);
                toast.show();
            }

            // 重複チェック
            for(int i = 0; i < listItems.size(); i++){
                if((listItems.get(i).getTitle()).equals(item.getTitle())){
                    errorFlag = false;
                    Toast toast = Toast.makeText(this, "すでに登録されている店舗名です", Toast.LENGTH_LONG);
                    toast.show();
                    break;
                }
            }

            if(errorFlag){
                // 店舗名の重複がなければ配列に項目をセットし、内部ストレージにも保存
                listItems.add(item);
                setMainApplication(listItems);

                //出力結果をリストビューに表示
                adapter = new SampleListAdapter(this, R.layout.tenpolist_item, listItems);
                listView.setAdapter(adapter);

                // 登録店舗数の値を更新して、最後にトーストを表示
                setStoreCount();
                Toast toast = Toast.makeText(this, newStore + "が追加されました", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        focusOut();
    }

    //リストビュー内の項目をタップした時のイベント処理
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        ListView listView = (ListView) parent;  //タップされたリストビューを取得
        SampleListItem item = (SampleListItem) listView.getItemAtPosition(position);
        String beforeName = item.getTitle();

        String[] selectList = {"店舗名編集", "削除", "キャンセル"};
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setItems(selectList, (dialog, idx) -> {
            if (idx == 0) {  //店舗名編集
                storeReName(beforeName, listView, position);
            } else if (idx == 1) {   //削除
                deleteList(item, listView);
            } //キャンセルを押したら何もしない
        });
        alert.show();
        focusOut();
    }

    public void storeReName(String beforeName, ListView listView, int position) {
        final EditText editText = new EditText(this);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(15);
        editText.setFilters(filters);

        editText.setHint("編集前：" + beforeName);
        new AlertDialog.Builder(this)
                .setTitle("店舗名編集")
                .setMessage("新しい店舗名を入力してください")
                .setView(editText)
                .setPositiveButton("変更", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newStoreName = editText.getText().toString();
                        ArrayAdapter<SampleListItem> adapter = (ArrayAdapter<SampleListItem>) listView.getAdapter();
                        SampleListItem item = (SampleListItem) listView.getItemAtPosition(position);
                        item.setmTitle(newStoreName);
                        adapter.notifyDataSetChanged();
                        setMainApplication(listItems);
                        Toast toast = Toast.makeText(TenpoKanriActivity.this, beforeName + "を" + newStoreName + "に変更しました", Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

    public void deleteList(SampleListItem item, ListView listView) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.main_gogo)
                .setTitle("登録店舗削除")

                .setMessage("「" + item.getTitle() + "」をリストから削除してよろしいですか？")

                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayAdapter<SampleListItem> adapter = (ArrayAdapter<SampleListItem>) listView.getAdapter();
                        adapter.remove(item);
                        listItems.remove(item);
                        setMainApplication(listItems);
                        TextView tourokuten = findViewById(R.id.tourokuten_text);
                        tourokuten.setText("登録店舗数：" + getStoreCount() + "件");
                        Toast toast = Toast.makeText(TenpoKanriActivity.this, "削除しました", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton("いいえ", null)
                .show();
    }

    private void setSampleListItem() {

        // 20店舗対応
        String storeItems[] = CommonFeature.getStoreItems(mainApplication);

        for (int i = 0; i < storeItems.length; i++) {

            if (!storeItems[i].equals("null")) {
                // わからないがbmpをいれる
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.seven);
                // xmlの上から順に店舗名を入れる
                SampleListItem item = new SampleListItem(bmp, storeItems[i]);
                listItems.add(item);
            }
        }
    }

    private void setMainApplication(ArrayList<SampleListItem> listItems) {

        String storeName;
        SampleListItem item;
        // 20店舗対応
        int storeLimit = 20;

        for (int i = 0; i < storeLimit; i++) {

            if (i < listItems.size()) {
                item = listItems.get(i);
                storeName = item.getTitle();
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

    private void setStoreCount() {
        //登録店舗数の件数をセット
        TextView storeCount = findViewById(R.id.tourokuten_text);
        storeCount.setText("登録店舗数：" + getStoreCount() + "件");
    }

    public String getEditText() {    //EditTextに入力された文字列を取得用メソッド
        String newStore = editText.getText().toString();    //EditTextの文字列取得
        editText.getEditableText().clear();     //EditTextの入力文字列をクリア
        return newStore;
    }

    public int getStoreCount() {     //リストビューにセットされた項目(店舗)数を取得して返す
        count = adapter.getCount();
        return count;
    }

    // onTouchEventではアクテビティにしか反応せず、リストビュー上をタッチしても意味がない
    // そこでリストビューをタッチしてもフォーカスが外れるようにするには、dispatchTouchEventを使う
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
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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