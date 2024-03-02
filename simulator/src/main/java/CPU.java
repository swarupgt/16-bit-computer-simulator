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

    // Halt indicator
    private int halt = 0;

    // Memory
    private Memory Mem;

    private ArrayList<String> ConsoleText = new ArrayList<String>();

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
    }


    // Run a single instruction.
    public boolean RunOneStep() {
        if (halt == 1) {
            return false;
        }

        // Set MAR based on PC
        MAR.Set(PC.Get());
        IncrementPC();
        System.out.println("MAR value: " + MAR.Get());
        int faultIndex = Mem.ReadFromAddress(MAR.Get());
        if (faultIndex == -1) {
            System.out.println("Incorrect address provided");
        }
        
        // Get instruction from memory pointed to by MAR and store in MBR
        MBR.Set(Mem.ReadFromAddress(MAR.Get()));
        System.out.println("MBR value: " + MBR.Get());

        // Set IR as the instruction stored in MBR
        IR.Set(MBR.Get());
        System.out.println("IR values: " + IR.GetOpCode() + " " + IR.GetLSIndexRegister());
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
                    break;
                }
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
            default:
                // Unknown Opcode
                halt = 1;
                PrintToConsole("Unknown opcode detected");
        }
    }

    public void LDR() {
        int EA = ComputeEffectiveAddress();
        MAR.Set(EA);

        int res = Mem.ReadFromAddress(MAR.Get());
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