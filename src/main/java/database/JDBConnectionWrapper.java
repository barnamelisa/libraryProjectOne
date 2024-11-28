package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnectionWrapper {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/"; // jdbc=driverul, localhost=serverul
    private static final String USER = "root";
    private static final String PASSWORD = "Melisa09.Barna2003!";
    private static final int TIMEOUT =5; // daca bd-ul nu rasp in mai putin de 5 secunde
    private Connection connection;

    public JDBConnectionWrapper(String schema){
        // baza de date de pe acel server urmeaza sa o cream intr-un mod dinamic in constructor
        try {
            Class.forName(JDBC_DRIVER);
            connection= DriverManager.getConnection(DB_URL + schema, USER, PASSWORD);
           // createTables();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

//    private void createTables() throws SQLException {
//        Statement statement = connection.createStatement();
//
//        String sql = "CREATE TABLE IF NOT EXISTS book(" +
//                " id bigint NOT NULL AUTO_INCREMENT," +
//                " author VARCHAR(500) NOT NULL," +
//                " title VARCHAR(500) NOT NULL," +
//                " publishedDate datetime DEFAULT NULL," +
//                " price int(11) NOT NULL," +
//                " stock int(11) NOT NULL," +
//                " PRIMARY KEY(id)," +
//                " UNIQUE KEY id_UNIQUE(id)" +
//                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
//        statement.execute(sql);
//
//        String sql1 = "CREATE TABLE IF NOT EXISTS orders(" +
//                " id bigint NOT NULL AUTO_INCREMENT," +
//                " orderDate datetime DEFAULT NULL," +
//                " bookAuthor VARCHAR(500) NOT NULL," +
//                " bookTitle VARCHAR(500) NOT NULL," +
//                " bookPrice int(11) NOT NULL," +
//                " bookStock int(11) NOT NULL," +
//                " userId int(11) NOT NULL," +
//                " PRIMARY KEY(id)," +
//                " UNIQUE KEY id_UNIQUE(id)" +
//                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
//        statement.execute(sql1);
//
//    }

    public boolean testConnection() throws SQLException{
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection(){
        return connection;
    }

}
