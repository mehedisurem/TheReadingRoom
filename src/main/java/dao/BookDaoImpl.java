package dao;

import model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    private static final String TABLE_NAME = "books";
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 1000;

    @Override
    public void setup() throws SQLException {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try (Connection conn = Database.getConnection();
                 Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        "title TEXT PRIMARY KEY, " +
                        "authors TEXT, " +
                        "physical_copies INTEGER, " +
                        "price REAL, " +
                        "sold_copies INTEGER, " +
                        "img_src TEXT)";
                stmt.executeUpdate(sql);

                // Check if the table is empty
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + TABLE_NAME);
                if (rs.next() && rs.getInt(1) == 0) {
                    insertInitialData();
                }
                return; // If successful, exit the method
            } catch (SQLException e) {
                if (attempt == MAX_RETRIES) {
                    throw e; // If all retries failed, throw the exception
                }
                System.out.println("Database locked, retrying in " + RETRY_DELAY_MS + "ms...");
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void insertInitialData() throws SQLException {
        String[] books = {
                "Absolute Java|Savitch|10|50.0|142|@../Image/book1.png",
                "JAVA: How to Program|Deitel and Deitel|100|70.0|475|@../Image/book2.png",
                "Computing Concepts with JAVA 8 Essentials|Horstman|500|89.0|60|@../Image/book3.png",
                "Java Software Solutions|Lewis and Loftus|500|99.0|12|@../Image/book4.png",
                "Java Program Design|Cohoon and Davidson|2|29.0|86|@../Image/book5.png",
                "Clean Code|Robert Martin|100|45.0|300|@../Image/book6.png",
                "Gray Hat C#|Brandon Perry|300|68.0|178|@../Image/book7.png",
                "Python Basics|David Amos|1000|49.0|79|@../Image/book8.png",
                "Bayesian Statistics The Fun Way|Will Kurt|600|42.0|155|@../Image/book9.png"
        };

        String sql = "INSERT INTO " + TABLE_NAME + " (title, authors, physical_copies, price, sold_copies, img_src) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String book : books) {
                String[] data = book.split("\\|");
                pstmt.setString(1, data[0]);
                pstmt.setString(2, data[1]);
                pstmt.setInt(3, Integer.parseInt(data[2]));
                pstmt.setDouble(4, Double.parseDouble(data[3]));
                pstmt.setInt(5, Integer.parseInt(data[4]));
                pstmt.setString(6, data[5]);
                pstmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("title"),
                        rs.getString("authors"),
                        rs.getInt("physical_copies"),
                        rs.getDouble("price"),
                        rs.getInt("sold_copies"),
                        rs.getString("img_src")
                ));
            }
        }
        return books;
    }

    @Override
    public List<Book> getTopBooks(int limit) throws SQLException {
        List<Book> topBooks = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY sold_copies DESC LIMIT ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    topBooks.add(new Book(
                            rs.getString("title"),
                            rs.getString("authors"),
                            rs.getInt("physical_copies"),
                            rs.getDouble("price"),
                            rs.getInt("sold_copies"),
                            rs.getString("img_src")
                    ));
                }
            }
        }
        return topBooks;
    }


}