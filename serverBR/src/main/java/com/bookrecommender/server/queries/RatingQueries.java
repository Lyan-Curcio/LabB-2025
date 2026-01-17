package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.Valutazione;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import com.bookrecommender.common.enums.rating.DeleteRatingResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

public class RatingQueries {
    public static CreateRatingResult createRating(String userId, Valutazione v) {
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


    public static DeleteRatingResult deleteRating(String userId, int valutazioneId) {
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
