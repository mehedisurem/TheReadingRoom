package model;

public class CartItem {
    private Book book;
    private int quantity;

    // Constructor
    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    // Getters and setters
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotal() {
        return book.getPrice() * quantity;
    }
}