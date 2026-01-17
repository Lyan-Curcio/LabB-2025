package com.bookrecommender.common;

import com.bookrecommender.common.dto.Libri;
import com.bookrecommender.common.dto.UtentiRegistrati;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//
// Funzionalità Accesso Libero
//

public interface BookRepositoryService extends Remote {
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
    Pair<RegisterResult, AuthedBookRepositoryService> registrazione(UtentiRegistrati utente, String password) throws RemoteException;

    /** Login. Ritorna true se credenziali valide. */
    Pair<LoginResult, AuthedBookRepositoryService> login(String userId, String password) throws RemoteException;
}
