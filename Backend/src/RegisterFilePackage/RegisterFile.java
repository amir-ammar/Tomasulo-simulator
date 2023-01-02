package RegisterFilePackage;
import DataBusPackage.DataBusObserver;
import LoadBufferPackage.LoadBufferObserver;
import MessagesPackage.Message;
import ReservationStationPackage.ReservationStation;
import ReservationStationPackage.ReservationStationObserver;
import StoreBuffer.StoreBuffer;
import StoreBuffer.StoreBufferObserver;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterFile implements ReservationStationObserver, RegisterFileSubject, DataBusObserver, LoadBufferObserver, StoreBufferObserver {

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
    public void register(RegisterFileObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(RegisterFileObserver observer) {
        observers.remove(observer);
    }



    /* Reservation start */

    /* Send data start */
    @Override
    public void notifyObservers(Message message, char type) {
        for (RegisterFileObserver o : observers) {
            if (o instanceof ReservationStation && type == 'R') {
                o.regFileUpdate(message);
            } else if (o instanceof StoreBuffer && type == 'S') {
                o.regFileUpdate(message);
            }
        }
    }
    /* Send data end */

    /* Receive data start */
    @Override
    public void ReservationUpdateRegisterFile(Message message) {
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
        notifyObservers(new Message(newMessage), 'R');
        String updateRegister = updatedRegister.split(":")[0].substring(1);
        registerFile[Integer.parseInt(updateRegister)].setQi(updateLabel);
    }
    /* Receive data end */

    /* Reservation end */


    /* LoadBuffer start */

    /* Send data start */

    /* Send data end */

    /* Receive data start */
    @Override
    public void LoadBufferUpdateRegFile(Message message) {
        System.out.println(message);
        String updatedRegister = message.getUpdatedRegister();
        String updateLabel = updatedRegister.split(":")[1];
        String updateRegister = updatedRegister.split(":")[0].substring(1);
        registerFile[Integer.parseInt(updateRegister)].setQi(updateLabel);
    }
    /* Receive data end */

    /* LoadBuffer end */


    /* StoreBuffer start */

    /* Send data start */

    /* Send data end */

    /* Receive data start */
    @Override
    public void storeBufferUpdateRegFile(Message message) {
        System.out.println(message);
        String [] requiredRegisters = message.getRequiredRegisters();
        StringBuilder requiredRegistersValue = new StringBuilder();

        for (String register : requiredRegisters) {
            RegisterFileItem item = registerFile[Integer.parseInt(register.substring(1))];
            requiredRegistersValue.append(item.getQi().equals("0") ? item.getContent() + " " : item.getQi() + " ");
        }
        HashMap<String, String> newMessage = new HashMap<>();
        newMessage.put("requiredRegistersValue", requiredRegistersValue.toString());
        notifyObservers(new Message(newMessage), 'S');
    }
    /* Receive data end */

    /* StoreBuffer end */



    /* DataBus start */

    /* Send data start */

    /* Send data end */

    /* Receive data start */

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
    /* Receive data end */

    /* DataBus end */




    public String toJson() {
        StringBuilder json = new StringBuilder("[");
        for (RegisterFileItem registerFileItem : registerFile) {
            json.append(registerFileItem.toJson()).append(",");
        }
        json = new StringBuilder(json.substring(0, json.length() - 1));
        json.append("]");
        return json.toString();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (RegisterFileItem i : registerFile) {
            s.append(i.toString()).append("\n");
        }
        return s.toString();
    }

    @Override
    public void ReservationUpdateInstructionQueue(Message message) {}
    @Override
    public void ReservationUpdateDataBus(Message message) {}
    @Override
    public void ReservationUpdateLoadBuffer(Message message) {}
    @Override
    public void ReservationUpdateStoreBuffer(Message message) {}
    @Override
    public void LoadBufferUpdateDataBus(Message message) {}
    @Override
    public void LoadBufferUpdateReservation(Message message) {}
    @Override
    public void storeBufferUpdateQueue(Message message) {}
    @Override
    public void storeBufferUpdateReservation(Message message) {}
    @Override
    public void LoadBufferUpdateQueue(Message message) {}
}
