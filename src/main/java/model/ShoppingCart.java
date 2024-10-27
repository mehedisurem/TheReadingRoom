package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCart {
    private List<CartItem> items;

    // Initialize a new shopping cart with an empty list of items
    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    // Returns the list of items currently in the cart
    public List<CartItem> getItems() {
        return items;
    }

    // Adds a book to the cart or updates quantity if book already exists in cart
    public void addItem(Book book, int quantity) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getBook().getTitle().equals(book.getTitle()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            items.add(new CartItem(book, quantity));
        }
    }

    // Removes a specific book from the cart based on its title
    public void removeItem(String bookTitle) {
        items.removeIf(item -> item.getBook().getTitle().equals(bookTitle));
    }

    // Updates the quantity of a specific book in the cart to a new value
    public void updateQuantity(String bookTitle, int newQuantity) {
        items.stream()
                .filter(item -> item.getBook().getTitle().equals(bookTitle))
                .findFirst()
                .ifPresent(item -> item.setQuantity(newQuantity));
    }

    // Removes all items from the cart
    public void clear() {
        items.clear();
    }

    // Calculates and returns the total price of all items in the cart
    public double getTotal() {
        return items.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }
}