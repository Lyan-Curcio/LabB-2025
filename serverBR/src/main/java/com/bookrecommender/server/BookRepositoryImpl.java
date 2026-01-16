package com.bookrecommender.server;

import com.bookrecommender.common.BookRepositoryService;
import com.bookrecommender.common.dto.Libri;
import com.bookrecommender.common.dto.ValutazioniLibri;
import com.bookrecommender.common.dto.UtentiRegistrati;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;
import com.bookrecommender.server.queries.AuthQueries;
import com.bookrecommender.server.queries.BookQueries;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.List;

public class BookRepositoryImpl extends UnicastRemoteObject implements BookRepositoryService {

    public BookRepositoryImpl() throws RemoteException {
        super();
    }

    //
    // Accesso libero
    //

    //
    // Ricerca libri
    @Override
    public List<Libri> cercaLibroPerTitolo(String titolo) throws RemoteException {
        return BookQueries.searchByTitle(titolo);
    }

    @Override
    public List<Libri> cercaLibroPerAutore(String autore) throws RemoteException {
        return BookQueries.searchByAuthor(autore);
    }

    @Override
    public List<Libri> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException {
        return BookQueries.searchByAuthorAndYear(autore, anno);
    }

    @Override
    public LoginResult login(String userid, String pass) throws RemoteException {
        return AuthQueries.login(userid, pass);
    }

    //
    // Autenticazione
    @Override
    public RegisterResult registrazione(UtentiRegistrati user, String password) throws RemoteException {
        return AuthQueries.register(user, password);
    }


    //
    // Funzionalità Utenti Registrati
    //

    //
    // Autenticazione
    @Override
    public void logout(String userid) throws RemoteException {

    }

    //
    // Librerie
    @Override
    public boolean creaLibreria(String userId, String nomeLibreria) throws RemoteException {
        return true;
    }

    @Override
    public boolean aggiungiLibroALibreria(String userId, int libreriaId, int bookId) throws RemoteException {
        return true;
    }

    @Override
    public boolean rimuoviLibroDaLibreria(String userId, int libreriaId, int libroId) throws RemoteException {
        return true;
    }

    @Override
    public boolean eliminaLibreria(String userId, String nomeLibreria) throws RemoteException {
        return true;
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
    public boolean inserisciSuggerimentoLibro(String userId, int bookId, int consiglioId) throws RemoteException {
        return true;
    }

    @Override
    public boolean rimuoviSuggerimentoLibro(String userId, int bookId, int consiglioId) throws RemoteException {
        return true;
    }
}