import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.BookRepository;
import repository.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {
    private static BookRepository bookRepository;
    private static Connection connection;

    @BeforeAll
    public static void setup(){
        // true pentru test_schema(test_library)
        JDBConnectionWrapper connectionWrapper = DatabaseConnectionFactory.getConnectionWrapper(true);
        connection = connectionWrapper.getConnection();
        bookRepository = new BookRepositoryMySQL(connection);
    }

    @Test
    public void findAll(){
        bookRepository.removeAll(); // stergem toate inregistrarile din bd

        Book bookIon = new BookBuilder()
                .setAuthor("ionicuta")
                .setTitle("ion")
                .setPublishedDate(LocalDate.of(1900, 10, 2))
                .build();


        // salvam cartea in baza de date si adaugam un mesaj in caz de eroare
        assertTrue(bookRepository.save(bookIon), "Failed to save bookIon");

        List<Book> books = bookRepository.findAll();
        assertEquals(1,books.size());
    }

    @Test
    public void findById(){
        bookRepository.removeAll(); // stergem toate inregistrarile din bd

        Book bookMaria = new BookBuilder()
                .setAuthor("maricuta")
                .setTitle("maria")
                .setPublishedDate(LocalDate.of(1900, 10, 2))
                .build();

        // salvam cartea in baza de date si adaugam un mesaj in caz de eroare
        bookRepository.save(bookMaria);

        Optional<Book> book = bookRepository.findById(1L); // L=Long
        assertTrue(book.isPresent()); // isPresent() verifica daca un obiect de tip Optional contine sau nu o valoare
    }

    @Test
    public void save(){
        Book book = new BookBuilder()
                .setAuthor("ion")
                .setTitle("ionicuta")
                .setPublishedDate(LocalDate.of(1900, 10, 2))
                .build();

        boolean result = bookRepository.save(book);

        assertTrue(result);
    }

    @Test
    public void delete(){
        Book book = new BookBuilder()
                .setAuthor("ion")
                .setTitle("ionicuta")
                .setPublishedDate(LocalDate.of(1900, 10, 2))
                .build();

        boolean result = bookRepository.delete(book);
        assertTrue(result);
    }

}
