package com.bookrecommender.common;

import com.bookrecommender.common.dto.Libri;
import com.bookrecommender.common.dto.ValutazioniLibri;
import com.bookrecommender.common.dto.UtentiRegistrati;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;

import java.rmi.Remote;
import java.rmi.RemoteException;

//
// Funzionalità Utenti Registrati
//

public interface AuthedBookRepositoryService extends Remote {
    //
    // Autenticazione
    void logout() throws RemoteException;

    //
    // Librerie
    /** Crea una nuova libreria per l'utente */
    CreateLibResult creaLibreria(String nomeLibreria) throws RemoteException;

    /** Elimina una libreria dell'utente */
    DeleteLibResult eliminaLibreria(int libreriaId) throws RemoteException;
    
    /** Aggiunge un libro a una libreria specifica */
    AddBookToLibResult aggiungiLibroALibreria(int libreriaId, int libroId) throws RemoteException;

    /** Rimuove un libro a una libreria specifica */
    RemoveBookFromLibResult rimuoviLibroDaLibreria(int libreriaId, int libroId) throws RemoteException;

    //
    // Valutazioni
    /** Inserisce una valutazione */
    boolean inserisciValutazioneLibro(ValutazioniLibri valutazione) throws RemoteException;

    /** Rimuove una valutazione */
    boolean rimuoviValutazioneLibro(int valutazioneId) throws RemoteException;

    //
    // Suggerimenti
    /** Inserisce un consiglio */
    boolean inserisciSuggerimentoLibro(int libroSorgenteId, int libroConsigliatiId) throws RemoteException;

    /** Rimuove un consiglio */
    boolean rimuoviSuggerimentoLibro(int libroSorgenteId, int libroConsigliatiId) throws RemoteException;
}