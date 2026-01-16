package com.bookrecommender.db_init;

import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Main
{
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

            // Separa gli statement
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
