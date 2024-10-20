package model;

public class Book {
    private String title;
    private String authors;
    private int physicalCopies;
    private double price;
    private int soldCopies;
    private String imgSrc;

    public Book(String title, String authors, int physicalCopies, double price, int soldCopies, String imgSrc) {
        this.title = title;
        this.authors = authors;
        this.physicalCopies = physicalCopies;
        this.price = price;
        this.soldCopies = soldCopies;
        this.imgSrc = imgSrc;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public int getPhysicalCopies() { return physicalCopies; }
    public void setPhysicalCopies(int physicalCopies) { this.physicalCopies = physicalCopies; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getSoldCopies() { return soldCopies; }
    public void setSoldCopies(int soldCopies) { this.soldCopies = soldCopies; }

    public String getImgSrc() { return imgSrc; }
    public void setImgSrc(String imgSrc) { this.imgSrc = imgSrc; }
}