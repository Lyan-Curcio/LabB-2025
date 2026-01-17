package com.bookrecommender.server.queries;

import com.bookrecommender.common.enums.suggestion.AddSuggestionResult;
import com.bookrecommender.common.enums.suggestion.RemoveSuggestionResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

public class SuggestionQueries {
    public static AddSuggestionResult createRating(String userId, int libroSorgenteId, int libroConsigliatoId) {
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
                -- 3: Se entrambi i libri sono in qualche libreria
                ELSE 3
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
                userId, libroSorgenteId, libroConsigliatoId
            }
        );

        if (result.size() != 1 || result.getFirst() == null) return AddSuggestionResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return AddSuggestionResult.MAIN_BOOK_NOT_IN_LIBRARY;
        else if (result.getFirst() == 1) return AddSuggestionResult.SUGGESTED_BOOK_NOT_IN_LIBRARY;
        else if (result.getFirst() == 2) return AddSuggestionResult.ALREADY_SUGGESTED;

        query = "INSERT INTO \"ConsigliLibri\" (userid, libro_sorgente_id, libro_consigliato_id) VALUES (?, ?, ?)";

        if (!DatabaseManager.getInstance().execute(
            query,
            new Object[] {userId, libroSorgenteId, libroConsigliatoId}
        )) return AddSuggestionResult.UNEXPECTED_ERROR;

        return AddSuggestionResult.OK;
    }


    public static RemoveSuggestionResult deleteRating(String userId, int libroSorgenteId, int libroConsigliatoId) {
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

        if (result.size() != 1 || result.getFirst() == null) return RemoveSuggestionResult.UNEXPECTED_ERROR;
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
