package dao;

import model.Order;
import model.OrderItem;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static final String ORDERS_TABLE = "orders";
    private static final String ORDER_ITEMS_TABLE = "order_items";

    @Override
    public void setup() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create orders table
            String ordersSQL = "CREATE TABLE IF NOT EXISTS " + ORDERS_TABLE + " (" +
                    "order_number TEXT PRIMARY KEY, " +
                    "user_id TEXT NOT NULL, " +
                    "order_date TIMESTAMP NOT NULL, " +
                    "total_amount REAL NOT NULL)";
            stmt.executeUpdate(ordersSQL);

            // Create order_items table
            String itemsSQL = "CREATE TABLE IF NOT EXISTS " + ORDER_ITEMS_TABLE + " (" +
                    "order_number TEXT NOT NULL, " +
                    "book_title TEXT NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "PRIMARY KEY (order_number, book_title), " +
                    "FOREIGN KEY (order_number) REFERENCES " + ORDERS_TABLE + "(order_number))";
            stmt.executeUpdate(itemsSQL);
        }
    }

    @Override
    public String createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO " + ORDERS_TABLE +
                " (order_number, user_id, order_date, total_amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderNumber());
            stmt.setString(2, order.getUserId());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            stmt.setDouble(4, order.getTotalAmount());
            stmt.executeUpdate();
            return order.getOrderNumber();
        }
    }

    @Override
    public void addOrderItems(List<OrderItem> items) throws SQLException {
        String sql = "INSERT INTO " + ORDER_ITEMS_TABLE +
                " (order_number, book_title, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (OrderItem item : items) {
                stmt.setString(1, item.getOrderNumber());
                stmt.setString(2, item.getBookTitle());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getPrice());
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Order> getOrdersByUser(String userId) throws SQLException {
        return List.of();
    }

    @Override
    public List<OrderItem> getOrderItems(String orderNumber) throws SQLException {
        return List.of();
    }


}