package main.java.com.lab_b.client.controller;

import common.BookRepositoryService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientController {
    private BookRepositoryService server;
    private String currentUserSessionID; // Per tracciare l'utente loggato

    public ClientController() {
        try {
            // Connessione al server RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (BookRepositoryService) registry.lookup("BookRecommenderService");
        } catch (Exception e) {
            System.err.println("Impossibile connettersi al server: " + e.getMessage());
        }
    }

    public void eseguiLogin(String user, String pass) {
        try {
            boolean success = server.login(user, pass);
            if (success) {
                this.currentUserSessionID = user;
                System.out.println("Login effettuato!");
                // Apri Dashboard utente
            } else {
                System.out.println("Credenziali errate.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Metodi wrapper per chiamare le funzioni del server dalla GUI
}