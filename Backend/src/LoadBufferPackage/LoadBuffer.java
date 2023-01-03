package LoadBufferPackage;

import DataBusPackage.DataBus;
import InstructionQueuePackage.InstructionQueue;
import InstructionQueuePackage.InstructionQueueObserver;
import MainPackage.Executor;
import MainPackage.ExecutorObserver;
import MessagesPackage.Message;
import RegisterFilePackage.RegisterFile;
import ReservationStationPackage.ReservationStation;
import ReservationStationPackage.ReservationStationObserver;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadBuffer implements InstructionQueueObserver, ExecutorObserver, LoadBufferSubject, ReservationStationObserver {

    LoadBufferItem [] loadBuffer;

    int empty;

    int pointer;

    boolean newInstruction;

    ArrayList<LoadBufferObserver> observers;


    public LoadBuffer() {
        loadBuffer = new LoadBufferItem[3];
        observers = new ArrayList<>();
        empty = 3;
        pointer = 0;
        newInstruction = false;
        for (int i = 0; i < 3; i++) {
            loadBuffer[i] = new LoadBufferItem("L" + i);
        }
    }

    /* Operations start */

    boolean isExecuting(int i) {
        return loadBuffer[i].isBusy() &&
                    (loadBuffer[i].getLatency() > 0 || loadBuffer[i].getLatency() == -1);
    }
    void updateLatency() {
        for (int i = 0; i < 3; i++) {
            if (isExecuting(i))
                loadBuffer[i].setLatency(loadBuffer[i].getLatency() - 1);
        }
    }

    void removeFinishedInstructions() {
        for (int i = 0; i < 3; i++) {
            if (loadBuffer[i].isBusy() && loadBuffer[i].getLatency() == -2) {
                loadBuffer[i].clear();
                empty++;
                pointer = i;
            }
        }
    }

    void updatePointer() {
        pointer = (pointer + 1) % 3;
    }

    @Override
    public void registerObserver(LoadBufferObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(LoadBufferObserver o) {
        observers.remove(o);
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        for (int i = 0; i < 3; i++) {
            json.append(loadBuffer[i].toJson());
            if (i != 2) {
                json.append(",");
            }
        }
        json.append("}");
        return json.toString();
    }

    // toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // add line
        sb.append("\n");
        sb.append("Load Buffer:\n");
        for (int i = 0; i < 3; i++) {
            sb.append(loadBuffer[i].toString());
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
        for (LoadBufferObserver o : observers) {
            if (o instanceof InstructionQueue) {
                o.LoadBufferUpdateQueue(new Message(message));
            }
        }
    }

    /* Send data end */

    /* receive data start */
    public LoadBufferItem convertInstructionToLoadBufferItem(String newInstruction) {
        System.out.println(newInstruction);
        LoadBufferItem item = new LoadBufferItem();
        String [] newInstructionFields = newInstruction.split(" ");
        item.setBusy(true);
        item.setLabel("L" + pointer);
        item.setTargetReg(newInstructionFields[1]);
        item.setAddress(Integer.parseInt(newInstructionFields[2]));
        item.setLatency(Integer.parseInt(newInstructionFields[3]));
        item.setIssueCycle(Integer.parseInt(newInstructionFields[4]));
        return item;
    }



    @Override
    public void instructionQueueUpdate(Message message) {
        LoadBufferItem item = convertInstructionToLoadBufferItem(message.getNewInstruction());
        if (item.label == null) return;
        loadBuffer[pointer] = item;
        updatePointer();
        empty--;
        newInstruction = true;
    }
    /* receive data end */

    /* InstructionQueue end */




    /* RegFile start */

    /* Send data start */
    public Message generateRegisterFileMessage() {
        String updatedRegister = "";
        int idx;

        idx = pointer;
        if (idx == 0) idx = 2;
        else idx = idx - 1;

        updatedRegister = loadBuffer[idx].getTargetReg() + ":" + loadBuffer[idx].getLabel();
        HashMap<String, String> map = new HashMap<>();
        map.put("updatedRegister", updatedRegister);
        return new Message(map);
    }

    @Override
    public void notifyRegFile() {
        for (LoadBufferObserver o : observers) {
            if (o instanceof RegisterFile && newInstruction) {
                o.LoadBufferUpdateRegFile(generateRegisterFileMessage());
            }
        }
    }
    /* Send data end */

    /* receive data start */

    /* receive data end */

    /* RegFile end */


    /* ReservationStation start */

    /* Send data start */

    Message generateReservationMessage () {
        Message message = new Message();
        StringBuilder readyToWrite = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (loadBuffer[i].isBusy() && loadBuffer[i].getLatency() == 0) {
                readyToWrite.
                        append(loadBuffer[i].getLabel()).
                        append(":").
                        append(loadBuffer[i].getIssueCycle()).
                        append(",");
            }
        }
        if (readyToWrite.length() > 0) {
            readyToWrite.deleteCharAt(readyToWrite.length() - 1);
        }
        message.setMessage("readyToWrite", readyToWrite.toString());
        return message;
    }

    @Override
    public void notifyReservation() {
        for (LoadBufferObserver o : observers) {
            if (o instanceof ReservationStation) {
                o.LoadBufferUpdateReservation(generateReservationMessage());
            }
        }
    }

    /* Send data end */

    /* receive data end */
    @Override
    public void ReservationUpdateLoadBuffer(Message message) { // write to data bus
        char l = message.getWriteToDataBusLabel().charAt(0);
        int idx = Integer.parseInt(message.getWriteToDataBusLabel().substring(1));
        if (l == 'L') {
            String writeBackInstruction = "write " + loadBuffer[idx].getLabel() +
                    " " + loadBuffer[idx].getTargetReg() + " " + Executor.memory.read(loadBuffer[idx].getAddress());
            loadBuffer[idx].setLatency(-1);
            notifyDataBus(writeBackInstruction);
        } else {
            System.out.println("load buffer will not write");
        }
    }
    /* receive data end */

    /* ReservationStation end */




    /* DataBus start */

    /* Send data start */
    @Override
    public void notifyDataBus(String data) {
        HashMap<String, String> message = new HashMap<>();
        message.put("dataBus", data);
        for (LoadBufferObserver o : observers) {
            if (o instanceof DataBus) {
                System.out.println("DataBus: " + data);
                o.LoadBufferUpdateDataBus(new Message(message));
            }
        }
    }

    void writeToDataBus() {
        notifyReservation();
    }
    /* Send data end */

    /* receive data start */

    /* receive data end */

    /* DataBus end */





    @Override
    public void updateClock() {
        writeToDataBus();
        removeFinishedInstructions();
        updateLatency();
        notifyInstructionQueue();
        notifyRegFile();
        newInstruction = false;
        System.out.println(Executor.loadBuffer);
    }

    @Override
    public void ReservationUpdateStoreBuffer(Message message) {}
    @Override
    public void ReservationUpdateInstructionQueue(Message message) {

    }
    @Override
    public void ReservationUpdateRegisterFile(Message message) {

    }
    @Override
    public void ReservationUpdateDataBus(Message message) {

    }
}
