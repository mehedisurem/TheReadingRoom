package controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import model.Order;
import model.OrderItem;
import javafx.collections.FXCollections;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportFileController implements Controller, Initializable {

    @FXML
    private TableColumn<OrderItem, String> BookTitleColumn;
    @FXML
    private TableColumn<OrderItem, Double> PriceColumn;
    @FXML
    private TableColumn<OrderItem, Integer> QuantityColumn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField fileNameTextField;
    @FXML
    private TableColumn<OrderItem, String> orderDateColumn;
    @FXML
    private TableView<OrderItem> ordersTableView;
    @FXML
    private CheckBox selectAllCheckBox;

    private Model model;
    private String selectedFilePath;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        setupTable();
        loadOrders();
        setupSelectAllCheckBox();
    }

    private void setupTable() {
        // Enable multiple selection
        ordersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Setup Order Date Column
        orderDateColumn.setCellValueFactory(cellData -> {
            Order order = cellData.getValue().getOrder(); // Assuming you have a reference to Order in OrderItem
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(order.getOrderDate().format(formatter));
        });

        // Setup Book Title Column
        BookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        // Setup Quantity Column
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Setup Price Column with currency formatting
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        PriceColumn.setCellFactory(tc -> new TableCell<OrderItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price * getTableView().getItems().get(getIndex()).getQuantity()));
                }
            }
        });

        // Set column sizes
        orderDateColumn.setPrefWidth(150);
        BookTitleColumn.setPrefWidth(200);
        QuantityColumn.setPrefWidth(100);
        PriceColumn.setPrefWidth(100);
    }

    private void setupSelectAllCheckBox() {
        selectAllCheckBox.setOnAction(event -> {
            if (selectAllCheckBox.isSelected()) {
                ordersTableView.getSelectionModel().selectAll();
            } else {
                ordersTableView.getSelectionModel().clearSelection();
            }
        });

        // Update checkbox state based on selection
        ordersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int selectedCount = ordersTableView.getSelectionModel().getSelectedItems().size();
            int totalCount = ordersTableView.getItems().size();
            selectAllCheckBox.setSelected(selectedCount == totalCount && totalCount > 0);
        });
    }

    private void loadOrders() {
        try {
            String userId = model.getCurrentUser().getUsername();
            List<Order> orders = model.getOrderDao().getOrdersByUser(userId);

            // Create a list to hold all order items
            ObservableList<OrderItem> allOrderItems = FXCollections.observableArrayList();

            // Get items for each order
            for (Order order : orders) {
                List<OrderItem> items = model.getOrderDao().getOrderItems(order.getOrderNumber());
                for (OrderItem item : items) {

                    item.setOrder(order);
                    allOrderItems.add(item);
                }
            }

            ordersTableView.setItems(allOrderItems);
        } catch (Exception e) {
            showError("Error loading orders: " + e.getMessage());
        }
    }

    @FXML
    void chooseFileLocation(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        String fileName = fileNameTextField.getText().trim();
        if (fileName.isEmpty()) {
            fileName = "orders_export.csv";
        }
        if (!fileName.endsWith(".csv")) {
            fileName += ".csv";
        }
        fileChooser.setInitialFileName(fileName);

        File file = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
        if (file != null) {
            selectedFilePath = file.getAbsolutePath();
            fileNameTextField.setText(file.getName());
        }
    }

    @FXML
    void exportSelectedOrders(ActionEvent event) {
        List<OrderItem> selectedItems = ordersTableView.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            showError("Please select orders to export");
            return;
        }

        if (selectedFilePath == null || selectedFilePath.isEmpty()) {
            showError("Please choose a file location");
            return;
        }

        try {
            exportToCSV(selectedItems, selectedFilePath);
            showSuccess("Orders exported successfully to: " + selectedFilePath);
        } catch (Exception e) {
            showError("Error exporting orders: " + e.getMessage());
        }
    }

    private void exportToCSV(List<OrderItem> orderItems, String filePath) throws IOException {
        try (FileWriter fw = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Write CSV header
            bw.write("Order Date,Order Number,Book Title,Quantity,Price Per Book,Total Amount\n");

            // Write order details
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (OrderItem item : orderItems) {
                Order order = item.getOrder();
                String orderDate = order.getOrderDate().format(formatter);
                double totalAmount = item.getPrice() * item.getQuantity();

                // Write the order item as a CSV row
                bw.write(String.format("%s,%s,\"%s\",%d,%.2f,%.2f\n",
                        orderDate,
                        order.getOrderNumber(),
                        item.getBookTitle().replace("\"", "\"\""), // Escape quotes in book titles
                        item.getQuantity(),
                        item.getPrice(),
                        totalAmount
                ));
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
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
        // Refresh current scene
        loadOrders();
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