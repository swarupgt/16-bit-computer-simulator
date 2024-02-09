import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class Assembler {

    // Create the instruction-opcode map while constucting
    private HashMap<String, String> OpCode;
    private ArrayList<String[]> InputInstructions;
    private ArrayList<String> ListingFileContent, LoadingFileContent;

    // Constructor to create an Assembler object
    public Assembler(String inputFilename) {

        OpCode = new HashMap<String, String>();
        // Set the opcode HashMap
        ArrayList<String[]> OpCodes = ReadInput("InstructionList.txt");
        for (String[] str : OpCodes) {
            OpCode.put(str[1], str[0]);
        }

        InputInstructions = ReadInput(inputFilename);
        ListingFileContent = new ArrayList<String>();
        LoadingFileContent = new ArrayList<String>();
    }

    // Create array of String lines from given file
    private static ArrayList<String[]> ReadInput(String filename) {
        ArrayList<String> inputText = new ArrayList<String>();
        ArrayList<String[]> allInstructions = new ArrayList<String[]>();
        // Open and read input file
        try {
            File inp = new File(filename);
            Scanner FileScanner = new Scanner(inp);
            while (FileScanner.hasNextLine()) {
                inputText.add(FileScanner.nextLine());
            }

            FileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occured reading the input file.");
            e.printStackTrace();
        }

        // Split input text by whitespace
        for (String str : inputText) {
            String[] temp = str.split(" ");
            allInstructions.add(temp);
        }
        return allInstructions;
    }

    // Process instruction based on type of instruction and length of operands 
    private String ProcessInstruction(String[] inst) {
        String R = "00";
        String IX = "00";
        String I = "0";
        String OpCodeBinary = "000000";
        String Address = "00000";
        String[] Fields = inst[1].split(",");

        OpCodeBinary = Integer.toBinaryString(Integer.parseInt(OpCode.get(inst[0]), 8));

        switch(inst[0]) {
            case "LDR":
            case "LDA":
            case "STR":
            case "JCC":
            case "SOB":
            case "AMR":
            case "SMR":
            case "JGE":
                R = Integer.toBinaryString(Integer.parseInt(Fields[0]));
                IX = Integer.toBinaryString(Integer.parseInt(Fields[1]));
                Address = Integer.toBinaryString(Integer.parseInt(Fields[2]));
                if (Fields.length == 4) {
                    I = "1";
                } else {
                    I = "0";
                }
                // System.out.println(OpCodeBinary+ " "+ R + " "+IX + " " + I + " "+ Address+"---X");
                break;
            case "LDX":
            case "STX":
            case "JZ":
            case "JNE":
            case "JMA":
            case "JSR":
            case "MLT":
            case "DVD":
            case "TRR":
            case "AND":
            case "ORR":
                R = "00";
                IX = Integer.toBinaryString(Integer.parseInt(Fields[0]));
                Address = Integer.toBinaryString(Integer.parseInt(Fields[1]));
                if (Fields.length == 3) {
                    I = "1";
                }
                // System.out.println(OpCodeBinary+ " "+ R + " "+IX + " " + I + " "+ Address+"---X");
                break;
            case "SETCCE":
            case "NOT":
                R = FormatNumberString(Integer.toBinaryString(Integer.parseInt(Fields[0])),2);
                // System.out.println(OpCodeBinary+ " "+ R + " "+IX + " " + I + " "+ Address+"---X");
                break;
            case "RFS":
                Address = FormatNumberString(Integer.toBinaryString(Integer.parseInt(Fields[0])), 5);
                // System.out.println(OpCodeBinary+ " "+ R + " "+IX + " " + I + " "+ Address+"---X");
                break;
            case "AIR":
            case "SIR":
                R = Integer.toBinaryString(Integer.parseInt(Fields[0]));
                Address = FormatNumberString(Integer.toBinaryString(Integer.parseInt(Fields[1])), 5);
                // System.out.println(OpCodeBinary+ " "+ R + " "+IX + " " + I + " "+ Address+"---X");
                break;
            case "SRC":
            case "RRC":
                R = Integer.toBinaryString(Integer.parseInt(Fields[0]));
                IX = Fields[2]+Fields[3];
                Address = FormatNumberString(Integer.toBinaryString(Integer.parseInt(Fields[1])), 5);
                // System.out.println(OpCodeBinary+ " "+ R + " "+IX + " " + I + " "+ Address+"---X");
                break;
            case "IN":
            case "OUT":
            case "CHK":
                R = Integer.toBinaryString(Integer.parseInt(Fields[0]));
                Address = FormatNumberString(Integer.toBinaryString(Integer.parseInt(Fields[1])), 5);
                // System.out.println(OpCodeBinary+ " "+ R + " "+IX + " " + I + " "+ Address+"---X");
                break;
            default:
                return "Fail";
                

        }

        return FormatNumberString(OpCodeBinary, 6)+FormatNumberString(R, 2)+FormatNumberString(IX, 2)+FormatNumberString(I, 1)+FormatNumberString(Address, 5);
    }

    // Add specified string to loading file
    public void AddToLoadingFileContent(String line) {
        LoadingFileContent.add(line);
    }

    // Add the output columns and input instructions to the listing file
    public void AddToListingFileContent(String cols, String[] inp) {
        String res = "";
        for (String s : inp) {
            res += s + " ";
        }
        ListingFileContent.add(cols + "\t" + res);
    }

    // Generate the listing and loading files based on the arrays with the respective contents.
    public void CreateListingAndLoadingFiles() {
        try {
            FileWriter LoadingWriter = new FileWriter("loading.txt");
            FileWriter ListingWriter = new FileWriter("listing.txt");
            for (String line : LoadingFileContent) {
                LoadingWriter.write(line+"\n");
            }
            for (String line : ListingFileContent) {
                ListingWriter.write(line+"\n");
            }
            LoadingWriter.close();
            ListingWriter.close();
            System.out.println("Generated loading and listing files.");
        } catch (IOException e) {
            System.out.println("An error occurred writing to file(s).");
            e.printStackTrace();
        }
    }

    // Prefix a given number string with zeroes upto the specified n 
    public static String FormatNumberString(String s, int n) {
        int oldLength = s.length();
        if (oldLength < n) {
            for (int i = 0; i < (n-oldLength); i++) {
                s = "0"+s;
            }
        }
        return s;
    }
    
    // Convert a Binary string to Octal
    public static String BinaryToOctal(String num) {
        int n = Integer.parseInt(num, 2);
        return Integer.toOctalString(n);
    }

    // MAIN
    public static void main(String[] args) {

        // Instantiate an Assembler object with input file
        Assembler a = new Assembler(args[0]);

        // Initialise Program Counter to 0
        int ProgramCounter = 0;

        // Loop through instructions
        for (String[] inst : a.InputInstructions) {

            String column2 = "";

            if (inst[0].equals("LOC")) {

                // Output only needs to go to the listing file
                ProgramCounter = Integer.parseInt(inst[1]);
                a.AddToListingFileContent("      "+ "\t"+"      ", inst);
                continue;
            } else if (inst[0].equals("Data")) {

                // Set to 1024 if End mentioned
                if (inst[1].equals("End")) {
                    column2 = "10000000000"; 
                } else {
                    int n = Integer.parseInt(inst[1]);
                    String val = Integer.toBinaryString(n);
                    column2 = FormatNumberString(val, 16);
                }
            } else if (inst[0].equals("End:") && inst[1].equals("HLT")) {
                column2 = "0";
            } else {
                // Process given instruction
                column2 = a.ProcessInstruction(inst);
                // System.out.println(inst[0]+" "+inst[1] + "\t COLUMN 2: " + column2);
            }

            //convert Program Counter and word to octal
            String PCOctalString = FormatNumberString(Integer.toOctalString(ProgramCounter), 6);
            String instOctalString = FormatNumberString(Integer.toOctalString(Integer.parseInt(column2, 2)), 6);

            // write to loading and listing files
            a.AddToLoadingFileContent(PCOctalString+ "\t"+instOctalString);
            a.AddToListingFileContent(PCOctalString+ "\t"+instOctalString, inst);
            System.out.println(PCOctalString+ "\t"+instOctalString);
            

            // update PC
            ProgramCounter++;
        }

        a.CreateListingAndLoadingFiles();
        System.out.println("All done.");

    }
}