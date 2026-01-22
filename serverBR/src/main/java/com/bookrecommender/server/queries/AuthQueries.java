package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.User;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Classe di utility contenente le query SQL per la gestione dell'autenticazione.
 * <p>
 * Questa classe offre metodi statici per la registrazione e il login degli utenti.
 * La sicurezza delle password è garantita delegando l'hashing direttamente al DBMS PostgreSQL
 * tramite l'estensione <code>pgcrypto</code> (funzioni <code>crypt</code> e <code>gen_salt</code> con algoritmo Blowfish).
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class AuthQueries {

    /**
     * Gestisce la registrazione di un nuovo utente nel database.
     * <p>
     * Il metodo esegue due operazioni distinte:
     * </p>
     * <ol>
     * <li>Verifica preliminare dell'unicità di UserID, Codice Fiscale ed Email.</li>
     * <li>Se i vincoli sono rispettati, procede all'inserimento (INSERT) calcolando l'hash della password.</li>
     * </ol>
     *
     * @param user     l'oggetto DTO contenente i dati anagrafici dell'utente
     * @param password la password in chiaro scelta dall'utente (verrà salvata solo hashata)
     * @return un valore dell'enum <code>RegisterResult</code> che indica il successo o il tipo di errore (es. duplicato)
     */
    public synchronized static RegisterResult register(User user, String password) {
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
        // Utilizza crypt() di PostgreSQL per l'hashing sicuro
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

    /**
     * Verifica le credenziali di un utente per effettuare il login.
     * <p>
     * Utilizza una query SQL condizionale (CASE) per distinguere in un unico passaggio se:
     * </p>
     * <ul>
     * <li>L'utente non esiste (Codice 0)</li>
     * <li>L'utente esiste ma la password è errata (Codice 1)</li>
     * <li>Le credenziali sono valide (Codice 2)</li>
     * </ul>
     * <p>
     * Questo approccio migliora l'efficienza e permette di restituire messaggi di errore precisi al client.
     * </p>
     *
     * @param userid   l'identificativo dell'utente
     * @param password la password in chiaro da verificare contro l'hash nel DB
     * @return un valore dell'enum <code>LoginResult</code> che indica l'esito dell'autenticazione
     */
    public synchronized static LoginResult login(String userid, String password) {
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
                        return rs.getInt("r");
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