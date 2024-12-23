package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController implements Controller, Initializable {
    @FXML
    private TextField LoginPagePassword;

    @FXML
    private TextField LoginPageUsername;

    @FXML
    private Label errorLabel;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        // Initialize login view if needed
    }

    // Login Credential check and navigate scene/ alert based on that
    @FXML
    void LogInOnClick(ActionEvent event) throws IOException {
        String username = LoginPageUsername.getText();
        String password = LoginPagePassword.getText();

        try {
            User user = model.getUserDao().getUser(username, password);
            if (user != null) {
                model.setCurrentUser(user);
                if ("admin".equals(user.getUsername()) && "reading_admin".equals(user.getPassword())) {
                    navigateTo("/view/AdminDashboard.fxml", "Admin Dashboard");
                } else {
                    navigateTo("/view/Dashboard.fxml", "Dashboard");
                }
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Error logging in");
        }
    }

    @FXML
    void SignupPageSignInOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/SignUp.fxml", "Sign Up");
    }


    private void navigateTo(String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        System.out.println("Setting model in new controller: " + controller.getClass().getSimpleName());
        controller.setModel(model);
        if (controller instanceof Initializable) {
            System.out.println("Initializing data in new controller");
            ((Initializable) controller).initData();
        }
        Stage stage = (Stage) LoginPageUsername.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}