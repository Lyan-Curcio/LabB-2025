package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.Libri;
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
     * Recupera tutti i libri presenti nel database.
     * @return lista completa dei libri
     */
    public static LinkedList<Libri> selectAll() {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\"";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Libri::new,
                null
        );
    }

    /**
     * Cerca i libri il cui titolo contiene la stringa specificata.
     * @param title titolo o parte di esso
     * @return lista dei libri trovati
     */
    public static LinkedList<Libri> searchByTitle(String title) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE titolo ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Libri::new,
                new Object[] {title}
        );
    }

    /**
     * Cerca i libri scritti dall'autore specificato.
     * @param author autore o parte del nome
     * @return lista dei libri trovati
     */
    public static LinkedList<Libri> searchByAuthor(String author) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE autori ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Libri::new,
                new Object[] {author}
        );
    }

    /**
     * Cerca i libri per autore e anno di pubblicazione.
     * @param author autore
     * @param year anno esatto
     * @return lista dei libri trovati
     */
    public static LinkedList<Libri> searchByAuthorAndYear(String author, int year) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Libri\" WHERE autori ILIKE '%'||?||'%' AND anno_pubblicazione = ?";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Libri::new,
                new Object[] {author, year}
        );
    }
}