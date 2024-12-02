package repository.book;

import model.Book;
import model.User;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {
    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache = cache;
    }
    @Override
    public List<Book> findAll() {
        // verificam daca avem ceva in cache sau nu
        if (cache.hasResult()){
            return cache.load(); // daca da, atunci vom returna continut cache (nu mai ajungem in bd)
        }

        // daca nu avem nimic in cache mergem in bd, citim rez si incarcam rezultatele
        List<Book> books= decoratedBookRepository.findAll();
        cache.save(books);

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if (cache.hasResult()){
            return cache.load().stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }

        return decoratedBookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        // cache-ul se modifica deci ce am avut anterior nu mai este valabil, deci trebuie invalidat cache-ul
        cache.invalidateCache();
        return decoratedBookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.delete(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedBookRepository.removeAll();
    }

    @Override
    public void saleBook(Book book) {
        cache.invalidateCache();
        decoratedBookRepository.saleBook(book);
    }


}
