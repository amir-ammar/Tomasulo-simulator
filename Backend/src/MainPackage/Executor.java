package MainPackage;
import DataBusPackage.DataBus;
import InstructionQueuePackage.InstructionQueue;
import InstructionQueuePackage.Instruction;
import RegisterFilePackage.RegisterFile;
import ReservationStationPackage.ReservationStation;


import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Executor implements ExecutorSubject {


    public static int cycle = 0;
    public static ArrayList<ExecutorObserver> observers;

    InstructionQueue instructions;

    ReservationStation reservationStation;
    RegisterFile registerFile;
    DataBus dataBus;
    public Executor() {

        observers = new ArrayList<>();
        instructions = new InstructionQueue();
        reservationStation = new ReservationStation();
        registerFile = new RegisterFile();
        dataBus = new DataBus();
//        instructions.enqueue(new Instruction("MUL R3 R1 R2 6"));
//        instructions.enqueue(new Instruction("ADD R5 R3 R4 4"));
//        instructions.enqueue(new Instruction("ADD R7 R2 R6 4"));
//        instructions.enqueue(new Instruction("ADD R10 R8 R9 4"));
//        instructions.enqueue(new Instruction("MUL R11 R7 R10 6"));
//        instructions.enqueue(new Instruction("ADD R5 R5 R11 4"));

        observers.add(reservationStation);
        reservationStation.registerObserver(instructions);
        instructions.registerObserver(reservationStation);

        reservationStation.registerObserver(dataBus);
        dataBus.registerObserver(reservationStation);

        registerFile.register(reservationStation);
        reservationStation.registerObserver(registerFile);

        dataBus.registerObserver(registerFile);
    }

    @Override
    public void registerObserver(ExecutorObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ExecutorObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (ExecutorObserver o : observers) {
            o.updateClock();
        }
    }

    public void run() {
        notifyObservers();
        cycle++;
    }

    public void enqueueInstructions(String instructions) {
        String[] instructionArray = instructions.split(",");
        for (String instruction : instructionArray) {
            this.instructions.enqueue(new Instruction(instruction.substring(1, instruction.length() - 1)));
        }
    }


    public String getJsonData() {
        return "{\n" +
                "  \"cycle\": " + cycle + ",\n" +
                "  \"reservationStation\": " + reservationStation.reservationStationToJson() + ",\n" +
                "  \"registerFile\": " + registerFile.toJson() + "\n" +
                "}";
    }


    public static void main(String[] args) throws InterruptedException {
//        Executor executor = new Executor();
//        InstructionQueue instructions = new InstructionQueue();
//        ReservationStation reservationStation = new ReservationStation();
//        RegisterFile registerFile = new RegisterFile();
//        DataBus dataBus = new DataBus();
//        instructions.enqueue(new Instruction("MUL R3 R1 R2 6"));
//        instructions.enqueue(new Instruction("ADD R5 R3 R4 4"));
//        instructions.enqueue(new Instruction("ADD R7 R2 R6 4"));
//        instructions.enqueue(new Instruction("ADD R10 R8 R9 4"));
//        instructions.enqueue(new Instruction("MUL R11 R7 R10 6"));
//        instructions.enqueue(new Instruction("ADD R5 R5 R11 4"));
//
//        observers.add(reservationStation);
//        reservationStation.registerObserver(instructions);
//        instructions.registerObserver(reservationStation);
//
//        reservationStation.registerObserver(dataBus);
//        dataBus.registerObserver(reservationStation);
//
//        registerFile.register(reservationStation);
//        reservationStation.registerObserver(registerFile);
//
//        dataBus.registerObserver(registerFile);
//
//
//        for (cycle = 0; cycle < 23; cycle++) {
//            System.out.println("Cycle: " + cycle);
//            executor.notifyObservers();
//            System.out.println(reservationStation);
//            System.out.println(registerFile);
//            System.out.println(dataBus);
//            System.out.println("----------------------------");
//        }
    }


}