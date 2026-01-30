package com.bookrecommender.common.extended_dto;

import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.dto.Rating;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Classe DTO (Data Transfer Object) che aggrega tutte le informazioni relative a un libro specifico.
 * <p>
 * Questo oggetto viene utilizzato per restituire in un'unica chiamata RMI il libro stesso,
 * la lista delle valutazioni associate e la lista dei suggerimenti correlati.
 * Implementa <code>Serializable</code> per il trasferimento via rete.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class BookInfo implements Serializable {
    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** L'oggetto {@link Book} contenente i metadati principali (titolo, autore, anno, ecc.). */
    public final Book book;

    /** Lista delle valutazioni (recensioni) rilasciate dagli utenti per questo libro. */
    public final LinkedList<Rating> ratings;

    /** Lista dei suggerimenti che collegano questo libro ad altri titoli consigliati. */
    public final LinkedList<SuggestionWithBooks> suggestions;

    public final AverageRatings averageRatings;

    public final LinkedList<SuggestionCount> suggestionCounts;

    /**
     * Costruisce un nuovo oggetto aggregatore {@link BookInfo}.
     *
     * @param book        l'oggetto libro principale
     * @param ratings      la lista delle valutazioni associate al libro
     * @param suggestions la lista dei suggerimenti associati al libro
     */
    public BookInfo(
        Book book,
        LinkedList<Rating> ratings,
        LinkedList<SuggestionWithBooks> suggestions,
        AverageRatings averageRatings,
        LinkedList<SuggestionCount> suggestionCounts
    ) {
        this.book = book;
        this.ratings = ratings;
        this.suggestions = suggestions;
        this.averageRatings = averageRatings;
        this.suggestionCounts = suggestionCounts;
    }
}