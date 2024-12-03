package repository.user;

import model.User;
import model.validation.Notification;

import java.util.*;

// la aceasta interfata vom avea acces din service
public interface UserRepository {
    List<User> findAll(); // va returna o lista de useri
    Notification<User> findByUsernameAndPassword(String username, String password);
    boolean save(User user);
    void removeAll();

    // verificam daca user-ul are adresa de email introdusa in bd sau daca un user vrea sa se inregistreze cu un numer care este deja luat
    boolean existsByUsername(String username); // username va fii si email-ul
    User findUserById(Long userId);


}
