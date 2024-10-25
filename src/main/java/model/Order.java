package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private String orderNumber;
    private String userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private List<OrderItem> items;

    public Order(String orderNumber, String userId, LocalDateTime orderDate, double totalAmount) {
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>();
    }

    // Getters and setters
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}