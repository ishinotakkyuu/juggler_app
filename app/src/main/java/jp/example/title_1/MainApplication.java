package jp.example.title_1;

import android.app.Application;
import android.content.Context;
import org.w3c.dom.Document;
import java.util.Random;

public class MainApplication extends Application {

    private Context context;
    private Document document;

    private int userId,machinePosition;
    private String totalGames,startGames,singleBig,cherryBig,singleReg,cherryReg,cherry,grape;
    private String store001,store002,store003,store004,store005,store006,store007,store008,store009,store010,
                   store011,store012,store013,store014,store015,store016,store017,store018,store019,store020;
    private String memo001,memo002,memo003,memo004,memo005,memo006,memo007,memo008,memo009,memo010,
                   memo011,memo012,memo013,memo014,memo015,memo016,memo017,memo018,memo019,memo020;

    // 初期セット
    public void init() {
        Random rnd = new Random();
        //乱数を取得する
        if(userId == 0){
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

    public String getMemo001(){
        return memo001;
    }

    public void setMemo001(String memo001){
        this.memo001 = memo001;
    }

    public String getMemo002() {
        return memo002;
    }

    public void setMemo002(String memo002) {
        this.memo002 = memo002;
    }

    public String getMemo003() {
        return memo003;
    }

    public void setMemo003(String memo003) {
        this.memo003 = memo003;
    }

    public String getMemo004() {
        return memo004;
    }

    public void setMemo004(String memo004) {
        this.memo004 = memo004;
    }

    public String getMemo005() {
        return memo005;
    }

    public void setMemo005(String memo005) {
        this.memo005 = memo005;
    }

    public String getMemo006() {
        return memo006;
    }

    public void setMemo006(String memo006) {
        this.memo006 = memo006;
    }

    public String getMemo007() {
        return memo007;
    }

    public void setMemo007(String memo007) {
        this.memo007 = memo007;
    }

    public String getMemo008() {
        return memo008;
    }

    public void setMemo008(String memo008) {
        this.memo008 = memo008;
    }

    public String getMemo009() {
        return memo009;
    }

    public void setMemo009(String memo009) {
        this.memo009 = memo009;
    }

    public String getMemo010() {
        return memo010;
    }

    public void setMemo010(String memo010) {
        this.memo010 = memo010;
    }

    public String getMemo011() {
        return memo011;
    }

    public void setMemo011(String memo011) {
        this.memo011 = memo011;
    }

    public String getMemo012() {
        return memo012;
    }

    public void setMemo012(String memo012) {
        this.memo012 = memo012;
    }

    public String getMemo013() {
        return memo013;
    }

    public void setMemo013(String memo013) {
        this.memo013 = memo013;
    }

    public String getMemo014() {
        return memo014;
    }

    public void setMemo014(String memo014) {
        this.memo014 = memo014;
    }

    public String getMemo015() {
        return memo015;
    }

    public void setMemo015(String memo015) {
        this.memo015 = memo015;
    }

    public String getMemo016() {
        return memo016;
    }

    public void setMemo016(String memo016) {
        this.memo016 = memo016;
    }

    public String getMemo017() {
        return memo017;
    }

    public void setMemo017(String memo017) {
        this.memo017 = memo017;
    }

    public String getMemo018() {
        return memo018;
    }

    public void setMemo018(String memo018) {
        this.memo018 = memo018;
    }

    public String getMemo019() {
        return memo019;
    }

    public void setMemo019(String memo019) {
        this.memo019 = memo019;
    }

    public String getMemo020() {
        return memo020;
    }

    public void setMemo020(String memo020) {
        this.memo020 = memo020;
    }

    public String[] getMemos(){
        return new String[]{
                memo001,memo002,memo003,memo004,memo005,memo006,memo007,memo008,memo009,memo010,
                memo011,memo012,memo013,memo014,memo015,memo016,memo017,memo018,memo019,memo020};
    }

}
