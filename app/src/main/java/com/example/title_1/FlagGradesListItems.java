package com.example.title_1;

public class FlagGradesListItems {

    private String machineName = null;
    private String storeName = null;
    private String keepTime = null;

    public FlagGradesListItems(){}

    public FlagGradesListItems(String machineName,String storeName,String keepTime){
        this.machineName = machineName;
        this.storeName = storeName;
        this.keepTime = keepTime;
    }

    public void setMachineName(String machineName){
        this.machineName = machineName;
    }

    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public void setKeepTime(String keepTime){ this.keepTime = keepTime; }

    public String getMachineName(){
        return this.machineName;
    }

    public String getStoreName(){
        return this.storeName;
    }

    public String getKeepTime(){ return this.keepTime; }


}
