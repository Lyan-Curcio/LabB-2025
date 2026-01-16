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

    private final Connection pgsqlConn;

    public BookRepositoryImpl() throws RemoteException {
        super();
        pgsqlConn = DatabaseManager.getInstance().getPgsqlConn();
    }


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
    public void logout(String userid) throws RemoteException {

    }

    @Override
    public boolean inserisciValutazioneLibro(ValutazioniLibri v) throws RemoteException {
        return true; 
    }

    @Override
    public boolean inserisciSuggerimentoLibro(String userId, int bookId, List<Integer> ratings) throws RemoteException {
        return true;
    }

    @Override
    public boolean aggiungiLibroALibreria(String userId, int libreriaId, int bookId) throws RemoteException {
        return true;
    }

    @Override
    public boolean creaLibreria(String userId, String nomeLibreria) throws RemoteException {
        return true;
    }
    /** Rimuove un libro a una libreria specifica */
    @Override
    public boolean rimuoviLibroDaLibreria(String userId, int libreriaId, int libroId) throws RemoteException {
        return true;
    }

    /** Elimina una libreria dell'utente */
    @Override
    public boolean eliminaLibreria(String userId, String nomeLibreria) throws RemoteException {
        return true;
    }

    @Override
    synchronized public LoginResult login(String userid, String pass) throws RemoteException {
        return AuthQueries.login(userid, pass);
    }

    @Override
    synchronized public RegisterResult registrazione(UtentiRegistrati user, String password) throws RemoteException {
        return AuthQueries.register(user, password);
    }
}