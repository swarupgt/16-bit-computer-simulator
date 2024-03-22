package src.main.java;

import java.io.InputStream;
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
            // File inp = new File(filename);
            InputStream inp = Util.class.getResourceAsStream(filename);
            Scanner FileScanner = new Scanner(inp);
            while (FileScanner.hasNextLine()) {
                inputText.add(FileScanner.nextLine());
            }

            FileScanner.close();
        } catch (Exception e) {
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

    public static int setOrClearIthBit(int num, int i, boolean set) {
        // Create a mask with the ith bit set to 1 using left shift.
        int mask = 1 << i;

        // Use bitwise operations based on the set boolean:
        if (set) {
            // Set the ith bit using bitwise OR.
            return num | mask;
        } else {
            // Clear the ith bit using bitwise AND with inverted mask.
            int invertedMask = ~mask;
            return num & invertedMask;
        }
    }

    public static int getIthBit(int num, int i) {
        int mask = 1 << i;
        return num & mask;
    }

    public static int[] multiplyRegisters(int crx, int cry, int bitLength) {
        int a[] = new int[2];
        int res = crx * cry;

        a[0] = (int) (res >> bitLength);
        a[1] = (int) (res & 0xFFFF);

        return a;
    }

    public static int[] divideRegisters(int crx, int cry) {
        int a[] = new int[2];
        a[0] = (int) (crx / cry);
        a[1] = (int) (crx % cry);

        return a;
    }
}
