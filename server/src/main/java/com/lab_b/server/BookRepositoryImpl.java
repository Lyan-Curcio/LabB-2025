package com.lab_b.server;

import com.lab_b.common.BookRepositoryService;
import com.lab_b.common.dto.Book;
import com.lab_b.common.dto.Rating;
import com.lab_b.common.dto.User;
import com.lab_b.common.enums.auth.LoginResult;
import com.lab_b.common.enums.auth.RegisterResult;
import com.lab_b.server.queries.AuthQueries;
import com.lab_b.server.queries.BookQueries;

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
    public List<Book> cercaLibroPerTitolo(String titolo) throws RemoteException {
        return BookQueries.searchByTitle(titolo);
    }

    @Override
    public List<Book> cercaLibroPerAutore(String autore) throws RemoteException {
        return BookQueries.searchByAuthor(autore);
    }

    @Override
    public List<Book> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException {
        return BookQueries.searchByAuthorAndYear(autore, anno);
    }

    @Override
    public boolean inserisciValutazioneLibro(Rating v) throws RemoteException {
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
    public boolean registraLibreria(String userId, String nomeLibreria) throws RemoteException {
        return true;
    }

    @Override
    synchronized public LoginResult login(String userid, String pass) throws RemoteException {
        return AuthQueries.login(userid, pass);
    }

    @Override
    synchronized public RegisterResult registrazione(User user, String password) throws RemoteException {
        return AuthQueries.register(user, password);
    }
}