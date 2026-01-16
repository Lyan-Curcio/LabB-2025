package com.bookrecommender.server;

import com.bookrecommender.common.BookRepositoryService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    static void main() {
        // 1. Inizializza DB
        System.out.println("Connessione al Database...");
        DatabaseManager.getInstance();

        try {
            // 2. Crea istanza del servizio
            BookRepositoryService service = new BookRepositoryImpl();

            // 3. Pubblica il servizio su RMI Registry (default port 1099)
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BookRecommenderService", service);
        } catch (Exception e) {
            System.err.println("Errore durante il recupero dell'oggetto 'BookRecommenderService' dal registry!");
            System.exit(1);
        }

        System.out.println("Server BookRecommender pronto e in attesa su porta 1099.");
    }
}