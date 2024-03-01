package src;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CPU {

    private static HashMap<Integer, String> OpCodeMap = new HashMap<Integer, String>();

    private Register PC = new Register(12);
    private Register CC = new Register(4);
    private InstructionRegister IR = new InstructionRegister();
    private Register MAR = new Register(12);
    private Register MBR = new Register();
    private Register[] GPR = new Register[4];
    private Register[] IXR = new Register[3];

    private int halt = 0;

    private Memory Mem;

    public CPU() {
        // Set the OpCode Map

        ArrayList<String[]> OpCodes = Util.ReadInput("src/opcodes.txt");
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

        // Set first non-protected location and halt to view fault
        Mem.WriteToAddress(1, 6);
        Mem.WriteToAddress(6, 0b0000000000000000);
        halt = 0;
    }

    // Read input file and load it into memory.
    public void LoadFromROM(String Filepath) {
        File f = new File(Filepath);
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
            BufferedReader br = new BufferedReader(reader);
            String l = "";
            while (l != null) {
                l = br.readLine();
                if (l == null) break;
                String[] insts = l.split(" ");
                
                // can use later for fault handling
                // we add 7 to reserve the first 6 for faults
                int PCMemValue = Integer.parseInt(insts[0], 8) + 7;
                int inst = Integer.parseInt(insts[1], 8);
                int faultIndex = Mem.WriteToAddress(PCMemValue, inst);
                if (faultIndex == -1) {
                    System.out.println("Incorrect address provided");
                }
                boolean initPC = false;
                if (!initPC && Util.IsValidInstruction(inst)) {
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
        int faultIndex = Mem.ReadFromAddress(MAR.Get()+7);
        if (faultIndex == -1) {
            System.out.println("Incorrect address provided");
        }
        
        // Get instruction from memory pointed to by MAR and store in MBR
        MBR.Set(Mem.ReadFromAddress(MAR.Get()+7));

        // Set IR as the instruction stored in MBR
        IR.Set(MBR.Get());
        ExecuteInstruction();

        return true;

    }

    private void ExecuteInstruction() {
        switch (OpCodeMap.get(IR.GetOpCode())) {
            case "LDR":
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
        }
    }

    public void LDR() {
        int EA = ComputeEffectiveAddress();
        MAR.Set(EA);

        int res = Mem.ReadFromAddress(MAR.Get()+7);
        if (res < 0) {
            System.out.println("Memory fault occured, code " + res);
        }
        // Set MBR as value fetched using address in MAR
        MBR.Set(res);
        // Set GPR register as value present in MBR
        GPR[IR.GetLSRegister()].Set(MBR.Get());

        System.out.println("LDR successful, data " + res + "read from loc " + MAR.Get());
    }

    public void STR() {
        int EA = ComputeEffectiveAddress();
        MAR.Set(EA);

        int res = Mem.ReadFromAddress(MAR.Get()+7);
        if (res < 0) {
            System.out.println("Memory fault occured, code " + res);
        }

        // Set MAR to Ea computed
        MAR.Set(EA);
        // Set MBR as specified register's value to be written
        MBR.Set(GPR[IR.GetLSRegister()].Get());
        // Write MBR data into memory 
        Mem.WriteToAddress(MAR.Get()+7, MBR.Get());

        System.out.println("STR successful, data " + Mem.ReadFromAddress(MAR.Get()) + "written to loc " + MAR.Get());
    }

    public void LDA() {

    }

    public void LDX() {

    }

    public void STX() {

    }

    // Load 
    public void Load() {
        MBR.Set(Mem.ReadFromAddress(MAR.Get()+7));
    }

    public void Store() {
        int address = MAR.Get();
        int value = MBR.Get();
        Mem.WriteToAddress(address+7, value);
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

        System.out.println("inside SetGPR, s and num = " + s + num);
        System.out.println("GPR num val: " + GPR[num].Get());

        if (Util.IsValidBooleanValue(s, GPR[num].GetBitsize())) {
            System.out.println("it is a valid number");
            GPR[num].Set(Integer.parseInt(s,2));
            System.out.println("New GPR num val: " + GPR[num].Get());
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

}