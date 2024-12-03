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
            preparedStatement.setLong(6, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean isUserEmployee(Long userId) {
        String query = "SELECT role_id FROM user_role WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int roleId = resultSet.getInt("role_id");
                if (roleId == 2) {
                    return true; // userul este angajat
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // CALCULARE SUMA TOTALA CARTI + NR CARTI VANDUTE:
    // ->parcurg tebla de orders si in functie de id ul userului, de cate ori apare inseamna ca de atatea ori
    // o carte a fost vanduta(1 sale= -1 carte) deci nr de carti vandute se incrementeaza cu 1 la fiecare aparitie noua a userului
    // ->suma totala de carti este mai apoi calculata prin extragerea price-ului fiecarei carti vandute de catre user
    @Override
    public double[] getTotalSalesAndBooksSold(Long userId) {
        String query = "SELECT COUNT(o.userId) AS totalSoldBooks, SUM(o.price) AS totalSalesValue " +
                "FROM orders o " +
                "WHERE o.userId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);  // setam id utilizator
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalSoldBooks = rs.getInt("totalSoldBooks");
                    double totalSalesValue = rs.getDouble("totalSalesValue");
                    return new double[] { totalSoldBooks, totalSalesValue };
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new double[] { 0, 0.0 };
    }
}
