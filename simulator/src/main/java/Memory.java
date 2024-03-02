package src.main.java;
public class Memory {
    // Array representing the memory.
    private int[] Locations = new int[2048];

    public Memory(){}

    // Creates memory with new size if size between 2048 and 4096.
    public Memory(int size) {
        if (size > 2048 && size <=4096) {
            Locations = new int[size];
        }
    }

    // Read value stored in the specified location.
    public int ReadFromAddress(int address) {
        if (address > Locations.length -1 || address < 0) {
            return -1;
        } else {
            return Locations[address];
        }
    }

    // Write value into the given location if both are valid. 
    public int WriteToAddress(int address, int data) {
        if (address > Locations.length -1 || address < 0) {
            return -1;
        } else if (data < 0 || data > Math.pow(2, 16)){
            return -2;
        } else {
            Locations[address] = data;
            return 0;
        }
    }

    // A silly function that does nothing, just because my friend asked to be a part of it
    public String AdvikaGodLevelInsaneoStyle() {
        return "Beep";
    }
}
