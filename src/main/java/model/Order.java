package model;

import java.time.LocalDate;

public class Order {
    private Long id; // id-ul comenzii
    private LocalDate orderDate;
    private String bookAuthor; // aici putem inlocui cu id_book
    private String bookTitle;
    private Integer bookPrice;
    private Integer bookStock;
    private Long userId; // el vinde cartea

    public Long getId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Integer getBookPrice() {
        return bookPrice;
    }

    public Integer getBookStock() {
        return bookStock;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setBookPrice(Integer bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setBookStock(Integer bookStock) {
        this.bookStock = bookStock;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
