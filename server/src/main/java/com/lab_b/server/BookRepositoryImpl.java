package com.lab_b.server;

import com.lab_b.common.BookRepositoryService;
import com.lab_b.common.Book;
import com.lab_b.common.Rating;
import com.lab_b.common.User;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl extends UnicastRemoteObject implements BookRepositoryService {

    public BookRepositoryImpl() throws RemoteException {
        super();
    }

    @Override
    public List<Book> cercaLibroPerTitolo(String titolo) throws RemoteException {
        List<Book> risultati = new ArrayList<>();
        String query = "SELECT * FROM Libri WHERE LOWER(titolo) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + titolo + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // TODO: Mapping
            }
        } catch (SQLException e) {
            System.err.println("Errore SQL: " + e.getMessage());
            throw new RemoteException("Errore Database", e);
        }
        return risultati;
    }

    // --- NUOVO METODO AGGIUNTO (cercaLibroPerAutore) ---
    @Override
    public List<Book> cercaLibroPerAutore(String autore) throws RemoteException {
        List<Book> risultati = new ArrayList<>();
        System.out.println("Ricerca per solo autore: " + autore);
        // TODO: Implementare query SELECT * FROM Libri WHERE autore LIKE ?
        return risultati;
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