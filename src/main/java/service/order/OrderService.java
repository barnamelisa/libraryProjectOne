package service.order;

import model.Book;
import model.Order;
import model.User;

import java.util.List;

public interface OrderService {
    boolean save(Book book, Long id);
    void addSoldBooksFromOrders(User user, List<Order> orders);
    boolean isUserEmployee(Long userId);
}
