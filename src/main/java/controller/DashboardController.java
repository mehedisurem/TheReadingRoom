package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Model;
import model.User;
import model.Book;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

public class DashboardController implements Controller, Initializable {
    @FXML
    private Pane Card1, Card2, Card3, Card4, Card5;

    @FXML
    private Label card1Author, card1Copies, card1Sold, card1Title, card1price;
    @FXML
    private Label card2Author, card2Copies, card2Sold, card2Title, card2price;
    @FXML
    private Label card3Author, card3Copies,card3Sold, card3Title, card3price;
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
        System.out.println("Setting model in DashboardController");
        this.model = model;
        System.out.println("Model set. Current user in model: " + (model.getCurrentUser() != null ? model.getCurrentUser().getUsername() : "null"));
    }

    @Override
    public void initData() {
        System.out.println("Initializing data in DashboardController");
        User currentUser = model.getCurrentUser();
        System.out.println("Current user from model: " + currentUser);
        if (currentUser != null) {
            System.out.println("User details - Username: " + currentUser.getUsername()
                    + ", FirstName: " + currentUser.getFname()
                    + ", LastName: " + currentUser.getLname());
            userWelcomeMsg.setText("Welcome, " + currentUser.getFname() + " " + currentUser.getLname() + "!");
        } else {
            System.out.println("Current user is null in DashboardController");
            userWelcomeMsg.setText("Welcome, Guest!");
        }
        try {
            List<Book> topBooks = model.getBookDao().getTopBooks(5);
            displayTopBooks(topBooks);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the error, maybe show an alert to the user
        }
    }

    private void displayTopBooks(List<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            switch (i) {
                case 0:
                    setCardData(card1Title, card1Author, card1Copies, card1Sold, card1price, card1img, book);
                    break;
                case 1:
                    setCardData(card2Title, card2Author, card2Copies, card2Sold, card2price, card2img, book);
                    break;
                case 2:
                    setCardData(card3Title, card3Author, card3Copies, card3Sold, card3price, card3img, book);
                    break;
                case 3:
                    setCardData(card4Title, card4Author, card4Copies, card4Sold, card4price, card4img, book);
                    break;
                case 4:
                    setCardData(card5Title, card5Author, card5Copies, card5Sold, card5price, card5img, book);
                    break;
            }
        }
    }

    private void setCardData(Label title, Label author, Label copies, Label sold, Label price, ImageView img, Book book) {
        title.setText(book.getTitle());
        author.setText(book.getAuthors());
        if (copies != null) {
            copies.setText(String.valueOf(book.getPhysicalCopies()));
        }
        sold.setText(String.valueOf(book.getSoldCopies()));
        price.setText(String.format("$%.2f", book.getPrice()));

        // Load the image
        String imagePath = book.getImgSrc();
        if (imagePath.startsWith("@")) {
            imagePath = imagePath.substring(1); // Remove the @ from the start
        }

        // Try loading as a resource first
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            img.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            // If not found as a resource, try as a file path
            File file = new File(imagePath);
            if (file.exists()) {
                img.setImage(new Image(file.toURI().toString()));
            } else {
                // Set a default image or handle the error
                System.out.println("Image file not found: " + book.getImgSrc());
                // Optionally, set a default image
                // img.setImage(new Image("/path/to/default/image.png"));
            }
        }
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
        Stage stage = (Stage) userWelcomeMsg.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}