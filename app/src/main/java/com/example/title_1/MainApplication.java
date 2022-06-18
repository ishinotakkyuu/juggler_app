package com.example.title_1;

import android.app.Application;
import android.content.Context;

import org.w3c.dom.Document;

import java.util.Random;

public class MainApplication extends Application {

    private Context context;
    private Document document;

    private int userId;
    private int machinePosition;
    private String totalGames;
    private String startGames;
    private String singleBig;
    private String cherryBig;
    private String singleReg;
    private String cherryReg;
    private String cherry;
    private String grape;
    private String store001;
    private String store002;
    private String store003;
    private String store004;
    private String store005;
    private String store006;
    private String store007;
    private String store008;
    private String store009;
    private String store010;
    private String store011;
    private String store012;
    private String store013;
    private String store014;
    private String store015;
    private String store016;
    private String store017;
    private String store018;
    private String store019;
    private String store020;

    // 初期セット
    public void init() {
        Random rnd = new Random();
        //乱数を取得する
        if(userId != 0){
            setUserId(rnd.nextInt(2147483646)+1);
        }
        setTotalGames("0");
        setStartGames("0");
        setSingleBig("0");
        setCherryBig("0");
        setSingleReg("0");
        setCherryReg("0");
        setCherry("0");
        setGrape("0");
    }


    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMachinePosition() {
        return machinePosition;
    }

    public void setMachinePosition(int machinePosition) {
        this.machinePosition = machinePosition;
    }

    public String getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(String total) {
        this.totalGames = total;
    }

    public String getStartGames() {
        return startGames;
    }

    public void setStartGames(String start) {
        this.startGames = start;
    }

    public String getSingleBig() {
        return singleBig;
    }

    public void setSingleBig(String aB) {
        this.singleBig = aB;
    }

    public String getCherryBig() {
        return cherryBig;
    }

    public void setCherryBig(String cB) {
        this.cherryBig = cB;
    }

    public String getSingleReg() {
        return singleReg;
    }

    public void setSingleReg(String aR) {
        this.singleReg = aR;
    }

    public String getCherryReg() {
        return cherryReg;
    }

    public void setCherryReg(String cR) {
        this.cherryReg = cR;
    }

    public String getCherry() {
        return cherry;
    }

    public void setCherry(String ch) {
        this.cherry = ch;
    }

    public String getGrape() {
        return grape;
    }

    public void setGrape(String gr) {
        this.grape = gr;
    }

    // 20店舗に対応

    public String getStore001() {
        return store001;
    }

    public void setStore001(String store001) {
        this.store001 = store001;
    }

    public String getStore002() {
        return store002;
    }

    public void setStore002(String store002) {
        this.store002 = store002;
    }

    public String getStore003() {
        return store003;
    }

    public void setStore003(String store003) {
        this.store003 = store003;
    }

    public String getStore004() {
        return store004;
    }

    public void setStore004(String store004) {
        this.store004 = store004;
    }

    public String getStore005() {
        return store005;
    }

    public void setStore005(String store005) {
        this.store005 = store005;
    }

    public String getStore006() {
        return store006;
    }

    public void setStore006(String store006) {
        this.store006 = store006;
    }

    public String getStore007() {
        return store007;
    }

    public void setStore007(String store007) {
        this.store007 = store007;
    }

    public String getStore008() {
        return store008;
    }

    public void setStore008(String store008) {
        this.store008 = store008;
    }

    public String getStore009() {
        return store009;
    }

    public void setStore009(String store009) {
        this.store009 = store009;
    }

    public String getStore010() {
        return store010;
    }

    public void setStore010(String store010) {
        this.store010 = store010;
    }

    public String getStore011() {
        return store011;
    }

    public void setStore011(String store011) {
        this.store011 = store011;
    }

    public String getStore012() {
        return store012;
    }

    public void setStore012(String store012) {
        this.store012 = store012;
    }

    public String getStore013() {
        return store013;
    }

    public void setStore013(String store013) {
        this.store013 = store013;
    }

    public String getStore014() {
        return store014;
    }

    public void setStore014(String store014) {
        this.store014 = store014;
    }

    public String getStore015() { return store015; }

    public void setStore015(String store015) {
        this.store015 = store015;
    }

    public String getStore016() {
        return store016;
    }

    public void setStore016(String store016) {
        this.store016 = store016;
    }

    public String getStore017() {
        return store017;
    }

    public void setStore017(String store017) {
        this.store017 = store017;
    }

    public String getStore018() {
        return store018;
    }

    public void setStore018(String store018) {
        this.store018 = store018;
    }

    public String getStore019() {
        return store019;
    }

    public void setStore019(String store019) {
        this.store019 = store019;
    }

    public String getStore020() {
        return store020;
    }

    public void setStore020(String store020) {
        this.store020 = store020;
    }

}
