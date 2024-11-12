package model;

import java.time.LocalDate;

public class Book {
    private Long id; // folosim Long ca sa rpimeasca val default NULL, daca foloseam long era 0
    private String title;
    private String author;
    private LocalDate publishedDate;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString(){
        return "Book: Id: " + id + " Title: " + title + " Author: " +  author + " Published Date: " + publishedDate;
    }
}
