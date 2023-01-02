package StoreBuffer;

public class StoreBufferItem {

        String label;
        boolean busy;

        Integer address;
        Integer V;

        String Q;

        Integer latency;
        Integer issueCycle;


        public StoreBufferItem(String label) {
            this.label = label;
        }

        public StoreBufferItem() {}

        public StoreBufferItem(String label, boolean busy, Integer address, Integer v, String q, Integer latency, Integer issueCycle) {
            this.label = label;
            this.busy = busy;
            this.address = address;
            this.V = v;
            this.Q = q;
            this.latency = latency;
            this.issueCycle = issueCycle;
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

        public Integer getAddress() {
            return address;
        }

        public void setAddress(Integer address) {
            this.address = address;
        }

        public Integer getV() {
            return V;
        }

        public void setV(Integer v) {
            V = v;
        }

        public String getQ() {
            return Q;
        }

        public void setQ(String q) {
            Q = q;
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

        public void clear() {
            busy = false;
            address = null;
            V = null;
            Q = null;
            latency = null;
            issueCycle = null;
        }

         public String toString() {
             return "StoreBufferItem [label=" + label + ", busy=" + busy + ", address=" + address + ", V=" + V + ", Q=" + Q
                     + ", latency=" + latency + ", issueCycle=" + issueCycle + "]";
         }

        public String toJsonString() {
            return "{\"label\":\"" + label + "\",\"busy\":\"" + busy + "\",\"address\":\"" + address + "\",\"V\":\"" + V + "\",\"Q\":\"" + Q
                    + "\",\"latency\":\"" + latency + "\",\"issueCycle\":\"" + issueCycle + "\"}";
        }

}
