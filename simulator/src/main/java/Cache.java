package src.main.java;

import java.io.*;

import org.apache.commons.compress.compressors.lz77support.LZ77Compressor.Block;

public class Cache {
    // col0 = fifo_order, col1 = tag, col2-9 = words
    private int[][] Blocks = new int[16][10];
    private int fifoLength = 0;

    Cache() {}

    public void Init() {
        Blocks = new int[16][10];
        fifoLength = 0;
    }

    public int[] Lookup(int address) {
        int[] res = new int[2];

        int wordNum = address % (int) Math.pow(2, 3);
        int tag = address / (int) Math.pow(2, 3);

        System.out.println("lookup Address: " + address + "Tag: " + tag + "word number: " + wordNum);

        boolean found = false;

        for (int i = 0; i < fifoLength; i++) {
            if (tag == Blocks[i][1]) {
                //cache hit
                res[0] = 0;
                res[1] = Blocks[i][wordNum+2];
                System.out.println("Cache hit for address" + address);
                found = true;
            }
        }

        if (!found) {
            // cache miss 
            System.out.println("Cache miss for address" + address);
            res[0] = -1;
        }

        return res;
    }

    public void UpdateCache(int address, int[] block) {
        int tag = address / (int) Math.pow(2, 3);

        System.out.println("update cache Address: " + address + "Tag: " + tag);

        if (fifoLength < 16) {
            //set fifolength'th block to the input block
            Blocks[fifoLength][1] = tag;

            for (int i = 0; i < block.length; i++) {
                Blocks[fifoLength][i+2] = block[i];
            }
            // set fifo order too
            Blocks[fifoLength][0] = fifoLength;
            fifoLength++;
        } else {

            int idxToReplace = -1;
            for (int i = 1; i < 16; i++) {
                // update fifo order
                (Blocks[i][0])--;

                // replace the block with the lowest fifo order
                if (idxToReplace > Blocks[i][0]) {
                    idxToReplace = Blocks[i][0];
                }
            }

            for (int i = 0; i < block.length; i++) {
                Blocks[idxToReplace+1][i+2] = block[i];
            }
            Blocks[idxToReplace+1][0] = 15;
            Blocks[idxToReplace+1][1] = tag;
        }
    }

    // function for memory to update when write occurs 
    public void UpdateSingleWord(int address, int val) {
        int tag = address / (int) Math.pow(2, 3);
        int wordNum = address % (int) Math.pow(2, 3);

        for (int i = 0; i < fifoLength; i++) {
            if (tag == Blocks[i][1]) {
                Blocks[i][wordNum+2] = val;
            }
        }
    }

    public int[][] GetActiveCacheBlock() {
        int [][] ac = new int[fifoLength][10];
        for (int i = 0; i < fifoLength; i++) {
            for (int j = 0; j < 10; j++) {
                ac[i][j] = Blocks[i][j];
            }
        }
        
        return ac;
    }

    public int GetFifoLength() {
        return fifoLength;
    }
}
