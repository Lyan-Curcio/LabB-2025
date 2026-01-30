package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.dto.Library;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Classe di utility che contiene le query SQL per la gestione delle librerie personali degli utenti.
 * <p>
 * Fornisce metodi statici per creare ed eliminare librerie, nonché per aggiungere o rimuovere libri da esse.
 * La logica di controllo (es. verifica esistenza libreria, duplicati, permessi utente) è implementata
 * in modo efficiente direttamente nelle query SQL tramite costrutti <code>CASE WHEN</code>,
 * riducendo il numero di round-trip verso il database.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class LibraryQueries {

    /**
     * Cerca nel database le librerie il cui nome contiene la stringa specificata (case-insensitive).
     *
     * @param libraryName il nome (o parte del nome) da cercare
     * @return una lista di oggetti {@link Library} trovati
     */
    public synchronized static LinkedList<Library> searchLibraryByName(String libraryName) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Librerie\" WHERE nome ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Library::new,
                new Object[] {libraryName}
        );
    }

    /**
     * Cerca nel database tutte le librerie il cui utente associato contiene la stringa specificata (case-insensitive).
     *
     * @param userId l'identificativo (o parte dell'identificativo) dell'utente proprietario
     * @return una lista di oggetti {@link Library} appartenenti all'utente
     */
    public synchronized static LinkedList<Library> searchLibraryByUser(String userId) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Librerie\" WHERE userid ILIKE '%'||?||'%'";
        return DatabaseManager.getInstance().executeQuery(
                query,
                Library::new,
                new Object[] {userId}
        );
    }

    /**
     * Cerca nel database tutte le librerie associate a un determinato UserID.
     *
     * @param userId l'identificativo (esatto) dell'utente proprietario
     * @return una lista di oggetti {@link Library} appartenenti all'utente
     */
    public synchronized static LinkedList<Library> getLibrerieFrom(String userId) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"Librerie\" WHERE userid = ?";
        return DatabaseManager.getInstance().executeQuery(
            query,
            Library::new,
            new Object[] {userId}
        );
    }

    /**
     * Cerca nel database tutti i libri contenuti nella libreria specificata.
     *
     * @param libraryId l'identificativo della libreria in cui cercare
     * @return una lista di oggetti {@link Book} contenuti nella libreria specificata
     */
    public synchronized static LinkedList<Book> getLibriFromLibreria(int libraryId) {
        @Language("PostgreSQL")
        String query = """
            SELECT * FROM "Libri" AS l
            JOIN "LibriXLibrerie" AS lxl ON l.id = lxl.libro_id
            WHERE lxl.libreria_id = ?
        """;
        return DatabaseManager.getInstance().executeQuery(
            query,
            Book::new,
            new Object[] {libraryId}
        );
    }

    /**
     * Crea una nuova libreria per un utente specifico.
     * <p>
     * Verifica preventivamente se l'utente possiede già una libreria con lo stesso nome.
     * </p>
     *
     * @param userId       l'identificativo dell'utente proprietario
     * @param nomeLibreria il nome della nuova libreria
     * @return <code>CreateLibResult.OK</code> se la creazione ha successo,
     * <code>CreateLibResult.DUPLICATE_NAME</code> se il nome è già in uso,
     * <code>CreateLibResult.UNEXPECTED_ERROR</code> in caso di errore SQL.
     */
    public synchronized static CreateLibResult createLibrary(String userId, String nomeLibreria) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE WHEN EXISTS(
                SELECT 1 FROM "Librerie"
                WHERE userid = ? AND nome = ?
            ) THEN 1 ELSE 0
            END AS r
        """;

        LinkedList<Integer> result = DatabaseManager.getInstance().executeQuery(
            query,
            rs ->{
                try
                {
                    return rs.getInt("r");
                }
                catch (SQLException e)
                {
                    System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'createLibrary()'!");
                    return null;
                }
            },
            new Object[] {userId, nomeLibreria}
        );

        if (result == null || result.size() != 1 || result.getFirst() == null) return CreateLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 1) return CreateLibResult.DUPLICATE_NAME;

        query = "INSERT INTO \"Librerie\" (nome, userid) VALUES (?, ?)";

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {nomeLibreria, userId}
        )) return CreateLibResult.UNEXPECTED_ERROR;

        return CreateLibResult.OK;
    }

    /**
     * Elimina definitivamente una libreria utente.
     * <p>
     * Prima di procedere all'eliminazione, verifica che la libreria esista e appartenga effettivamente all'utente richiedente.
     * </p>
     *
     * @param userId     l'identificativo dell'utente proprietario
     * @param libreriaId l'identificativo univoco della libreria da eliminare
     * @return <code>DeleteLibResult.OK</code> se l'eliminazione ha successo,
     * <code>DeleteLibResult.LIBRARY_NOT_FOUND</code> se la libreria non esiste o non appartiene all'utente,
     * <code>DeleteLibResult.UNEXPECTED_ERROR</code> in caso di errore SQL.
     */
    public synchronized static DeleteLibResult deleteLibrary(String userId, int libreriaId) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE WHEN EXISTS(
                SELECT 1 FROM "Librerie"
                WHERE userid = ? AND id = ?
            ) THEN 1 ELSE 0
            END AS r
        """;

        LinkedList<Integer> result = DatabaseManager.getInstance().executeQuery(
            query,
            rs ->{
                try
                {
                    return rs.getInt("r");
                }
                catch (SQLException e)
                {
                    System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'deleteLibrary()'!");
                    return null;
                }
            },
            new Object[] {userId, libreriaId}
        );

        if (result == null || result.size() != 1 || result.getFirst() == null) return DeleteLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return DeleteLibResult.LIBRARY_NOT_FOUND;

        query = "DELETE FROM \"Librerie\" WHERE id = ?";

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {libreriaId}
        )) return DeleteLibResult.UNEXPECTED_ERROR;

        return DeleteLibResult.OK;
    }

    /**
     * Aggiunge un libro a una libreria specifica.
     * <p>
     * Utilizza una query con logica <code>CASE WHEN</code> per distinguere tre scenari in un unico passaggio:
     * </p>
     * <ul>
     * <li><strong>0</strong>: La libreria non esiste o non appartiene all'utente.</li>
     * <li><strong>2</strong>: Il libro è già presente nella libreria (duplicato).</li>
     * <li><strong>1</strong>: Condizioni valide per l'inserimento.</li>
     * </ul>
     *
     * @param userId     l'identificativo dell'utente
     * @param libreriaId l'identificativo della libreria di destinazione
     * @param libroId    l'identificativo del libro da aggiungere
     * @return un valore di <code>AddBookToLibResult</code> che descrive l'esito specifico (Successo, Libreria non trovata, Libro già presente).
     */
    public synchronized static AddBookToLibResult addBookToLibrary(String userId, int libreriaId, int libroId) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE
                -- 0: La libreria non esiste
                WHEN NOT EXISTS (
                    SELECT 1
                    FROM "Librerie"
                    WHERE id = ?
                        AND userid = ?
                ) THEN 0
        
                -- 2: La libreria esiste e contiene il libro
                WHEN EXISTS (
                    SELECT 1
                    FROM "LibriXLibrerie"
                    WHERE libreria_id = ?
                        AND libro_id = ?
                ) THEN 2
        
                -- 1: La libreria esiste ma non contiene il libro
                ELSE 1
            END AS r;
        """;

        LinkedList<Integer> result = DatabaseManager.getInstance().executeQuery(
            query,
            rs ->{
                try
                {
                    return rs.getInt("r");
                }
                catch (SQLException e)
                {
                    System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'addBookToLibrary()'!");
                    return null;
                }
            },
            new Object[] {libreriaId, userId, libreriaId, libroId}
        );

        if (result == null || result.size() != 1 || result.getFirst() == null) return AddBookToLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return AddBookToLibResult.LIBRARY_NOT_FOUND;
        else if (result.getFirst() == 2) return AddBookToLibResult.BOOK_ALREADY_IN_LIBRARY;

        query = "INSERT INTO \"LibriXLibrerie\" (libro_id, libreria_id) VALUES (?, ?)";

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {libroId, libreriaId}
        )) return AddBookToLibResult.UNEXPECTED_ERROR;

        return AddBookToLibResult.OK;
    }

    /**
     * Rimuove un libro da una libreria specifica.
     * <p>
     * Utilizza una logica simile all'aggiunta per verificare l'esistenza della libreria e la presenza effettiva del libro prima della rimozione.
     * </p>
     *
     * @param userId     l'identificativo dell'utente
     * @param libreriaId l'identificativo della libreria sorgente
     * @param libroId    l'identificativo del libro da rimuovere
     * @return <code>RemoveBookFromLibResult.OK</code> se rimosso con successo,
     * <code>RemoveBookFromLibResult.LIBRARY_NOT_FOUND</code> se la libreria non esiste,
     * <code>RemoveBookFromLibResult.BOOK_NOT_IN_LIBRARY</code> se il libro non era presente.
     */
    public synchronized static RemoveBookFromLibResult removeBookFromLibrary(String userId, int libreriaId, int libroId) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE
                -- 0: La libreria non esiste
                WHEN NOT EXISTS (
                    SELECT 1
                    FROM "Librerie"
                    WHERE id = ?
                        AND userid = ?
                ) THEN 0
        
                -- 1: La libreria esiste ma non contiene il libro
                WHEN NOT EXISTS (
                    SELECT 1
                    FROM "LibriXLibrerie"
                    WHERE libreria_id = ?
                        AND libro_id = ?
                ) THEN 1
        
                -- 2: Se il libro è solo in questa libreria e ci sono delle recensioni o consigliati per quel libro
                WHEN
                    count_libri_in_librerie(?, ?) <= 1
                    AND EXISTS(
                        SELECT 1 FROM (
                            SELECT libro_id AS l, userid AS u FROM "ValutazioniLibri" UNION
                            SELECT libro_sorgente_id AS l, userid AS u FROM "ConsigliLibri" UNION
                            SELECT libro_consigliato_id AS l, userid AS u FROM "ConsigliLibri"
                        ) AS _
                        WHERE l = ? AND u = ?
                    )
                THEN 2
        
                -- 3: La libreria esiste e contiene il libro e non è usato in nessun consigliato/recensione
                ELSE 3
            END AS r;
        """;

        LinkedList<Integer> result = DatabaseManager.getInstance().executeQuery(
            query,
            rs ->{
                try
                {
                    return rs.getInt("r");
                }
                catch (SQLException e)
                {
                    System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'removeBookFromLibrary()'!");
                    return null;
                }
            },
            new Object[] {
                libreriaId, userId,
                libreriaId, libroId,
                userId, libroId, libroId, userId
            }
        );

        if (result == null || result.size() != 1 || result.getFirst() == null) return RemoveBookFromLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return RemoveBookFromLibResult.LIBRARY_NOT_FOUND;
        else if (result.getFirst() == 1) return RemoveBookFromLibResult.BOOK_NOT_IN_LIBRARY;
        else if (result.getFirst() == 2) return RemoveBookFromLibResult.BOOK_IS_SUGGESTED_OR_RATED;

        query = "DELETE FROM \"LibriXLibrerie\" WHERE libro_id = ? AND libreria_id = ?";

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {libroId, libreriaId}
        )) return RemoveBookFromLibResult.UNEXPECTED_ERROR;

        return RemoveBookFromLibResult.OK;
    }
}