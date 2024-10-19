package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.io.IOException;

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
        // Implement profile update logic
        String newFname = FnameText.getText();
        String newLname = LnameText.getText();
        String currentPassword = currPassText.getText();
        String newPassword = newPassText.getText();

        User currentUser = model.getCurrentUser();
        if (currentUser != null) {
            // Update user information in the model
            currentUser.setFname(newFname);
            currentUser.setLname(newLname);
            if (!currentPassword.isEmpty() && !newPassword.isEmpty()) {
                // Verify current password and update to new password
                // This is a simplistic approach, you should add proper validation
                if (currentPassword.equals(currentUser.getPassword())) {
                    currentUser.setPassword(newPassword);
                } else {
                    // Show error message for incorrect current password
                }
            }
            // Update the user in the database
            // model.getUserDao().updateUser(currentUser);
        }
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