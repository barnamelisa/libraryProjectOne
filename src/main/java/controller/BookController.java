package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Book;
import model.User;
import service.book.BookService;
import service.order.OrderService;
import service.user.AuthenticationService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    private final OrderService orderService;
    private final Long userId;

    public BookController(BookView bookView, BookService bookService, OrderService orderService, Long userId){
        this.bookView = bookView;
        this.bookService = bookService;
        this.orderService = orderService;
        this.userId = userId;

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

    private class SaleBookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BookDTO selectedBookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();

            if (selectedBookDTO == null) {
                bookView.addDisplayAlertMessage("Vanzare", "Eroare", "Selectați o carte pentru a efectua vanzarea.");
                return;
            }

            Book selectedBook = BookMapper.convertBookDTOToBook(selectedBookDTO);
            boolean saleSuccessful = bookService.saleBook(selectedBook);

            if (saleSuccessful) {

                // salvez comanda folosind userId si detaliile cartii
                boolean orderSaved = orderService.save(selectedBook, userId);

                if (orderSaved){
                    bookView.saleBookFromObservableList(selectedBookDTO);
                    bookView.addDisplayAlertMessage("Vanzare", "Succes", "Cartea \"" + selectedBookDTO.getTitle() + "\" a fost vândută cu succes.");
                } else {
                    bookView.addDisplayAlertMessage("Vanzare", "Eroare", "A fost o problema la salvarea comenzii. Va rugam sa incercayi din nou.");
                }
            } else {
                bookView.addDisplayAlertMessage("Vanzare", "Eroare", "Cartea \"" + selectedBookDTO.getTitle() + "\" nu mai este in stoc.");
            }
        }
    }




}
