package com.lab_b.server;

import com.lab_b.common.BookRepositoryService;
import com.lab_b.common.Book;
import com.lab_b.common.Rating;
import com.lab_b.common.User;
import com.lab_b.server.queries.BookQueries;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl extends UnicastRemoteObject implements BookRepositoryService {

    private final Connection pgsqlConn;

    public BookRepositoryImpl() throws RemoteException {
        super();
        pgsqlConn = DatabaseManager.getInstance().getPgsqlConn();
    }


    @Override
    public List<Book> cercaLibroPerTitolo(String titolo) throws RemoteException, SQLException {
        return BookQueries.searchByTitle(titolo);
    }

    @Override
    public List<Book> cercaLibroPerAutore(String autore) throws RemoteException, SQLException {
        return BookQueries.searchByAuthor(autore);
    }

    @Override
    public List<Book> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException {
        List<Book> risultati = new ArrayList<>();
        System.out.println("Ricerca per autore: " + autore + " e anno: " + anno);
        return risultati;
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
    public boolean login(String user, String pass) throws RemoteException {
        System.out.println("Login chiamato per: " + user);
        return true; 
    }

    @Override
    public boolean registrazione(User user, String password) throws RemoteException {
        System.out.println("Registrazione chiamata per utente: " + user); 
        return true;
    }
}