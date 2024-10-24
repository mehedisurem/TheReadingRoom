package dao;

import model.Book;
import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    void setup() throws SQLException;
    List<Book> getAllBooks() throws SQLException;
    List<Book> getTopBooks(int limit) throws SQLException;
}