package com.bookrecommender.server;

import com.bookrecommender.common.dto.Suggestion;
import com.bookrecommender.common.dto.SuggestionWithBooks;
import com.bookrecommender.server.queries.BookQueries;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class Utils {
    static public LinkedList<SuggestionWithBooks> suggWithBooksFromSugg(LinkedList<Suggestion> suggestions) {
        return suggestions.stream().map(s -> new SuggestionWithBooks(
            s.userId,
            BookQueries.getBook(s.mainBookId),
            BookQueries.getBook(s.suggestedBookId)
        )).collect(Collectors.toCollection(LinkedList::new));
    }
}
