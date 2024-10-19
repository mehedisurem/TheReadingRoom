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
import model.Model;

import java.io.IOException;

public class AdminUpdateStockController implements Controller, Initializable {

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

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        // Initialize update stock view data if needed
    }

    @FXML
    void DashboardOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/AdminDashboard.fxml", "Admin Dashboard");
    }

    @FXML
    void LogOutOnClick(MouseEvent event) throws IOException {
        model.setCurrentUser(null);
        navigateTo("/view/Login.fxml", "Login");
    }

    @FXML
    void UpdateStockOnClick(MouseEvent event) throws IOException {
        // Implement stock update logic here
    }

    @FXML
    void ViewStockOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/AdminViewStock.fxml", "Admin View Stock");
    }

    private void navigateTo(String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        controller.setModel(model);
        if (controller instanceof Initializable) {
            ((Initializable) controller).initData();
        }
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}