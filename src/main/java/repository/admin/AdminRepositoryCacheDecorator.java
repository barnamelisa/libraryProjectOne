package repository.admin;

import model.Book;
import model.Order;
import model.User;
import repository.book.Cache;

import java.util.List;

public class AdminRepositoryCacheDecorator extends AdminRepositoryDecorator{
    private Cache<User> cache;
    public AdminRepositoryCacheDecorator(AdminRepository adminRepository, Cache<User> cache) {
        super(adminRepository);
        this.cache = cache;
    }

    @Override
    public boolean save(User user) {
        // cache-ul se modifica deci ce am avut anterior nu mai este valabil, deci trebuie invalidat cache-ul
        cache.invalidateCache();
        return decoratedAdminRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        if (cache.hasResult()){
            return cache.load(); // daca da, atunci vom returna continut cache (nu mai ajungem in bd)
        }

        // daca nu avem nimic in cache mergem in bd, citim rez si incarcam rezultatele
        List<User> users= decoratedAdminRepository.findAll();
        cache.save(users);

        return users;
    }

    @Override
    public List<Order> getOrdersForUser(Long userId) {
        return decoratedAdminRepository.getOrdersForUser(userId);
    }

    @Override
    public List<User> findAllEmployees() {
        if (cache.hasResult()) {
            return cache.load(); // returnam rez din cache
        }

        // daca nu avem date in cache, interogam baza de date pentru a obtine angajatii
        List<User> employees = decoratedAdminRepository.findAllEmployees();

        cache.save(employees);

        return employees;
    }

    @Override
    public List<Book> findSoldBooksByUserId(Long userId) {
        return decoratedAdminRepository.findSoldBooksByUserId(userId);
    }
}
