package com.bookrecommender.common;

import com.bookrecommender.common.dto.Libri;
import com.bookrecommender.common.dto.UtentiRegistrati;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaccia remota che definisce le funzionalità ad accesso libero del sistema (senza login).
 * <p>
 * Questa interfaccia estende <code>Remote</code> e permette ai client di ricercare libri
 * e di effettuare le operazioni di autenticazione (login e registrazione).
 * I metodi restituiscono un riferimento a <code>AuthedBookRepositoryService</code> in caso di successo.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public interface BookRepositoryService extends Remote {

    /**
     * Cerca i libri nel database che contengono la stringa specificata nel titolo.
     * <p>La ricerca è <em>case-insensitive</em> e considera le sottostringhe.</p>
     *
     * @param titolo la stringa (o parte di essa) da cercare nel titolo dei libri
     * @return una lista di oggetti <code>Libri</code> che soddisfano i criteri di ricerca
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Libri> cercaLibroPerTitolo(String titolo) throws RemoteException;

    /**
     * Cerca i libri scritti dall'autore specificato.
     *
     * @param autore il nome (o parte del nome) dell'autore da cercare
     * @return una lista di oggetti <code>Libri</code> scritti dall'autore richiesto
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Libri> cercaLibroPerAutore(String autore) throws RemoteException;

    /**
     * Cerca i libri scritti da un autore specifico in un determinato anno di pubblicazione.
     *
     * @param autore il nome dell'autore
     * @param anno   l'anno di pubblicazione del libro
     * @return una lista di oggetti <code>Libri</code> corrispondenti ai criteri
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Libri> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException;

    /**
     * Registra un nuovo utente nel sistema.
     * <p>
     * Se la registrazione ha successo, viene restituito anche il servizio per le operazioni autenticate.
     * </p>
     *
     * @param utente   l'oggetto <code>UtentiRegistrati</code> contenente i dati anagrafici del nuovo utente
     * @param password la password scelta dall'utente per l'accesso
     * @return un oggetto <code>Pair</code> contenente l'esito della registrazione (<code>RegisterResult</code>)
     * e, in caso di successo, il riferimento al servizio autenticato
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    Pair<RegisterResult, AuthedBookRepositoryService> registrazione(UtentiRegistrati utente, String password) throws RemoteException;

    /**
     * Effettua il login di un utente già registrato.
     *
     * @param userId   l'identificativo dell'utente (es. email o username)
     * @param password la password di accesso
     * @return un oggetto <code>Pair</code> contenente l'esito del login (<code>LoginResult</code>)
     * e, se le credenziali sono valide, il riferimento al servizio autenticato (<code>AuthedBookRepositoryService</code>)
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    Pair<LoginResult, AuthedBookRepositoryService> login(String userId, String password) throws RemoteException;
}