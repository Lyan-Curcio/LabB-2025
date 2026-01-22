package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

public class BookInfo implements Serializable {
    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    public final Book book;
    public final LinkedList<Rating> ratings;
    public final LinkedList<Suggestion> suggestions;

    public BookInfo(Book book, LinkedList<Rating> rating, LinkedList<Suggestion> suggestions) {
        this.book = book;
        this.ratings = rating;
        this.suggestions = suggestions;
    }
}
