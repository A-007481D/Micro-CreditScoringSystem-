package main.java.com.microfin.config;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static DBConnection instance;
    private final Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/microfin";
    private static final String USER = "postgres";
    private static final String PASSWORD = "malik";

    private DBConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            this.connection.setAutoCommit(true);
        } catch (SQLException ex) {
            throw new RuntimeException("failed to connect DB", ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Postgres driver not found " , ex);
        }
    }


    public static DBConnection getInstance() {
        if (instance == null){
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }


}