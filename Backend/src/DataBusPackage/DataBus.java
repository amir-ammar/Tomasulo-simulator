package DataBusPackage;
import LoadBufferPackage.LoadBufferObserver;
import MessagesPackage.Message;
import ReservationStationPackage.ReservationStationObserver;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBus implements DataBusSubject, ReservationStationObserver, LoadBufferObserver {

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
        if (!data.getTag().equals("")) {
            for (DataBusObserver o : observers) {
                o.DataBusUpdate(new Message(message));
//                clearData();
            }
        }
    }

    @Override
    public void LoadBufferUpdateDataBus(Message message) {
        System.out.println("LoadBufferUpdate -> DataBus: " + message.getDataBusUpdate());
        String [] dataBusUpdate = message.getDataBusUpdate().split(" ");
        if (dataBusUpdate[0].equals("write")) {
            data.setTag(dataBusUpdate[1]);
            data.setRegister(dataBusUpdate[2]);
            data.setValue(Integer.parseInt(dataBusUpdate[3]));
        } else {
            notifyObservers();
        }
    }

    public void ReservationUpdateDataBus(Message message) {
        System.out.println("ReservationUpdate -> DataBus: " + message.getDataBusUpdate());
        String [] dataBusUpdate = message.getDataBusUpdate().split(" ");
        if (dataBusUpdate[0].equals("write")) {
            data.setTag(dataBusUpdate[1]);
            data.setRegister(dataBusUpdate[2]);
            data.setValue(Integer.parseInt(dataBusUpdate[3]));
        } else {
            notifyObservers();
        }
    }

    public String toJson() {
        return data.toJson();
    }


    @Override
    public String toString() {
        if (data == null) {
            return "null";
        }
        return data.toString();
    }


    @Override
    public void ReservationUpdateLoadBuffer(Message message) {}

    @Override
    public void ReservationUpdateStoreBuffer(Message message) {

    }
    @Override
    public void LoadBufferUpdateQueue(Message message) {}
    @Override
    public void LoadBufferUpdateRegFile(Message message) {}
    @Override
    public void LoadBufferUpdateReservation(Message message) {}
    @Override
    public void ReservationUpdateInstructionQueue(Message message) {}
    @Override
    public void ReservationUpdateRegisterFile(Message message) {}

}
