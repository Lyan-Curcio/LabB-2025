package com.lab_b.client.controller; // ERRORE: c'era "main.java."

import com.lab_b.common.BookRepositoryService; // ERRORE: c'era "main.java."
import com.lab_b.common.Book; 
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientController {
    private BookRepositoryService server;
    private String currentUserSessionID; 

    public ClientController() {
        try {
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
            } else {
                System.out.println("Credenziali errate.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}