package DataBusPackage;

public class DataBusItem {
    String tag;
    String register;

    int value;



    public DataBusItem() {
        tag = "";
        register = "";
        value = 0;
    }
    public DataBusItem(String tag, String register, int value) {
        this.tag = tag;
        this.register = register;
        this.value = value;
    }



    public String getTag() {
        return tag;
    }

    public void setTag(String t) {
        this.tag = t;
    }

    public String getRegister() {
        return this.register;
    }

    public void setRegister(String r) {
        this.register = r;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int v) {
        this.value = v;
    }

    // function to convert dataBusItem to json string
    public String toJson() {
        return "{\"tag\":\"" + tag + "\",\"register\":\"" + register + "\",\"value\":" + value + "}";
    }

    // toString method
    @Override
    public String toString() {
        return tag + " " + register + " " + value;
    }


}
