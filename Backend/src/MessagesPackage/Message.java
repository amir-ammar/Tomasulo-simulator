package MessagesPackage;

import java.util.HashMap;

public class Message {

    // this class is used to send messages from the reservation station to the register file
    HashMap<String, String> message;

    public Message() {
        message = new HashMap<>();
    }

    public Message(HashMap<String, String> message) {
        this.message = message;
    }

    public String getMessage(String key) {
        return message.get(key);
    }

    public void setMessage(String key, String message) {
        this.message.put(key, message);
    }

    public HashMap<String, String> getMessages() {
        return message;
    }

    public void setMessages(HashMap<String, String> message) {
        this.message = message;
    }

    public String [] getRequiredRegisters() {
        return message.get("requiredRegisters").split(" ");
    }
    public String [] getRequiredLabels() {
        return message.get("requiredLabels").split(" ");
    }


    public String getUpdatedRegister() {
        return message.get("updatedRegister");
    }

    public String getRequiredRegistersValue() {
        return message.get("requiredRegistersValue");
    }


    public int getAddSubStationSize() {
        return Integer.parseInt(message.get("addSubStationSize"));
    }

    public int getMulDivStationSize() {
        return Integer.parseInt(message.get("mulDivStationSize"));
    }

    public String getNewInstruction() {
        return message.get("newInstruction");
    }

    public String getDataBusUpdate() {
        return message.get("dataBus");
    }

    public int getBufferSize() {
        return Integer.parseInt(message.get("emptySize"));
    }

    public String getLoadBufferUpdate() {
        return message.get("readyToWrite");
    }

    public String getWriteToDataBusLabel() {
        return message.get("WriteToDataBusLabel");
    }

    @Override
    public String toString() {
        return "Message{" +
                "message=" + message +
                '}';
    }

}
