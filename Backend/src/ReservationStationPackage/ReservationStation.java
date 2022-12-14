package ReservationStationPackage;
import DataBusPackage.DataBus;
import DataBusPackage.DataBusObserver;
import InstructionQueuePackage.InstructionQueue;
import InstructionQueuePackage.InstructionQueueObserver;
import MainPackage.ExecutorObserver;
import MessagesPackage.Message;
import RegisterFilePackage.RegisterFile;
import RegisterFilePackage.RegisterFileObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import ALUPackage.ALU;

public class ReservationStation implements InstructionQueueObserver, ExecutorObserver, ReservationStationSubject, RegisterFileObserver, DataBusObserver
{

    ReservationStationItem[] AddSubStation;
    ReservationStationItem[] MulDivStation;

    int addSubPointer;
    int mulDivPointer;

    int lastUpdated;   // 0 -> addSub, 1 -> mulDiv

    ArrayList<ReservationStationObserver> observers;

    int addSubStationEmpty;
    int mulDivStationEmpty;

    boolean newInstruction;

    public ReservationStation() {
        addSubStationEmpty = 3;
        mulDivStationEmpty = 2;
        addSubPointer = 0;
        mulDivPointer = 0;
        newInstruction = false;
        AddSubStation = new ReservationStationItem[3];
        MulDivStation = new ReservationStationItem[2];
        observers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AddSubStation[i] = new ReservationStationItem("A" + i);
        }
        for (int i = 0; i < 2; i++) {
            MulDivStation[i] = new ReservationStationItem("M" + i);
        }
    }


    static class Tuple implements Comparable<Tuple>{
        int index;
        int priority;
        int issueCycle;

        public Tuple(int index, int priority, int issueCycle){
            this.index = index;
            this.priority = priority;
            this.issueCycle = issueCycle;
        }

        @Override
        public int compareTo(Tuple o) {
            if (this.priority == o.priority) {
                return o.issueCycle - this.issueCycle;
            }
            return this.priority - o.priority;
        }

        public String toString() {
            return "index: " + index + " priority: " + priority + " issueCycle: " + issueCycle;
        }

    }


    void updateAddSubPointer() {
        addSubPointer = (addSubPointer + 1) % 3;
    }

    void updateMulDivPointer() {
        mulDivPointer = (mulDivPointer + 1) % 2;
    }

    int getAddSubPointer() {
        return addSubPointer;
    }

    int getMulDivPointer() {
        return mulDivPointer;
    }


    public int getAddSubStationEmpty() {
        return addSubStationEmpty;
    }

    public int getMulDivStationEmpty() {
        return mulDivStationEmpty;
    }

    public int performALU(int i, char type) {
        if (type == 'A') {
            if (AddSubStation[i].getOp().equals(("ADD"))) {
                return ALU.add(AddSubStation[i].getVj(), AddSubStation[i].getVk());
            } else if (AddSubStation[i].getOp().equals(("SUB"))) {
                return ALU.sub(AddSubStation[i].getVj(), AddSubStation[i].getVk());
            }
        } else {
            if (MulDivStation[i].getOp().equals(("MUL"))) {
                return ALU.mul(MulDivStation[i].getVj(), MulDivStation[i].getVk());
            } else if (MulDivStation[i].getOp().equals(("DIV"))) {
                return ALU.div(MulDivStation[i].getVj(), MulDivStation[i].getVk());
            }
        }
        return 0;
    }

    /* Data Bus start */
    void readFromDataBus() {
        notifyDataBus("read");
    }

    /* Send data start */
    int prioritizeWriteToBus(ArrayList<Tuple> writeBackInstruction) {
        for (Tuple t: writeBackInstruction) {
            String label = t.index <= 2 ? AddSubStation[t.index].getLabel() : MulDivStation[t.index - 3].getLabel();
            for (int i = 0; i < 5; i++) {
                if (i <= 2) {
                    if (
                                AddSubStation[i].isBusy() &&
                            (
                                AddSubStation[i].getQj() != null &&
                                AddSubStation[i].getQj().equals(label) &&
                                AddSubStation[i].getQk() == null
                            )
                            ||
                            (
                                AddSubStation[i].getQk() != null &&
                                AddSubStation[i].getQk().equals(label) &&
                                AddSubStation[i].getQj() == null
                            )
                    )
                    {
                        t.priority++;
                    }
                } else {
                    if (
                            MulDivStation[i - 3].isBusy() &&
                        (
                            MulDivStation[i - 3].getQj() != null &&
                            MulDivStation[i - 3].getQj().equals(label) &&
                            MulDivStation[i - 3].getQk() == null
                        )
                        ||
                        (
                            MulDivStation[i - 3].getQk() != null &&
                            MulDivStation[i - 3].getQk().equals(label) &&
                            MulDivStation[i - 3].getQj() == null

                        )
                    )
                    {
                        t.priority++;
                    }
                }
            }
        }
        System.out.println("Write Back Instruction: " + writeBackInstruction);
        writeBackInstruction.sort(Collections.reverseOrder());
        return writeBackInstruction.get(0).index;
    }

    void writeToDataBus() {

        String writeBackInstruction = "";
        ArrayList<Tuple> readyToWrite = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (AddSubStation[i].isBusy() && AddSubStation[i].getLatency() == 0) {
                readyToWrite.add(new Tuple(i, 0, AddSubStation[i].getIssueCycle()));
            }
        }
        for (int i = 0; i < 2; i++) {
            if (MulDivStation[i].isBusy() && MulDivStation[i].getLatency() == 0) {
                readyToWrite.add(new Tuple(i + 3, 0, MulDivStation[i].getIssueCycle()));
            }
        }

        if (readyToWrite.size() == 0) return;
        int index = prioritizeWriteToBus(readyToWrite);
        if (index > 2) {
            writeBackInstruction = "write " + MulDivStation[index - 3].getLabel() + " " + MulDivStation[index - 3].getTargetReg() + " " + performALU(index - 3, 'M');
            MulDivStation[index - 3].setLatency(-1);
            notifyDataBus(writeBackInstruction);
        } else {
            writeBackInstruction = "write " + AddSubStation[index].getLabel() + " " + AddSubStation[index].getTargetReg() + " " + performALU(index, 'A');
            AddSubStation[index].setLatency(-1);
            notifyDataBus(writeBackInstruction);
        }
    }

    @Override
    public void notifyDataBus(String data) {
        HashMap<String, String> message = new HashMap<>();
        message.put("dataBus", data);
        for (ReservationStationObserver o : observers) {
            if (o instanceof DataBus) {
                System.out.println("DataBus: " + data);
                o.updateDataBus(new Message(message));
            }
        }
    }

    /* Send data end */

    /* receive data start */
    @Override
    public void DataBusUpdate(Message message)  {
        String tag = message.getDataBusUpdate().split(" ")[0];
        int value = Integer.parseInt(message.getDataBusUpdate().split(" ")[2]);

        for (int i = 0; i < 3; i++) {
            if (!AddSubStation[i].isBusy()) continue;
            if (AddSubStation[i].getQj() != null && AddSubStation[i].getQj().equals(tag)) {
                AddSubStation[i].setVj(value);
                AddSubStation[i].setQj(null);
            }
            if (AddSubStation[i].getQk() != null && AddSubStation[i].getQk().equals(tag)) {
                AddSubStation[i].setVk(value);
                AddSubStation[i].setQk(null);
            }
        }

        for (int i = 0; i < 2; i++) {
            if (!MulDivStation[i].isBusy()) continue;
            if (MulDivStation[i].getQj() != null && MulDivStation[i].getQj().equals(tag)) {
                MulDivStation[i].setVj(value);
                MulDivStation[i].setQj(null);
            }
            if (MulDivStation[i].getQk() != null && MulDivStation[i].getQk().equals(tag)) {
                MulDivStation[i].setVk(value);
                MulDivStation[i].setQk(null);
            }
        }
    }

    /* receive data end */

    /* Data Bus end */



    /* Instruction queue start */

    /* receive data start */
    ReservationStationItem convertInstructionToReservationStationItem(String newInstruction) {
        ReservationStationItem item = new ReservationStationItem();
        String [] newInstructionFields = newInstruction.split(" ");
        String label = switch (newInstructionFields[0]) {
            case "ADD", "SUB" -> "A" + getAddSubPointer();
            case "MUL", "DIV" -> "M" + getMulDivPointer();
            default -> "NOP";
        };
        if (label.equals("NOP")) return item;
        item.setTargetReg(newInstructionFields[1]);
        item.setLabel(label);
        item.setBusy(true);
        item.setOp(newInstructionFields[0]);
        item.setQj(newInstructionFields[2]);
        item.setQk(newInstructionFields[3]);
        item.setLatency(Integer.parseInt(newInstructionFields[4]));
        item.setIssueCycle(Integer.parseInt(newInstructionFields[5]));
        return item;
    }

    @Override
    public void instructionQueueUpdate(Message message) {
        ReservationStationItem item = convertInstructionToReservationStationItem(message.getNewInstruction());
        if (item.label == null) return;
        if (item.getLabel().charAt(0) == 'A') {
            AddSubStation[addSubPointer] = item;
            updateAddSubPointer();
            addSubStationEmpty--;
            lastUpdated = 0;
        } else {
            MulDivStation[mulDivPointer] = item;
            updateMulDivPointer();
            mulDivStationEmpty--;
            lastUpdated = 1;
        }
        newInstruction = true;
    }
    /* receive data end */

    /* Send data start */
    @Override
    public void notifyInstructionQueue() {
        HashMap<String, String> message = new HashMap<>();
        message.put("addSubStationSize", addSubStationEmpty + "");
        message.put("mulDivStationSize", mulDivStationEmpty + "");
        for (ReservationStationObserver o : observers) {
            if (o instanceof InstructionQueue) {
                o.updateInstructionQueue(new Message(message));
            }
        }
    }

    /* Send data end */
    /* Instruction queue end */



    /* Register file start */

    /* Send data start */

    public Message generateRegisterFileMessage() {
        String requiredRegisters = "";
        String updatedRegister = "";
        int idx;

        if (lastUpdated == 0) {
            idx = addSubPointer;
            if (idx == 0) idx = 2;
            else idx = idx - 1;
            requiredRegisters = AddSubStation[idx].getQj() + " " + AddSubStation[idx].getQk();
            updatedRegister = AddSubStation[idx].getTargetReg() + ":" + AddSubStation[idx].getLabel();
        } else if (lastUpdated == 1) {
            idx = mulDivPointer;
            if (idx == 0) idx = 1;
            else idx = idx - 1;
            System.out.println(MulDivStation[idx]);
            requiredRegisters = MulDivStation[idx].getQj() + " " + MulDivStation[idx].getQk();
            updatedRegister = MulDivStation[idx].getTargetReg() + ":" + MulDivStation[idx].getLabel();
        } else {
            System.out.println("There is no last updated station");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("requiredRegisters", requiredRegisters);
        map.put("updatedRegister", updatedRegister);
        return new Message(map);
    }

    @Override
    public void notifyRegisterFile() {
        for (ReservationStationObserver o : observers) {
            if (o instanceof RegisterFile && newInstruction) {
                o.updateRegisterFile(generateRegisterFileMessage());
            }
        }
    }

    /* Send data end */

    /* receive data start */
    @Override
    public void regFileUpdate(Message message) {
        if (lastUpdated == 0) {
            int idx = addSubPointer;
            if (idx == 0) idx = 2;
            else idx = idx - 1;
            String[] parts = message.getRequiredRegistersValue().split(" ");
            String part1 = parts[0];
            String part2 = parts[1];

            // check if part1 is a number ot String
            if (part1.matches("-?\\d+(\\.\\d+)?")) {
                AddSubStation[idx].setVj(Integer.parseInt(part1));
                AddSubStation[idx].setQj(null);
            } else {
                AddSubStation[idx].setVk(null);
                AddSubStation[idx].setQj(part1);
            }

            // check if part2 is a number ot String
            if (part2.matches("-?\\d+(\\.\\d+)?")) {
                AddSubStation[idx].setVk(Integer.parseInt(part2));
                AddSubStation[idx].setQk(null);
            } else {
                AddSubStation[idx].setQk(part2);
                AddSubStation[idx].setVk(null);
            }
        } else if(lastUpdated == 1){
            int idx = mulDivPointer;
            if (idx == 0) idx = 1;
            else idx = idx - 1;
            String[] parts = message.getRequiredRegistersValue().split(" ");
            String part1 = parts[0];
            String part2 = parts[1];

            // check if part1 is a number ot String
            if (part1.matches("-?\\d+(\\.\\d+)?")) {
                MulDivStation[idx].setVj(Integer.parseInt(part1));
                MulDivStation[idx].setQj(null);
            } else {
                MulDivStation[idx].setVk(null);
                MulDivStation[idx].setQj(part1);
            }

            // check if part2 is a number ot String
            if (part2.matches("-?\\d+(\\.\\d+)?")) {
                MulDivStation[idx].setVk(Integer.parseInt(part2));
                MulDivStation[idx].setQk(null);
            } else {
                MulDivStation[idx].setQk(part2);
                MulDivStation[idx].setVk(null);
            }
        } else {
            System.out.println("There is no last updated station");
        }
    }

    /* receive data end */
    /* Register file end */

    /* Operations start */
    boolean isExecuting(int i, char type) {
        if (type == 'A') {
            return AddSubStation[i].isBusy() &&
                    (AddSubStation[i].getLatency() > 0 || AddSubStation[i].getLatency() == -1) &&
                    AddSubStation[i].getQj() == null &&
                    AddSubStation[i].getQk() == null;
        } else {
            return MulDivStation[i].isBusy() &&
                    (MulDivStation[i].getLatency() > 0 || MulDivStation[i].getLatency() == -1) &&
                    MulDivStation[i].getQj() == null &&
                    MulDivStation[i].getQk() == null;
        }
    }
    void updateLatency() {
        for (int i = 0; i < 3; i++) {
            if (isExecuting(i, 'A'))
                AddSubStation[i].setLatency(AddSubStation[i].getLatency() - 1);
        }
        for (int i = 0; i < 2; i++) {
            if(isExecuting(i, 'M'))
                MulDivStation[i].setLatency(MulDivStation[i].getLatency() - 1);
        }
    }

    void removeFinishedInstruction() {
        for (int i = 0; i < 5; i++) {
            if (i <= 2) {
                if (AddSubStation[i].isBusy() && AddSubStation[i].getLatency() == -2) {
                    AddSubStation[i].clear();
                    addSubStationEmpty++;
                    addSubPointer = i;
                }
            } else {
                if (MulDivStation[i - 3].isBusy() && MulDivStation[i - 3].getLatency() == -2) {
                    MulDivStation[i - 3].clear();
                    mulDivStationEmpty++;
                    mulDivPointer = i - 3;
                }
            }
        }
    }

    @Override
    public void updateClock() {
        readFromDataBus();
        writeToDataBus();
        removeFinishedInstruction();
        updateLatency();
        notifyInstructionQueue();
        notifyRegisterFile();
        newInstruction = false;
    }
    /* Operations end */

    /* Reservation station start */
    @Override
    public void registerObserver(ReservationStationObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ReservationStationObserver o) {
        observers.remove(o);
    }

    public String reservationStationToJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"AddSubStation\": [");
        for (int i = 0; i < 3; i++) {
            json.append(AddSubStation[i].toJson());
            if (i != 2) json.append(",");
        }
        json.append("],");
        json.append("\"MulDivStation\": [");
        for (int i = 0; i < 2; i++) {
            json.append(MulDivStation[i].toJson());
            if (i != 1) json.append(",");
        }
        json.append("]}");
        return json.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reservation Station: \n");
        sb.append("AddSubStation: \n");
        for (int i = 0; i < 3; i++) {
            sb.append(AddSubStation[i].toString());
            sb.append("\n");
        }
        sb.append("MulDivStation: \n");
        for (int i = 0; i < 2; i++) {
            sb.append(MulDivStation[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /* Reservation station end */

    public static void main(String[] args) {
        ReservationStation rs = new ReservationStation();
        ReservationStationItem item = new ReservationStationItem("R4", "A0", true,"ADD", null, null,"R1", "R2", 5, 2, 2);
        rs.AddSubStation[0] = item;
        rs.lastUpdated = 0;
        rs.updateAddSubPointer();
        RegisterFile rf = new RegisterFile();

        rf.register(rs);
        rs.registerObserver(rf);

        rs.notifyRegisterFile();

    }


}
