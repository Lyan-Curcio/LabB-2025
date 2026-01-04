package com.lab_b.server.queries;

import com.lab_b.common.Book;
import com.lab_b.server.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

public class BookQueries {
    public static LinkedList<Book> selectAll() throws SQLException {
        String query = "SELECT * FROM Libri";
        return DatabaseManager.getInstance().executeQuery(query, Book::new);
    }

    public static LinkedList<Book> searchByTitle(String title) throws SQLException {
        String query = "SELECT * FROM Libri WHERE LOWER(titolo) LIKE LOWER(?)";
        return DatabaseManager.getInstance().executeQuery(query, Book::new, new String[] {title});
    }
}