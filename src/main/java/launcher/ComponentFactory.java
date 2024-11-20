package launcher;

import controller.BookController;

import database.DatabaseConnectionFactory;
import java.sql.Connection;
import java.util.*;

import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import view.BookView;
import view.model.BookDTO;

/** clasa Singleton care va contine clasele de care noi avem nevoie di dependintele pe care le folosim */
public class ComponentFactory {
    private final BookView bookView; // partea de interfata
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static volatile ComponentFactory instance; // instanta a clasei pt ca este Singleton

    /** clasa va fi locul unde vom consytrui arborele de dependency injection si practic vom controla app folosindu-ne de dependinte
        TO DO: adaugat tot ce e nevoie in aceasta clasa ca sa fie thread-safe si sa fie Singleton cu adevarat(lazi load in cazul nostru)
     */
    public static ComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage){
        if (instance == null) {
            synchronized (ComponentFactory.class){
                if (instance == null){
                    instance=new ComponentFactory(componentsForTest,primaryStage);
                }
            }
        }
        return instance;
    }

    // ca sa avem clasa Singleton avem nevoie de constructor privat
    private ComponentFactory(Boolean componentsForTest, Stage primaryStage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, bookDTOs);
        this.bookController = new BookController(bookView, bookService);
    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}
