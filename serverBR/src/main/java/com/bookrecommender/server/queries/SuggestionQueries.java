package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.dto.Suggestion;
import com.bookrecommender.common.enums.suggestion.AddSuggestionResult;
import com.bookrecommender.common.enums.suggestion.RemoveSuggestionResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Classe di utility che contiene le query SQL per la gestione dei suggerimenti (correlazioni) tra libri.
 * <p>
 * I metodi di questa classe gestiscono la logica per cui un utente può consigliare un libro "B" a chi ha letto il libro "A".
 * La complessità dei controlli (es. l'utente deve possedere entrambi i libri nelle proprie librerie)
 * è delegata in parte al database tramite l'uso di funzioni SQL custom e costrutti condizionali.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class SuggestionQueries {

    /**
     * Recupera tutti i suggerimenti creati da un utente specifico partendo da un dato libro sorgente.
     *
     * @param userId l'identificativo dell'utente
     * @param bookId l'identificativo del libro sorgente
     * @return una lista di oggetti {@link Suggestion} trovati
     */
    public synchronized static LinkedList<Suggestion> getSuggestionsFrom(String userId, int bookId) {
        @Language("PostgreSQL")
        String query = """
                    SELECT * FROM "ConsigliLibri"
                    WHERE userid = ? AND libro_sorgente_id = ?
                """;
        return DatabaseManager.getInstance().executeQuery(
                query,
                Suggestion::new,
                new Object[]{userId, bookId}
        );
    }

    /**
     * Inserisce un nuovo suggerimento di correlazione tra due libri.
     * <p>
     * La query utilizza una logica complessa basata su <code>CASE WHEN</code> e sulla function PostgreSQL
     * <code>count_libri_in_librerie(userid, libroid)</code> per verificare in un unico passaggio:
     * </p>
     * <ol>
     * <li>Se l'utente possiede il libro sorgente (Codice 0 se manca).</li>
     * <li>Se l'utente possiede il libro consigliato (Codice 1 se manca).</li>
     * <li>Se il suggerimento esiste già (Codice 2 se duplicato).</li>
     * <li>Se tutte le condizioni sono soddisfatte (Codice 3).</li>
     * </ol>
     *
     * @param userId             l'identificativo dell'utente che crea il suggerimento
     * @param libroSorgenteId    l'ID del libro principale (a cui si riferisce il consiglio)
     * @param libroConsigliatoId l'ID del libro suggerito
     * @return un valore dell'enum <code>AddSuggestionResult</code> che rispecchia l'esito dei controlli o il successo dell'operazione.
     */
    public synchronized static AddSuggestionResult createRating(String userId, int libroSorgenteId, int libroConsigliatoId) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE
                -- 0: Se il libro sorgente non è in nessuna libreria
                WHEN count_libri_in_librerie(?, ?) = 0 THEN 0
                -- 1: Se il libro consigliato non è in nessuna libreria
                WHEN count_libri_in_librerie(?, ?) = 0 THEN 1
                -- 2: Se esiste già un consiglio identico
                WHEN EXISTS(
                    SELECT 1 FROM "ConsigliLibri"
                    WHERE userid = ? AND libro_sorgente_id = ? AND libro_consigliato_id = ?
                ) THEN 2
                -- 3: Se ci sono già 3 consigliati
                WHEN (
                    SELECT COUNT(*) FROM "ConsigliLibri"
                    WHERE userid = ? AND libro_sorgente_id = ?
                ) >= 3 THEN 3
                -- 4: Se è tutto okay per l'aggiunta di un consigliato
                ELSE 4
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
                        System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'createRating()'!");
                        return null;
                    }
                },
                new Object[] {
                        userId, libroSorgenteId,
                        userId, libroConsigliatoId,
                        userId, libroSorgenteId, libroConsigliatoId,
                        userId, libroSorgenteId
                }
        );

        if (result == null || result.size() != 1 || result.getFirst() == null) return AddSuggestionResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return AddSuggestionResult.MAIN_BOOK_NOT_IN_LIBRARY;
        else if (result.getFirst() == 1) return AddSuggestionResult.SUGGESTED_BOOK_NOT_IN_LIBRARY;
        else if (result.getFirst() == 2) return AddSuggestionResult.ALREADY_SUGGESTED;
        else if (result.getFirst() == 3) return AddSuggestionResult.TOO_MANY_SUGGESTIONS;

        query = "INSERT INTO \"ConsigliLibri\" (userid, libro_sorgente_id, libro_consigliato_id) VALUES (?, ?, ?)";

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {userId, libroSorgenteId, libroConsigliatoId}
        )) return AddSuggestionResult.UNEXPECTED_ERROR;

        return AddSuggestionResult.OK;
    }

    /**
     * Rimuove un suggerimento esistente tra due libri.
     * <p>
     * Verifica che la correlazione esista e sia stata creata dall'utente specificato prima di procedere all'eliminazione.
     * </p>
     *
     * @param userId             l'identificativo dell'utente proprietario del suggerimento
     * @param libroSorgenteId    l'ID del libro principale
     * @param libroConsigliatoId l'ID del libro suggerito da rimuovere
     * @return <code>RemoveSuggestionResult.OK</code> se rimosso con successo,
     * <code>RemoveSuggestionResult.NOT_SUGGESTED</code> se il suggerimento non esiste,
     * <code>RemoveSuggestionResult.UNEXPECTED_ERROR</code> in caso di errore.
     */
    public synchronized static RemoveSuggestionResult deleteRating(String userId, int libroSorgenteId, int libroConsigliatoId) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE WHEN EXISTS(
                SELECT 1 FROM "ConsigliLibri"
                WHERE userid = ? AND libro_sorgente_id = ? AND libro_consigliato_id = ?
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
                        System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'deleteRating()'!");
                        return null;
                    }
                },
                new Object[] {userId, libroSorgenteId, libroConsigliatoId}
        );

        if (result == null || result.size() != 1 || result.getFirst() == null) return RemoveSuggestionResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return RemoveSuggestionResult.NOT_SUGGESTED;

        query = """
            DELETE FROM "ConsigliLibri"
            WHERE userid = ? AND libro_sorgente_id = ? AND libro_consigliato_id = ?
        """;

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {userId, libroSorgenteId, libroConsigliatoId}
        )) return RemoveSuggestionResult.UNEXPECTED_ERROR;

        return RemoveSuggestionResult.OK;
    }
}