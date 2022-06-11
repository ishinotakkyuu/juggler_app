package com.example.title_1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class FlagExportCSV extends Fragment {

    ConstraintLayout layout;
    EditText mailEdit;
    Button sendButton;

    // メール関係(送信元アカウント情報)
    final String myEmail = "sas.kenji.0718@icloud.com";
    final String password = "Kenji0718";
    // メール関係(送信先アカウント情報と件名・本文)
    String email = "sas.kenji.0718@icloud.com";
    String subject = "これがメールの件名になります";
    String body = "これがメールの本文になります";


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

        //メール送信にあたっては以下の２行をonCreateに書かないとエラーになるらしい
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

                                        //email と password更新
                                        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                                        //sp.edit().putString("email", email).commit();
                                        //sp.edit().putString("password", password).commit();

                                        //以下メール送信設定
                                        final Properties property = new Properties();
                                        property.put("mail.smtp.host",                "smtp.gmail.com");
                                        property.put("mail.host",                     "smtp.gmail.com");
                                        property.put("mail.smtp.port",                "465");
                                        property.put("mail.smtp.socketFactory.port",  "465");
                                        property.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

                                    try {

                                        // セッション
                                        final Session session = Session.getInstance(property, new javax.mail.Authenticator() {
                                            @Override
                                            protected PasswordAuthentication getPasswordAuthentication() {
                                                // 引数は、①送信元メールアドレス ②パスワード を入れる
                                                return new PasswordAuthentication(myEmail, password);
                                            }
                                        });

                                        MimeMessage mimeMsg = new MimeMessage(session);

                                        // 送信元を設定
                                        mimeMsg.setFrom(new InternetAddress(myEmail));
                                        //　To/CC/BCCと送信先を設定(現時点では自分自身に送信する)
                                        mimeMsg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(myEmail));
                                        // 件名を指定
                                        mimeMsg.setSubject(subject, "utf-8");
                                        // 本文を設定
                                        mimeMsg.setText(body);

                                        /* 添付ファイルをする場合はこれを使う
                                        final MimeBodyPart txtPart = new MimeBodyPart();
                                        txtPart.setText(body, "utf-8");

                                        final MimeBodyPart filePart = new MimeBodyPart();
                                        File file = new File("[添付ファイルパス]");
                                        FileDataSource fds = new FileDataSource(file);
                                        DataHandler data = new DataHandler(fds);
                                        filePart.setDataHandler(data);
                                        filePart.setFileName(MimeUtility.encodeWord("[メール本文の添付ファイル名]"));*/

                                        //final Multipart mp = new MimeMultipart();
                                        //mp.addBodyPart(txtPart);
                                        //mp.addBodyPart(filePart); //添付ファイルをする場合はこれ
                                        //mimeMsg.setContent(mp);

                                        // メール送信する
                                        final Transport transport = session.getTransport("smtp");
                                        transport.connect(myEmail,password);
                                        transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                                        transport.close();

                                    } catch (MessagingException e) {
                                        System.out.println("exception = " + e);

                                    } /*catch (UnsupportedEncodingException e) {  必要あるのか不明
                                 }*/ finally {
                                        //System.out.println("finish sending email");

                                        // トースト表示
                                        Toast toast = Toast.makeText(getActivity(), "CSVを送信しました", Toast.LENGTH_LONG);
                                        toast.show();
                                    }

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
