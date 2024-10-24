package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Book;
import model.Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminViewStockController implements Controller, Initializable {

    @FXML
    private TableView<Book> bookTableView;

    @FXML
    private TableColumn<Book, String> T_Title;

    @FXML
    private TableColumn<Book, String> T_Author;

    @FXML
    private TableColumn<Book, Double> T_Price;

    @FXML
    private TableColumn<Book, Integer> T_Copies;

    @FXML
    private TableColumn<Book, Integer> T_Sold;

    @FXML
    private BorderPane borderPane;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        setupTableColumns();
        loadBookData();
    }

    private void setupTableColumns() {
        T_Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        T_Author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        T_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        T_Copies.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));
        T_Sold.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));

        // Set column resize policies
        bookTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add custom formatting for price column if needed
        T_Price.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Book, Double>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", price));
                    }
                }
            };
        });
    }

    private void loadBookData() {
        try {
            List<Book> books = model.getBookDao().getAllBooks();
            ObservableList<Book> bookList = FXCollections.observableArrayList(books);
            bookTableView.setItems(bookList);
        } catch (SQLException e) {
            showError("Error loading books", e.getMessage());
        }
    }

    private void showError(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        navigateTo("/view/AdminUpdateStock.fxml", "Update Stock");
    }

    @FXML
    void ViewStockOnClick(MouseEvent event) throws IOException {
        // Refresh the current view
        loadBookData();
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