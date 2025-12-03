package main.java.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            // 1. Inizializza DB
            System.out.println("Connessione al Database...");
            DatabaseManager.getInstance();

            // 2. Crea istanza del servizio
            BookRepositoryService service = new BookRepositoryImpl();

            // 3. Pubblica il servizio su RMI Registry (default port 1099)
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BookRecommenderService", service);

            System.out.println("Server BookRecommender pronto e in attesa su porta 1099.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}