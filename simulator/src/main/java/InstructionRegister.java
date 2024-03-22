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

    public int GetDevID() {
        return this.value % (int) Math.pow(2, 5);
    }

    public int GetImmed() {
        int num = this.value % (int) Math.pow(2, 4);
        int num2 = this.value % (int) Math.pow(2, 5);
        if (num2 > num) {
            return -num;
        }
        return num;
    }

    public int GetRegisterX() {
        return this.value % (int) Math.pow(2, 10) / (int) Math.pow(2, 8);
    }

    public int GetRegisterY() {
        return this.value % (int) Math.pow(2, 8) / (int) Math.pow(2, 6);
    }

    public int GetShiftCount() {
        return this.value % (int) Math.pow(2, 4);
    }

    public int GetShiftLR() {
        return Util.getIthBit(this.value, 6);
    }

    public int GetShiftAL() {
        return Util.getIthBit(this.value, 7);
    }

}
