package delson.android.j_management_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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


public final class MainManagementStore extends AppCompatActivity implements AdapterView.OnItemClickListener, KeyboardVisibility.OnKeyboardVisibilityListener {

    // 登録店舗名を入力するEditText
    EditText eStoreName;

    // 店舗数を表示するTextView
    TextView tStoreCounter;

    // 登録店舗を表示させるListView,登録店舗名を格納するArrayList,ListViewに登録店舗名をセットする時に使用するadapter
    ListView lStoreName;
    ArrayList<String> Store_List_Items = new ArrayList<>();
    MainManagementStoreAdapter adapter;

    // フォーカス関係で使用
    Activity activity;
    ConstraintLayout mainLayout;
    InputMethodManager inputMethodManager;

    // ListViewのどの要素がタップされたかを格納する変数
    int tappedPosition = 0;

    // 共有データ
    MainApplication mainApplication;
    Context context;

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
        mainLayout = findViewById(R.id.main_layout);
        tStoreCounter = findViewById(R.id.tStoreCount);
        lStoreName = findViewById(R.id.lStore);
        eStoreName = findViewById(R.id.eStoreName);

        // 戻るボタン等でキーボードを非表示にされた時のフォーカス対応
        activity = this;
        KeyboardVisibility kv = new KeyboardVisibility(activity);
        kv.setKeyboardVisibilityListener(this);

        // 共有データに保存している店舗名を取得 ⇒ itemsに店舗名を格納 ⇒ adapterを介してListViewにセット
        this.mainApplication = (MainApplication) this.getApplication();
        context = getApplicationContext();
        setStorageListItem();
        adapter = new MainManagementStoreAdapter(this, R.layout.main01_storemanagement02_listitem, Store_List_Items);
        lStoreName.setAdapter(adapter);

        // 登録店舗数の件数をセット
        tStoreCounter.setText(getString(R.string.store_count,adapter.getCount()));

        // ListViewにリスナーを登録
        lStoreName.setOnItemClickListener(this);

        // 登録店舗名を入力するEditTextにフォーカスを外すための機能をセット
        actionListenerFocusOut();

        setFocusEvent();
    }

    //「追加」ボタンに設定
    public void addStore(View view) {

        boolean errorFlag = true;
        int Store_Items_Size = Store_List_Items.size();

        // 入力された店舗名を取得
        String addStoreName = eStoreName.getText().toString();
        // Normalizerを使って文字列に含まれる全角スペースを半角に変換(同時に数値は半角・カタカナは全角に変換される)
        addStoreName = Normalizer.normalize(addStoreName,Normalizer.Form.NFKC);
        // 文字列前後の半角スペースを削除
        addStoreName = addStoreName.trim();

        // 文字列内に絵文字が含まれるか判定。ただし絵文字だけに限らず、日常的に使われない文字もはじく使用になってます。
        // R04.05.21時点で適当に入れた中国語・アラビア語・ロシア語は通過。ただ再起動すると文字化けするものもあった。また、古代文字ははじいた。
        for(int i = 0; i < addStoreName.length(); i++){
            char c = addStoreName.charAt(i);
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                Toast toast = Toast.makeText(this, getString(R.string.not_use_string), Toast.LENGTH_LONG);
                toast.show();
                focusOut();
                return;
            }
        }

        // 上記の文字列チェック後、正常値と判定されたらEditText内の入力文字列をクリアする。
        eStoreName.getEditableText().clear();

        // EditTextに文字が入力されていない状態で「追加」ボタンを押しても何も処理されない。
        if (!addStoreName.isEmpty()) {

            // 20店舗対応
            if (Store_Items_Size >= 20) {
                //トーストを表示
                Toast toast = Toast.makeText(this, getString(R.string.upper_limit_store_toast), Toast.LENGTH_SHORT);
                toast.show();
                focusOut();
                return;
            }

            //空白チェック
            if(StringUtils.isBlank(addStoreName)){
                errorFlag = false;
                Toast toast = Toast.makeText(this, getString(R.string.not_blank_store_name), Toast.LENGTH_LONG);
                toast.show();
            }

            // 重複チェック
            for(int i = 0; i < Store_Items_Size; i++){
                if((Store_List_Items.get(i)).equals(addStoreName)){
                    errorFlag = false;
                    Toast toast = Toast.makeText(this, getString(R.string.already_store_name), Toast.LENGTH_LONG);
                    toast.show();
                    break;
                }
            }

            if(errorFlag){

                // 店舗名の重複がなければ配列に項目をセットし、内部ストレージにも保存
                Store_List_Items.add(addStoreName);
                setMainApplication(Store_List_Items);

                //出力結果をリストビューに表示
                adapter = new MainManagementStoreAdapter(this, R.layout.main01_storemanagement02_listitem, Store_List_Items);
                lStoreName.setAdapter(adapter);

                // 登録店舗数の値を更新して、最後にトーストを表示
                tStoreCounter.setText(getString(R.string.store_count,adapter.getCount()));
                Toast toast = Toast.makeText(this, getString(R.string.add_store,addStoreName),Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        focusOut();
    }

    //リストビュー内の項目をタップした時のイベント処理
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        focusOut();

        //タップされたリストビューを取得 ⇒ タップされた位置をtappedPositionに保持 ⇒ タップされた店舗名を取得
        ListView listView = (ListView) parent;
        tappedPosition = position;
        String tappedStoreItem = (String)listView.getItemAtPosition(position);

        // selectListをセット
        String[] Select_List = getResources().getStringArray(R.array.DIALOG_MENU);

        // アラートダイアグラムの表示 ⇒ 各選択項目ごとの処理を行う
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setItems(Select_List, (dialog, idx) -> {
            switch (idx){

                // 上に移動
                case 0:
                    if (tappedPosition > 0){
                        moveUpItem(listView,tappedPosition,(String)listView.getItemAtPosition(tappedPosition - 1),tappedStoreItem);
                        moveUpMemo(tappedPosition);
                    } else {
                        Toast toast = Toast.makeText(this, getString(R.string.not_up_toast), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    break;

                // 下に移動
                case 1:
                    if (tappedPosition < Store_List_Items.size() - 1){
                        moveDownItem(listView,tappedPosition,(String)listView.getItemAtPosition(tappedPosition + 1),tappedStoreItem);
                        moveDownMemo(tappedPosition);
                    } else {
                        Toast toast = Toast.makeText(this, getString(R.string.not_down_toast), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    break;

                // 店舗メモ
                case 2:
                    Intent intent = new Intent(getApplicationContext(),MainManagementStoreMemo.class);
                    intent.putExtra("tappedPosition",String.valueOf(tappedPosition));
                    startActivity(intent);
                    break;

                // 店舗名編集
                case 3:
                    storeReName(tappedStoreItem, listView, tappedPosition);
                    break;

                // 削除
                case 4:
                    deleteList(tappedStoreItem, listView, tappedPosition);
                    break;
            }
        });
        alert.show();
        focusOut();
    }

    public void moveUpItem(ListView listView, int parentPosition, String upStoreName, String tappedStoreName){

        // 更新前のadapterを取得
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();

        // 入れ替えを行う
        Store_List_Items.set(parentPosition - 1,tappedStoreName);
        Store_List_Items.set(parentPosition,upStoreName);

        // adapterの更新
        adapter.notifyDataSetChanged();

        // 共有データの更新
        setMainApplication(Store_List_Items);

        // トーストの表示
        Toast toast = Toast.makeText(this, getString(R.string.up_toast), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void moveUpMemo(int position){

        // XMLの読み込み
        ReadXML.readInfo(mainApplication);

        // 各メモ及びタグを取得
        String[] memos = mainApplication.getMemos();
        String[] memoTagNames = CreateXML.getMemosTagName();

        //　XMLに保存されている文字列を入れ替える
        String parentMemo = memos[position];
        String memo = memos[position - 1];
        CreateXML.updateText(mainApplication,memoTagNames[position],memo,context);
        CreateXML.updateText(mainApplication,memoTagNames[position - 1],parentMemo,context);
    }

    public void moveDownItem(ListView listView, int parentPosition, String downStoreName, String tappedStoreName){

        // 更新前のadapterを取得
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();

        // 入れ替えを行う
        Store_List_Items.set(parentPosition + 1,tappedStoreName);
        Store_List_Items.set(parentPosition,downStoreName);

        // adapterの更新
        adapter.notifyDataSetChanged();

        // 共有データの更新
        setMainApplication(Store_List_Items);

        // トーストの表示
        Toast toast = Toast.makeText(this, getString(R.string.down_toast), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void moveDownMemo(int position){

        // XMLの読み込み
        ReadXML.readInfo(mainApplication);

        // 各メモ及びタグを取得
        String[] memos = mainApplication.getMemos();
        String[] memoTagNames = CreateXML.getMemosTagName();

        //　XMLに保存されている文字列を入れ替える
        String parentMemo = memos[position];
        String memo = memos[position + 1];
        CreateXML.updateText(mainApplication,memoTagNames[position],memo,context);
        CreateXML.updateText(mainApplication,memoTagNames[position + 1],parentMemo,context);
    }


    public void storeReName(String beforeName, ListView listView, int position) {

        final EditText eStoreReName = new EditText(this);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(20);
        eStoreReName.setFilters(filters);

        eStoreReName.setHint(getString(R.string.hint_before_name,beforeName));
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.rename_tittle))
                .setMessage(getString(R.string.rename_message))
                .setView(eStoreReName)
                .setPositiveButton(getString(R.string.rename), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean errorFlag = true;
                        int Store_Items_Size = Store_List_Items.size();

                        // 更新後の店舗名を取得 ⇒ セットしてadapterを更新
                        String newStoreName = eStoreReName.getText().toString();
                        // Normalizerを使って文字列に含まれる全角スペースを半角に変換(同時に数値は半角・カタカナは全角に変換される)
                        newStoreName = Normalizer.normalize(newStoreName,Normalizer.Form.NFKC);
                        // 文字列前後の半角スペースを削除
                        newStoreName = newStoreName.trim();
                        // 文字列内に絵文字が含まれるか判定。ただし絵文字だけに限らず、日常的に使われない文字もはじく使用になってます。
                        // R04.05.21時点で適当に入れた中国語・アラビア語・ロシア語は通過。ただ再起動すると文字化けするものもあった。また、古代文字ははじいた。
                        for(int i = 0; i < newStoreName.length(); i++){
                            char c = newStoreName.charAt(i);
                            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                                Toast toast = Toast.makeText(MainManagementStore.this, getString(R.string.not_use_string), Toast.LENGTH_LONG);
                                toast.show();
                                return;
                            }
                        }
                        //空白チェック
                        if(StringUtils.isBlank(newStoreName)){
                            errorFlag = false;
                            Toast toast = Toast.makeText(MainManagementStore.this, getString(R.string.not_blank_store_name), Toast.LENGTH_LONG);
                            toast.show();
                        }

                        // 重複チェック
                        for(int i = 0; i < Store_Items_Size; i++){
                            if((Store_List_Items.get(i)).equals(newStoreName)){
                                errorFlag = false;
                                Toast toast = Toast.makeText(MainManagementStore.this, getString(R.string.already_store_name), Toast.LENGTH_LONG);
                                toast.show();
                                break;
                            }
                        }

                        // 店舗名の重複がなければ配列に項目をセットし、内部ストレージにも保存
                        if(errorFlag){

                            ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();
                            Store_List_Items.set(position,newStoreName);
                            adapter.notifyDataSetChanged();

                            // 共有データも更新
                            setMainApplication(Store_List_Items);

                            // トースト表示
                            Toast toast = Toast.makeText(MainManagementStore.this, getString(R.string.rename_toast,beforeName,newStoreName), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    public void deleteList(String tappedStoreName, ListView listView, int position) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_store_tittle))
                .setMessage(getString(R.string.delete_store_message,tappedStoreName))
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // XMLの読み込み
                        ReadXML.readInfo(mainApplication);

                        // 各メモ及びタグを取得
                        String[] memos = mainApplication.getMemos();
                        String[] memoTagNames = CreateXML.getMemosTagName();

                        //　XMLに保存されている文字列を入れ替える
                        for(int x = position; x < adapter.getCount(); x++){
                            if(x < 19){
                                String memo = memos[x + 1];
                                CreateXML.updateText(mainApplication,memoTagNames[x],memo,context);
                            } else {
                                CreateXML.updateText(mainApplication,memoTagNames[x],"null",context);
                            }
                        }

                        // adapterから店舗名＆リストから削除 ⇒ adapterを更新
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
                        adapter.remove(tappedStoreName);
                        Store_List_Items.remove(tappedStoreName);

                        // 共有データも更新
                        setMainApplication(Store_List_Items);

                        // 店舗数表示を更新してトースト表示
                        tStoreCounter.setText(getString(R.string.store_count,adapter.getCount()));
                        Toast toast = Toast.makeText(MainManagementStore.this, getString(R.string.delete_toast), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    private void setStorageListItem() {

        // 20店舗対応
        String[] Storage_Store_Items = CommonFeature.getStoreItems(mainApplication);
        int Storage_Store_Items_Size = Storage_Store_Items.length;

        for (int i = 0; i < Storage_Store_Items_Size; i++) {

            if (!Storage_Store_Items[i].equals("null")) {
                // xmlの上から順に店舗名を入れる
                String item = Storage_Store_Items[i];
                this.Store_List_Items.add(item);
            }
        }
    }

    private void setMainApplication(ArrayList<String> Items) {

        String storeName;
        // 20店舗対応
        int storeLimit = 20;
        int Items_Size = Items.size();

        for (int i = 0; i < storeLimit; i++) {

            if (i < Items_Size) {
                storeName = Items.get(i);
            } else {
                storeName = "null";
            }

            switch (i) {
                case 0:
                    mainApplication.setStore001(storeName);
                    CreateXML.updateText(mainApplication, "store001", storeName,context);
                    break;
                case 1:
                    mainApplication.setStore002(storeName);
                    CreateXML.updateText(mainApplication, "store002", storeName,context);
                    break;
                case 2:
                    mainApplication.setStore003(storeName);
                    CreateXML.updateText(mainApplication, "store003", storeName,context);
                    break;
                case 3:
                    mainApplication.setStore004(storeName);
                    CreateXML.updateText(mainApplication, "store004", storeName,context);
                    break;
                case 4:
                    mainApplication.setStore005(storeName);
                    CreateXML.updateText(mainApplication, "store005", storeName,context);
                    break;
                case 5:
                    mainApplication.setStore006(storeName);
                    CreateXML.updateText(mainApplication, "store006", storeName,context);
                    break;
                case 6:
                    mainApplication.setStore007(storeName);
                    CreateXML.updateText(mainApplication, "store007", storeName,context);
                    break;
                case 7:
                    mainApplication.setStore008(storeName);
                    CreateXML.updateText(mainApplication, "store008", storeName,context);
                    break;
                case 8:
                    mainApplication.setStore009(storeName);
                    CreateXML.updateText(mainApplication, "store009", storeName,context);
                    break;
                case 9:
                    mainApplication.setStore010(storeName);
                    CreateXML.updateText(mainApplication, "store010", storeName,context);
                    break;
                case 10:
                    mainApplication.setStore011(storeName);
                    CreateXML.updateText(mainApplication, "store011", storeName,context);
                    break;
                case 11:
                    mainApplication.setStore012(storeName);
                    CreateXML.updateText(mainApplication, "store012", storeName,context);
                    break;
                case 12:
                    mainApplication.setStore013(storeName);
                    CreateXML.updateText(mainApplication, "store013", storeName,context);
                    break;
                case 13:
                    mainApplication.setStore014(storeName);
                    CreateXML.updateText(mainApplication, "store014", storeName,context);
                    break;
                case 14:
                    mainApplication.setStore015(storeName);
                    CreateXML.updateText(mainApplication, "store015", storeName,context);
                    break;
                case 15:
                    mainApplication.setStore016(storeName);
                    CreateXML.updateText(mainApplication, "store016", storeName,context);
                    break;
                case 16:
                    mainApplication.setStore017(storeName);
                    CreateXML.updateText(mainApplication, "store017", storeName,context);
                    break;
                case 17:
                    mainApplication.setStore018(storeName);
                    CreateXML.updateText(mainApplication, "store018", storeName,context);
                    break;
                case 18:
                    mainApplication.setStore019(storeName);
                    CreateXML.updateText(mainApplication, "store019", storeName,context);
                    break;
                case 19:
                    mainApplication.setStore020(storeName);
                    CreateXML.updateText(mainApplication, "store020", storeName,context);
                    break;
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setFocusEvent(){
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                focusOut();
                return false;
            }
        });
    }

    public void focusOut() {
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        eStoreName.setSelection(0);
        mainLayout.requestFocus();
    }

    public void actionListenerFocusOut() {
        eStoreName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    focusOut();
                }
                return false;
            }
        });
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if(!visible){
            //キーボードが非表示になったことを検知した時
            mainLayout.requestFocus();
        }
    }

}