package src.main.java;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CPU {

    private static HashMap<Integer, String> OpCodeMap = new HashMap<Integer, String>();

    // All registers
    private Register PC = new Register(12);
    private Register CC = new Register(4);
    private InstructionRegister IR = new InstructionRegister();
    private Register MAR = new Register(12);
    private Register MBR = new Register();
    private Register[] GPR = new Register[4];
    private Register[] IXR = new Register[3];

    // I/O devices
    private Devices Dev = new Devices();

    // Halt indicator
    private int halt = 0;

    // Memory
    private Memory Mem;

    private ArrayList<String> ConsoleText = new ArrayList<String>();

    private boolean normalPC = true;

    public CPU() {

        // Set the OpCode Map
        ArrayList<String[]> OpCodes = Util.ReadInput("/opcodes.txt");
        for (String[] str : OpCodes) {
            int opnum = Integer.parseInt(str[0]);
            OpCodeMap.put(opnum, str[1]);
        }
        System.out.println("Opcodes have been set.");

        for (int i = 0; i < 4; i++) {
            GPR[i] = new Register();
        }

        for (int i = 0; i < 3; i++) {
            IXR[i] = new Register();
        }
    }

    // Set initial components of the computer
    public void Initialise() {
        GPR[0].Set(0);
        GPR[1].Set(0);
        GPR[2].Set(0);
        GPR[3].Set(0);
        IXR[0].Set(0);
        IXR[1].Set(0);
        IXR[2].Set(0);
        PC.Set(0);
        CC.Set(0);
        IR.Set(0);
        MAR.Set(0);
        MBR.Set(0);
        Mem = new Memory();

        ConsoleText = new ArrayList<String>();

        // Set first non-protected location and halt to view fault
        // Mem.WriteToAddress(1, 6);
        // Mem.WriteToAddress(6, 0b0000000000000000);
        halt = 0;
    }

    // Read input file and load it into memory.
    public void LoadFromROM(String Filepath) {
        File f = new File(Filepath);
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
            BufferedReader br = new BufferedReader(reader);
            String l = "";
            boolean initPC = false;
            while (l != null) {
                l = br.readLine();
                if (l == null) break;
                String[] insts = l.split(" ");
                
                // can use later for fault handling
                // add 7 to reserve the first 6 for faults for part 3 of the project
                int PCMemValue = Integer.parseInt(insts[0], 8);
                int inst = Integer.parseInt(insts[1], 8);
                int faultIndex = Mem.WriteToAddress(PCMemValue, inst);
                if (faultIndex == -1) {
                    System.out.println("Incorrect address provided");
                }
                if (!initPC && Util.IsValidInstruction(inst)) {
                    System.out.println("PC being set to: " + PCMemValue);
                    PC.Set(PCMemValue);
                    initPC = true;
                }
            }
            System.out.println("loaded instructions from ROM to memory successfully :)");
            br.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Mem.CB.Init();
    }


    // Run a single instruction.
    public boolean RunOneStep() {
        if (halt == 1) {
            return false;
        }

        // Set MAR based on PC
        MAR.Set(PC.Get());
        if (normalPC) {
            IncrementPC();
        } else {
            normalPC = true;
        }

        int val = Mem.ReadFromAddress(MAR.Get());
        if (val == -1) {
            System.out.println("Incorrect address provided");
        }
        
        // Get instruction from memory pointed to by MAR and store in MBR
        MBR.Set(val);

        System.out.println("MAR, MBR: " + MAR.Get() + ", " + MBR.Get());

        // Set IR as the instruction stored in MBR
        IR.Set(MBR.Get());
        System.out.println("IR values: " + IR.GetOpCode() + " " + IR.GetLSRegister() + " " + IR.GetLSIndexRegister() + " " + IR.GetLSIndirectBit() + " " + IR.GetAddress());
        ExecuteInstruction();

        if (halt == 1) {
            return false;
        }

        return true;

    }

    // execute instruction based on opcode
    private void ExecuteInstruction() {
        System.out.println("Opcode in IR: " + IR.GetOpCode());
        switch (OpCodeMap.get(IR.GetOpCode())) {
            case "HLT":
                if (IR.GetAddress() == 0) {
                    halt = 1;
                }
                break;
            case "LDR":
                if (IR.GetAddress() == 0 && IR.GetLSRegister() == 0 && IR.GetLSIndexRegister() == 0) {
                    return;
                }
                LDR();
                break;
            case "STR":
                STR();
                break;
            case "LDA":
                LDA();
                break;
            case "LDX":
                LDX();
                break;
            case "STX":
                STX();
                break;
            case "SETCCE":
                SETCCE();
                break;
            case "JZ":
                JZ();
                break;
            case "JNE":
                JNE();
                break;
            case "JCC":
                JCC();
                break;
            case "JMA":
                JMA();
                break;
            case "JSR":
                JSR();
                break;
            case "RFS":
                RFS();
                break;
            case "SOB":
                SOB();
                break;
            case "JGE":
                JGE();
                break;
            case "AMR":
                AMR();
                break;
            case "SMR":
                SMR();
                break;
            case "AIR":
                AIR();
                break;
            case "SIR":
                SIR();
                break;
            case "MLT":
                MLT();
                break;
            case "DVD":
                DVD();
                break;
            case "TRR":
                TRR();
                break;
            case "AND":
                AND();
                break;
            case "ORR":
                ORR();
                break;
            case "NOT":
                NOT();
                break;
            case "SRC":
                SRC();
                break;
            case "RRC":
                RRC();
                break;
            case "IN":
                IN();
                break;
            case "OUT":
                OUT();
                break;
            default:
                // Unknown Opcode
                halt = 1;
                PrintToConsole("Unknown opcode detected");
        }
    }

    public void LDR() {
        System.out.println("inside LDR()");
        int EA = ComputeEffectiveAddress();
        MAR.Set(EA);

        System.out.println("EA: " + EA);

        int res = Mem.ReadFromAddress(MAR.Get());

        System.out.println("RES: " + res);

        if (res < 0) {
            PrintToConsole("Memory fault occured, code " + res);
        }
        // Set MBR as value fetched using address in MAR
        MBR.Set(res);
        // Set GPR register as value present in MBR
        GPR[IR.GetLSRegister()].Set(MBR.Get());

        PrintToConsole("LDR successful, data " + res + " read from loc " + MAR.Get() + " to GPR " + IR.GetLSRegister());
    }

    public void STR() {
        int EA = ComputeEffectiveAddress();

        // Set MAR to Ea computed
        MAR.Set(EA);
        // Set MBR as specified register's value to be written
        MBR.Set(GPR[IR.GetLSRegister()].Get());
        // Write MBR data into memory 
        Mem.WriteToAddress(MAR.Get(), MBR.Get());

        PrintToConsole("STR successful, data " + Mem.ReadFromAddress(MAR.Get()) + " written to loc " + MAR.Get());
    }

    public void LDA() {
        int EA = ComputeEffectiveAddress();
        
        // Set GPR register as EA
        GPR[IR.GetLSRegister()].Set(EA);

        PrintToConsole("LDA successful, address " + EA + " loaded to GPR " + IR.GetLSRegister());
    }

    public void LDX() {
        System.out.println("inside LDX()");
        int EA = ComputeEffectiveAddress();
        MAR.Set(EA);

        int res = Mem.ReadFromAddress(MAR.Get());
        if (res < 0) {
            PrintToConsole("Memory fault occured, code " + res);
        }
        // Set MBR as value fetched using address in MAR
        MBR.Set(res);
        // Set GPR register as value present in MBR
        IXR[IR.GetLSIndexRegister()-1].Set(MBR.Get());

        PrintToConsole("LDX successful, data " + res + " read from loc " + MAR.Get() + " to IXR " + IR.GetLSIndexRegister());
        // System.out.println("LDX successful, data " + res + "read from loc " + MAR.Get() + "to IXR " + IR.GetLSIndexRegister());
    }

    public void STX() {
        int EA = ComputeEffectiveAddress();

        // Set MAR to EA computed
        MAR.Set(EA);
        // Set MBR as specified index register's value to be written
        MBR.Set(IXR[IR.GetLSIndexRegister()-1].Get());
        // Write MBR data into memory 
        Mem.WriteToAddress(MAR.Get(), MBR.Get());

        PrintToConsole("STX successful, data " + Mem.ReadFromAddress(MAR.Get()) + " written to loc " + MAR.Get());
    }

    public void SETCCE() {
        if (GPR[IR.GetLSRegister()].Get() == 0) {
            CC.Set(Util.setOrClearIthBit(CC.Get(), 3, true));
            PrintToConsole("CC's E bit set to 1");
        } else {
            CC.Set(Util.setOrClearIthBit(CC.Get(), 3, false));
            PrintToConsole("CC's E bit set to 0");
        }
    }

    public void JZ() {
        int EA = ComputeEffectiveAddress();
        if (Util.getIthBit(CC.Get(), 3) == 1) {
            PC.Set(EA);
            normalPC = false;
            PrintToConsole("JZ successful, PC = " + EA);
        }
    }

    public void JNE() {
        int EA = ComputeEffectiveAddress();
        if (Util.getIthBit(CC.Get(), 3) == 0) {
            PC.Set(EA);
            normalPC = false;
            PrintToConsole("JNE successful, PC = " + EA);
        }
    }

    public void JCC() {
        int EA = ComputeEffectiveAddress();
        int conditionCodeIndex = IR.GetLSRegister();
        if (Util.getIthBit(CC.Get(), conditionCodeIndex) == 1) {
            PC.Set(EA);
            normalPC = false;
            PrintToConsole("JCC successful, PC = " + EA);
        }
    }

    public void JMA() {
        int EA = ComputeEffectiveAddress();
        PC.Set(EA);
        normalPC = false;
        PrintToConsole("JMA successful, PC = " + EA);
    }

    public void JSR() {
        int EA =ComputeEffectiveAddress();
        GPR[3].Set(PC.Get()+1);
        PC.Set(EA);
        PrintToConsole("JSR successful, PC = " + EA);
    }

    public void RFS() {
        GPR[0].Set(IR.GetImmed());
        PC.Set(GPR[3].Get());
        normalPC = false;

        PrintToConsole("RFS successful, PC = " + PC.Get());
    }

    public void SOB() {
        GPR[IR.GetLSRegister()].Set(GPR[IR.GetLSRegister()].Get()-1);
        int EA = ComputeEffectiveAddress();
        if (GPR[IR.GetLSRegister()].Get() > 0) {
            PC.Set(EA);
            normalPC = false;
            PrintToConsole("SOB successful, PC = " + EA);
        }
    }

    public void JGE() {
        int EA = ComputeEffectiveAddress();
        if (GPR[IR.GetLSRegister()].Get() >= 0) {
            PC.Set(EA);
            normalPC = false;
            PrintToConsole("JGE successful, PC = " + EA);
        }
    }

    public void AMR() {
        int cr = GPR[IR.GetLSRegister()].Get();
        int EA = ComputeEffectiveAddress();
        int val = Mem.ReadFromAddress(EA);
        GPR[IR.GetLSRegister()].Set(cr+val);

        PrintToConsole("AMR successful, R"+IR.GetLSRegister()+" value = " + cr+val);
    }

    public void SMR() {
        int cr = GPR[IR.GetLSRegister()].Get();
        int EA = ComputeEffectiveAddress();
        int val = Mem.ReadFromAddress(EA);
        GPR[IR.GetLSRegister()].Set(cr-val);

        PrintToConsole("SMR successful, R"+IR.GetLSRegister()+" value = " + cr+val);
    }

    public void AIR() {
        int cr = GPR[IR.GetLSRegister()].Get();
        int val = IR.GetImmed();
        GPR[IR.GetLSRegister()].Set(cr+val);

        PrintToConsole("AIR successful, R"+IR.GetLSRegister()+" value = " + cr+val);
    }

    public void SIR() {
        int cr = GPR[IR.GetLSRegister()].Get();
        int val = IR.GetImmed();
        GPR[IR.GetLSRegister()].Set(cr-val);

        PrintToConsole("SIR successful, R"+IR.GetLSRegister()+" value = " + cr+val);
    }

    public void MLT() {
        int crx = GPR[IR.GetRegisterX()].Get();
        int cry = GPR[IR.GetRegisterY()].Get();

        if ((IR.GetRegisterX() % 2 != 0) || (IR.GetRegisterY() % 2 != 0)) {
            //trap as rx and ry need to be 0 or 2
        }

        int a[] = Util.multiplyRegisters(crx, cry, GPR[0].bitsize);
        int of = GPR[IR.GetRegisterX()].Set(a[0]);
        GPR[IR.GetRegisterX()+1].Set(a[1]);

        // overflow
        if (of == -1) {
            CC.Set(Util.setOrClearIthBit(CC.Get(), 0, true));
            // PrintToConsole("MLT Overflow");
        }

        PrintToConsole("MLT Successful");
    }

    public void DVD() {
        int crx = GPR[IR.GetRegisterX()].Get();
        int cry = GPR[IR.GetRegisterY()].Get();

        if ((IR.GetRegisterX() % 2 != 0) || (IR.GetRegisterY() % 2 != 0)) {
            //trap as rx needs to be 0 or 2
        }

        // div by zero
        if (cry == 0) {
            CC.Set(Util.setOrClearIthBit(CC.Get(), 2, true));
        }

        int a[] = Util.divideRegisters(crx, cry);
        int of = GPR[IR.GetRegisterX()].Set(a[0]);
        GPR[IR.GetRegisterX()+1].Set(a[1]);

        PrintToConsole("DVD Successful");
    }

    public void TRR() {
        int crx = GPR[IR.GetRegisterX()].Get();
        int cry = GPR[IR.GetRegisterY()].Get();

        CC.Set(Util.setOrClearIthBit(CC.Get(), 3, crx == cry));

        PrintToConsole("TRR Successful, equality = "+ (crx == cry));
    }

    public void AND() {
        int crx = GPR[IR.GetRegisterX()].Get();
        int cry = GPR[IR.GetRegisterY()].Get();

        GPR[IR.GetRegisterX()].Set(crx & cry);
        PrintToConsole("AND Successful, res = "+ GPR[IR.GetRegisterX()].Get());
    }

    public void ORR() {
        int crx = GPR[IR.GetRegisterX()].Get();
        int cry = GPR[IR.GetRegisterY()].Get();

        GPR[IR.GetRegisterX()].Set(crx | cry);
        PrintToConsole("ORR Successful, res = "+ GPR[IR.GetRegisterX()].Get());
    }
    
    public void NOT() {
        int crx = GPR[IR.GetRegisterX()].Get();

        GPR[IR.GetRegisterX()].Set(~crx);
        PrintToConsole("ORR Successful, res = "+ GPR[IR.GetRegisterX()].Get());
    }

    public void SRC() {
        int cr = GPR[IR.GetRegisterX()].Get();
        int AL = IR.GetShiftAL(), LR = IR.GetShiftLR();
        int count = IR.GetShiftCount(), bitsize = GPR[0].GetBitsize();

        boolean isNegative = cr < 0;
        int finalCr = cr % (int) Math.pow(2, bitsize-1);
        int res;

        // shift right
        if (LR == 0) {
            // arithmetically
            if (AL == 0) {
                res = (finalCr >> count) % (int) Math.pow(2, bitsize-1);
                if (res > 0 && isNegative) {
                    res = -res;
                }
            } else {
                res = (finalCr >>> count) % (int) Math.pow(2, bitsize-1);
                if (res > 0 && isNegative) {
                    res = -res;
                }
            }

            // set underflow
            if (finalCr > 0 && res < 0) {
                CC.Set(Util.setOrClearIthBit(CC.Get(), 1, true));
            }

        // shift left
        } else {
            res = (finalCr << count) % (int) Math.pow(2, bitsize-1);
            if (res > 0 && isNegative) {
                res = -res;
            }
        }

        PrintToConsole("SRC Successful, c(r) = " + res);
    }

    public void RRC() {
        int cr = GPR[IR.GetRegisterX()].Get();
        int AL = IR.GetShiftAL(), LR = IR.GetShiftLR();
        int count = IR.GetShiftCount(), bitsize = GPR[0].GetBitsize();

        boolean isNegative = cr < 0;
        int finalCr = cr % (int) Math.pow(2, bitsize-1);
        short res;

        // shift right
        if (LR == 0) {
            // arithmetically
            if (AL == 0) {
                res = (short) ((finalCr >> count) | (finalCr << (int) Math.pow(2, bitsize-1)));
                if (res > 0 && isNegative) {
                    res = (short) -res;
                }
            } else {
                res = (short) ((finalCr >>> count) | (finalCr << (int) Math.pow(2, bitsize-1)));
                if (res > 0 && isNegative) {
                    res = (short) -res;
                }
            }

        // shift left
        } else {
            res = (short) ((finalCr << count) | (finalCr >> (int) Math.pow(2, bitsize-1)));
            if (res > 0 && isNegative) {
                res = (short) -res;
            }
        }

        PrintToConsole("RRC Successful, c(r) = " + res);
    }

    public void IN() {
        int devID = IR.GetDevID();
        int rIdx = IR.GetLSRegister();
        if (devID == 0) {
            int val = Dev.PopKeyboardBuffer();
            GPR[rIdx].Set(val);
            PrintToConsole("IN Successful from Keyboard");

        } else if (devID == 2) {
            int val = Dev.PopCardBuffer();
            GPR[rIdx].Set(val);
            PrintToConsole("IN Successful from Card");
        } 
    }

    public void OUT() {
        int devID = IR.GetDevID();
        int rIdx = IR.GetLSRegister();
        if (devID == 1) {
            int val = GPR[rIdx].Get();
            Dev.PushToPrinterBuffer(val);
            PrintToConsole("OUT Successful to Printer");
        }
    }

    // Load 
    public void Load() {
        MBR.Set(Mem.ReadFromAddress(MAR.Get()));
        PrintToConsole("Load successful, data " + MBR.Get() + " read from loc " + MAR.Get());
    }

    public void Store() {
        int address = MAR.Get();
        int value = MBR.Get();
        Mem.WriteToAddress(address, value);

        PrintToConsole("Store successful, data " + value + " written to loc " + address);
    }

    public void IncrementPC() {
        PC.Set(PC.Get() + 1);
    }

    public boolean HasHalted() {
        return halt == 1;
    }

    public int ComputeEffectiveAddress() {
        int EA;
        int indirect = IR.GetLSIndirectBit();
        int ix = IR.GetLSIndexRegister();
        int address = IR.GetAddress();

        if (indirect == 0) {
            if (ix == 0) {
                EA = address;
            } else {
                EA = IXR[ix-1].Get() + address;
            }
        } else {
            if (ix == 0) {
                EA = Mem.ReadFromAddress(address);
            } else {
                EA = Mem.ReadFromAddress(IXR[ix-1].Get() + address);
            }
        }

        return EA;
    }

    // Getters and Setters for all the registers.

    public String GetPC() {
        return Util.FormatNumberString(Integer.toBinaryString(PC.Get()), PC.GetBitsize());
    }

    public String GetGPR(int num) {
        return Util.FormatNumberString(Integer.toBinaryString(GPR[num].Get()), GPR[num].GetBitsize());
    }

    public String GetIXR(int num) {
        return Util.FormatNumberString(Integer.toBinaryString(IXR[num-1].Get()), IXR[num-1].GetBitsize());
    }

    public String GetMAR() {
        return Util.FormatNumberString(Integer.toBinaryString(MAR.Get()), MAR.GetBitsize());
    }

    public String GetMBR() {
        return Util.FormatNumberString(Integer.toBinaryString(MBR.Get()), MBR.GetBitsize());
    }

    public String GetIR() {
        return Util.FormatNumberString(Integer.toBinaryString(IR.Get()), IR.GetBitsize());
    }

    public String GetCC() {
        return Util.FormatNumberString(Integer.toBinaryString(CC.Get()), CC.GetBitsize());
    }

    public String GetPrinter() {
        return Dev.GetPrinterBuffer();
    }

    public String GetKeyboardBuffer() {
        return Dev.GetKeyboardBuffer();
    }

    public String GetActiveCacheText() {
        String s = "";

        int[][] ac = Mem.CB.GetActiveCacheBlock();
        int fifoLength = Mem.CB.GetFifoLength();

        for (int i = 0; i < fifoLength; i++) {
            for (int j = 1; j < 10; j++) {
                if (j == 1) {
                    s += "Tag:" + Integer.toString(ac[i][j]) + " - ";
                } else {
                    s += Integer.toString(ac[i][j]) + " ";
                }
                
            }
            s += "\n";
        }

        return s;
    }

    public void SetKeyboardBuffer(String s) {
        Dev.SetKeyboardBuffer(s);
    }

    public void SetPC(String s) {
        if (Util.IsValidBooleanValue(s, PC.GetBitsize())) {
            PC.Set(Integer.parseInt(s,2));
        }
    }

    public void SetGPR(String s, int num) {

        if (Util.IsValidBooleanValue(s, GPR[num].GetBitsize())) {
            GPR[num].Set(Integer.parseInt(s,2));
        }
    }

    public void SetIXR(String s, int num) {
        if (Util.IsValidBooleanValue(s, IXR[num-1].GetBitsize())) {
            IXR[num-1].Set(Integer.parseInt(s,2));
        }
    }

    public void SetMAR(String s) {
        if (Util.IsValidBooleanValue(s, MAR.GetBitsize())) {
            MAR.Set(Integer.parseInt(s,2));
        }
    }

    public void SetMBR(String s) {
        if (Util.IsValidBooleanValue(s, MBR.GetBitsize())) {
            MBR.Set(Integer.parseInt(s,2));
        }
    }

    public void SetIR(String s) {
        if (Util.IsValidBooleanValue(s, IR.GetBitsize())) {
            IR.Set(Integer.parseInt(s,2));
        }
    }

    public void SetCC(String s) {
        if (Util.IsValidBooleanValue(s, CC.GetBitsize())) {
            CC.Set(Integer.parseInt(s,2));
        }
    }

    public void PrintToConsole(String s) {
        System.out.println(s);
        ConsoleText.add(s);
    }

    public ArrayList<String> GetConsoleText() {
        return ConsoleText;
    }

}