package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

public class EditProfileController implements Controller, Initializable {
    @FXML
    private TextField FnameText;
    @FXML
    private TextField LnameText;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField currPassText;
    @FXML
    private TextField newPassText;
    @FXML
    private Label userNameText;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        User currentUser = model.getCurrentUser();
        if (currentUser != null) {
            userNameText.setText(currentUser.getUsername());
            FnameText.setText(currentUser.getFname());
            LnameText.setText(currentUser.getLname());
        }
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
        // Refresh current scene or handle navigation
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
        navigateTo("/view/SearchBook.fxml", "Search Books");
    }

    @FXML
    void viewOrderOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/ViewOrder.fxml", "View Order");
    }

    @FXML
    void UpdateProfileOnClick(ActionEvent event) {
        User currentUser = model.getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user logged in");
            return;
        }

        String newFname = FnameText.getText().trim();
        String newLname = LnameText.getText().trim();
        String currentPassword = currPassText.getText();
        String newPassword = newPassText.getText();

        // Validate inputs
        if (newFname.isEmpty() || newLname.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "First name and last name cannot be empty");
            return;
        }

        // Check if password is being changed
        if (!currentPassword.isEmpty() || !newPassword.isEmpty()) {
            if (!currentPassword.equals(currentUser.getPassword())) {
                showAlert(Alert.AlertType.ERROR, "Invalid Password", "Current password is incorrect");
                return;
            }
            if (newPassword.length() < 8) {
                showAlert(Alert.AlertType.WARNING, "Invalid Password", "New password must be at least 8 characters long");
                return;
            }
        }

        // Update user information
        currentUser.setFname(newFname);
        currentUser.setLname(newLname);
        if (!newPassword.isEmpty()) {
            currentUser.setPassword(newPassword);
        }

        try {
            model.getUserDao().updateUser(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully");
            // Refresh the current user in the model
            model.setCurrentUser(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating profile: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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