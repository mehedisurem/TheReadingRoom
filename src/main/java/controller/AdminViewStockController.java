package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;

public class AdminViewStockController implements Controller, Initializable {

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

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        // Initialize view stock data here
        // For example, populate the table with stock data
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
        navigateTo("/view/AdminUpdateStock.fxml", "Admin Update Stock");
    }

    @FXML
    void ViewStockOnClick(MouseEvent event) throws IOException {
        // Refresh current scene or handle navigation
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