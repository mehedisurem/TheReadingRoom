package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import model.ShoppingCart;

import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchBookController implements Controller, Initializable {

    @FXML
    private Pane Card1;
    @FXML
    private ScrollPane ScrollPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label card1Author;
    @FXML
    private Label card1Copies;
    @FXML
    private Label card1Sold;
    @FXML
    private Label card1Title;
    @FXML
    private ImageView card1img;
    @FXML
    private Label card1price;
    @FXML
    private TextField selectedbook_Quantity;

    private Model model;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void initData() {
        loadBooks();
    }

    private void loadBooks() {
        try {
            List<Book> books = model.getBookDao().getAllBooks();
            VBox container = new VBox(10); // 10px spacing between cards

            for (Book book : books) {
                Pane bookCard = createBookCard(book);
                container.getChildren().add(bookCard);
            }

            ScrollPane.setContent(container);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading books");
        }
    }

    // creates book class
    private Pane createBookCard(Book book) {
        Pane card = new Pane();
        card.setPrefSize(259, 260);
        card.getStyleClass().add("bookCard");

        // Create all components
        ImageView bookImage = new ImageView();
        bookImage.setFitHeight(100);
        bookImage.setFitWidth(100);
        bookImage.setLayoutX(81);
        bookImage.setLayoutY(0);
        bookImage.setPreserveRatio(true);

        // Load the image using the provided format
        String imagePath = book.getImgSrc();
        if (imagePath.startsWith("@")) {
            imagePath = imagePath.substring(1);
        }

        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            bookImage.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            File file = new File(imagePath);
            if (file.exists()) {
                bookImage.setImage(new Image(file.toURI().toString()));
            } else {
                System.out.println("Image file not found: " + book.getImgSrc());
                // Optionally, set a default image
                // bookImage.setImage(new Image("/path/to/default/image.png"));
            }
        }

        // Create and set up labels
        Label titleLabel = new Label(book.getTitle());
        titleLabel.setLayoutX(56);
        titleLabel.setLayoutY(102);
        titleLabel.setPrefWidth(147);
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Label authorLabel = new Label(book.getAuthors());
        authorLabel.setLayoutX(47);
        authorLabel.setLayoutY(119);
        authorLabel.setPrefWidth(147);
        authorLabel.setAlignment(javafx.geometry.Pos.CENTER);

        Label copiesTextLabel = new Label("Copies:");
        copiesTextLabel.setLayoutX(79);
        copiesTextLabel.setLayoutY(137);

        Label copiesLabel = new Label(String.valueOf(book.getPhysicalCopies()));
        copiesLabel.setLayoutX(147);
        copiesLabel.setLayoutY(137);

        Label soldTextLabel = new Label("Sold:");
        soldTextLabel.setLayoutX(79);
        soldTextLabel.setLayoutY(154);

        Label soldLabel = new Label(String.valueOf(book.getSoldCopies()));
        soldLabel.setLayoutX(146);
        soldLabel.setLayoutY(154);

        Label priceTextLabel = new Label("Price:");
        priceTextLabel.setLayoutX(79);
        priceTextLabel.setLayoutY(171);

        Label priceLabel = new Label(String.format("$%.2f", book.getPrice()));
        priceLabel.setLayoutX(146);
        priceLabel.setLayoutY(171);

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.setLayoutX(75);
        quantityLabel.setLayoutY(194);

        TextField quantityField = new TextField();
        quantityField.setLayoutX(141);
        quantityField.setLayoutY(190);
        quantityField.setPrefSize(53, 16);

        Button addToCartButton = new Button("Add To Cart");
        addToCartButton.setLayoutX(104);
        addToCartButton.setLayoutY(227);
        addToCartButton.setOnAction(e -> handleAddToCart(book, quantityField));

        // Add all components to the card
        card.getChildren().addAll(
                bookImage,
                titleLabel,
                authorLabel,
                copiesTextLabel,
                copiesLabel,
                soldTextLabel,
                soldLabel,
                priceTextLabel,
                priceLabel,
                quantityLabel,
                quantityField,
                addToCartButton
        );

        return card;
    }

    private void handleAddToCart(Book book, TextField quantityField) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());

            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Please enter a valid quantity");
                return;
            }

            // Validate stock availability
            if (model.validateBookQuantity(book, quantity)) {
                ShoppingCart cart = model.getCartForUser(model.getCurrentUser().getUsername());
                cart.addItem(book, quantity);
                quantityField.clear();
                showAlert(Alert.AlertType.INFORMATION, "Book added to cart successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Insufficient stock available");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Please enter a valid quantity");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error checking stock availability");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.INFORMATION ? "Success" : "Error");
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
        // Refresh current scene or handle navigation
    }

    @FXML
    void viewOrderOnClick(MouseEvent event) throws IOException {
        navigateTo("/view/ViewOrder.fxml", "View Order");
    }

    @FXML
    void viewCartOnClick(ActionEvent event) throws IOException {
        navigateTo("/view/ViewCart.fxml", "View Cart");
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