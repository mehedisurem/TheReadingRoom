package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;
import model.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
public class AdminUpdateStockController implements Controller, Initializable {

    @FXML
    private TextField QuantityText;

    @FXML
    private Label bookAuthorLabel;

    @FXML
    private ComboBox<String> bookComboBox;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label priceText;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        loadBooks();

        // Add listener to ComboBox selection
        bookComboBox.setOnAction(event -> updateBookInfo());
    }
    private void loadBooks() {
        try {
            List<Book> books = model.getBookDao().getAllBooks();
            List<String> bookTitles = books.stream()
                    .map(Book::getTitle)
                    .collect(Collectors.toList());
            bookComboBox.setItems(FXCollections.observableArrayList(bookTitles));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load books: " + e.getMessage());
        }
    }

    private void updateBookInfo() {
        String selectedTitle = bookComboBox.getValue();
        if (selectedTitle != null) {
            try {
                Book book = model.getBookDao().getBookByTitle(selectedTitle);
                if (book != null) {
                    String getQuantity = Integer.toString(book.getPhysicalCopies());
                    String getPrice = Double.toString(book.getPrice());
                    bookAuthorLabel.setText(book.getAuthors());
                    QuantityText.setText(getQuantity);
                    priceText.setText(getPrice);
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load book details: " + e.getMessage());
            }
        }
    }

    @FXML
    void UpdateStockOnClick(ActionEvent event) {
        String selectedTitle = bookComboBox.getValue();
        if (selectedTitle == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a book first");
            return;
        }

        try {
            int newQuantity = Integer.parseInt(QuantityText.getText().trim());
            if (newQuantity < 0) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Quantity cannot be negative");
                return;
            }

            model.getBookDao().updateBookStock(selectedTitle, newQuantity);
            updateBookInfo(); // Refresh the display
            showAlert(Alert.AlertType.INFORMATION, "Success", "Stock updated successfully");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a valid number");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update stock: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
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