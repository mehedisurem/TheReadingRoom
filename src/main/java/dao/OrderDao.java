package dao;

import model.Order;
import model.OrderItem;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    void setup() throws SQLException;
    String createOrder(Order order) throws SQLException;
    void addOrderItems(List<OrderItem> items) throws SQLException;
    List<Order> getOrdersByUser(String userId) throws SQLException;
    List<OrderItem> getOrderItems(String orderNumber) throws SQLException;
}