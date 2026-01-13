package com.lab_b.server.queries;

import com.lab_b.common.Book;
import com.lab_b.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

public class BookQueries {
    public static LinkedList<Book> selectAll() {
        @Language("PostgreSQL")
        String query = "SELECT * FROM libri";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            null
        );
    }

    public static LinkedList<Book> searchByTitle(String title) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM libri WHERE titolo ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            new Object[] {title}
        );
    }

    public static LinkedList<Book> searchByAuthor(String author) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM libri WHERE autori ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            new Object[] {author}
        );
    }

    public static LinkedList<Book> searchByAuthorAndYear(String author, int year) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM libri WHERE autori ILIKE '%'||?||'%' AND anno_pubblicazione = ?";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            new Object[] {author, year}
        );
    }
}