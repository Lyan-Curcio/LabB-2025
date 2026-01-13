package com.lab_b.server.queries;

import com.lab_b.common.dto.User;
import com.lab_b.common.enums.auth.LoginResult;
import com.lab_b.common.enums.auth.RegisterResult;
import com.lab_b.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

public class AuthQueries {

    synchronized public static RegisterResult register(User user, String password) {
        //
        // Controllo vincoli
        @Language("PostgreSQL")
        String sql = """
            SELECT
                EXISTS(SELECT 1 FROM utenti WHERE userid = ?) AS uid_ex,
                EXISTS(SELECT 1 FROM utenti WHERE codice_fiscale = ?) AS cf_ex,
                EXISTS(SELECT 1 FROM utenti WHERE email = ?) AS email_ex
        """;

        LinkedList<Boolean[]> result = DatabaseManager.getInstance().executeQuery(
            sql,
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
        sql = """
            INSERT INTO utenti (userid, nome, cognome, codice_fiscale, email, password)
                VALUES (?, ?, ?, ?, ?, crypt(?, gen_salt('bf')))
        """;
        DatabaseManager.getInstance().execute(
            sql,
            new Object[] {user.userId, user.nome, user.cognome, user.codiceFiscale, user.email, password}
        );

        return RegisterResult.OK;
    }

    synchronized public static LoginResult login(String userid, String password) {
        //
        // Controllo password
        @Language("PostgreSQL")
        String query = """
            SELECT COALESCE(
                (SELECT CASE
                    WHEN password = crypt(?, password) THEN 2
                    ELSE 1
                 END
                 FROM utenti
                 WHERE userid = ?
                ),
                0
            ) AS n
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
                    System.err.println("Impossibile recuperare la colonna 'n' dalla query di 'login()'!");
                    return null;
                }
            },
            new Object[] {userid, password}
        );

        if (result.size() != 1 || result.getFirst() == null) return LoginResult.UNEXPECTED_ERROR;
        else if (result.getFirst() == 0) return LoginResult.USER_ID_NOT_FOUND;
        else if (result.getFirst() == 1) return LoginResult.INCORRECT_PASSWORD;
        else if (result.getFirst() == 2) return LoginResult.OK;

        return LoginResult.UNEXPECTED_ERROR;
    }
}
