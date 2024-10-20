
import controller.Controller;
import controller.Initializable;
import dao.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Model model = new Model();
            model.setup();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Controller controller = fxmlLoader.getController();
            controller.setModel(model);
            if (controller instanceof Initializable) {
                ((Initializable) controller).initData();
            }

            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException | SQLException e) {
            Scene scene = new Scene(new Label("Error: " + e.getMessage()), 800, 600);
            primaryStage.setTitle("Error");
            primaryStage.setScene(scene);
            primaryStage.show();
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        Database.closeConnection();
        Platform.exit();
    }
}