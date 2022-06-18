package com.example.title_1;

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

        Document document = documentBuilder.newDocument();

        Element info = document.createElement("info");
        document.appendChild(info);

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

        // 内部ストレージに入れてる
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
        transformer.setOutputProperty("encoding", "Shift_JIS"); // エンコーディング

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
}
