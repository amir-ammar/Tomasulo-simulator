package MemoryPackage;

public class Memory {
    private final int[] memory;
    private final int size;

    public Memory(int size) {
        this.size = size;
        memory = new int[size];
    }

    public int read(int address) {
        return memory[address];
    }

    public void write(int address, int data) {
        memory[address] = data;
    }

    public int getSize() {
        return size;
    }

    public void printMemory() {
        for (int i = 0; i < size; i++) {
            System.out.println("memory[" + i + "] = " + memory[i]);
        }
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            json.append("{\"address\":").append(i).append(",\"value\":").append(memory[i]).append("},");
        }
        json = new StringBuilder(json.substring(0, json.length() - 1));
        json.append("]");
        return json.toString();
    }
}
