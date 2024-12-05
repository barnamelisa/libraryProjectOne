package repository.book;

// add ob meu in interiorul unui alt obiect care are deja acel comportament
public abstract class BookRepositoryDecorator implements BookRepository {

    // trebuie sa fie protected pt ca astfel clasele care mostenesc BookRepositoryDecorator sa aiba acces la acest atribut
    protected  BookRepository decoratedBookRepository;

    public BookRepositoryDecorator(BookRepository bookRepository){
        decoratedBookRepository = bookRepository;
    }
}
