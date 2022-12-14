package InstructionQueuePackage;

import MainPackage.Executor;
import MessagesPackage.Message;
import ReservationStationPackage.ReservationStationObserver;

import java.util.*;

public class InstructionQueue implements InstructionQueueSubject, ReservationStationObserver {

    ArrayList<InstructionQueueObserver> observers;
    Queue<Instruction> instructions;


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

    @Override
    public void notifyObservers() {
        HashMap<String, String> message = new HashMap<>();
        for (InstructionQueueObserver o : observers) {
            if(instructions.size() > 0) {
                instructions.peek().setIssueCycle(Executor.cycle);
                System.out.println("InstructionQueue: " + instructions.peek());
                message.put("newInstruction", Objects.requireNonNull(instructions.poll()).toString());
                o.instructionQueueUpdate(new Message(message));
            } else System.out.println("Instruction Queue is empty");
        }
    }

    @Override
    public void updateInstructionQueue(Message message) {
        int addSubStationSize = message.getAddSubStationSize();
        int mulDivStationSize = message.getMulDivStationSize();
        Instruction next = instructions.peek();
        if (addSubStationSize > 0 && instructions.size() > 0 &&
                (Objects.requireNonNull(next).getOp().equals("ADD") ||
                        Objects.requireNonNull(next).getOp().equals("SUB")))
        {
            notifyObservers();
        }
        else if (mulDivStationSize > 0 && instructions.size() > 0 &&
                (Objects.requireNonNull(next).getOp().equals("MUL") ||
                        Objects.requireNonNull(next).getOp().equals("DIV")))
        {
            notifyObservers();
        }
        else {
            System.out.println("Reservation station is full");
        }
    }

    @Override
    public void updateRegisterFile(Message message) {
        // do nothing
    }

    @Override
    public void updateDataBus(Message message) {

    }


    // function to convert the instruction queue to a json string
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


}
