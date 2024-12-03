package service.admin;

import model.Book;
import model.User;

import java.util.List;

public interface AdminService {
    // aici are loc partea de logica/procesare
    boolean addUser(User user);
    List<User> findAll();
    boolean generateReport();
    void generatePdfReport(List<User> users);
}
