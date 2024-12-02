package repository.admin;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import repository.security.RightsRolesRepository;
import service.user.AuthenticationServiceMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepositoryMySQL implements AdminRepository{
    private final Connection connection;

    public AdminRepositoryMySQL(Connection connection) {
        this.connection=connection;
    }

    @Override
    public boolean save(User user) {
        try {
            String hashedPassword = AuthenticationServiceMySQL.hashPassword(user.getPassword());
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

            addUserRole(userId,2);

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

        List<User> users =  new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException{
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .build();
    }
}
