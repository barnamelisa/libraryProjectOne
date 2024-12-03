package repository.order;

import model.Book;
import model.User;

public interface OrderRepository {
    boolean save(Book book, Long id); // salvam o comanda in tabla orders
    boolean isUserEmployee(Long userId);
    double[] getTotalSalesAndBooksSold(Long userId);

}
