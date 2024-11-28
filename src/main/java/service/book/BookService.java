package service.book;

import model.Book;

import java.util.List;

public interface BookService {
    // aici are loc partea de logica/procesare
    List<Book> findAll();
    Book findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    int getAgeOfBook(Long id);
    boolean saleBook(Book book);
}
