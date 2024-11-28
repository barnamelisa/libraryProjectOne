package model.builder;

import model.Book;

import java.time.LocalDate;

// Design Pattern Creational (se ocupa de creat ob: new Book())

// BookBuilder ne ajuta sa instantiem obiecte doar cu atributele dorite, si totodata nu avem nevoie de constructori cu multiple argumente
// ex: daca voiam un obiect de tip Book cu toti parametrii aveam 1 constructor, daca voiam un ob Book cu 2 parametrii aveam alt constructor etc
public class BookBuilder {
    private Book book;
    public BookBuilder(){
        book=new Book();
    }

    public BookBuilder setId(Long id){
        book.setId(id);
        return this;
    }

    public BookBuilder setTitle(String title){
        book.setTitle(title);
        return this;
    }

    public BookBuilder setAuthor(String author){
        book.setAuthor(author);
        return this;
    }

    public BookBuilder setPublishedDate(LocalDate publishedDate){
        book.setPublishedDate(publishedDate);
        return this;
    }
    public BookBuilder setStock(Integer stock){
        book.setStock(stock);
        return this;
    }
    public BookBuilder setPrice(Integer price){
        book.setPrice(price);
        return this;
    }

    public Book build(){
        return book;
    }
}
