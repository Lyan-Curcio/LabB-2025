package com.bookrecommender.server;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.BookRepositoryService;
import com.bookrecommender.common.Pair;
import com.bookrecommender.common.dto.Libri;
import com.bookrecommender.common.dto.UtentiRegistrati;
import com.bookrecommender.common.enums.auth.LoginResult;
import com.bookrecommender.common.enums.auth.RegisterResult;
import com.bookrecommender.server.queries.AuthQueries;
import com.bookrecommender.server.queries.BookQueries;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class BookRepositoryImpl extends UnicastRemoteObject implements BookRepositoryService {

    protected BookRepositoryImpl() throws RemoteException {
        super();
    }

    //
    // Ricerca libri
    @Override
    public List<Libri> cercaLibroPerTitolo(String titolo) throws RemoteException {
        return BookQueries.searchByTitle(titolo);
    }

    @Override
    public List<Libri> cercaLibroPerAutore(String autore) throws RemoteException {
        return BookQueries.searchByAuthor(autore);
    }

    @Override
    public List<Libri> cercaLibroPerAutoreEAnno(String autore, int anno) throws RemoteException {
        return BookQueries.searchByAuthorAndYear(autore, anno);
    }

    //
    // Autenticazione
    @Override
    public Pair<RegisterResult, AuthedBookRepositoryService> registrazione(UtentiRegistrati user, String password) throws RemoteException {
        RegisterResult result = AuthQueries.register(user, password);
        return new Pair<>(
            result,
            result == RegisterResult.OK
                ? new AuthedBookRepositoryImpl(user.userId)
                : null
        );
    }

    @Override
    public Pair<LoginResult, AuthedBookRepositoryService> login(String userid, String pass) throws RemoteException {
        LoginResult result = AuthQueries.login(userid, pass);
        return new Pair<>(
            result,
            result == LoginResult.OK
                ? new AuthedBookRepositoryImpl(userid)
                : null
        );
    }
}
