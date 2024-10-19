package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViewStockController {

    @FXML
    private TableColumn<?, ?> T_Author;

    @FXML
    private TableColumn<?, ?> T_Copies;

    @FXML
    private TableColumn<?, ?> T_Price;

    @FXML
    private TableColumn<?, ?> T_Sold;

    @FXML
    private TableColumn<?, ?> T_Title;

    @FXML
    private BorderPane borderPane;

    @FXML
    void DashboardOnClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Admin Scene");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void LogOutOnClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Login Scene");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void UpdateStockOnClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AdminUpdateStock.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Admin UpdateStock Scene");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void ViewStockOnClick(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AdminViewStock.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("SignUp Scene");
        stage.setScene(scene);
        stage.show();
    }

}
