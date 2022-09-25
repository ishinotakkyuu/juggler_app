package delson.android.j_management_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FlagExportCSV extends Fragment implements View.OnClickListener {

    // 出力ボタン
    Button bExportCSV;

    // CSV作成関係
    final String FILE_NAME = "j_management_date.csv";
    final String HEADER = "日付,店舗名,台番号,機種名,差枚数,開始ゲーム数,総ゲーム数,単独BIG,チェリーBIG,単独REG,チェリーREG,非重複チェリー,ぶどう";
    final String[] COLUMN_NAME = {"OPERATION_DATE", "STORE_NAME", "TABLE_NUMBER", "MACHINE_NAME", "DIFFERENCE_NUMBER", "START_GAME", "TOTAL_GAME", "SINGLE_BIG", "CHERRY_BIG", "SINGLE_REG", "CHERRY_REG", "CHERRY", "GRAPE"};
    final String COMMA = ",";
    final String SQL = "SELECT * FROM TEST ORDER BY OPERATION_DATE ASC , SAVE_DATE ASC;";

    // メーラー関係
    final String SUBJECT = "";
    final String TEXT = "「J管理」CSVデータ送信";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main06_exportcsv01, container, false);

        // 出力ボタンにリスナー登録
        bExportCSV = view.findViewById(R.id.ExportButton);
        bExportCSV.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // CSVデータの作成
        createCSV();
        // メーラー起動
        callMailer();
    }

    public void createCSV() {

        File file;
        StringBuilder date = new StringBuilder();

        try {

            file = new File(getContext().getFilesDir(), FILE_NAME);

            // CSVファイルのヘッダーを書き出し
            date.append(HEADER);
            date.append("\n");

            // DBオープン
            DatabaseHelper helper = new DatabaseHelper(getContext());
            SQLiteDatabase db = helper.getWritableDatabase();

            // DBデータをCSVにセット
            Cursor cursor = db.rawQuery(SQL, null);
            while (cursor.moveToNext()) {
                setDate(cursor, date);
            }

            // CSVを保存
            FileWriter writer = new FileWriter(file);
            writer.write(date.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDate(Cursor cursor, StringBuilder date) {
        int index;
        for (int i = 0, size = COLUMN_NAME.length - 1; i < size; i++) {
            index = cursor.getColumnIndex(COLUMN_NAME[i]);
            date.append(cursor.getString(index));
            date.append(COMMA);
        }

        // 最後のデータをセットしたら改行
        index = cursor.getColumnIndex(COLUMN_NAME[12]);
        date.append(cursor.getString(index));
        date.append("\n");
    }

    public void callMailer() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT); // 件名
            intent.putExtra(Intent.EXTRA_TEXT, TEXT); // 本文

            File file = new File(getContext().getFilesDir(), FILE_NAME);
            Uri uri = FileProvider.getUriForFile(getContext(), "delson.android.management_app.fileprovider", file);
            intent.putExtra(Intent.EXTRA_STREAM, uri);

            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "本機能を利用できるアプリが存在しません", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
