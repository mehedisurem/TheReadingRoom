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

    // This method will take a userID and fetch all the order that this user has been placed
    @Override
    public List<Order> getOrdersByUser(String userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDERS_TABLE + " WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            System.out.println("Executing query for user: " + userId); // Debug print

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getString("order_number"),
                            rs.getString("user_id"),
                            rs.getTimestamp("order_date").toLocalDateTime(),
                            rs.getDouble("total_amount")
                    );
                    System.out.println("Found order: " + order.getOrderNumber()); // Debug print
                    orders.add(order);
                }
            }
        }
        return orders;
    }


    // This method will fetch order items from database and return to it's caller
    @Override
    public List<OrderItem> getOrderItems(String orderNumber) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM " + ORDER_ITEMS_TABLE + " WHERE order_number = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderNumber);
            System.out.println("Getting items for order: " + orderNumber); // Debug print

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getString("order_number"),
                            rs.getString("book_title"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    // Storing the order data into database
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

            int result = stmt.executeUpdate();
            System.out.println("Order created with number: " + order.getOrderNumber()); // Debug print

            if (result == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            return order.getOrderNumber();
        }
    }

    // Storing each book separately into the database
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
                System.out.println("Added item to order: " + item.getOrderNumber()); // Debug print
            }
        }
    }

}