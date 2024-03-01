package src;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class Controller {

    private Stage mainWindow;

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

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
        this.mainWindow.setTitle("Team 5 Simulator");
    }

    @FXML
    void OnIPLButtonClick(ActionEvent event) {

    }

    @FXML
    void OnInitButtonClick(ActionEvent event) {

    }

    @FXML
    void OnLoadButtonClick(ActionEvent event) {

    }

    @FXML
    void OnRunButtonClick(ActionEvent event) {
        String text = Octal_text.getText();
        Binary_text.setText(text);
    }

    @FXML
    void OnStepButtonClick(ActionEvent event) {

    }

    @FXML
    void OnStoreButtonClick(ActionEvent event) {

    }

    @FXML
    void PutBinary(ActionEvent event) {

    }

}
