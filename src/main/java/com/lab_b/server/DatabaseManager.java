package main.java.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    
    // Credenziali DB (da caricare preferibilmente da file config o args)
    private String url = "jdbc:postgresql://localhost:5432/bookrecommender";
    private String user = "postgres";
    private String password = "password";

    private DatabaseManager() {
        try {
            // Caricamento driver JDBC [cite: 227]
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
    
    // Metodi per chiudere connessione, gestire transazioni, ecc.
}