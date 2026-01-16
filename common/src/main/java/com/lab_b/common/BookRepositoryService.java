package com.lab_b.common;

import com.lab_b.common.dto.Libri;
import com.lab_b.common.dto.ValutazioniLibri;
import com.lab_b.common.dto.UtentiRegistrati;
import com.lab_b.common.enums.auth.LoginResult;
import com.lab_b.common.enums.auth.RegisterResult;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BookRepositoryService extends Remote {

    //
    // Funzionalità Accesso Libero
    //

    //
    // Ricerca libri
    /** Ricerca per titolo (case insensitive, sottostringhe) */
    List<Libri> cercaLibroPerTitolo(String titolo) throws RemoteException;

    /** Ricerca per autore */
    List<Libri> cercaLibroPerAutore(String autore) throws RemoteException;

    /** Ricerca per autore e anno */
    List<Libri> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException;

    //
    // Autenticazione
    /** Registra un nuovo utente. Ritorna true se successo. */
    RegisterResult registrazione(UtentiRegistrati utente, String password) throws RemoteException;

    /** Login. Ritorna true se credenziali valide. */
    LoginResult login(String userId, String password) throws RemoteException;
    

    //
    // Funzionalità Utenti Registrati
    //

    //
    // Autenticazione
    void logout(String userid) throws RemoteException;

    //
    // Librerie
    /** Crea una nuova libreria per l'utente */
    boolean creaLibreria(String userId, String nomeLibreria) throws RemoteException;
    
    /** Aggiunge un libro a una libreria specifica */
    boolean aggiungiLibroALibreria(String userId, int libreriaId, int libroId) throws RemoteException;

    /** Rimuove un libro a una libreria specifica */
    boolean rimuoviLibroDaLibreria(String userId, int libreriaId, int libroId) throws RemoteException;

    /** Elimina una libreria dell'utente */
    boolean eliminaLibreria(String userId, String nomeLibreria) throws RemoteException;
    
    /** Inserisce valutazione (solo se libro presente in libreria utente) */
    boolean inserisciValutazioneLibro(ValutazioniLibri valutazione) throws RemoteException;
    
    /** Inserisce consigli (max 3 libri) */
    boolean inserisciSuggerimentoLibro(String userId, int libroSorgenteId, List<Integer> libriConsigliatiIds) throws RemoteException;
}