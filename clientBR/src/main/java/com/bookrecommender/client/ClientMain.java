package com.bookrecommender.client;

/**
 * La classe {@code ClientMain} funge da punto di ingresso per l'applicazione.
 * <p>
 *   Poiché JavaFX richiede che il metodo {@code main} richiami la classe che estende
 *   {@link javafx.application.Application}, questa classe si limita a delegare
 *   l'avvio a {@link ClientApplication}.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class ClientMain {

    /**
     * Punto di ingresso dell'applicazione.
     * <p>
     *   Avvia l'applicazione JavaFX delegando l'esecuzione alla classe
     *   {@link ClientApplication}, che estende {@link javafx.application.Application}.
     * </p>
     *
     * @param args Argomenti della linea di comando.
     */
    public static void main(String[] args) {
        // Avvia l'applicazione JavaFX tramite la classe ClientApplication
        ClientApplication.main(args);
    }
}
