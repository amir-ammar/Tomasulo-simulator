package StoreBuffer;

import DataBusPackage.DataBusObserver;
import InstructionQueuePackage.InstructionQueue;
import InstructionQueuePackage.InstructionQueueObserver;
import MainPackage.Executor;
import MainPackage.ExecutorObserver;
import MessagesPackage.Message;
import RegisterFilePackage.RegisterFile;
import RegisterFilePackage.RegisterFileObserver;
import ReservationStationPackage.ReservationStationObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreBuffer implements InstructionQueueObserver, StoreBufferSubject, RegisterFileObserver, ReservationStationObserver, ExecutorObserver, DataBusObserver {

    StoreBufferItem[] storeBuffer;

    int empty;

    int pointer;

    boolean newInstruction;

    ArrayList<StoreBufferObserver> observers;

    public StoreBuffer() {
        storeBuffer = new StoreBufferItem[3];
        observers = new ArrayList<>();
        empty = 3;
        pointer = 0;
        newInstruction = false;
        for (int i = 0; i < 3; i++) {
            storeBuffer[i] = new StoreBufferItem("S" + i);
        }
    }

    /* Operations start */
    boolean isExecuting(int i) {
        return storeBuffer[i].isBusy() &&
                (storeBuffer[i].getLatency() > 0 || storeBuffer[i].getLatency() == -1);
    }
    void updateLatency() {
        for (int i = 0; i < 3; i++) {
            if (isExecuting(i))
                storeBuffer[i].setLatency(storeBuffer[i].getLatency() - 1);
        }
    }

    void removeFinishedInstructions() {
        for (int i = 0; i < 3; i++) {
            if (storeBuffer[i].isBusy() && storeBuffer[i].getLatency() == -2) {
                storeBuffer[i].clear();
                empty++;
                pointer = i;
            }
        }
    }

    void updatePointer() {
        pointer = (pointer + 1) % 3;
    }

    @Override
    public void registerObserver(StoreBufferObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(StoreBufferObserver o) {
        observers.remove(o);
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < 3; i++) {
            json.append(storeBuffer[i].toJsonString());
            if (i != 2) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // add line
        sb.append("\n");
        sb.append("Store Buffer:\n");
        for (int i = 0; i < 3; i++) {
            sb.append(storeBuffer[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }


    /* Operations end */


    /* InstructionQueue start */

    /* Send data start */
    @Override
    public void notifyInstructionQueue() {
        HashMap<String, String> message = new HashMap<>();
        message.put("emptySize", empty + "");
        for (StoreBufferObserver o : observers) {
            if (o instanceof InstructionQueue) {
                o.storeBufferUpdateQueue(new Message(message));
            }
        }
    }
    /* Send data end */

    /* Receive data start */
    public StoreBufferItem convertInstructionToStoreBufferItem(String newInstruction) {
        StoreBufferItem item = new StoreBufferItem();
        String [] newInstructionFields = newInstruction.split(" ");
        item.setBusy(true);
        item.setLabel("S" + pointer);
        item.setQ(newInstructionFields[1]);
        item.setAddress(Integer.parseInt(newInstructionFields[2]));
        item.setLatency(Integer.parseInt(newInstructionFields[3]));
        item.setIssueCycle(Integer.parseInt(newInstructionFields[4]));
        return item;
    }

    @Override
    public void instructionQueueUpdate(Message message) {
        StoreBufferItem item = convertInstructionToStoreBufferItem(message.getNewInstruction());
        if (item.label == null) return;
        storeBuffer[pointer] = item;
        updatePointer();
        empty--;
        newInstruction = true;
    }
    /* Receive data end */

    /* InstructionQueue end */





    /* Register file start */

    /* Send data start */

    public Message generateRegisterFileMessage() {
        String requiredRegisters = "";
        int idx;
        idx = pointer;
        if (idx == 0) idx = 2;
        else idx = idx - 1;
        requiredRegisters = storeBuffer[idx].getQ();
        HashMap<String, String> map = new HashMap<>();
        map.put("requiredRegisters", requiredRegisters);
        return new Message(map);
    }

    @Override
    public void notifyRegisterFile() {
        for (StoreBufferObserver o : observers) {
            if (o instanceof RegisterFile && newInstruction) {
                o.storeBufferUpdateRegFile(generateRegisterFileMessage());
            }
        }
    }

    /* Send data end */

    /* receive data start */
    @Override
    public void regFileUpdate(Message message) {
        int idx = pointer;
        if (idx == 0) idx = 2;
        else idx = idx - 1;
        String[] parts = message.getRequiredRegistersValue().split(" ");
        String part1 = parts[0];

        // check if part1 is a number ot String
        if (part1.matches("-?\\d+(\\.\\d+)?")) {
            storeBuffer[idx].setV(Integer.parseInt(part1));
            storeBuffer[idx].setQ(null);
        } else {
            storeBuffer[idx].setV(null);
            storeBuffer[idx].setQ(part1);
        }
    }

    /* receive data end */

    /* Register file end */



    /* Reservation station start */

    /* Send data start */
    String generateReservationStationMessage() {
        StringBuilder requiredRegisters = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (storeBuffer[i].isBusy() && storeBuffer[i].getQ() != null) {
                requiredRegisters.append(storeBuffer[i].getQ()).append(" ");
            }
        }
        return requiredRegisters.toString();
    }

    @Override
    public void notifyReservationStation() {
        Message message = new Message();
        message.setMessage("requiredLabels", generateReservationStationMessage());
        for (StoreBufferObserver o : observers) {
            if (o instanceof ReservationStationObserver) {
                o.storeBufferUpdateReservation(message);
            }
        }
    }
    /* Send data end */

    /* Receive data start */
    @Override
    public void ReservationUpdateStoreBuffer(Message message) {
        notifyReservationStation();
    }

    /* Receive data end */

    /* Reservation station end */




    /* Data bus start */

    /* Send data start */

    /* Send data end */

    /* Receive data start */
    @Override
    public void DataBusUpdate(Message message) {
        String tag = message.getDataBusUpdate().split(" ")[0];
        int value = Integer.parseInt(message.getDataBusUpdate().split(" ")[2]);

        for (int i = 0; i < 3; i++) {
            if (!storeBuffer[i].isBusy()) continue;
            if (storeBuffer[i].getQ() != null && storeBuffer[i].getQ().equals(tag)) {
                storeBuffer[i].setV(value);
                storeBuffer[i].setQ(null);
            }
        }
    }

    /* Receive data end */

    /* Data bus end */
    @Override
    public void updateClock() {
        removeFinishedInstructions();
        updateLatency();
        notifyInstructionQueue();
        notifyRegisterFile();
        newInstruction = false;
        System.out.println(Executor.storeBuffer);
    }

    @Override
    public void ReservationUpdateInstructionQueue(Message message) {}
    @Override
    public void ReservationUpdateRegisterFile(Message message) {}
    @Override
    public void ReservationUpdateDataBus(Message message) {}
    @Override
    public void ReservationUpdateLoadBuffer(Message message) {}
}
