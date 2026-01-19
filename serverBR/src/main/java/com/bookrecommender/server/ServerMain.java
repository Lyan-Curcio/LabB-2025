package com.bookrecommender.server;

import com.bookrecommender.common.BookRepositoryService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe principale (Entry Point) dell'applicazione server BookRecommender.
 * <p>
 * Questa classe ha il compito di configurare e avviare l'infrastruttura lato server:
 * </p>
 * <ol>
 * <li>Inizializza la connessione al database PostgreSQL tramite {@link DatabaseManager}.</li>
 * <li>Crea l'istanza dell'implementazione del servizio RMI ({@link BookRepositoryImpl}).</li>
 * <li>Avvia il registro RMI locale sulla porta predefinita (1099).</li>
 * <li>Effettua il <em>rebind</em> del servizio con il nome "BookRecommenderService" per renderlo visibile ai client.</li>
 * </ol>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class ServerMain {

    /**
     * Costruttore privato per nascondere quello implicito, trattandosi di una classe con soli metodi statici.
     */
    private ServerMain() {}

    /**
     * Metodo di avvio dell'applicazione server.
     * <p>
     * Gestisce le eccezioni critiche durante l'avvio (es. porta RMI occupata o problemi di rete).
     * In caso di errore durante la pubblicazione del servizio, l'applicazione termina con codice di uscita 1.
     * </p>
     *
     * @param args argomenti da riga di comando (attualmente non utilizzati)
     */
    public static void main(String[] args) {
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
            e.printStackTrace(); // Utile per il debug per vedere l'errore esatto
            System.exit(1);
        }

        System.out.println("Server BookRecommender pronto e in attesa su porta 1099.");
    }
}