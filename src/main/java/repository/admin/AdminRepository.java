package repository.admin;

import model.Book;
import model.Order;
import model.User;

import java.util.List;

public interface AdminRepository {
    boolean save(User user);
    List<User> findAll();
    List<Order> getOrdersForUser(Long userId);
    List<User> findAllEmployees();
    List<Book> findSoldBooksByUserId(Long userId);


}
