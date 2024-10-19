package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;

public class AdminDashboardController implements Controller, Initializable {

    @FXML
    private BorderPane borderPane;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        // Initialize admin dashboard data if needed
    }

    @FXML
    void DashboardOnClick(MouseEvent event) throws IOException {
        // Refresh current scene or handle navigation
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