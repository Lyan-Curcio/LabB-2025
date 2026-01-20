package com.bookrecommender.server;

import org.checkerframework.checker.tainting.qual.Untainted;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * Classe Singleton per la gestione della connessione al database PostgreSQL.
 * <p>
 * Gestisce il caricamento del driver JDBC e mantiene l'istanza di <code>Connection</code>.
 * Fornisce metodi di utilità per eseguire query (SELECT) e aggiornamenti (INSERT, UPDATE, DELETE)
 * in modo sicuro utilizzando <code>PreparedStatement</code>.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class DatabaseManager {

    /** Istanza unica (Singleton) della classe. */
    private static DatabaseManager instance;

    /** Oggetto di connessione JDBC attivo verso PostgreSQL. */
    private Connection pgsqlConn;

    // Credenziali DB (da caricare preferibilmente da file config o args)

    /** URL di connessione al database (include host e nome DB). */
    private final String url = "jdbc:postgresql://localhost:5432/bookrecommender";

    /** Username per l'accesso al database. */
    private final String user = "postgres";

    /** Password per l'accesso al database. */
    private final String password = "password";

    /**
     * Costruttore privato che inizializza la connessione al DB.
     * Termina l'applicazione in caso di errore critico.
     */
    private DatabaseManager() {
        // Caricamento driver JDBC
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

    /**
     * Restituisce l'unica istanza di DatabaseManager (Singleton).
     * @return l'istanza del manager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Restituisce la connessione JDBC attiva.
     * @return oggetto <code>Connection</code>
     */
    public Connection getPgsqlConn() {
        return pgsqlConn;
    }

    /**
     * Esegue una query di selezione e mappa i risultati.
     *
     * @param <T>               tipo del risultato mappato
     * @param query             query SQL
     * @param resultConstructor funzione per convertire il <code>ResultSet</code> nell'oggetto <code>T</code>
     * @param args              argomenti per il <code>PreparedStatement</code>
     * @return lista dei risultati o null in caso di errore
     */
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

    /**
     * Esegue un comando di aggiornamento (INSERT, UPDATE, DELETE).
     *
     * @param query query SQL
     * @param args  argomenti per il <code>PreparedStatement</code>
     * @return true se ha successo, false altrimenti
     */
    public boolean execute(
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
            return false;
        }

        return true;
    }
}