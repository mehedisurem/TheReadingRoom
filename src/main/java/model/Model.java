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

    public Model() {
        userDao = new UserDaoImpl();
        bookDao = new BookDaoImpl();
        userCarts = new HashMap<>();
        orderDao = new OrderDaoImpl();
    }

    public ShoppingCart getCartForUser(String username) {
        return userCarts.computeIfAbsent(username, k -> new ShoppingCart());
    }

    public boolean validateBookQuantity(Book book, int requestedQuantity) throws SQLException {
        Book currentBook = bookDao.getBookByTitle(book.getTitle());
        return currentBook.getPhysicalCopies() >= requestedQuantity;
    }

    public void setup() throws SQLException {
        userDao.setup();
        bookDao.setup();
        orderDao.setup();
    }

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