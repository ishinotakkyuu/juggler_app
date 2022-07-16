package jp.example.title_1;

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
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Z_FlagExportCSV extends Fragment {

    ConstraintLayout layout;
    EditText mailEdit;
    Button sendButton;

    // メール情報
    final String ACCOUNT = "axptx80895@yahoo.co.jp";
    final String PASSWORD = "Kenji0718";
    String title = "これがメールの件名になります";
    String text = "これがメールの本文になります";


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.z_main99_exportcsv01, container, false);

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
    public void sendCSV() {

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // mailEditに文字列が入っていたら
                if (StringUtils.isNotEmpty(mailEdit.getText())) {

                    // フォーカスを外す
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                    mailEdit.setSelection(0);
                    layout.requestFocus();

                    new AlertDialog.Builder(getActivity())
                            .setTitle("CSV出力")
                            .setMessage("入力したアドレス宛にCSVを送信します")
                            .setPositiveButton("送信", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {

                                        // メール送信のプロパティ設定
                                        Properties props = new Properties();
                                        props.put("mail.smtp.host", "smtp.mail.yahoo.co.jp");
                                        props.put("mail.smtp.port", "587");
                                        props.put("mail.smtp.auth", "true");
                                        props.put("mail.transport.protocol", "smtp");
                                        props.put("mail.smtp.ssl.trust", "*");
                                        props.put("mail.smtp.starttls.enable", "true");
                                        props.put("mail.smtp.connectiontimeout", "10000");
                                        props.put("mail.smtp.timeout", "10000");

                                        // セッションを作成する
                                        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                                            protected PasswordAuthentication getPasswordAuthentication() {
                                                return new PasswordAuthentication(ACCOUNT, "PW");
                                            }
                                        });

                                        // 送信元はYahooメール 送信先はGメール
                                        Message message = new MimeMessage(session);
                                        message.setFrom(new InternetAddress(ACCOUNT, "J-管理"));
                                        message.setReplyTo(new Address[]{new InternetAddress("j.management.app@gmail.com")});
                                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("j.management.app@gmail.com"));
                                        message.setSubject("テスト");
                                        MimeBodyPart messageBodyPart = new MimeBodyPart();
                                        messageBodyPart.setText("テストメール");

                                        // メールのメタ情報を作成
                                        Multipart multipart = new MimeMultipart();
                                        multipart.addBodyPart(messageBodyPart);
                                        message.setHeader("Content-Transfer-Encoding", "base64");

                                        // メールを送信する
                                        message.setContent(multipart);
                                        Transport.send(message);

                                    } catch (Exception e) {
                                        System.out.print("例外が発生！");
                                        e.printStackTrace();
                                    } finally {

                                    }
                                }
                            })
                            .setNegativeButton("戻る", null)
                            .show();
                } else {

                    // フォーカスを外す
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
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
    public void focusOut() {
        // レイアウトタッチ時にフォーカスを外す
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                mailEdit.setSelection(0);
                layout.requestFocus();
                return false;
            }
        });

        // キーボード確定ボタン押下時にフォーカスを外す
        mailEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                mailEdit.setSelection(0);
                layout.requestFocus();
            }
            return false;
        });
    }

}
