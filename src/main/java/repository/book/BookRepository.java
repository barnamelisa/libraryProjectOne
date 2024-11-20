package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    // se ocupa doar cu citirea din bd
    List<Book> findAll();
    Optional<Book> findById(Long id); // Optional indica faptul ca am putea avea o carte sau ar putea lipsi o carte (ne ajuta sa scapam de null)
    boolean save(Book book);
    boolean delete(Book book);
    void removeAll(); // face flash-sterge tot ce este in bd
}
