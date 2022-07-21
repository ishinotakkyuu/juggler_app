package delson.android.j_management_app;

import android.content.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CreateXML {

    static Document execution(MainApplication mainApplication , Context context) {

        // Documentインスタンスの生成
        DocumentBuilder documentBuilder = null;
        try {
            // なにやってるかはわからない
            documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // XML文書の土台となるDocumentノードを生成
        Document document = documentBuilder.newDocument();

        // root要素の生成
        // root要素とは、XML文書のツリー構造で最上位の要素。Activityで例えるなら、XMLの一番上にある「<androidx.constraintLayout.widget.ConstraintLayout」ってやつ
        // XML文書には必ずこのroot要素が必要であり、Elementとは、XML 文書内の要素を表す。要素にはroot要素やtext要素などがあり、他にもたくさんあるらしい
        Element info = document.createElement("info");

        // root要素をDocumentノードへ追加
        document.appendChild(info);

        // 以下は、XML文書のtext要素追加コード。Activityで例えるなら、「TextView」とか「Button」とかにあたる。
            // text要素はroot要素の子ノード
                // ①createElementメソッドは、text要素をセットするもの
                // ②setAttributeメソッドは、属性をセットしてするもの
                // ③appendChildメソッドは、text要素(子ノード)を親要素の末尾に追加するもの
                // ④createTextNodeメソッドは、指定した文字列でtextノードを作成するもの。XML文書内の表記でいうと、＞＜で挟まれたやつがtextノード
                    // これらを踏まえて、以下のコードは次の流れを示している。
                        //①指定したタグ名で開始タグを作成　⇒　<userId
                        //②作成した開始タグの後ろに属性をセット　⇒　<userId id="ユーザID">
                        //③textノード名をmainApplicationから取得　⇒　int型なので初期値だと0が入る
                        //④取得したtextノード名を①と②で作った要素列の末尾にセット　⇒　<userId id="ユーザID">int型なので初期値だと0が入る
                        //⑤最後に①と同じタグ名を使って終了タグを末尾にセット　⇒　<userId id="ユーザID">int型なので初期値だと0が入る</userId>　これにて完成
                            //なお、String.valueOfの引数にnullが入ると、文字列の"null"が入るという、ちょっと変わった仕様になってる模様(String.valueOf null で検索すると色々情報が出てきた)
                            //ただし、nullが来ると色々と面倒なので、文字列"null"のままで進める

        // ユーザID
        Element userId = document.createElement("userId");
        userId.setAttribute("id", "ユーザID");
        String userIdValue = String.valueOf(mainApplication.getUserId());
        userId.appendChild(document.createTextNode(userIdValue));
        info.appendChild(userId);

        //　機種名
        Element machineName = document.createElement("machineName");
        machineName.setAttribute("id", "機種名");
        String machineNameValue = String.valueOf(mainApplication.getMachinePosition());
        machineName.appendChild(document.createTextNode(machineNameValue));
        info.appendChild(machineName);

        //　総ゲーム数
        Element total = document.createElement("total");
        total.setAttribute("id", "総ゲーム数");
        String totalValue = String.valueOf(mainApplication.getTotalGames());
        total.appendChild(document.createTextNode(totalValue));
        info.appendChild(total);

        //　打ち始めゲーム数
        Element start = document.createElement("start");
        start.setAttribute("id", "打ち始めゲーム数");
        String startValue = String.valueOf(mainApplication.getStartGames());
        start.appendChild(document.createTextNode(startValue));
        info.appendChild(start);

        //　単独BIG
        Element aB = document.createElement("aB");
        aB.setAttribute("id", "単独BIG");
        String aBValue = String.valueOf(mainApplication.getSingleBig());
        aB.appendChild(document.createTextNode(aBValue));
        info.appendChild(aB);

        //　チェリーBIG
        Element cB = document.createElement("cB");
        cB.setAttribute("id", "チェリーBIG");
        String cBValue = String.valueOf(mainApplication.getCherryBig());
        cB.appendChild(document.createTextNode(cBValue));
        info.appendChild(cB);

        //　単独REG
        Element aR = document.createElement("aR");
        aR.setAttribute("id", "単独REG");
        String aRValue = String.valueOf(mainApplication.getSingleReg());
        aR.appendChild(document.createTextNode(aRValue));
        info.appendChild(aR);

        //　チェリーREG
        Element cR = document.createElement("cR");
        cR.setAttribute("id", "チェリーREG");
        String cRValue = String.valueOf(mainApplication.getCherryReg());
        cR.appendChild(document.createTextNode(cRValue));
        info.appendChild(cR);

        //　非重複チェリー
        Element ch = document.createElement("ch");
        ch.setAttribute("id", "非重複チェリー");
        String chValue = String.valueOf(mainApplication.getCherry());
        ch.appendChild(document.createTextNode(chValue));
        info.appendChild(ch);

        //　ぶどう
        Element gr = document.createElement("gr");
        gr.setAttribute("id", "ぶどう");
        String grValue = String.valueOf(mainApplication.getGrape());
        gr.appendChild(document.createTextNode(grValue));
        info.appendChild(gr);

        //　店舗001
        Element store001 = document.createElement("store001");
        store001.setAttribute("id", "店舗001");
        String store001Value = String.valueOf(mainApplication.getStore001());
        store001.appendChild(document.createTextNode(store001Value));
        info.appendChild(store001);

        //　店舗002
        Element store002 = document.createElement("store002");
        store002.setAttribute("id", "店舗002");
        String store002Value = String.valueOf(mainApplication.getStore002());
        store002.appendChild(document.createTextNode(store002Value));
        info.appendChild(store002);

        //　店舗003
        Element store003 = document.createElement("store003");
        store003.setAttribute("id", "店舗003");
        String store003Value = String.valueOf(mainApplication.getStore003());
        store003.appendChild(document.createTextNode(store003Value));
        info.appendChild(store003);

        //　店舗004
        Element store004 = document.createElement("store004");
        store004.setAttribute("id", "店舗004");
        String store004Value = String.valueOf(mainApplication.getStore004());
        store004.appendChild(document.createTextNode(store004Value));
        info.appendChild(store004);

        //　店舗005
        Element store005 = document.createElement("store005");
        store005.setAttribute("id", "店舗005");
        String store005Value = String.valueOf(mainApplication.getStore005());
        store005.appendChild(document.createTextNode(store005Value));
        info.appendChild(store005);

        //　店舗006
        Element store006 = document.createElement("store006");
        store006.setAttribute("id", "店舗006");
        String store006Value = String.valueOf(mainApplication.getStore006());
        store006.appendChild(document.createTextNode(store006Value));
        info.appendChild(store006);

        //　店舗007
        Element store007 = document.createElement("store007");
        store007.setAttribute("id", "店舗007");
        String store007Value = String.valueOf(mainApplication.getStore007());
        store007.appendChild(document.createTextNode(store007Value));
        info.appendChild(store007);

        //　店舗008
        Element store008 = document.createElement("store008");
        store008.setAttribute("id", "店舗008");
        String store008Value = String.valueOf(mainApplication.getStore008());
        store008.appendChild(document.createTextNode(store008Value));
        info.appendChild(store008);

        //　店舗009
        Element store009 = document.createElement("store009");
        store009.setAttribute("id", "店舗009");
        String store009Value = String.valueOf(mainApplication.getStore009());
        store009.appendChild(document.createTextNode(store009Value));
        info.appendChild(store009);

        //　店舗010
        Element store010 = document.createElement("store010");
        store010.setAttribute("id", "店舗010");
        String store010Value = String.valueOf(mainApplication.getStore010());
        store010.appendChild(document.createTextNode(store010Value));
        info.appendChild(store010);

        //　店舗011
        Element store011 = document.createElement("store011");
        store011.setAttribute("id", "店舗011");
        String store011Value = String.valueOf(mainApplication.getStore011());
        store011.appendChild(document.createTextNode(store011Value));
        info.appendChild(store011);

        //　店舗012
        Element store012 = document.createElement("store012");
        store012.setAttribute("id", "店舗012");
        String store012Value = String.valueOf(mainApplication.getStore012());
        store012.appendChild(document.createTextNode(store012Value));
        info.appendChild(store012);

        //　店舗013
        Element store013 = document.createElement("store013");
        store013.setAttribute("id", "店舗013");
        String store013Value = String.valueOf(mainApplication.getStore013());
        store013.appendChild(document.createTextNode(store013Value));
        info.appendChild(store013);

        //　店舗014
        Element store014 = document.createElement("store014");
        store014.setAttribute("id", "店舗014");
        String store014Value = String.valueOf(mainApplication.getStore014());
        store014.appendChild(document.createTextNode(store014Value));
        info.appendChild(store014);

        //　店舗015
        Element store015 = document.createElement("store015");
        store015.setAttribute("id", "店舗015");
        String store015Value = String.valueOf(mainApplication.getStore015());
        store015.appendChild(document.createTextNode(store015Value));
        info.appendChild(store015);

        //　店舗016
        Element store016 = document.createElement("store016");
        store016.setAttribute("id", "店舗016");
        String store016Value = String.valueOf(mainApplication.getStore016());
        store016.appendChild(document.createTextNode(store016Value));
        info.appendChild(store016);

        //　店舗017
        Element store017 = document.createElement("store017");
        store017.setAttribute("id", "店舗017");
        String store017Value = String.valueOf(mainApplication.getStore017());
        store017.appendChild(document.createTextNode(store017Value));
        info.appendChild(store017);

        //　店舗018
        Element store018 = document.createElement("store018");
        store018.setAttribute("id", "店舗018");
        String store018Value = String.valueOf(mainApplication.getStore018());
        store018.appendChild(document.createTextNode(store018Value));
        info.appendChild(store018);

        //　店舗019
        Element store019 = document.createElement("store019");
        store019.setAttribute("id", "店舗019");
        String store019Value = String.valueOf(mainApplication.getStore019());
        store019.appendChild(document.createTextNode(store019Value));
        info.appendChild(store019);

        //　店舗020
        Element store020 = document.createElement("store020");
        store020.setAttribute("id", "店舗020");
        String store020Value = String.valueOf(mainApplication.getStore020());
        store020.appendChild(document.createTextNode(store020Value));
        info.appendChild(store020);

        //　店舗メモ001
        Element memo001 = document.createElement("memo001");
        memo001.setAttribute("id", "メモ001");
        String memo001Value = String.valueOf(mainApplication.getMemo001());
        memo001.appendChild(document.createTextNode(memo001Value));
        info.appendChild(memo001);

        //　店舗メモ002
        Element memo002 = document.createElement("memo002");
        memo002.setAttribute("id", "メモ002");
        String memo002Value = String.valueOf(mainApplication.getMemo002());
        memo002.appendChild(document.createTextNode(memo002Value));
        info.appendChild(memo002);

        //　店舗メモ003
        Element memo003 = document.createElement("memo003");
        memo003.setAttribute("id", "メモ003");
        String memo003Value = String.valueOf(mainApplication.getMemo003());
        memo003.appendChild(document.createTextNode(memo003Value));
        info.appendChild(memo003);

        //　店舗メモ004
        Element memo004 = document.createElement("memo004");
        memo004.setAttribute("id", "メモ004");
        String memo004Value = String.valueOf(mainApplication.getMemo004());
        memo004.appendChild(document.createTextNode(memo004Value));
        info.appendChild(memo004);

        //　店舗メモ005
        Element memo005 = document.createElement("memo005");
        memo005.setAttribute("id", "メモ005");
        String memo005Value = String.valueOf(mainApplication.getMemo005());
        memo005.appendChild(document.createTextNode(memo005Value));
        info.appendChild(memo005);

        //　店舗メモ006
        Element memo006 = document.createElement("memo006");
        memo006.setAttribute("id", "メモ006");
        String memo006Value = String.valueOf(mainApplication.getMemo006());
        memo006.appendChild(document.createTextNode(memo006Value));
        info.appendChild(memo006);

        //　店舗メモ007
        Element memo007 = document.createElement("memo007");
        memo007.setAttribute("id", "メモ007");
        String memo007Value = String.valueOf(mainApplication.getMemo007());
        memo007.appendChild(document.createTextNode(memo007Value));
        info.appendChild(memo007);

        //　店舗メモ008
        Element memo008 = document.createElement("memo008");
        memo008.setAttribute("id", "メモ008");
        String memo008Value = String.valueOf(mainApplication.getMemo008());
        memo008.appendChild(document.createTextNode(memo008Value));
        info.appendChild(memo008);

        //　店舗メモ009
        Element memo009 = document.createElement("memo009");
        memo009.setAttribute("id", "メモ009");
        String memo009Value = String.valueOf(mainApplication.getMemo009());
        memo009.appendChild(document.createTextNode(memo009Value));
        info.appendChild(memo009);

        //　店舗メモ010
        Element memo010 = document.createElement("memo010");
        memo010.setAttribute("id", "メモ010");
        String memo010Value = String.valueOf(mainApplication.getMemo010());
        memo010.appendChild(document.createTextNode(memo010Value));
        info.appendChild(memo010);

        //　店舗メモ011
        Element memo011 = document.createElement("memo011");
        memo011.setAttribute("id", "メモ011");
        String memo011Value = String.valueOf(mainApplication.getMemo011());
        memo011.appendChild(document.createTextNode(memo011Value));
        info.appendChild(memo011);

        //　店舗メモ012
        Element memo012 = document.createElement("memo012");
        memo012.setAttribute("id", "メモ012");
        String memo012Value = String.valueOf(mainApplication.getMemo012());
        memo012.appendChild(document.createTextNode(memo012Value));
        info.appendChild(memo012);

        //　店舗メモ013
        Element memo013 = document.createElement("memo013");
        memo013.setAttribute("id", "メモ013");
        String memo013Value = String.valueOf(mainApplication.getMemo013());
        memo013.appendChild(document.createTextNode(memo013Value));
        info.appendChild(memo013);

        //　店舗メモ014
        Element memo014 = document.createElement("memo014");
        memo014.setAttribute("id", "メモ014");
        String memo014Value = String.valueOf(mainApplication.getMemo014());
        memo014.appendChild(document.createTextNode(memo014Value));
        info.appendChild(memo014);

        //　店舗メモ015
        Element memo015 = document.createElement("memo015");
        memo015.setAttribute("id", "メモ015");
        String memo015Value = String.valueOf(mainApplication.getMemo015());
        memo015.appendChild(document.createTextNode(memo015Value));
        info.appendChild(memo015);

        //　店舗メモ016
        Element memo016 = document.createElement("memo016");
        memo016.setAttribute("id", "メモ016");
        String memo016Value = String.valueOf(mainApplication.getMemo016());
        memo016.appendChild(document.createTextNode(memo016Value));
        info.appendChild(memo016);

        //　店舗メモ017
        Element memo017 = document.createElement("memo017");
        memo017.setAttribute("id", "メモ017");
        String memo017Value = String.valueOf(mainApplication.getMemo017());
        memo017.appendChild(document.createTextNode(memo017Value));
        info.appendChild(memo017);

        //　店舗メモ018
        Element memo018 = document.createElement("memo018");
        memo018.setAttribute("id", "メモ018");
        String memo018Value = String.valueOf(mainApplication.getMemo018());
        memo018.appendChild(document.createTextNode(memo018Value));
        info.appendChild(memo018);

        //　店舗メモ019
        Element memo019 = document.createElement("memo019");
        memo019.setAttribute("id", "メモ019");
        String memo019Value = String.valueOf(mainApplication.getMemo019());
        memo019.appendChild(document.createTextNode(memo019Value));
        info.appendChild(memo019);

        //　店舗メモ020
        Element memo020 = document.createElement("memo020");
        memo020.setAttribute("id", "メモ020");
        String memo020Value = String.valueOf(mainApplication.getMemo020());
        memo020.appendChild(document.createTextNode(memo020Value));
        info.appendChild(memo020);

        // 内部ストレージに入れてる
        // 内部ストレージはアプリ固有の領域のため、アクセス権限対応は不要
        File file = new File(context.getFilesDir(), "info.xml");
        write(file, document);

        return document;
    }

    public static boolean write(File file, Document document) {

        // Transformerインスタンスの生成
        Transformer transformer = null;
        try {
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        }

        // Transformerの設定
        transformer.setOutputProperty("indent", "yes"); //改行指定
        //transformer.setOutputProperty("encoding", "Shift_JIS"); // エンコーディング
        transformer.setOutputProperty("encoding", "utf-8"); // エンコーディング

        // XMLファイルの作成
        try {
            transformer.transform(new DOMSource(document), new StreamResult(file));
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void updateText(MainApplication mainApplication, String id,String text) {

        Node node = mainApplication.getDocument().getElementsByTagName(id).item(0);

        if(node != null) {
            Node nodeValue = node.getFirstChild();
            if(nodeValue==null){
                // 子ノードを新規作成
                node.appendChild(mainApplication.getDocument().createTextNode(text));
            }else{
                // 子ノードに更新
                nodeValue.setTextContent(text);
            }
            File file = new File(mainApplication.getContext().getFilesDir(), "info.xml");
            CreateXML.write(file, mainApplication.getDocument());

        }
    }

    public static String[] getMemosTagName(){
        return new String[]{
                "memo001","memo002","memo003","memo004","memo005","memo006","memo007","memo008","memo009","memo010",
                "memo011","memo012","memo013","memo014","memo015","memo016","memo017","memo018","memo019","memo020"};
    }




}
