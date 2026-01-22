package com.bookrecommender.server;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.checkerframework.checker.tainting.qual.Untainted;
import org.intellij.lang.annotations.Language;

import java.io.File;
import java.net.URISyntaxException;
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

    /**
     * Costruttore privato che inizializza la connessione al DB.
     * Termina l'applicazione in caso di errore critico.
     */
    private DatabaseManager() {
        String dbUrl = "";
        String dbUser = "";
        String dbPassword = "";

        // Prova a caricare le credenziali DB da file ".env"
        //
        // Il path del file ".env" dipende da come si sta eseguendo il programma:
        // - Se si sta sviluppando verranno eseguiti i file ".class"
        //    e il file ".env" sarà cercato nella root del progetto.
        // - Se si sta utilizzando il programma verrà eseguito un file ".jar"
        //    e il file ".env" sarà cercato nella stessa cartella del file ".jar".
        try {
            Dotenv dotenv;

            String jarPath = getJarPath();
            if (jarPath != null)
            {
                dotenv = Dotenv
                    .configure()
                    .directory(new File(jarPath).getParent())
                    .load();
            }
            else
            {
                dotenv = Dotenv.load();
            }

            String dbHost = dotenv.get("DB_HOST");
            String dbPort = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

            dbUser = dotenv.get("DB_USER");
            dbPassword = dotenv.get("DB_PASSWORD");
        }
        catch (DotenvException e) {
            // Altrimenti richiedi le credenziali da stdin
            System.out.println("Non è stato possibile caricare le credenziali del DB dal file '.env'");

            String dbHost = System.console().readLine("Inserire l'host del DB [localhost]: ");
            if (dbHost.isBlank()) dbHost = "localhost";
            String dbPort = System.console().readLine("Inserire la porta del DB [5432]: ");
            if (dbPort.isBlank()) dbPort = "5432";
            String dbName = System.console().readLine("Inserire il nome del DB [bookrecommender]: ");
            if (dbName.isBlank()) dbName = "bookrecommender";
            dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

            dbUser = System.console().readLine("Inserire l'utente del DB [postgres]: ");
            if (dbUser.isBlank()) dbUser = "postgres";
            dbPassword = System.console().readLine("Inserire la password del DB [password]: ");
            if (dbPassword.isBlank()) dbPassword = "password";
        }

        // Caricamento driver JDBC
        try {
            Class.forName("org.postgresql.Driver");
            this.pgsqlConn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
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


    /**
     * Recupera il path del file ".jar" che sta eseguendo il programma
     *
     * @return il path se si sta eseguendo un file .jar, <code>null</code> altrimenti
     */
    private String getJarPath() {
        // Recupera il nome della risorsa e controlla se inizia con "jar:"
        boolean runFromJar = DatabaseManager
            .class
            .getResource(
                DatabaseManager
                    .class
                    .getSimpleName() + ".class"
            )
            .toString()
            .startsWith("jar:");

        if (!runFromJar) {
            return null;
        }

        try {
            return new File(
                DatabaseManager
                    .class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            ).getPath();
        } catch (URISyntaxException e) {
            // Non dovrebbe mai arrivare qui
            e.printStackTrace();
            System.exit(1);

            // Per far contento java
            return null;
        }
    }
}