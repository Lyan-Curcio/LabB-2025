package com.bookrecommender.server;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.BookRepositoryService;
import com.bookrecommender.common.BRPair;
import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.dto.User;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;
import com.bookrecommender.server.queries.AuthQueries;
import com.bookrecommender.server.queries.BookQueries;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Implementazione del servizio RMI principale per l'accesso non autenticato.
 * <p>
 * Questa classe estende <code>UnicastRemoteObject</code> per permettere l'invocazione remota.
 * Funge da punto di ingresso, gestendo le ricerche di libri e le operazioni di autenticazione.
 * In caso di login/registrazione con successo, agisce come Factory restituendo un riferimento
 * a un oggetto <code>AuthedBookRepositoryImpl</code> specifico per la sessione.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class BookRepositoryImpl extends UnicastRemoteObject implements BookRepositoryService {

    /**
     * Costruttore protetto che esporta l'oggetto per la ricezione di chiamate RMI.
     * @throws RemoteException in caso di errore di esportazione
     */
    protected BookRepositoryImpl() throws RemoteException {
        super();
    }

    //
    // Ricerca libri
    //

    /** {@inheritDoc} */
    @Override
    public List<Book> cercaLibroPerTitolo(String titolo) throws RemoteException {
        return BookQueries.searchByTitle(titolo);
    }

    /** {@inheritDoc} */
    @Override
    public List<Book> cercaLibroPerAutore(String autore) throws RemoteException {
        return BookQueries.searchByAuthor(autore);
    }

    /** {@inheritDoc} */
    @Override
    public List<Book> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException {
        return BookQueries.searchByAuthorAndYear(autore, anno);
    }

    //
    // Autenticazione
    //

    /** {@inheritDoc} */
    @Override
    public BRPair<RegisterResult, AuthedBookRepositoryService> registrazione(User user, String password) throws RemoteException {
        RegisterResult result = AuthQueries.register(user, password);
        return new BRPair<>(
                result,
                result == RegisterResult.OK
                        ? new AuthedBookRepositoryImpl(user.userId)
                        : null
        );
    }

    /** {@inheritDoc} */
    @Override
    public BRPair<LoginResult, AuthedBookRepositoryService> login(String userid, String pass) throws RemoteException {
        LoginResult result = AuthQueries.login(userid, pass);
        return new BRPair<>(
                result,
                result == LoginResult.OK
                        ? new AuthedBookRepositoryImpl(userid)
                        : null
        );
    }
}