package RegisterFilePackage;

import DataBusPackage.DataBusObserver;
import MessagesPackage.Message;
import ReservationStationPackage.ReservationStationObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterFile implements ReservationStationObserver, RegisterFileSubject, DataBusObserver {

    RegisterFileItem[] registerFile;

    ArrayList<RegisterFileObserver> observers;

    public RegisterFile() {
        registerFile = new RegisterFileItem[12];
        observers = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            registerFile[i] = new RegisterFileItem("R" + i, "0", 0);
        }
    }

    public RegisterFileItem[] getRegisterFile() {
        return registerFile;
    }

    public void setRegisterFile(RegisterFileItem[] registerFile) {
        this.registerFile = registerFile;
    }


    @Override
    public void updateInstructionQueue(Message message) {
        // do nothing
    }

    @Override
    public void updateRegisterFile(Message message) {
        System.out.println(message);
        String [] requiredRegisters = message.getRequiredRegisters();
        String updatedRegister = message.getUpdatedRegister();
        String updateLabel = updatedRegister.split(":")[1];
        StringBuilder requiredRegistersValue = new StringBuilder();

        for (String register : requiredRegisters) {
            RegisterFileItem item = registerFile[Integer.parseInt(register.substring(1))];
            requiredRegistersValue.append(item.getQi().equals("0") ? item.getContent() + " " : item.getQi() + " ");
        }
        HashMap<String, String> newMessage = new HashMap<>();
        newMessage.put("requiredRegistersValue", requiredRegistersValue.toString());
        notifyObservers(new Message(newMessage));
        String updateRegister = updatedRegister.split(":")[0].substring(1);
        registerFile[Integer.parseInt(updateRegister)].setQi(updateLabel);
    }

    @Override
    public void updateDataBus(Message message) {

    }

    @Override
    public void register(RegisterFileObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(RegisterFileObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Message message) {
        for (RegisterFileObserver o : observers) {
            o.regFileUpdate(message);
        }
    }

    // function to convert the register file to a json string for the GUI
    public String toJson() {
        StringBuilder json = new StringBuilder("[");
        // add , after each item in the array except the last one
        for (RegisterFileItem registerFileItem : registerFile) {
            json.append(registerFileItem.toJson()).append(",");
        }
        json = new StringBuilder(json.substring(0, json.length() - 1));
        json.append("]");
        return json.toString();
    }

    @Override
    public void DataBusUpdate(Message message) {
        System.out.println("DataBusUpdate: " + message);
        String tag = message.getDataBusUpdate().split(" ")[0];
        int value = Integer.parseInt(message.getDataBusUpdate().split(" ")[2]);
        for (RegisterFileItem i : registerFile) {
            if (i.getQi().equals(tag)) {
                i.setContent(value);
                i.setQi("0");
            }
        }
    }

    // function toString
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (RegisterFileItem i : registerFile) {
            // put each register file item in a new line
            s.append(i.toString()).append("\n");
        }
        return s.toString();
    }

}
