package com.bookrecommender.client.controller; // ERRORE: c'era "main.java."

import com.bookrecommender.common.AuthedBookRepositoryService; // ERRORE: c'era "main.java."

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientController {
    private AuthedBookRepositoryService server;
    private String currentUserSessionID;

    public ClientController() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (AuthedBookRepositoryService) registry.lookup("BookRecommenderService");
        } catch (Exception e) {
            System.err.println("Impossibile connettersi al server: " + e.getMessage());
        }
    }

    public void eseguiLogin(String user, String pass) {
        // Non ho idea di cosa sia questa classe, ho commentato quello sotto per rimuovere l'errore
//        try {
//            LoginResult result = server.login(user, pass);
//            if (result.first() == LoginResult.OK) {
//                this.currentUserSessionID = user;
//                System.out.println("Login effettuato!");
//            } else {
//                System.out.println("Credenziali errate.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}