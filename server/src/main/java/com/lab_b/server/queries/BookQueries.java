package com.lab_b.server.queries;

import com.lab_b.common.Book;
import com.lab_b.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

public class BookQueries {
    public static LinkedList<Book> selectAll() throws SQLException {
        @Language("PostgreSQL")
        String query = "SELECT * FROM Libri";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new
        );
    }

    public static LinkedList<Book> searchByTitle(String title) throws SQLException {
        @Language("PostgreSQL")
        String query = "SELECT * FROM Libri WHERE titolo ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            new String[] {title}
        );
    }

    public static LinkedList<Book> searchByAuthor(String author) throws SQLException {
        @Language("PostgreSQL")
        String query = "SELECT * FROM libri WHERE autori ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            new String[] {author}
        );
    }

    public static LinkedList<Book> searchByAuthorAndYear(String author, int year) throws SQLException {
        @Language("PostgreSQL")
        String query = "SELECT * FROM libri WHERE autori ILIKE '%'||?||'%' AND anno_pubblicazione = ?";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            new String[] {author, Integer.toString(year)}
        );
    }
}