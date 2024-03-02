package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Util {

    // Reads given file.
    public static ArrayList<String[]> ReadInput(String filename) {
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

    // Check if string is boolean and of desired bit length.
    public static boolean IsValidBooleanValue(String s, int len) {
        if (s.length() > len) {
            return false;
        }

        for (char c : s.toCharArray()) {
            if (c != '0' && c != '1') {
                return false;
            }
        }

        return true;
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

    // Returns whether given instruction has a valid opcode.
    public static boolean IsValidInstruction(int inst) {
        int opcode = inst / (int) Math.pow(2, 10);
        System.out.println("instruction valid for pc: " + opcode);
        return (opcode > 0 && opcode < 64 && (inst % (int) Math.pow(2, 5) != 0));
    }

    // Convert Octal to Binary
    public static String ConvertOctalToBinary(String oct) {
        if (IsOctal(oct)) {
            int dec = Integer.parseInt(oct, 8);
            return Integer.toBinaryString(dec);
        }
        return "";
    }

    public static boolean IsOctal(String s) {
        return Pattern.matches("^[0-7]+$", s);
    }
}
