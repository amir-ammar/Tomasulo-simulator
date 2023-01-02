package InstructionQueuePackage;

public class Instruction {

    String op;

    String reg;
    String dest;

    Integer address;
    String op1;

    String op2;

    int latency;

    int issueCycle;

    public Instruction(String instruction) {
        decode(instruction);
    }


    public void decode(String instruction) {
        String[] tokens = instruction.split(" ");
        this.op = tokens[0];

        if (op.equals("LW") || op.equals("SW")) {
            this.reg = tokens[1];
            this.address = Integer.parseInt(tokens[2]);
            this.latency = Integer.parseInt(tokens[3]);
        } else {
            this.dest = tokens[1];
            this.op1 = tokens[2];
            this.op2 = tokens[3];
            this.latency = Integer.parseInt(tokens[4]);
        }
    }

    public String getOp() {
        return op;
    }

    public String getDest() {
        return dest;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public int getLatency() {
        return latency;
    }

    public int getIssueCycle() {
        return issueCycle;
    }

    public void setIssueCycle(int issueCycle) {
        this.issueCycle = issueCycle;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getReg() {
        return reg;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public Integer getAddress() {
        return address;
    }

    @Override
    public String toString() {
        if (op.equals("LW") || op.equals("SW")) {
            return op + " " + reg + " " + address + " " + latency + " " + issueCycle;
        } else {
            return op + " " + dest + " " + op1 + " " + op2 + " " + latency + " " + issueCycle;
        }
    }
    // function to convert the instruction to a json string
    public String toJson() {
        if (op.equals("LW") || op.equals("SW")) {
            return "{\"op\":\"" + op + "\",\"reg\":\"" + reg + "\",\"address\":" + address + ",\"latency\":" + latency + "}";
        } else {
            return "{\"op\":\"" + op + "\",\"dest\":\"" + dest + "\",\"op1\":\"" + op1 + "\",\"op2\":\"" + op2 + "\",\"latency\":" + latency + "}";
        }
    }

}
