package service.admin;

import model.Book;
import model.User;
import repository.admin.AdminRepository;

import java.util.List;

public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    public AdminServiceImpl(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }
    @Override
    public boolean addUser(User user) {
        return adminRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return adminRepository.findAll();
    }
}
