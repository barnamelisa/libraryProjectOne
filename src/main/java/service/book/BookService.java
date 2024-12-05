package service.book;

import model.Book;

import java.util.List;

// aici are loc partea de logica/procesare
public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    int getAgeOfBook(Long id);
    boolean saleBook(Book book);
}
