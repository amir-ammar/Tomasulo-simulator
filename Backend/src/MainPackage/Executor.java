package MainPackage;
import DataBusPackage.DataBus;
import InstructionQueuePackage.InstructionQueue;
import InstructionQueuePackage.Instruction;
import LoadBufferPackage.LoadBuffer;
import MemoryPackage.Memory;
import RegisterFilePackage.RegisterFile;
import ReservationStationPackage.ReservationStation;
import StoreBuffer.StoreBuffer;
import java.util.ArrayList;


public class Executor implements ExecutorSubject {


    public static int cycle = 0;
    public static ArrayList<ExecutorObserver> observers;
    public static Memory memory;

    InstructionQueue instructions;

    ReservationStation reservationStation;

    LoadBuffer loadBuffer;
    RegisterFile registerFile;
    DataBus dataBus;

    StoreBuffer storeBuffer;

    public Executor() {

        observers = new ArrayList<>();
        instructions = new InstructionQueue();
        reservationStation = new ReservationStation();
        loadBuffer = new LoadBuffer();
        registerFile = new RegisterFile();
        dataBus = new DataBus();
        storeBuffer = new StoreBuffer();
        memory = new Memory(1024);
        instructions.enqueue(new Instruction("LW R6 32 1"));
        instructions.enqueue(new Instruction("LW R2 44 1"));
        instructions.enqueue(new Instruction("MUL R0 R2 R4 10"));
        instructions.enqueue(new Instruction("SUB R8 R6 R2 1"));
        instructions.enqueue(new Instruction("SUB R10 R0 R6 1"));
        instructions.enqueue(new Instruction("ADD R6 R8 R2 1"));

        observers.add(instructions);
        observers.add(loadBuffer);
        observers.add(storeBuffer);
        observers.add(reservationStation);

        reservationStation.registerObserver(instructions);
        reservationStation.registerObserver(dataBus);
        reservationStation.registerObserver(registerFile);
        reservationStation.registerObserver(loadBuffer);
        reservationStation.registerObserver(storeBuffer);

        registerFile.register(reservationStation);
        registerFile.register(storeBuffer);


        dataBus.registerObserver(registerFile);
        dataBus.registerObserver(reservationStation);

        instructions.registerObserver(reservationStation);
        instructions.registerObserver(loadBuffer);
        instructions.registerObserver(storeBuffer);

        loadBuffer.registerObserver(instructions);
        loadBuffer.registerObserver(reservationStation);

        storeBuffer.registerObserver(instructions);
        storeBuffer.registerObserver(reservationStation);
        storeBuffer.registerObserver(registerFile);


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
        dataBus.notifyObservers();
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
                "  \"loadBuffer\": " + loadBuffer.toJson() + "\n" +
                "  \"storeBuffer\": " + storeBuffer.toJson() + "\n" +
                "  \"memory\": " + memory.toJson() + "\n" +
                "}";
    }


    public static void main(String[] args) throws InterruptedException
    {
        Executor executor = new Executor();
        while (true) {
            executor.run();
//            Thread.sleep(1000);
        }
    }



}