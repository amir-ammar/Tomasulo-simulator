package InstructionQueuePackage;
import LoadBufferPackage.LoadBuffer;
import LoadBufferPackage.LoadBufferObserver;
import MainPackage.Executor;
import MainPackage.ExecutorObserver;
import MessagesPackage.Message;
import ReservationStationPackage.ReservationStation;
import ReservationStationPackage.ReservationStationObserver;
import StoreBuffer.StoreBuffer;
import StoreBuffer.StoreBufferObserver;

import java.util.*;

public class InstructionQueue implements InstructionQueueSubject, ExecutorObserver, ReservationStationObserver, LoadBufferObserver, StoreBufferObserver {

    ArrayList<InstructionQueueObserver> observers;
    Queue<Instruction> instructions;

    boolean send;


    public InstructionQueue() {
        instructions = new ArrayDeque<>();
        observers = new ArrayList<>();
    }

    public void enqueue(Instruction instruction) {
        instructions.add(instruction);
    }

    public Instruction dequeue() {
        return instructions.poll();
    }

    @Override
    public void registerObserver(InstructionQueueObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(InstructionQueueObserver o) {
        observers.remove(o);
    }


    /* Reservation Station start */

    /* Send Data start */
    @Override
    public void notifyReservationStation() {
        HashMap<String, String> message = new HashMap<>();
        for (InstructionQueueObserver o : observers) {
            if (o instanceof ReservationStation) {
                if(instructions.size() > 0) {
                    instructions.peek().setIssueCycle(Executor.cycle);
                    System.out.println("InstructionQueue: " + instructions.peek());
                    message.put("newInstruction", Objects.requireNonNull(instructions.poll()).toString());
                    o.instructionQueueUpdate(new Message(message));
                } else System.out.println("Instruction Queue is empty");
            }
        }
    }
    /* Send Data end */

    /* Receive data start  */
    @Override
    public void ReservationUpdateInstructionQueue(Message message) {
        int addSubStationSize = message.getAddSubStationSize();
        int mulDivStationSize = message.getMulDivStationSize();
        Instruction next = instructions.peek();
        if (addSubStationSize > 0 && instructions.size() > 0 &&
                (Objects.requireNonNull(next).getOp().equals("ADD") ||
                        Objects.requireNonNull(next).getOp().equals("SUB")) && !send)
        {
            notifyReservationStation();
            send = true;
        }
        else if (mulDivStationSize > 0 && instructions.size() > 0 &&
                (Objects.requireNonNull(next).getOp().equals("MUL") ||
                        Objects.requireNonNull(next).getOp().equals("DIV")) && !send)
        {
            notifyReservationStation();
            send = true;
        }
        else {
            System.out.println("Can not notify reservation station");
        }
    }
    /* Receive data end  */

    /* Reservation Station end */



    /* Load Buffer start */

    /* Send Data start */
    @Override
    public void notifyLoadBuffer() {
        HashMap<String, String> message = new HashMap<>();
        for (InstructionQueueObserver o : observers) {
            if (o instanceof LoadBuffer) {
                if(instructions.size() > 0) {
                    instructions.peek().setIssueCycle(Executor.cycle);
                    message.put("newInstruction", Objects.requireNonNull(instructions.poll()).toString());
                    o.instructionQueueUpdate(new Message(message));
                } else System.out.println("Instruction Queue is empty");
            }
        }
    }
    /* Send Data end */

    /* Receive data start */

    @Override
    public void LoadBufferUpdateQueue(Message message) {
        int loadBufferSize = message.getBufferSize();
        Instruction next = instructions.peek();
        if (loadBufferSize > 0 && instructions.size() > 0 &&
                (Objects.requireNonNull(next).getOp().equals("LW")) && !send)
        {
            notifyLoadBuffer();
            send = true;
        }
        else {
            System.out.println("Can not notify load buffer");
        }
    }

    /* Receive data end  */

    /* Load Buffer end */



    /* Store Buffer start */

    /* Send Data start */
    @Override
    public void notifyStoreBuffer() {
        HashMap<String, String> message = new HashMap<>();
        for (InstructionQueueObserver o : observers) {
            if (o instanceof StoreBuffer) {
                if(instructions.size() > 0) {
                    instructions.peek().setIssueCycle(Executor.cycle);
                    System.out.println("InstructionQueue: " + instructions.peek());
                    message.put("newInstruction", Objects.requireNonNull(instructions.poll()).toString());
                    o.instructionQueueUpdate(new Message(message));
                } else System.out.println("Instruction Queue is empty");
            }
        }
    }
    /* Send Data end */

    /* Receive data start */
    @Override
    public void storeBufferUpdateQueue(Message message) {
        int storeBufferSize = message.getBufferSize();
        Instruction next = instructions.peek();
        if (storeBufferSize > 0 && instructions.size() > 0 &&
                (Objects.requireNonNull(next).getOp().equals("SW")) && !send)
        {
            notifyStoreBuffer();
            send = true;
        }
        else {
            System.out.println("Can not notify store buffer");
        }
    }
    /* Receive data end  */

    /* Store Buffer end */


    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (Instruction instruction : instructions) {
            json.append(instruction.toJson()).append(",");
        }
        json.deleteCharAt(json.length() - 1);
        json.append("]");
        return json.toString();
    }


    @Override
    public void ReservationUpdateRegisterFile(Message message) {}
    @Override
    public void ReservationUpdateDataBus(Message message) {}
    @Override
    public void ReservationUpdateLoadBuffer(Message message) {}
    @Override
    public void ReservationUpdateStoreBuffer(Message message) {}
    @Override
    public void storeBufferUpdateRegFile(Message message) {}
    @Override
    public void storeBufferUpdateReservation(Message message) {}
    @Override
    public void LoadBufferUpdateRegFile(Message message) {}
    @Override
    public void LoadBufferUpdateDataBus(Message message) {}
    @Override
    public void LoadBufferUpdateReservation(Message message) {}

    @Override
    public void updateClock() {
        send = false;
    }
}
