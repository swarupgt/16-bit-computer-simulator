package src.main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Memory {
    // Array representing the memory.
    private int[] Locations = new int[2048];
    public Cache CB = new Cache();

    // private ArrayList<String> DebugText;
    // private int TrackerDebugText;

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
        }

        //lookup in cache
        int[] res = CB.Lookup(address);
        if (res[0] == 0) {
            // cache hit
            // add debug output line later if possible saying cache hit
            return res[1];
        }

        // cache miss, update block with tag in cache
        int val = Locations[address];
        // fetch block
        int tag = address / (int) Math.pow(2, 3);

        System.out.println("tag range: " + tag + "-" + (tag+7));

        int[] block = new int[8];
        for (int i = 0; i < 8; i++) {
            block[i] = Locations[(8*tag)+i];
        }

        System.out.println("Tag: " + tag + ", " + "block: " + Arrays.toString(block));

        CB.UpdateCache(address, block);
        return val;
    }

    // Write value into the given location if both are valid. 
    public int WriteToAddress(int address, int data) {
        if (address > Locations.length -1 || address < 0) {
            return -1;
        } else if (data > Math.pow(2, 16)){
            return -2;
        } 

        Locations[address] = data;
        CB.UpdateSingleWord(address, data);
        return 0;
    }

    // A silly function that does nothing, just because my friend asked to be a part of it
    public String AdvikaGodLevelInsaneoStyle() {
        return "Beep";
    }
}
