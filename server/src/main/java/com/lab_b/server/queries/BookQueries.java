package com.lab_b.server.queries;

import com.lab_b.common.dto.Libri;
import com.lab_b.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.util.LinkedList;

public class BookQueries {
    public static LinkedList<Libri> selectAll() {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\"";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Libri::new,
            null
        );
    }

    public static LinkedList<Libri> searchByTitle(String title) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE titolo ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Libri::new,
            new Object[] {title}
        );
    }

    public static LinkedList<Libri> searchByAuthor(String author) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE autori ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Libri::new,
            new Object[] {author}
        );
    }

    public static LinkedList<Libri> searchByAuthorAndYear(String author, int year) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE autori ILIKE '%'||?||'%' AND anno_pubblicazione = ?";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Libri::new,
            new Object[] {author, year}
        );
    }
}