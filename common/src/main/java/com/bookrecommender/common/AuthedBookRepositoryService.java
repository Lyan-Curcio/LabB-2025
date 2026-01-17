package com.bookrecommender.common;

import com.bookrecommender.common.dto.Valutazione;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import com.bookrecommender.common.enums.rating.DeleteRatingResult;
import com.bookrecommender.common.enums.suggestion.AddSuggestionResult;
import com.bookrecommender.common.enums.suggestion.RemoveSuggestionResult;

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
    CreateRatingResult inserisciValutazioneLibro(Valutazione valutazione) throws RemoteException;

    /** Rimuove una valutazione */
    DeleteRatingResult rimuoviValutazioneLibro(int valutazioneId) throws RemoteException;

    //
    // Suggerimenti
    /** Inserisce un consiglio */
    AddSuggestionResult inserisciSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException;

    /** Rimuove un consiglio */
    RemoveSuggestionResult rimuoviSuggerimentoLibro(int libroSorgenteId, int libroConsigliatoId) throws RemoteException;
}