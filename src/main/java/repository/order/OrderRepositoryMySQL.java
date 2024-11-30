package repository.order;

import model.Book;
import model.Order;
import model.User;

import java.sql.*;

public class OrderRepositoryMySQL implements OrderRepository{
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Book book, Long id) {
        String newSql = "INSERT INTO orders (orderDate, author, title, price, stock, userId) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(newSql)){
            preparedStatement.setTimestamp(1,  Timestamp.valueOf(java.time.LocalDateTime.now()));
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getTitle());
            preparedStatement.setInt(4, book.getPrice());
            preparedStatement.setInt(5, book.getStock());
            preparedStatement.setLong(6, 1L);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
