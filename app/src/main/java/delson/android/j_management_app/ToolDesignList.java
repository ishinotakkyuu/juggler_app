package delson.android.j_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ToolDesignList extends AppCompatActivity {

    // 項目を表示するリストビュー
    ListView designList;

    // アダプター
    ToolDesignListAdapter adapter;

    // リストビューに表示する項目を格納するリスト
    static ArrayList<ToolDesignListItems> designs;

    // メニュー表示切り替えに使用する判定値
    boolean menuFlag = true;

    // チェックボックス関係
    static boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool03_design03_list);

        designList = findViewById(R.id.design_list);

        // リストビューにリスナー登録
        setItemClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // staticフラグの初期化
        check = false;

        // リストに項目をセット
        designs = new ArrayList<>();
        Context context = getApplicationContext();
        String sql = "select * from DESIGN;";
        DatabaseResultSet.execution("ToolDesignList", context, sql);

        // アダプターをセット
        adapter = new ToolDesignListAdapter(this, R.layout.tool03_design04_list_item, designs);
        designList.setAdapter(adapter);

    }

    // メニューのセット
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.design_list_menu, menu);
        return true;
    }

    // フラグ判定でメニュー表示を変更
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        int count = designList.getCount();
        if (count != 0) {
            if (menuFlag) {
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(true);

            } else {
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
                menu.getItem(2).setVisible(true);
                menu.getItem(3).setVisible(false);
            }
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // 選択削除
            case R.id.selection_delete:

                // 選択削除中フラグ
                check = true;

                // フラグを変更してメニューを更新
                menuFlag = false;
                invalidateOptionsMenu();

                int a = 0;
                while (designList.getChildAt(a) != null){
                    CheckBox checkBox = designList.getChildAt(a).findViewById(R.id.delete_check);
                    checkBox.setVisibility(View.VISIBLE);
                    a++;
                }

                break;

            case R.id.selection_cancel:

                // 選択削除中フラグ
                check = false;

                // フラグを変更してメニューを更新
                menuFlag = true;
                invalidateOptionsMenu();

                int b = 0;
                while (designList.getChildAt(b) != null){
                    CheckBox checkBox = designList.getChildAt(b).findViewById(R.id.delete_check);
                    // チェックが付いていたら外してからViewを消す
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    }
                    checkBox.setVisibility(View.GONE);
                    b++;
                }

                // チェック情報を格納しているリストの初期化
                adapter.initBoolList(designs);

                break;

            case R.id.selection_delete_execution:

                new AlertDialog.Builder(this)
                        .setTitle("選択項目の削除")
                        .setMessage("削除してよろしいですか？")
                        .setPositiveButton("はい", (dialog, which) -> {

                            // 選択削除中フラグ
                            check = false;

                            // フラグを変更してメニューを更新
                            menuFlag = true;
                            invalidateOptionsMenu();

                            // 該当のDB情報を削除する
                            int designCount = adapter.getBoolList().size();
                            Context context = getApplicationContext();

                            for (int i = 0; i < designCount; i++) {
                                if (adapter.getBoolList().get(i)){
                                    String sql = "DELETE FROM DESIGN WHERE ID = '" + designs.get(i).getID() + "';";
                                    DatabaseResultSet.UpdateOrDelete(context, sql);
                                }
                            }

                            // リスト再更新
                            designs = new ArrayList<>();
                            context = getApplicationContext();
                            String sql = "select * from DESIGN;";
                            DatabaseResultSet.execution("ToolDesignList", context, sql);

                            // アダプターを更新してリストビュー再更新
                            adapter = new ToolDesignListAdapter(this, R.layout.tool03_design04_list_item, designs);
                            designList.setAdapter(adapter);

                            // トースト表示
                            if (designCount != adapter.getCount()){
                                Toast toast = Toast.makeText(ToolDesignList.this, "選択項目を削除しました", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(ToolDesignList.this, "削除する項目がありませんでした", Toast.LENGTH_LONG);
                                toast.show();
                            }

                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                break;

            case R.id.design_all_delete_execution:

                new AlertDialog.Builder(this)
                        .setTitle("データ全消去")
                        .setMessage("保存されている出目データを全て削除してよろしいですか？")
                        .setPositiveButton("はい", (dialog, which) -> {

                            // 念のためフラグを初期化
                            check = false;

                            // フラグを変更してメニューを更新
                            menuFlag = true;
                            invalidateOptionsMenu();

                            // 全消去
                            Context context = getApplicationContext();
                            String sql = "DELETE FROM DESIGN;";
                            DatabaseResultSet.execution("ToolDesignList", context, sql);

                            // リストビュー再更新
                            designs = new ArrayList<>();
                            adapter = new ToolDesignListAdapter(this, R.layout.tool03_design04_list_item, designs);
                            designList.setAdapter(adapter);

                            Toast toast = Toast.makeText(ToolDesignList.this, "全て削除しました", Toast.LENGTH_LONG);
                            toast.show();
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setItemClick() {
        designList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // クリックされたリストビューを取得
                ListView listView = (ListView) parent;

                // データ詳細画面にデータを引き継ぐ際に使用するIntentを定義
                Intent intent = new Intent(getApplicationContext(), ToolDesignDetail.class);

                // クリックした位置の各項目(ID・機種名・店舗名・登録日時)を取得
                ToolDesignListItems items = (ToolDesignListItems) listView.getItemAtPosition(position);

                // Intentに引き継ぐIDと機種名をセット
                intent.putExtra("ID", items.getID());
                intent.putExtra("NO", items.getNo());

                // ToolDesignDetail.xmlに遷移させる
                startActivity(intent);
            }
        });
    }
}