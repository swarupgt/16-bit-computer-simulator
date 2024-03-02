package src.main.java;
public class InstructionRegister extends Register {
    
    public int GetOpCode() {
        return this.value / (int) Math.pow(2, 10);
    }

    public int GetLSRegister() {
        return this.value % (int) Math.pow(2, 10) / (int) Math.pow(2, 8);
    }

    public int GetLSIndexRegister() {
        return this.value % (int) Math.pow(2, 8) / (int) Math.pow(2, 6);
    }

    public int GetLSIndirectBit() {
        return this.value % (int) Math.pow(2, 6) / (int) Math.pow(2, 5);
    }

    public int GetAddress() {
        return this.value % (int) Math.pow(2, 5);
    }
}
