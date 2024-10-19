package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminUpdateStockController {

    @FXML
    private TextField QuantityText;

    @FXML
    private Label bookAuthorLabel;

    @FXML
    private ComboBox<?> bookComboBox;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField priceText;

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
