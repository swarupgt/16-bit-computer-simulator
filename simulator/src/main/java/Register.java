package src.main.java;
public class Register {
    protected int value = 0;
    protected int bitsize = 16;

    public Register() {}

    // Set bitsize of register.
    Register(int bs) {
        bitsize = bs;
    }

    // Get register value.
    public int Get() {
        return value;
    }

    public int GetBitsize() {
        return bitsize;
    }

    // Set register value if valid.
    public int Set(int v) {
        if (v > Math.pow(2, bitsize)) {
            int mask = (1 << bitsize) - 1;
            value = v & mask;
            return -1;
        }
        value = v;
        return 0;
    }
}
