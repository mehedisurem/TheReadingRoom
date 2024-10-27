package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController implements Controller, Initializable {
    @FXML
    private Label SignInpageAlert;

    @FXML
    private TextField SignUpFName;

    @FXML
    private TextField SignUpLName;

    @FXML
    private TextField SignUpUserName;

    @FXML
    private TextField SignupPassword;

    @FXML
    private AnchorPane rootPane;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        // Initialize signup view if needed
    }

    // Creating a new user and store it to database
    @FXML
    void CreateAccountButtonOnClick(ActionEvent event) throws IOException {
        String username = SignUpUserName.getText();
        String password = SignupPassword.getText();
        String fname = SignUpFName.getText();
        String lname = SignUpLName.getText();

        try {
            User newUser = model.getUserDao().createUser(username, password, fname, lname);
            if (newUser != null) {
                model.setCurrentUser(newUser);
                if ("admin".equals(newUser.getUsername()) && "reading_admin".equals(newUser.getPassword())) {
                    navigateTo("/view/AdminDashboard.fxml", "Admin Dashboard");
                } else {
                    navigateTo("/view/Dashboard.fxml", "Dashboard");
                }

            } else {
                SignInpageAlert.setText("Error creating account");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SignInpageAlert.setText("Error creating account");
        }
    }

    @FXML
    void switchToLogInScene(MouseEvent event) throws IOException {
        navigateTo("/view/Login.fxml", "Login");
    }

    private void navigateTo(String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        controller.setModel(model);
        if (controller instanceof Initializable) {
            ((Initializable) controller).initData();
        }
        Stage stage = (Stage) SignUpUserName.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}