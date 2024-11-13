package repository;

import model.Book;
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

public class BookRepositoryMySQL implements BookRepository{

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
        String newSql = "INSERT INTO book (author, title, publishedDate) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(newSql)){
            preparedStatement.setString(1, book.getAuthor()); // 1 se refera la primul ? ; getAuthor() returneaza autorul; intreaga secventa inlocuieste de fapt primul ? cu autorul(ex:Ion Luca Caragiale)
            preparedStatement.setString(2, book.getTitle()); // 2 se refera la primul ? ; getTitle() returneaza titlul; intreaga secventa inlocuieste de fapt al doilea ? cu titlul(ex:O scrisoare pierduta)
            preparedStatement.setDate(3,Date.valueOf(book.getPublishedDate()));
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
        String newSql = "DELETE FROM book WHERE author = ? AND title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(newSql)){
            preparedStatement.setString(1, book.getAuthor()); // 1 se refera la primul ? ; getAuthor() returneaza autorul; intreaga secventa inlocuieste de fapt primul ? cu autorul(ex:Ion Luca Caragiale)
            preparedStatement.setString(2, book.getTitle()); // 2 se refera la primul ? ; getTitle() returneaza titlul; intreaga secventa inlocuieste de fapt al doilea ? cu titlul(ex:O scrisoare pierduta)
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

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .build();
    }
}
