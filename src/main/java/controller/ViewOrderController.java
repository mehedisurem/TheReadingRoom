package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;
import model.Order;
import model.OrderItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class ViewOrderController implements Controller, Initializable {

    @FXML
    private TableView<Order> ViewordersTableView;
    @FXML
    private TableColumn<Order, String> orderNumberColumn;
    @FXML
    private TableColumn<Order, String> orderDateColumn;
    @FXML
    private TableColumn<Order, Double> totalAmountColumn;

    @FXML
    private TableView<OrderItem> orderDetailsTableView;
    @FXML
    private TableColumn<OrderItem, String> bookTitleColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, Double> priceColumn;

    @FXML
    private BorderPane borderPane;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        setupOrdersTable();
        setupDetailsTable();
        loadOrders();
    }

    private void setupOrdersTable() {
        // Setup main orders table columns
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));

        orderDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(cellData.getValue().getOrderDate().format(formatter));
        });

        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalAmountColumn.setCellFactory(tc -> new TableCell<Order, Double>() {
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

        // Add selection listener to show order details
        ViewordersTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        loadOrderDetails(newSelection.getOrderNumber());
                    }
                });
    }

    private void setupDetailsTable() {
        // Setup order details table columns
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        priceColumn.setCellFactory(tc -> new TableCell<OrderItem, Double>() {
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
    }

    private void loadOrders() {
        try {
            String userId = model.getCurrentUser().getUsername();
            System.out.println("Loading orders for user: " + userId);

            List<Order> orders = model.getOrderDao().getOrdersByUser(userId);
            System.out.println("Orders retrieved: " + (orders != null ? orders.size() : "null"));

            if (orders != null && !orders.isEmpty()) {
                for (Order order : orders) {
                    System.out.println("Order found: " +
                            "Number=" + order.getOrderNumber() +
                            ", Date=" + order.getOrderDate() +
                            ", Amount=" + order.getTotalAmount());
                }
                // Sort orders by date in reverse chronological order
                orders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
                ViewordersTableView.setItems(FXCollections.observableArrayList(orders));
            } else {
                System.out.println("No orders found for user");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print full stack trace
            showError("Error loading orders: " + e.getMessage());
        }
    }

    // Load order Details Based on Order Number
    private void loadOrderDetails(String orderNumber) {
        try {
            List<OrderItem> items = model.getOrderDao().getOrderItems(orderNumber);
            orderDetailsTableView.setItems(FXCollections.observableArrayList(items));
        } catch (Exception e) {
            showError("Error loading order details: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    void CheckOutOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/ViewCart.fxml", "View Cart");
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
        // Refresh current scene or handle navigation
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