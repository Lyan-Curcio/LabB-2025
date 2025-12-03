package main.java.server;

import common.*;
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
        
        try (PreparedStatement pstmt = DatabaseManager.getInstance().getConnection().prepareStatement(query)) {
            pstmt.setString(1, "%" + titolo + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Mapping ResultSet -> Oggetto Book
                // risultati.add(new Book(...));
            }
        } catch (SQLException e) {
            throw new RemoteException("Errore Database", e);
        }
        return risultati;
    }

    @Override
    public boolean inserisciValutazioneLibro(Rating v) throws RemoteException {
        // Implementazione query INSERT INTO ValutazioniLibri ...
        // Calcolo automatico della media arrotondata prima dell'inserimento
        int votoFinale = v.getVotoFinale();
        // ... Logica SQL
        return true;
    }
    
    // ... Implementazione degli altri metodi dell'interfaccia ...
}