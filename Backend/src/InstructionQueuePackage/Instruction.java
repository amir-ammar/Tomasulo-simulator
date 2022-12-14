package InstructionQueuePackage;

public class Instruction {

    String op;
    String dest;
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
        this.dest = tokens[1];
        this.op1 = tokens[2];

        if (op.equals("LW") || op.equals("SW")) {
            this.latency = Integer.parseInt(tokens[3]);
        } else {
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

    @Override
    public String toString() {
        return op + " " + dest + " " + op1 + " " + op2 + " " + latency + " " + issueCycle;

    }
    // function to convert the instruction to a json string
    public String toJson() {
        return "{\"op\":\"" + op + "\",\"dest\":\"" + dest + "\",\"op1\":\"" + op1 + "\",\"op2\":\"" + op2 + "\",\"latency\":\"" + latency + "\"}";
    }

}
