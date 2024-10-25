package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Model;
import model.CartItem;
import model.ShoppingCart;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;

public class ViewCartController implements Controller, Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label cartTotalAmount;

    @FXML
    private TableView<CartItem> cartTableView;

    @FXML
    private TableColumn<CartItem, String> titleColumn;

    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;

    @FXML
    private TableColumn<CartItem, Double> priceColumn;

    @FXML
    private TableColumn<CartItem, Double> totalColumn;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        if (titleColumn != null) {
            setupTableColumns();
            loadCartItems();
        } else {
            System.err.println("Table columns not properly initialized!");
        }
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBook().getTitle()));

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getBook().getPrice()).asObject());

        totalColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getTotal()).asObject());

        // Add currency formatting for price and total columns
        priceColumn.setCellFactory(tc -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        totalColumn.setCellFactory(tc -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", total));
                }
            }
        });
    }

    private void loadCartItems() {
        if (model.getCurrentUser() != null) {
            ShoppingCart cart = model.getCartForUser(model.getCurrentUser().getUsername());
            cartTableView.getItems().setAll(cart.getItems());
            updateTotalAmount();
        }
    }

    private void updateTotalAmount() {
        ShoppingCart cart = model.getCartForUser(model.getCurrentUser().getUsername());
        cartTotalAmount.setText(String.format("Total Amount: $%.2f", cart.getTotal()));
    }
    @FXML
    void updateQuantity(ActionEvent event) {
        CartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog(
                    String.valueOf(selectedItem.getQuantity()));
            dialog.setTitle("Update Quantity");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new quantity:");

            dialog.showAndWait().ifPresent(result -> {
                try {
                    int newQuantity = Integer.parseInt(result);
                    if (newQuantity > 0) {
                        if (model.validateBookQuantity(selectedItem.getBook(), newQuantity)) {
                            ShoppingCart cart = model.getCartForUser(model.getCurrentUser().getUsername());
                            cart.updateQuantity(selectedItem.getBook().getTitle(), newQuantity);
                            loadCartItems();
                        } else {
                            showAlert("Insufficient stock available");
                        }
                    } else {
                        showAlert("Please enter a valid quantity");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Please enter a valid number");
                } catch (Exception e) {
                    showAlert("Error updating quantity");
                }
            });
        }
    }

    @FXML
    void removeItem(ActionEvent event) {
        CartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ShoppingCart cart = model.getCartForUser(model.getCurrentUser().getUsername());
            cart.removeItem(selectedItem.getBook().getTitle());
            loadCartItems();
            showAlert("Item removed from cart");
        }
    }

    @FXML
    void CheckOutOnClick(ActionEvent event) {
        // Refresh dashboard or handle navigation
    }

    @FXML
    void PlaceOrderOnClick(ActionEvent event) throws IOException {
        navigateTo("/view/Checkout.fxml", "Check Out");
    }


    @FXML
    void ContinueShoppingOnClick(ActionEvent event) throws IOException {
        navigateTo("/view/SearchBook.fxml", "Search Books");
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
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cart Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}