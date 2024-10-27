package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckoutController implements Controller, Initializable {

    @FXML
    private TextField CVC;

    @FXML
    private TextField CardNo;

    @FXML
    private TextField ExpDate;

    @FXML
    private Label billamount;

    @FXML
    private BorderPane borderPane;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        ShoppingCart cart = model.getCartForUser(model.getCurrentUser().getUsername());
        billamount.setText(String.format("$%.2f", cart.getTotal()));
    }

    @FXML
    void ProceedtoCheckoutOnClick(ActionEvent event) {
        // Get cart first
        ShoppingCart cart = model.getCartForUser(model.getCurrentUser().getUsername());

        // Check if cart is empty
        if (cart.getItems().isEmpty()) {
            showError("Your cart is empty. Please add items before checking out.");
            return;
        }

        // Proceed with card validation
        if (!validateCardDetails()) {
            return;
        }

        try {
            // Verify stock availability before proceeding
            for (CartItem cartItem : cart.getItems()) {
                if (!model.validateBookQuantity(cartItem.getBook(), cartItem.getQuantity())) {
                    showError("Sorry, " + cartItem.getBook().getTitle() + " is no longer available in the requested quantity.");
                    return;
                }
            }

            // Create order
            String orderNumber = generateOrderNumber();

            Order order = new Order(
                    orderNumber,
                    model.getCurrentUser().getUsername(),
                    LocalDateTime.now(),
                    cart.getTotal()
            );

            // Create order items and update book stock
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem(
                        orderNumber,
                        cartItem.getBook().getTitle(),
                        cartItem.getQuantity(),
                        cartItem.getBook().getPrice()
                );
                orderItems.add(orderItem);

                // Update book stock
                Book currentBook = model.getBookDao().getBookByTitle(cartItem.getBook().getTitle());
                int newStock = currentBook.getPhysicalCopies() - cartItem.getQuantity();
                model.getBookDao().updateBookStock(currentBook.getTitle(), newStock);
            }

            // Save order to database
            model.getOrderDao().createOrder(order);
            model.getOrderDao().addOrderItems(orderItems);

            // Clear the cart
            cart.clear();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your order has been placed successfully!\nOrder Number: " + orderNumber);
            alert.showAndWait();

            // Navigate to order history
            navigateTo("/view/ViewOrder.fxml", "Order History");

        } catch (Exception e) {
            showError("Error processing order: " + e.getMessage());
        }
    }

    private boolean validateCardDetails() {
        // Validate card number (16 digits)
        String cardNumber = CardNo.getText().replaceAll("\\s+", "");
        if (!cardNumber.matches("\\d{16}")) {
            showError("Card number must be 16 digits");
            return false;
        }

        // Check if it's Visa (starts with 4) or Mastercard (starts with 5)
        char firstDigit = cardNumber.charAt(0);
        if (firstDigit != '4' && firstDigit != '5') {
            showError("Only Visa and Mastercard are accepted");
            return false;
        }

        // Validate expiry date (future date)
        String expiry = ExpDate.getText().trim();
        if (!expiry.matches("\\d{2}/\\d{2}")) {
            showError("Invalid expiry date format (MM/YY)");
            return false;
        }

        try {
            String[] parts = expiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]); // Convert YY to 20YY

            // Check month is valid (1-12)
            if (month < 1 || month > 12) {
                showError("Invalid month");
                return false;
            }

            LocalDate expiryDate = LocalDate.of(year, month, 1);
            if (expiryDate.isBefore(LocalDate.now())) {
                showError("Card has expired");
                return false;
            }
        } catch (Exception e) {
            showError("Invalid expiry date format (MM/YY)");
            return false;
        }

        // Validate CVV (3 digits)
        if (!CVC.getText().matches("\\d{3}")) {
            showError("CVV must be 3 digits");
            return false;
        }

        return true;
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void ContinueCartOnClick(ActionEvent event) throws IOException {
        navigateTo("/view/ViewCart.fxml", "View Cart");
    }

    @FXML
    void viewOrderHistoryOnClick(ActionEvent event) throws IOException {
        navigateTo("/view/ViewOrder.fxml", "View Order");
    }


    @FXML
    void CheckOutOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/ViewCart.fxml", "View Cart");
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
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

}
