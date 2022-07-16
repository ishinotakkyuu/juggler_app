package jp.example.title_1;

public class FlagGradesListItems {

    private String ID = null;
    private String machineName = null;
    private String storeName = null;
    private String tableNumber = null;
    private String date = null;
    private String keepTime = null;

    public FlagGradesListItems(){}

    public FlagGradesListItems(String ID,String machineName,String storeName,String tableNumber,String date,String keepTime){
        this.ID = ID;
        this.machineName = machineName;
        this.storeName = storeName;
        this.tableNumber = tableNumber;
        this.date = date;
        this.keepTime = keepTime;
    }

    public void setID(String ID){ this.ID = ID; }

    public void setMachineName(String machineName){
        this.machineName = machineName;
    }

    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setDate(String date){ this.date = date; }

    public void setKeepTime(String keepTime){ this.keepTime = keepTime; }

    public String getID(){ return this.ID; }

    public String getMachineName(){
        return this.machineName;
    }

    public String getStoreName(){ return this.storeName; }

    public String getTableNumber() {
        return tableNumber;
    }

    public String getDate(){ return this.date; }

    public String getKeepTime(){ return this.keepTime; }


}
