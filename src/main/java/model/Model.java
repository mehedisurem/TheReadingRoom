package model;

import dao.UserDao;
import dao.UserDaoImpl;
import dao.BookDao;
import dao.BookDaoImpl;

import java.sql.SQLException;

public class Model {
    private UserDao userDao;
    private BookDao bookDao;
    private User currentUser;

    public Model() {
        userDao = new UserDaoImpl();
        bookDao = new BookDaoImpl();
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