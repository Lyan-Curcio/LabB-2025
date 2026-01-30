package com.bookrecommender.common.dto;


import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuggestionCount implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    public final Book suggestedBook;
    public final int count;

    public SuggestionCount(Book suggestedBook, ResultSet rs) {
        int _count;

        try {
            _count = rs.getInt("count");
        } catch (SQLException e) {
            System.err.println("Impossibile costruire un 'AverageRatings' con il 'ResultSet': " + rs);
            _count = 0;
        }

        this.suggestedBook = suggestedBook;
        this.count = _count;
    }
}