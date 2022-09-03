package delson.android.j_management_app;

public class ToolDesignListItems {

    private String ID;
    private String No;
    private String designText;

    public ToolDesignListItems(String ID, String No, String designText) {
        this.ID = ID;
        this.No = No;
        this.designText = designText;
    }

    public String getID() {
        return ID;
    }

    public String getNo() {
        return No;
    }

    public String getDesignText() {
        return designText;
    }

}
