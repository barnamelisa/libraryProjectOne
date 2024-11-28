package service.order;

import model.Book;
import model.Order;
import model.User;
import repository.order.OrderRepository;

public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean save(Book book, Long id) {
        return orderRepository.save(book, id);
    }
}
