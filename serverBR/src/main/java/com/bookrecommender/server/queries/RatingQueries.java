package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import com.bookrecommender.common.enums.rating.DeleteRatingResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Classe di utility che contiene le query SQL per la gestione delle valutazioni (recensioni) dei libri.
 * <p>
 * Gestisce l'inserimento di nuovi voti e commenti e la loro rimozione, assicurando
 * che ogni utente possa valutare un determinato libro una sola volta.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class RatingQueries {

    /**
     * Inserisce una nuova valutazione per un libro nel database.
     * <p>
     * Esegue preventivamente un controllo per verificare se l'utente ha già valutato
     * lo stesso libro. Se il controllo passa, vengono inseriti tutti i punteggi parziali
     * (stile, contenuto, ecc.) e le relative note testuali.
     * </p>
     *
     * @param userId l'identificativo dell'utente che sta rilasciando la valutazione
     * @param v      l'oggetto DTO contenente i punteggi e i commenti
     * @return <code>CreateRatingResult.OK</code> se l'inserimento ha successo,
     * <code>CreateRatingResult.ALREADY_RATED</code> se esiste già una valutazione per quel libro,
     * <code>CreateRatingResult.UNEXPECTED_ERROR</code> in caso di errore SQL.
     */
    public synchronized static CreateRatingResult createRating(String userId, Rating v) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE WHEN EXISTS(
                SELECT 1 FROM "ValutazioniLibri"
                WHERE userid = ? AND libro_id = ?
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
                        System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'createRating()'!");
                        return null;
                    }
                },
                new Object[] {userId, v.libroId}
        );

        if (result.size() != 1 || result.getFirst() == null) return CreateRatingResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 1) return CreateRatingResult.ALREADY_RATED;

        query = """
            INSERT INTO "ValutazioniLibri" (
                userid, libro_id,
                stile, contenuto, gradevolezza, originalita, edizione,
                note_stile, note_contenuto, note_gradevolezza, note_originalita, note_edizione, note_finale
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {
                        userId, v.libroId,
                        v.stile, v.contenuto, v.gradevolezza, v.originalita, v.edizione,
                        v.noteStile, v.noteContenuto, v.noteGradevolezza, v.noteOriginalita, v.noteEdizione, v.noteFinale
                }
        )) return CreateRatingResult.UNEXPECTED_ERROR;

        return CreateRatingResult.OK;
    }

    /**
     * Rimuove una valutazione esistente dal database.
     * <p>
     * Verifica l'esistenza della valutazione associata all'utente prima di procedere
     * con l'eliminazione effettiva tramite ID.
     * </p>
     *
     * @param userId        l'identificativo dell'utente proprietario della valutazione
     * @param valutazioneId l'identificativo univoco della valutazione da eliminare
     * @return <code>DeleteRatingResult.OK</code> se l'eliminazione ha successo,
     * <code>DeleteRatingResult.NOT_RATED</code> se la valutazione non esiste o non appartiene all'utente,
     * <code>DeleteRatingResult.UNEXPECTED_ERROR</code> in caso di errore SQL.
     */
    public synchronized static DeleteRatingResult deleteRating(String userId, int valutazioneId) {
        @Language("PostgreSQL")
        String query = """
            SELECT CASE WHEN EXISTS(
                SELECT 1 FROM "ValutazioniLibri"
                WHERE userid = ? AND libro_id = ?
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
                new Object[] {userId, valutazioneId}
        );

        if (result.size() != 1 || result.getFirst() == null) return DeleteRatingResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return DeleteRatingResult.NOT_RATED;

        query = "DELETE FROM \"ValutazioniLibri\" WHERE id = ?";

        if (!DatabaseManager.getInstance().execute(
                query,
                new Object[] {valutazioneId}
        )) return DeleteRatingResult.UNEXPECTED_ERROR;

        return DeleteRatingResult.OK;
    }
}