package com.lab_b.db_init;

import com.lab_b.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class Main
{
    static void main()
    {
        System.out.println("Connessione al Database...");
        DatabaseManager dbm = DatabaseManager.getInstance();

        System.out.println("Creazione delle tabelle...");
        for (@Language("PostgreSQL") String s : readSqlFileFromRes("create-tables.sql"))
        {
            System.out.println("Esecuzione di: " + s + "\n");
            dbm.executeQuery(s, null, null);
        }
    }

    public static String[] readSqlFileFromRes(String path) {
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

            // Split on semicolons (;) to get individual statements
            return sql.toString().trim().split(";");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
