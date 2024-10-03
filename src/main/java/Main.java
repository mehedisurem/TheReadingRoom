import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }




    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));

            Scene scene = new Scene(fxmlLoader.load());


            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException | RuntimeException e) {
            Scene scene = new Scene(new Label(e.getMessage()), 400, 300);
            primaryStage.setTitle("Error");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}
