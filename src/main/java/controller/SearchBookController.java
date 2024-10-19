package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;

public class SearchBookController implements Controller, Initializable {

    @FXML
    private Pane Card1;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label card1Author;
    @FXML
    private Label card1Copies;
    @FXML
    private Label card1Sold;
    @FXML
    private Label card1Title;
    @FXML
    private ImageView card1img;
    @FXML
    private Label card1price;
    @FXML
    private TextField selectedbook_Quantity;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        // Initialize data for the search book view
    }

    @FXML
    void CheckOutOnClick(MouseEvent event) {
        // Implement checkout functionality
    }

    @FXML
    void DashboardOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/Dashboard.fxml", "Dashboard");
    }

    @FXML
    void EditProfileOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/EditProfile.fxml", "Edit Profile");
    }

    @FXML
    void ExportOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/ExportFile.fxml", "Export File");
    }

    @FXML
    void LogOutOnClick(MouseEvent event) throws IOException {
        model.setCurrentUser(null);
        navigateTo("/view/Login.fxml", "Login");
    }

    @FXML
    void SearchBookOnClick(MouseEvent event) throws IOException {
        // Refresh current scene or handle navigation
    }

    @FXML
    void viewOrderOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/ViewOrder.fxml", "View Order");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cart Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void addToCartOnClick(ActionEvent event) {
        try {
            int quantity = Integer.parseInt(selectedbook_Quantity.getText());
            // Implement add to cart logic here
            selectedbook_Quantity.clear();
            showAlert("Book added to cart successfully!");
        } catch (NumberFormatException ex) {
            showAlert("Please enter a valid quantity.");
        }
    }

    @FXML
    void viewCartOnClick(ActionEvent event) throws IOException {
        navigateTo("/view/ViewCart.fxml", "View Cart");
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