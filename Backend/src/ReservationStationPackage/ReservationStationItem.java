package ReservationStationPackage;

public class ReservationStationItem {


    String label;
    String targetReg;
    boolean busy;
    String op;
    Integer vj;
    Integer vk;
    String qj;
    String qk;
    Integer a;

    Integer latency;

    Integer issueCycle;

    public ReservationStationItem(String label) {
        this.label = label;
        busy = false;
        op = "";
        vj = null;
        vk = null;
        qj = null;
        qk = null;
        a = null;
        latency = null;
        issueCycle = null;
    }

    public ReservationStationItem() {}

    public ReservationStationItem(String targetReg, String label, boolean busy, String op, Integer vj, Integer vk, String qj, String qk, Integer a, int latency, int issueCycle) {
        this.targetReg = targetReg;
        this.label = label;
        this.busy = busy;
        this.op = op;
        this.vj = vj;
        this.vk = vk;
        this.qj = qj;
        this.qk = qk;
        this.a = a;
        this.latency = latency;
        this.issueCycle = issueCycle;
    }


    public String getTargetReg() {
        return targetReg;
    }

    public void setTargetReg(String targetReg) {
        this.targetReg = targetReg;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Integer getVj() {
        return vj;
    }

    public void setVj(Integer vj) {
        this.vj = vj;
    }

    public Integer getVk() {
        return vk;
    }

    public void setVk(Integer vk) {
        this.vk = vk;
    }

    public String getQj() {
        return qj;
    }

    public void setQj(String qj) {
        this.qj = qj;
    }

    public String getQk() {
        return qk;
    }

    public void setQk(String qk) {
        this.qk = qk;
    }

    public Integer getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }


    Integer getIssueCycle() {
        return issueCycle;
    }

    void setIssueCycle(int issueCycle) {
        this.issueCycle = issueCycle;
    }

    public void clear() {
        busy = false;
        op = null;
        vj = null;
        vk = null;
        qj = null;
        qk = null;
        a = null;
        latency = null;
        issueCycle = null;
    }

    public String toString() {
        return label + " " + busy + " " + op + " " + vj + " " + vk + " " + qj + " " + qk + " " + a + " " + latency + " " + issueCycle;
    }

    // function to convert the reservation station item to a json string to be sent to the client
    public String toJson() {
        return "{\"label\":\"" + label + "\",\"busy\":\"" + busy + "\",\"op\":\"" + op + "\",\"vj\":\"" + vj + "\",\"vk\":\"" + vk + "\",\"qj\":\"" + qj + "\",\"qk\":\"" + qk + "\",\"a\":\"" + a + "\",\"issueCycle\":\"" + issueCycle + "\"}";
    }


}
