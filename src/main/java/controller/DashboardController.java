package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Model;
import model.User;

import java.io.IOException;

public class DashboardController implements Controller, Initializable {
    @FXML
    private Pane Card1, Card2, Card3, Card4, Card5;

    @FXML
    private Label card1Author, card1Copies, card1Sold, card1Title, card1price;
    @FXML
    private Label card2Author, card2Copies, card2Sold, card2Title, card2price;
    @FXML
    private Label card3Author, card3Sold, card3Title, card3price;
    @FXML
    private Label card4Author, card4Copies, card4Sold, card4Title, card4price;
    @FXML
    private Label card5Author, card5Copies, card5Sold, card5Title, card5price;

    @FXML
    private ImageView card1img, card2img, card3img, card4img, card5img;

    @FXML
    private Text userWelcomeMsg;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private BorderPane borderPane;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        User currentUser = model.getCurrentUser();
        if (currentUser != null) {
            userWelcomeMsg.setText("Welcome, " + currentUser.getFname() + " " + currentUser.getLname() + "!");
        }
        // Initialize other dashboard data here
    }

    @FXML
    void CheckOutOnClick(MouseEvent event) {
        // Implement checkout functionality
    }

    @FXML
    void DashboardOnClick(MouseEvent event) throws IOException {
        // Refresh dashboard or handle navigation
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
        navigateTo("/view/SearchBook.fxml", "Search Books");
    }

    @FXML
    void viewOrderOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/ViewOrder.fxml", "View Order");
    }

    private void navigateTo(String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        controller.setModel(model);
        if (controller instanceof Initializable) {
            ((Initializable) controller).initData();
        }
        Stage stage = (Stage) userWelcomeMsg.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}