package com.example.title_1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;


public class FlagExportCSV extends Fragment {

    ConstraintLayout layout;
    EditText mailEdit;
    Button sendButton;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main05_exportcsv01,container,false);

        layout = view.findViewById(R.id.layoutCSV);
        mailEdit = layout.findViewById(R.id.EditMailAddress);
        sendButton = layout.findViewById(R.id.SendButton);

        // フォーカス関係
        focusOut();

        // 送信ボタン処理
        sendCSV();

        return view;
    }

    // 送信ボタンに設定
    public void sendCSV(){

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // mailEditに文字列が入っていたら
                if (StringUtils.isNotEmpty(mailEdit.getText())){

                    // フォーカスを外す
                    InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
                    mailEdit.setSelection(0);
                    layout.requestFocus();

                    new AlertDialog.Builder(getActivity())
                            .setTitle("CSV出力")
                            .setMessage("入力したアドレス宛にCSVを送信します")
                            .setPositiveButton("送信", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {






                                    // ここにCSV作成・メール送信関係を記述









                                    // トースト表示
                                    Toast toast = Toast.makeText(getActivity(), "CSVを送信しました", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            })
                            .setNegativeButton("戻る", null)
                            .show();
                } else {

                    // フォーカスを外す
                    InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
                    mailEdit.setSelection(0);
                    layout.requestFocus();

                    // トースト表示
                    Toast toast = Toast.makeText(getActivity(), "アドレスを入力してください", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    public void focusOut(){
        // レイアウトタッチ時にフォーカスを外す
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
                mailEdit.setSelection(0);
                layout.requestFocus();
                return false;
            }
        });

        // キーボード確定ボタン押下時にフォーカスを外す
        mailEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i== EditorInfo.IME_ACTION_DONE){
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),0);
                mailEdit.setSelection(0);
                layout.requestFocus();
            }
            return false;});
    }

}
