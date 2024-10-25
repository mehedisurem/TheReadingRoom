package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCart {
    private List<CartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

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

    public void removeItem(String bookTitle) {
        items.removeIf(item -> item.getBook().getTitle().equals(bookTitle));
    }

    public void updateQuantity(String bookTitle, int newQuantity) {
        items.stream()
                .filter(item -> item.getBook().getTitle().equals(bookTitle))
                .findFirst()
                .ifPresent(item -> item.setQuantity(newQuantity));
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }
}