package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.Libri;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedList;

public class LibraryQueries {
    public static CreateLibResult createLibrary(String userId, String nomeLibreria) {
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

        if (result.size() != 1 || result.getFirst() == null) return CreateLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 1) return CreateLibResult.DUPLICATE_NAME;

        query = "INSERT INTO \"Librerie\" (nome, userid) VALUES (?, ?)";

        if (!DatabaseManager.getInstance().execute(
            query,
            new Object[] {nomeLibreria, userId}
        )) return CreateLibResult.UNEXPECTED_ERROR;

        return CreateLibResult.OK;
    }


    public static DeleteLibResult deleteLibrary(String userId, int libreriaId) {
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

        if (result.size() != 1 || result.getFirst() == null) return DeleteLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return DeleteLibResult.LIBRARY_NOT_FOUND;

        query = "DELETE FROM \"Librerie\" WHERE id = ?";

        if (!DatabaseManager.getInstance().execute(
            query,
            new Object[] {libreriaId}
        )) return DeleteLibResult.UNEXPECTED_ERROR;

        return DeleteLibResult.OK;
    }


    public static AddBookToLibResult addBookToLibrary(String userId, int libreriaId, int libroId) {
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
            new Object[] {libreriaId, userId, libroId}
        );

        if (result.size() != 1 || result.getFirst() == null) return AddBookToLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return AddBookToLibResult.LIBRARY_NOT_FOUND;
        else if (result.getFirst() == 2) return AddBookToLibResult.BOOK_ALREADY_IN_LIBRARY;

        query = "INSERT INTO \"LibriXLibrerie\" (libro_id, libreria_id) VALUES (?, ?)";

        if (!DatabaseManager.getInstance().execute(
            query,
            new Object[] {libroId, libreriaId}
        )) return AddBookToLibResult.UNEXPECTED_ERROR;

        return AddBookToLibResult.OK;
    }


    public static RemoveBookFromLibResult removeBookFromLibrary(String userId, int libreriaId, int libroId) {
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
                    System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'removeBookFromLibrary()'!");
                    return null;
                }
            },
            new Object[] {libreriaId, userId, libroId}
        );

        if (result.size() != 1 || result.getFirst() == null) return RemoveBookFromLibResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return RemoveBookFromLibResult.LIBRARY_NOT_FOUND;
        else if (result.getFirst() == 1) return RemoveBookFromLibResult.BOOK_NOT_IN_LIBRARY;

        query = "DELETE FROM \"LibriXLibrerie\" WHERE libro_id = ? AND libreria_id = ?";

        if (!DatabaseManager.getInstance().execute(
            query,
            new Object[] {libroId, libreriaId}
        )) return RemoveBookFromLibResult.UNEXPECTED_ERROR;

        return RemoveBookFromLibResult.OK;
    }
}
