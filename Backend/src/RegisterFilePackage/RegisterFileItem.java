package RegisterFilePackage;

public class RegisterFileItem {

    String registerName;
    String Qi;
    int content;


    public RegisterFileItem(String registerName, String Qi, int content) {
        this.registerName = registerName;
        this.Qi = Qi;
        this.content = content;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getQi() {
        return Qi;
    }

    public void setQi(String Qi) {
        this.Qi = Qi;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    // toString
    @Override
    public String toString() {
        return "RegisterFileItem{" +
                "registerName='" + registerName + '\'' +
                ", Qi='" + Qi + '\'' +
                ", content=" + content +
                '}';
    }

    // function to convert the register file to a json string to be sent to the frontend
    public String toJson() {
        return "{\"label\":\"" + registerName + "\",\"Qi\":\"" + Qi + "\",\"content\":" + content + "}";
    }


}
