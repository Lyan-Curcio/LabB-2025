package com.bookrecommender.common;

import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.extended_dto.BookInfo;
import com.bookrecommender.common.dto.User;
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
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public interface BookRepositoryService extends Remote {

    /**
     * Recupera le informazioni di un utente
     *
     * @param userId l'id dell'utente
     * @return un oggetto {@link User} contenente le informazioni dell'utente cercato
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    User getUserInfo(String userId) throws RemoteException;

    /**
     * Recupera le informazioni inerenti a un libro (libro, valutazioni e suggerimenti)
     *
     * @param libroId l'id del libro
     * @return un oggetto {@link BookInfo} contenente le informazioni del libro richiesto
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    BookInfo getBookInfo(int libroId) throws RemoteException;

    /**
     * Cerca i libri nel database che contengono la stringa specificata nel titolo.
     * <p>La ricerca è <em>case-insensitive</em> e considera le sottostringhe.</p>
     *
     * @param titolo la stringa da cercare nel titolo dei libri
     * @return una lista di oggetti {@link Book} che soddisfano i criteri di ricerca
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Book> cercaLibroPerTitolo(String titolo) throws RemoteException;

    /**
     * Cerca i libri scritti dall'autore specificato.
     *
     * @param autore il nome (o parte del nome) dell'autore da cercare
     * @return una lista di oggetti {@link Book} scritti dall'autore richiesto
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Book> cercaLibroPerAutore(String autore) throws RemoteException;

    /**
     * Cerca i libri scritti da un autore specifico pubblicati in un determinato anno.
     *
     * @param autore il nome (o parte del nome) dell'autore
     * @param anno   l'anno di pubblicazione del libro
     * @return una lista di oggetti {@link Book} corrispondenti ai criteri di ricerca
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    List<Book> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException;

    /**
     * Registra un nuovo utente nel sistema.
     * <p>
     * Se la registrazione ha successo, viene restituito anche il servizio per le operazioni autenticate.
     * </p>
     *
     * @param utente   l'oggetto {@link User} contenente i dati anagrafici del nuovo utente
     * @param password la password scelta dall'utente per l'accesso
     * @return un oggetto {@link BRPair} contenente l'esito della registrazione ({@link RegisterResult})
     * e, in caso di successo, il riferimento al servizio autenticato ({@link AuthedBookRepositoryService})
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    BRPair<RegisterResult, AuthedBookRepositoryService> registrazione(User utente, String password) throws RemoteException;

    /**
     * Effettua il login di un utente già registrato.
     *
     * @param userId   l'identificativo dell'utente
     * @param password la password di accesso
     * @return un oggetto {@link BRPair} contenente l'esito del login ({@link LoginResult})
     * e, se le credenziali sono valide, il riferimento al servizio autenticato ({@link AuthedBookRepositoryService})
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    BRPair<LoginResult, AuthedBookRepositoryService> login(String userId, String password) throws RemoteException;
}