package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.UtentiRegistrati;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

public class AuthQueries {

    public static RegisterResult register(UtentiRegistrati user, String password) {
        //
        // Controllo vincoli
        @Language("PostgreSQL")
        String query = """
            SELECT
                EXISTS(SELECT 1 FROM "UtentiRegistrati" WHERE userid = ?) AS uid_ex,
                EXISTS(SELECT 1 FROM "UtentiRegistrati" WHERE codice_fiscale = ?) AS cf_ex,
                EXISTS(SELECT 1 FROM "UtentiRegistrati" WHERE email = ?) AS email_ex
        """;

        LinkedList<Boolean[]> result = DatabaseManager.getInstance().executeQuery(
            query,
            rs -> {
                try
                {
                    return new Boolean[] {
                        rs.getBoolean("uid_ex"),
                        rs.getBoolean("cf_ex"),
                        rs.getBoolean("email_ex")
                    };
                }
                catch (SQLException e)
                {
                    System.err.println("Impossibile recuperare le colonne 'uid_ex', 'cf_ex', 'email_ex' dalla query di 'register()'!");
                    return null;
                }
            },
            new Object[] {user.userId, user.codiceFiscale, user.email}
        );

        if (result.size() != 1 || result.getFirst() == null) return RegisterResult.UNEXPECTED_ERROR;
        else if (result.getFirst()[0]) return RegisterResult.DUPLICATE_USERID;
        else if (result.getFirst()[1]) return RegisterResult.DUPLICATE_CF;
        else if (result.getFirst()[2]) return RegisterResult.DUPLICATE_EMAIL;


        //
        // Inserimento
        query = """
            INSERT INTO "UtentiRegistrati" (userid, nome, cognome, codice_fiscale, email, password_hash)
                VALUES (?, ?, ?, ?, ?, crypt(?, gen_salt('bf')))
        """;
        if (!DatabaseManager.getInstance().execute(
            query,
            new Object[] {user.userId, user.nome, user.cognome, user.codiceFiscale, user.email, password}
        )) return RegisterResult.UNEXPECTED_ERROR;

        return RegisterResult.OK;
    }

    public static LoginResult login(String userid, String password) {
        //
        // Controllo password
        @Language("PostgreSQL")
        String query = """
            SELECT CASE
                -- 0: L'utente non esiste
                WHEN NOT EXISTS(
                    SELECT 1 FROM "UtentiRegistrati"
                    WHERE userid = ?
                ) THEN 0
        
                -- 1: Se la password non è corretta
                WHEN NOT EXISTS(
                    SELECT 1 FROM "UtentiRegistrati"
                    WHERE userid = ? AND password_hash = crypt(?, password_hash)
                ) THEN 1
        
                -- 2: Se l'utente esiste e la password è corretta
                ELSE 2
            END AS r
        """;

        LinkedList<Integer> result = DatabaseManager.getInstance().executeQuery(
            query,
            rs -> {
                try
                {
                    return rs.getInt("n");
                }
                catch (SQLException e)
                {
                    System.err.println("Impossibile recuperare la colonna 'r' dalla query di 'login()'!");
                    return null;
                }
            },
            new Object[] {userid, userid, password}
        );

        if (result.size() != 1 || result.getFirst() == null) return LoginResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return LoginResult.USER_ID_NOT_FOUND;
        else if (result.getFirst() == 1) return LoginResult.INCORRECT_PASSWORD;
        else if (result.getFirst() == 2) return LoginResult.OK;

        return LoginResult.UNEXPECTED_ERROR;
    }
}
