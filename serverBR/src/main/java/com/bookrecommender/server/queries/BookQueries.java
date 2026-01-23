package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.dto.BookInfo;
import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.dto.Suggestion;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.util.LinkedList;

/**
 * Classe di utility per la ricerca e il recupero dei libri dal database.
 * Utilizza l'operatore ILIKE per ricerche case-insensitive.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class BookQueries {

    /**
     * Recupera tutte le informazioni aggregate relative a un libro (Dettagli, Valutazioni, Suggerimenti).
     * <p>
     * Questo metodo esegue tre query distinte in sequenza:
     * 1. Recupera i dati del libro dalla tabella "Libri".
     * 2. Recupera tutte le recensioni dalla tabella "ValutazioniLibri".
     * 3. Recupera tutti i consigli dalla tabella "ConsigliLibri".
     * </p>
     *
     * @param bookId l'identificativo univoco del libro
     * @return un oggetto {@link BookInfo} contenente tutti i dati aggregati
     * @throws java.util.NoSuchElementException se il libro con l'ID specificato non esiste
     */
    public synchronized static BookInfo getBookInfo(int bookId) {
        @Language("PostgreSQL")
        String query = """
            SELECT * FROM "Libri"
            WHERE id = ?
        """;
        Book book = DatabaseManager.getInstance().executeQuery(
                query,
                Book::new,
                new Object[] {bookId}
        ).getFirst();

        query = """
            SELECT * FROM "ValutazioniLibri"
            WHERE libro_id = ?
        """;
        LinkedList<Rating> ratings = DatabaseManager.getInstance().executeQuery(
                query,
                Rating::new,
                new Object[] {bookId}
        );

        query = """
            SELECT * FROM "ConsigliLibri"
            WHERE libro_sorgente_id = ?
        """;
        LinkedList<Suggestion> suggestions = DatabaseManager.getInstance().executeQuery(
                query,
                Suggestion::new,
                new Object[] {bookId}
        );

        return new BookInfo(book, ratings, suggestions);
    }

    /**
     * Recupera tutti i libri presenti nel database.
     * @return lista completa dei libri
     */
    public synchronized static LinkedList<Book> selectAll() {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\"";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Book::new,
                null
        );
    }

    /**
     * Cerca i libri il cui titolo contiene la stringa specificata.
     * @param title titolo o parte di esso
     * @return lista dei libri trovati
     */
    public synchronized static LinkedList<Book> searchByTitle(String title) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE titolo ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Book::new,
                new Object[] {title}
        );
    }

    /**
     * Cerca i libri scritti dall'autore specificato.
     * @param author autore o parte del nome
     * @return lista dei libri trovati
     */
    public synchronized static LinkedList<Book> searchByAuthor(String author) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE autori ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Book::new,
                new Object[] {author}
        );
    }

    /**
     * Cerca i libri per autore e anno di pubblicazione.
     * @param author autore
     * @param year anno esatto
     * @return lista dei libri trovati
     */
    public synchronized static LinkedList<Book> searchByAuthorAndYear(String author, int year) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE autori ILIKE '%'||?||'%' AND anno_pubblicazione = ?";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Book::new,
                new Object[] {author, year}
        );
    }
}