package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.User;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.util.NoSuchElementException;

/**
 * Classe di utility contenente le query SQL per il recupero delle informazioni degli utenti.
 * <p>
 * Questa classe fornisce metodi statici per interrogare la tabella "UtentiRegistrati"
 * e mappare i risultati sugli oggetti DTO {@link User}.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class UserQueries {

    /**
     * Recupera le informazioni anagrafiche complete di un utente registrato dato il suo UserID.
     * <p>
     * Esegue una query di selezione sulla tabella "UtentiRegistrati" filtrando per l'identificativo specificato.
     * </p>
     *
     * @param userId l'identificativo univoco (username) dell'utente da cercare
     * @return un oggetto {@link User} popolato con i dati trovati nel database o null se l'utente non viene trovato
     */
    public synchronized static User getUserInfo(String userId) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"UtentiRegistrati\" WHERE userid = ?";

        try {
            return DatabaseManager.getInstance().executeQuery(
                    query,
                    User::new,
                    new Object[] {userId}
            ).getFirst();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }
}