package com.bookrecommender.server;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.dto.ValutazioniLibri;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.server.queries.LibraryQueries;

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
    public boolean inserisciValutazioneLibro(ValutazioniLibri v) throws RemoteException {
        return true;
    }

    @Override
    public boolean rimuoviValutazioneLibro(int valutazioneId) throws RemoteException {
        return true;
    }

    //
    // Suggerimenti
    @Override
    public boolean inserisciSuggerimentoLibro(int bookId, int consiglioId) throws RemoteException {
        return true;
    }

    @Override
    public boolean rimuoviSuggerimentoLibro(int bookId, int consiglioId) throws RemoteException {
        return true;
    }
}