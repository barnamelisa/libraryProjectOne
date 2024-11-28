package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Book;
import model.Order;
import model.User;
import model.validation.Notification;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.order.OrderService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.util.function.LongFunction;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    private final OrderService orderService;

    public BookController(BookView bookView, BookService bookService, OrderService orderService){
        this.bookView = bookView;
        this.bookService = bookService;
        this.orderService = orderService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSaleBookButtonListener(new SaleBookButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            Integer price = bookView.getPrice();
            Integer stock = bookView.getStock();

            if (title.isEmpty() || author.isEmpty() || price==null || stock==null){
                bookView.addDisplayAlertMessage("Save Error","Problem at Author or Title fields","Can not have an empty Title or Author field.");
            } else {
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).setPrice(price).setStock(stock).build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook){
                    bookView.addDisplayAlertMessage("Save Successful","Book Added","Book was successfully added to the database.");
                    bookView.addBookToObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Save Error","Problem at adding Book","There was a problem at adding the book to the database. Please try again.");
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem(); // va returna item-ul selectat
            if (bookDTO != null){
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if (deletionSuccessful){
                    bookView.addDisplayAlertMessage("Delete Successful", "Book Deleted", "Book was successfully deleted from the database.");
                    bookView.removeBookFromObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "There was a problem with the database. Please try again!");
                }
            } else {
                bookView.addDisplayAlertMessage("Delete Error", "Problem at deleting book", "You must select a book before pressing the delete button.");
            }
        }
    }

    // de explicat + inteles
    private class SaleBookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BookDTO selectedBookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();

            if (selectedBookDTO == null) {
                bookView.addDisplayAlertMessage("Vanzare", "Eroare", "Selectați o carte pentru a efectua vânzarea.");
                return;
            }

            Book selectedBook = BookMapper.convertBookDTOToBook(selectedBookDTO);
            boolean saleSuccessful = bookService.saleBook(selectedBook);

            // aici trebuie sa folosesc metoda save din clasa OrderServiceImpl
            boolean orderSuccessful = orderService.save(selectedBook, 1L);

            if (saleSuccessful) {
                // Reflectăm modificările în lista observabilă
                bookView.saleBookFromObservableList(selectedBookDTO);
            } else {
                bookView.addDisplayAlertMessage("Vânzare", "Eroare", "Cartea \"" + selectedBookDTO.getTitle() + "\" nu mai este în stoc.");
            }
        }
    }


}
