package repository.book;

import model.Book;
import model.User;
import model.builder.BookBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public class BookRepositoryMySQL implements BookRepository {

    private final Connection connection;
    public BookRepositoryMySQL(Connection connection){
        this.connection=connection;
    }
    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books =  new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id=" + id;

        Optional<Book> book =Optional.empty();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // avem if in loc de while pt ca avem un singur element nu o lista
            if(resultSet.next()){
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public boolean save(Book book) {
        // avem apostrof(') pt fiecare elem ca sa se poata realiza cu succes scrierea in baza de date
        // String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'" + book.getTitle()+"\', \'" + book.getPublishedDate() + "\' );";
        String newSql = "INSERT INTO book (author, title, publishedDate, price, stock) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(newSql)){
            preparedStatement.setString(1, book.getAuthor()); // 1 se refera la primul ? ; getAuthor() returneaza autorul; intreaga secventa inlocuieste de fapt primul ? cu autorul(ex:Ion Luca Caragiale)
            preparedStatement.setString(2, book.getTitle()); // 2 se refera la primul ? ; getTitle() returneaza titlul; intreaga secventa inlocuieste de fapt al doilea ? cu titlul(ex:O scrisoare pierduta)
            preparedStatement.setDate(3,Date.valueOf(book.getPublishedDate()));
            preparedStatement.setInt(4,book.getPrice());
            preparedStatement.setInt(5,book.getStock());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Book book) {
        // String newSql = "DELETE FROM book WHERE author=\'" + book.getAuthor() +"\' AND title=\'" + book.getTitle()+"\';"; -- VULNERABIL
        // Folosim PreparedStatement in loc de Statement si ? ca sa evitam SQL Injection (adaugam protectie si nu mai avem concatenare de string-uri)
        String newSql = "DELETE FROM book WHERE author = ? AND title = ? AND price = ? AND stock = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(newSql)){
            preparedStatement.setString(1, book.getAuthor()); // 1 se refera la primul ? ; getAuthor() returneaza autorul; intreaga secventa inlocuieste de fapt primul ? cu autorul(ex:Ion Luca Caragiale)
            preparedStatement.setString(2, book.getTitle()); // 2 se refera la primul ? ; getTitle() returneaza titlul; intreaga secventa inlocuieste de fapt al doilea ? cu titlul(ex:O scrisoare pierduta)
            preparedStatement.setInt(3,book.getPrice());
            preparedStatement.setInt(4,book.getStock());
            preparedStatement.executeUpdate();
        //iar interogarea sql va deveni: DELETE FROM book WHERE author = 'Ion Luca Caragiale' AND title = 'O scrisoare pierduta';
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeAll() {
        // TRUNCATE- se reseteaza si id-urile, adica incep din nou de la 0
        // DELETE- id-urile cresc tot cu cate 1 chiar daca am sters inainte carti sau nu
        String sql = "TRUNCATE TABLE book;";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // aici implementam logica/functionalitatea saleBook
    // cand vand cartea(Sale) in loc sa decrementeze stocul chiar daca mai este disponibil in stoc, o sterge din baza de date
    // CONDITII: daca cartea are 1 sau mai multe bucati in stock si se apasa butonul de sale, se va decrementa doar stockul, daca se vinde ultima carte
    // atunci cartea va fi stearsa din book
    // cand se apasa butonul de sale se face ce am zis mai sus + se insereaza in tabela order datele necesare comenzii
    @Override
    public void saleBook(Book book) {
        if (book.getStock() > 0) {
            book.setStock(book.getStock() - 1);
            update(book);
        } else {
            System.out.println("Out of stock for book: " + book.getTitle());
        }
    }

    // de explicat + inteles
    public boolean update(Book book){
        String updateStock = "UPDATE book SET stock = ? WHERE title = ? and author = ?";

        try{
            PreparedStatement updateStockStatement  = connection.prepareStatement(updateStock);
            updateStockStatement.setLong(1,book.getStock());
            updateStockStatement.setString(2, book.getTitle());
            updateStockStatement.setString(3, book.getAuthor());

            updateStockStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPrice(resultSet.getInt("price"))
                .setStock(resultSet.getInt("stock"))
                .build();
    }
}
