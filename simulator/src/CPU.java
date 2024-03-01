package src;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CPU {

    private static HashMap<Integer, String> OpCodeMap;

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
        ArrayList<String[]> OpCodes = Util.ReadInput("opcodes.txt");
        for (String[] str : OpCodes) {
            OpCodeMap.put(Integer.parseInt(str[0]), str[1]);
        }
    }

    // Set initial components of the computer
    public void Initialise() {
        GPR[0].Set(0);
        GPR[1].Set(0);
        GPR[2].Set(0);
        GPR[3].Set(0);
        IXR[0].Set(0);
        IXR[1].Set(100);
        IXR[2].Set(1000);
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
                int faultIndex = Mem.WriteToAddress(Integer.parseInt(insts[0], 8) + 7, Integer.parseInt(insts[1], 8));
                if (faultIndex == -1) {
                    System.out.println("Incorrect address provided");
                }
                System.out.println("loaded instructions from ROM to memory successfully :)");
            }
            br.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Run a single instruction.
    public void RunOneStep() {
        if (halt == 1) {
            return;
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


    }

    private void ExecuteInstruction() {
        switch (OpCodeMap.get(IR.GetOpCode())) {
            case "LDR":
                Load();
                break;
            case "STR":
                Store();
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

    public void Load() {

    }

    public void Store() {

    }

    public void LDA() {

    }

    public void LDX() {

    }

    public void STX() {

    }

    public void IncrementPC() {
        PC.Set(PC.Get() + 1);
    }

}