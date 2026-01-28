package com.bookrecommender.server;

import com.bookrecommender.common.dto.Suggestion;
import com.bookrecommender.common.dto.SuggestionWithBooks;
import com.bookrecommender.server.queries.BookQueries;

import java.util.LinkedList;
import java.util.stream.Collectors;


/**
 * Classe di utility generale.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class Utils {

    /**
     * Costruisce una lista di {@link SuggestionWithBooks} da una lista di {@link Suggestion}
     *
     * @param suggestions la lista di {@link Suggestion} da cui costruire la lista di {@link SuggestionWithBooks}
     * @return la lista di {@link SuggestionWithBooks}
     */
    static public LinkedList<SuggestionWithBooks> suggWithBooksFromSugg(LinkedList<Suggestion> suggestions) {
        return suggestions.stream().map(s -> new SuggestionWithBooks(
            s.userId,
            BookQueries.getBook(s.mainBookId),
            BookQueries.getBook(s.suggestedBookId)
        )).collect(Collectors.toCollection(LinkedList::new));
    }
}
