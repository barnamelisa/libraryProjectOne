package service.order;

import model.Book;
import model.User;

public interface OrderService {
    boolean save(Book book, Long id);
}
