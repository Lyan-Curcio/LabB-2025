package com.bookrecommender.server;

import com.bookrecommender.common.AuthedBookRepositoryService;
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
import com.bookrecommender.server.queries.LibraryQueries;
import com.bookrecommender.server.queries.RatingQueries;
import com.bookrecommender.server.queries.SuggestionQueries;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * Implementazione del servizio RMI riservato agli utenti autenticati.
 * <p>
 * Implementa il pattern Session: viene creata una nuova istanza per ogni utente loggato,
 * mantenendo lo stato della sessione (userId) per garantire che ogni operazione sia
 * eseguita con i permessi corretti.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class AuthedBookRepositoryImpl extends UnicastRemoteObject implements AuthedBookRepositoryService {

    /** L'identificativo univoco dell'utente associato a questa sessione. */
    final String loggedUserId;

    /**
     * Costruisce una nuova sessione per l'utente specificato.
     *
     * @param loggedUserId l'ID dell'utente autenticato
     * @throws RemoteException in caso di errore RMI
     */
    public AuthedBookRepositoryImpl(String loggedUserId) throws RemoteException {
        super();
        this.loggedUserId = loggedUserId;
    }

    //
    // Autenticazione
    //

    /** {@inheritDoc} */
    @Override
    public void logout() throws RemoteException {
        // Fa in modo che l'oggetto remoto non sia più usabile dal client
        UnicastRemoteObject.unexportObject(this, true);
    }

    //
    // Librerie
    //

    /** {@inheritDoc} */
    public LinkedList<Library> getMyLibrerie() throws RemoteException {
        return LibraryQueries.getLibrerieFrom(loggedUserId);
    }

    /** {@inheritDoc} */
    public LinkedList<Book> getLibriFromLibreria(int libraryId) throws RemoteException {
        return LibraryQueries.getLibriFromLibreria(libraryId);
    }

    /** {@inheritDoc} */
    @Override
    public CreateLibResult creaLibreria(String nomeLibreria) throws RemoteException {
        return LibraryQueries.createLibrary(loggedUserId, nomeLibreria);
    }

    /** {@inheritDoc} */
    @Override
    public DeleteLibResult eliminaLibreria(int libreriaId) throws RemoteException {
        return LibraryQueries.deleteLibrary(loggedUserId, libreriaId);
    }

    /** {@inheritDoc} */
    @Override
    public AddBookToLibResult aggiungiLibroALibreria(int libreriaId, int libroId) throws RemoteException {
        return LibraryQueries.addBookToLibrary(loggedUserId, libreriaId, libroId);
    }

    /** {@inheritDoc} */
    @Override
    public RemoveBookFromLibResult rimuoviLibroDaLibreria(int libreriaId, int libroId) throws RemoteException {
        return LibraryQueries.removeBookFromLibrary(loggedUserId, libreriaId, libroId);
    }

    //
    // Valutazioni
    //

    /** {@inheritDoc} */
    @Override
    public Rating getMyValutazione(int libroId) throws RemoteException {
        return RatingQueries.getRatingFrom(loggedUserId, libroId);
    }

    /** {@inheritDoc} */
    @Override
    public CreateRatingResult inserisciValutazioneLibro(Rating v) throws RemoteException {
        return RatingQueries.createRating(loggedUserId, v);
    }

    /** {@inheritDoc} */
    @Override
    public DeleteRatingResult rimuoviValutazioneLibro(int libroId) throws RemoteException {
        return RatingQueries.deleteRating(loggedUserId, libroId);
    }

    //
    // Suggerimenti
    //

    /** {@inheritDoc} */
    @Override
    public LinkedList<SuggestionWithBooks> getMySuggerimenti(int libroId) throws RemoteException {
        return Utils.suggWithBooksFromSugg(
            SuggestionQueries.getSuggestionsFrom(loggedUserId, libroId)
        );
    }

    /** {@inheritDoc} */
    @Override
    public AddSuggestionResult inserisciSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException {
        return SuggestionQueries.createSuggestion(loggedUserId, libroSorgenteId, libroConsigliatoId);
    }

    /** {@inheritDoc} */
    @Override
    public RemoveSuggestionResult rimuoviSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException {
        return SuggestionQueries.deleteSuggestion(loggedUserId, libroSorgenteId, libroConsigliatoId);
    }
}