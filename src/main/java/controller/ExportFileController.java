package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;

public class ExportFileController implements Controller, Initializable {

    @FXML
    private TableColumn<?, ?> BookTitleColumn;
    @FXML
    private TableColumn<?, ?> PriceColumn;
    @FXML
    private TableColumn<?, ?> QuantityColumn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField fileNameTextField;
    @FXML
    private TableColumn<?, ?> orderDateColumn;
    @FXML
    private TableView<?> ordersTableView;
    @FXML
    private CheckBox selectAllCheckBox;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        // Initialize data for the export file view
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
        // Refresh current scene or handle navigation
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
    void chooseFileLocation(ActionEvent event) {
        // Implement file location choosing logic
    }

    @FXML
    void exportSelectedOrders(ActionEvent event) {
        // Implement order exporting logic
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        navigateTo("/view/Dashboard.fxml", "Dashboard");
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