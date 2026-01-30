package com.bookrecommender.common.extended_dto;

import com.bookrecommender.common.dto.Book;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Rappresenta un libro consigliato insieme al numero di volte in cui è stato consigliato.
 */
public class SuggestionCount implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Il libro che è stato consigliato. */
    public final Book suggestedBook;

    /** Il numero di volte in cui il libro è stato consigliato. */
    public final int count;

    /**
     * Costruisce un oggetto {@code SuggestionCount} a partire da un libro suggerito
     * e da un {@link ResultSet} che contiene il conteggio delle raccomandazioni.
     *
     * @param suggestedBook il libro che è stato suggerito
     * @param rs il {@link ResultSet} contenente il campo "count" con il numero di raccomandazioni
     */
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

    /**
     * Restituisce una rappresentazione leggibile dell'oggetto.
     *
     * @return una stringa descrittiva del suggerimento e del conteggio
     */
    public String toStringInfo() {
        return "\"" + suggestedBook.titolo + "\" è stato consigliato " + count + " volte";
    }
}
