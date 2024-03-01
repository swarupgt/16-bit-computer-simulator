package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Util {
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
}
