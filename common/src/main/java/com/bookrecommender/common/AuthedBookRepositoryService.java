package com.bookrecommender.common;

import com.bookrecommender.common.dto.*;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import com.bookrecommender.common.enums.rating.DeleteRatingResult;
import com.bookrecommender.common.enums.suggestion.AddSuggestionResult;
import com.bookrecommender.common.enums.suggestion.RemoveSuggestionResult;
import com.bookrecommender.common.extended_dto.SuggestionWithBooks;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * Interfaccia remota che espone le funzionalità riservate agli utenti registrati e autenticati.
 * <p>
 * Questa interfaccia estende <code>Remote</code> e permette la gestione delle librerie personali,
 * l'inserimento di valutazioni e la gestione dei suggerimenti tra libri.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public interface AuthedBookRepositoryService extends Remote {

    /**
     * Termina la sessione dell'utente corrente.
     *
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    void logout() throws RemoteException;

    // Gestione Librerie

    /**
     * Cerca le librerie nel sistema che contengono la stringa specificata nel nome.
     *
     * @param nomeLibreria il nome (o parte del nome) da cercare tra i nomi delle librerie
     * @return una lista di oggetti {@link Library} che soddisfano i criteri di ricerca
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    LinkedList<Library> cercaLibreriePerNome(String nomeLibreria) throws RemoteException;

    /**
     * Cerca tutte le librerie appartenenti a un utente con l'id contenente la striga specificata.
     *
     * @param userId l'identificativo (o parte dell'identificativo) dell'utente proprietario delle librerie
     * @return una lista di oggetti {@link Library} appartenenti all'utente specificato
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    LinkedList<Library> cercaLibreriePerUtente(String userId) throws RemoteException;

    /**
     * Recupera le librerie create dall'utente loggato.
     *
     * @return una lista di oggetti {@link Library} appartenenti all'utente loggato
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    LinkedList<Library> getMyLibrerie() throws RemoteException;

    /**
     * Recupera i libri contenuti della libreria specificata.
     *
     * @param libraryId l'id della libreria da cui prendere i libri
     * @return una lista di oggetti {@link Book} contenuti nella libreria
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    LinkedList<Book> getLibriFromLibreria(int libraryId) throws RemoteException;

    /**
     * Crea una nuova libreria associata all'utente.
     *
     * @param nomeLibreria il nome da assegnare alla nuova libreria
     * @return un valore dell'enum {@link CreateLibResult} che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    CreateLibResult creaLibreria(String nomeLibreria) throws RemoteException;

    /**
     * Elimina una libreria dell'utente.
     *
     * @param libreriaId l'identificativo univoco della libreria da eliminare
     * @return un valore dell'enum {@link DeleteLibResult} che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    DeleteLibResult eliminaLibreria(int libreriaId) throws RemoteException;

    /**
     * Aggiunge un libro esistente a una specifica libreria dell'utente.
     *
     * @param libreriaId l'identificativo della libreria di destinazione
     * @param libroId    l'identificativo del libro da aggiungere
     * @return un valore dell'enum {@link AddBookToLibResult} che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    AddBookToLibResult aggiungiLibroALibreria(int libreriaId, int libroId) throws RemoteException;

    /**
     * Rimuove un libro da una specifica libreria dell'utente.
     *
     * @param libreriaId l'identificativo della libreria da cui rimuovere il libro
     * @param libroId    l'identificativo del libro da rimuovere
     * @return un valore dell'enum {@link RemoveBookFromLibResult} che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    RemoveBookFromLibResult rimuoviLibroDaLibreria(int libreriaId, int libroId) throws RemoteException;

    // Valutazioni

    /**
     * Recupera la valutazione che l'utente loggato ha rilasciato per un determinato libro.
     *
     * @param libroId l'identificativo del libro
     * @return l'oggetto {@link Rating} se esiste, altrimenti null o un oggetto vuoto a seconda dell'implementazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    Rating getMyValutazione(int libroId) throws RemoteException;

    /**
     * Inserisce una nuova valutazione (voto e/o commento) per un libro.
     * <p>
     * Richiede un oggetto {@link Rating} compilato con i dati necessari (punteggio, commento, id libro).
     * </p>
     *
     * @param valutazione l'oggetto DTO contenente i dettagli della valutazione
     * @return un valore dell'enum {@link CreateRatingResult} che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    CreateRatingResult inserisciValutazioneLibro(Rating valutazione) throws RemoteException;

    /**
     * Rimuove una valutazione precedentemente inserita dall'utente.
     *
     * @param libroId l'identificativo del libro da cui eliminare la valutazione
     * @return un valore dell'enum <code>DeleteRatingResult</code> che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    DeleteRatingResult rimuoviValutazioneLibro(int libroId) throws RemoteException;

    // Suggerimenti

    /**
     * Recupera i suggerimenti che l'utente loggato ha creato per un determinato libro sorgente.
     *
     * @param libroId l'identificativo del libro sorgente
     * @return una lista di {@link SuggestionWithBooks} creati dall'utente per quel libro
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    LinkedList<SuggestionWithBooks> getMySuggerimenti(int libroId) throws RemoteException;

    /**
     * Inserisce un suggerimento tra due libri.
     * <p>
     * Collega un "libro sorgente" a un "libro consigliato", indicando che chi legge il primo
     * potrebbe apprezzare il secondo.
     * </p>
     *
     * @param libroSorgenteId    l'identificativo del libro di partenza
     * @param libroConsigliatoId l'identificativo del libro suggerito
     * @return un valore dell'enum {@link AddSuggestionResult} che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    AddSuggestionResult inserisciSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException;

    /**
     * Rimuove un suggerimento tra due libri.
     *
     * @param libroSorgenteId    l'identificativo del libro di partenza
     * @param libroConsigliatoId l'identificativo del libro suggerito da rimuovere
     * @return un valore dell'enum {@link RemoveSuggestionResult} che indica l'esito dell'operazione
     * @throws RemoteException se si verifica un errore di comunicazione RMI
     */
    RemoveSuggestionResult rimuoviSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException;
}