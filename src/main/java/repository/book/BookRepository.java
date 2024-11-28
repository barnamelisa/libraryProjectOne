package repository.book;

import model.Book;
import model.User;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    // se ocupa doar cu citirea din bd
    // aici ar trebui eliminat save si delete si saleBook
    List<Book> findAll();
    Optional<Book> findById(Long id); // Optional indica faptul ca am putea avea o carte sau ar putea lipsi o carte (ne ajuta sa scapam de null)
    boolean save(Book book);
    boolean delete(Book book);
    void removeAll(); // face flash-sterge tot ce este in bd
    void saleBook(Book book); // metoda cu care vom crea comenzi, respectiv vom vinde o carte // o facem void deoarece metoda are rolul de a actualiza bd si interfata
}
