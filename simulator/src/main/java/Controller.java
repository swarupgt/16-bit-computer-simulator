package src.main.java;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

// Class that controls the GUI
public class Controller {

    private CPU cpu = new CPU();

    private Stage mainWindow;

    @FXML
    private TextArea ConsoleOutput_text;

    @FXML
    private TextField GPR0_text;

    @FXML
    private Button GPR0_btn;

    @FXML
    private TextField GPR1_text;

    @FXML
    private Button GPR1_btn;

    @FXML
    private TextField GPR2_text;

    @FXML
    private Button GPR2_btn;

    @FXML
    private TextField GPR3_text;

    @FXML
    private Button GPR3_btn;

    @FXML
    private TextField IXR1_text;

    @FXML
    private Button IXR1_btn;

    @FXML
    private TextField IXR2_text;

    @FXML
    private Button IXR2_btn;

    @FXML
    private TextField IXR3_text;

    @FXML
    private Button IXR3_btn;

    @FXML
    private TextField PC_text;

    @FXML
    private Button PC_btn;

    @FXML
    private TextField MAR_text;

    @FXML
    private Button MAR_btn;

    @FXML
    private TextField MBR_text;

    @FXML
    private Button MBR_btn;

    @FXML
    private TextField IR_text;

    @FXML
    private Button Store_btn;

    @FXML
    private Button Load_btn;

    @FXML
    private Button Step_btn;

    @FXML
    private Button INIT_btn;

    @FXML
    private Button Run_btn;

    @FXML
    private Button IPL_btn;

    @FXML
    private TextField InputFile_text;

    @FXML
    private TextField Binary_text;

    @FXML
    private TextField Octal_text;

    @FXML
    private TextField CC_text;

    @FXML
    private TextField MFR_text;

    @FXML
    private Button MBR_btn1;

    @FXML
    private TextField Keyboard_text;

    @FXML
    private TextArea ConsolePrinter_text;

    @FXML
    private TextArea Cache_text;

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
        this.mainWindow.setTitle("Team 5 Simulator");
    }

    // Set all GUI values based on the CPU state.
    public void UpdateAllGUIValues() {
        System.out.println("updating GUI now");
        GPR0_text.setText(cpu.GetGPR(0));
        GPR1_text.setText(cpu.GetGPR(1));
        GPR2_text.setText(cpu.GetGPR(2));
        GPR3_text.setText(cpu.GetGPR(3));
        IXR1_text.setText(cpu.GetIXR(1));
        IXR2_text.setText(cpu.GetIXR(2));
        IXR3_text.setText(cpu.GetIXR(3));
        PC_text.setText(cpu.GetPC());
        MAR_text.setText(cpu.GetMAR());
        MBR_text.setText(cpu.GetMBR());
        IR_text.setText(cpu.GetIR());
        CC_text.setText(cpu.GetCC());

        ConsolePrinter_text.setText(cpu.GetPrinter());
        Cache_text.setText(cpu.GetActiveCacheText());



        UpdateConsoleOutput();
    }

    @FXML
    void OnIPLButtonClick(ActionEvent event) {
        String filepath = InputFile_text.getText();

        // check if file exists
        File f = new File(filepath);
        if (!f.exists()) {
            InputFile_text.setStyle("-fx-control-inner-background: #FFB6C1;");
            cpu.PrintToConsole("input file not found");
            UpdateAllGUIValues();
        }
        else {
            InputFile_text.setStyle("-fx-control-inner-background: white;");
            cpu.Initialise();
            cpu.LoadFromROM(filepath);
            UpdateAllGUIValues();
        }
    }

    @FXML
    void OnInitButtonClick(ActionEvent event) {
        cpu.Initialise();
        cpu.PrintToConsole("CPU has been reset");
        UpdateAllGUIValues();
    }

    @FXML
    void OnLoadButtonClick(ActionEvent event) {
        cpu.Load();
        UpdateAllGUIValues();
    }

    @FXML
    void OnRunButtonClick(ActionEvent event) {

        while (!cpu.HasHalted()) {
            cpu.RunOneStep();
            UpdateAllGUIValues();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();                
            }
        }

        cpu.PrintToConsole("Halt occurred");
        UpdateAllGUIValues();
        
    }

    @FXML
    void OnStepButtonClick(ActionEvent event) {
        cpu.RunOneStep();
        if (cpu.HasHalted()) {
            cpu.PrintToConsole("Halt occured");
        }
        UpdateAllGUIValues();
    }

    @FXML
    void OnStoreButtonClick(ActionEvent event) {
        cpu.Store();
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryCC(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetCC(bin);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryGPR0(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetGPR(bin, 0);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryGPR1(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetGPR(bin, 1);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryGPR2(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetGPR(bin, 2);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryGPR3(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetGPR(bin, 3);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryIXR1(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetIXR(bin, 1);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryIXR2(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetIXR(bin, 2);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryIXR3(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetIXR(bin, 3);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryMAR(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetMAR(bin);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryMBR(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetMBR(bin);
        UpdateAllGUIValues();
    }

    @FXML
    void PutBinaryPC(ActionEvent event) {
        String bin = Binary_text.getText();
        cpu.SetPC(bin);
        UpdateAllGUIValues();
    }

    @FXML
    void SetBinaryTextValue(ActionEvent event) {
        SetBinaryTextFromOctalInput();
        UpdateAllGUIValues();
    }

    @FXML
    void OnKeyboardEnter(ActionEvent event) {
        String kbf = Keyboard_text.getText();
        cpu.SetKeyboardBuffer(kbf);
        UpdateAllGUIValues();
    }

    String SetBinaryTextFromOctalInput() {
        String oct = Octal_text.getText(), bin;
        bin = Util.ConvertOctalToBinary(oct);
        if (bin != "") {
            Binary_text.setText(bin);
            Octal_text.setStyle("-fx-control-inner-background: white;");
        }
        else {
            cpu.PrintToConsole("Incorrect base for input");
            Octal_text.setStyle("-fx-control-inner-background: #FFB6C1;");
        }

        return bin;
    }

    private void UpdateConsoleOutput() {
        ArrayList<String> cl = cpu.GetConsoleText();
        String res = "";
        for (int i = cl.size()-1; i >= 0; i--) {
            res = res + cl.get(i) + "\n";
        }

        ConsoleOutput_text.setText(res);
    }

}
