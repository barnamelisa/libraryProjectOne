package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import service.book.BookService;
import service.book.BookServiceImpl;
import view.BookView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

/** clasa Singleton care va contine clasele de care noi avem nevoie di dependintele pe care le folosim */
public class EmployeeComponentFactory {
    private final BookView bookView; // partea de interfata
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static volatile EmployeeComponentFactory instance; // instanta a clasei pt ca este Singleton

    /** clasa va fi locul unde vom consytrui arborele de dependency injection si practic vom controla app folosindu-ne de dependinte
        TO DO: adaugat tot ce e nevoie in aceasta clasa ca sa fie thread-safe si sa fie Singleton cu adevarat(lazi load in cazul nostru)
     */
    public static EmployeeComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage){
        if (instance == null) {
            synchronized (EmployeeComponentFactory.class){
                if (instance == null){
                    instance=new EmployeeComponentFactory(componentsForTest,primaryStage);
                }
            }
        }
        return instance;
    }

    // ca sa avem clasa Singleton avem nevoie de constructor privat
    private EmployeeComponentFactory(Boolean componentsForTest, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(this.bookService.findAll());
        this.bookView = new BookView(stage, bookDTOs);
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
