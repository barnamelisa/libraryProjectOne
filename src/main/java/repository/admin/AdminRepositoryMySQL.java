package repository.admin;

import model.Book;
import model.Order;
import model.User;
import model.builder.UserBuilder;
import service.user.AuthenticationServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepositoryMySQL implements AdminRepository {
    private final Connection connection;

    public AdminRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(User user) {
        try {
            String hashedPassword = AuthenticationServiceImpl.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            addUserRole(userId, 2);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addUserRole(long userId, int roleId) {
        String sql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, roleId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user;";

        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public List<Order> getOrdersForUser(Long userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders " +
                "WHERE userId = ? AND MONTH(orderDate) = MONTH(CURRENT_DATE()) " +
                "AND YEAR(orderDate) = YEAR(CURRENT_DATE());";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getLong("id"));
                order.setOrderDate(resultSet.getTimestamp("orderDate"));
                order.setBookAuthor(resultSet.getString("author"));
                order.setBookTitle(resultSet.getString("title"));
                order.setBookPrice(resultSet.getInt("price"));
                order.setBookStock(resultSet.getInt("stock"));
                order.setUserId(resultSet.getLong("userId"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<User> findAllEmployees() {
        String sql = "SELECT u.* FROM user u " +
                "JOIN user_role ur ON u.id = ur.user_id " +
                "WHERE ur.role_id = 2;";

        List<User> employees = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                employees.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<Book> findSoldBooksByUserId(Long userId) {
        String query = "SELECT b.id, b.title, b.author, b.stock, b.price " +
                "FROM book b " +
                "JOIN orders o ON b.id = o.author " +
                "WHERE o.userId = ?";
        List<Book> soldBooks = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setStock(resultSet.getInt("stock"));
                book.setPrice(resultSet.getInt("price"));
                soldBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soldBooks;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException{
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .build();
    }
}
