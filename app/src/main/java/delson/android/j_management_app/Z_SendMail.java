package delson.android.j_management_app;

public class Z_SendMail extends android.os.AsyncTask{

    protected String account;
    protected String password;
    protected String title;
    protected String text;


    @Override
    protected Object doInBackground(Object... obj){

        account = (String)obj[0];
        password = (String)obj[1];
        title = (String)obj[2];
        text = (String)obj[3];

        java.util.Properties properties = new java.util.Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.post", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        final javax.mail.Message msg = new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(account,password);
            }
        }));

        try {

            // 送信元の設定
            msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
            //送信先の設定
            msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(account + "@gmail.com"));
            // 件名の設定
            msg.setSubject(title);
            // 本文の設定
            msg.setText(text);

            javax.mail.Transport.send(msg);

        } catch (Exception e) {
            return (Object)e.toString();
        }

        return (Object)"送信が完了しました";

    }

    @Override
    protected void onPostExecute(Object obj) {
    }

}
