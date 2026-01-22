package com.bookrecommender.db_init;

import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Classe principale (Entry Point) del modulo di inizializzazione del database.
 * <p>
 * Questa classe è responsabile del setup iniziale dell'ambiente di persistenza. Esegue in sequenza:
 * </p>
 * <ol>
 * <li>Connessione al database tramite {@link DatabaseManager}.</li>
 * <li>Abilitazione dell'estensione <code>pgcrypto</code> necessaria per l'hashing delle password.</li>
 * <li>Creazione dello schema (tabelle) eseguendo lo script SQL <code>create-tables.sql</code>.</li>
 * <li>Popolamento iniziale della tabella Libri importando i dati dal file CSV <code>BooksDatasetClean.csv</code>.</li>
 * </ol>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class Main
{
    /**
     * Costruttore privato implicito.
     */
    public Main() {}

    /**
     * Metodo principale che orchestra l'intera procedura di inizializzazione.
     * <p>
     * Stampa a video i log delle operazioni per monitorare l'avanzamento del processo.
     * </p>
     */
    static void main()
    {
        System.out.println("Connessione al Database...");
        DatabaseManager dbm = DatabaseManager.getInstance();

        System.out.println("Abilitando l'estensione 'pgcrypto'...");
        dbm.execute("CREATE EXTENSION IF NOT EXISTS pgcrypto", null);

        System.out.println("Creazione delle tabelle...");
        dbm.execute(readSqlFileFromRes("create-tables.sql"), null);

        System.out.println("Caricamento dei dati...");
        loadFromCsv("BooksDatasetClean.csv");

        System.out.println("FATTO!");
    }

    /**
     * Importa i dati dei libri da un file CSV utilizzando le API native di PostgreSQL per il caricamento massivo.
     * <p>
     * Al posto di eseguire singole query <code>INSERT</code>, questo metodo utilizza {@link CopyManager}
     * e il comando SQL <code>COPY FROM STDIN</code>. Questo approccio garantisce performance elevate
     * anche con dataset di grandi dimensioni.
     * </p>
     *
     * @param path il percorso del file CSV all'interno delle risorse del progetto (classpath)
     */
    public static void loadFromCsv(String path) {
        if (path == null) return;

        try (InputStream is = Main.class
                .getClassLoader()
                .getResourceAsStream(path)
        ) {
            if (is == null) {
                System.err.println("Impossibile trovare il file " + path + " nelle risorse!");
                System.exit(1);
            }

            CopyManager copyManager = ((PGConnection) DatabaseManager.getInstance().getPgsqlConn()).getCopyAPI();

            @Language("PostgreSQL")
            String sql = "COPY \"Libri\" (titolo, autori, anno_pubblicazione, editore, categorie) FROM STDIN WITH (FORMAT csv, HEADER true)";

            long rows = copyManager.copyIn(sql, is);
            System.out.println("Importate " + rows + " righe");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Legge il contenuto di un file SQL dalle risorse e lo restituisce come stringa.
     * <p>
     * Il metodo filtra automaticamente le righe vuote e i commenti SQL (che iniziano con "--")
     * per pulire lo script prima dell'esecuzione.
     * </p>
     *
     * @param path il percorso del file SQL nelle risorse
     * @return una stringa contenente i comandi SQL validi, oppure <code>null</code> in caso di errore
     */
    public static String readSqlFileFromRes(String path) {
        if (path == null) return null;

        try (InputStream is = Main.class
                .getClassLoader()
                .getResourceAsStream(path)
        ) {
            if (is == null) {
                System.err.println("Impossibile trovare il file " + path + " nelle risorse!");
                System.exit(1);
            }

            StringBuilder sql = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Salta commenti o righe vuote
                    if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                        continue;
                    }
                    sql.append(line).append("\n");
                }
            }

            return sql.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}