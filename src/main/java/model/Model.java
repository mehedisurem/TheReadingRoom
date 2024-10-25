package model;

import dao.UserDao;
import dao.UserDaoImpl;
import dao.BookDao;
import dao.BookDaoImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private UserDao userDao;
    private BookDao bookDao;
    private User currentUser;
    private Map<String, ShoppingCart> userCarts;

    public Model() {
        userDao = new UserDaoImpl();
        bookDao = new BookDaoImpl();
        userCarts = new HashMap<>();
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
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}