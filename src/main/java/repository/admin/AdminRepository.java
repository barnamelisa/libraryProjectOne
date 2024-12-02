package repository.admin;

import model.Book;
import model.User;

import java.util.List;

public interface AdminRepository {
    boolean save(User user);
    List<User> findAll();
}
