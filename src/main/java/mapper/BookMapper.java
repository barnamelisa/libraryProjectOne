package mapper;

import model.Book;
import model.builder.BookBuilder;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BookMapper {
    // facem doar metode statice ca sa nu trebuiasca sa cream obiecte de tipul BookMapper
    public static BookDTO convertBookToBookDTO(Book book){
        return new BookDTOBuilder().setTitle(book.getTitle()).setAuthor(book.getAuthor()).build();
    }

    public static Book convertBookDTOToBook(BookDTO bookDTO){
        return new BookBuilder().setTitle(bookDTO.getTitle()).setAuthor(bookDTO.getAuthor()).setPublishedDate(LocalDate.of(2010, 1, 1)).build();
    }

    public static List<BookDTO> convertBookListToBookDTOList(List<Book> books){
        return books.parallelStream().map(BookMapper::convertBookToBookDTO).collect(Collectors.toList()); // mapam fiecare elem la BookMapper de convertBookToBookDTO si colectam rez intr-o lista
    }
    public static List<Book> convertBookDTOListToBookList(List<BookDTO> bookDTOS){
        return bookDTOS.parallelStream().map(BookMapper::convertBookDTOToBook).collect(Collectors.toList());
    }
}
