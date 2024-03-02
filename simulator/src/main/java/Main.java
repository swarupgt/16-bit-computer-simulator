package src.main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.setMainWindow(primaryStage);
            primaryStage.setTitle("Team 5 Simulator");
            primaryStage.setScene(new Scene(root, 1223, 846));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}