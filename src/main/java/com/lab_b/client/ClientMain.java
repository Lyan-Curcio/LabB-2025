package main.java.com.lab_b.client;

import javax.swing.*; // O JavaFX

public class ClientMain {
    public static void main(String[] args) {
        // Avvio Interfaccia Grafica
        SwingUtilities.invokeLater(() -> {
            ClientController controller = new ClientController();
            // Esempio: Apertura finestra Login o Ricerca Ospite
            new MainWindow(controller).setVisible(true);
        });
    }
}