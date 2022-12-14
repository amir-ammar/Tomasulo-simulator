package DataBusPackage;

import MessagesPackage.Message;
import ReservationStationPackage.ReservationStationObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBus implements DataBusSubject, ReservationStationObserver {

    DataBusItem data;

    ArrayList<DataBusObserver> observers;

    public DataBus() {
        data = new DataBusItem();
        observers = new ArrayList<>();
    }

    public DataBus(DataBusItem data) {
        this.data = data;
        observers = new ArrayList<>();
    }

    public void setData(DataBusItem data) {
        this.data = data;
    }

    public DataBusItem getData() {
        return data;
    }


    void clearData() {
        data = new DataBusItem();
    }

    public void registerObserver(DataBusObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(DataBusObserver o) {
        observers.remove(o);
    }


    @Override
    public void notifyObservers() {
        HashMap<String, String> message = new HashMap<>();
        message.put("dataBus", data.toString());
        for (DataBusObserver o : observers) {
            if (!data.getTag().equals("")) {
                o.DataBusUpdate(new Message(message));
//                clearData();
            }
        }
    }

    public String toJson() {
        return data.toJson();
    }

    @Override
    public void updateInstructionQueue(Message message) {

    }

    @Override
    public void updateRegisterFile(Message message) {

    }


    public void updateDataBus(Message message) {
        String [] dataBusUpdate = message.getDataBusUpdate().split(" ");
        if (dataBusUpdate[0].equals("write")) {
            data.setTag(dataBusUpdate[1]);
            data.setRegister(dataBusUpdate[2]);
            data.setValue(Integer.parseInt(dataBusUpdate[3]));
        } else {
            notifyObservers();
        }
    }

    // toString method
    @Override
    public String toString() {
        if (data == null) {
            return "null";
        }
        return data.toString();
    }
}
