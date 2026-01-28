package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;

public class SuggestionWithBooks implements Serializable {
    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificativo univoco del suggerimento nel database. */
    public final int id;

    /** Identificativo dell'utente che ha creato il suggerimento. */
    public final String userId;

    /** Il libro sorgente (il libro sul quale viene dato il consiglio). */
    public final Book mainBook;

    /** Il libro consigliato. */
    public final Book suggestedBook;

    /**
     * Costruisce un nuovo oggetto {@link SuggestionWithBooks} con i dati specificati.
     *
     * @param userId          l'ID dell'utente autore del consiglio
     * @param mainBook        il del libro sorgente
     * @param suggestedBook   il libro consigliato
     */
    public SuggestionWithBooks(String userId, Book mainBook, Book suggestedBook) {
        id = -1;
        this.userId = userId;
        this.mainBook = mainBook;
        this.suggestedBook = suggestedBook;
    }

    /**
     * Restituisce una rappresentazione stringa del suggerimento per scopi di debug.
     *
     * @return una stringa con ID, utente e la relazione tra i due libri
     */
    public String toStringDebug() {
        return id + ": da " + userId +
            "\n\t" + mainBook.id + " -> " + suggestedBook.id;
    }

    /**
     * Restituisce una stringa formattata sintetica per la visualizzazione all'utente.
     *
     * @return una stringa formattata
     */
    public String toStringInfo() {
        return "Da: " + userId + "\nPer \"" + mainBook.titolo + "\" consiglia \"" + suggestedBook.titolo + "\"";
    }
}
