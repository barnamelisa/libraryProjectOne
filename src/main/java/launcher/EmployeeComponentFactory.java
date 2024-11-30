package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.Order;
import model.User;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import view.BookView;
import view.model.BookDTO;
import java.lang.Boolean;
import java.sql.Connection;
import java.util.List;

/** clasa Singleton care va contine clasele de care noi avem nevoie di dependintele pe care le folosim */
public class EmployeeComponentFactory {
    private final BookView bookView; // partea de interfata
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    private static volatile EmployeeComponentFactory instance; // instanta a clasei pt ca este Singleton

    /** clasa va fi locul unde vom consytrui arborele de dependency injection si practic vom controla app folosindu-ne de dependinte
        TO DO: adaugat tot ce e nevoie in aceasta clasa ca sa fie thread-safe si sa fie Singleton cu adevarat(lazi load in cazul nostru)
     */
    public static EmployeeComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage, Long userId){
        if (instance == null) {
            synchronized (EmployeeComponentFactory.class){
                if (instance == null){
                    instance=new EmployeeComponentFactory(componentsForTest, primaryStage, userId);
                }
            }
        }
        return instance;
    }

    // ca sa avem clasa Singleton avem nevoie de constructor privat
    private EmployeeComponentFactory(Boolean componentsForTest, Stage stage, Long userId){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
        this.bookService = new BookServiceImpl(bookRepository);

        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(this.bookService.findAll());

        this.orderRepository = new OrderRepositoryMySQL(connection);
        this.orderService = new OrderServiceImpl(orderRepository);

        this.bookView = new BookView(stage, bookDTOs);
        this.bookController = new BookController(bookView, bookService, orderService, userId); // aici tr user id
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
