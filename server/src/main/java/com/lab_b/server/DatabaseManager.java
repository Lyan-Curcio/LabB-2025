package com.lab_b.server;

import org.checkerframework.checker.tainting.qual.Untainted;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.LinkedList;
import java.util.function.Function;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection pgsqlConn;

    // Credenziali DB (da caricare preferibilmente da file config o args)
    private final String url = "jdbc:postgresql://localhost:5432/bookrecommender";
    private final String user = "postgres";
    private final String password = "password";

    private DatabaseManager() {
        // Caricamento driver JDBC [cite: 227]
        try {
            Class.forName("org.postgresql.Driver");
            this.pgsqlConn = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e) {
            System.err.println("Errore con il caricamento del driver di postgres!");
            System.exit(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante la connessione al database!");
            System.exit(1);
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getPgsqlConn() {
        return pgsqlConn;
    }

    public <T> LinkedList<T> executeQuery(
            @Untainted @Language("PostgreSQL")
            String query,
            Function<ResultSet, T> resultConstructor,
            Object[] args
    ) {
        try {
            PreparedStatement statement = pgsqlConn.prepareStatement(query);
            if (args != null) {
                for (int i = 0; i < args.length; ++i) {
                    statement.setObject(i + 1, args[i]);
                }
            }

            ResultSet rs = statement.executeQuery();
            LinkedList<T> result = new LinkedList<>();

            while (rs.next()) {
                result.add(resultConstructor.apply(rs));
            }

            return result;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void execute(
        @Untainted @Language("PostgreSQL")
        String query,
        Object[] args
    ) {
        try {
            PreparedStatement statement = pgsqlConn.prepareStatement(query);
            if (args != null) {
                for (int i = 0; i < args.length; ++i) {
                    statement.setObject(i + 1, args[i]);
                }
            }

            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Metodi per chiudere connessione, gestire transazioni, ecc.
}