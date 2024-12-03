package service.order;

import model.Book;
import model.Order;
import model.User;
import repository.order.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean save(Book book, Long id) {
        return orderRepository.save(book, id);
    }

    @Override
    public void addSoldBooksFromOrders(User user, List<Order> orders) {
        int totalBooks = 0;
        int totalValue = 0;

        try {
            // obtinem datele de vanzari
            double[] salesData = orderRepository.getTotalSalesAndBooksSold(user.getId());

            totalBooks = (int) salesData[0]; // total carti vandute
            totalValue = (int) salesData[1]; // total valori vanzari

            // adaugam informatiile obtinute la user
            user.setTotalSoldBooks(totalBooks);
            user.setTotalSalesValue(totalValue);

            if (user.getSoldBooks() == null) {
                user.setSoldBooks(new ArrayList<>());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUserEmployee(Long userId) {
        return orderRepository.isUserEmployee(userId);
    }
}
