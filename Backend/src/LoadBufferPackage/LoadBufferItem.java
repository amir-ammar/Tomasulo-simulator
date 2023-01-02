package LoadBufferPackage;
public class LoadBufferItem {

    String label;

    String targetReg;

    boolean busy;

    Integer address;
    Integer latency;
    Integer issueCycle;

    public LoadBufferItem(String label) {
        this.label = label;
    }

    public LoadBufferItem() {}

    public LoadBufferItem(String label, String targetReg, boolean busy, Integer address, Integer latency, Integer issueCycle) {
        this.label = label;
        this.targetReg = targetReg;
        this.busy = busy;
        this.address = address;
        this.latency = latency;
        this.issueCycle = issueCycle;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTargetReg() {
        return targetReg;
    }

    public void setTargetReg(String targetReg) {
        this.targetReg = targetReg;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public Integer getIssueCycle() {
        return issueCycle;
    }

    public void setIssueCycle(Integer issueCycle) {
        this.issueCycle = issueCycle;
    }

    public void decrementLatency() {
        latency--;
    }

    public void incrementIssueCycle() {
        issueCycle++;
    }

    public void clear() {
        busy = false;
        address = null;
        latency = null;
        issueCycle = null;
    }

    public String toString() {
        return "LoadBufferItem [label=" + label + ", busy=" + busy + ", address=" + address + ", latency=" + latency + ", issueCycle=" + issueCycle + "]";
    }

    public String toJson() {
        return "{\"label\":\"" + label + "\",\"busy\":\"" + busy + "\",\"address\":\"" + address + "\",\"latency\":\"" + latency + "\",\"issueCycle\":\"" + issueCycle + "\"}";
    }

}
