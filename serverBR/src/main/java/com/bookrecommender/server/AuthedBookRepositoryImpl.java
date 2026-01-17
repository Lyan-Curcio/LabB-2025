package com.bookrecommender.server;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.dto.Valutazione;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import com.bookrecommender.common.enums.rating.DeleteRatingResult;
import com.bookrecommender.common.enums.suggestion.AddSuggestionResult;
import com.bookrecommender.common.enums.suggestion.RemoveSuggestionResult;
import com.bookrecommender.server.queries.LibraryQueries;
import com.bookrecommender.server.queries.RatingQueries;
import com.bookrecommender.server.queries.SuggestionQueries;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class AuthedBookRepositoryImpl extends UnicastRemoteObject implements AuthedBookRepositoryService {

    final String loggedUserId;

    public AuthedBookRepositoryImpl(String loggedUserId) throws RemoteException {
        super();
        this.loggedUserId = loggedUserId;
    }

    //
    // Autenticazione
    @Override
    public void logout() throws RemoteException {
        // Fa in modo che l'oggetto remoto non sia più usabile dal client
        UnicastRemoteObject.unexportObject(this, true);
    }

    //
    // Librerie
    @Override
    public CreateLibResult creaLibreria(String nomeLibreria) throws RemoteException {
        return LibraryQueries.createLibrary(loggedUserId, nomeLibreria);
    }

    @Override
    public DeleteLibResult eliminaLibreria(int libreriaId) throws RemoteException {
        return LibraryQueries.deleteLibrary(loggedUserId, libreriaId);
    }

    @Override
    public AddBookToLibResult aggiungiLibroALibreria(int libreriaId, int libroId) throws RemoteException {
        return LibraryQueries.addBookToLibrary(loggedUserId, libreriaId, libroId);
    }

    @Override
    public RemoveBookFromLibResult rimuoviLibroDaLibreria(int libreriaId, int libroId) throws RemoteException {
        return LibraryQueries.removeBookFromLibrary(loggedUserId, libreriaId, libroId);
    }

    //
    // Valutazioni
    @Override
    public CreateRatingResult inserisciValutazioneLibro(Valutazione v) throws RemoteException {
        return RatingQueries.createRating(loggedUserId, v);
    }

    @Override
    public DeleteRatingResult rimuoviValutazioneLibro(int valutazioneId) throws RemoteException {
        return RatingQueries.deleteRating(loggedUserId, valutazioneId);
    }

    //
    // Suggerimenti
    @Override
    public AddSuggestionResult inserisciSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException {
        return SuggestionQueries.createRating(loggedUserId, libroSorgenteId, libroConsigliatoId);
    }

    @Override
    public RemoveSuggestionResult rimuoviSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException {
        return SuggestionQueries.deleteRating(loggedUserId, libroSorgenteId, libroConsigliatoId);
    }
}