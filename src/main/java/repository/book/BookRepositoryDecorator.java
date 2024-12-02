package repository.book;

// add ob meu in interiorul unui alt obiect care are deja acel comportament
public abstract class BookRepositoryDecorator implements BookRepository {
    protected  BookRepository decoratedBookRepository;

    public BookRepositoryDecorator(BookRepository bookRepository){
        decoratedBookRepository = bookRepository;
    }
}
