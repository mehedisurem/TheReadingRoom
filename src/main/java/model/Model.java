package model;

import dao.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private UserDao userDao;
    private BookDao bookDao;
    private User currentUser;
    private Map<String, ShoppingCart> userCarts;
    private OrderDao orderDao;

    // Constructor
    public Model() {
        userDao = new UserDaoImpl();
        bookDao = new BookDaoImpl();
        userCarts = new HashMap<>();
        orderDao = new OrderDaoImpl();
    }

    //Retrieves or creates a shopping cart for a specific user.
    public ShoppingCart getCartForUser(String username) {
        return userCarts.computeIfAbsent(username, k -> new ShoppingCart());
    }

    //Validates if there is sufficient stock available for a requested quantity of a book.
    public boolean validateBookQuantity(Book book, int requestedQuantity) throws SQLException {
        Book currentBook = bookDao.getBookByTitle(book.getTitle());
        return currentBook.getPhysicalCopies() >= requestedQuantity;
    }

    public void setup() throws SQLException {
        userDao.setup();
        bookDao.setup();
        orderDao.setup();
    }

    // Getters and setters
    public UserDao getUserDao() {
        return userDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}