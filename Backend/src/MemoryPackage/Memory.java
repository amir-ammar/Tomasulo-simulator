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
}
