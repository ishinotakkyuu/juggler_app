package com.example.title_1;

import android.content.Context;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


class ReadXML {

    private static final String fileName = "info.xml";

    static Document readXML(Context context) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(context.getFilesDir(),fileName));
        } catch ( ParserConfigurationException | IOException | SAXException e ) {
            return null;
        }
        return document;
    }

    static void readInfo(MainApplication mainApplication) {

        String text;

        Node info = mainApplication.getDocument().getDocumentElement();
        Node elementNodes = info.getFirstChild();
        while(elementNodes != null) {
            String elementNodesNodeName = elementNodes.getNodeName();
            switch (elementNodesNodeName) {

                case "userId":
                    Node textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setUserId(Integer.parseInt(text));
                    }
                    break;

                case "machineName":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMachinePosition(Integer.parseInt(text));
                    }
                    break;
                case "total":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setTotalGames(text);
                    }
                    break;
                case "start":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStartGames(text);
                    }
                    break;
                case "aB":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setSingleBig(text);
                    }
                    break;
                case "cB":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setCherryBig(text);
                    }
                    break;
                case "aR":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setSingleReg(text);
                    }
                    break;
                case "cR":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setCherryReg(text);
                    }
                    break;
                case "ch":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setCherry(text);
                    }
                    break;
                case "gr":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setGrape(text);
                    }
                    break;

                case "store001":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore001(text);
                    }
                    break;
                case "store002":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore002(text);
                    }
                    break;
                case "store003":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore003(text);
                    }
                    break;
                case "store004":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore004(text);
                    }
                    break;
                case "store005":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore005(text);
                    }
                    break;

                case "store006":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore006(text);
                    }
                    break;

                case "store007":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore007(text);
                    }
                    break;

                case "store008":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore008(text);
                    }
                    break;

                case "store009":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore009(text);
                    }
                    break;

                case "store010":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore010(text);
                    }
                    break;

                case "store011":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore011(text);
                    }
                    break;

                case "store012":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore012(text);
                    }
                    break;

                case "store013":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore013(text);
                    }
                    break;

                case "store014":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore014(text);
                    }
                    break;

                case "store015":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore015(text);
                    }
                    break;

                case "store016":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore016(text);
                    }
                    break;

                case "store017":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore017(text);
                    }
                    break;

                case "store018":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore018(text);
                    }
                    break;

                case "store019":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore019(text);
                    }
                    break;

                case "store020":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setStore020(text);
                    }
                    break;

                case "memo001":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo001(text);
                    }
                    break;

                case "memo002":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo002(text);
                    }
                    break;

                case "memo003":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo003(text);
                    }
                    break;

                case "memo004":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo004(text);
                    }
                    break;

                case "memo005":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo005(text);
                    }
                    break;

                case "memo006":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo006(text);
                    }
                    break;

                case "memo007":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo007(text);
                    }
                    break;

                case "memo008":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo008(text);
                    }
                    break;

                case "memo009":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo009(text);
                    }
                    break;

                case "memo010":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo010(text);
                    }
                    break;

                case "memo011":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo011(text);
                    }
                    break;

                case "memo012":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo012(text);
                    }
                    break;

                case "memo013":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo013(text);
                    }
                    break;

                case "memo014":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo014(text);
                    }
                    break;

                case "memo015":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo015(text);
                    }
                    break;

                case "memo016":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo016(text);
                    }
                    break;

                case "memo017":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo017(text);
                    }
                    break;

                case "memo018":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo018(text);
                    }
                    break;

                case "memo019":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo019(text);
                    }
                    break;

                case "memo020":
                    textNode = elementNodes.getFirstChild();
                    if(textNode != null) {
                        text = textNode.getNodeValue();
                        mainApplication.setMemo020(text);
                    }
                    break;
            }
            elementNodes = elementNodes.getNextSibling();
        }
    }
}
