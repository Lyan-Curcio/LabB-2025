package com.lab_b.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BookRepositoryService extends Remote {

    // --- Funzionalità Accesso Libero [cite: 86] ---
    
    /** Ricerca per titolo (case insensitive, sottostringhe) */
    List<Book> cercaLibroPerTitolo(String titolo) throws RemoteException;

    /** Ricerca per autore */
    List<Book> cercaLibroPerAutore(String autore) throws RemoteException;

    /** Ricerca per autore e anno */
    List<Book> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException;
    

    // --- Funzionalità Utenti Registrati [cite: 106, 122, 135, 148] ---
    
    /** Registra un nuovo utente. Ritorna true se successo. */
    boolean registrazione(User utente, String password) throws RemoteException;

    /** Login. Ritorna true se credenziali valide. */
    boolean login(String userId, String password) throws RemoteException;

    /** Crea una nuova libreria per l'utente */
    boolean registraLibreria(String userId, String nomeLibreria) throws RemoteException;
    
    /** Aggiunge un libro a una libreria specifica */
    boolean aggiungiLibroALibreria(String userId, int libreriaId, int libroId) throws RemoteException;
    
    /** Inserisce valutazione (solo se libro presente in libreria utente) */
    boolean inserisciValutazioneLibro(Rating valutazione) throws RemoteException;
    
    /** Inserisce consigli (max 3 libri) [cite: 37] */
    boolean inserisciSuggerimentoLibro(String userId, int libroSorgenteId, List<Integer> libriConsigliatiIds) throws RemoteException;
}